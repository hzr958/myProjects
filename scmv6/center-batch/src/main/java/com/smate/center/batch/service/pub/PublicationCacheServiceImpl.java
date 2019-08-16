package com.smate.center.batch.service.pub;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.smate.center.batch.model.sns.pub.PublicationXml;

/**
 * 成果缓存服务.
 * 
 * @author yamingd
 * 
 */
// @Service("publicationCacheService")
public class PublicationCacheServiceImpl implements PublicationCacheService, InitializingBean {

  private final Logger logger = LoggerFactory.getLogger(PublicationCacheServiceImpl.class);
  private Ehcache cache;
  private boolean enabled;

  @Override
  public Object get(String key) {
    Assert.notNull(key, "key参数不能为空");
    if (this.isEnabled()) {
      Element element = null;
      try {

        element = cache.get(key);
        if (element != null) {
          logger.debug("Cache hit: " + (element != null) + "; key: " + key);

          return element.getValue();
        }
        return null;

      } catch (IllegalStateException e) {
        logger.error("读取PublicationCache,key=" + key, e);
      } catch (CacheException e) {
        logger.error("读取PublicationCache,key=" + key, e);
      }
    }
    return null;
  }

  @Override
  public void put(String key, Object value) {
    Assert.notNull(key, "key参数不能为空");
    if (this.isEnabled()) {
      Element element = new Element(key, value);
      cache.put(element);
    }
  }

  @Override
  public void remove(String key) {
    Assert.notNull(key, "key参数不能为空");
    if (this.isEnabled()) {
      logger.debug("Remove Cache:" + key);
      cache.remove(key);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(cache, "cache mandatory");
  }

  /**
   * @param cache the cache to set
   */
  public void setCache(Ehcache cache) {
    this.cache = cache;
  }

  /**
   * @return the cache
   */
  public Ehcache getCache() {
    return cache;
  }

  /**
   * @param enabled the enabled to set
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * @return the enabled
   */
  public boolean isEnabled() {
    // XML需要缓存，故忽略这个设置.
    return true;
  }

  public String getSNSXmlCacheKey(long pubId) {
    return String.format("snsPubXml_%s", pubId);
  }

  public String getROLXmlCacheKey(long pubId) {
    return String.format("rolPubXml_%s", pubId);
  }

  @Override
  public void cacheROLPubXml(PublicationXml xml) {
    if (xml == null || StringUtils.isBlank(xml.getXmlData())) {
      return;
    }
    this.put(this.getROLXmlCacheKey(xml.getId()), xml.getXmlData());
  }

  @Override
  public void cacheSNSPubXml(PublicationXml xml) {

    if (xml == null || xml.getXmlData() == null) {
      return;
    }
    this.put(this.getSNSXmlCacheKey(xml.getId()), xml.getXmlData());
  }

  @Override
  public PublicationXml getROLPubXml(long pubId) {
    String pubXml = (String) this.get(this.getROLXmlCacheKey(pubId));
    PublicationXml xml = null;
    if (pubXml != null) {
      xml = new PublicationXml(pubId, pubXml);
    }
    return xml;
  }

  @Override
  public PublicationXml getSNSPubXml(long pubId) {
    String pubXml = (String) this.get(this.getSNSXmlCacheKey(pubId));
    PublicationXml xml = null;
    if (pubXml != null) {
      xml = new PublicationXml(pubId, pubXml);
    }
    return xml;
  }

  @Override
  public void removeROLPubXml(long pubId) {
    this.remove(this.getROLXmlCacheKey(pubId));
  }

  @Override
  public void removeSNSPubXml(long pubId) {
    this.remove(this.getSNSXmlCacheKey(pubId));
  }

}
