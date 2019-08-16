package com.smate.center.mail.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.dao.MailSenderDao;
import com.smate.center.mail.model.MailSender;

/**
 * 发件账号 服务
 * 
 * @author tsz
 *
 */
@Service("mailSenderService")
@Transactional(rollbackFor = Exception.class)
public class MailSenderServiceImpl implements MailSenderService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailSenderDao mailSenderDao;

  /**
   * 检查是否有可用发送账户
   * 
   * @return
   */
  @Override
  public boolean hasAvailableSender() {
    List<MailSender> list = mailSenderDao.getAvailableSenderAccount();
    boolean temp = false;
    if (!CollectionUtils.isEmpty(list)) {
      temp = true;
    }
    return temp;
  }

  @Override
  public List<MailSender> getAllAvailableSender() {

    return mailSenderDao.getAvailableSenderAccount();
  }

  @Override
  public void checkMaxMailCountLimit(MailSender mailSender) {
    if (mailSender.getTodayMailCount() >= mailSender.getMaxMailCount()) {
      // 更新状态为不可用
      mailSender.setStatus(9);
      mailSender.setUpdateDate(new Date());
      mailSender.setMsg("邮件发送超过限制,更新为不可用");
      mailSenderDao.save(mailSender);
    }
  }

  @Override
  public void initTodayMailCount() {

    mailSenderDao.initTodayMailCount();
  }

  @Override
  public MailSender getMailSenderBySender(String account) {
    return mailSenderDao.getSender(account);
  }

  /**
   * 获取配置了 接收邮箱 类别 优先的可用发送账号
   * 
   * @return
   */
  @Override
  public List<MailSender> getMailSenderByEmail(String email) {
    String emailType = email.split("@")[1];
    return mailSenderDao.getMailSenderByEmailType(emailType);
  }

  /**
   * 获取配置了 模板 优先的可用发送账号
   * 
   * @return
   */
  @Override
  public List<MailSender> getMailSenderByTemplate(String templateCode) {
    return mailSenderDao.getMailSenderByTemplate(templateCode);
  }

  /**
   * 获取配置了 客户端 优先的可用发送账号
   * 
   * @return
   */
  @Override
  public List<MailSender> getMailSenderByClient(String clinetName) {
    return mailSenderDao.getMailSenderByClient(clinetName);
  }

  @Override
  public List<MailSender> getMailSenderByNoPrior() {

    return mailSenderDao.getMailSenderByNoprior();
  }

  @Override
  public void addTodayMailCount(String account) {
    MailSender mailSender = mailSenderDao.getSender(account);
    mailSender.setTodayMailCount(mailSender.getTodayMailCount() + 1);
    mailSenderDao.save(mailSender);
  }

  @Override
  public void subTodayMailCount(String account) {
    MailSender mailSender = mailSenderDao.getSender(account);
    int todayMailCount = mailSender.getTodayMailCount() - 1 < 0 ? 0 : mailSender.getTodayMailCount() - 1;
    mailSender.setTodayMailCount(todayMailCount);
    mailSenderDao.save(mailSender);
  }

  @Override
  public List<MailSender> getManagerMailSender() {
    return mailSenderDao.getManagerMailSender();
  }

}
