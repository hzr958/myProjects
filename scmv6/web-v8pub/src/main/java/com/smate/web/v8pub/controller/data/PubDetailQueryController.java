package com.smate.web.v8pub.controller.data;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.web.v8pub.service.pubdetailquery.PubDetailHandleService;

/**
 * 成果详情查询的
 * 
 * @author aijiangbin
 * @date 2018年8月3日
 */
@Controller
public class PubDetailQueryController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDetailHandleService pubDetailHandleService;

  /**
   * 成果详情 入口 需要两个参数 pubId 和 serviceType
   * 
   * @param params
   * @return
   */
  @RequestMapping(value = "/data/pub/query/detail", method = RequestMethod.POST,
      produces = "application/json; charset=UTF-8")
  @ResponseBody
  public Object query(@RequestBody Map<String, Object> params) {
    Map<String, String> checkMap = null;
    try {
      checkMap = pubDetailHandleService.checkParmas(params);
      if (checkMap == null) {
        PubDetailVO pubDetailVO = pubDetailHandleService.queryPubDetail(params);
        return pubDetailVO;
      }
    } catch (Exception e) {
      logger.error("查询成果详情 异常", e);
    }

    return checkMap;
  }

  public static void main(String[] args) {

  }

}
