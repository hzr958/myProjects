package com.smate.web.fund.action.wechat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WeChatUtilsService;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.service.recommend.FundRecommendService;
import com.smate.web.fund.service.wechat.FundWeChatQueryService;
import com.smate.web.prj.exception.FundExcetpion;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.wechat.FundWeChatForm;

/*
 * 信息获取
 * 
 * @author zjh
 * 
 */
@Results({@Result(name = "wechat_funds", location = "/WEB-INF/jsp/fund/mobile/wechat_fund.jsp"),
    @Result(name = "wechat_myfunds", location = "/WEB-INF/jsp/fund/mobile/wechat_myfund.jsp"),
    @Result(name = "wechat_fund_conditions", location = "/WEB-INF/jsp/fund/mobile/wechat_fund_conditions.jsp"),
    @Result(name = "edit_area_conditions", location = "/WEB-INF/jsp/fund/mobile/edit_area_conditions.jsp"),
    @Result(name = "fundfind_editarea", location = "/WEB-INF/jsp/fund/mobile/fundfind_edit_area_conditions.jsp"),
    @Result(name = "wechat_funds_sub", location = "/WEB-INF/jsp/fund/mobile/wechat_fund_sub.jsp"),
    @Result(name = "wechat_myfunds_sub", location = "/WEB-INF/jsp/fund/mobile/wechat_myfund_sub.jsp"),
    @Result(name = "wechat_fundsxml", location = "/WEB-INF/jsp/fund/mobile/wechat_fundxml.jsp"),
    @Result(name = "wechat_fund_area", location = "/WEB-INF/jsp/fund/mobile/wechat_fund_area.jsp"),
    @Result(name = "wechat_fund_area_choose", location = "/WEB-INF/jsp/fund/mobile/wechat_fund_area_choose.jsp"),
    @Result(name = "wechat_fund_area_search", location = "/WEB-INF/jsp/fund/mobile/wechat_fund_area_search.jsp"),
    @Result(name = "edit_agency_conditions", location = "/WEB-INF/jsp/fund/mobile/edit_agency_conditions.jsp"),
    @Result(name = "redirectUrl", location = "${forwardUrl}", type = "redirect")})
public class FundWeChatAction extends WechatBaseAction implements ModelDriven<FundWeChatForm>, Preparable {

  private static final long serialVersionUID = 3327684079169476491L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private FundWeChatForm form;
  @Autowired
  private FundWeChatQueryService fundWeChatQueryservice;
  @Autowired
  private WeChatUtilsService weChatUtilsService;
  private String forwardUrl;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private FundRecommendService fundRecommendService;

