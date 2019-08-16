package com.smate.web.mobile.controller.web.psn;

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
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PsnApiConsts;

/**
 * 获取人员信息
 * 
 * @author wsn
 * @date Apr 29, 2019
 */
@Controller
public class PsnDataController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainScm;


  /**
   * 个人姓名信息
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/psn/mobile/ajaxnames", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object findPsnNames(String des3PsnId) {
    Map<String, Object> result = new HashMap<String, Object>();
    boolean success = false;
    try {
      if (StringUtils.isNotBlank(des3PsnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        HashMap<String, Object> nameMap = new HashMap<>();
        params.add("des3PsnId", des3PsnId);
        nameMap = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.GET_PSNINFO_NAMES,
            RestUtils.buildPostRequestEntity(params), Map.class);
        if ("success".equals(nameMap.get("result"))) {
          result = (HashMap<String, Object>) nameMap;
          success = true;
        }
      }
    } catch (Exception e) {
      logger.error("获取人员姓名信息异常,psnId={}", des3PsnId, e);
    }
    if (!success) {
      result.put("result", "error");
    }
    return result;
  }


  /**
   * 进入移动端系统设置页面
   * 
   * @return
   */
  @RequestMapping("/psn/sys/settings")
  public ModelAndView showSysSettings() {
    ModelAndView view = new ModelAndView();
    try {
      view.setViewName("/psn/settings/sys_settings");
    } catch (Exception e) {
      logger.error("进入移动端系统设置页面异常， psnId={}", SecurityUtils.getCurrentUserId(), e);
    }
    return view;
  }


  /**
   * 获取人员账号信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/psn/ajaxaccount", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object findCurrentPsnAccount() {
    Map<String, Object> result = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.GET_PSNINFO_ACCOUNT,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("获取人员账号信息异常， psnId={}", psnId, e);
    }
    return result;
  }

}
