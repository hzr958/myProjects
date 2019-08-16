package com.smate.web.v8pub.controller.data;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.query.PubQueryhandlerService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 成果查询入口
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
@Controller
public class PubQueryController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubQueryhandlerService pubQueryhandlerService;
  @Autowired
  private PubSnsService pubSnsService;

  @RequestMapping(value = "/data/pub/query/list", method = RequestMethod.POST,
      produces = "application/json; charset=UTF-8")
  @ResponseBody
  public String query(@RequestBody Map<String, Object> queryData) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    PubListResult pubListResult = new PubListResult();
    try {
      ConvertUtils.register(new DateConverter(null), java.util.Date.class);// 处理转换过程中Date为空转换的异常
      BeanUtils.populate(pubQueryDTO, queryData);
      pubListResult = pubQueryhandlerService.queryPub(pubQueryDTO);
    } catch (IllegalAccessException | InvocationTargetException e) {
      pubListResult.setStatus(V8pubConst.ERROR);
      pubListResult.setMsg("解析参数异常：queryData=" + queryData);
      logger.error("解析参数异常：queryData=" + queryData, e);
    } catch (Exception e) {
      pubListResult.setStatus(V8pubConst.ERROR);
      pubListResult.setMsg("系统异常,稍后再试");
      logger.error("系统异常", e);
    }
    String result = JacksonUtils.jsonObjectSerializer(pubListResult);
    return result;
  }


  /**
   * 获取查询出的成果总数和指定成果在查询结果中的位置
   * 
   * @param queryData
   * @return
   */
  @RequestMapping(value = "/data/pub/index", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public String queryPubIndexAndTotalCount(@RequestBody Map<String, Object> queryData) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    Map<String, String> result = new HashMap<String, String>();
    try {
      ConvertUtils.register(new DateConverter(null), java.util.Date.class);// 处理转换过程中Date为空转换的异常
      BeanUtils.populate(pubQueryDTO, queryData);
      pubSnsService.queryPubIndexAndTotalCount(pubQueryDTO);
      result.put("totalCount", Objects.toString(pubQueryDTO.getTotalCount(), "0"));
      result.put("pubIndex", Objects.toString(pubQueryDTO.getPubIndex(), "0"));
      result.put("status", "success");
    } catch (Exception e) {
      logger.error("查询成果顺序下标和总的成果数异常", e);
      result.put("status", "error");
    }
    return JacksonUtils.jsonObjectSerializer(result);
  }

}
