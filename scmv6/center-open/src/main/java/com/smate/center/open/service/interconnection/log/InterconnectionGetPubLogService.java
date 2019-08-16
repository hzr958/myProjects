package com.smate.center.open.service.interconnection.log;

import java.util.Map;

/**
 * 记录成果拉取 日志
 * 
 * @author tsz
 *
 */
public interface InterconnectionGetPubLogService {

  /**
   * 保存日志
   * 
   * @throws Exception
   */
  public void saveGetPubLog(int accessType, int nums, Map<String, Object> dataParamet) throws Exception;
}
