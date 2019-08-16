package com.smate.core.base.utils.cache;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Locator;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.SerializingTranscoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * memcached缓存工厂基础服务.
 * 
 * @author liqinghua
 * 
 */
public abstract class AbstractMemcachedClientFactoryBean
    implements MemcachedClientFactoryBean, InitializingBean, DisposableBean {

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final ConnectionFactoryBuilder connectionFactoryBuilder = new ConnectionFactoryBuilder();
  private MemcachedClient memcachedClient = null;
  private String cacheSrvUrls;

  public AbstractMemcachedClientFactoryBean() {
    super();
    initCacheParams();
  }

  /**
   * 获取memcache客户端.
   * 
   * @return
   */
  @Override
  public MemcachedClient getMemcachedClient() {

    // 服务地址为空
    String tpCacheSrvUrls = getCacheSrvUrls();
    if (StringUtils.isBlank(tpCacheSrvUrls)) {
      destroyMemcachedClient();
      this.cacheSrvUrls = null;
      return null;
    }
    if (this.memcachedClient == null) {
      return instanceMemcachedClient(tpCacheSrvUrls);
    } else {
      // 地址发生变化，重新初始化
      if (!tpCacheSrvUrls.equalsIgnoreCase(this.cacheSrvUrls)) {
        destroyMemcachedClient();
      }
      return instanceMemcachedClient(tpCacheSrvUrls);
    }
  }

  /**
   * 创建缓存服务器.
   * 
   * @param tpCacheSrvUrls
   * @return
   */
  private MemcachedClient instanceMemcachedClient(String tpCacheSrvUrls) {
    if (this.memcachedClient == null) {
      synchronized (connectionFactoryBuilder) {
        if (this.memcachedClient == null) {
          try {
            this.memcachedClient =
                new MemcachedClient(connectionFactoryBuilder.build(), AddrUtil.getAddresses(tpCacheSrvUrls));
            cacheSrvUrls = tpCacheSrvUrls;
          } catch (Exception e) {
            logger.error("构建memcachedClient失败，CacheSrvUrls=" + tpCacheSrvUrls, e);
            return null;
          }
        }
      }
    }
    return this.memcachedClient;
  }

  private void destroyMemcachedClient() {
    if (this.memcachedClient != null) {
      synchronized (connectionFactoryBuilder) {
        if (this.memcachedClient != null) {
          this.memcachedClient.shutdown();
          this.memcachedClient = null;
        }
      }
    }
  }

  /**
   * 初始化参数.
   */
  private void initCacheParams() {

    connectionFactoryBuilder.setProtocol(Protocol.BINARY);
    SerializingTranscoder transcoder = new ScmSpyMemSerializingTranscoder();
    transcoder.setCompressionThreshold(1024);
    connectionFactoryBuilder.setTranscoder(transcoder);
    connectionFactoryBuilder.setOpTimeout(1000);
    connectionFactoryBuilder.setTimeoutExceptionThreshold(1998);
    connectionFactoryBuilder.setHashAlg(DefaultHashAlgorithm.KETAMA_HASH);
    connectionFactoryBuilder.setLocatorType(Locator.CONSISTENT);
    connectionFactoryBuilder.setFailureMode(FailureMode.Redistribute);
    connectionFactoryBuilder.setUseNagleAlgorithm(false);
  }

  /**
   * 获取缓存服务URL.
   * 
   * @return
   */
  public abstract String getCacheSrvUrls();

  @Override
  public void afterPropertiesSet() throws Exception {
    // 服务地址为空
    String tpCacheSrvUrls = getCacheSrvUrls();
    if (StringUtils.isNotBlank(tpCacheSrvUrls)) {
      this.memcachedClient = instanceMemcachedClient(tpCacheSrvUrls);
      if (this.memcachedClient == null) {
        logger.error("服务器启动构建memcachedClient失败，CacheSrvUrls=" + tpCacheSrvUrls);
      }
    } else {
      logger.error("服务器启动构建memcachedClient失败，服务器缓存地址为空");
    }
  }

  @Override
  public void destroy() throws Exception {
    if (this.memcachedClient != null) {
      this.memcachedClient.shutdown();
      this.memcachedClient = null;
    }
  }
}
