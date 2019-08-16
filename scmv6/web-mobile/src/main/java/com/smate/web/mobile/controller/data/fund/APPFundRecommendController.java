package com.smate.web.mobile.controller.data.fund;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PrjApiConsts;

@RestController
public class APPFundRecommendController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private HttpServletRequest request;

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/getfundrecommendpsnagency", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getFundRecommendPsnAgency() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("encryptedPsnId", Des3Utils.encodeToDes3(psnId.toString()));
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_RECOMMEND_AGENCY, params, Map.class);

    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), (Integer) map.get("total"),
          AppActionUtils.changeResultStatus("success"), Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/savefundrecommendpsnagency", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object saveFundRecommendPsnAgency(String saveAgencyIds) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    Map<String, Object> map = new HashMap<>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      params.add("encryptedPsnId", Des3Utils.encodeToDes3(psnId.toString()));
      params.add("saveAgencyIds", saveAgencyIds);
      map = restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_RECOMMEND_SAVE_AGENCY,
          RestUtils.buildPostRequestEntity(params), Map.class);
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("显示基金科技领域弹出框出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus((String) map.get("status")),
        Objects.toString(map.get("errmsg"), ""));
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/editfundrecommendpsnagency", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object editFundRecommendPsnAgency() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    Map<String, Object> map = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("encryptedPsnId", Des3Utils.encodeToDes3(psnId.toString()));
    map = restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_RECOMMEND_EDIT_AGENCY,
        RestUtils.buildPostRequestEntity(params), Map.class);

    if (map != null && "success".equals(map.get("status"))) {
      Map<String, Object> result = new HashMap<>();
      result.put("fundAgencyInterestList", map.get("fundAgencyInterestList"));// 设置的资助机构
      result.put("psnAgencyIds", map.get("psnAgencyIds"));// 设置的资助机构id，多个用逗号分隔
      result.put("fundAgencyMapList", map.get("fundAgencyMapList"));// 全部的地区下的资助机构
      return AppActionUtils.buildReturnInfo(result, (Integer) map.get("total"),
          AppActionUtils.changeResultStatus("success"), Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 动态首页基金推荐接口
   * 
   * @param pubRecommendVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/getfundrecommendshowindyn", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getDynPubRecommend(String pageNo, String des3FundIds) {
    Map<String, Object> map = new HashMap<>();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0 && StringUtils.isNoneBlank(pageNo)) {
        params.add("psnId", psnId.toString());
        params.add("pageNo", pageNo);
        params.add("des3FundIds", Objects.toString(des3FundIds, ""));
        map = restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_GET_FUNDRECOMMENDS_DYN,
            RestUtils.buildPostRequestEntity(params), Map.class);

      } else {
        map.put("status", "error");
        map.put("errmsg", "获取不到psnId或pageNo");
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("显示动态首页的基金推荐出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return AppActionUtils.buildReturnInfo(map, 0, AppActionUtils.changeResultStatus((String) map.get("status")),
        Objects.toString(map.get("errmsg"), ""));
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/funduninterested", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object fundUnInterested(String des3FundId) {
    Map<String, Object> map = new HashMap<>();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0 && StringUtils.isNoneBlank(des3FundId)) {
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("des3FundId", des3FundId);
        map = restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUND_RECOMMEND_UNINTERESTED,
            RestUtils.buildPostRequestEntity(params), Map.class);

      } else {
        map.put("status", "error");
        map.put("errmsg", "获取不到psnId或者des3FundId为空");
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("对某个基金推荐不敢兴趣操作出错， psnId = {},des3fundId={}", SecurityUtils.getCurrentUserId(), des3FundId, e);
    }
    return AppActionUtils.buildReturnInfo(map, 0, AppActionUtils.changeResultStatus((String) map.get("status")),
        Objects.toString(map.get("errmsg"), ""));
  }
}

