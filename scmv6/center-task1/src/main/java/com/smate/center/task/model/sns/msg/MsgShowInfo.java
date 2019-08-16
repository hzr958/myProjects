package com.smate.center.task.model.sns.msg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 消息展示类
 * 
 * @author zzx
 *
 */
public class MsgShowInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long psnId;
  /**
   * 发送者ID
   */
  private Long senderId;
  private String receiverIds;
  private String senderAvatars;
  private String senderZhName;
  private String senderEnName;
  private String senderShortUrl;

  /**
   * 接收者ID
   */
  private Long receiverId;
  private String receiverAvatars;
  private String receiverZhName;
  private String receiverEnName;
  private String receiverShortUrl;

  /**
   * 消息内容ID
   */
  private Long contentId;
  /**
   * MsgRelation主键ID
   */
  private Long msgRelationId;
  /**
   * MsgChatRelationId主键ID
   */
  private Long msgChatRelationId;
  // 会话未读消息数量
  private Long msgChatCount;


  // 成果
  private String pubTitleZh;
  private String pubTitleEn;
  private String pubAuthorName;
  private String pubBriefEn;
  private String pubBriefZh;
  private String pubShortUrl;
  private String grpPubShortUrl;
  // 文件id
  private String des3FileId;
  private String fileName;
  private String fileType;
  private String filePath;
  private String archiveFileId;
  private String belongPerson;

  // 全文
  private String hasPubFulltext;// 是否上传全文ture/false
  private Long pubFulltextId;
  private String pubFulltextExt; // 后缀名
  private String pubFulltextImagePath;// 图片则有路径

  // 好友推荐集合,逗号隔离
  private String rcmdFriendIdList;

  // 请求好友Id
  private String requestFriendId;
  // 请求去群组id
  private Long requestGrpId;
  private String grpName;
  private String grpShortUrl;
  // 推荐群组id
  private Long rcmdGrpId;

  // 好友推荐集合,逗号隔离
  private List<String> rcmdFriendZhNameList = new ArrayList<String>();
  private List<String> rcmdFriendEnNameList = new ArrayList<String>();
  private List<String> rcmdFriendShortUrlList = new ArrayList<String>();


  /**
   * 消息内容，存放json、 页面显示需要的字段 （人员信息除外）
   */
  private String content;
  /**
   * 消息类型： 0=系统消息、1=好友请求、2=成果认领、3=全文认领、4=成果推荐、5=好友推荐、 6=基金推荐、7=站内信、8=群组请求、9=好友动向、10=群组动向
   */
  private Integer type;
  /**
   * 创建时间
   */
  private Date createDate;
  /**
   * 消息状态：0=未读、1=已读、2=删除
   */
  private Integer status;
  /**
   * 消息处理状态：0=未处理、1=同意、2=拒绝/忽略
   */
  private Integer dealStatus;
  /**
   * 消息处理时间
   */
  private Date dealDate;
  /**
   * 站内信聊天状态 ：0=正常 、1=删除（删除状态再次发送消息时会重新设置为0）
   */
  private Integer chatStatus;

  // 站内信类型 ： text pub file
  private String smateInsideLetterType;
  private Long fileId;
  private Long pubId;
  private Integer IAmSender = 0; // 1=是，0=否

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getSmateInsideLetterType() {
    return smateInsideLetterType;
  }

  public void setSmateInsideLetterType(String smateInsideLetterType) {
    this.smateInsideLetterType = smateInsideLetterType;
  }

  public Long getMsgRelationId() {
    return msgRelationId;
  }

  public void setMsgRelationId(Long msgRelationId) {
    this.msgRelationId = msgRelationId;
  }


  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public Long getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  public Long getContentId() {
    return contentId;
  }

  public void setContentId(Long contentId) {
    this.contentId = contentId;
  }

  public String getContent() {
    return content;
  }

  public String getSenderAvatars() {
    return senderAvatars;
  }

  public void setSenderAvatars(String senderAvatars) {
    this.senderAvatars = senderAvatars;
  }

  public String getSenderZhName() {
    return senderZhName;
  }

  public void setSenderZhName(String senderZhName) {
    this.senderZhName = senderZhName;
  }

  public String getSenderEnName() {
    return senderEnName;
  }

  public void setSenderEnName(String senderEnName) {
    this.senderEnName = senderEnName;
  }

  public String getSenderShortUrl() {
    return senderShortUrl;
  }

  public void setSenderShortUrl(String senderShortUrl) {
    this.senderShortUrl = senderShortUrl;
  }

  public String getReceiverAvatars() {
    return receiverAvatars;
  }

  public void setReceiverAvatars(String receiverAvatars) {
    this.receiverAvatars = receiverAvatars;
  }

  public String getReceiverZhName() {
    return receiverZhName;
  }

  public void setReceiverZhName(String receiverZhName) {
    this.receiverZhName = receiverZhName;
  }

  public String getReceiverEnName() {
    return receiverEnName;
  }

  public void setReceiverEnName(String receiverEnName) {
    this.receiverEnName = receiverEnName;
  }

  public String getReceiverShortUrl() {
    return receiverShortUrl;
  }

  public void setReceiverShortUrl(String receiverShortUrl) {
    this.receiverShortUrl = receiverShortUrl;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

  public Date getDealDate() {
    return dealDate;
  }

  public void setDealDate(Date dealDate) {
    this.dealDate = dealDate;
  }

  public Integer getChatStatus() {
    return chatStatus;
  }

  public void setChatStatus(Integer chatStatus) {
    this.chatStatus = chatStatus;
  }

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = SecurityUtils.getCurrentUrlId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getIAmSender() {
    return IAmSender;
  }

  public void setIAmSender(Integer iAmSender) {
    IAmSender = iAmSender;
  }

  public String getPubTitleZh() {
    if (StringUtils.isBlank(pubTitleZh) && StringUtils.isNotBlank(pubTitleEn)) {
      pubTitleZh = pubTitleEn;
    }
    return pubTitleZh;
  }

  public void setPubTitleZh(String pubTitleZh) {
    this.pubTitleZh = pubTitleZh;
  }

  public String getPubTitleEn() {
    if (StringUtils.isBlank(pubTitleEn) && StringUtils.isNotBlank(pubTitleZh)) {
      pubTitleEn = pubTitleZh;
    }
    return pubTitleEn;
  }

  public void setPubTitleEn(String pubTitleEn) {
    this.pubTitleEn = pubTitleEn;
  }

  public String getPubAuthorName() {
    return pubAuthorName;
  }

  public void setPubAuthorName(String pubAuthorName) {
    this.pubAuthorName = pubAuthorName;
  }

  public String getPubBriefEn() {
    return pubBriefEn;
  }

  public void setPubBriefEn(String pubBriefEn) {
    this.pubBriefEn = pubBriefEn;
  }

  public String getPubBriefZh() {
    return pubBriefZh;
  }

  public void setPubBriefZh(String pubBriefZh) {
    this.pubBriefZh = pubBriefZh;
  }

  public String getDes3FileId() {
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getArchiveFileId() {
    return archiveFileId;
  }

  public void setArchiveFileId(String archiveFileId) {
    this.archiveFileId = archiveFileId;
  }

  public String getHasPubFulltext() {
    return hasPubFulltext;
  }

  public void setHasPubFulltext(String hasPubFulltext) {
    this.hasPubFulltext = hasPubFulltext;
  }

  public Long getPubFulltextId() {
    return pubFulltextId;
  }

  public void setPubFulltextId(Long pubFulltextId) {
    this.pubFulltextId = pubFulltextId;
  }

  public String getPubFulltextExt() {
    return pubFulltextExt;
  }

  public void setPubFulltextExt(String pubFulltextExt) {
    this.pubFulltextExt = pubFulltextExt;
  }

  public String getPubFulltextImagePath() {
    return pubFulltextImagePath;
  }

  public void setPubFulltextImagePath(String pubFulltextImagePath) {
    this.pubFulltextImagePath = pubFulltextImagePath;
  }

  public String getReceiverIds() {
    return receiverIds;
  }

  public void setReceiverIds(String receiverIds) {
    this.receiverIds = receiverIds;
  }

  public String getPubShortUrl() {
    return pubShortUrl;
  }

  public void setPubShortUrl(String pubShortUrl) {
    this.pubShortUrl = pubShortUrl;
  }

  public String getRcmdFriendIdList() {
    return rcmdFriendIdList;
  }

  public void setRcmdFriendIdList(String rcmdFriendIdList) {
    this.rcmdFriendIdList = rcmdFriendIdList;
  }


  public String getRequestFriendId() {
    return requestFriendId;
  }

  public void setRequestFriendId(String requestFriendId) {
    this.requestFriendId = requestFriendId;
  }



  public List<String> getRcmdFriendZhNameList() {
    return rcmdFriendZhNameList;
  }

  public void setRcmdFriendZhNameList(List<String> rcmdFriendZhNameList) {
    this.rcmdFriendZhNameList = rcmdFriendZhNameList;
  }

  public List<String> getRcmdFriendEnNameList() {
    return rcmdFriendEnNameList;
  }

  public void setRcmdFriendEnNameList(List<String> rcmdFriendEnNameList) {
    this.rcmdFriendEnNameList = rcmdFriendEnNameList;
  }

  public List<String> getRcmdFriendShortUrlList() {
    return rcmdFriendShortUrlList;
  }

  public void setRcmdFriendShortUrlList(List<String> rcmdFriendShortUrlList) {
    this.rcmdFriendShortUrlList = rcmdFriendShortUrlList;
  }

  public String getBelongPerson() {
    return belongPerson;
  }

  public void setBelongPerson(String belongPerson) {
    this.belongPerson = belongPerson;
  }

  public String getGrpPubShortUrl() {
    return grpPubShortUrl;
  }

  public void setGrpPubShortUrl(String grpPubShortUrl) {
    this.grpPubShortUrl = grpPubShortUrl;
  }

  public Long getMsgChatRelationId() {
    return msgChatRelationId;
  }

  public void setMsgChatRelationId(Long msgChatRelationId) {
    this.msgChatRelationId = msgChatRelationId;
  }


  public String getGrpName() {
    return grpName;
  }

  public void setGrpName(String grpName) {
    this.grpName = grpName;
  }

  public String getGrpShortUrl() {
    return grpShortUrl;
  }

  public void setGrpShortUrl(String grpShortUrl) {
    this.grpShortUrl = grpShortUrl;
  }

  public Long getRequestGrpId() {
    return requestGrpId;
  }

  public void setRequestGrpId(Long requestGrpId) {
    this.requestGrpId = requestGrpId;
  }

  public Long getRcmdGrpId() {
    return rcmdGrpId;
  }

  public void setRcmdGrpId(Long rcmdGrpId) {
    this.rcmdGrpId = rcmdGrpId;
  }

  public Long getMsgChatCount() {
    return msgChatCount;
  }

  public void setMsgChatCount(Long msgChatCount) {
    this.msgChatCount = msgChatCount;
  }

}
