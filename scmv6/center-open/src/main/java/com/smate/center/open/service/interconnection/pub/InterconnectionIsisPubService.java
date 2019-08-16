package com.smate.center.open.service.interconnection.pub;

import java.util.Map;

import org.dom4j.Element;

import com.smate.center.open.model.pdwh.pub.PdwhPubXml;
import com.smate.core.base.pub.vo.PubDetailVO;

/**
 * 获取Isis成果服务
 * 
 * @author AiJiangBin
 *
 */
public interface InterconnectionIsisPubService {

  public void fillCommonElement(Element pubElement, String pubXml);

  /**
   *
   * @param pubDataStore
   * @return
   * @throws Exception
   */

  public Map<String, Object> parseXmlToMap1(PubDetailVO pubDetailVO) throws Exception;

  /**
   * 解析基准库的成果
   * 
   * @param pubDataStore
   * @return
   * @throws Exception
   */

  public Map<String, Object> parsePdwhXmlXmlToMap1(PubDetailVO pubDetailVO, Long ownPsnId) throws Exception;

}
