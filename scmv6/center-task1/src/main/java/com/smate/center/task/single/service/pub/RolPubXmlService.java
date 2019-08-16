package com.smate.center.task.single.service.pub;

import java.io.Serializable;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.RolPubXml;


/**
 * 成果XML服务.
 * 
 * @author liqinghua
 * 
 */
public interface RolPubXmlService extends Serializable {

  /**
   * 保存成果XML.
   * 
   * @param pubId
   * @param xml
   * @return
   * @throws ServiceException
   */
  public RolPubXml savePubXml(Long pubId, String xml) throws ServiceException;

  /**
   * 获取成果XML.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public RolPubXml getPubXml(Long pubId) throws ServiceException;

  /**
   * 添加成果XML为空的记录.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void saveRolPubXmlEmpty(Long pubId) throws ServiceException;
}
