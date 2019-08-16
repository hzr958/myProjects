package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * 成果xml存储，读取服务.
 * 
 * @author liqinghua
 * 
 */
public interface PubXmlStoreService extends Serializable {

  /**
   * 存储XML.
   * 
   * @param pubId
   * @param xml
   * @throws BatchTaskException
   */
  PublicationXml save(Long pubId, String xml) throws BatchTaskException;

  /**
   * 读取XML.
   * 
   * @param pubId
   * @throws BatchTaskException
   */
  PublicationXml get(Long pubId) throws BatchTaskException;

  /**
   * 获取批量指定ID的成果XML数据.
   * 
   * @param pubIds
   * @return
   * @throws BatchTaskException
   */
  List<PublicationXml> getBatchXmlByPubIds(Long[] pubIds) throws BatchTaskException;

  /**
   * 存储XML，不缓存.
   * 
   * @param pubId
   * @param xml
   * @throws BatchTaskException
   */
  PublicationXml saveNoCache(Long pubId, String xml) throws BatchTaskException;

  /**
   * 清除缓存.
   * 
   * @param pubId
   */
  void clearCache(Long pubId);

}
