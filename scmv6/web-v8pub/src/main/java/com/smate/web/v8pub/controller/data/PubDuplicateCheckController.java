package com.smate.web.v8pub.controller.data;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.service.findduplicate.PubDuplicateCheckService;
import com.smate.web.v8pub.service.findduplicate.PubDuplicateDTO;

/**
 * 成果查重数据接口
 * 
 * @author tsz
 *
 * @date 2018年8月16日
 */
@RestController
public class PubDuplicateCheckController {

  @Autowired
  private PubDuplicateCheckService pubDuplicateCheckService;

  @PostMapping(value = "/data/pub/dupcheck", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public Map<String, String> findDuplicatePub(@RequestBody String jsonData) {
    PubDuplicateDTO dup = JacksonUtils.jsonObject(jsonData, PubDuplicateDTO.class);
    Map<String, String> result = pubDuplicateCheckService.dupCheck(dup);
    return result;
  }

}
