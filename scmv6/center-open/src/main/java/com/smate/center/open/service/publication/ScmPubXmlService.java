package com.smate.center.open.service.publication;

import java.io.Serializable;

import com.smate.center.open.model.publication.ScmPubXml;

/**
 * 成果XML服务.
 * 
 * @author liqinghua
 * 
 */

public interface ScmPubXmlService extends Serializable {

  /**
   * 获取成果XML.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public ScmPubXml getPubXml(Long pubId) throws Exception;
}
