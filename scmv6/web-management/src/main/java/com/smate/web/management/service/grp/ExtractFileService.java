package com.smate.web.management.service.grp;

import java.io.File;
import java.util.Map;

/**
 * 解析文件service
 */
public interface ExtractFileService {

  /**
   *
   *  resutlMap.put("list", "");   项目列表
   *  resutlMap.put("warnmsg", "");   错误信息
   *  resutlMap.put("count", 0);      条数
   * @return
   */
  public Map<String ,Object> extractFile(File file, String sourceFileFileName);
}
