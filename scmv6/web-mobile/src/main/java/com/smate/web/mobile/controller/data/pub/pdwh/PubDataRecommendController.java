package com.smate.web.mobile.controller.data.pub.pdwh;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.service.PubRecommendRestService;
import com.smate.web.mobile.v8pub.vo.pdwh.PubRecommendVO;

/*
 * 成果推荐
 */
@RestController
public class PubDataRecommendController {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRecommendRestService pubRecommendRestService;
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  /**
   * 成果推荐条件设置获取
   * 
   * @param pubQueryDTO
   * @return
   */
  @RequestMapping(value = "/data/pub/recommend/conditional", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pubRecommendConditional(PubQueryDTO pubQueryDTO) {
    PubRecommendVO pubVO = new PubRecommendVO();
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0) {
        pubRecommendRestService.pubRecommendConditional(psnId, pubVO);
        result.put("result", "success");
      } else {
        result.put("errmsg", "psnId is null");
      }
    } catch (Exception e) {
      result.put("result", "error");
      logger.error("获取推荐条件设置出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("defultPubYear", pubVO.getDefultPubYear());
    data.put("defultKeyJson", pubVO.getDefultKeyJson());
    data.put("defultArea", pubVO.getDefultArea());
    data.put("defultPubType", pubVO.getDefultPubType());
    data.put("areaList", pubVO.getAreaList());
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("result"), "error")),
        Objects.toString(result.get("errmsg"), ""));
  }

  /**
   * 论文推荐查询接口
   * 
   * @param pubQueryDTO
   * @return
   */
  @RequestMapping(value = "/data/pubrecommend/pubList", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object searchPubList(PubRecommendVO pubRecommendVO) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (!NumberUtils.isNullOrZero(psnId)) {
        pubRecommendVO.setDes3PsnId(Des3Utils.encodeToDes3(psnId.toString()));
        pubRecommendRestService.searchPubList(pubRecommendVO);
        result.put("result", "success");
      } else {
        result.put("errmsg", "psnId is null");
      }
    } catch (Exception e) {
      result.put("result", "error");
      logger.error("查询推荐论文列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return AppActionUtils.buildReturnInfo(pubRecommendVO.getPage().getResult(),
        NumberUtils.parseInt(ObjectUtils.toString(pubRecommendVO.getPage().getTotalCount(), "0")),
        AppActionUtils.changeResultStatus(Objects.toString(result.get("result"), "error")),
        Objects.toString(result.get("errmsg"), ""));
  }

  /**
   * 添加科技领域
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pubrecommend/addScienAreas", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object addScienArea(PubRecommendVO pubRecommendVO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String addPsnAreaCode = pubRecommendVO.getAddPsnAreaCode();
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(addPsnAreaCode)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("addPsnAreaCodeList", addPsnAreaCode);
        resultMap = restTemplate.postForObject(domainscm + PubApiInfoConsts.ADD_SCIENAREA,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } else {
        resultMap.put("errmsg", "psnId is null or addPsnAreaCodes is null");
      }
    } catch (Exception e) {
      logger.error("论文科技领域出错， psnId = " + psnId, e);
      resultMap.put("result", "error");
    }
    return AppActionUtils.buildReturnInfo(resultMap, 0,
        AppActionUtils.changeResultStatus(Objects.toString(resultMap.get("result"), "error")),
        Objects.toString(resultMap.get("errmsg"), ""));
  }

  /**
   * 删除科技领域
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/recommend/deleteScienAreas", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object deleteScienArea(PubRecommendVO pubRecommendVO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String deletePsnAreaCodes = pubRecommendVO.getDeletePsnAreaCode();
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(deletePsnAreaCodes)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("deletePsnAreaCodeList", deletePsnAreaCodes);
        resultMap = restTemplate.postForObject(domainscm + PubApiInfoConsts.DELETE_SCIENAREA,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } else {
        resultMap.put("errmsg", "psnId is null or deletePsnAreaCodes is null");
      }
    } catch (Exception e) {
      logger.error("论文科技领域出错， psnId = " + psnId, e);
      resultMap.put("result", "error");
    }
    return AppActionUtils.buildReturnInfo(resultMap, 0,
        AppActionUtils.changeResultStatus(Objects.toString(resultMap.get("result"), "error")),
        Objects.toString(resultMap.get("errmsg"), ""));
  }

  /**
   * 增加关键词
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/recommend/addKeyWords", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object addKeyWord(PubRecommendVO pubRecommendVO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String addPsnKeyWords = pubRecommendVO.getAddPsnKeyWord();
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(addPsnKeyWords)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("addPsnKeyWord", addPsnKeyWords);// 单个
        resultMap = restTemplate.postForObject(domainscm + PubApiInfoConsts.ADD_KEYWORD,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } else {
        resultMap.put("errMsg", "psnId is null or addPsnKeyWords is null");
      }
    } catch (Exception e) {
      logger.error("论文推荐添加关键词出错， psnId = " + psnId, e);
      resultMap.put("result", "error");
    }
    return AppActionUtils.buildReturnInfo(resultMap, 0,
        AppActionUtils.changeResultStatus(Objects.toString(resultMap.get("result"), "error")),
        Objects.toString(resultMap.get("errMsg"), ""));
  }

  /**
   * 删除关键词
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/recommend/deleteKeyWords", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object deleteKeyWord(PubRecommendVO pubRecommendVO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String deletePsnKeyWords = pubRecommendVO.getDeletePsnKeyWord();
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(deletePsnKeyWords)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("deletePsnKeyWord", deletePsnKeyWords);
        resultMap = restTemplate.postForObject(domainscm + PubApiInfoConsts.DELETE_KEYWORD,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } else {
        resultMap.put("errmsg", "psnId is null or deletePsnKeyWords is null");
      }
    } catch (Exception e) {
      logger.error("论文推荐删除关键词出错， psnId = " + psnId, e);
      resultMap.put("result", "error");
    }
    return AppActionUtils.buildReturnInfo(resultMap, 0,
        AppActionUtils.changeResultStatus(Objects.toString(resultMap.get("result"), "error")),
        Objects.toString(resultMap.get("errmsg"), ""));
  }

  /**
   * 动态首页论文推荐
   * 
   * @param pubRecommendVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/getdynpubrecommend", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getDynPubRecommend(PubRecommendVO pubRecommendVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    Map<String, Object> map = new HashMap<>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0 && StringUtils.isNotBlank(pubRecommendVO.getPageNo())) {
        pubRecommendVO.setPsnId(psnId);
        params.add("psnId", psnId.toString());
        /*
         * map = restTemplate.postForObject(domainscm + PubApiInfoConsts.GET_PUB_RECOMMEND_DYN,
         * RestUtils.buildPostRequestEntity(params), Map.class);
         */
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.GET_PUB_RECOMMEND_DYN, pubRecommendVO, Map.class);

      } else {
        map.put("status", "error");
        map.put("errmsg", "获取不到psnId或者pageNo");
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("显示动态首页的论文推荐出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return AppActionUtils.buildReturnInfo(map, 0, AppActionUtils.changeResultStatus((String) map.get("status")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 动态首页论文推荐不感兴趣
   * 
   * @param pubOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/pubuninterested", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object notViewPubRecommend(PubRecommendVO pubRecommendVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId > 0L) {
      try {
        pubRecommendVO.setPubId(NumberUtils.toLong(Des3Utils.decodeFromDes3(pubRecommendVO.getDes3PubId()), 0L));
        pubRecommendVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.NOT_VIEW_PDWH, pubRecommendVO, Map.class);
      } catch (Exception e) {
        logger.error("mobile首页动态推荐论文不感兴趣操作出错,psnId=" + psnId + ",pubId=" + pubRecommendVO.getPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobilepsnId,psnId={},pubId={}", psnId, pubRecommendVO);
      map.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(map, 0, AppActionUtils.changeResultStatus((String) map.get("result")),
        Objects.toString(map.get("errmsg"), ""));
  }


}
