package com.smate.center.open.service.publication;


import com.smate.center.open.model.publication.PublicationXml;

/**
 * 成果XML基本服务，提供与当前人、单位无关的操作.
 * 
 */
public interface PublicationXmlService {

  /**
   * @param pubId
   * @return
   * @throws ServiceException
   */
  PublicationXml getById(Long pubId) throws Exception;

}
