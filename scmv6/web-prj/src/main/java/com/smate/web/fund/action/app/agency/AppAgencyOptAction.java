package com.smate.web.fund.action.app.agency;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.agency.model.AgencyStatistics;
import com.smate.web.fund.agency.model.FundAgencyForm;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.service.agency.FundAgencyService;
import com.smate.web.fund.service.recommend.FundRecommendService;
import com.smate.web.prj.consts.FundAgencyConst;

/**
 * 资助机构操作Action
 * 
 * @author wsn
 * @date Nov 13, 2018
 */
public class AppAgencyOptAction extends ActionSupport implements ModelDriven<FundAgencyForm>, Preparable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private FundAgencyForm form;
  @Autowired
  private FundAgencyService fundAgencyService;
  @Autowired
  private FundRecommendService fundRecommendService;

  /**
   * 赞、取消赞资助机构操作
   * 
   * @return
   */
  @Action("/prjdata/agency/award")
  public String awardAgencyOpt() {
    Long currentPsnId = form.getPsnId();
    Map<String, String> result = new HashMap<String, String>();
    String optResult = "success";
    AgencyStatistics stat = null;
    try {
      // 更新赞、取消赞资助机构记录
      String dealResult = fundAgencyService.dealWithAwardOpt(currentPsnId, form.getFundAgencyId(), form.getOptType());
      if (StringUtils.isBlank(dealResult)) {
        // 是增加还是减少统计数
        Integer addOrResuce = CommonUtils.compareIntegerValue(form.getOptType(), FundAgencyConst.AWARD_AGENCY_OPT)
            ? FundAgencyConst.ADD_AGENCY_STATISTICS
            : FundAgencyConst.REDUCE_AGENCY_STATISTICS;
        // 更新资助机构赞统计数
        stat = fundAgencyService.updateAgencyStatistics(form.getFundAgencyId(), false, true, false, addOrResuce, 1);
      } else {
        stat = fundAgencyService.findAgencyStatistics(form.getFundAgencyId());
        form.setErrorMsg(dealResult);
      }
    } catch (Exception e) {
      logger.error(
          "赞资助机构操作失败，psnId = " + currentPsnId + ", agencyId=" + form.getFundAgencyId() + ", opt =" + form.getOptType(),
          e);
      optResult = "error";
      form.setErrorMsg("catch a exception");
    }
    result.put("result", optResult);
    result.put("errorMsg", form.getErrorMsg());
    buildAgencyStatisticsReturnInfo(result, stat);
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

  /**
   * 关注、取消关注资助机构操作
   * 
   * @return
   */
  @Action("/prjdata/agency/interest")
  public String interestAgencyOpt() {
    Long currentPsnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
    Map<String, String> result = new HashMap<String, String>();
    String optResult = "success";
    AgencyStatistics stat = null;
    try {
      // 处理关注、取消关注操作
      String dealResult =
          fundAgencyService.dealWithInterestOpt(currentPsnId, form.getFundAgencyId(), form.getOptType());
      if (StringUtils.isBlank(dealResult)) {
        // 是增加还是减少统计数
        Integer addOrResuce = CommonUtils.compareIntegerValue(form.getOptType(), FundAgencyConst.INTEREST_AGENCY_OPT)
            ? FundAgencyConst.ADD_AGENCY_STATISTICS
            : FundAgencyConst.REDUCE_AGENCY_STATISTICS;
        // 更新资助机构分享数
        stat = fundAgencyService.updateAgencyStatistics(form.getFundAgencyId(), false, false, true, addOrResuce, 1);
      } else {
        stat = fundAgencyService.findAgencyStatistics(form.getFundAgencyId());
        form.setErrorMsg(dealResult);
        optResult = "error";
      }
    } catch (Exception e) {
      logger.error(
          "关注资助机构操作失败，psnId = " + currentPsnId + ", agencyId=" + form.getFundAgencyId() + ", opt =" + form.getOptType(),
          e);
      optResult = "error";
      form.setErrorMsg("catch a exception");
    }
    result.put("result", optResult);
    result.put("errorMsg", form.getErrorMsg());
    buildAgencyStatisticsReturnInfo(result, stat);
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

  // 构建操作统计数返回值
  private void buildAgencyStatisticsReturnInfo(Map<String, String> result, AgencyStatistics stat) {
    Map<String, String> optCount = new HashMap<String, String>();
    if (stat != null) {
      optCount.put("awardCount", stat.getAwardSum() == null ? "0" : stat.getAwardSum().toString());
      optCount.put("shareCount", stat.getShareSum() == null ? "0" : stat.getShareSum().toString());
      optCount.put("interestCount", stat.getInterestSum() == null ? "0" : stat.getInterestSum().toString());
    } else {
      optCount.put("awardCount", "0");
      optCount.put("shareCount", "0");
      optCount.put("interestCount", "0");
    }
    result.put("optCount", JacksonUtils.jsonMapSerializer(optCount));
  }

  /**
   * 更新分享统计数和操作记录
   * 
   * @return
   */
  @Action("/prjdata/share/ajaxupdate")
  public String ajaxUpdateShareInfo() {
    Long currentPsnId = form.getPsnId();
    Map<String, String> result = new HashMap<String, String>();
    String dealResult = "success";
    AgencyStatistics stat = null;
    try {
      form.setCurrentPsnId(currentPsnId);
      // 新增分享记录
      fundAgencyService.dealWithShareOpt(form);
      if (StringUtils.isBlank(form.getErrorMsg())) {
        // 更新资助机构分享数
        stat = fundAgencyService.updateAgencyStatistics(form.getFundAgencyId(), true, false, false,
            FundAgencyConst.ADD_AGENCY_STATISTICS, 1);
      } else {
        stat = fundAgencyService.findAgencyStatistics(form.getFundAgencyId());
      }
    } catch (Exception e) {
      logger.error("更新分享资助机构记录失败，psnId = " + currentPsnId + ", agencyId=" + form.getFundAgencyId(), e);
      dealResult = "error";
      form.setErrorMsg("catch a exception");
    }
    result.put("result", dealResult);
    result.put("errorMsg", form.getErrorMsg());
    buildAgencyStatisticsReturnInfo(result, stat);
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

  @Action("/prjdata/getfundrecommendshowindyn")
  public String getFundRecommendShowInDyn() {
    Long psnId = form.getPsnId();
    FundRecommendForm fundForm = new FundRecommendForm();
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      fundForm.setPsnId(psnId);
      fundForm.setPageSize(1);
      fundForm.setPageNum(form.getPageNo());
      if (form.getPsnId() > 0) {
        fundRecommendService.initAgencyInterestAndAreaSeniority(psnId);
        fundRecommendService.fundRecommendListSearch(fundForm);
        fundRecommendService.buildDynFundRecommend(fundForm);
        result.put("status", "success");
        result.put("result", fundForm.getDynRemdResList());
      }
    } catch (Exception e) {
      result.put("status", "error");
      logger.error("获取首页动态基金推荐出错， psnId = " + psnId, e);
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

  /**
   * 首页基金推荐 不感兴趣操作
   * 
   * @param pubOperateVO
   * @return
   */
  @Action("/prjdata/funduninterested")
  public String notViewFundRecommend() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
    Long fundId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId()), 0L);
    try {
      if (psnId > 0 && fundId > 0) {
        fundRecommendService.insertFundRecmRecord(psnId, fundId);
        map.put("status", "success");
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("首页动态推荐基金不感兴趣操作出错,psnId=" + psnId + ",fundId=" + fundId, e);
    }
    Struts2Utils.renderJson(map, "Encoding:UTF-8");
    return null;
  }



  /**
   * 分享资助机构给好友
   * 
   * @return
   */
  @Action("/prjdata/shareagency/tofriend")
  public void shareAgencyToFriend() {
    Map<String, String> map = new HashMap<String, String>();
    String status = "error";
    AgencyStatistics stat = null;
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
    try {
      form.setCurrentPsnId(psnId);
      TheadLocalPsnId.setPsnId(psnId);
      form.setShareToPlatform(FundAgencyConst.SHARE_TO_FRIEND);
      fundAgencyService.shareAgencyToFriends(form);
      status = "success";
    } catch (Exception e) {
      logger.error("分享资助机构到好友出错， agencyId = {}, sharePsnId = {}, receivePsnIds = {}", form.getDes3AgencyIds(), psnId,
          form.getDes3ReceiverIds(), e);
      form.setErrorMsg("catch a exception");
    }
    map.put("errorMsg", form.getErrorMsg());
    map.put("status", status);
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundAgencyForm();
    }
  }

  @Override
  public FundAgencyForm getModel() {
    return form;
  }

}
