package com.smate.center.batch.service.rol.pub;

import org.dom4j.Element;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PublicationListRol;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * 成果收录情况.
 * 
 * @author LY
 * 
 */
public interface PublicationListRolService {
  /**
   * 保存成果的收录情况.
   * 
   * @throws ServiceException
   */
  public void savePublictionList(PublicationListRol pubList) throws ServiceException;

  /**
   * 查询成果的收录情况.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PublicationListRol getPublicationList(Long pubId) throws ServiceException;

  public PublicationListRol elementConvertPubList(Element node) throws ServiceException;

  public void saveOrUpdatePublictionList(PublicationListRol pubList) throws ServiceException;

  public PublicationListRol wrapPublicationList(String[] pubLists, String[] pubListsSource);

  /**
   * 返回格式如下的字符串:.<br>
   * EI,SCI,ISTP,SSCI
   * 
   * @param pubList
   * @return
   */
  public String convertPubListToString(PublicationListRol pubList);

  /**
   * 返回格式如下的字符串(原始收录，不能更改):.<br>
   * EI,SCI,ISTP,SSCI
   * 
   * @param pubList
   * @return
   */
  public String convertPubListSourceToString(PublicationListRol pubList);

  /**
   * 成果pubList拆分.
   * 
   * @param doc
   * @param pub
   */
  PublicationListRol prasePubList(PubXmlDocument doc);

  /**
   * 成果导入时pubList拆分（包括原始引用情况）.
   * 
   * @param doc
   * @param pub
   */
  PublicationListRol praseSourcePubList(PubXmlDocument doc);

  /**
   * 成果pubList拆分保存
   * 
   * @param remainDoc
   */
  public PublicationListRol praseAndSavePubList(PubXmlDocument remainDoc);

}
