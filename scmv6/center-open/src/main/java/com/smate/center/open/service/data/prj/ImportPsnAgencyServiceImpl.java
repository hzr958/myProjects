package com.smate.center.open.service.data.prj;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;

@Transactional(rollbackFor = Exception.class)
public class ImportPsnAgencyServiceImpl extends ThirdDataTypeBase {
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  public String domainscm;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    if (paramet.get(OpenConsts.MAP_DATA) == null && StringUtils.isEmpty(paramet.get(OpenConsts.MAP_DATA).toString())) {
      logger.error("Open系统-更新单位人员关注资助机构接口-数据不能为空,服务码:siesync4");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_209, paramet, "Open系统-更新单位人员关注资助机构接口-数据不能为空,服务码:siesync4");
      return temp;
    } else {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    }
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(Objects.toString(paramet.get("data")));
    Long insId = NumberUtils.toLong(Objects.toString(dataMap.get("insId")));
    String agencyIdsStr = Objects.toString(dataMap.get("agencyIdsStr"));
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("insId", insId);
    param.add("agencyIdsStr", agencyIdsStr);
    return (Map<String, Object>) restTemplate.postForObject(domainscm + "/prjdata/updatepsnagency", param,
        Object.class);
  }

}
