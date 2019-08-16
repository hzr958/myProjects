package com.smate.web.v8pub.service.query;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.service.pubquery.PubQueryService;
import com.smate.web.v8pub.vo.PubListResult;

@Transactional(rollbackFor = Exception.class)
public class PubQueryhandlerServiceImpl implements PubQueryhandlerService {

  private Map<String, PubQueryService> serviceMap;

  @Override
  public PubListResult queryPub(PubQueryDTO pubQueryDTO) {
    PubListResult pubListResult = new PubListResult();
    Map<String, Object> checkParams = checkParams(pubQueryDTO);
    if (checkParams == null) {
      PubQueryService queryService = serviceMap.get(pubQueryDTO.getServiceType());
      pubListResult = queryService.handleData(pubQueryDTO);
    } else {
      pubListResult.setMsg(checkParams.get(V8pubConst.ERROR_MSG).toString());
      pubListResult.setStatus(V8pubConst.ERROR);
    }
    return pubListResult;
  }

  public Map<String, Object> checkParams(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isBlank(pubQueryDTO.getServiceType())) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询成果的服务类型不能为空");
      return map;
    }
    if (serviceMap.get(pubQueryDTO.getServiceType()) == null) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询成果的服务类型错误");
      return map;
    }
    return null;
  }

  public Map<String, PubQueryService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, PubQueryService> serviceMap) {
    this.serviceMap = serviceMap;
  }

}
