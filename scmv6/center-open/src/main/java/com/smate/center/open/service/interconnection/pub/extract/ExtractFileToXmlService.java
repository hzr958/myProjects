/**
 * 
 */
package com.smate.center.open.service.interconnection.pub.extract;

import java.util.Map;

/**
 * 
 * @author AiJiangBin
 *
 */
public interface ExtractFileToXmlService {

  /**
   * 通过 xsl模板 生成 xml格式的字符串
   * 
   * @param resultMap
   * @param fileName
   * @return
   */
  public String extract(Map<Integer, Map<String, Object>> resultMap, String fileName);
}
