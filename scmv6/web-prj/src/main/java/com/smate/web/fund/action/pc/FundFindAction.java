package com.smate.web.fund.action.pc;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.common.BeanUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.find.dto.FundFindDTO;
import com.smate.web.fund.find.model.FundFindForm;
import com.smate.web.fund.service.find.FundFindService;
import com.smate.web.fund.service.wechat.FundWeChatQueryService;


@Results({@Result(name = "conditions_pc", location = "/WEB-INF/jsp/fund/fundFind/fund_find_conditions.jsp"),
    @Result(name = "fundlist_pc", location = "/WEB-INF/jsp/fund/fundFind/fund_find_list.jsp"),
    @Result(name = "fundfind_region_mobile", location = "/WEB-INF/jsp/fund/fundFind/fund_find_list.jsp")})
public class FundFindAction extends ActionSupport implements ModelDriven<FundFindForm>, Preparable {

  private static final long serialVersionUID = 2333970976100254966L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FundFindForm form;
  @Autowired
  private FundFindService fundFindService;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private FundWeChatQueryService fundWeChatQueryservice;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundFindForm();
    }
  }

  @Override
  public FundFindForm getModel() {
    return form;
  }


  /**
   * 基金发现条件 -- 左边栏
   * 
   * @return
   */
  @Action("/prjweb/fundfind/ajaxmain")
  public String showMyFundFind() {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0) {
        form.setPsnId(psnId);
        fundFindService.fundFindConditionsShow(form);
      } else {
        form.setErrorMsg("psnId is empty");
      }
    } catch (Exception e) {
      logger.error("获取基金发现页面出错，psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return "conditions_pc";
  }

  /**
   * 基金发现列表 --PC
   * 
   * @return
   */
  @Action("/prjweb/fundfind/ajaxfundlist")
  public String ajaxShowFundList() {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (NumberUtils.isNotZero(psnId)) {
        form.setPsnId(psnId);
        fundFindService.fundFindListSearch(form);
      } else {
        form.setErrorMsg("psnId is empty");
      }
    } catch (Exception e) {
      logger.error("获取基金发现页面出错，psnId = " + SecurityUtils.getCurrentUserId(), e);
    } finally {
    }
    return "fundlist_pc";
  }

  /**
   * 基金发现 根据下一级地区查找省市级地区
   */
  @Action("/prjweb/fundfind/fundRegion")
  public void FundRegion() {
    Map<String, Object> map = new HashMap<>();
    try {
      String regionName = fundFindService.findSuperRegion(form.getSearchRegion(), form.getLocale());
      map.put("result", regionName);
      map.put("status", "success");
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("基金发现获取上级地区名失败", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  /**
   * 基金发现地区筛选--移动端
   */
  @Action("/prjdata/fundfind/region")
  public void showFundFind() {
    Map<String, Object> map = new HashMap<>();
    try {
      LocaleContextHolder.setLocale(Locale.CHINA, true);
      List<ConstRegion> regionList = fundFindService.getFundRegion();
      map.put("regionList", regionList);
      map.put("status", "success");
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("基金发现地区列表加载失败", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }


  /**
   * 基金发现列表--移动端
   */
  @Action("/prjdata/fundfind/ajaxfundlist")
  public void ajaxfundlist() {
    Map<String, Object> map = new HashMap<>();
    try {
      form.getPage().setPageNo(form.getPageNum());
      fundFindService.fundFindListForWeChat(form);
      FundFindDTO fundFindDTO = new FundFindDTO();
      BeanUtils.copyProperties(fundFindDTO, form);
      map.put("result", fundFindDTO);
      map.put("resultList", form.getResultList());
      map.put("status", "success");
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("基金发现-基金列表加载失败", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }
}


