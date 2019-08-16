package com.smate.center.mail.service;

import java.util.List;

import com.smate.center.mail.model.MailSender;

/**
 * 发件账号 服务接口
 * 
 * @author tsz
 *
 */
public interface MailSenderService {

  /**
   * 检查是否有可用发送账户
   * 
   * @return
   */
  public boolean hasAvailableSender();

  /**
   * 获取 所有可用发送账号
   * 
   * @return
   */
  public List<MailSender> getAllAvailableSender();

  /**
   * 检查 最大发送限制
   */
  public void checkMaxMailCountLimit(MailSender mailSender);

  /**
   * 初始化今天 的发送次数
   */
  public void initTodayMailCount();

  /**
   * 增加今天 的发送统计
   */
  public void addTodayMailCount(String account);

  /**
   * 减 今天 的发送统计
   */
  public void subTodayMailCount(String account);

  /**
   * 获取 发送者信息
   * 
   * @return
   */
  public MailSender getMailSenderBySender(String account);

  /**
   * 获取配置了 接收邮箱 类别 优先的可用发送账号
   * 
   * @return
   */

  public List<MailSender> getMailSenderByEmail(String email);

  /**
   * 获取配置了 模板 优先的可用发送账号
   * 
   * @return
   */
  public List<MailSender> getMailSenderByTemplate(String templateCode);

  /**
   * 获取配置了 客户端 优先的可用发送账号
   * 
   * @return
   */

  public List<MailSender> getMailSenderByClient(String clinetName);

  /**
   * 获取 没有配置优先的 发送账号
   * 
   * @return
   */

  public List<MailSender> getMailSenderByNoPrior();

  /**
   * 获取 管理员发送账号
   * 
   * @return
   */
  public List<MailSender> getManagerMailSender();

}
