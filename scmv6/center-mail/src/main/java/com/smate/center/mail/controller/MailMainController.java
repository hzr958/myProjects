package com.smate.center.mail.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.center.mail.connector.service.MailHandleOriginalDataService;

/**
 * 控制类 主要控制器
 * 
 * @author tsz
 *
 */
@Controller
@RequestMapping("/mail")
public class MailMainController {
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;

  @RequestMapping("main")
  public String index() {

    return "main";
  }

  @SuppressWarnings({})
  @RequestMapping(value = "send", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, String> sendMail(@RequestBody Map<String, String> paramData) throws Exception {
    Map<String, String> resultMap = mailHandleOriginalDataService.doHandle(paramData);
    return resultMap;
  }

}
