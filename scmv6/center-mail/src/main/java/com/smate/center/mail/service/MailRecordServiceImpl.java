package com.smate.center.mail.service;

import com.smate.center.mail.cache.MailCacheService;
import com.smate.center.mail.connector.dao.MailContentDao;
import com.smate.center.mail.connector.dao.MailOriginalDataDao;
import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;
import com.smate.center.mail.connector.model.MailInfo;
import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.connector.mongodb.model.MailContent;
import com.smate.center.mail.dao.MailRecordDao;
import com.smate.center.mail.model.MailRecord;
import com.smate.center.mail.model.MailSender;
import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 邮件发送记录 服务实现
 * 
 * @author tsz
 *
 */
@Service("mailRecordService")
@Transactional(rollbackFor = Exception.class)
public class MailRecordServiceImpl implements MailRecordService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  // 邮件调度优先级 规则 ....
  private static final Double LEVEL_A = 0.6;
  private static final Double LEVEL_B = 0.25;
  private static final Double LEVEL_C = 0.1;
  private static final Double LEVEL_D = 0.05;

  @Autowired
  private MailRecordDao mailRecordDao;
  @Autowired
  private MailSenderService mailSenderService;
  @Autowired
  private MailContentDao mailContentDao;
  @Autowired
  private MailOriginalDataDao mailOriginalDataDao;
  @Autowired
  private MailCacheService mailCacheService;

  @Override
  public List<MailRecord> getToBeRecoveryMail(int size, int recoveryTime) {
    return mailRecordDao.getToBeRecovery(size, recoveryTime);
  }

  /**
   * 获取 待分配的数据
   */
  @Override
  public List<MailRecord> getToBeAllocatedMail(int size) {

    return getMailByLevel(size, 0, null);

  }

  /**
   * 按比例获取 数据
   * 
   * @param size
   * @param status
   * @return
   */
  private List<MailRecord> getMailByLevel(int size, int status, String client) {
    int LEVEL_A_SIZE = (int) (size * LEVEL_A);
    int LEVEL_B_SIZE = (int) (size * LEVEL_B);
    int LEVEL_C_SIZE = (int) (size * LEVEL_C);
    int LEVEL_D_SIZE = (int) (size * LEVEL_D);
    List<MailRecord> resutltList = new ArrayList<MailRecord>();

    List<MailRecord> resutltListA = mailRecordDao.getToBeAllocatedMail(LEVEL_A_SIZE, "A", status, client);
    resutltList.addAll(resutltListA);
    if (resutltListA.size() <= LEVEL_A_SIZE) {
      LEVEL_B_SIZE = LEVEL_B_SIZE + (LEVEL_A_SIZE - resutltListA.size());
    }
    List<MailRecord> resutltListB = mailRecordDao.getToBeAllocatedMail(LEVEL_B_SIZE, "B", status, client);
    resutltList.addAll(resutltListB);
    if (resutltListB.size() <= LEVEL_B_SIZE) {
      LEVEL_C_SIZE = LEVEL_C_SIZE + (LEVEL_B_SIZE - resutltListB.size());
    }
    List<MailRecord> resutltListC = mailRecordDao.getToBeAllocatedMail(LEVEL_C_SIZE, "C", status, client);
    resutltList.addAll(resutltListC);
    if (resutltListC.size() <= LEVEL_C_SIZE) {
      LEVEL_D_SIZE = LEVEL_D_SIZE + (LEVEL_C_SIZE - resutltListC.size());
    }
    List<MailRecord> resutltListD = mailRecordDao.getToBeAllocatedMail(LEVEL_D_SIZE, "D", status, client);
    resutltList.addAll(resutltListD);
    return resutltList;
  }

  @Override
  public void updateMailRecord(MailRecord mailRecord) {
    mailRecordDao.saveOrUpdate(mailRecord);
    // 同步更新原始数据表 的冗余字段
    MailOriginalData mailOriginal = mailOriginalDataDao.get(mailRecord.getMailId());
    if (mailOriginal != null) {
      mailOriginal.setUpdateDate(mailRecord.getUpdateDate());
      mailOriginal.setSendStatus(mailRecord.getStatus());
      mailOriginal.setMsg("同步状态");
      mailOriginalDataDao.save(mailOriginal);
    }
  }

  /**
   * 获取待发送的数据
   * 
   * @param size
   * @return
   */
  @Override
  public List<String> getToBeSendMail(int size, String client) {
    List<String> mailInfoList = new ArrayList<String>();
    List<MailRecord> mailRecordList = getMailByLevel(size, 1, client);
    if (CollectionUtils.isNotEmpty(mailRecordList)) {
      for (MailRecord mailRecord : mailRecordList) {
        try {
          // 判断发送账号 是否锁定
          if (!lockSender(mailRecord.getSender())) {
            // 被锁定就继续等待
            this.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_1, "发送账号被锁定，等待重新发送");
            continue;
          }
          MailInfo mailInfo = new MailInfo();
          mailInfo.setMailId(mailRecord.getMailId());
          mailInfo.setReceiver(mailRecord.getReceiver());
          mailInfo.setSubject(mailRecord.getSubject());
          mailInfo.setSenderName(mailRecord.getSenderName());
          MailSender mailSender = mailSenderService.getMailSenderBySender(mailRecord.getSender());
          mailInfo.setAccount(mailSender.getAccount());
          mailInfo.setPassword(mailSender.getPassword());
          mailInfo.setPort(mailSender.getPort());
          mailInfo.setHost(mailSender.getHost());
          MailContent mailContent = mailContentDao.findById(mailRecord.getMailId());
          mailInfo.setContent(mailContent.getContent());
          mailInfoList.add(JacksonUtils.jsonObjectSerializer(mailInfo));
          // 更新邮件状态为正在发送状态 避免 客户端 更新状态出错 导致邮件重复发送的问题
          this.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_10, "");
        } catch (Exception e) {
          logger.error("构造邮件发送信息出错!" + mailRecord.toString(), e);
          this.updateMailRecordStatus(mailRecord.getMailId(), MailSendStatusEnum.STATUS_11, "");
        }
      }
    }
    return mailInfoList;
  }

  private boolean lockSender(String account) {
    if ("true".equals(mailCacheService.getLockSender(account))) {
      return false;
    } else {
      mailCacheService.lockSender(account);
      return true;
    }
  }

  @Override
  public void updateMailRecordStatus(Long mailId, MailSendStatusEnum status, String msg) {

    Date currentDate = new Date();

    MailRecord mailRecord = mailRecordDao.get(mailId);
    mailRecord.setStatus(status.toInt());
    mailRecord.setUpdateDate(currentDate);
    mailRecord.setMsg(msg);
    mailRecordDao.saveOrUpdate(mailRecord);

    // 同步更新原始数据表 的冗余字段
    MailOriginalData mailOriginal = mailOriginalDataDao.get(mailId);
    if (mailOriginal != null) {
      mailOriginal.setUpdateDate(currentDate);
      mailOriginal.setSendStatus(status.toInt());
      mailOriginal.setMsg(msg);
      mailOriginalDataDao.save(mailOriginal);
    }
  }

}
