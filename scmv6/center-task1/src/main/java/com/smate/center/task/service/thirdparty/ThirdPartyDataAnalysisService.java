package com.smate.center.task.service.thirdparty;

import java.util.Map;

/**
 * 业务系统提供数据解析 服务接口.
 * 
 * @author tsz
 *
 */
public interface ThirdPartyDataAnalysisService {

  /**
   * 保存数据。
   * 
   * @throws Exception
   */
  public void handle(String token, Map<String, Object> sourcesData) throws Exception;

}
