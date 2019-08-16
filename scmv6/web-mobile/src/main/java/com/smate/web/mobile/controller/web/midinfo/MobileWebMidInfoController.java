package com.smate.web.mobile.controller.web.midinfo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 移动端中间信息页面.
 * 
 * @author tsz
 *
 */
@Controller
public class MobileWebMidInfoController {

  /**
   * 基金发现界面
   * 
   * @param model
   * @return
   */
  @RequestMapping(value = "/m/midinfo")
  public ModelAndView showFundAgencyList(String targetUrl) {
    ModelAndView model = new ModelAndView();
    model.addObject("targetUrl", targetUrl);
    model.setViewName("/midinfo/midinfo");
    return model;
  }


}
