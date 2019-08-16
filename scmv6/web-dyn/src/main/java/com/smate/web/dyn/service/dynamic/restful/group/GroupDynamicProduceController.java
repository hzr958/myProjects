package com.smate.web.dyn.service.dynamic.restful.group;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smate.web.dyn.service.dynamic.group.GroupDynamicRealtimeService;

/**
 * 群组动态动态生成 restful接口
 * 
 * @author tsz
 *
 */
@RestController
public class GroupDynamicProduceController {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupDynamicRealtimeService groupDynamicRealtimeService;

  @RequestMapping(value = "/groupdynamicprodece", method = RequestMethod.POST)
  public String getScmOpenData(@RequestBody Map<String, Object> mapData) {
    try {
      groupDynamicRealtimeService.groupDynRealtime(mapData);
    } catch (Exception e) {
      logger.error("生成群组动态异常=============" + mapData, e);
      return "error";
    }
    return "success";
  }


}
