package com.smate.core.base.utils.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * memcached缓存工厂服务.
 * 
 * @author liqinghua
 * 
 */
public class IrisMemcachedClientFactoryBean extends AbstractMemcachedClientFactoryBean {

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String memcachedSrvUrls;

  public IrisMemcachedClientFactoryBean() {
    super();
  }

  /**
   * 获取缓存服务URL.
   * 
   * @return
   */
  public String getCacheSrvUrls() {

    try {
      return this.memcachedSrvUrls;
    } catch (Exception e) {
      logger.error("获取缓存服务URL错误", e);
    }
    return null;
  }

  public String getMemcachedSrvUrls() {
    return memcachedSrvUrls;
  }

  public void setMemcachedSrvUrls(String memcachedSrvUrls) {
    this.memcachedSrvUrls = memcachedSrvUrls;
  }

}
