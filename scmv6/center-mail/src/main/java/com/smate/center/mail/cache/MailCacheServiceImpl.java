package com.smate.center.mail.cache;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.smate.center.mail.model.MailClientInfo;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.cache.MemcachedClientFactoryBean;
import com.smate.core.base.utils.json.JacksonUtils;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

/**
 * 邮件缓存服务
 * 
 * @author tsz
 *
 */
public class MailCacheServiceImpl implements MailCacheService {

  private MemcachedClientFactoryBean memcacheClient;
  private final String KEY_MODEL = "%s_%s";

  private static final String CACHE_NAME = "MAIL_DISPATCH";

  private static final String MAIL_CLIENT_CHECK_MSG = "MAIL_CLIENT_CHECK_MSG";

  private static final String CACHE_NAME_LOCK = "CACHE_NAME_LOCK";

  private static final String CAHCE_MONITOR_LOCK = "MAIL_MONITOR_LOCK";

  private static final String CACHE_NAME_WAIT = "CACHE_NAME_WAIT";

  private static final String WAIT_NAME = "WAIT";

  @Override
  public void monitorLock(String key) {
    this.put(CAHCE_MONITOR_LOCK, CacheService.EXP_HOUR_1, key, "true");
  }

  @Override
  public String getMonitorLock(String key) {
    Serializable obj = this.get(CAHCE_MONITOR_LOCK, key);
    if (obj != null) {
      return (String) obj;
    }
    return "";
  }

  /**
   * 锁定5S
   */
  @Override
  public void lockSender(String account) {
    this.put(CACHE_NAME_LOCK, 5, account, "true");
  }

  @Override
  public String getLockSender(String account) {
    Serializable obj = this.get(CACHE_NAME_LOCK, account);
    if (obj != null) {
      return (String) obj;
    }
    return "";
  }

  /**
   * 等待waitTime S
   */
  @Override
  public void waitDispatch(int waitTime) {
    this.put(CACHE_NAME_WAIT, waitTime, WAIT_NAME, "true");

  }

  @Override
  public String getWaitDispatch() {
    Serializable obj = this.get(CACHE_NAME_WAIT, WAIT_NAME);
    if (obj != null) {
      return (String) obj;
    }
    return "";
  }

  /**
   * 获取 客户端信息
   * 
   * @param key
   * @return
   */
  @Override
  public MailClientInfo getMailClientInfo(String key) {
    Serializable obj = this.get(CACHE_NAME, key);
    if (obj != null) {
      return JacksonUtils.jsonObject(obj.toString(), MailClientInfo.class);
    }
    return null;

  }

  /**
   * 更新 客户端信息 20s过期,客户端 10s更新一次
   * 
   * @param str
   * @return
   */
  @Override
  public void updateMailClientInfo(String jsonClientInfo) {
    if (StringUtils.isNotBlank(jsonClientInfo)) {
      MailClientInfo client = JacksonUtils.jsonObject(jsonClientInfo, MailClientInfo.class);
      this.put(CACHE_NAME, 10, client.getClientName(), jsonClientInfo);
    }
  }

  @SuppressWarnings("static-access")
  @Override
  public void putMailClientCheckMsg(String value) {
    this.put(CACHE_NAME, this.EXP_HOUR_1, MAIL_CLIENT_CHECK_MSG, value);
  }

  @Override
  public String getMailClientCheckMsg() {
    Serializable obj = this.get(CACHE_NAME, MAIL_CLIENT_CHECK_MSG);
    if (obj != null) {
      return (String) obj;
    }
    return "";
  }

  @Override
  public void removeMailClientCheckMsg() {
    this.remove(CACHE_NAME, MAIL_CLIENT_CHECK_MSG);
  }

  @SuppressWarnings("static-access")
  @Override
  public void putMailSenderCheckMsg(String key, String value) {
    this.put(CACHE_NAME, this.EXP_HOUR_1, key, value);

  }

  @Override
  public String getMailSenderCheckMsg(String key) {
    Serializable obj = this.get(CACHE_NAME, key);
    if (obj != null) {
      return (String) obj;
    }
    return "";
  }

  @Override
  public Serializable get(String cacheName, String key) {
    if (memcacheClient == null) {
      return null;
    }
    MemcachedClient client = memcacheClient.getMemcachedClient();
    if (client == null) {
      return null;
    }
    return (Serializable) client.get(getKey(cacheName, key));
  }

  @Override
  public boolean remove(String key) {
    return this.remove(CACHE_NAME, key);
  }

  @Override
  public boolean remove(String cacheName, String key) {
    if (memcacheClient == null) {
      return false;
    }
    MemcachedClient client = memcacheClient.getMemcachedClient();
    if (client == null) {
      return false;
    }
    OperationFuture<Boolean> fr = client.delete(getKey(cacheName, key));
    try {
      return fr.get();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 构造KEY.
   * 
   * @param cacheName
   * @param key
   * @return
   */
  private String getKey(String cacheName, String key) {

    return String.format(KEY_MODEL, cacheName, key);
  }

  public void setMemcacheClient(MemcachedClientFactoryBean memcacheClient) {
    this.memcacheClient = memcacheClient;
  }

  /**
   * 缓存对象，exp为过期时间，单位是秒.
   * 
   * @param cacheName
   * @param exp
   * @param key
   * @param obj
   * @return
   */
  public void putSession(String cacheName, int exp, String key, Serializable obj) {
    if (memcacheClient == null) {
      return;
    }
    MemcachedClient client = memcacheClient.getMemcachedClient();
    if (client == null) {
      return;
    }
    // 默认3天过期
    client.set(getKey(cacheName, key), exp, obj);
  }

  @Override
  public void put(String cacheName, String key, Serializable obj) {
    this.putSession(cacheName, key, obj);
  }

  @Override
  public void put(String cacheName, int exp, String key, Serializable obj) {
    this.putSession(cacheName, exp, key, obj);
  }

  public void putSession(String cacheName, String key, Serializable obj) {
    this.putSession(cacheName, 60 * 10, key, obj);
  }

}
