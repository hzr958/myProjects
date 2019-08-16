package com.smate.core.web.sys.security.cache;

import java.io.Serializable;

import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * session缓存处理服务
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface UserCacheService extends CacheService {
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
  public void putUser(String cacheName, String key, Serializable obj);

  /**
   * 缓存对象，exp为过期时间，单位是秒.
   * 
   * @param cacheName
   * @param exp
   * @param key
   * @param obj
   * @return
   */
  public void putUser(String cacheName, int exp, String key, Serializable obj);

  /**
   * 删除缓存.
   * 
   * @param cacheName
   * @param key
   */
  public boolean remove(String cacheName, String key);



  /**
   * 添加人员数据到缓存.
   * 
   * @param key
   * @param value
   */
  void put(Long key, User obj) throws SysServiceException;

  /**
   * 删除指定KEY的缓存数据.
   * 
   * @param key
   */
  boolean remove(Long key);

  /**
   * 获取缓存中的数据.
   * 
   * @param key
   * @return
   */
  User getCacheUser(Long key);

  /**
   * 获取指定KEY的缓存数据，如果缓存找不到，则查找cas端数据.
   * 
   * @param key
   * @return
   */
  User get(Long key);
}
