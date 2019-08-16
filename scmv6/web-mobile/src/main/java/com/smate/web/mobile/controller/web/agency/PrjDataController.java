package com.smate.web.mobile.controller.web.agency;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.mobile.prj.vo.FundOperateVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PrjApiConsts;

@Controller
public class PrjDataController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private HttpServletRequest request;

  /**
   * 资助机构列表界面
   * 
   * @param model
   * @return
   */
  @RequestMapping(value = "/prj/mobile/fundagency")
  public ModelAndView showFundAgencyList(FundOperateVO prjOperateVO) {
    ModelAndView model = new ModelAndView();
    model.addObject("searchKey", prjOperateVO.getSearchKey());
    model.addObject("regionAgency", prjOperateVO.getRegionAgency());
    model.addObject("flag", prjOperateVO.getFlag());
    model.setViewName("/prj/fundagency/wechat_fundagnecy_main");
    return model;
  }

  /**
   * 资助机构列表ajax
   */
  @RequestMapping(value = "/prj/mobile/ajaxfundagencylist", produces = "application/json;charset=UTF-8")
  public ModelAndView ajaxShowfundagencylist(HttpServletResponse response, FundOperateVO prjOperateVO) {
    ModelAndView model = new ModelAndView();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        params.add("psnId", psnId.toString());
        params.add("searchKey", prjOperateVO.getSearchKey());
        params.add("regionAgency", prjOperateVO.getRegionAgency());
        params.add("page.pageNo", prjOperateVO.getPage().getPageNo().toString());
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_LIST,
            RestUtils.buildPostRequestEntity(params), Map.class);
        if ("success".equals(result.get("status"))) {
          model.addObject("result", result.get("result"));
          model.addObject("totalPrjCount", result.get("totalCount"));
          model.addObject("totalPages", result.get("totalPages"));
          model.addObject("pageNo", result.get("pageNo"));
          model.addObject("searchKey", prjOperateVO.getSearchKey());
          model.addObject("regionAgency", prjOperateVO.getRegionAgency());
        }
        model.setViewName("/prj/fundagency/fundagency_list");
      }
    } catch (Exception e) {
      logger.error("mobile资助机构列表异常,psnId=" + psnId, e);
    }
    return model;
  }

  /**
   * 地区条件筛选
   */
  @RequestMapping(value = "/prj/mobile/agencycondition", produces = "application/json;charset=UTF-8")
  public ModelAndView showAgencycondition(FundOperateVO prjOperateVO) {
    ModelAndView model = new ModelAndView();
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
        model.addObject("regionList", result.get("regionList"));
        model.addObject("searchKey", prjOperateVO.getSearchKey());
        model.addObject("regionAgency", prjOperateVO.getRegionAgency());
        model.setViewName("/prj/fundagency/fundagency_condition");
      }
    } catch (Exception e) {
      logger.error("mobile资助机构地区筛选列表异常,psnId=" + psnId, e);
    }
    return model;
  }

  /**
   * 机构详情列表 页面显示
   */

  @RequestMapping(value = {"/prj/mobile/agencydetail", "/prj/outside/agency"})
  public ModelAndView agencyDetailList(FundOperateVO prjOperateVO) {
    // 如果是PC端的要跳转PC端链接,暂时拦截器那边有点问题，暂时先这样跳转一下
    ModelAndView model = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (!SmateMobileUtils.isMobileBrowser(SpringUtils.getRequest())) {
        String redirectUri = "/prjweb/outside/agency?des3FundAgencyId=";
        if (NumberUtils.isNotNullOrZero(psnId)) {
          redirectUri = "/prjweb/fundAgency/detailMain?des3FundAgencyId=";
        }
        SpringUtils.getResponse().sendRedirect(domainScm + redirectUri + prjOperateVO.getDes3FundAgencyId());
        return null;
      }
      model.addObject("des3FundAgencyId", prjOperateVO.getDes3FundAgencyId());
      model.addObject("searchKey", prjOperateVO.getSearchKey());
      if (NumberUtils.isNotNullOrZero(psnId)) {
        model.setViewName("/prj/fundagency/wechat_fundlist_main");
      } else {
        model.setViewName("/prj/fundagency/outside_mobile_agency_fundlist_main");
      }
    } catch (Exception e) {
      logger.error("进入移动端资助机构详情页面出错，psnId = " + psnId + ", des3AgencyId=" + prjOperateVO.getDes3FundAgencyId(), e);
    }
    return model;
  }

  /**
   * 机构详情列表 数据回显
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/prj/mobile/ajaxagencydetail", "/prj/outside/ajaxfunds"},
      produces = "application/json;charset=UTF-8")
  public ModelAndView ajaxagencydetail(FundOperateVO prjOperateVO) {
    ModelAndView model = new ModelAndView();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      params.add("des3FundAgencyId", prjOperateVO.getDes3FundAgencyId());
      params.add("searchKey", prjOperateVO.getSearchKey());
      params.add("psnId", psnId.toString());
      params.add("page.pageNo", prjOperateVO.getPage().getPageNo().toString());
      result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_DETAIL,
          RestUtils.buildPostRequestEntity(params), Map.class);
      model.addObject("resultList", result.get("result"));
      model.addObject("totalPrjCount", result.get("totalCount"));
      model.addObject("totalPages", result.get("pageCount"));
      model.addObject("pageNo", result.get("pageNo"));
      model.addObject("des3FundIds", result.get("des3FundIds"));
      model.addObject("hasLogin", result.get("hasLogin") != null ? "0" : "1");
      if (result.get("hasLogin") != null) {
        model.setViewName("/prj/fundagency/wechat_myfund_outside");
      } else {
        model.setViewName("/prj/fundagency/wechat_myfund_sub");
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId + ", des3AgencyId = " + prjOperateVO.getDes3FundAgencyId(), e);
    }
    return model;
  }

  /**
   * 资助机构列表 -- 赞/取消赞操作
   * 
   * @param prjOperateVO
   * @return
   */
  @RequestMapping(value = "/prj/mobile/ajaxAward", produces = "application/json;charset=UTF-8")
  public void awardAgencyOpt(FundOperateVO prjOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if (psnId != null && psnId != 0L) {
        params.add("des3FundAgencyId", prjOperateVO.getDes3FundAgencyId());
        params.add("optType", prjOperateVO.getOptType().toString());
        params.add("psnId", psnId.toString());
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_AWARD,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
  }

  /**
   * 资助机构列表 -- 分享操作
   * 
   * @param prjOperateVO
   * @return
   */
  @RequestMapping(value = "/prj/mobile/ajaxShare", produces = "application/json;charset=UTF-8")
  public void shareAgencyOpt(FundOperateVO prjOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if (psnId != null && psnId != 0L) {
        params.add("des3FundAgencyId", prjOperateVO.getDes3FundAgencyId());
        params.add("optType", prjOperateVO.getOptType().toString());
        result = (HashMap<String, Object>) restTemplate.postForObject(
            domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_UPDATE_SHARE_COUNT, RestUtils.buildPostRequestEntity(params),
            Map.class);
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
  }

  /**
   * 资助机构列表 -- 关注操作
   * 
   * @param prjOperateVO
   * @return
   */
  @RequestMapping(value = {"/prj/mobile/interest", "/prj/agency/ajaxinterest"},
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object interestAgencyOpt(FundOperateVO prjOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    HashMap<String, Object> result = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNotBlank(prjOperateVO.getDes3FundAgencyId())) {
        params.add("des3FundAgencyId", prjOperateVO.getDes3FundAgencyId());
        params.add("optType", prjOperateVO.getOptType().toString());
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        result =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PrjApiConsts.PRJ_FUNDAGENCY_INTEREST,
                RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端关注、取消关注资助机构异常,psnId={}， optType={}, fundId={}", psnId, prjOperateVO.getOptType(),
          prjOperateVO.getDes3FundAgencyId(), e);
      result.put("result", "error");
    }
    return result;
  }
}
