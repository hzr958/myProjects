package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息model.
 * 
 * @author chenxiangrong
 * 
 */
public class Message implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8340470174510394646L;
  // 收件人以逗号隔开
  private String receivers;
  // 发件人psnId
  private Long sendPsnId;
  private String title;
  private String enTitle;
  private String content;
  // 邀请Id
  private Long inviteId;
  // 加密人员Id
  private String desPsnId;
  private Long psnId;
  // 推荐人以逗号隔开
  private String recommenders;
  private String psnName;
  private String psnFirstName;
  private String psnLastName;
  // 消息类型(inside-站内短消息；invite-站内邀请；req-站内请求；share-站内推荐；msgNoticeStyle-站内通知).
  private String type;
  // 消息状态
  private String statusType;
  // 0 -默认类型 1-请求成果检索 2--自加入群组的请求类型(以前自加入群组类型用这个，现在用inviteType)
  // 3-普通站内短消息_MJG_2013-04-03_SCM-1854.
  private Integer insideType;
  // 回复Id主键
  private String replyId;
  // 收件箱Id
  private String recvId;
  // 发件箱Id
  private String mailId;

  private String action;

  private String actionKey;
  // 0 好友邀请 1 群组邀请 2-自加入群组的请求类型
  private Integer inviteType;
  // 群组节点
  private Integer groupNodeId;
  // 搜索关键字
  private String searchKey;

  private Long groupId;

  private Long gId;

  // 短消息状态0未读、1已读、2全部,默认全部.
  private String status = "2";

  // 请求信息
  private String requestMessage;

  // 携带附件信息
  private String insideFile;

  // 附件Id
  private String des3FileId;
  // 功能推荐URL
  private String recommendUrl;
  // 简历附件url
  private String pdfUrl;
  // 简历附件url
  private String wordUrl;
  // 简历名称
  private String cvName;
  // 组名
  private String groupName;

  /**
   * 系统消息类型，分2中情况：邀请类型和短消息类型;
   * 
   * @邀请类型：0或空-邀请好友成功，1-邀请好友失败，2-加入群成功，3加入群失败； @短消息类型：1-好友评价，2-请求检索成果,3-推荐好友,6-单位中删除人员,11-邀请群组成员共享成果,14-发送消息,50-同意分享成果.
   * 
   */
  private Integer msgType;
  // 操作日期
  private Date optDate;

  // 扩展字段,保存一些其他信息，以json格式保存
  private String ExtOtherInfo;

  private Date from;
  private Date to;

  // groupNodeIds,inviteIds,groupIds对应整型字段的string字段用于批量处理
  private String inviteTypes;
  private String groupNodeIds;
  private String inviteIds;
  private String groupIds;

  // 因需求更改所增加的新增的字段invitePsnId 保存加入群组请求的id
  private Long invitePsnId;

  private String resIds;
  private String jsonParam;

  // 邮件内需要自登录
  private String casUrl;

  /** 评价好友. */
  private FriendFappraisalSend fappraisalSend;

  /** 是否支持邮件. */
  private Integer supportEmail = 0;

  private String resDetailsJson;

  private String shareUrl;

  private String jsonFile;

  private String work;

  /*
   * 用于后台任务，记录操作人的界面语言环境
   */
  private String locale;

  /**
   * @return the receivers
   */
  public String getReceivers() {
    return receivers;
  }

  /**
   * @param receivers the receivers to set
   */
  public void setReceivers(String receivers) {
    this.receivers = receivers;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  /**
   * @return the content
   */
  public String getContent() {
    if (content != null)
      return content.trim();
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the inviteId
   */
  public Long getInviteId() {
    return inviteId;
  }

  /**
   * @param inviteId the inviteId to set
   */
  public void setInviteId(Long inviteId) {
    this.inviteId = inviteId;
  }

  /**
   * @return the recommenders
   */
  public String getRecommenders() {
    return recommenders;
  }

  /**
   * @param recommenders the recommenders to set
   */
  public void setRecommenders(String recommenders) {
    this.recommenders = recommenders;
  }

  /**
   * @return the psnId
   */
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the desPsnId
   */
  public String getDesPsnId() {

    return desPsnId;
  }

  /**
   * @param desPsnId the desPsnId to set
   */
  public void setDesPsnId(String desPsnId) {
    this.desPsnId = desPsnId;
  }

  /**
   * @return the psnName
   */
  public String getPsnName() {
    return psnName;
  }

  /**
   * @return the sendPsnId
   */
  public Long getSendPsnId() {
    return sendPsnId;
  }

  /**
   * @param sendPsnId the sendPsnId to set
   */
  public void setSendPsnId(Long sendPsnId) {
    this.sendPsnId = sendPsnId;
  }

  /**
   * @param psnName the psnName to set
   */
  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the replyId
   */
  public String getReplyId() {
    return replyId;
  }

  /**
   * @return the insideType
   */
  public Integer getInsideType() {
    return insideType;
  }

  /**
   * @param insideType the insideType to set
   */
  public void setInsideType(Integer insideType) {
    this.insideType = insideType;
  }

  /**
   * @param replyId the replyId to set
   */
  public void setReplyId(String replyId) {
    this.replyId = replyId;
  }

  /**
   * @return the recvId
   */
  public String getRecvId() {

    return recvId;
  }

  /**
   * @param recvId the recvId to set
   */
  public void setRecvId(String recvId) {
    this.recvId = recvId;
  }

  /**
   * @return the mailId
   */
  public String getMailId() {
    return mailId;
  }

  /**
   * @param mailId the mailId to set
   */
  public void setMailId(String mailId) {
    this.mailId = mailId;
  }

  /**
   * @return the action
   */
  public String getAction() {
    return action;
  }

  /**
   * @return the groupId
   */
  public Long getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getGId() {
    return gId;
  }

  public void setGId(Long gId) {
    this.gId = gId;
  }

  /**
   * @param action the action to set
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * @return the actionKey
   */
  public String getActionKey() {
    return actionKey;
  }

  /**
   * @return the searchKey
   */
  public String getSearchKey() {
    return searchKey;
  }

  /**
   * @param searchKey the searchKey to set
   */
  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  /**
   * @return the groupNodeId
   */
  public Integer getGroupNodeId() {
    return groupNodeId;
  }

  /**
   * @param groupNodeId the groupNodeId to set
   */
  public void setGroupNodeId(Integer groupNodeId) {
    this.groupNodeId = groupNodeId;
  }

  /**
   * @param actionKey the actionKey to set
   */
  public void setActionKey(String actionKey) {
    this.actionKey = actionKey;
  }

  /**
   * @return the inviteType
   */
  public Integer getInviteType() {
    return inviteType;
  }

  /**
   * @param inviteType the inviteType to set
   */
  public void setInviteType(Integer inviteType) {
    this.inviteType = inviteType;
  }

  public String getRequestMessage() {
    return requestMessage;
  }

  public void setRequestMessage(String requestMessage) {
    this.requestMessage = requestMessage;
  }

  public String getInsideFile() {
    return insideFile;
  }

  public void setInsideFile(String insideFile) {
    this.insideFile = insideFile;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getPsnFirstName() {
    return psnFirstName;
  }

  public void setPsnFirstName(String psnFirstName) {
    this.psnFirstName = psnFirstName;
  }

  public String getPsnLastName() {
    return psnLastName;
  }

  public void setPsnLastName(String psnLastName) {
    this.psnLastName = psnLastName;
  }

  public Integer getMsgType() {
    return msgType;
  }

  public void setMsgType(Integer msgType) {
    this.msgType = msgType;
  }

  public String getDes3FileId() {
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }

  public String getRecommendUrl() {
    return recommendUrl;
  }

  public void setRecommendUrl(String recommendUrl) {
    this.recommendUrl = recommendUrl;
  }

  public String getPdfUrl() {
    return pdfUrl;
  }

  public void setPdfUrl(String pdfUrl) {
    this.pdfUrl = pdfUrl;
  }

  public String getWordUrl() {
    return wordUrl;
  }

  public void setWordUrl(String wordUrl) {
    this.wordUrl = wordUrl;
  }

  public String getCvName() {
    return cvName;
  }

  public void setCvName(String cvName) {
    this.cvName = cvName;
  }

  public Date getOptDate() {
    return optDate;
  }

  public void setOptDate(Date optDate) {
    this.optDate = optDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getExtOtherInfo() {
    return ExtOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    ExtOtherInfo = extOtherInfo;
  }

  /**
   * @return the from
   */
  public Date getFrom() {
    return from;
  }

  /**
   * @param from the from to set
   */
  public void setFrom(Date from) {
    this.from = from;
  }

  /**
   * @return the to
   */
  public Date getTo() {
    return to;
  }

  /**
   * @param to the to to set
   */
  public void setTo(Date to) {
    this.to = to;
  }

  public String getInviteTypes() {
    return inviteTypes;
  }

  public void setInviteTypes(String inviteTypes) {
    this.inviteTypes = inviteTypes;
  }

  public String getGroupNodeIds() {
    return groupNodeIds;
  }

  public void setGroupNodeIds(String groupNodeIds) {
    this.groupNodeIds = groupNodeIds;
  }

  public String getInviteIds() {
    return inviteIds;
  }

  public void setInviteIds(String inviteIds) {
    this.inviteIds = inviteIds;
  }

  public String getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(String groupIds) {
    this.groupIds = groupIds;
  }

  public Long getInvitePsnId() {
    return invitePsnId;
  }

  public void setInvitePsnId(Long invitePsnId) {
    this.invitePsnId = invitePsnId;
  }

  public String getResIds() {
    return resIds;
  }

  public void setResIds(String resIds) {
    this.resIds = resIds;
  }

  public String getStatusType() {
    return statusType;
  }

  public void setStatusType(String statusType) {
    this.statusType = statusType;
  }

  public String getJsonParam() {
    return jsonParam;
  }

  public void setJsonParam(String jsonParam) {
    this.jsonParam = jsonParam;
  }

  public String getCasUrl() {
    return casUrl;
  }

  public void setCasUrl(String casUrl) {
    this.casUrl = casUrl;
  }

  public Integer getSupportEmail() {
    return supportEmail;
  }

  public void setSupportEmail(Integer supportEmail) {
    this.supportEmail = supportEmail;
  }

  public FriendFappraisalSend getFappraisalSend() {
    return fappraisalSend;
  }

  public void setFappraisalSend(FriendFappraisalSend fappraisalSend) {
    this.fappraisalSend = fappraisalSend;
  }

  public String getResDetailsJson() {
    return resDetailsJson;
  }

  public void setResDetailsJson(String resDetailsJson) {
    this.resDetailsJson = resDetailsJson;
  }

  public String getShareUrl() {
    return shareUrl;
  }

  public void setShareUrl(String shareUrl) {
    this.shareUrl = shareUrl;
  }

  public String getJsonFile() {
    return jsonFile;
  }

  public void setJsonFile(String jsonFile) {
    this.jsonFile = jsonFile;
  }

  public String getWork() {
    return work;
  }

  public void setWork(String work) {
    this.work = work;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

}
