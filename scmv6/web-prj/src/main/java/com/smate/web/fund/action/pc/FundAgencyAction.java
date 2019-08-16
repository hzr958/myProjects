package com.smate.web.fund.action.pc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.agency.dto.FundAgencyDTO;
import com.smate.web.fund.agency.model.FundAgencyForm;
import com.smate.web.fund.model.common.ConstFundAgency;
import com.smate.web.fund.service.agency.FundAgencyService;
import com.smate.web.fund.service.recommend.FundRecommendService;

/**
 * 基金列表
 * 
 * @author Administrator
 *
 */
@Results({@Result(name = "fundAgencyMain", location = "/WEB-INF/jsp/fund/fundAgency/funding_agencies_main.jsp"),
    @Result(name = "fundagencycondition", location = "/WEB-INF/jsp/fund/fundAgency/fundagency_conditions.jsp"),
    @Result(name = "detailMain", location = "/WEB-INF/jsp/fund/fundAgency/agency_detail_main.jsp"),
    @Result(name = "agencyNotExist", location = "/WEB-INF/jsp/fund/fundAgency/agency_not_exist.jsp"),
    @Result(name = "agencyfunds", location = "/WEB-INF/jsp/fund/fundAgency/agency_funds.jsp"),
    @Result(name = "addFundAgency", location = "/WEB-INF/jsp/fund/recommend/addFundAgency.jsp"),
    @Result(name = "fundAgency", location = "/WEB-INF/jsp/fund/fundAgency/funding_agencies_list.jsp"),
    @Result(name = "detailList", location = "/WEB-INF/jsp/fund/fundAgency/agency_detail_list.jsp"),
    @Result(name = "detailLeft", location = "/WEB-INF/jsp/fund/fundAgency/agency_detail_left.jsp"),
    @Result(name = "outside_agency_detail", location = "/WEB-INF/jsp/fund/fundAgency/outside_agency_detail_main.jsp"),
    @Result(name = "outside_fund_list", location = "/WEB-INF/jsp/fund/fundAgency/outside_agency_fund_list.jsp")})

public class FundAgencyAction extends ActionSupport implements ModelDriven<FundAgencyForm>, Preparable {

  private static final long serialVersionUID = -4897544724075498472L;
  private FundAgencyForm form;
  @Autowired
  private FundAgencyService fundAgencyService;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private FundRecommendService fundRecommendService;

