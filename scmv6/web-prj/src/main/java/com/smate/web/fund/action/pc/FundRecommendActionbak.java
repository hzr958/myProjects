package com.smate.web.fund.action.pc;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.service.recommend.FundRecommendService;

/**
 * 基金推荐Action
 * 
 * @author WSN
 *
 *         2017年8月18日 上午10:07:33
 *
 */
@Results({@Result(name = "myFund", location = "/WEB-INF/jsp/fund/myfund/fund_collected_list.jsp"),
    @Result(name = "conditions", location = "/WEB-INF/jsp/fund/myfund/fund_recommend_conditions.jsp"),
    @Result(name = "recommendfund", location = "/WEB-INF/jsp/fund/myfund/recommend_fund_main.jsp"),
    @Result(name = "fundList", location = "/WEB-INF/jsp/fund/myfund/fund_recommend_list.jsp"),
    @Result(name = "fundScienceArea", location = "/WEB-INF/jsp/fund/myfund/fund_science_area_box.jsp"),
    @Result(name = "fundDetails", location = "/WEB-INF/jsp/fund/myfund/fund_details_main.jsp")})
public class FundRecommendActionbak extends ActionSupport implements ModelDriven<FundRecommendForm>, Preparable {

  private static final long serialVersionUID = 2333970976100254966L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FundRecommendForm form;
  @Autowired
  private FundRecommendService fundRecommendService;
  @Value("${domainscm}")
  private String domainScm;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundRecommendForm();
    }
  }

  @Override
  public FundRecommendForm getModel() {
    return form;
  }

  /**
   * 我的基金列表
   * 
   * @return
   */
  /*
   * @Action("/prjweb/collectedfund/ajaxlist") public String showMyFund() { try { // TODO 数据校验 Long
   * psnId = SecurityUtils.getCurrentUserId(); if (psnId > 0) { form.setPsnId(psnId);
   * fundRecommendService.searchMyFund(form); } else { form.setErrorMsg("psnId is empty"); } } catch
   * (Exception e) { logger.error("进入我的基金页面出错， psnId = " + SecurityUtils.getCurrentUserId(), e); }
   * return "myFund"; }
   * 
   *//**
      * 赞/取消赞操作
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fund/ajaxaward") public String awardRecommendFund() { Map<String, Object> result
   * = new HashMap<String, Object>(); try { // TODO 数据校验 if (form.getFundId() == null &&
   * StringUtils.isNotBlank(form.getEncryptedFundId())) {
   * form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId()))); } Long
   * psnId = SecurityUtils.getCurrentUserId(); if (psnId > 0 && form.getFundId() != null &&
   * form.getFundId() > 0) { form.setPsnId(psnId); form =
   * fundRecommendService.fundAwardOperation(form); result.put("awardCount", form.getAwardCount());
   * result.put("hasAward", form.getHasAward()); result.put("result", "success"); } else {
   * result.put("result", "error"); // result.put("msg", "psnId or fundId is empty"); } } catch
   * (Exception e) { logger.error("基金赞操作失败， psnId = " + SecurityUtils.getCurrentUserId(), e);
   * result.put("result", "error"); } Struts2Utils.renderJson(result, "Encoding:UTF-8"); return null;
   * }
   * 
   *//**
      * 收藏/取消收藏操作
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fund/ajaxcollect") public String recommendFundCollection() { Map<String, String>
   * result = new HashMap<String, String>(); try { // TODO 数据校验 if (form.getFundId() == null &&
   * StringUtils.isNotBlank(form.getEncryptedFundId())) { if
   * (NumberUtils.isNumber(form.getEncryptedFundId())) {
   * form.setFundId(NumberUtils.toLong(form.getEncryptedFundId())); } else {
   * form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId()))); } } Long
   * psnId = SecurityUtils.getCurrentUserId(); if (psnId > 0 && form.getFundId() != null &&
   * form.getFundId() > 0) { form.setPsnId(psnId); form =
   * fundRecommendService.fundCollectOperation(form); result.put("result", "success"); } else {
   * result.put("result", "error"); form.setErrorMsg("psnId or fundId is empty"); } } catch (Exception
   * e) { logger.error("收藏基金操作出错， psnId = " + SecurityUtils.getCurrentUserId() + ", fundId = " +
   * form.getFundId(), e); result.put("result", "error"); } result.put("errorMsg",
   * form.getErrorMsg()); Struts2Utils.renderJson(result, "Encoding:UTF-8"); return null; }
   * 
   *//**
      * 保存基金推荐条件关注地区
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fundconditions/ajaxsavefundregion") public String saveFundConditionsRegionSave()
   * { Map<String, Object> result = new HashMap<String, Object>(); try {
   * form.setPsnId(SecurityUtils.getCurrentUserId()); if (form.getPsnId() > 0) { result =
   * fundRecommendService.fundConditionsRegionSave(form); List<FundRecommendRegion> allRegionList =
   * new ArrayList<FundRecommendRegion>(); allRegionList =
   * fundRecommendService.getAllregion(form.getPsnId());// 查询地区设置 result.put("allRegionList",
   * allRegionList); result.put("result", "success"); } } catch (Exception e) {
   * logger.error("保存推荐条件关注地区出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
   * result.put("result", "error"); } Struts2Utils.renderJson(result, "Encoding:UTF-8"); return null;
   * }
   * 
   *//**
      * 删除基金推荐条件关注地区
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fundconditions/ajaxdeletefundregion") public String ajaxdeletefundregion() {
   * Map<String, String> result = new HashMap<String, String>(); try {
   * form.setPsnId(SecurityUtils.getCurrentUserId()); if (form.getPsnId() > 0) { result =
   * fundRecommendService.deleteFundConditionRegion(form.getRegionCode()); result.put("result",
   * "success"); } } catch (Exception e) { logger.error("删除推荐条件关注地区出错， psnId = " +
   * SecurityUtils.getCurrentUserId(), e); result.put("result", "error"); }
   * Struts2Utils.renderJson(result, "Encoding:UTF-8"); return null; }
   * 
   *//**
      * 保存基金推荐条件科技领域
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fundconditions/ajaxsavefundarea") public String
   * saveFundConditionsScienceAreaSave() { Map<String, Object> result = new HashMap<String, Object>();
   * try { form.setPsnId(SecurityUtils.getCurrentUserId()); if (form.getPsnId() > 0) {
   * List<FundRecommendArea> areaList = fundRecommendService.fundConditionsScienceAreaSave(form);
   * result.put("areaList", areaList); result.put("result", "success"); } } catch (Exception e) {
   * logger.error("保存推荐条件科技领域出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
   * result.put("result", "error"); } Struts2Utils.renderJson(result, "Encoding:UTF-8"); return null;
   * }
   * 
   *//**
      * 保存基金推荐条件申请资格
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fundconditions/ajaxsaveseniority") public String
   * saveFundConditionsSenioritySave() { Map<String, Object> result = new HashMap<String, Object>();
   * try { form.setPsnId(SecurityUtils.getCurrentUserId()); if (form.getPsnId() > 0) {
   * fundRecommendService.fundConditionsSenioritySave(form); result.put("result", "success"); } }
   * catch (Exception e) { logger.error("保存推荐条件科技领域出错， psnId = " + SecurityUtils.getCurrentUserId(),
   * e); result.put("result", "error"); } Struts2Utils.renderJson(result, "Encoding:UTF-8"); return
   * null; }
   * 
   *//**
      * 删除基金推荐条件科技领域
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fundconditions/ajaxdeletefundarea") public String ajaxdeletefundarea() {
   * Map<String, String> result = new HashMap<String, String>(); try {
   * form.setPsnId(SecurityUtils.getCurrentUserId()); if (form.getPsnId() > 0) { result =
   * fundRecommendService.deleteFundScienceArea(form.getAreaCode()); result.put("result", "success");
   * } } catch (Exception e) { logger.error("删除推荐条件关注地区出错， psnId = " +
   * SecurityUtils.getCurrentUserId(), e); result.put("result", "error"); }
   * Struts2Utils.renderJson(result, "Encoding:UTF-8"); return null; }
   * 
   *//**
      * 基金推荐条件（左边栏和弹框）
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fundconditions/ajaxshow") public String showRecommendFundCOnditions() { try {
   * form.setPsnId(SecurityUtils.getCurrentUserId()); if (form.getPsnId() > 0) { form =
   * fundRecommendService.fundRecommendConditionsShow(form); } } catch (Exception e) {
   * logger.error("弹出推荐条件编辑框出错， psnId = " + SecurityUtils.getCurrentUserId(), e); } return
   * "conditions"; }
   * 
   *//**
      * 推荐基金主页面
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fund/main") public String fundRecommendMain() { if
   * (StringUtils.isBlank(form.getModule())) { form.setModule("recommend"); } return "recommendfund";
   * }
   * 
   *//**
      * 查询推荐基金列表
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fund/ajaxrecommend") public String searchRecommendFund() { try { Long psnId =
   * SecurityUtils.getCurrentUserId(); if (psnId > 0) { form.setPsnId(psnId); form =
   * fundRecommendService.fundRecommendListSearch(form); } } catch (Exception e) {
   * logger.error("查询推荐基金列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e); } return "fundList";
   * }
   * 
   *//**
      * 查询推荐基金的logo
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fund/ajaxrecommendlogo") public String searchRecommendFundLogo() { try { Long
   * psnId = SecurityUtils.getCurrentUserId(); if (psnId > 0) { String[] des3FundAgencyIds =
   * StringUtils.split(form.getDes3FundAgencyIds(), ","); List<Map<String, String>> fundLogos =
   * fundRecommendService.getFundLogos(des3FundAgencyIds); Struts2Utils.renderJson(fundLogos,
   * "Encoding:UTF-8"); } } catch (Exception e) { logger.error("查询推荐基金LOGO出错， psnId = " +
   * SecurityUtils.getCurrentUserId(), e); } return null; }
   * 
   *//**
      * 显示基金详情页面
      * 
      * @return
      */
  /*
   * @Action("/prjweb/funddetails/show") public String showFundDetails() { try {
   * form.setPsnId(SecurityUtils.getCurrentUserId());
   * form.setEncryptedFundId(URLEncoder.encode(form.getEncryptedFundId(), "utf-8")); if
   * (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
   * form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId()))); } if
   * (form.getPsnId() != null && form.getPsnId() > 0l && form.getFundId() != null && form.getFundId()
   * > 0l) { fundRecommendService.buildFundDetailsInfo(form); } if (form.getPsnId() == null ||
   * form.getPsnId() == 0l) { Struts2Utils.getResponse().sendRedirect( domainScm +
   * "/prjweb/outside/showfund?encryptedFundId=" + form.getEncryptedFundId()); } // 判断基金是否过期 String
   * showDate = form.getConstFundCategoryInfo().getShowDate(); if (StringUtils.isNotBlank(showDate)) {
   * String strLastDate = showDate.substring(showDate.indexOf("~") + 2); SimpleDateFormat DATE_FORMAT
   * = new SimpleDateFormat("yyyy-MM-dd"); Date lastDate = DATE_FORMAT.parse(strLastDate); Date now =
   * new Date(); if (now.after(lastDate)) { form.setIsStaleDated(true); } } } catch (Exception e) {
   * logger.error("显示基金详情出错， fundId = " + form.getFundId(), e); } return "fundDetails"; }
   * 
   *//**
      * 站外-------显示基金详情页面
      * 
      * @return
      */
  /*
   * @Action("/prjweb/outside/showfund") public String showFundDetailsOutSide() { try {
   * form.setPsnId(SecurityUtils.getCurrentUserId());
   * form.setEncryptedFundId(URLEncoder.encode(form.getEncryptedFundId(), "utf-8")); if
   * (form.getPsnId() != null && form.getPsnId() > 0l) { Struts2Utils.getResponse().sendRedirect(
   * domainScm + "/prjweb/funddetails/show?encryptedFundId=" + form.getEncryptedFundId()); return
   * null; } if (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
   * form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId()))); } if
   * (form.getFundId() != null && form.getFundId() > 0l) {
   * fundRecommendService.buildFundDetailsInfo(form); } } catch (Exception e) {
   * logger.error("显示基金详情出错， fundId = " + form.getFundId(), e); } return "fundDetails"; }
   * 
   *//**
      * 更新分享统计数
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fund/ajaxsharecount") public String fundShareCount() { Map<String, Object>
   * result = new HashMap<String, Object>(); try { if (form.getFundId() == null &&
   * StringUtils.isNotBlank(form.getDes3FundId())) {
   * form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId()))); }
   * fundRecommendService.updateFundShareCount(form); result.put("result", "success"); } catch
   * (Exception e) { logger.error("更新基金分享统计数出错， fundId = " + form.getFundId() + ", psnId = " +
   * SecurityUtils.getCurrentUserId(), e); result.put("result", "error"); } result.put("shareCount",
   * form.getShareCount()); Struts2Utils.renderJson(result, "Encoding:UTF-8"); return null; }
   * 
   *//**
      * 初始化基金收藏的状态
      * 
      * @return
      */
  /*
   * @Action("/prjweb/fund/initCollectStatus") public String initFundCollectedStatus() { Map<String,
   * Object> result = new HashMap<String, Object>(); try { result =
   * fundRecommendService.initFundCollectedStatus(form); } catch (Exception e) { logger.error(
   * "查询基金收藏状态出错， fundIds = " + form.getDes3FundIds() + ",  psnId = " +
   * SecurityUtils.getCurrentUserId(), e); } Struts2Utils.renderJson(result, "Encoding:UTF-8"); return
   * null; }
   * 
   *//**
      * 初始化基金操作
      * 
      * @return
      *//*
         * @Action("/prjweb/fundop/ajaxinit") public String initFundOperation() { Map<String, Object> result
         * = new HashMap<String, Object>(); try { if (form.getFundId() == null &&
         * StringUtils.isNotBlank(form.getDes3FundId())) {
         * form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId()))); } form =
         * fundRecommendService.initFundOperations(form); result.put("result", "success"); } catch
         * (Exception e) { logger.error( "查询基金收藏状态出错， fundIds = " + form.getDes3FundIds() + ",  psnId = " +
         * SecurityUtils.getCurrentUserId(), e); result.put("result", "error"); } result.put("hasAward",
         * form.getHasAward()); result.put("hasCollect", form.getHasCollected()); result.put("shareCount",
         * form.getShareCount()); result.put("awardCount", form.getAwardCount());
         * Struts2Utils.renderJson(result, "Encoding:UTF-8"); return null; }
         */
}