  /**
   * 获取推荐基金列表
   * 
   * @return
   */
  @Action("/prjweb/wechat/findfunds")
  public String queryfunds() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      fundWeChatQueryservice.initFundCondition(form);
    } catch (PrjException e) {
      logger.error("初始化基金条件出错", e);
    }
    return "wechat_funds";

  }

  /**
   * 获取收藏的基金列表
   * 
   * @return
   */
  @Action("/prjweb/wechat/myfunds")
  public String myfunds() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      fundWeChatQueryservice.initFundCondition(form);
    } catch (PrjException e) {
      logger.error("初始化基金条件出错", e);
    }
    return "wechat_myfunds";

  }


  /**
   * 获取推荐基金条件
   * 
   * @return
   */
  @Action("/prjweb/wechat/findfundcondition")
  public String findfundcondition() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      fundWeChatQueryservice.getFundCondition(form);
    } catch (PrjException e) {
      logger.error("获取基金条件出错", e);
    }
    return "wechat_fund_conditions";

  }

  /**
   * 编辑基金科技领域
   * 
   * @return
   */
  @Action("/prjweb/wechat/editcoditionsarea")
  public String editCoditionsArea() {
    try {
      fundWeChatQueryservice.getAllScienceArea(form);
    } catch (PrjException e) {
      logger.error("获取基金条件出错", e);
    }
    return "edit_area_conditions";

  }

  /**
   * 基金发现 -- 编辑科技领域
   * 
   * @return
   */
  @Action("/prjweb/wechat/fundfind/editsarea")
  public String editCoditionsAreaByFundFind() {
    try {
      fundWeChatQueryservice.getAllScienceArea(form);
    } catch (PrjException e) {
      logger.error("获取基金条件出错", e);
    }
    return "fundfind_editarea";

  }


  /**
   * 保存基金关注资助机构
   * 
   * @return
   */
  @Action("/prjweb/wechat/ajaxsavepsnagency")
  public String savePsnAgency() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() > 0) {
        result = fundWeChatQueryservice.savePsnAgency(form);
      } else {
        result.put("result", "error");
      }
    } catch (PrjException e) {
      result.put("result", "error");
      logger.error("获取基金条件出错", e);
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;

  }

  /**
   * 编辑基金科技领域
   * 
   * @return
   */
  @Action("/prjweb/wechat/ajaxsavepsnarea")
  public String ajaxSavePsnArea() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() > 0) {
        fundWeChatQueryservice.savePsnArea(form);
        resultMap.put("result", "success");
      }
    } catch (Exception e) {
      resultMap.put("result", "error");
      logger.error("移动端保存科技领域设置出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;

  }

  /**
   * 获取基金收藏列表
   * 
   * @return
   */
  @Action("/prjweb/wechat/ajaxmyfundlist")
  public String ajaxMyFundList() {
    try {
      /* form.setOpenid("odGqjxLl5YCJ286-19r4pEe04t4I"); */
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
      }
      if (SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest())) {
        this.handleWxJsApiTicket(this.getDomain() + "/prjweb/wechat/myfunds" + this.handleRequestParams());
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      fundWeChatQueryservice.searchMyFund(form);
    } catch (FundExcetpion e) {
      logger.error("获取基金列表出错", e);
    } catch (Exception e) {
      logger.error("获取基金列表出错", e);
    }
    return "wechat_myfunds_sub";

  }

  @Action("/prjweb/wechat/ajaxfindfunds")
  public String queryfundList() {
    try {
      /* form.setOpenid("odGqjxLl5YCJ286-19r4pEe04t4I"); */
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
      }
      if (SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest())) {
        this.handleWxJsApiTicket(this.getDomain() + "/prjweb/wechat/findfunds" + this.handleRequestParams());
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      fundWeChatQueryservice.queryFundForWeChat(form);
    } catch (FundExcetpion e) {
      logger.error("获取基金列表出错", e);
    } catch (Exception e) {
      logger.error("获取基金列表出错", e);
    }
    return "wechat_funds_sub";

  }

  /**
   * 获取推荐基金详情
   * 
   * @return
   */
  @Action("/prjweb/wechat/findfundsxml")
  public String queryfundxml() {
    try {
      if (SecurityUtils.getCurrentUserId() > 0) {
        form.setHasLogin(1);
      }
      fundWeChatQueryservice.queryFundxml(form);
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
      }
      String queryString = StringUtils.isNotBlank(Struts2Utils.getRequest().getQueryString())
          ? "?" + Struts2Utils.getRequest().getQueryString()
          : "";
      // 改在页面用ajax处理
      // this.handleWxJsApiTicket(this.getDomain() + "/prjweb/wechat/findfundsxml" + queryString);
      form.setLoginTargetUrl(Des3Utils.encodeToDes3("/prjweb/wechat/findfundsxml" + queryString));
    } catch (Exception e) {
      logger.error("获取基金详情出错,fundId=" + form.getDes3FundId(), e);
    }
    return "wechat_fundsxml";
  }

  @Action("/prjweb/wechat/showallarea")
  public String showAllArea() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
      }
      // this.handleWxJsApiTicket(this.getDomain() +
      // "/prjweb/wechat/filterarea" + this.handleRequestParams());
      form.setFundRegionList(fundWeChatQueryservice.getFundRegion(form.getPsnId()));// 获取设置的地区
      fundWeChatQueryservice.queryAllProvince(form);
    } catch (Exception e) {
      logger.error("获取基金列表出错", e);
    }
    return "wechat_fund_area";

  }

  @Action("/prjweb/wechat/editagencyinterest")
  public String editAgencyInterest() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
      }
      // this.handleWxJsApiTicket(this.getDomain() +
      // "/prjweb/wechat/filterarea" + this.handleRequestParams());
      fundWeChatQueryservice.editFundAgencyInterest(form);
    } catch (Exception e) {
      logger.error("获取基金列表出错", e);
    }
    return "edit_agency_conditions";

  }

  @Action("/prjweb/wechat/queryareanext")
  public String queryNext() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
      }
      // this.handleWxJsApiTicket(this.getDomain() +
      // "/prjweb/wechat/filterarea" + this.handleRequestParams());
      form.setFundRegionList(fundWeChatQueryservice.getFundRegion(form.getPsnId()));// 获取设置的地区
      fundWeChatQueryservice.queryAreaNext(form);
    } catch (Exception e) {
      logger.error("获取基金列表出错", e);
    }
    return "wechat_fund_area_choose";

  }

  /**
   * 删除关注地区
   * 
   * @return
   */
  @Action("/prjweb/wechat/ajaxdeleteregion")
  public String ajaxDeleteRegion() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
      }
      /* result = fundWeChatQueryservice.deleteRegion(form.getRegionCode()); */
      result.put("result", "success");
    } catch (Exception e) {
      logger.error("获取基金列表出错", e);
    }
    Struts2Utils.renderJson(result, "Encoding:UTF-8");
    return null;

  }

  @Action("/prjweb/wechat/ajaxsearchregion")
  public String searchRegion() {
    try {
      if (StringUtils.isNotBlank(form.getSearchKey())) {
        if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
          this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        }
        // this.handleWxJsApiTicket(this.getDomain() +
        // "/prjweb/wechat/filterarea" + this.handleRequestParams());
        fundWeChatQueryservice.searchRegion(form);
      }
    } catch (Exception e) {
      logger.error("获取基金列表出错", e);
    }
    return "wechat_fund_area_search";

  }

  @Action("/prjweb/mobile/savefundarea")
  public String saveFundArea() throws IOException {
    try {
      Long userId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(form.getAreaIds()) && userId > 0) {
        String[] split = form.getAreaIds().split(",");
        if (split != null && split.length > 0) {
          FundRecommendForm recommendForm = new FundRecommendForm();
          for (String one : split) {
            recommendForm.setRegionCode(one);
            try {
              /* fundRecommendService.fundConditionsRegionSave(recommendForm); */
            } catch (Exception e) {
              logger.error("基金过滤选项-地区保存 单条记录保存 出错，RegionCode=" + one, e);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("移动端保存基金推荐的地区条件出错", e);
    }
    Struts2Utils.getResponse().sendRedirect("/prjweb/wechat/showallarea");
    return null;
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

  public FundWeChatForm getForm() {
    return form;
  }

  public void setForm(FundWeChatForm form) {
    this.form = form;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public String getDomainScm() {
    return domainScm;
  }

  public void setDomainScm(String domainScm) {
    this.domainScm = domainScm;
  }

}
