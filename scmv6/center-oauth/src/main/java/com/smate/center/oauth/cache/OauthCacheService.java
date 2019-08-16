package com.smate.center.oauth.cache;

import java.io.Serializable;

import com.smate.core.base.utils.cache.CacheService;

/**
 * 
 * oauth业务缓存
 * 
 * @author tsz
 * @since 6.0.1
 * @version 6.0.1
 *
 */
public interface OauthCacheService extends CacheService {

  /**
   * 获取缓存对象.
   * 
   * @param cacheName
   * @param key
   * @return
   */
  public Serializable get(String cacheName, String key);

  /**
   * 缓存对象，默认10分钟过期.
   * 
   * @param cacheName
   * @param key
   * @param obj
   */
  public void putSession(String cacheName, String key, Serializable obj);

  /**
   * 缓存对象，exp为过期时间，单位是秒.
   * 
   * @param cacheName
   * @param exp
   * @param key
   * @param obj
   * @return
   */
  public void putSession(String cacheName, int exp, String key, Serializable obj);

  /**
   * 删除缓存.
   * 
   * @param cacheName
   * @param key
   */
  public boolean remove(String cacheName, String key);
}
