package com.smate.center.mail.cache;

import com.smate.center.mail.model.MailClientInfo;
import com.smate.core.base.utils.cache.CacheService;

/**
 * 
 * 邮件缓存服务
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 *
 */
public interface MailCacheService extends CacheService {

  /**
   * 邮件监控管理锁定
   * 
   * @param key
   */
  public void monitorLock(String key);

  /**
   * 获取管理监控锁定 对象
   * 
   * @param key
   * @return
   */
  public String getMonitorLock(String key);

  /**
   * 锁定 发送账号
   */
  public void lockSender(String account);

  /**
   * 获取 锁定 的账号
   */
  public String getLockSender(String account);

  /**
   * 设置等待 发送账号
   */
  public void waitDispatch(int waitTime);

  /**
   * 获取 等待 的账号
   */
  public String getWaitDispatch();

  /**
   * 获取 客户端信息
   * 
   * @param key
   * @return
   */
  public MailClientInfo getMailClientInfo(String key);

  /**
   * 更新 客户端信息
   * 
   * @param jsonClientInfo
   * @return
   */

  public void updateMailClientInfo(String jsonClientInfo);

  /**
   * 获取 节点检查 管理员通知邮件发送情况
   * 
   * @return
   */
  public String getMailClientCheckMsg();

  /**
   * 保存节点检查 管理员通知邮件发送情况
   * 
   * @param value
   */
  public void putMailClientCheckMsg(String value);

  public void removeMailClientCheckMsg();

  /**
   * 保存 发送账号 检查邮件通知发送情况
   */
  public void putMailSenderCheckMsg(String key, String value);

  /**
   * 获取 发送账号 检查邮件通知发送情况
   */
  public String getMailSenderCheckMsg(String key);

  /**
   * 删除节点
   * 
   * @param key
   * @return
   */
  public boolean remove(String key);

}
