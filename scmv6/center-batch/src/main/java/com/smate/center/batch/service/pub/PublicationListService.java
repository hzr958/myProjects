package com.smate.center.batch.service.pub;

import org.dom4j.Element;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * 成果收录情况.
 * 
 * @author LY
 * 
 */
public interface PublicationListService {
  /**
   * 保存成果的收录情况.
   * 
   * @throws ServiceException
   */
  public void savePublictionList(PublicationList pubList) throws ServiceException;

  /**
   * 查询成果的收录情况.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PublicationList getPublicationList(Long pubId) throws ServiceException;

  public PublicationList elementConvertPubList(Element node) throws ServiceException;

  public void saveOrUpdatePublictionList(PublicationList pubList) throws ServiceException;

  public PublicationList wrapPublicationList(String[] pubLists, String[] pubListsSource);

  /**
   * 返回格式如下的字符串:.<br>
   * EI,SCI,ISTP,SSCI
   * 
   * @param pubList
   * @return
   */
  public String convertPubListToString(PublicationList pubList);

  /**
   * 返回格式如下的字符串(原始收录，不能更改):.<br>
   * EI,SCI,ISTP,SSCI
   * 
   * @param pubList
   * @return
   */
  public String convertPubListSourceToString(PublicationList pubList);

  /**
   * 成果pubList拆分.
   * 
   * @param doc
   * @param pub
   */
  PublicationList prasePubList(PubXmlDocument doc);

  /**
   * 成果导入时pubList拆分（包括原始引用情况）.
   * 
   * @param doc
   * @param pub
   */
  PublicationList praseSourcePubList(PubXmlDocument doc);
}
