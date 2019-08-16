package com.smate.center.open.action.wechat.bind;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
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
import com.smate.center.open.cache.OauthCacheService;
import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.wechat.bind.WeChatBindForm;
import com.smate.center.open.service.wechat.MessageHandlerService;
import com.smate.center.open.service.wechat.WxJsApiTicketService;
import com.smate.center.open.service.wechat.bind.WeChatBindService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;

/*
 * @author zjh 手机端 微信绑定action
 */
@Results({@Result(name = "bind", location = "/WEB-INF/jsp/open/wechat/wechat_bind.jsp"),
    @Result(name = "redirectUrl", location = "${forwardUrl}", type = "redirect"),
    @Result(name = "redirect", location = "${forwardUrl}", type = "redirect"),})
public class WeChatBindAction extends ActionSupport implements ModelDriven<WeChatBindForm>, Preparable {

  private static final long serialVersionUID = 1L;

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private String service;// 目标页面

  private WeChatBindForm form;
  @Autowired
  private WeChatBindService weChatBindService;
  @Autowired
  private MessageHandlerService messageHandlerService;
  @Autowired
  private WxJsApiTicketService wxJsApiTicketService;
  @Value("${domainscm}")
  private String domain;
  @Value("${domainoauth}")
  private String domainoauth;
  @Value("${wechat.appid}")
  private String appId;
  @Value("${domainMobile}")
  private String domainMobile;

  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private OauthCacheService oauthCacheService;

  /**
   * 微信绑定 ，因sessionId会变化，wxopenid和wxurl不能存入sessionId
   * 
   * @return
   */
  @Action("/open/wechat/bind")
  public String bindInterface() {
    // TODO需要更新wxOpenID
    try {
      service = Struts2Utils.getParameter("service");
      if ("reg".equals(form.getFrom())) {
        dealReqFromReg(form);
        return "bind";
      }
      // 预处理一些必要的参数
      dealWithParams();
      // 如果已经登录过 就直接 进入 默认首页
      String SYSID = Struts2Utils.getSession().getId();
      // 取缓存
      Object object = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, SYSID);
      if (object != null) {
        if (StringUtils.isNotBlank(service)) {
          String targetUrl = ServiceUtil.decodeFromDes3(service);
          if (StringUtils.isNotBlank(targetUrl)) {
            Struts2Utils.getResponse().sendRedirect(targetUrl);
            return null;
          }
        } else {
          Struts2Utils.redirect(domainMobile + "/dynweb/mobile/dynshow");
          return null;
        }
      }
      // 微信中访问绑定页面既没有微信unionId，也没有code，可以尝试获取code，后面用code获取微信unionId和微信openId
      // 绑定处理
      dealWithBindUser(form);
    } catch (Exception e) {
      form.setSuccess(false);
      form.setMsg("绑定失败!");
      logger.error("绑定失败!", e);
    }
    return "bind";
  }

  /**
   * 处理注册后跳转过来的请求
   * 
   * @param form
   * @return
   * @throws UnsupportedEncodingException
   */
  protected void dealReqFromReg(WeChatBindForm form) throws UnsupportedEncodingException {
    form.setSuccess(true);
    form.setMsg("你已注册并绑定成功");
    form.setAppId(this.appId);
    String wxUrl = Des3Utils.decodeFromDes3(URLEncoder.encode(this.form.getUrl(), "utf-8"));
    if (wxUrl == null) {
      if (service == null || "".equals(service)) {
        wxUrl = "/psnweb/mobile/homepage";
      } else {
        wxUrl = service;
      }
    }
    form.setUrl(wxUrl);
  }

  /**
   * 处理用户绑定关系
   * 
   * @param form
   */
  protected void dealWithBindUser(WeChatBindForm form) {
    if (form.getIsFirst() == 1) {
      form.setIsFirst(0);
      if (StringUtils.isBlank(form.getUrl()) && StringUtils.isNotBlank(service)) {
        form.setUrl(service);
      }
      if (StringUtils.isBlank(form.getUserName()) && StringUtils.isBlank(form.getPassword())) {// 第一次进来不需要走下面的的了
        return;
      }
    }
    weChatBindService.bindUserWithWxUnionId(form);
    if (form.isSuccess()) {
      form.setAppId(this.appId);
      String targetUrl = Des3Utils.decodeFromDes3(form.getUrl());
      if (StringUtils.isBlank(targetUrl)) {
        if (StringUtils.isBlank(service)) {
          targetUrl = "/dynweb/mobile/dynshow";
        } else {
          targetUrl = Des3Utils.decodeFromDes3(service);
          targetUrl = StringUtils.isNotBlank(targetUrl) ? targetUrl : service;
        }
      }
      logger.info("--------------------------------------targetUrl={}, service={}, url={}-------------------------",
          targetUrl, service, form.getUrl());
      form.setUrl(targetUrl);
    }
  }

  // 处理一些必要的参数
  protected void dealWithParams() {
    form.setBindType(0);
    // 有些是传递的加密的wxopenId
    if (StringUtils.isBlank(form.getWxOpenId()) && StringUtils.isNotBlank(form.getDes3WxOpenId())) {
      form.setWxOpenId(Des3Utils.decodeFromDes3(form.getDes3WxOpenId()));
    }
    if (StringUtils.isBlank(form.getWxUnionId()) && StringUtils.isNotBlank(form.getDes3WxUnionId())) {
      form.setWxUnionId(Des3Utils.decodeFromDes3(form.getDes3WxUnionId()));
    }
    Object errorNum = openCacheService.get(OpenConsts.LOGIN_CACHE, Struts2Utils.getSession().getId());
    if (errorNum != null && (int) errorNum >= 3) {
      form.setNeedValidateCode(1);
    }
  }



  /**
   * ajax方式登录
   * 
   * @return
   */
  @Action("/open/mobile/ajaxbind")
  public String loginBoxLoginToScm() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      // 校验账号密码
      dealWithBindUser(form);
      // 登录是否有问题
      if (!form.isSuccess()) {
        result.put("result", form.getMsg());
        result.put("needValidateCode", Objects.toString(form.getNeedValidateCode(), "0"));
      } else {
        // 成功更新会话ID
        // Struts2Utils.getRequest().changeSessionId();
        result.put("result", "success");
        result.put("targetUrl", form.getUrl());
      }
    } catch (Exception e) {
      logger.error("ajax绑定微信操作出错了", e);
      result.put("result", "serviceError");
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

  @Override
  public WeChatBindForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new WeChatBindForm();
    }
  }

  public WeChatBindForm getForm() {
    return form;
  }

  public void setForm(WeChatBindForm form) {
    this.form = form;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }
}
