package com.smate.center.open.cache;

import java.io.Serializable;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import com.smate.core.base.utils.cache.MemcachedClientFactoryBean;

/**
 * oauth系统缓存服务
 * 
 * @author tsz
 *
 */
public class OauthCacheServiceImpl implements OauthCacheService {

  private MemcachedClientFactoryBean memcacheClient;
  private final String KEY_MODEL = "%s_%s";

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

  @Override
  public void putSession(String cacheName, String key, Serializable obj) {
    this.putSession(cacheName, 60 * 10, key, obj);
  }

}
