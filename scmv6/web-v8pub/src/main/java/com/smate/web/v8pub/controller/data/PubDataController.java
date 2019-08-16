package com.smate.web.v8pub.controller.data;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubDataService;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

@Controller
public class PubDataController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDataService pubDataService;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Resource(name = "asOpenDynamicProduceImpl")
  private PubHandlerAssemblyService asOpenDynamicProduceImpl;

  @RequestMapping(value = "/data/pub/saveorupdate", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String save(@RequestBody String jsonData) {
    PubDTO pub = JacksonUtils.jsonObject(jsonData, PubDTO.class);
    String result = pubDataService.pubHandleByType(pub);
    // 保存成果时，要发动态
    if (pub.pubHandlerName.equals("saveSnsPubHandler")) {
      asOpenDynamicProduceImpl.excute(pub);
    }
    return result;
  }

}
