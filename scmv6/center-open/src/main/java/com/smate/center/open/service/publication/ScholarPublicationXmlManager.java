/**
 * 
 */
package com.smate.center.open.service.publication;

import com.smate.center.open.form.PublicationForm;
import com.smate.center.open.model.publication.PubXmlDocument;

/**
 * 
 * 
 * @author yamingd 成果XML处理服务
 */
public interface ScholarPublicationXmlManager {
  /**
   * 加载成果XML.
   * 
   * @param form 成果PublicationForm
   * @return String (成果XML串) @ Exception
   */
  PublicationForm loadXml(PublicationForm form) throws Exception;

  /**
   * 获取成果XML.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   * @throws PublicationNotFoundException
   */
  PubXmlDocument getPubXml(Long pubId) throws Exception;
}
