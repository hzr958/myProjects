package com.smate.center.mail.service.monitor;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.dao.MailSenderDao;
import com.smate.center.mail.dao.MonitorLogDao;
import com.smate.center.mail.model.MailSender;
import com.smate.center.mail.model.MonitorLog;

/**
 * 邮件调度监控
 * 
 * @author tsz
 *
 */
@Service("mailMonitorService")
@Transactional(rollbackFor = Exception.class)
public class MailMonitorServiceImpl implements MailMonitorService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailSenderDao mailSenderDao;
  @Autowired
  private MonitorLogDao monitorLogDao;

  @Override
  public void sendMonitorMail(String subject, String content) {

    // 使用独立的邮箱作为管理员监控邮件发送
    MailSender mailSender = mailSenderDao.getMonitorMailSender();
    if (mailSender == null) {
      logger.error("发送监控邮件出错 -没有邮件发送账号  邮件主题:" + subject);
      saveMonitorLog(subject, content, "", "");
      return;
    }
    String[] receivers = new String[] {};
    if (StringUtils.isNoneBlank(mailSender.getMsg())) {
      receivers = mailSender.getMsg().split(";");
    }
    mailSender.setTodayMailCount(mailSender.getTodayMailCount() + 1);
    mailSenderDao.save(mailSender);
    // 记录邮件发送情况
    saveMonitorLog(subject, content, mailSender.getAccount(), mailSender.getMsg());
    // 并且 发送到固定的人员
    try {
      // 只有生产机 才发管理员通知邮件
      String env = System.getenv("RUN_ENV");
      if ("run".equals(env)) {
        sendMail(subject, content, mailSender, receivers);
      }
    } catch (Exception e) {
      logger.error("发送监控邮件出错  ,邮件主题为:" + subject, e);
    }

  }

  private void saveMonitorLog(String subject, String content, String sender, String receivers) {
    MonitorLog monitorLog = new MonitorLog();
    monitorLog.setSender(sender);
    monitorLog.setReceiver(receivers);
    monitorLog.setUpdateDate(new Date());
    monitorLog.setMsg(subject + "-->" + content);
    monitorLogDao.save(monitorLog);
  }

  private void sendMail(String subject, String content, MailSender mailSender, String[] receivers)
      throws MessagingException {
    JavaMailSenderImpl sendService = new JavaMailSenderImpl();
    sendService.setHost(mailSender.getHost());
    sendService.setPort(mailSender.getPort());
    sendService.setUsername(mailSender.getAccount());
    sendService.setPassword(mailSender.getPassword());
    sendService.setDefaultEncoding("Utf-8");
    MimeMessage msg = sendService.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
    if (receivers.length > 0) {
      helper.setTo(receivers[0]);
      helper.setCc(receivers);
    } else {
      helper.setTo("shaozhitan@irissz.com");
      helper.setCc(new String[] {"zhiqiangfan@irissz.com", "yanmingzhuang@irissz.com", "kexing@irissz.com",
          "linglingzhang@irissz.com", "huixiaye@irissz.com"});
    }
    helper.setFrom("科研之友" + "<" + mailSender.getAccount() + ">");
    helper.setSubject(subject);
    helper.setText(content, true);
    sendService.send(msg);
  }

}
