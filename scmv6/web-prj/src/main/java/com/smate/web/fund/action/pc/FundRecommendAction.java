package com.smate.web.fund.action.pc;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.util.HtmlUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.agency.model.FundAgencyInterest;
import com.smate.web.fund.recommend.model.FundRecommendArea;
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
    @Result(name = "conditions", location = "/WEB-INF/jsp/fund/recommend/fund_recommend_conditions.jsp"),
    @Result(name = "recommendfund", location = "/WEB-INF/jsp/fund/recommend/recommend_fund_main.jsp"),
    @Result(name = "fundList", location = "/WEB-INF/jsp/fund/recommend/fund_recommend_list.jsp"),
    @Result(name = "fundScienceArea", location = "/WEB-INF/jsp/fund/myfund/fund_science_area_box.jsp"),
    @Result(name = "fundDetails", location = "/WEB-INF/jsp/fund/myfund/fund_details_main.jsp"),
    @Result(name = "fundNotExist", location = "/WEB-INF/jsp/fund/myfund/fund_not_exist.jsp"),
    @Result(name = "dyn_res_recommend", location = "/WEB-INF/jsp/fund/recommend/dyn_res_recommend.jsp"),
    @Result(name = "mobile_dyn_res_recommend", location = "/WEB-INF/jsp/fund/mobile/mobile_dyn_res_recommend.jsp")})
