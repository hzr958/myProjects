package com.smate.center.task.service.thirdparty;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方数据处理入口.
 *
 * @author tsz
 *
 */
//@Service("thirdPartyDataHandleService")
@Transactional (rollbackFor = Exception.class)
public class ThirdPartyDataHandleServiceImpl implements ThirdPartyDataHandleService {

  /**
   * 服务类别.
   */
  private Map<String, ThirdPartyDataAnalysisService> serviceMap = new HashMap<String, ThirdPartyDataAnalysisService>();

  public Map<String, ThirdPartyDataAnalysisService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, ThirdPartyDataAnalysisService> serviceMap) {
    this.serviceMap = serviceMap;
  }

  @Override
  public void handle(String sourceToken ,String type, Map<String, Object> sourcesData) throws Exception {
    ThirdPartyDataAnalysisService tpdService = this.serviceMap.get(type);
    if (tpdService == null) {
      throw new Exception("服务类别type 不存在 type=" + type);
    }
    tpdService.handle(sourceToken, sourcesData);
  }
}
