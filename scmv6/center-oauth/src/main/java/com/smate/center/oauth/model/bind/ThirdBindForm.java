package com.smate.center.oauth.model.bind;

import java.io.Serializable;
import java.util.Optional;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 微信绑定form对象
 * 
 * @author wcw
 */
public class ThirdBindForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1333007896862503106L;
  private Long psnId;// 人员Id
  private String des3PsnId;
  private String openid;// 第三方openId（微信，qq）
  private String des3Openid;
  private String unionid;// 微信unionId
  private String des3Unionid;
  private String qqUnionId;// QQunionId
  private String des3QQUnionId;
  private Integer bindType;// 微信绑定方式 0-微信端 1-PC端 PC端通过开放平台，openId默认0
  private String msg;// 返回信息说明
  private Long scmOpenId; // 科研之友openid
  private String userName;// 邮箱
  private String passwords;
  private String des3ThirdId;// type|thirdId
  private String wechatName;// 微信昵称
  private String qqName;// QQ昵称
  private String parentWindowUrl; // 父页面的url，绑定登录后跳转的页面url
  private String weiboUid; // 微博uid
  private String weiboNickname; // 微博昵称

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = Optional.ofNullable(des3PsnId).map(Des3Utils::decodeFromDes3)
          .map(des3id -> NumberUtils.parseLong(des3id, null)).orElse(null);
      if (psnId == null) {
        psnId = Optional.ofNullable((String) Struts2Utils.getSession().getAttribute("des3PsnId"))
            .map(Des3Utils::decodeFromDes3).map(des3id -> NumberUtils.parseLong(des3id, null)).orElse(null);
      }
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getQqUnionId() {
    return qqUnionId;
  }

  public void setQqUnionId(String qqUnionId) {
    this.qqUnionId = qqUnionId;
  }

  public String getDes3QQUnionId() {
    return des3QQUnionId;
  }

  public void setDes3QQUnionId(String des3qqUnionId) {
    des3QQUnionId = des3qqUnionId;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getDes3Openid() {
    return des3Openid;
  }

  public void setDes3Openid(String des3Openid) {
    this.des3Openid = des3Openid;
  }

  public String getUnionid() {
    return unionid;
  }

  public void setUnionid(String unionid) {
    this.unionid = unionid;
  }

  public String getDes3Unionid() {
    return des3Unionid;
  }

  public void setDes3Unionid(String des3Unionid) {
    this.des3Unionid = des3Unionid;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Long getScmOpenId() {
    return scmOpenId;
  }

  public void setScmOpenId(Long scmOpenId) {
    this.scmOpenId = scmOpenId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPasswords() {
    return passwords;
  }

  public void setPasswords(String passwords) {
    this.passwords = passwords;
  }

  public String getDes3ThirdId() {
    return des3ThirdId;
  }

  public void setDes3ThirdId(String des3ThirdId) {
    this.des3ThirdId = des3ThirdId;
  }

  public Integer getBindType() {
    return bindType;
  }

  public void setBindType(Integer bindType) {
    this.bindType = bindType;
  }

  public String getWechatName() {
    return wechatName;
  }

  public void setWechatName(String wechatName) {
    this.wechatName = wechatName;
  }

  public String getQqName() {
    return qqName;
  }

  public void setQqName(String qqName) {
    this.qqName = qqName;
  }

  public String getParentWindowUrl() {
    return parentWindowUrl;
  }

  public void setParentWindowUrl(String parentWindowUrl) {
    this.parentWindowUrl = parentWindowUrl;
  }

  public String getWeiboUid() {
    return weiboUid;
  }

  public void setWeiboUid(String weiboUid) {
    this.weiboUid = weiboUid;
  }

  public String getWeiboNickname() {
    return weiboNickname;
  }

  public void setWeiboNickname(String weiboNickname) {
    this.weiboNickname = weiboNickname;
  }


}
