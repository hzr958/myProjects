package com.smate.center.open.service.publication;

import com.smate.center.open.model.publication.PublicationXml;

/**
 * 成果xml存储，读取服务.
 * 
 * @author ajb
 * 
 */
public interface PubXmlStoreService {

  /**
   * 读取XML.
   * 
   * @param pubId
   * @throws ServiceException
   */
  PublicationXml get(Long pubId) throws Exception;

}
