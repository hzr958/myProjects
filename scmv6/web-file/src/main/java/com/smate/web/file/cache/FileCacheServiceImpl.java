package com.smate.web.file.cache;

import java.io.Serializable;

import com.smate.core.base.utils.cache.MemcachedClientFactoryBean;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

/**
 * 文件项目公共缓存
 * 
 * @author tsz
 * @since 6.0.1
 */
public class FileCacheServiceImpl implements FileCacheService {

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

  @Override
  public void put(String cacheName, String key, Serializable obj) {
    put(cacheName, 60 * 10, key, obj);
  }

  @Override
  public void put(String cacheName, int exp, String key, Serializable obj) {
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
}
