package com.smate.center.mail.service;

import java.util.List;

import com.smate.center.mail.model.MailClient;

/**
 * 邮件客户端 服务接口
 * 
 * @author tsz
 *
 */
public interface MailClientService {

  /**
   * 获取 所有客户端标记
   * 
   * @return
   */
  public List<MailClient> getAllMailClient();

  /**
   * 获取 可用客户端标记
   * 
   * @return
   */
  public List<MailClient> getAvailableMailClient();

  /**
   * 获取 不可用客户端标记
   * 
   * @return
   */
  public List<MailClient> getUnavailableMailClient();

  /**
   * 标记 客户端为不可用
   * 
   * @param mailClient
   */
  public void updateClientUnavailable(MailClient mailClient);

  /**
   * 标记 客户端为可用
   * 
   * @param mailClient
   */
  public void updateClientAvailable(MailClient mailClient);

  /**
   * 获取 模板优先级的客户端
   * 
   * @param templateCode
   * @return
   */
  public List<MailClient> getClientByTemmlateCode(String templateCode);

  /**
   * 获取 发送账号优先级的客户端
   * 
   * @param templateCode
   * @return
   */
  public List<MailClient> getClientByAccount(String account);

  /**
   * 获取接收邮件类别 优先级的客户端
   * 
   * @param templateCode
   * @return
   */
  public List<MailClient> getClientByEmail(String email);

  /**
   * 获取 没有配置优先级的客户端
   * 
   * @return
   */
  public List<MailClient> getClientByNoPrior();

  /**
   * 根据客户端名称 获取 客户端等待时间
   * 
   * @param clientName
   * @return
   */
  public int getMailClientWaitTimeByName(String clientName);

  /**
   * 获取 等待时间
   * 
   * @return
   */
  public int getMailClientWaitTime();

}
