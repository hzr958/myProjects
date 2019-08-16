package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PublicationXml;

public interface RolPublicationXmlService {
  /**
   * @param pubId
   * @return
   * @throws ServiceException
   */
  PublicationXml getById(Long pubId) throws ServiceException;

  /**
   * 保存成果XML.
   * 
   * @param pubId
   * @param xml
   * @return
   * @throws ServiceException
   */
  PublicationXml save(Long pubId, String xml) throws ServiceException;

}
