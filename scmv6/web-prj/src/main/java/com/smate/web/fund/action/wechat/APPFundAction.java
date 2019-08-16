package com.smate.web.fund.action.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.recommend.model.FundRecommendArea;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.service.recommend.FundRecommendService;

/**
 * APP基金显示相关数据接口
 * 
 * @author LJ
 *
 *         2017年10月23日
 */
@Results({@Result(name = "FundDetails", location = "/WEB-INF/jsp/fund/app_fundxml.jsp")})
public class APPFundAction extends ActionSupport implements ModelDriven<FundRecommendForm>, Preparable {

  private static final long serialVersionUID = -3921137430789090851L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FundRecommendForm form;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private FundRecommendService fundRecommendService;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  private int total = 0;
  private String des3PsnId;

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundRecommendForm();
    }
    if (form.getPage() == null) {
      form.setPage(new Page<Long>());
    }
  }

  @Override
  public FundRecommendForm getModel() {
    return form;
  }

  public FundRecommendForm getForm() {
    return form;
  }

  public void setForm(FundRecommendForm form) {
    this.form = form;
  }

  /**
   * 保存基金推荐条件科技领域
   * 
   * @return
   */
  @Action("/app/prjweb/fundconditions/ajaxsavefundarea")
  public void saveFundConditionsScienceAreaSave() {
    Map<String, Object> result = new HashMap<String, Object>();
    List<FundRecommendArea> areaList = new ArrayList<FundRecommendArea>();
    try {
      status = IOSHttpStatus.OK;
      Long userId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isBlank(form.getAreaCodes()) || userId == null || userId == 0) {
        status = IOSHttpStatus.BAD_REQUEST;
      } else {
        if (StringUtils.isNotBlank(form.getAreaCodes())) {
          areaList.addAll(fundRecommendService.fundConditionsScienceAreaSave(form));
          result.put("areaList", areaList);
        }
      }
    } catch (Exception e) {
      status = IOSHttpStatus.BAD_REQUEST;
      logger.error("app保存推荐条件科技领域出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    AppActionUtils.renderAPPReturnJson(result, total, status);
  }

  /**
   * 删除基金推荐条件科技领域
   * 
   * @return
   */
  @Action("/app/prjweb/fundconditions/ajaxdeletefundarea")
  public void ajaxdeletefundarea() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      status = IOSHttpStatus.OK;
      Long userId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isBlank(form.getAreaCodes()) || userId == null || userId == 0) {
        status = IOSHttpStatus.BAD_REQUEST;
      } else {
        String[] areaCodeArr = form.getAreaCodes().split(",");
        if (areaCodeArr != null && areaCodeArr.length > 0) {
          for (String areaCode : areaCodeArr) {
            try {
              form.setAreaCode(areaCode);
              fundRecommendService.deleteFundScienceArea(form.getAreaCode());
            } catch (Exception e) {
              // 吃掉异常
            }
          }
        }
      }
    } catch (Exception e) {
      status = IOSHttpStatus.BAD_REQUEST;
      logger.error("app删除推荐条件关注地区出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    AppActionUtils.renderAPPReturnJson(result, total, status);
  }

  /**
   * 我的基金列表
   * 
   * @return
   */
  @Action("/app/prjweb/collectedfund/ajaxlist")
  public String showMyFund() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPageSize() == null || 0 == form.getPageSize()) {
        form.setPageSize(10);
      }
      fundRecommendService.showMyFund(form);
      status = IOSHttpStatus.OK;
      total = form.getPage().getTotalCount().intValue();
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("APP进入我的基金列表错误， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    AppActionUtils.renderAPPReturnJson(form.getFundInfoList(), total, status);
    return null;
  }

  /**
   * 保存基金推荐条件
   * 
   * @return
   */
  @Action("/app/prjweb/fundconditions/ajaxsave")
  public String saveFundRecommendConditions() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // form = fundRecommendService.fundRecommendConditionsSave(form);
      result.put("result", "success");
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app保存推荐条件编辑出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    AppActionUtils.renderAPPReturnJson(result.get("result"), total, status);
    return null;
  }

  /**
   * 删除基金推荐条件关注地区
   * 
   * @return
   */
  @Action("/app/prjweb/fundconditions/ajaxdeletefundregion")
  public void ajaxdeletefundregion() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      status = IOSHttpStatus.OK;
      Long userId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isBlank(form.getRegionCodes()) || userId == null || userId == 0) {
        status = IOSHttpStatus.BAD_REQUEST;
      } else {
        String[] split = form.getRegionCodes().split(",");
        if (split == null || split.length == 0) {
          status = IOSHttpStatus.BAD_REQUEST;
        } else {
          for (String one : split) {
            form.setRegionCode(one);
            try {
              /* fundRecommendService.deleteFundConditionRegion(form.getRegionCode()); */
            } catch (Exception e) {
              // 吃掉异常
              logger.error("删除基金推荐条件关注地区-单项- 出错，RegionCode=" + one, e);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("删除推荐条件关注地区出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      status = IOSHttpStatus.BAD_REQUEST;
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
  }

  /**
   * app保存基金推荐条件关注地区
   * 
   * @return
   */
  @Action("/app/prjweb/fundconditions/ajaxsavefundregion")
  public void saveFundConditionsRegionSave() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      status = IOSHttpStatus.OK;
      Long userId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isBlank(form.getRegionCodes()) || userId == null || userId == 0) {
        status = IOSHttpStatus.BAD_REQUEST;
      } else {
        List<Long> regionCodeList = new ArrayList<Long>();
        List<String> regionNameList = new ArrayList<String>();
        String[] split = form.getRegionCodes().split(",");
        if (split == null || split.length == 0) {
          status = IOSHttpStatus.BAD_REQUEST;
        } else {
          Map<String, Object> result = new HashMap<String, Object>();
          for (String one : split) {
            form.setRegionCode(one);
            try {
              /* result = fundRecommendService.fundConditionsRegionSave(form); */
              regionCodeList.addAll((List<Long>) result.get("regionCodeList"));
              regionNameList.addAll((List<String>) result.get("regionNameList"));
            } catch (Exception e) {
              // 吃掉异常
              logger.error("基金过滤选项-地区保存 单条记录保存 出错，RegionCode=" + one, e);
            }
          }
          map.put("regionCodeList", regionCodeList);
          map.put("regionNameList", regionNameList);
        }
        /*
         * List<FundRecommendRegion> allRegionList = fundRecommendService.getAllregion(form.getPsnId());//
         * 查询地区设置 map.put("allRegionList", allRegionList);
         */
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app保存基金推荐条件关注地区出错，regionCode=" + form.getRegionCodes(), e);
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
  }

  /**
   * 条件显示
   * 
   * @return
   */
  @Action("/app/prjweb/fundconditions/ajaxshow")
  public String showRecommendFundConditions() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      status = IOSHttpStatus.OK;
      fundRecommendService.fundRecommendConditionsShow(form);
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app获取条件编辑出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }

    map.put("fundAgencyInterestList", form.getFundAgencyInterestList());
    map.put("fundAreaList", form.getFundAreaList());
    map.put("seniorityCode", form.getSeniorityCode());

    map.put("regionInfo", form.getRegionInfo());
    map.put("fundScienceArea", form.getFundScienceArea());
    map.put("seniority", form.getSeniority());
    map.put("timeFlag", form.getTimeFlag());
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

  /**
   * 默认推荐基金列表
   * 
   * @return
   */
  @Action("/app/prjweb/fund/firstrecommend")
  public String firstRecommendFund() {
    try {
      // form.setFirstIn(true);
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // 先获取条件设置
      form = fundRecommendService.fundRecommendConditionsShow(form);
      if (form.getPageSize() == null || 0 == form.getPageSize()) {
        form.setPageSize(10);
      }
      form = fundRecommendService.fundRecommendListSearch(form);
      status = IOSHttpStatus.OK;
      total = form.getFundInfoList() == null ? 0 : form.getFundInfoList().size();
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app获取默认推荐基金列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    AppActionUtils.renderAPPReturnJson(form.getFundInfoList(), total, status);
    return null;
  }

  /**
   * 推荐基金列表
   * 
   * @return
   */
  @Action("/app/prjweb/fund/ajaxrecommend")
  public String searchRecommendFund() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPageSize() == null || 0 == form.getPageSize()) {
        form.setPageSize(10);
      }
      form = fundRecommendService.fundRecommendListSearch(form);
      status = IOSHttpStatus.OK;
      total = form.getTotalCount();
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("获取推荐基金列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    AppActionUtils.renderAPPReturnJson(form.getFundInfoList(), total, status);
    return null;
  }

  /**
   * 显示基金科技领域条件
   * 
   * @return
   */
  @Action("/app/prjweb/fundscience/ajaxbox")
  public String showFundScienceAreaBox() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      fundRecommendService.appBuildFundScienceAreaInfo(form);
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("显示基金科技领域弹出框出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    AppActionUtils.renderAPPReturnJson(form.getCategoryScmList(), total, status);
    return null;

  }

  /**
   * 显示基金详情页面
   * 
   * @return
   */
  @Action("/app/prjweb/funddetails/show")
  public String showFundDetails() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId())));
      }
      if (form.getPsnId() != null && form.getPsnId() > 0L && form.getFundId() != null && form.getFundId() > 0L) {
        fundRecommendService.buildFundDetailsInfo(form);
      }
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("显示基金详情出错， fundId = " + form.getFundId(), e);
    }
    return "FundDetails";
  }

  /**
   * 显示基金详情页面
   * 
   * @return
   */
  @Action("/app/prjweb/fund/query/detail")
  public void queryFundDetail() {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getDes3FundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId())));
      }
      if (form.getPsnId() != null && form.getPsnId() > 0L && form.getFundId() != null && form.getFundId() > 0L) {
        fundRecommendService.buildFundDetailsInfo(form);
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      AppActionUtils.renderAPPReturnJson("server error", total, status);
      logger.error("显示基金详情出错， fundId = " + form.getFundId(), e);
      return;
    }
    // return "FundDetails";
    if ("200".equals(status)) {
      resultMap.put("result", form);
      resultMap.put("status", "success");
    } else {
      resultMap.put("result", "显示基金详情出错， fundId = " + form.getFundId());
      resultMap.put("status", "500");
    }
    Struts2Utils.renderJsonNoNull(resultMap, "encoding:utf-8");
  }

  /**
   * 赞/取消赞操作
   * 
   * @return
   */
  @Action("/app/prjweb/fund/ajaxaward")
  public String awardRecommendFund() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId())));
      }
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0 && form.getFundId() != null && form.getFundId() > 0 && form.getAwardOperation() != null) {
        form.setPsnId(psnId);
        form = fundRecommendService.fundAwardOperation(form);
        result.put("awardCount", form.getAwardCount());
        result.put("hasAward", form.getHasAward());
        result.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        result.put("result", "error");
        status = IOSHttpStatus.BAD_REQUEST;

      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;

      logger.error("基金赞操作失败， psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    AppActionUtils.renderAPPReturnJson(result, total, status);
    return null;
  }

  /**
   * 收藏/取消收藏操作
   * 
   * @return
   */
  @Action("/app/prjweb/fund/ajaxcollect")
  public String recommendFundCollection() {
    Map<String, String> result = new HashMap<String, String>();
    try {
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
        status = IOSHttpStatus.OK;
      } else {
        result.put("result", "error");
        status = IOSHttpStatus.BAD_REQUEST;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("收藏基金操作出错， psnId = " + SecurityUtils.getCurrentUserId() + ", fundId = " + form.getFundId(), e);
      result.put("result", "error");
    }
    AppActionUtils.renderAPPReturnJson(result.get("result"), total, status);
    return null;
  }

  /**
   * 更新分享统计数
   * 
   * @return
   */
  @Action("/app/prjweb/fund/ajaxsharecount")
  public String fundShareCount() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId())));
      }
      if (form.getFundId() != null) {
        fundRecommendService.updateFundShareCount(form);
        result.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        result.put("result", "error");
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;

      logger.error("更新基金分享统计数出错， fundId = " + form.getFundId() + ", psnId = " + SecurityUtils.getCurrentUserId(), e);
      result.put("result", "error");
    }
    result.put("shareCount", form.getShareCount());
    AppActionUtils.renderAPPReturnJson(result, total, status);

    return null;
  }

  /**
   * 初始化基金操作
   * 
   * @return
   */
  @Action("/app/prjweb/fundop/ajaxinit")
  public String initFundOperation() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (form.getFundId() == null && StringUtils.isNotBlank(form.getEncryptedFundId())) {
        form.setFundId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId())));
      }
      if (form.getFundId() != null) {
        form = fundRecommendService.initFundOperations(form);
        result.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        result.put("result", "error");
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("查询基金收藏状态出错， fundIds = " + form.getDes3FundIds() + ",  psnId = " + SecurityUtils.getCurrentUserId(),
          e);
      result.put("result", "error");
    }
    result.put("awardStatus", form.getHasAward());
    result.put("hasCollect", form.getHasCollected());
    result.put("shareCount", form.getShareCount());
    result.put("awardCount", form.getAwardCount());
    AppActionUtils.renderAPPReturnJson(result, total, status);
    return null;
  }

}
