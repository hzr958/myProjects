package com.smate.center.batch.service.rol.pubassign;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * CnkiPat成果指派XML拆分服务.
 * 
 * @author liqinghua
 * 
 */
public interface PubAssignCnkiPatXmlService extends Serializable {

  /**
   * 导入成果展开指派数据.
   * 
   * @param xmlDocument
   * @param context
   * @throws ServiceException
   */
  public void extractAssignData(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException;

  /**
   * 判断是否已经展开过成果指派XML.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public boolean isExtractAssignData(Long pubId) throws ServiceException;

  /**
   * 保存导入成果展开作者数据.
   * 
   * @param pubId
   * @param pubInsId
   * @param name
   * @param insId
   * @param seqNo
   * @throws ServiceException
   */
  public void savePubAuthor(Long pubId, Long pubInsId, String name, Long insId, Integer seqNo) throws ServiceException;

  /**
   * 保存成果部门信息.
   * 
   * @param dept
   * @param pubId
   * @param pubInsId
   * @param seqNo TODO
   * @param insId TODO
   * @throws ServiceException
   */
  public void savePubDept(String dept, Long pubId, Long pubInsId, Integer seqNo, Long insId) throws ServiceException;

  /**
   * 保存成果关键词.
   * 
   * @param keywords
   * @param pubId
   * @param pubInsId
   * @throws ServiceException
   */
  public void savePubKeywords(String keywords, Long pubId, Long pubInsId) throws ServiceException;

}