  /**
   * 基金列表主页面
   * 
   * @return
   */
  @Actions({@Action("/prjweb/fundAgency/main"), @Action("/prjweb/fundAgency/ajaxmain")})
  public String fundAgencyMain() {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      List<ConstRegion> regionList = fundAgencyService.getFundRegion();
      form.setDes3AgencyIds(fundAgencyService.findPsnAllInterestAgencyIds(psnId));
      form.setRegionList(regionList);
    } catch (Exception e) {
      logger.error("资助机构地区列表加载失败", e);
    }
    return "fundAgencyMain";
  }

  /**
   * 资助机构地区条件筛选
   * 
   * @return
   */
  @Action("/prjdata/agency/condition")
  public void fundAgencyCondition() {
    Map<String, Object> map = new HashMap<>();
    try {
      LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
      List<ConstRegion> regionList = fundAgencyService.getFundRegion();
      map.put("regionList", regionList);
      map.put("totalCount", regionList.size());
      map.put("status", "success");
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("资助机构地区列表加载失败", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  @Action("/prjweb/fundAgency/ajaxeditfund")
  public String ajaxEditFund() {
    try {
      // 默认限制选中10个
      if (form.getMaxSelectFund() == null || form.getMaxSelectFund() == 0) {
        form.setMaxSelectFund(10);
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // 获取资助机构构建成的Map
      fundAgencyService.buildFundMapBaseInfo(form);
    } catch (Exception e) {
      logger.error("弹出资助机构弹出框出错", e);
    }
    return "addFundAgency";
  }

  /**
   * 资助机构列表加载页面
   */
  @Action("/prjweb/fundAgency/ajaxfundlist")
  public String fundAgencyList() {
    String regionIds = form.getRegionAgency();
    List<Long> regionList = new ArrayList<Long>();
    if (StringUtils.isNotBlank(regionIds)) {
      String[] ids = regionIds.split(",");
      for (String id : ids) {
        regionList.add(NumberUtils.toLong(id));
      }
    }
    List<ConstFundAgency> constFundAgency;
    try {
      constFundAgency = fundAgencyService.getFundAgencyList(regionList, form.getPage());
      form.setFundAgencyList(constFundAgency);
    } catch (Exception e) {
      logger.error("资助机构列表加载失败", e);
    }
    return "fundAgency";
  }

  /**
   * 移动端 - 资助机构列表加载页面
   */
  @Action("/prjdata/agency/ajaxlist")
  public void ajaxfundagencylist() {
    // 查询条件：关键字/地区
    Map<String, Object> map = new HashMap<>();
    try {
      String searchKey = form.getSearchKey();
      if (StringUtils.isNotBlank(searchKey)) {
        if (searchKey.contains("&quot;")) {
          form.setSearchKey(searchKey.replaceAll("&quot;", "\""));
        }
      }
      LocaleContextHolder.setLocale(new Locale("zh_CN"));
      List<FundAgencyDTO> findFundAgencyList = fundAgencyService.findFundAgencyList(form);
      map.put("result", findFundAgencyList);
      map.put("totalCount", form.getPage().getTotalCount());
      map.put("totalPages", form.getPage().getTotalPages());
      map.put("pageNo", form.getPage().getPageNo());
      map.put("status", "success");
    } catch (Exception e) {
      logger.error("资助机构列表加载失败", e);
      map.put("status", "error");
    }
    Struts2Utils.renderJsonNoNull(map, "encoding:UTF-8");
  }

  /**
   * 加载左侧列表地区的统计数
   */
  @Action("/prjweb/fundAgency/ajaxfundcount")
  public String fundAgencyCount() {
    String jsonStr = null;
    try {
      jsonStr = fundAgencyService.getAllFundAgencyCount();
    } catch (Exception e) {
      logger.error("左侧列表地区的统计数失败", e);
    }

    Struts2Utils.renderJson(jsonStr, "encoding:UTF-8");
    return null;
  }

  /**
   * 进入资助机构详情页面
   * 
   * @return
   */
  @Actions({@Action("/prjweb/fundAgency/detailMain"), @Action("/prjweb/outside/agency")})
  public String fundDetailmain() {
    Long psnId = SecurityUtils.getCurrentUserId();
    boolean hasLogin = !NumberUtils.isNullOrZero(psnId);
    try {
      // 如果是邮件跳转,查看是否已经登录,若登录,应该重定向到登录链接
      HttpServletRequest request = Struts2Utils.getRequest();
      HttpServletResponse response = Struts2Utils.getResponse();
      String URI = request.getRequestURI();
      String queryStr = request.getQueryString();
      if (URI.contains("outside") && hasLogin) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/prjweb/fundAgency/detailMain");
        int index = queryStr.indexOf("?");
        if (index != -1) {
          stringBuffer.append("&" + queryStr);
        } else {
          stringBuffer.append("?" + queryStr);
        }
        response.sendRedirect(stringBuffer.toString());
        return "";
      }
      if (SmateMobileUtils.isMobileBrowser(request)) {
        response.sendRedirect(domainMobile + "/prj/outside/agency?des3FundAgencyId=" + form.getDes3FundAgencyId());
        return null;
      }
      if (form.getFundAgencyId() != 0L) {
        fundAgencyService.getFundAgency(form);
        if (form.getFund() == null) {
          return "agencyNotExist";
        }
      } else {
        return "agencyNotExist";
      }
      form.setHasLogin(hasLogin ? 1 : 0);
    } catch (Exception e) {
      logger.error("进入机构详情页面出错，fundAgencyId=" + form.getFundAgencyId(), e);
    }
    if (!hasLogin) {
      return "outside_agency_detail";
    }
    return "detailMain";
  }

  /**
   * 移动端 -- 进入资助机构基金详情页面
   * 
   * @return
   */
  @Action("/prjweb/fundAgency/funddetailMain")
  public String showfundDetailmain() {
    try {
      if (form.getDes3FundAgencyId() != null) {
        form.setFundAgencyId(Long.parseLong(Des3Utils.decodeFromDes3(form.getDes3FundAgencyId())));
        fundAgencyService.getFundAgency(form);
      }
    } catch (Exception e) {
      logger.error("进入机构详情页面出错，fundAgencyId=" + form.getFundAgencyId());
    }
    return "agencyfunds";
  }

  /**
   * 移动端 -- 进入资助机构详情页面
   * 
   * @return
   */
  @Action("/prjdata/agency/detail")
  public void fundAgencyDetail() {
    Map<String, Object> map = new HashMap<>();
    try {
      LocaleContextHolder.setLocale(new Locale("zh_CN"));
      String strFundAgencyId = Des3Utils.decodeFromDes3(form.getDes3FundAgencyId());
      form.setFundAgencyId(NumberUtils.toLong(strFundAgencyId));
      Long psnId = SecurityUtils.getCurrentUserId();
      if (NumberUtils.isZero(psnId) && NumberUtils.isZero(form.getPsnId())) {
        map.put("hasLogin", 0);// 判断是否为站外访问
      } else if (NumberUtils.isZero(psnId)) {
        psnId = form.getPsnId();
      }
      if (StringUtils.isNotBlank(strFundAgencyId)) {
        Long fundAgencyId = Long.parseLong(strFundAgencyId);
        form.setPsnId(psnId);
        fundAgencyService.getFundAgency(form);
        if (form.getFund() != null) {
          form.setLogoUrl(form.getFund().getLogoUrl());
        }
        fundAgencyService.getFundList(form);
        Page page = form.getPage();
        map.put("result", page.getResult());
        map.put("totalCount", page.getTotalCount());
        map.put("pageCount", page.getTotalPages());
        map.put("pageNo", page.getPageNo());
        map.put("des3FundIds", form.getDes3FundIds());
      }
      map.put("status", "success");
    } catch (Exception e) {
      logger.error("移动端机构详情页面查询基金列表出错，fundAgencyId=" + form.getFundAgencyId(), e);
      map.put("status", "error");
    }
    Struts2Utils.renderJson(map, "Encoding:UTF-8");
  }

  /**
   * 为资助机构分享提供数据
   */
  @Action("/prjdata/aidinsdetail/forshare")
  public void aidInsForShare() {
    Map<String, String> result = new HashMap<String, String>();
    String des3FundAgencyId = form.getDes3FundAgencyId();
    if (StringUtils.isNotEmpty(des3FundAgencyId)) {
      try {
        form.setFundAgencyId(NumberUtils.toLong(Des3Utils.decodeFromDes3(des3FundAgencyId)));
        fundAgencyService.getFundAgency(form);
        ConstFundAgency fund = form.getFund();
        if (Objects.nonNull(fund)) {
          result.put("zhName", fund.getNameZh());
          result.put("address", fund.getAddress());
          result.put("logo", form.getLogoUrl());
          result.put("status", "success");
          result.put("msg", "get data success");
        } else {
          result.put("status", "error");
          result.put("msg", "resource is not exists");
        }
      } catch (Exception e) {
        logger.error("获取资助机构相关信息出错,des3FundAgencyId={}", des3FundAgencyId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
  }

  /**
   * 资助机构详情页面基金列表查询
   * 
   * @return
   */
  @Actions({@Action("/prjweb/fundAgency/ajaxDetailList"), @Action("/prjweb/outside/ajaxfundlist")})
  public String ajaxDetailList() {
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (form.getFundAgencyId() != null && form.getFundAgencyId() != 0L) {
        if (form.getPsnId() == null || form.getPsnId() == 0L) {
          form.setPsnId(psnId);
        }
        fundAgencyService.getFundList(form);
        form.setDes3FundAgencyId(Des3Utils.encodeToDes3(form.getFundAgencyId().toString()));
      }
    } catch (Exception e) {
      logger.error("机构详情页面查询基金列表出错，fundAgencyId=" + form.getFundAgencyId(), e);
    }
    if (NumberUtils.isNullOrZero(psnId)) {
      return "outside_fund_list";
    }
    return "detailList";
  }

  /**
   * 资助机构左侧查询条件
   * 
   * @return
   */
  @Actions({@Action("/prjweb/fundAgency/ajaxDetailLeft"), @Action("/prjweb/outside/ajaxfundcondition")})
  public String ajaxDetailLeft() {
    try {
      if (form.getFundAgencyId() != null && form.getFundAgencyId() != 0L) {
        fundAgencyService.getDetailLeftCondition(form);
      }
    } catch (Exception e) {
      logger.error("机构详情页面查询左侧条件查询出错，fundAgencyId=" + form.getFundAgencyId(), e);
    }
    return "detailLeft";
  }

  /**
   * 查询资助机构详情页面左侧统计数
   */
  @Actions({@Action("/prjweb/fundAgency/ajaxLeftCount"), @Action("/prjweb/outside/ajaxfundcount")})
  public void ajaxLeftCount() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (StringUtils.isNotBlank(form.getDisIdStr()) && form.getFundAgencyId() != null) {
        List<Map<String, String>> resultList =
            fundAgencyService.getDetailLeftCount(form.getDisIdStr(), form.getFundAgencyId());
        String resultStr = JacksonUtils.jsonListSerializer(resultList);
        Struts2Utils.renderJson(resultStr, "Encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("统计资助机构详情页面的基金数出错，disIdStr=" + form.getDisIdStr(), e);
    }
  }

  @Action("/prjweb/outside/test")
  public String testAction() {
    // String comments =
    // "http://apps.webofknowledge.com/InboundService.do?SID=F61D9d3H3vznotF9ron&uml_return_url=http%3A%2F%2Fpcs.webofknowledge.com%2Fuml%2Fuml_view.cgi%3Fproduct_sid%S2igJonHFDcff498A2H%26product%3DWOS%26product_st_thomas%3Dhttp%253A%252F%252Festi%252Eisiknowledge%252Ecom%253A8360%252Festi%252Fxrpc%26sort_opt%3DDate&action=retrieve&product=WOS&mode=FullRecord&viewType=fullRecord&frmUML=1&UT=000448536200001";
    String comments = "<h2>呵呵<h2>";
    form.setComments(comments);
    return "test";
  }

  @Action("/prjweb/ajaxCheckPub")
  public void ajaxCheckFundAgency() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long fundAgencyId = form.getFundAgencyId();
    Long resType = form.getResType();
    try {
      if (fundAgencyId != null && resType != null) {
        // 基金机会
        if (DynamicConstants.RES_TYPE_FUND == resType) {
          if (checkFund(fundAgencyId)) {
            map.put("status", "success");
          } else {
            map.put("status", "fundIsDel");
          }
        } else {// 资助机构
          if (checkFundAgency(fundAgencyId)) {
            map.put("status", "success");
          } else {
            map.put("status", "fundAgencyIsDel");
          }
        }
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      map.put("status", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
  }

  /**
   * 查看基金是否存在
   * 
   * @param des3FundAgencyId
   * @param resType 11 基金 25资助机构
   * @return
   */
  private boolean checkFund(Long fundAgencyId) {
    // 基金机会
    return fundAgencyService.existsFund(fundAgencyId);
  }

  /**
   * 查看资助机构是否存在
   * 
   * @param des3FundAgencyId
   * @param resType 11 基金 25资助机构
   * @return
   */
  private boolean checkFundAgency(Long fundAgencyId) {
    // 资助机构
    return fundAgencyService.existsAgency(fundAgencyId);
  }

  /**
   * 获取基金详情
   */
  @Action("/prjdata/funddetails/forshare")
  public void queryFundDetails() {
    Map<String, String> result = new HashMap<String, String>();
    if (StringUtils.isNotEmpty(form.getDes3FundId())) {
      try {
        Long fundId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId()));
        // 先从solr获取基金详情信息
        Map<String, Object> fundDetail = fundAgencyService.queryFundDetail(fundId);
        String resultStr = fundDetail.get("items") != null ? fundDetail.get("items").toString() : "";
        if (StringUtils.isNotEmpty(resultStr)) {
          List<Map<String, Object>> resultList = JacksonUtils.jsonToList(resultStr);
          Map<String, Object> resultMap = resultList != null && resultList.size() > 0 ? resultList.get(0) : null;
          if (Objects.nonNull(resultMap)) {
            result.put("zhTitle", resultMap.get("fundNameZh") != null ? resultMap.get("fundNameZh").toString() : "");
            result.put("agencyName",
                resultMap.get("fundAgencyNameZh") != null ? resultMap.get("fundAgencyNameZh").toString() : "");
            StringBuffer stringBuffer = new StringBuffer();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (resultMap.get("fundStartDate") != null) {
              stringBuffer.append(simpleDateFormat.format(new Date(resultMap.get("fundStartDate").toString())));
            }
            boolean hasEndTimer =
                resultMap.get("fundEndDate") != null && StringUtils.isNotEmpty(resultMap.get("fundEndDate").toString());
            if (stringBuffer.length() > 0 && hasEndTimer) {
              stringBuffer.append(" ~ ");
            }
            if (hasEndTimer) {
              stringBuffer.append(simpleDateFormat.format(new Date(resultMap.get("fundEndDate").toString())));
            }
            result.put("timer", stringBuffer.toString());
            // 分享需要的一些信息
            result.put("enTitle", resultMap.get("fundNameEn") != null ? resultMap.get("fundNameEn").toString() : "");
            StringBuffer zhOtherInfo = new StringBuffer();
            StringBuffer enOtherInfo = new StringBuffer();
            StringBuffer otherTimer = new StringBuffer();
            zhOtherInfo
                .append(resultMap.get("fundAgencyNameZh") != null ? resultMap.get("fundAgencyNameZh").toString() : "");
            if (zhOtherInfo.length() > 0 && resultMap.get("fundCategoryStrZh") != null
                && StringUtils.isNotEmpty(resultMap.get("fundCategoryStrZh").toString())) {
              zhOtherInfo.append("，");
              zhOtherInfo.append(resultMap.get("fundCategoryStrZh").toString());
            }
            enOtherInfo
                .append(resultMap.get("fundAgencyNameEn") != null ? resultMap.get("fundAgencyNameEn").toString() : "");
            if (zhOtherInfo.length() > 0 && resultMap.get("fundCategoryStrEn") != null
                && StringUtils.isNotEmpty(resultMap.get("fundCategoryStrEn").toString())) {
              enOtherInfo.append("，");
              enOtherInfo.append(resultMap.get("fundCategoryStrEn").toString());
            }
            if (StringUtils.isNotEmpty(stringBuffer.toString())) {
              otherTimer.append("，");
              otherTimer.append(stringBuffer.toString());
            }
            if (zhOtherInfo.length() > 0 && enOtherInfo.length() > 0) {
              zhOtherInfo.append(otherTimer.toString());
              enOtherInfo.append(otherTimer.toString());
            }
            result.put("zhShowDesc", zhOtherInfo.toString());
            result.put("enShowDesc", enOtherInfo.toString());
            // 再获取基金的logo
            String des3FundAgencyId = Des3Utils
                .encodeToDes3(resultMap.get("fundAgencyId") != null ? resultMap.get("fundAgencyId").toString() : "");
            String[] des3FundId = {des3FundAgencyId};
            List<Map<String, String>> fundLogos = fundRecommendService.getFundLogos(des3FundId);
            Map<String, String> mapLogo = fundLogos != null && fundLogos.size() > 0 ? fundLogos.get(0) : null;
            result.put("logo", mapLogo != null ? mapLogo.get("logoUrl") : "");
            result.put("status", "success");
            result.put("msg", "get data success");
          }
        } else {
          result.put("status", "error");
          result.put("msg", "resource is not exists");
        }
      } catch (Exception e) {
        result.put("status", "error");
        result.put("msg", "system error");
        logger.error("获取基金详情出错,des3FundId={}", form.getDes3FundId(), e);
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }



  @Override
  public FundAgencyForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundAgencyForm();
    }
  }

}
