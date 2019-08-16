package com.smate.web.prj.action.wechat;

import java.net.URLEncoder;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.prj.form.ProjectQueryForm;
import com.smate.web.prj.form.wechat.PrjWeChatForm;
import com.smate.web.prj.service.project.SnsProjectQueryService;
import com.smate.web.prj.service.wechat.PrjWeChatQueryService;

/*
 * @author zkj 项目信息获取
 */
@Results({@Result(name = "wechat_prjs", location = "/WEB-INF/jsp/prj/mobile/mobile_prj.jsp"),
    @Result(name = "wechat_prjs_sub", location = "/WEB-INF/jsp/prj/mobile/mobile_prj_sub.jsp"),
    @Result(name = "mobile_prj_conditions", location = "/WEB-INF/jsp/prj/mobile/mobile_prj_conditions.jsp"),
    @Result(name = "wechat_prjxml", location = "/WEB-INF/jsp/prj/wechat/wechat_prjxml.jsp"),
    @Result(name = "prjNotExit", location = "/WEB-INF/jsp/prj/mobile/prjNotExit.jsp"),
    @Result(name = "prjNoPrivacy", location = "/WEB-INF/jsp/prj/mobile/prjNoPrivacy.jsp"),
    @Result(name = "redirectUrl", location = "${forwardUrl}", type = "redirect")})
public class PrjWeChatAction extends WechatBaseAction implements ModelDriven<PrjWeChatForm>, Preparable {
  private static final long serialVersionUID = -6688611413646534069L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PrjWeChatForm form;
  @Autowired
  private PrjWeChatQueryService prjWeChatQueryService;
  private String forwardUrl;
  private String referer;
  @Autowired
  private SnsProjectQueryService snsProjectQueryService;

  @Actions(value = {@Action("/prjweb/wechat/prjmain"), @Action("/prjweb/outside/mobileotherprjs")})
  public String prjMain() {
    try {
      Long currentPsnId = SecurityUtils.getCurrentUserId();
      if (currentPsnId == 0) {
        form.setHasLogin(0);
      } else {
        if (StringUtils.isBlank(form.getDes3PsnId())) {
          form.setDes3PsnId(Des3Utils.encodeToDes3(currentPsnId.toString()));
        }

        form.setHasLogin(1);
        Long searchPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
        if (searchPsnId.equals(currentPsnId)) {
          form.setIsMyPrj(true);
        } else {
          form.setIsMyPrj(false);
        }
        // 获取当前查询是否有隐私项目
        Long prjCount = prjWeChatQueryService.queryPrivatePrjCount(searchPsnId);
        if (prjCount != null && prjCount > 0) {
          form.setHasPrivatePrj(true);
        }
      }
    } catch (Exception e) {
      logger.error("获取项目列表出错", e);
    }
    return "wechat_prjs";
  }

  // 项目列表
  @Action("/prjweb/wechat/findprjs")
  public String queryPrj() {
    try {
      Long currentPsnId = SecurityUtils.getCurrentUserId();
      form.setPsnId(currentPsnId);
      snsProjectQueryService.queryPrjList(form);
    } catch (Exception e) {
      logger.error("获取项目列表出错", e);
    }
    return "wechat_prjs_sub";
  }

