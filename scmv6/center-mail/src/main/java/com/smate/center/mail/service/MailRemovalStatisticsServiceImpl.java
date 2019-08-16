package com.smate.center.mail.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.dao.MailEverydayStatisticDao;
import com.smate.center.mail.connector.dao.MailOriginalDataDao;
import com.smate.center.mail.connector.model.MailEverydayStatistic;
import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.center.mail.dao.MailOriginalDataLogDao;
import com.smate.center.mail.dao.MailRecordDao;
import com.smate.center.mail.dao.MailRecordLogDao;
import com.smate.center.mail.model.MailOriginalDataLog;
import com.smate.center.mail.model.MailRecord;
import com.smate.center.mail.model.MailRecordLog;

/**
 * 邮件迁移统计服务
 * 
 * @author zzx
 *
 */
@Service("mailRemovalStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class MailRemovalStatisticsServiceImpl implements MailRemovalStatisticsService {
  @Autowired
  private MailOriginalDataDao mailOriginalDataDao;
  @Autowired
  private MailOriginalDataLogDao mailOriginalDataLogDao;
  @Autowired
  private MailRecordDao mailRecordDao;
  @Autowired
  private MailRecordLogDao mailRecordLogDao;
  @Autowired
  private MailEverydayStatisticDao mailEverydayStatisticDao;

  @Override
  public List<MailOriginalData> findOriginalList(int size) throws Exception {
    return mailOriginalDataDao.findListByDate(30, size);
  }

  @Override
  public List<MailRecord> findRecordList(int size) throws Exception {
    return mailRecordDao.findListByDate(30, size);
  }

  @Override
  public void handleOriginalList(List<MailOriginalData> originalList) throws Exception {
    if (originalList != null && originalList.size() > 0) {
      for (MailOriginalData one : originalList) {
        MailOriginalDataLog log = new MailOriginalDataLog();
        log.setMailId(one.getMailId());
        log.setMailTemplateCode(one.getMailTemplateCode());
        log.setCreateDate(one.getCreateDate());
        log.setMsg(one.getMsg());
        log.setPriorLevel(one.getPriorLevel());
        log.setReceiver(one.getReceiver());
        log.setReceiverPsnId(one.getReceiverPsnId());
        log.setSenderPsnId(one.getSenderPsnId());
        log.setSendStatus(one.getSendStatus());
        log.setStatus(one.getStatus());
        log.setUpdateDate(one.getUpdateDate());
        mailOriginalDataLogDao.save(log);
        mailOriginalDataDao.delete(one);
      }
    }

  }

  @Override
  public boolean existOriginalList() throws Exception {
    return mailOriginalDataDao.existOriginalList(30);
  }

  @Override
  public void handleMailRecord(List<MailRecord> mailRecordList) throws Exception {
    if (mailRecordList != null && mailRecordList.size() > 0) {
      for (MailRecord one : mailRecordList) {
        MailRecordLog log = new MailRecordLog();
        log.setMailId(one.getMailId());
        log.setCreateDate(one.getCreateDate());
        log.setDistributeDate(one.getDistributeDate());
        log.setMailClient(one.getMailClient());
        log.setMailTemplateCode(one.getMailTemplateCode());
        log.setMsg(one.getMsg());
        log.setPriorLevel(one.getPriorLevel());
        log.setReceiver(one.getReceiver());
        log.setSender(one.getSender());
        log.setSenderName(one.getSenderName());
        log.setStatus(one.getStatus());
        log.setSubject(one.getSubject());
        mailRecordLogDao.save(log);
        mailRecordDao.delete(one);
      }
    }

  }

  @Override
  public boolean existRecordList() throws Exception {
    return mailRecordDao.existRecordList(30);
  }

  @Override
  public void statisticsMailInfo() throws Exception {
    List<Object[]> list = mailOriginalDataDao.findStatusCount();
    List<Object[]> list2 = mailOriginalDataDao.findSendStatusCount();
    if (list != null && list.size() > 0 || list2 != null && list2.size() > 0) {
      MailEverydayStatistic ms = mailEverydayStatisticDao.findCurrentInfo();
      if (ms == null) {
        ms = new MailEverydayStatistic();
        ms.setCreateDate(new Date());
      }
      ms.setTotalCount(mailOriginalDataDao.findCount());
      handleStatusList(ms, list);
      handleSendStatusList(ms, list2);
      mailEverydayStatisticDao.save(ms);
    }

  }

  private void handleSendStatusList(MailEverydayStatistic ms, List<Object[]> list2) {
    if (list2 != null && list2.size() > 0) {
      for (Object[] one : list2) {
        // 参考MailRecord的status字段
        // 0=待分配 1=待发送 2=发送成功 3=黑名单 4=receiver不存在,5邮件不在白名单 ,8邮件发送出错 ,9邮件调度出错,10邮件正在发送
        Integer status = Integer.parseInt(one[0].toString());
        Integer MailCount = Integer.parseInt(one[1].toString());
        if (status == 0) {
          ms.setToBeDistributed(MailCount);
        } else if (status == 1) {
          ms.setToBeSend(MailCount);
        } else if (status == 2) {
          ms.setSendSuccess(MailCount);
        } else if (status == 3) {
          ms.setBlacklist(MailCount);
        } else if (status == 4) {
          ms.setReceiverInexistence(MailCount);
        } else if (status == 5) {
          ms.setNoWhiteList(MailCount);
        } else if (status == 8) {
          ms.setSendError(MailCount);
        } else if (status == 9) {
          ms.setMailDispatchError(MailCount);;
        } else if (status == 10) {
          ms.setSending(MailCount);
        }
      }
    }

  }

  private void handleStatusList(MailEverydayStatistic ms, List<Object[]> list) {
    if (list != null && list.size() > 0) {
      for (Object[] one : list) {
        // 参考MailOriginalData的status字段
        // 构造状态 0=待构造邮件 2=构造失败 3=用户不接收此类邮件
        Integer status = Integer.parseInt(one[0].toString());
        Integer MailCount = Integer.parseInt(one[1].toString());
        if (status == 0) {
          ms.setToBeConstruct(MailCount);
        } else if (status == 2) {
          ms.setConstructError(MailCount);
        } else if (status == 3) {
          ms.setRefuseReceive(MailCount);
        } else if (status == 4) {
          ms.setTemplateTimeLimit(MailCount);
        } else if (status == 5) {
          ms.setFirstEmailSame(MailCount);
        }
      }
    }
  }

}
