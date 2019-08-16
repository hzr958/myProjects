/**
 * 
 */
package com.smate.center.batch.service.pub;

import com.smate.center.batch.model.sns.pub.PublicationXml;


/**
 * 
 * 成果缓存服务.
 * 
 * @author yamingd
 * 
 */
public interface PublicationCacheService {

  /**
   * 添加数据到缓存.
   * 
   * @param key
   * @param value
   */
  void put(String key, Object value);

  /**
   * 删除指定KEY的缓存数据.
   * 
   * @param key
   */
  void remove(String key);

  /**
   * 获取指定KEY的缓存数据.
   * 
   * @param key
   * @return
   */
  Object get(String key);

  /**
   * 缓存个人XML数据.
   * 
   * @param pubId
   * @param xml
   */
  void cacheSNSPubXml(PublicationXml xml);

  /**
   * 获取个人XML缓存数据.
   * 
   * @param pubId
   * @return
   */
  PublicationXml getSNSPubXml(long pubId);

  /**
   * 删除个人XML缓存数据.
   * 
   * @param pubId
   */
  void removeSNSPubXml(long pubId);

  /**
   * 缓存单位XML数据.
   * 
   * @param pubId
   * @param xml
   */
  void cacheROLPubXml(PublicationXml xml);

  /**
   * 获取单位XML缓存数据.
   * 
   * @param pubId
   * @return
   */
  PublicationXml getROLPubXml(long pubId);

  /**
   * 删除单位XML缓存数据.
   * 
   * @param pubId
   */
  void removeROLPubXml(long pubId);
}
