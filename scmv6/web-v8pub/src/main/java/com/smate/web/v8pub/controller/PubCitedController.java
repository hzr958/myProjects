package com.smate.web.v8pub.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.service.searchimport.PubCitedService;
import com.smate.web.v8pub.vo.searchimport.PubCitedVo;

/**
 * 成果引用
 * 
 * @author wsn
 * @date 2018年8月28日
 */
@Controller
public class PubCitedController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubCitedService pubCitedService;

  /**
   * 返回更新引用插件所需的指定参数.
   * 
   * @return JSON
   * @throws Exception
   */
  @RequestMapping(value = "/pub/cite/ajaxparams", method = RequestMethod.POST)
  @ResponseBody
  public String findUpdateParams(@ModelAttribute PubCitedVo pubCitedVo) {
    Map<String, String> result = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      pubCitedVo.setPsnId(psnId);
      String citedParams = pubCitedService.getUpdatePubCitedParams(pubCitedVo);
      result.put("result", "success");
      result.put("params", citedParams);
    } catch (Exception e) {
      logger.error("获取更新引用所需的指定参数出错，psnId = " + psnId, e);
      result.put("result", "error");
    }
    return JacksonUtils.jsonMapSerializer(result);
  }

  /**
   * 更新成果引用数数据
   * 
   * @param pubCitedVo
   * @return
   */
  @RequestMapping(value = "/pub/cite/ajaxsave", method = RequestMethod.POST)
  @ResponseBody
  public String savePubCitations(@ModelAttribute PubCitedVo pubCitedVo) {
    Map<String, String> result = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      String resultStr = "success";
      pubCitedVo.setPsnId(psnId);
      String updateResult = pubCitedService.updatePubCited(pubCitedVo);
      if (StringUtils.isNotBlank(updateResult)) {
        Map<String, Object> statusMap = JacksonUtils.json2HashMap(updateResult);
        resultStr = Objects.toString(statusMap.get("status"), "error");
      }
      result.put("result", resultStr);
    } catch (Exception e) {
      logger.error("更新成果引用数据出错，psnId = " + psnId, e);
      result.put("result", "error");
    }
    return JacksonUtils.jsonMapSerializer(result);
  }

}
