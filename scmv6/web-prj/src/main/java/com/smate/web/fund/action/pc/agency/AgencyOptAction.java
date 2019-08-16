package com.smate.web.fund.action.pc.agency;

import java.util.HashMap;
import java.util.List;
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
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.agency.model.AgencyStatistics;
import com.smate.web.fund.agency.model.FundAgencyForm;
import com.smate.web.fund.service.agency.FundAgencyService;
import com.smate.web.prj.consts.FundAgencyConst;

/**
 * 资助机构操作Action
 * 
 * @author wsn
 * @date Nov 13, 2018
 */
public class AgencyOptAction extends ActionSupport implements ModelDriven<FundAgencyForm>, Preparable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private FundAgencyForm form;
  @Autowired
  private FundAgencyService fundAgencyService;

  /**
   * 赞、取消赞资助机构操作
   * 
   * @return
   */
  @Action("/prjweb/agency/ajaxaward")
  public String awardAgencyOpt() {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
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
        optResult = "error";
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
   * 移动端 --- 赞、取消赞资助机构操作
   * 
   * @return
   */
  @Action("/prjdata/agency/award")
  public String awardAgencyOptForMobile() {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
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
        optResult = "error";
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
  @Action("/prjweb/agency/ajaxinterest")
  public String interestAgencyOpt() {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
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
        optResult = "error";
        stat = fundAgencyService.findAgencyStatistics(form.getFundAgencyId());
        form.setErrorMsg(dealResult);
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

  /**
   * 初始化资助机构操作
   * 
   * @return
   */
  @Action("/prjweb/agency/ajaxinit")
  public String initAgencyOpt() {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      List<Map<String, Object>> initInfo = fundAgencyService.initAgencyOpt(form.getDes3AgencyIds(), currentPsnId);
      result.put("initInfo", initInfo);
      result.put("result", "success");
    } catch (Exception e) {
      logger.error("初始化资助机构操作失败, psnId = " + currentPsnId + ", des3Ids = " + form.getDes3AgencyIds(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

  // 构建操作统计数返回值
  private void buildAgencyStatisticsReturnInfo(Map<String, String> result, AgencyStatistics stat) {
    if (stat != null) {
      result.put("awardCount", stat.getAwardSum() == null ? "0" : stat.getAwardSum().toString());
      result.put("shareCount", stat.getShareSum() == null ? "0" : stat.getShareSum().toString());
      result.put("interestCount", stat.getInterestSum() == null ? "0" : stat.getInterestSum().toString());
    } else {
      result.put("awardCount", "0");
      result.put("shareCount", "0");
      result.put("interestCount", "0");
    }
  }

  /**
   * 分享资助机构给好友
   * 
   * @return
   */
  @Action("/prjweb/agency/ajaxshare")
  public String shareAgencyToPsn() {
    Map<String, String> map = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    AgencyStatistics stat = null;
    try {
      form.setCurrentPsnId(psnId);
      form.setShareToPlatform(FundAgencyConst.SHARE_TO_FRIEND);
      fundAgencyService.shareAgencyToFriends(form);
      if (StringUtils.isBlank(form.getErrorMsg())) {
        map.put("result", "success");
        stat = fundAgencyService.updateAgencyStatistics(form.getFundAgencyId(), true, false, false,
            FundAgencyConst.ADD_AGENCY_STATISTICS, 1);
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("分享资助机构到好友出错， agencyId = " + form.getDes3AgencyIds() + ", sharePsnId = " + psnId
          + ", receivePsnIds = " + form.getDes3ReceiverIds(), e);
      map.put("result", "error");
      form.setErrorMsg("catch a exception");
    }
    map.put("errorMsg", form.getErrorMsg());
    buildAgencyStatisticsReturnInfo(map, stat);
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取所有已关注的资助机构ID
   * 
   * @return
   */
  @Action("/prjweb/interest/ajaxall")
  public String ajaxInitInterest() {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String allInterestedAgencyIds = fundAgencyService.findPsnAllInterestAgencyIds(psnId);
      map.put("allInterestedId", allInterestedAgencyIds);
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("获取已关注的资助机构ID出错, psnId = " + psnId, e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取资助机构操作统计数
   * 
   * @return
   */
  @Action("/prjweb/agency/ajaxstat")
  public String ajaxAgencyStatistics() {
    Long agencyId = form.getFundAgencyId();
    Map<String, String> map = new HashMap<String, String>();
    AgencyStatistics stat = null;
    try {
      stat = fundAgencyService.findAgencyStatistics(form.getFundAgencyId());
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("获取资助机构操作统计数出错， agencyId = " + agencyId, e);
      map.put("result", "error");
    }
    buildAgencyStatisticsReturnInfo(map, stat);
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 更新分享统计数和操作记录
   * 
   * @return
   */
  @Action("/prjweb/share/ajaxupdate")
  public String ajaxUpdateShareInfo() {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
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
        dealResult = "error";
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
