package com.smate.web.fund.action.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.service.wechat.FundQueryService;
import com.smate.web.prj.form.wechat.FundWeChatForm;
import com.smate.web.prj.model.wechat.FundWeChat;

/**
 * 基金信息数据接口
 * 
 * @author wsn
 * @date Jun 3, 2019
 */
public class FundDataAction extends ActionSupport implements ModelDriven<FundWeChatForm>, Preparable {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FundQueryService fundQueryService;
  private FundWeChatForm form;



  /**
   * 获取基金详情
   */
  @Action("/prjdata/share/fundinfo")
  public void findShareFundInfo() {
    Map<String, Object> result = new HashMap<String, Object>();
    String status = "error";
    try {
      Long fundId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3FundId()));
      if (NumberUtils.isNotNullOrZero(fundId)) {
        fundQueryService.queryFundInfoForShare(form);
        if (CollectionUtils.isNotEmpty(form.getResultList())) {
          FundWeChat fund = form.getResultList().get(0);
          result.put("title", StringUtils.defaultIfEmpty(fund.getFundName(), fund.getFundNameEn()));
          result.put("logo",
              StringUtils.defaultIfEmpty(fund.getLogoUrl(), "/ressns/images/default/default_fund_logo.jpg"));
          result.put("showDesc", StringUtils.defaultIfEmpty(fund.getZhShowDesc(), fund.getEnShowDesc()));
          result.put("agencyName", StringUtils.defaultIfEmpty(fund.getFundAgency(), fund.getFundAgencyEn()));
          result.put("time", StringUtils.defaultIfEmpty(fund.getTime(), ""));
          status = "success";
        }
      }
    } catch (Exception e) {
      logger.error("获取分享所需基金信息异常,des3FundId={}", form.getDes3FundId(), e);
    }
    result.put("status", status);
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundWeChatForm();
    }

  }

  @Override
  public FundWeChatForm getModel() {
    return form;
  }
}
