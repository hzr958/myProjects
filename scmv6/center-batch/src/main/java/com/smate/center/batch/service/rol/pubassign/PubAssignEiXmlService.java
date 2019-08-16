package com.smate.center.batch.service.rol.pubassign;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * 成果指派XML拆分服务.
 * 
 * @author liqinghua
 * 
 */
public interface PubAssignEiXmlService extends Serializable {

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
   * @param fullName
   * @param insId
   * @param seqNo
   * @throws ServiceException
   */
  public void savePubAuthor(Long pubId, Long pubInsId, String initName, String fullName, Long insId, Integer seqNo)
      throws ServiceException;

  /**
   * 保存导入成果会议数据.
   * 
   * @param name
   * @param pubId
   * @param pubInsId
   * @throws ServiceException
   */
  public void savePubConference(String name, Long pubId, Long pubInsId) throws ServiceException;

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
   * 保存成果期刊信息.
   * 
   * @param jid
   * @param jname
   * @param issn
   * @param pubId
   * @param pubInsId
   * @throws ServiceException
   */
  public void savePubJournal(Long jid, String jname, String issn, Long pubId, Long pubInsId) throws ServiceException;

  /**
   * 保存成果关键词.
   * 
   * @param keywords
   * @param pubId
   * @param pubInsId
   * @throws ServiceException
   */
  public void savePubKeywords(String keywords, Long pubId, Long pubInsId) throws ServiceException;

  /**
   * 保存成果作者EMAIL.
   * 
   * @param email
   * @param pubId
   * @param pubInsId
   * @param insId TODO
   * @throws ServiceException
   */
  public void savePubEmail(String email, Long pubId, Long pubInsId, Long insId, Integer seqNo) throws ServiceException;
}
