package com.smate.center.open.model.wechat.bind;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/*
 * @author zjh 微信绑定表单
 */
public class WeChatBindForm {
  private String wxOpenId;// 微信openid
  private String des3WxOpenId;// 微信加密openId
  private String openid;// 微信openId
  private String wxUnionId;// 微信unionId
  private String des3WxUnionId;// 微信加密unionId
  private Integer bindType;// 微信绑定方式 0-微信端 1-PC端 PC端通过开放平台，openId默认0
  private String userName;// 登录名
  private String password;// 密码
  private Long scmOpenId;// 科研之友openid
  private Long psnId;// 人员id
  private String from;// 从哪里来标识
  private Integer needValidateCode = 0;// 是否需要显示验证码
  private String validateCode;// 验证码
  private String msg;// 绑定后提示信息
  private boolean isSuccess;// 是否绑定成功
  private Integer isFirst = 1;// 是否第一次
  private String eventkey;// 微信调用事件key
  private String appId;// 微信appid
  // 页面调用微信js需要的参数---begin
  private String timestamp;// 必填，生成签名的时间戳
  private String nonceStr;// 必填，生成签名的随机串
  private String signature;// 必填，签名
  // 页面调用微信js需要的参数---end
  private String url; // 跳转url
  private String code; // 微信跳转过来的链接带的code，可用来获取微信用户openId
  private Boolean mobileCodeLogin = false; // 手机验证码登录
  private String mobileNum; // 手机号
  private String mobileCode; // 手机验证码
  private String AID; // 自动登录串

  public Boolean getMobileCodeLogin() {
    return mobileCodeLogin;
  }

  public void setMobileCodeLogin(Boolean mobileCodeLogin) {
    this.mobileCodeLogin = mobileCodeLogin;
  }

  public String getMobileNum() {
    return mobileNum;
  }

  public void setMobileNum(String mobileNum) {
    this.mobileNum = mobileNum;
  }

  public String getMobileCode() {
    return mobileCode;
  }

  public void setMobileCode(String mobileCode) {
    this.mobileCode = mobileCode;
  }

  public String getWxOpenId() {
    if (openid != null) {
      return openid;
    }
    if (StringUtils.isNotBlank(des3WxOpenId) && StringUtils.isBlank(wxOpenId)) {
      wxOpenId = Des3Utils.decodeFromDes3(des3WxOpenId);
    }
    return wxOpenId;
  }

  public void setWxOpenId(String wxOpenId) {
    this.wxOpenId = wxOpenId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getScmOpenId() {
    return scmOpenId;
  }

  public void setScmOpenId(Long scmOpenId) {
    this.scmOpenId = scmOpenId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getNeedValidateCode() {
    return needValidateCode;
  }

  public void setNeedValidateCode(Integer needValidateCode) {
    this.needValidateCode = needValidateCode;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public void setSuccess(boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public String getValidateCode() {
    return validateCode;
  }

  public void setValidateCode(String validateCode) {
    this.validateCode = validateCode;
  }

  public Integer getIsFirst() {
    return isFirst;
  }

  public void setIsFirst(Integer isFirst) {
    this.isFirst = isFirst;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getEventkey() {
    return eventkey;
  }

  public void setEventkey(String eventkey) {
    this.eventkey = eventkey;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getDes3WxOpenId() {
    return des3WxOpenId;
  }

  public void setDes3WxOpenId(String des3WxOpenId) {
    this.des3WxOpenId = des3WxOpenId;
  }

  public String getWxUnionId() {
    if (StringUtils.isNotBlank(des3WxUnionId) && StringUtils.isBlank(wxUnionId)) {
      wxUnionId = Des3Utils.decodeFromDes3(des3WxUnionId);
    }
    return wxUnionId;
  }

  public void setWxUnionId(String wxUnionId) {
    this.wxUnionId = wxUnionId;
  }

  public String getDes3WxUnionId() {
    return des3WxUnionId;
  }

  public void setDes3WxUnionId(String des3WxUnionId) {
    this.des3WxUnionId = des3WxUnionId;
  }

  public Integer getBindType() {
    return bindType;
  }

  public void setBindType(Integer bindType) {
    this.bindType = bindType;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getAID() {
    return AID;
  }

  public void setAID(String aID) {
    AID = aID;
  }



}
