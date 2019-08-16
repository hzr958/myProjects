package com.smate.web.psn.action;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.common.URIencodeUtils;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.psn.model.wechat.WechatForm;

/**
 * 处理微信签名
 * 
 * @author wsn
 * @date 2018年10月10日
 */
public class WechatSignatureAction extends WechatBaseAction implements ModelDriven<WechatForm>, Preparable {

  private static final long serialVersionUID = 2482279392480498631L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private WechatForm form;

  /**
   * 微信签名处理 传进来的url请在前台用encodeURIComponent编码一下， 不然被xss处理后，有可能链接中的&等符号会改变，到时候就 和页面上的url不一样导致签名失效了
   * 
   * @return
   */
  @Action("/psnweb/outside/ajaxsignature")
  public String dealWithWechatSignature() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      if (SmateMobileUtils.isWeChatBrowser(Struts2Utils.getRequest())) {
        this.handleWxJsApiTicket(URIencodeUtils.decodeURIComponent(form.getCurrentUrl()));
        result.put("result", "success");
        result.put("signature", this.getSignature());
        result.put("domain", this.getDomain());
        result.put("nonceStr", this.getNonceStr());
        result.put("timeStamp", this.getTimestamp());
        result.put("appId", this.getAppId());
      } else {
        result.put("result", "not wechat browser");
      }
    } catch (Exception e) {
      logger.error("处理微信签名出错", e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new WechatForm();
    }
  }

  /**
   * 处理微信js-api票据所需参数
   * 
   * jsapi签名与浏览器地址内容一致即可
   * 
   * @throws Exception
   */
  public String handleRequestParams() throws Exception {
    Map<String, String[]> paramMap = Struts2Utils.getRequest().getParameterMap();
    StringBuffer sb = new StringBuffer();
    sb.append("?");
    if (MapUtils.isNotEmpty(paramMap)) {
      for (String key : paramMap.keySet()) {
        String[] values = paramMap.get(key);
        sb.append(key + "=");
        // 只考虑第一个值
        if (values != null && values.length > 0) {
          sb.append(URLEncoder.encode(values[0], "utf-8"));
        }
        sb.append("&");
      }
    }
    return sb.toString().substring(0, sb.toString().length() - 1);
  }

  @Override
  public WechatForm getModel() {
    return form;
  }
}
