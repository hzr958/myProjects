package com.smate.sie.web.application.service.email;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.web.application.dao.validate.MailNotificationListDao;
import com.smate.sie.web.application.model.validate.MailNotificationKeys;

/**
 * 通知财务开发票邮件业务层
 * 
 * @author xr
 *
 */
@Service("emailNotificationService")
@Transactional(rollbackFor = Exception.class)
public class EmailNotificationServiceImpl implements EmailNotificationService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieSendEmailBuildService sieSendEmailBuildService;

  @Autowired
  private MailNotificationListDao mailNotificationListDao;

  @Override
  public void sendEmailInvoiceInformFinance(Map<String, Object> hashInput) throws Exception {
    // 开发票通知财务固定key为（validateNeedInvoice）
    String emails = mailNotificationListDao.getMailValuesByMailKey(MailNotificationKeys.VALIDATE_NEED_INVOICE);
    String[] emailArray = StringUtils.split(emails, ";|；");
    trimEmails(emailArray);
    if (emailArray != null) {
      for (int i = 0; i < emailArray.length; i++) {
        try {
          // 设置邮件主题，发送
          sieSendEmailBuildService.sendEmail(getEmailParam(emailArray[i], 10120, "开发票通知财务邮件", 0L), hashInput);
        } catch (Exception e) {
          logger.error("开发票通知财务邮件发送时失败", e.getMessage());
          throw new Exception();
        }
      }
    }
  }

  /**
   * 构建Email对应的内容
   * 
   * @param receiveEmail
   * @param templateCode
   * @param msg
   * @param receivePsnID
   */
  private Map<String, Object> getEmailParam(String receiveEmail, Integer templateCode, String msg, Long receivePsnID) {
    Map<String, Object> tempMap = new LinkedHashMap<String, Object>();
    tempMap.put(SieSendEmailBuildService.EMAIL_TEMPLATE_CODE, templateCode);
    tempMap.put(SieSendEmailBuildService.EMAIL_MSG, msg);
    tempMap.put(SieSendEmailBuildService.EMAIL_RECEIVE, receiveEmail);
    tempMap.put(SieSendEmailBuildService.EMAIL_RECEIVER_PSNID, receivePsnID);
    tempMap.put(SieSendEmailBuildService.EMAIL_SENDER_PSNID, 0L);
    return tempMap;
  }

  /**
   * 去除Emails 数组中每个 字符串的前后空格
   * 
   * @param Emails
   */
  private void trimEmails(String[] emails) {
    if (emails.length != 0) {
      for (int i = 0; i < emails.length; i++) {
        emails[i] = emails[i].trim();
      }
    }
  }

}
