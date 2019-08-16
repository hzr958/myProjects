package com.smate.core.base.utils.cache;

import java.io.Serializable;

/**
 * 远程CACHE服务.
 * 
 * @author liqinghua
 * 
 */
public interface CacheService {

  /**
   * 5分钟过期.
   */
  int EXP_MIN_5 = 60 * 5;
  /**
   * 10分钟过期.
   */
  int EXP_MIN_10 = 60 * 10;
  /**
   * 30分钟过期.
   */
  int EXP_MIN_30 = 60 * 30;
  /**
   * 1个小时过期.
   */
  int EXP_HOUR_1 = 60 * 60;
  /**
   * 3个小时过期.
   */
  int EXP_HOUR_3 = 60 * 60 * 3;
  /**
   * 5个小时过期.
   */
  int EXP_HOUR_6 = 60 * 60 * 6;
  /**
   * 12个小时过期.
   */
  int EXP_HOUR_12 = 60 * 60 * 12;
  /**
   * 1天过期.
   */
  int EXP_DAY_1 = 60 * 60 * 24;
  /**
   * 3天过期.
   */
  int EXP_DAY_3 = 60 * 60 * 24 * 3;
  /**
   * 7天过期.
   */
  int EXP_DAY_7 = 60 * 60 * 24 * 7;
  /**
   * 15天过期.
   */
  int EXP_DAY_15 = 60 * 60 * 24 * 15;
  /**
   * 30天过期.
   */
  int EXP_DAY_30 = 60 * 60 * 24 * 30;

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
  public void put(String cacheName, String key, Serializable obj);

  /**
   * 缓存对象，exp为过期时间，单位是秒.
   * 
   * @param cacheName
   * @param exp
   * @param key
   * @param obj
   * @return
   */
  public void put(String cacheName, int exp, String key, Serializable obj);

  /**
   * 删除缓存.
   * 
   * @param cacheName
   * @param key
   */
  public boolean remove(String cacheName, String key);
}
