package com.smate.web.dyn.model.share;

import java.io.Serializable;

/**
 * 分享操作VO
 * 
 * @author wsn
 * @date May 23, 2019
 */
public class SmateShareForm implements Serializable {

  private String shareTo; // 分享到哪个模块， friend/dyn/group
  private String resType; // 分享的资源类型
  private String des3ResId; // 分享的资源加密ID
  private String des3FriendIds; // 分享的资源接收者ID, 多个用逗号拼接
  private String des3GrpId; // 分享到的群组的ID
  private String shareText; // 分享留言
  private String tempType; // 分享的动态模板类型
  private String errorMsg; // 错误信息
  private String errorCode; // 错误码
  private String des3CurrentPsnId; // 加密的当前登录人员ID
  private String domainMobile; // 移动端域名
  private String receiverEmails; // 接受者的email

  public String getShareTo() {
    return shareTo;
  }

  public void setShareTo(String shareTo) {
    this.shareTo = shareTo;
  }

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public String getDes3ResId() {
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }

  public String getDes3FriendIds() {
    return des3FriendIds;
  }

  public void setDes3FriendIds(String des3FriendIds) {
    this.des3FriendIds = des3FriendIds;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getShareText() {
    return shareText;
  }

  public void setShareText(String shareText) {
    this.shareText = shareText;
  }

  public String getDes3CurrentPsnId() {
    return des3CurrentPsnId;
  }

  public void setDes3CurrentPsnId(String des3CurrentPsnId) {
    this.des3CurrentPsnId = des3CurrentPsnId;
  }

  public String getTempType() {
    return tempType;
  }

  public void setTempType(String tempType) {
    this.tempType = tempType;
  }

  public String getDomainMobile() {
    return domainMobile;
  }

  public void setDomainMobile(String domainMobile) {
    this.domainMobile = domainMobile;
  }

  public String getReceiverEmails() {
    return receiverEmails;
  }

  public void setReceiverEmails(String receiverEmails) {
    this.receiverEmails = receiverEmails;
  }


}
