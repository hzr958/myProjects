package com.smate.center.oauth.action.proxy;

import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.model.bind.WeChatProxyForm;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * PC端 微信登录代理服务 开放平台 回调域只能设置一个，测试环境需要通过此Action实现回调
 * 
 * @author wuchuanwen
 *
 */
public class WeChatProxyAction extends ActionSupport implements ModelDriven<WeChatProxyForm>, Preparable {
  /**
   * 
   */
  private static final long serialVersionUID = -3972647437528442941L;
  @Value("${wechat.open.appid}")
  private String appid;
  @Value("${domainscm}")
  private String domainscm;
  private WeChatProxyForm form;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 请求转发
   * 
   * @return
   */
  @Action("/oauth/wechat/proxy")
  public String wechatProxy() {
    StringBuilder reqUrl = new StringBuilder("https://open.weixin.qq.com/connect/qrconnect");
    try {
      // 必须参数不能为空
      if (StringUtils.isNotBlank(form.getFrom()) && StringUtils.isNotBlank(form.getState())) {
        // 构造请求地址
        reqUrl.append("?appid=" + appid);
        reqUrl.append("&redirect_uri="
            + URLEncoder.encode(domainscm + "/oauth/wechat/proxyback?from=" + form.getFrom(), "utf-8"));
        reqUrl.append("&response_type=code");
        reqUrl.append("&scope=snsapi_login");
        reqUrl.append("&state=" + URLEncoder.encode(form.getState(), "utf-8"));
        reqUrl.append("#wechat_redirect");
        Struts2Utils.getResponse().sendRedirect(reqUrl.toString());
      }
    } catch (Exception e) {
      logger.error("转发请求出错，from：" + form.getFrom() + ", state:" + form.getState(), e);
    }
    return null;
  }

  /**
   * 请求 - 回调分发
   * 
   * @return
   */
  @Action("/oauth/wechat/proxyback")
  public String wechatProxyBack() {
    try {
      if (StringUtils.isNotBlank(form.getFrom())) {
        StringBuilder backUrl = new StringBuilder();
        switch (form.getFrom()) {
          case "dev": {
            backUrl.append("https://dev.scholarmate.com");
            break;
          }
          case "alpha": {
            backUrl.append("https://alpha.scholarmate.com");
            break;
          }
          case "test": {
            backUrl.append("https://test.scholarmate.com");
            break;
          }
          case "uat": {
            backUrl.append("https://uat.scholarmate.com");
            break;
          }
          default: {
            backUrl.append(domainscm);
            break;
          }
        }
        // 拼接参数
        backUrl.append("/oauth/wechat/callback");
        if (StringUtils.isNotBlank(form.getCode())) {
          backUrl.append("?code=");
          backUrl.append(URLEncoder.encode(form.getCode(), "utf-8"));
          backUrl.append("&state=");
          backUrl.append(URLEncoder.encode(form.getState(), "utf-8"));
        } else {
          backUrl.append("?state=");
          backUrl.append(URLEncoder.encode(form.getState(), "utf-8"));
        }
        Struts2Utils.getResponse().sendRedirect(backUrl.toString());
      }
    } catch (Exception e) {
      logger.error("分发请求出错，from：" + form.getFrom(), e);
    }
    return null;

  }

  @Override
  public void prepare() throws Exception {
    if (form == null)
      form = new WeChatProxyForm();
  }

  @Override
  public WeChatProxyForm getModel() {
    return form;
  }


}
