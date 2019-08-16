package com.smate.core.base.utils.wechat;

import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 微信基础Acton
 * 
 * @author zk
 *
 */
public class WechatBaseAction extends ActionSupport implements Preparable {

  private static final long serialVersionUID = -3228868456998699537L;
  private String wxOpenId;// 微信openid
  private String des3Wid;// 微信加密id
  // 页面调用微信js
  private String timestamp;// 必填，生成签名的时间戳
  private String nonceStr;// 必填，生成签名的随机串
  private String signature;// 必填，签名
  // private String appId;// 微信appid
  private String code;// 微信网页授权code
  @Value("${wechat.appid}")
  private String appId;
  @Value("${domainMobile}")
  private String domain;

  @Autowired
  private WeChatUtilsService weChatUtilsService;

  // 处理微信js-api票据
  public void handleWxJsApiTicket(String url) throws Exception {
    Map<String, String> map = weChatUtilsService.handleWxJsApiTicket(url);
    Assert.notNull(map.get("nonceStr"));
    Assert.notNull(map.get("timestamp"));
    Assert.notNull(map.get("signature"));
    this.setNonceStr(map.get("nonceStr"));
    this.setTimestamp(map.get("timestamp"));
    this.setSignature(map.get("signature"));
    // this.setAppId(this.appId);
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

  /**
   * 是否是在微信端浏览器里面请求的
   * 
   * @return
   */
  public boolean isWechatBrowser() {
    String userAgent = Struts2Utils.getRequest().getHeader("User-Agent");
    // 是否是移动端请求---PC端也有微信
    // boolean isMobileRequest =
    // SmateMobileUtils.isMobileBrowser(userAgent);
    // 是否是微信端请求
    return SmateMobileUtils.isWeChatBrowser(userAgent);
  }

  public String getWxOpenId() {
    return wxOpenId;
  }

  public void setWxOpenId(String wxOpenId) {
    this.wxOpenId = wxOpenId;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getNonceStr() {
    return nonceStr;
  }

  public void setNonceStr(String nonceStr) {
    this.nonceStr = nonceStr;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public WeChatUtilsService getWeChatUtilsService() {
    return weChatUtilsService;
  }

  public void setWeChatUtilsService(WeChatUtilsService weChatUtilsService) {
    this.weChatUtilsService = weChatUtilsService;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  @Override
  public void prepare() throws Exception {}

  public String getDes3Wid() {
    return des3Wid;
  }

  public void setDes3Wid(String des3Wid) {
    this.des3Wid = des3Wid;
  }

}
