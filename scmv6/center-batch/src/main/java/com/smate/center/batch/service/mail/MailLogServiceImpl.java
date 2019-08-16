package com.smate.center.batch.service.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.mail.emailsrv.MailLogBigDao;
import com.smate.center.batch.dao.mail.emailsrv.MailLogBigHisDao;
import com.smate.center.batch.dao.mail.emailsrv.MailLogDao;
import com.smate.center.batch.dao.mail.emailsrv.MailLogHisDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.emailsrv.MailLog;
import com.smate.center.batch.model.mail.emailsrv.MailLogBig;
import com.smate.center.batch.model.mail.emailsrv.MailLogBigHis;
import com.smate.center.batch.model.mail.emailsrv.MailLogHis;
import com.smate.center.batch.model.mail.emailsrv.MailPromoteStatus;
import com.smate.core.base.utils.model.Page;

/**
 * 
 * 邮件记录服务类
 * 
 * @author zk
 * 
 */
@Service("mailLogService")
@Transactional(rollbackFor = Exception.class)
public class MailLogServiceImpl implements MailLogService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailLogDao mailLogDao;

  @Autowired
  private MailLogBigDao mailLogBigDao;

  @Autowired
  private MailLogHisDao mailLogHisDao;

  @Autowired
  private MailLogBigHisDao mailLogBigHisDao;

  @Autowired
  private MailPromoteStatusService mailPromoteStatusService;

  /**
   * 保存
   */
  @Override
  public void saveMail(MailLog mailLog, String context) throws ServiceException {
    mailLogDao.save(mailLog);
    MailLogBig big = new MailLogBig(mailLog.getId(), context);
    mailLogBigDao.save(big);
  }

  /**
   * 保存
   */
  @Override
  public void saveMailLog(MailLog mailLog) throws ServiceException {
    mailLogDao.save(mailLog);
  }

  /**
   * 获取未发送邮件记录
   */
  @Override
  public List<MailLog> getMailLogNotSend(Integer size, Long startMailId) throws ServiceException {

    try {
      return mailLogDao.getMailLogNotSend(size, startMailId);
    } catch (Exception e) {
      logger.error("获取未发送邮件记录出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 更新已发送邮件状态
   * 
   * @param mailId
   * @param mailstatus
   * @throws ServiceException
   */
  @Override
  public void updateSuccessLog(Long mailId, Integer mailstatus) throws ServiceException {

    MailLog mailLog = mailLogDao.get(mailId);
    if (mailLog != null) {
      mailLog.setStatus(mailstatus);
      mailLogDao.save(mailLog);
    }
  }

  /**
   * 获取邮件内容
   */
  @Override
  public String getMailContextByMailId(Long mailId) throws ServiceException {

    try {
      return mailLogBigDao.findMailContext(mailId);
    } catch (Exception e) {
      logger.error("获取邮件内容出错,mailId=" + mailId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取未发送完的邮件记录数
   */
  @Override
  public Integer getNotSendPromoteMailCount(Integer tempCode) throws ServiceException {

    try {
      return mailLogDao.getNotSendPromoteMailCount(tempCode);
    } catch (Exception e) {
      logger.error("获取未发送完的邮件记录数出错,tempCode=" + tempCode, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取未发送完的邮件记录数
   */
  @Override
  public Integer getNotSendPromoteMailCount2(MailPromoteStatus mailStatus) throws ServiceException {

    try {
      return mailLogDao.getNotSendPromoteMailCount2(mailStatus);
    } catch (Exception e) {
      logger.error("获取未发送完的邮件记录数出错,tempCode=" + mailStatus.getTempIdZh(), e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取推广邮件记录
   */
  @Override
  public List<MailLog> getPromoteMailByTempAndStatus(Integer status, Integer tempCode, Long startMailId, Integer size)
      throws ServiceException {
    try {
      return mailLogDao.getPromoteMailByTempAndStatus(status, tempCode, startMailId, size);
    } catch (Exception e) {
      logger.error("获取推广邮件记录出错,tempCode=" + tempCode + ",status=" + status, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取推广邮件记录(中英文)
   */
  @Override
  public List<MailLog> getPromoteMailByTempAndStatus2(Integer status, MailPromoteStatus mailStatus, Long startMailId,
      Integer size) throws ServiceException {
    try {
      return mailLogDao.getPromoteMailByTempAndStatus2(status, mailStatus, startMailId, size);
    } catch (Exception e) {
      logger.error("获取推广邮件记录出错,tempCode=" + mailStatus.getTempIdZh() + ",status=" + status, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取五条tempId推广邮件记录，用于预览
   * 
   * @param page
   * @param tempId
   * @return
   * @throws ServiceException
   */
  @Override
  public Page<String> getMailContextForPreView(Page<String> page, Integer tempId) throws ServiceException {
    try {
      MailPromoteStatus mailStatus = mailPromoteStatusService.getMailStatusByTempCode(tempId);
      if (mailStatus != null) {
        List<Long> mailIdList = mailLogDao.getlastThreeByTempId(mailStatus);
        if (CollectionUtils.isNotEmpty(mailIdList)) {
          List<String> contextList = mailLogBigDao.findMailContextByMailIds(mailIdList);
          if (CollectionUtils.isNotEmpty(contextList)) {
            page.setResult(contextList);
          }
        }
      }
      return page;
    } catch (Exception e) {
      logger.error("获取五条tempId推广邮件记录，用于预览出错,tempId=" + tempId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 通过tempCode获取未发送邮件记录
   */
  @Override
  public List<MailLog> getNotSendMailLogByTempCode(Integer tempCode) throws ServiceException {

    return null;
  }

  /**
   * 获取指定月份前size条数据
   */
  @Override
  public List<MailLog> getMailLogBeforeAssignMonths(Date assignMoths, Integer size) throws ServiceException {
    try {
      return mailLogDao.getMailLogBeforeAssignMonths(assignMoths, size);
    } catch (Exception e) {
      logger.error("获取指定月份前邮件记录出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取指定月份前size条邮件备份数据
   */
  @Override
  public List<MailLogHis> getMailLogHisBeforeAssignMonths(Date assignMoths, Integer size) throws ServiceException {
    try {
      return mailLogHisDao.getMailLogHisBeforeAssignMonths(assignMoths, size);
    } catch (Exception e) {
      logger.error("获取指定月份前邮件记录备份出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 清理邮件备份数据
   */
  @Override
  public void clearMailLogHis(List<MailLogHis> mailLogHisList) throws ServiceException {
    List<Long> mailIdList = new ArrayList<Long>();
    for (MailLogHis mailLogHis : mailLogHisList) {
      mailIdList.add(mailLogHis.getId());
      mailLogHisDao.delete(mailLogHis);
    }
    this.clearMailLogBigHis(mailIdList);
  }

  /**
   * 清理邮件备份大数据
   */

  private void clearMailLogBigHis(List<Long> mailIdList) throws ServiceException {
    mailLogBigHisDao.clearAssignId(mailIdList);
  }

  /**
   * 备份邮件数据
   */
  @Override
  public void backUpMailLog(List<MailLog> mailLogList) throws ServiceException {

    List<Long> mailIdList = new ArrayList<Long>();
    for (MailLog mailLog : mailLogList) {
      mailIdList.add(mailLog.getId());
      MailLogHis mailLogHis = new MailLogHis();
      mailLogHis.setAttachment(mailLog.getAttachment());
      mailLogHis.setContext(mailLog.getContext());
      mailLogHis.setCreateDate(mailLog.getCreateDate());
      mailLogHis.setFileNames(mailLog.getFileNames());
      mailLogHis.setId(mailLog.getId());
      mailLogHis.setNode(mailLog.getNode());
      mailLogHis.setPriorCode(mailLog.getPriorCode());
      mailLogHis.setPsnId(mailLog.getPsnId());
      mailLogHis.setSenderName(mailLogHis.getSenderName());
      mailLogHis.setStatus(mailLog.getStatus());
      mailLogHis.setSubject(mailLog.getSubject());
      mailLogHis.setTemplate(mailLog.getTemplate());
      mailLogHis.setTemplateCode(mailLog.getTemplateCode());
      mailLogHis.setToAddr(mailLog.getToAddr());
      mailLogDao.delete(mailLog);
      mailLogHisDao.save(mailLogHis);
    }
    this.backUpMailLogBig(mailIdList);
  }

  /**
   * 备份mailLogBig
   * 
   * @param mailIds
   * @throws ServiceException
   */
  private void backUpMailLogBig(List<Long> mailIds) throws ServiceException {
    try {
      if (CollectionUtils.isNotEmpty(mailIds)) {
        List<MailLogBig> mailLogBigList = mailLogBigDao.getMailLogBigByMailIds(mailIds);
        if (CollectionUtils.isNotEmpty(mailLogBigList)) {
          for (MailLogBig mailLogBig : mailLogBigList) {
            MailLogBigHis mailLogBigHis = new MailLogBigHis();
            mailLogBigHis.setContext(mailLogBig.getContext());
            mailLogBigHis.setId(mailLogBig.getId());
            mailLogBigHis.setMailId(mailLogBig.getMailId());
            mailLogBigHisDao.save(mailLogBigHis);
            mailLogBigDao.delete(mailLogBig);
          }
        }
      }
    } catch (Exception e) {
      logger.error("备份mailLogBig出错", e);
      throw new ServiceException(e);
    }
  }
}
