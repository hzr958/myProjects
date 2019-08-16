package com.smate.web.mobile.share.vo;

import java.io.Serializable;

import com.smate.core.base.utils.model.Page;

/**
 * 分享操作VO
 * 
 * @author wsn
 * @date May 23, 2019
 */
public class SmateShareVO implements Serializable {

  private String shareTo; // 分享到哪个模块， friend/dyn/group
  private String resType; // 分享的资源类型
  private String des3ResId; // 分享的资源加密ID
  private String des3FriendIds; // 分享的资源接收者ID, 多个用逗号拼接
  private String des3GrpId; // 分享到的群组的ID
  private String shareText; // 分享留言
  private String tempType; // 分享的动态模板类型
  private String errorMsg; // 错误信息
  private String errorCode; // 错误码
  private String warnCode; // 警告码
  private String warnMsg; // 警告信息
  private String des3CurrentPsnId; // 加密的当前登录人员ID
  private String domainMobile; // 移动端域名
  private String domainScm; // PC端域名
  private String receiverEmails; // 接受者的email
  private String resInfoJson; // json格式资源信息，基本不用了
  private Integer operatorType; // 操作类型，分享到动态用，暂时不太清楚是用于做什么的 // 操作类型 ， 1："评论了", 2"赞了", 3"分享了", 5"关注了"
  private ShareResShowInfo showInfo; // 分享界面显示的资源信息
  private String searchKey; // 检索字符
  private Page page = new Page();// 分页查询用
  private String currentDes3GrpId; // 进入分享页面前所在群组ID
  private String fromPage; // 从那个页面过来的
  private Integer hideModule;// 1、不显示分享到动态，2、不显示分享到好友，3、不显示分享到群组
  private boolean needUpdateStatistics = true; // 是否需要更新统计数
  private String grpResDes3GrpId; // 群组资源所属群组ID
  private String des3DynId; // 加密的动态ID

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

  public String getResInfoJson() {
    return resInfoJson;
  }

  public void setResInfoJson(String resInfoJson) {
    this.resInfoJson = resInfoJson;
  }

  public Integer getOperatorType() {
    return operatorType;
  }

  public void setOperatorType(Integer operatorType) {
    this.operatorType = operatorType;
  }

  public ShareResShowInfo getShowInfo() {
    return showInfo;
  }

  public void setShowInfo(ShareResShowInfo showInfo) {
    this.showInfo = showInfo;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getCurrentDes3GrpId() {
    return currentDes3GrpId;
  }

  public void setCurrentDes3GrpId(String currentDes3GrpId) {
    this.currentDes3GrpId = currentDes3GrpId;
  }

  public String getDomainScm() {
    return domainScm;
  }

  public void setDomainScm(String domainScm) {
    this.domainScm = domainScm;
  }

  public String getFromPage() {
    return fromPage;
  }

  public void setFromPage(String fromPage) {
    this.fromPage = fromPage;
  }

  public Integer getHideModule() {
    return hideModule;
  }

  public void setHideModule(Integer hideModule) {
    this.hideModule = hideModule;
  }

  public String getWarnCode() {
    return warnCode;
  }

  public void setWarnCode(String warnCode) {
    this.warnCode = warnCode;
  }

  public String getWarnMsg() {
    return warnMsg;
  }

  public void setWarnMsg(String warnMsg) {
    this.warnMsg = warnMsg;
  }

  public boolean getNeedUpdateStatistics() {
    return needUpdateStatistics;
  }

  public void setNeedUpdateStatistics(boolean needUpdateStatistics) {
    this.needUpdateStatistics = needUpdateStatistics;
  }

  public String getGrpResDes3GrpId() {
    return grpResDes3GrpId;
  }

  public void setGrpResDes3GrpId(String grpResDes3GrpId) {
    this.grpResDes3GrpId = grpResDes3GrpId;
  }

  public String getDes3DynId() {
    return des3DynId;
  }

  public void setDes3DynId(String des3DynId) {
    this.des3DynId = des3DynId;
  }



}
