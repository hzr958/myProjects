package com.smate.center.task.service.solrindex;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public class AllIndexHandleServiceImpl implements AllIndexHandleService {
  private Map<String, AllIndexService> serviceMap;

  @Override
  public void runIndex(IndexInfoVO indexInfo) {
    Map<String, Object> checkParams = checkParams(indexInfo);
    if (checkParams == null) {
      AllIndexService queryService = serviceMap.get(indexInfo.getServiceType());
      queryService.indexHandleByType(indexInfo);
    } else {
      indexInfo.setMsg(checkParams.get("msg").toString());
      indexInfo.setStatus("error");
    }
  }

  public Map<String, Object> checkParams(IndexInfoVO indexInfo) {
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isBlank(indexInfo.getServiceType())) {
      map.put("status", "error");
      map.put("msg", "查询成果的服务类型不能为空");
      return map;
    }
    if (serviceMap.get(indexInfo.getServiceType()) == null) {
      map.put("status", "error");
      map.put("msg", "查询成果的服务类型错误");
      return map;
    }
    return null;
  }

  public Map<String, AllIndexService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, AllIndexService> serviceMap) {
    this.serviceMap = serviceMap;
  }



}
