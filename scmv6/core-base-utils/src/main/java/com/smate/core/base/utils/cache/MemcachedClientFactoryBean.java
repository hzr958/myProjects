package com.smate.core.base.utils.cache;

import net.spy.memcached.MemcachedClient;

/**
 * memcache客户端工厂接口.
 * 
 * @author lqh
 *
 */
public interface MemcachedClientFactoryBean {

  /**
   * 获取memcache客户段.
   * 
   * @return
   */
  public MemcachedClient getMemcachedClient();
}
