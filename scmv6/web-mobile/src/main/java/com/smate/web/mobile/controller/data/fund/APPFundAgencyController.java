package com.smate.web.mobile.controller.data.fund;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.mobile.fund.dto.MobileFundAgencyDTO;
import com.smate.web.mobile.prj.vo.FundOperateVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PrjApiConsts;

@Controller
public class APPFundAgencyController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private HttpServletRequest request;

  /**
   * 资助机构列表
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/fundagencylist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getFundagencylist(HttpServletResponse response, FundOperateVO prjOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if (psnId != null && psnId != 0L) {
        params.add("psnId", psnId.toString());
        params.add("searchKey", prjOperateVO.getSearchKey());
        params.add("regionAgency", prjOperateVO.getRegionAgency());
        params.add("page.pageNo", prjOperateVO.getPage().getParamPageNo().toString());
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_LIST,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("mobile资助机构列表异常,psnId=" + psnId, e);
    }
    if ("success".equals(result.get("status"))) {
      return AppActionUtils.buildReturnInfo(result.get("result"), (Integer) result.get("totalCount"),
          AppActionUtils.changeResultStatus("success"), Objects.toString(result.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(result.get("errmsg"), ""));
    }

  }

  /**
   * 地区条件筛选
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/agencycondition", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getAgencycondition(FundOperateVO prjOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if (psnId != null && psnId != 0L) {
        params.add("searchKey", prjOperateVO.getSearchKey());
        params.add("regionAgency", prjOperateVO.getRegionAgency());
        result =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_CONDITION,
                RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("mobile资助机构地区筛选列表异常,psnId=" + psnId, e);
    }
    if (result != null) {
      return AppActionUtils.buildReturnInfo(result.get("regionList"), (Integer) result.get("totalCount"),
          AppActionUtils.changeResultStatus("success"), Objects.toString(result.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(result.get("errmsg"), ""));
    }
  }

  /**
   * 资助机构详情--基金列表
   * 
   * @param prjOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/agencydetail", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getagencydetail(FundOperateVO prjOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if (psnId != null && psnId != 0L && StringUtils.isNoneBlank(prjOperateVO.getDes3FundAgencyId())) {
        params.add("des3FundAgencyId", prjOperateVO.getDes3FundAgencyId());
        params.add("searchKey", prjOperateVO.getSearchKey());
        params.add("psnId", psnId.toString());
        params.add("page.pageNo", prjOperateVO.getPage().getParamPageNo().toString());
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_DETAIL,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }
    if ("success".equals(result.get("status"))) {
      return AppActionUtils.buildReturnInfo(result.get("result"), (Integer) result.get("totalCount"),
          AppActionUtils.changeResultStatus("success"), Objects.toString(result.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(result.get("errmsg"), ""));
    }
  }

  /**
   * 资助机构列表 -- 赞/取消赞操作
   * 
   * @param prjOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/mobile/ajaxAward", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String awardAgencyOpt(MobileFundAgencyDTO mobileFundAgencyDTO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNotBlank(mobileFundAgencyDTO.getDes3FundAgencyId())
          && mobileFundAgencyDTO.getOptType() != null) {
        params.add("des3FundAgencyId", mobileFundAgencyDTO.getDes3FundAgencyId());
        params.add("optType", mobileFundAgencyDTO.getOptType().toString());
        params.add("psnId", psnId.toString());
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_AWARD,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }
    return AppActionUtils.buildReturnInfo(Objects.toString(result.get("optCount")), 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("result"), "error")),
        Objects.toString(result.get("errorMsg"), ""));
  }

  /**
   * 资助机构列表 -- 分享操作
   * 
   * @param prjOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/agency/updatestat", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String shareAgencyOpt(MobileFundAgencyDTO mobileFundAgencyDTO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        params.add("des3FundAgencyId", mobileFundAgencyDTO.getDes3FundAgencyId());
        params.add("psnId", psnId.toString());
        params.add("shareToPlatform", Objects.toString(mobileFundAgencyDTO.getShareToPlatform(), ""));
        params.add("des3ReceiverIds", Objects.toString(mobileFundAgencyDTO.getDes3ReceiverIds(), ""));
        params.add("des3GroupId", Objects.toString(mobileFundAgencyDTO.getDes3GroupId(), ""));
        params.add("comments", Objects.toString(mobileFundAgencyDTO.getComments(), ""));
        result = (HashMap<String, Object>) restTemplate.postForObject(
            domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_UPDATE_SHARE_COUNT, RestUtils.buildPostRequestEntity(params),
            Map.class);
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }
    return AppActionUtils.buildReturnInfo(Objects.toString(result.get("optCount")), 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("result"), "error")),
        Objects.toString(result.get("errorMsg"), ""));
  }

  /**
   * 资助机构列表 -- 关注操作
   * 
   * @param prjOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/prj/mobile/interest", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String interestAgencyOpt(MobileFundAgencyDTO mobileFundAgencyDTO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNoneBlank(mobileFundAgencyDTO.getDes3FundAgencyId())
          && mobileFundAgencyDTO.getOptType() != null) {
        params.add("des3FundAgencyId", mobileFundAgencyDTO.getDes3FundAgencyId());
        params.add("optType", mobileFundAgencyDTO.getOptType().toString());
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        result =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_INTEREST,
                RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }
    return AppActionUtils.buildReturnInfo(Objects.toString(result.get("optCount")), 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("result"), "error")),
        Objects.toString(result.get("errorMsg"), ""));
  }

}
