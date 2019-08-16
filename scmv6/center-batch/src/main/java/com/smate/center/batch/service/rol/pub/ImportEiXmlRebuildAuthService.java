package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;


/**
 * 重构导入EI成果XML作者.
 * 
 * @author liqinghua
 * 
 */
public interface ImportEiXmlRebuildAuthService extends Serializable {

  /**
   * 重构导入成果作者XML.
   * 
   * @param doc
   * @param insId
   * @return
   * @throws ServiceException
   */
  public PubXmlDocument rebuildAuthXml(PubXmlDocument doc, Long insId) throws ServiceException;
}