  @Action("/prjweb/wechat/prjcondition")
  public String prjCondition() {
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      form.setCurrentYear(Calendar.getInstance().get(Calendar.YEAR));
      ProjectQueryForm formQuery = new ProjectQueryForm();
      if (form.getDes3PsnId() != null) {
        Long currentUserId = Long.valueOf(ServiceUtil.decodeFromDes3(form.getDes3PsnId()));
        formQuery.setSearchPsnId(currentUserId);
        if (!currentUserId.equals(psnId)) {
          formQuery.setOthersSee(true);
        }
      }
      snsProjectQueryService.queryAgencyName(formQuery);
      form.setAgencyNameList(formQuery.getAgencyNameList());// 设置查询资助机构名称
    } catch (Exception e) {
      logger.error("移动端获取项目查询条件出错 psnId={},seePsnId={}", psnId, form.getDes3PsnId(), e);
    }
    return "mobile_prj_conditions";
  }

  /* *//**
        * 站内查看他人项目列表
        * 
        * @return
        */

  /*
   * @Action("/prjweb/wechat/findotherprjs") public String queryOtherPrj() { if
   * (StringUtils.isNotBlank(form.getDes3PsnId())) {
   * form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId()))); }
   * form.setOther("true"); if (SecurityUtils.getCurrentUserId() > 0L) { form.setHasLogin(1); } if
   * (form.getPsnId() != null) { try { // 看是否有隐私的项目
   * form.setHasPrivatePrj(prjWeChatQueryService.hasPrivatePrj(form.getPsnId())); if
   * (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
   * this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
   * this.handleWxJsApiTicket( this.getDomain() + "/prjweb/wechat/findotherprjs" +
   * this.handleRequestParams()); } // 添加返回上一个界面 String tempReferer =
   * Struts2Utils.getRequest().getHeader("referer"); if (StringUtils.isBlank(tempReferer)) { referer =
   * "/prjweb/wechat/findotherprjs?des3PsnId=" + URLEncoder.encode(form.getDes3PsnId(), "utf-8"); }
   * else if (!tempReferer.contains("findotherprjs")) { referer = tempReferer; }
   * prjWeChatQueryService.queryPrjForWeChat(form); } catch (Exception e) { logger.error("获取项目列表出错",
   * e); } } if (form.getFlag() == 0) { return "wechat_prjs"; } else { return "wechat_prjs_sub"; }
   * 
   * }
   * 
   *//**
      * 站外查看他人项目列表
      * 
      * @return
      *//*
         * @Action("/prjweb/outside/ajaxotherprjs") public String queryOtherPrjOutside() { try { //
         * 已登录的跳转到站内地址 if (SecurityUtils.getCurrentUserId() > 0L &&
         * StringUtils.isNotBlank(form.getDes3PsnId())) {
         * Struts2Utils.getResponse().sendRedirect(this.getDomain() +
         * "/prjweb/wechat/findotherprjs?des3PsnId=" + URLEncoder.encode(form.getDes3PsnId(), "utf-8"));
         * return null; } if (StringUtils.isNotBlank(form.getDes3PsnId())) {
         * form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId()))); } if
         * (SecurityUtils.getCurrentUserId() > 0L) { form.setHasLogin(1); } form.setOther("true"); if
         * (form.getPsnId() != null) { // 看是否有隐私的项目
         * form.setHasPrivatePrj(prjWeChatQueryService.hasPrivatePrj(form.getPsnId())); if
         * (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
         * this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
         * this.handleWxJsApiTicket( this.getDomain() + "/prjweb/outside/mobileotherprjs" +
         * this.handleRequestParams()); } // 添加返回上一个界面 String tempReferer =
         * Struts2Utils.getRequest().getHeader("referer"); if (StringUtils.isBlank(tempReferer)) { referer =
         * "/prjweb/outside/mobileotherprjs?des3PsnId=" + form.getDes3PsnId(); } else if
         * (!tempReferer.contains("findotherprjs")) { referer = tempReferer; }
         * prjWeChatQueryService.queryPrjForWeChat(form); } form.setLoginTargetUrl(Des3Utils.encodeToDes3(
         * "/prjweb/wechat/findotherprjs?des3PsnId=" + URLEncoder.encode(form.getDes3PsnId(), "utf-8"))); }
         * catch (Exception e) { logger.error("获取项目列表出错, des3PsnId=" + form.getDes3PsnId(), e); } if
         * (form.getFlag() == 0) { return "wechat_prjs"; } else { return "wechat_prjs_sub"; } }
         */
  // 移动端站内项目详情
  @Action("/prjweb/wechat/findprjxml")
  public String queryPrjXml() {
    try {
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        // this.handleWxJsApiTicket(this.getDomain() +
        // "/prjweb/wechat/findprjxml" + this.handleRequestParams());
      }
      Long currentPsnId = SecurityUtils.getCurrentUserId();
      form.setHasLogin(currentPsnId == 0 ? 0 : 1);
      form.setCurrentPsnId(currentPsnId);
      if (form.getPsnId() == null || form.getPsnId() == 0L) {
        form.setPsnId(currentPsnId);
      }
      prjWeChatQueryService.queryPrjXml(form);
      prjWeChatQueryService.queryStaticAndcomment(form);
      if (SecurityUtils.getCurrentUserId() > 0) {
        form.setHasLogin(1);
      }
    } catch (Exception e) {
      logger.error("获取项目xml出错,prjid=" + form.getDes3PrjId(), e);
    }
    if ("notExists".equals(form.getResultMap().get("result"))) {
      return "prjNotExit";
    } else if ("noPrivacy".equals(form.getResultMap().get("result"))) {
      return "prjNoPrivacy";
    } else {
      return "wechat_prjxml";
    }
  }

  /**
   * 用prjweb/wechat/findprjxml
   * 
   * @return
   */
  @Deprecated
  // 移动端站外查看项目详情
  @Action("/prjweb/outside/mobileprjxml")
  public String queryPrjXmlOutside() {
    try {
      if (SecurityUtils.getCurrentUserId() > 0) {
        Struts2Utils.getResponse().sendRedirect(this.getDomain() + "/prjweb/wechat/findprjxml?des3PrjId="
            + URLEncoder.encode(form.getDes3PrjId(), "utf-8"));
        return null;
      }
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        // this.handleWxJsApiTicket(
        // this.getDomain() + "/prjweb/outside/mobileprjxml" +
        // this.handleRequestParams());
      }
      prjWeChatQueryService.queryPrjXml(form);
      form.setLoginTargetUrl(Des3Utils.encodeToDes3(
          this.getDomain() + "/prjweb/wechat/findprjxml?des3PrjId=" + URLEncoder.encode(form.getDes3PrjId(), "utf-8")));
    } catch (Exception e) {
      logger.error("获取项目xml出错,prjid=" + form.getDes3PrjId(), e);
    }
    if ("notExists".equals(form.getResultMap().get("result"))) {
      return "prjNotExit";
    } else if ("noPrivacy".equals(form.getResultMap().get("result"))) {
      return "prjNoPrivacy";
    } else {
      return "wechat_prjxml";
    }
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PrjWeChatForm();
    }

  }

  public PrjWeChatForm getForm() {
    return form;
  }

  public void setForm(PrjWeChatForm form) {
    this.form = form;
  }

  @Override
  public PrjWeChatForm getModel() {
    return form;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public String getReferer() {
    return referer;
  }

  public void setReferer(String referer) {
    this.referer = referer;
  }

}
