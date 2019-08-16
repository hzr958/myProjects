package com.smate.core.base.psn.service.vip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

@Service("personVipService")
public class PersonVipServiceImpl implements PersonVipService {

  @Value("${domainscm}")
  private String domainscm;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Autowired
  private RestTemplate restTemplate;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @SuppressWarnings("unchecked")
  @Override
  public boolean checkVIPByPsnId(Long psnId) throws ServiceException {
    /**
     * 数据返回格式："{\"result\":[{\"endTime\":\"\",\"status\":\"0\"}],\"openid\":\"99999999\",\"time\":10520,\"status\":\"success\"}"
     */
    try {
      Object resultData = restTemplate.postForObject(this.openResfulUrl, buildParams(psnId), Object.class);
      Map<String, Object> resultMap = JacksonUtils.json2HashMap(resultData.toString());
      if (resultMap != null) {
        if ("success".equals(resultMap.get("status"))) {
          List<Map<String, Object>> mapList = (List<Map<String, Object>>) resultMap.get("result");
          Object obj = mapList.get(0).get("status");
          int status = Integer.parseInt(obj == null ? "0" : String.valueOf(obj));
          return status == 1;
        } else {
          logger.error("项目助理接口获取人员VIP情况调用失败！原因：{}", resultMap.get("msg"));
        }
      }
    } catch (Exception e) {
      logger.error("获取人员VIP情况出错！psnId={}", psnId, e);
    }

    return false;
  }


  private Map<String, Object> buildParams(Long psnId) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("psnId", psnId);
    dataMap.put("appType", "3");// appType=1，开题分析；appType=2科研验证；appType=2项目信息
    dataMap.put("ip", Struts2Utils.getRemoteAddr());// 用户端ip
    map.put("openid", "99999999");
    map.put("token", "00000000kpipayv1");
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

}
