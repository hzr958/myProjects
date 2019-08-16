package com.smate.web.v8pub.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.service.query.PubCountHandlerService;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubCountResult;

/**
 * 成果统计数查询 查询的参数和查询成果列表的参数相同
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
@Controller
public class PubCountQueryController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubCountHandlerService pubCountHandlerService;


  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @RequestMapping(value = "/pub/query/ajaxpsnpubcount", method = RequestMethod.POST,
      produces = "application/json; charset=UTF-8")
  @ResponseBody
  public String querySnsPubCount(PubQueryDTO pubQueryDTO) {
    PubCountResult pubCountResult = new PubCountResult();
    try {
      if (pubQueryDTO.getSearchPsnId() != null && pubQueryDTO.getSearchPsnId() != 0L) {
        Map<String, Object> count = pubCountHandlerService.querySnsPubCount(pubQueryDTO);
        pubCountResult.setResultCount(count);
        pubCountResult.setStatus(V8pubConst.SUCCESS);
      } else {
        pubCountResult.setStatus(V8pubConst.ERROR);
        pubCountResult.setMsg("searchPsnId 不能为空");
      }
    } catch (Exception e) {
      pubCountResult.setStatus(V8pubConst.ERROR);
      pubCountResult.setMsg("系统异常,稍后再试");
      logger.error("系统异常", e);
    }
    String result = JacksonUtils.jsonObjectSerializer(pubCountResult.getResultCount());
    return result;
  }

  @RequestMapping(value = "/pub/query/ajaxgrppubcount", method = RequestMethod.POST,
      produces = "application/json; charset=UTF-8")
  @ResponseBody
  public String queryGrpPubCount(PubQueryDTO pubQueryDTO) {
    PubCountResult pubCountResult = new PubCountResult();
    String isPsnPubs = SpringUtils.getRequest().getParameter("isPsnPubs");
    String des3PsnId = SpringUtils.getRequest().getParameter("des3PsnId");
    if (StringUtils.isNotBlank(des3PsnId)) {
      pubQueryDTO.setDes3SearchPsnId(des3PsnId);
    }
    if ("1".equals(isPsnPubs)) {
      // 群组查询个人公开成果
      pubQueryDTO.setImportGrpMemberPubs(true);
      return querySnsPubCount(pubQueryDTO);
    }
    buildParams(pubQueryDTO);

    try {
      if (pubQueryDTO.getSearchGrpId() != null && pubQueryDTO.getSearchGrpId() != 0L) {
        Map<String, Object> count = pubCountHandlerService.queryGrpPubCount(pubQueryDTO);
        pubCountResult.setResultCount(count);
        pubCountResult.setStatus(V8pubConst.SUCCESS);
      } else {
        pubCountResult.setStatus(V8pubConst.ERROR);
        pubCountResult.setMsg("searchGrpId 不能为空");
      }
    } catch (Exception e) {
      pubCountResult.setStatus(V8pubConst.ERROR);
      pubCountResult.setMsg("系统异常,稍后再试");
      logger.error("系统异常", e);
    }
    String result = JacksonUtils.jsonObjectSerializer(pubCountResult.getResultCount());
    return result;
  }

  public void buildParams(PubQueryDTO pubQueryDTO) {
    String showPrjPub = SpringUtils.getRequest().getParameter("showPrjPub");
    String showRefPub = SpringUtils.getRequest().getParameter("showRefPub");
    if ("1".equals(showPrjPub)) {
      pubQueryDTO.setSearchPrjPub(true);
    }
    if ("1".equals(showRefPub)) {
      pubQueryDTO.setSearchRefPub(true);
    }
  }
}
