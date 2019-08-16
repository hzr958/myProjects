package com.smate.web.mobile.controller.web.pub;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 一些比较公共一点的操作
 * 
 * @author wsn
 * @date Oct 17, 2018
 */
@Controller
public class CommonOperationController {

  /**
   * 判断系统是否超时
   * 
   * @author ChuanjieHou
   * @date 2017年9月14日
   * @return
   */
  @RequestMapping("/pub/ajaxtimeout")
  @ResponseBody
  public Object ajaxTimeout() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    return map;
  }
}
