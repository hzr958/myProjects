package com.smate.center.task.single.service.person;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import com.smate.core.base.utils.cache.MemcachedClientFactoryBean;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * user缓存实现
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("userCacheService")
@Transactional(rollbackFor = Exception.class)
public class UserCacheServiceImpl implements UserCacheService {

  private MemcachedClientFactoryBean memcacheClient;
  private final String KEY_MODEL = "%s_%s";
  private final Logger logger = LoggerFactory.getLogger(UserCacheServiceImpl.class);
  private final String CACHE_NAME = "sys_user_cache";

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
  public void putUser(String cacheName, int exp, String key, Serializable obj) {
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
    this.putUser(cacheName, key, obj);
  }

  @Override
  public void put(String cacheName, int exp, String key, Serializable obj) {
    this.putUser(cacheName, exp, key, obj);
  }

  @Override
  public void putUser(String cacheName, String key, Serializable obj) {
    this.putUser(cacheName, 60 * 10, key, obj);
  }

  @Override
  public User getCacheUser(Long userId) {
    User user = null;
    if (userId == null)
      return null;
    user = (User) this.get(CACHE_NAME, userId.toString());
    if (user != null) {
      logger.debug("Cache user: " + (user != null) + "; userId: " + userId);
      return user;
    }
    return null;
  }

  @Override
  public User get(Long userId) {
    if (userId == null)
      return null;
    User user = getCacheUser(userId);
    return user;
  }

  @Override
  public void put(Long userId, User user) {
    if (userId == null || user == null)
      return;
    this.put(CACHE_NAME, userId.toString(), user);
  }

  @Override
  public boolean remove(Long psnId) {
    if (psnId == null)
      return false;
    return this.remove(CACHE_NAME, psnId.toString());
  }
}
