package com.smate.web.v8pub.controller.timeout;

import com.smate.core.base.utils.json.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 校验ajax超时
 * 
 * @author wsn
 * @date 2018年8月13日
 */
@Controller
public class SessionTimeOutController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @RequestMapping(value = "/pub/ajaxtimeout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String ajaxSessionTimeout() {
    String result = "";
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("result", "success");
      result = JacksonUtils.mapToJsonStr(map);
    } catch (Exception e) {
      logger.error("v8Pub校验session超时或未登录出错", e);
    }
    return result;
  }
}
