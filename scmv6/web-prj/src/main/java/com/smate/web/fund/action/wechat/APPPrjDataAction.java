package com.smate.web.fund.action.wechat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.service.recommend.FundRecommendService;
import com.smate.web.fund.service.wechat.FundWeChatQueryService;
import com.smate.web.prj.form.wechat.FundWeChatForm;

/**
 * APP基金显示相关数据接口
 * 
 * @author LJ
 *
 *         2017年10月23日
 */
@Results({@Result(name = "FundDetails", location = "/WEB-INF/jsp/fund/app_fundxml.jsp")})
public class APPPrjDataAction extends ActionSupport implements ModelDriven<FundRecommendForm>, Preparable {

  private static final long serialVersionUID = -3921137430789090851L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private FundRecommendForm form;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private FundRecommendService fundRecommendService;
  @Autowired
  private FundWeChatQueryService fundWeChatQueryservice;

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
   * 显示基金科技领域条件
   * 
   * @return
   */
  @Action("/prjdata/recommend/getpsnfundagency")
  public String getPsnFundAgency() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() == null && form.getEncryptedPsnId() != null) {
        form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedPsnId())));
      }
      if (form.getPsnId() != null) {
        fundRecommendService.fundRecommendConditionsShow(form);
        map.put("result", form.getFundAgencyInterestList());
        if (form.getFundAgencyInterestList() != null) {
          map.put("total", form.getFundAgencyInterestList().size());
        }
      }
      map.put("status", "success");
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("显示基金科技领域弹出框出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    Struts2Utils.renderJson(map, "Encoding:UTF-8");
    return null;
  }

  /**
   * 保存资助机构
   * 
   * @return
   */
  @Action("/prjdata/recommend/savefundagency")
  public String saveFundAgency() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() == null && form.getEncryptedPsnId() != null) {
        form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedPsnId())));
      }
      if (form.getPsnId() != null && form.getSaveAgencyIds() != null) {
        map = fundRecommendService.saveFundConditionsFundAgencyInterest(form);
        if (StringUtils.isNotBlank(Objects.toString(map.get("result")))) {
          map.put("status", map.get("result"));
        }
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("显示基金科技领域弹出框出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    Struts2Utils.renderJson(map, "Encoding:UTF-8");
    return null;
  }

  /**
   * 编辑资助机构
   * 
   * @return
   */
  @Action("/prjdata/recommend/editfundagency")
  public String editfundagency() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() == null && form.getEncryptedPsnId() != null) {
        form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getEncryptedPsnId())));
      }
      if (form.getPsnId() != null) {
        LocaleContextHolder.setLocale(new Locale("zh_CN"));
        FundWeChatForm fundForm = new FundWeChatForm();
        fundForm.setPsnId(form.getPsnId());
        fundWeChatQueryservice.editFundAgencyInterest(fundForm);
        map.put("fundAgencyInterestList", fundForm.getFundAgencyInterestList());
        map.put("psnAgencyIds", fundForm.getPsnAgencyIds());
        map.put("fundAgencyMapList", fundForm.getFundAgencyMapList());
      }
      map.put("status", "success");
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("显示基金科技领域弹出框出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    Struts2Utils.renderJson(map, "Encoding:UTF-8");
    return null;
  }

}
