package com.smate.center.batch.service.mail;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.mail.emailsrv.MailInitDataDao;
import com.smate.center.batch.dao.mail.emailsrv.SyncEmailFailureLogDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.emailsrv.MailInitData;
import com.smate.center.batch.model.mail.emailsrv.SyncEmailFailureLog;
import com.smate.center.batch.service.pub.mq.SyncMailToMailSrvMessage;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.json.JacksonUtils;


/**
 * 
 * 邮件同步信息标识处理
 * 
 * @author zk
 * 
 */
@Service("emailSyncFlagHandler")
@Transactional(rollbackFor = Exception.class)
public class EmailSyncFlagHandler implements EmailHandlerService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "emailImportantDataCheckHandler")
  private EmailHandlerService emailHandler;

  @Autowired
  private SyncEmailFailureLogDao syncEmailFailureLogDao;

  @Autowired
  private MailInitDataDao mailInitDataDao;

  @SuppressWarnings("rawtypes")
  @Override
  public String handler(Object... params) throws ServiceException {

    String returnStr = null;
    SyncMailToMailSrvMessage syncMessage = null;
    Map mailMap = null;
    try {
      syncMessage = (SyncMailToMailSrvMessage) params[0];
      if (EmailConstants.FAILURE.equals(syncMessage.getMailFlag())) {
        returnStr = "同步邮件信息发送端处理邮件数据时出错";
        this.saveFailTrace(syncMessage, returnStr, mailMap);
        return null;
      } else {
        mailMap = JacksonUtils.jsonMapUnSerializer(syncMessage.getMailJson());
        this.saveMailInitData(syncMessage, mailMap);
        // 类型＼邮件数据＼来源节点，默认节点1
        returnStr = emailHandler.handler(syncMessage.getMailType(), mailMap, 1);
        if (StringUtils.isNotBlank(returnStr)) {
          this.saveFailTrace(syncMessage, returnStr, mailMap);
        }
      }
    } catch (Exception e) {
      if (StringUtils.isBlank(returnStr)) {
        returnStr = "同步邮件信息接收端处理邮件数据时出错";
      }
      this.saveFailTrace(syncMessage, returnStr, mailMap);
      logger.error(returnStr + "exception:" + e.getLocalizedMessage());
    }
    return null;
  }

  /**
   * 备份初始数据
   * 
   * @param syncMessage
   * @param mailMap
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  private void saveMailInitData(SyncMailToMailSrvMessage syncMessage, Map mailMap) throws ServiceException {
    MailInitData initData = new MailInitData(syncMessage.getMailJson(), 1);
    if (mailMap.get(EmailConstants.EMAIL_RECEIVEEMAIL_KEY) != null) {
      initData.setToAddress(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_RECEIVEEMAIL_KEY)));
    }
    // 将状态置为1，后台任务才会发送邮件
    initData.setStatus(1);
    mailInitDataDao.save(initData);
  }

  /**
   * 
   * 错误日志备份
   * 
   * @param syncMessage
   * @param returnStr
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  private void saveFailTrace(SyncMailToMailSrvMessage syncMessage, String returnStr, Map mailMap)
      throws ServiceException {

    SyncEmailFailureLog failLog = new SyncEmailFailureLog();
    // 默认为1
    failLog.setFromNode(1);
    failLog.setEmailJson(syncMessage.getMailJson());
    failLog.setCtDate(new Date());
    // 错误信息限制为200字符
    failLog.setErrorMsg(returnStr.length() > 200 ? returnStr.substring(0, 200) : returnStr);
    if (MapUtils.isNotEmpty(mailMap)) {
      failLog.setToAddress(ObjectUtils.toString(mailMap.get(EmailConstants.EMAIL_RECEIVEEMAIL_KEY)));
    }
    syncEmailFailureLogDao.save(failLog);
  }
}
