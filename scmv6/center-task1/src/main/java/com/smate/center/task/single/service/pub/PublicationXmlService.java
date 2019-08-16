package com.smate.center.task.single.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.model.sns.quartz.PubDataStore;
import com.smate.center.task.model.sns.quartz.PubFulltextExtend;
import com.smate.center.task.model.sns.quartz.PublicationXml;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 成果XML基本服务，提供与当前人、单位无关的操作.
 * 
 */
public interface PublicationXmlService extends Serializable {

  /**
   * 清除缓存.
   * 
   * @param pubId
   * @throws BatchTaskException
   */
  void clearCache(Long pubId) throws BatchTaskException;

  /**
   * @param pubId
   * @return
   * @throws BatchTaskException
   */
  PublicationXml getById(Long pubId) throws BatchTaskException;

  /**
   * 保存成果XML.
   * 
   * @param pubId
   * @param xml
   * @return
   * @throws BatchTaskException
   */
  PublicationXml save(Long pubId, String xml) throws BatchTaskException;

  /**
   * 保存成果XML.
   * 
   * @param pubId
   * @param xml
   * @return
   * @throws BatchTaskException
   */
  PublicationXml saveNoCache(Long pubId, String xml) throws BatchTaskException;

  /**
   * 批量获取成果XML.
   * 
   * @param pubIds
   * @return
   * @throws BatchTaskException
   */
  List<PublicationXml> getBatchXmlByPubIds(Long[] pubIds) throws BatchTaskException;

  /**
   * 成果来源URL.
   * 
   * @param pubId 成果ID
   * @param isCache 从缓存库读取(0/1)
   * @return String
   * @throws BatchTaskException
   */
  String getSourceUrl(long pubId, boolean isCache, List<Long> insIdList) throws BatchTaskException;

  // TODO
  // String getPdwhPubSourceUrl(long pubId, int dbid, List<Long> insIdList) throws BatchTaskException;
  // TODO
  // String getPdwhPubFulltextUrl(long pubId, int dbid, List<Long> insIdList) throws
  // BatchTaskException;

  /**
   * 成果引用Url.
   * 
   * @param pubId 成果ID
   * @param isCache 从缓存库读取(0/1)
   * @return String
   * @throws BatchTaskException
   */
  String getCitationUrl(long pubId, boolean isCache, List<Long> insIdList) throws BatchTaskException;

  /**
   * 成果全文(全文链接.).
   * 
   * @param pubId 成果ID
   * @param isCache 从缓存库读取(0/1)
   * @return String
   * @throws BatchTaskException
   */
  String getFullTextUrl(long pubId, boolean isCache, List<Long> insIdList) throws BatchTaskException;

  /**
   * 成果全文(全文附件.).
   * 
   * @param pubId 成果ID
   * @param isCache 从缓存库读取(0/1)
   * @return String
   * @throws BatchTaskException
   */
  PubFulltextExtend getFullText(long pubId, boolean isCache) throws BatchTaskException;

  /**
   * 构造全文附件下载URL.
   * 
   * @param fullText
   * @return
   * @throws BatchTaskException
   */
  // TODO
  // String getGeneralFullTextDownUrl(PubFulltextExtend fullText) throws BatchTaskException;

  /**
   * 去除摘要版权信息.
   * 
   * @param pubXml
   * @throws BatchTaskException
   */
  void rebuildPubXMLCopyright(PubXmlDocument xmlDocument) throws BatchTaskException;

  public PubDataStore getXmlFromPubDataStore(Long pubId);

  PublicationXml rolGetById(Long pubId) throws BatchTaskException;

  PublicationXml rolSave(Long pubId, String xml) throws BatchTaskException;
}
