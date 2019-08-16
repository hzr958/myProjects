package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.center.task.model.SieDataPubXml2JsonRefresh;
import com.smate.sie.core.base.utils.model.pub.SiePatXml;
import com.smate.sie.core.base.utils.model.pub.SiePubXml;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/***
 * 成果专利xml转json服务
 * 
 * @author lijianming
 * @Date 20190325
 */
public interface SieFixPubXml2JsonService {

  List<SieDataPubXml2JsonRefresh> getNeedRefreshData(int size);

  void saveHandleFailRefresh(SieDataPubXml2JsonRefresh refresh);

  /**
   * 检查pubId是否存在xml和json数据
   * 
   * @param pubId
   * @return
   */
  public SiePubXml checkPubXmlAndJsonIsExist(Long pubId) throws Exception;

  /**
   * 检查patId是否存在xml和json数据
   * 
   * @param pubId
   * @return
   */
  public SiePatXml checkPatXmlAndJsonIsExist(Long pubId) throws Exception;

  public void savePubXml2JsonRefresh(SieDataPubXml2JsonRefresh pubXml2JsonRefresh) throws Exception;

  /**
   * 保存经转化的成果json数据
   * 
   * @param pubId
   * @param pubJson
   * @throws Exception
   */
  public void savePubJsonData(PubJsonDTO pubJson, Long pubId) throws Exception;

  /**
   * 保存经转化的专利json数据
   * 
   * @param patJson
   * @throws Exception
   */
  public void savePatJsonData(PubJsonDTO patJson, Long pubId) throws Exception;
}
