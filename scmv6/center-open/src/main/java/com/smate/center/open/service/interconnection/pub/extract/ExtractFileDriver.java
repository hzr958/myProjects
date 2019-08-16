package com.smate.center.open.service.interconnection.pub.extract;

import java.util.Map;

/**
 * 
 * @author AiJiangBin
 *
 */
public interface ExtractFileDriver {
  public static final int BUFFER_SIZE = 15 * 1024;

  /**
   * Isis 成果数据
   */
  public static final String EXTRACT_FILE_ISIS_PUB = "ISIS_PUB";

  public String extract(Map<Integer, Map<String, Object>> resultMap);


  public String getDbType();
}
