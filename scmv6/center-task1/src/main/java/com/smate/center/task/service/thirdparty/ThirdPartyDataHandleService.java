package com.smate.center.task.service.thirdparty;

import java.util.Map;

/**
 * 数据处理服务.
 * 
 * @author tsz
 *
 */
public interface ThirdPartyDataHandleService {


  public void handle(String sourceToken, String type, Map<String, Object> sourcesData) throws Exception;
}
