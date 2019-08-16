package com.smate.web.fund.action.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.service.agency.FundAgencyService;
import com.smate.web.fund.service.recommend.FundRecommendService;

/**
 * 基金操作接口
 * 
 * @author wsn
 * @date May 17, 2019
 */
public class FundOptDataAction extends ActionSupport implements ModelDriven<FundRecommendForm>, Preparable {

  private static final long serialVersionUID = 2333970976100254966L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FundRecommendForm form;
  @Autowired
  private FundRecommendService fundRecommendService;
  @Autowired
  private FundAgencyService fundAgencyService;
  @Value("${domainscm}")
  private String domainScm;


  /**
   * 收藏/取消收藏操作
   * 
   * @return
   */
  @Action("/prjdata/fund/collect")
  public void recommendFundCollection() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      Long fundId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedFundId()), 0L);
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedPsnId()), 0L);
      if (NumberUtils.isNotNullOrZero(fundId) && NumberUtils.isNotNullOrZero(psnId)
          && form.getCollectOperate() != null) {
        form.setFundId(fundId);
        form.setPsnId(psnId);
        form = fundRecommendService.fundCollectOperation(form);
        result.put("result", "success");
      } else {
        result.put("result", "error");
        form.setErrorMsg("psnId or fundId or optType is null");
      }
    } catch (Exception e) {
      logger.error("收藏基金操作出错， psnId = {}, fundId = {}, optType={}", form.getEncryptedPsnId(), form.getEncryptedFundId(),
          form.getCollectOperate(), e);
      result.put("result", "error");
    }
    result.put("errorMsg", form.getErrorMsg());
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
  }



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

}
