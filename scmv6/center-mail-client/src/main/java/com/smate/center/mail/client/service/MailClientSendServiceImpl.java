package com.smate.center.mail.client.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.client.exception.MailSendException;
import com.smate.center.mail.connector.mailenum.MailSendStatusEnum;
import com.smate.center.mail.connector.model.MailInfo;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 邮件发送服务
 * 
 * @author tsz
 *
 */
@Service("mailClientSendService")
public class MailClientSendServiceImpl implements MailClientSendService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${mail_dispatch_updatestatus}")
  private String updatestatus;
  @Autowired
  private RestTemplate restTemplate;

  // 执行业务
  @Override
  public void execute(String mailInfoStr) {
    MailInfo mailInfo = JacksonUtils.jsonObject(mailInfoStr, MailInfo.class);
    try {
      sendMail(mailInfo);
      // 发送成功
      updateMailStatus(mailInfo.getMailId(), MailSendStatusEnum.STATUS_2, "");
    } catch (MailSendException e) {
      logger.error("发送邮件出错" + mailInfo.toString(), e);
      updateMailStatus(mailInfo.getMailId(), MailSendStatusEnum.STATUS_8, e.getMessage());
    } catch (Exception e) {
      logger.error("发送邮件出错" + mailInfo.toString(), e);
      updateMailStatus(mailInfo.getMailId(), MailSendStatusEnum.STATUS_8, e.getMessage());
    }
  }

  @Override
  public void updateMailStatus(Long mailId, MailSendStatusEnum statsus, String msg) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("mailId", mailId);
    map.put("status", statsus.name());
    map.put("msg", msg);
    restTemplate.postForLocation(updatestatus, map);
  }

  /**
   * 邮件发送
   * 
   * @param mailInfo
   */
  private void sendMail(MailInfo mailInfo) throws MailSendException {
    try {
      JavaMailSenderImpl sendService = new JavaMailSenderImpl();
      sendService.setHost(mailInfo.getHost());
      sendService.setPort(mailInfo.getPort());
      sendService.setUsername(mailInfo.getAccount());
      sendService.setPassword(mailInfo.getPassword());
      sendService.setDefaultEncoding("Utf-8");
      MimeMessage msg = sendService.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
      helper.setTo(mailInfo.getReceiver());
      String senderName = mailInfo.getSenderName();
      if (StringUtils.isBlank(senderName)) {
        helper.setFrom(mailInfo.getAccount());
      } else {
        helper.setFrom(senderName + "<" + mailInfo.getAccount() + ">");
      }
      helper.setSubject(mailInfo.getSubject());
      helper.setText(mailInfo.getContent(), true);
      sendService.send(msg);
    } catch (Exception e) {
      throw new MailSendException("邮件发送出错" + mailInfo.toString() + ",error:" + e.getMessage(), e);
    }

  }

}