public class FundRecommendAction extends ActionSupport implements ModelDriven<FundRecommendForm>, Preparable {

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
  @Action("/prjweb/collectedfund/ajaxlist")
  public String showMyFund() {
    try {
      // TODO 数据校验
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0) {
        form.setPsnId(psnId);
        fundRecommendService.searchMyFund(form);
      } else {
        form.setErrorMsg("psnId is empty");
      }
    } catch (Exception e) {
      logger.error("进入我的基金页面出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return "myFund";
  }

  /**
   * 赞/取消赞操作
   * 
   * @return
   */
  @Action("/prjweb/fund/ajaxaward")
  public String awardRecommendFund() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // TODO 数据校验
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId())));
      }
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0 && form.getFundId() != null && form.getFundId() > 0) {
        form.setPsnId(psnId);
        form = fundRecommendService.fundAwardOperation(form);
        result.put("awardCount", form.getAwardCount());
        result.put("hasAward", form.getHasAward());
        result.put("result", "success");
      } else {
        result.put("result", "error");
        // result.put("msg", "psnId or fundId is empty");
      }
    } catch (Exception e) {
      logger.error("基金赞操作失败， psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 收藏/取消收藏操作
   * 
   * @return
   */
  @Action("/prjweb/fund/ajaxcollect")
  public String recommendFundCollection() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      // TODO 数据校验
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
        if (NumberUtils.isNumber(form.getEncryptedFundId())) {
          form.setFundId(NumberUtils.toLong(form.getEncryptedFundId()));
        } else {
          form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId())));
        }
      }
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0 && form.getFundId() != null && form.getFundId() > 0) {
        form.setPsnId(psnId);
        form = fundRecommendService.fundCollectOperation(form);
        result.put("result", "success");
      } else {
        result.put("result", "error");
        form.setErrorMsg("psnId or fundId is empty");
      }
    } catch (Exception e) {
      logger.error("收藏基金操作出错， psnId = " + SecurityUtils.getCurrentUserId() + ", fundId = " + form.getFundId(), e);
      result.put("result", "error");
    }
    result.put("errorMsg", form.getErrorMsg());
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 保存基金推荐条件关注资助机构
   * 
   * @return
   */
  @Action("/prjweb/fundconditions/ajaxsaveagencyInterest")
  public String saveFundConditionsAgencyInterest() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() > 0) {
        result = fundRecommendService.saveFundConditionsFundAgencyInterest(form);
        List<FundAgencyInterest> allFundAgencyInterest = new ArrayList<FundAgencyInterest>();
        allFundAgencyInterest = fundRecommendService.getPsnFundAgencyInterestList(form.getPsnId());// 查询个人关注的资助机构
        result.put("allFundAgencyInterest", allFundAgencyInterest);
        result.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("保存推荐条件资助机构出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 删除基金推荐条件关注资助机构
   * 
   * @return
   */
  @Action("/prjweb/fundconditions/ajaxdeleteagencyInterest")
  public String deleteAgencyInterest() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() > 0) {
        result = fundRecommendService.deleteFundConditionAgencyInterest(form.getPsnId(), form.getAgencyId());
        result.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("删除推荐条件关注资助机构出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 保存基金推荐条件科技领域
   * 
   * @return
   */
  @Action("/prjweb/fundconditions/ajaxsavefundarea")
  public String saveFundConditionsScienceAreaSave() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() > 0) {
        List<FundRecommendArea> areaList = fundRecommendService.fundConditionsScienceAreaSave(form);
        result.put("areaList", areaList);
        result.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("保存推荐条件科技领域出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 保存基金推荐条件申请资格
   * 
   * @return
   */
  @Action("/prjweb/fundconditions/ajaxsaveseniority")
  public String saveFundConditionsSenioritySave() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() > 0) {
        fundRecommendService.fundConditionsSenioritySave(form);
        result.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("保存推荐条件科技领域出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 删除基金推荐条件科技领域
   * 
   * @return
   */
  @Action("/prjweb/fundconditions/ajaxdeletefundarea")
  public String ajaxdeletefundarea() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() > 0) {
        result = fundRecommendService.deleteFundScienceArea(form.getAreaCode());
        result.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("删除推荐条件科技领域出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 基金推荐条件（左边栏和弹框）
   * 
   * @return
   */
  @Action("/prjweb/fundconditions/ajaxshow")
  public String showRecommendFundCOnditions() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() > 0) {
        form = fundRecommendService.fundRecommendConditionsShow(form);
      }
    } catch (Exception e) {
      logger.error("弹出推荐条件编辑框出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return "conditions";
  }

  /**
   * 推荐基金主页面
   * 
   * @return
   */
  @Action("/prjweb/fund/main")
  public String fundRecommendMain() {
    if (StringUtils.isBlank(form.getModule())) {
      form.setModule("recommend");
    }
    return "recommendfund";
  }

  /**
   * 查询推荐基金列表
   * 
   * @return
   */
  @Action("/prjweb/fund/ajaxrecommend")
  public String searchRecommendFund() {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0) {
        form.setPsnId(psnId);
        form.setPageNum(form.getPage() != null ? form.getPage().getPageNo() : 1);
        form = fundRecommendService.fundRecommendListSearch(form);
      }
    } catch (Exception e) {
      logger.error("查询推荐基金列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return "fundList";
  }

  @Action("/prjweb/fund/ajaxfundrecommendshowindyn")
  public String getFundRecommendShowInDyn() {
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      form.setPsnId(psnId);
      form.setPageSize(1);
      if (form.getPsnId() > 0) {
        // fundRecommendService.initAgencyInterestAndAreaSeniority(psnId);
        fundRecommendService.fundRecommendListSearch(form);
        fundRecommendService.buildDynFundRecommend(form);
      }
    } catch (Exception e) {
      logger.error("获取首页动态基金推荐出错， psnId = " + psnId, e);
    }
    if ("mobile".equals(form.getPlatform())) {
      return "mobile_dyn_res_recommend";
    }
    return "dyn_res_recommend";
  }

  /**
   * 查询推荐基金的logo
   * 
   * @return
   */
  @Actions({@Action("/prjweb/fund/ajaxrecommendlogo"), @Action("/prjweb/outside/fund/ajaxrecommendlogo")})
  public String searchRecommendFundLogo() {
    try {
      // Long psnId = SecurityUtils.getCurrentUserId();
      // if (psnId > 0) {
      // String[] des3FundAgencyIds = StringUtils.split(form.getDes3FundAgencyIds(), ",");
      // List<Map<String, String>> fundLogos = fundRecommendService.getFundLogos(des3FundAgencyIds);
      // Struts2Utils.renderJson(fundLogos, "Encoding:UTF-8");
      // }
      // 获取基金图片不需要验证登录,站内站外都需要显示基金图标
      String[] des3FundAgencyIds = StringUtils.split(form.getDes3FundAgencyIds(), ",");
      List<Map<String, String>> fundLogos = fundRecommendService.getFundLogos(des3FundAgencyIds);
      Struts2Utils.renderJson(fundLogos, "Encoding:UTF-8");
    } catch (Exception e) {
      logger.error("查询推荐基金LOGO出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return null;
  }

  /**
   * 显示基金详情页面
   * 
   * @return
   */
  @Action("/prjweb/funddetails/show")
  public String showFundDetails() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setEncryptedFundId(URLEncoder.encode(form.getEncryptedFundId(), "utf-8"));
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId())));
      }
      if (form.getFundId() == null || form.getFundId() == 0) {
        return "fundNotExist";
      }
      if (form.getPsnId() != null && form.getPsnId() > 0l && form.getFundId() != null && form.getFundId() > 0l) {
        FundRecommendForm result = fundRecommendService.buildFundDetailsInfo(form);
        if (result == null) {
          return "fundNotExist";
        }
      }
      if (form.getPsnId() == null || form.getPsnId() == 0l) {
        Struts2Utils.getResponse()
            .sendRedirect(domainScm + "/prjweb/outside/showfund?encryptedFundId=" + form.getEncryptedFundId());
      }
      // 判断基金是否过期
      String showDate = form.getConstFundCategoryInfo().getShowDate();
      if (StringUtils.isNotBlank(showDate)) {
        String strLastDate = showDate.substring(showDate.indexOf("~") + 2);
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        Date lastDate = DATE_FORMAT.parse(strLastDate);
        Date now = new Date();
        if (now.after(lastDate)) {
          form.setIsStaleDated(true);
        }
      }
      buildSEOTitle(form);
    } catch (Exception e) {
      logger.error("显示基金详情出错， fundId = " + form.getFundId(), e);
    }
    return "fundDetails";
  }

  private void buildSEOTitle(FundRecommendForm form) {
    Locale locale = LocaleContextHolder.getLocale();
    String fundName = form.getConstFundCategoryInfo().getFundTitle();
    if (StringUtils.isNoneBlank(fundName)) {
      form.setSeoTitle(
          HtmlUtils.htmlUnescape(fundName) + " | " + ("en_US".equals(locale.toString()) ? "Smate" : "科研之友"));
    }
  }

  /**
   * 站外-------显示基金详情页面
   * 
   * @return
   */
  @Action("/prjweb/outside/showfund")
  public String showFundDetailsOutSide() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setEncryptedFundId(URLEncoder.encode(form.getEncryptedFundId(), "utf-8"));
      if (form.getPsnId() != null && form.getPsnId() > 0l) {
        Struts2Utils.getResponse()
            .sendRedirect(domainScm + "/prjweb/funddetails/show?encryptedFundId=" + form.getEncryptedFundId());
        return null;
      }
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId())));
      }
      if (form.getFundId() != null && form.getFundId() > 0l) {
        FundRecommendForm fundRecommendForm = fundRecommendService.buildFundDetailsInfo(form);
        if (Objects.isNull(fundRecommendForm)) {
          return "fundNotExist";
        }
      }
    } catch (Exception e) {
      logger.error("显示基金详情出错， fundId = " + form.getFundId(), e);
    }
    return "fundDetails";
  }

  /**
   * 更新分享统计数
   * 
   * @return
   */
  @Action("/prjweb/fund/ajaxsharecount")
  public String fundShareCount() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getDes3FundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId())));
      }
      fundRecommendService.updateFundShareCount(form);
      result.put("result", "success");
    } catch (Exception e) {
      logger.error("更新基金分享统计数出错， fundId = " + form.getFundId() + ", psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    result.put("shareCount", form.getShareCount());
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 初始化基金收藏的状态
   * 
   * @return
   */
  @Action("/prjweb/fund/initCollectStatus")
  public String initFundCollectedStatus() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      result = fundRecommendService.initFundCollectedStatus(form);
    } catch (Exception e) {
      logger.error("查询基金收藏状态出错， fundIds = " + form.getDes3FundIds() + ",  psnId = " + SecurityUtils.getCurrentUserId(),
          e);
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 初始化基金操作
   * 
   * @return
   */
  @Action("/prjweb/fundop/ajaxinit")
  public String initFundOperation() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getDes3FundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId())));
      }
      form = fundRecommendService.initFundOperations(form);
      result.put("result", "success");
    } catch (Exception e) {
      logger.error("查询基金收藏状态出错， fundIds = " + form.getDes3FundIds() + ",  psnId = " + SecurityUtils.getCurrentUserId(),
          e);
      result.put("result", "error");
    }
    result.put("hasAward", form.getHasAward());
    result.put("hasCollect", form.getHasCollected());
    result.put("shareCount", form.getShareCount());
    result.put("awardCount", form.getAwardCount());
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 更新基金阅读数
   * 
   * @return
   */
  @Action("/prjweb/fundop/ajaxinitReads")
  public String fundViewOpt() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getDes3FundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId())));
      }
      form = fundRecommendService.updateFundReadCount(form);
      result.put("result", "success");
    } catch (Exception e) {
      logger.error("查询基金收藏状态出错， fundIds = " + form.getDes3FundIds() + ",  psnId = " + SecurityUtils.getCurrentUserId(),
          e);
      result.put("result", "error");

    }
    result.put("readCount", form.getReadCount());
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;
  }

  /**
   * 首页基金推荐 不感兴趣操作
   * 
   * @param pubOperateVO
   * @return
   */
  @Action("/prjweb/fund/ajaxuninterestedcmd")
  public String notViewFundRecommend() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long fundId = form.getFundId();
    try {
      if (psnId > 0) {
        fundRecommendService.insertFundRecmRecord(psnId, fundId);
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("首页动态推荐基金不感兴趣操作出错,psnId=" + psnId + ",fundId=" + fundId, e);
    }
    Struts2Utils.renderJson(map, "Encoding:UTF-8");
    return null;
  }
}
