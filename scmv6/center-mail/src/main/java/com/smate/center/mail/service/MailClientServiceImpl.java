package com.smate.center.mail.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.dao.MailClientDao;
import com.smate.center.mail.model.MailClient;

/**
 * 邮件客户端 服务
 * 
 * @author tsz
 *
 */
@Service("mailClientService")
@Transactional(rollbackFor = Exception.class)
public class MailClientServiceImpl implements MailClientService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MailClientDao mailClientDao;

  @Override
  public List<MailClient> getAllMailClient() {

    return null;
  }

  @Override
  public List<MailClient> getAvailableMailClient() {
    return mailClientDao.getAvailableMailClient();
  }

  @Override
  public List<MailClient> getUnavailableMailClient() {
    return mailClientDao.getUnavailableMailClient();
  }

  /**
   * 标记客户端为不可用
   */
  @Override
  public void updateClientUnavailable(MailClient mailClient) {
    mailClient.setStatus(1);
    mailClient.setUpdateDate(new Date());
    mailClient.setMsg("没有找到缓存中的节点信息,标记客户端为不可用");
    mailClientDao.save(mailClient);
  }

  /**
   * 标记客户端为可用
   */
  @Override
  public void updateClientAvailable(MailClient mailClient) {
    mailClient.setStatus(0);
    mailClient.setUpdateDate(new Date());
    mailClient.setMsg("缓存中存在该客户端信息,标记可客户端为可用!");
    mailClientDao.save(mailClient);
  }

  @Override
  public List<MailClient> getClientByTemmlateCode(String templateCode) {
    return mailClientDao.getMailClientByTemplateCode(templateCode);
  }

  @Override
  public List<MailClient> getClientByAccount(String account) {
    return mailClientDao.getMailClientByAccount(account);
  }

  @Override
  public List<MailClient> getClientByEmail(String email) {
    // 截取emal类型 emalhost
    String emailType = email.split("@")[1];
    return mailClientDao.getMailClientByEmailType(emailType);
  }

  @Override
  public List<MailClient> getClientByNoPrior() {
    return mailClientDao.getMailClientByNoPrior();
  }

  @Override
  public int getMailClientWaitTimeByName(String clientName) {
    return mailClientDao.getMailClientWaitTimeByName(clientName);
  }

  @Override
  public int getMailClientWaitTime() {
    return mailClientDao.getMailClientWaitTime();
  }

}
