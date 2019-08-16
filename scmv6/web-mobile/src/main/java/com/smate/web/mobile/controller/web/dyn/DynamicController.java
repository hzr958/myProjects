package com.smate.web.mobile.controller.web.dyn;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.consts.DynamicConstant;
import com.smate.web.mobile.dyn.vo.SmateDynVO;
import com.smate.web.mobile.utils.RestUtils;

@Controller
public class DynamicController {

  private static final Logger logger = LoggerFactory.getLogger(DynamicController.class);
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private RestTemplate restTemplate;

  /**
   * 获取资源的删除状态
   * 
   * @param vo
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/dyn/mobile/ajaxstatus", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object findSelectedGrpInfo(SmateDynVO vo) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(vo.getDes3ResId()) && StringUtils.isNotBlank(vo.getResType())) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3ResId", vo.getDes3ResId());
        params.add("resType", vo.getResType());
        result = restTemplate.postForObject(domainMobile + DynamicConstant.CHECK_RES_STATUS_API,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端获取资源删除状态信息异常， psnId={}, resId={}, resType={}", psnId, vo.getDes3ResId(), vo.getResType(), e);
      result.put("status", "error");
    }
    return result;
  }
}
