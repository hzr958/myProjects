package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import org.dom4j.Element;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.PubList;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 成果收录接口
 * 
 * @author jszhou
 */
public interface PubListService {
  public List<PubList> getAllPubList();

  /**
   * 保存成果的收录情况.
   * 
   * @throws ServiceException
   */
  public void savePubList(PubList pubList) throws SysServiceException;

  /**
   * 查询成果的收录情况.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PubList getPubList(Long pubId) throws SysServiceException;

  public PubList elementConvertPubList(Element node) throws SysServiceException;

  public void saveOrUpdatePubList(PubList pubList) throws SysServiceException;

  public PubList wrapPubList(String[] pubLists, String[] pubListsSource);

  /**
   * 返回格式如下的字符串:.<br>
   * EI,SCI,ISTP,SSCI
   * 
   * @param pubList
   * @return
   */
  public String convertPubListToString(PubList pubList);

  /**
   * 返回格式如下的字符串(原始收录，不能更改):.<br>
   * EI,SCI,ISTP,SSCI
   * 
   * @param pubList
   * @return
   */
  public String convertPubListSourceToString(PubList pubList);


  /**
   * 成果导入时pubList拆分（包括原始引用情况）.
   * 
   * @param doc
   * @param pub
   */
  PubList praseSourcePubList(PubXmlDocument doc);

  /**
   * 成果pubList拆分.
   * 
   * @param pubJson
   * @return
   */
  PubList prasePubList(PubJsonDTO pubJson);
}
