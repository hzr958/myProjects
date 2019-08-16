package com.smate.web.dyn.model.msg;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 消息展示类
 * 
 * @author zzx
 *
 */
public class MsgShowInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long psnId;
  private Long grpId;
  private Integer dbid;
  /**
   * 发送者ID
   */
  private Long senderId;
  private String des3senderId;
  private String receiverIds;
  private String senderAvatars;
  private String senderZhName;
  private String senderEnName;
  private String senderShortUrl;
  private Integer isFriend;

  /**
   * 接收者ID
   */
  private Long receiverId;
  private String des3receiverId;
  private String receiverAvatars;
  private String receiverZhName;
  private String receiverEnName;
  private String receiverShortUrl;
  private Long resCount;

  /**
   * 消息内容ID
   */
  private Long contentId;
  /**
   * MsgRelation主键ID
   */
  private Long msgRelationId;
  private String des3MsgRelationId;
  /**
   * MsgChatRelation主键ID
   */
  private Long msgChatRelationId;
  // 会话未读消息数量
  private Long msgChatCount;
  // 会话已读消息数量
  private Long msgChatReadCount;
  private String downloadUrl = "";

  // 成果
  private String pubTitleZh;
  private String pubTitleEn;
  private String pubAuthorName;
  private String pubBriefEn;
  private String pubBriefZh;
  private String pubShortUrl;
  private String grpPubShortUrl;
  private Long grpPubId;
  private Long rolpubId;
  private String des3PubId;// 加密的成果id
  private String des3PubOwnerPsnId;// 成果拥有者的psnId
  private Integer dbId;

  // 文件id
  private String des3FileId;
  private String fileName;
  private String fileType;
  private String filePath;
  private Long archiveFileId;
  private String belongPerson;
  private Long grpFileId;
  private String fileIconPath;// pdf,docx,zip,xls等文件默认图标
  private String fileDownloadUrl = "";// 文件下载路径
  private String fileShortDownloadUrl = "";// 文件下载短地址
  private String imgThumbUrl = ""; // 图片类型文件缩略图url

  // 全文
  private String hasPubFulltext;// 是否上传全文ture/false
  private Long pubFulltextId;
  private String pubFulltextExt; // 后缀名
  private String pubFulltextImagePath;// 图片则有路径
  private String pubFulltextPermit;// 全文权限

  // 基金
  private String fundZhTitle; // 中文标题
  private String fundEnTitle; // 英文标题
  private String showFundTitle; // 显示的标题
  private Long fundId; // 基金ID
  private String fundAgencyName; // 资助机构
  private String fundScienceArea; // 科技领域
  private String fundLogoUrl; // logo
  private String fundApplyTime; // 起止时间
  private String showDesc; // 描述
  private String zhFundDesc; // 中文描述
  private String enFundDesc; // 英文描述
  private String zhFundDescBr; // 中文描述
  private String enFundDescBr; // 英文描述
  private String encryptedFundId; // 加密基金id
  private Long searchCount;
  // 项目
  private String des3PrjId;
  private String prjTitleZh;
  private String prjTitleEn;
  private String prjAuthorNameZh;
  private String prjAuthorNameEn;
  private String prjBriefZh;
  private String prjBriefEn;
  private String prjUrl;
  private String prjImg;

  private String title;
  private String authorName;
  private String brief;

  // 新闻
  private String des3NewsId;
  private String newsUrl;
  private String newsTitle;
  private String newsImg;
  private String newsBrief;
  // 资助机构
  private String agencyZhTitle; // 中文标题
  private String agencyEnTitle; // 英文标题
  private String showAgencyTitle; // 显示的标题
  private Long agencyId; // 资助机构ID
  private String agencyLogoUrl; // logo
  private String zhAgencyDescBr; // 中文描述
  private String enAgencyDescBr; // 英文描述
  private String des3AgencyId; // 加密资助机构id
  private String zhAgencyDesc; // 中文描述
  private String enAgencyDesc; // 英文描述
  private Integer sendMsg;// 发送消息的权限;1、联系人，0、任何人
  private Integer resDelete = 1;// 1、资源未删除，0、资源已经删除
  private String noneHtmlLablepubAuthorName;
  // 个人主页
  private String des3PsnId;
  private String psnProfileUrl;
  // 机构
  private String des3InsId;
  private String insHomeUrl;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public String getBrief() {
    return brief;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  public String getDes3PrjId() {
    return des3PrjId;
  }

  public void setDes3PrjId(String des3PrjId) {
    this.des3PrjId = des3PrjId;
  }

  public String getPrjTitleZh() {
    return prjTitleZh;
  }

  public void setPrjTitleZh(String prjTitleZh) {
    this.prjTitleZh = prjTitleZh;
  }

  public String getPrjTitleEn() {
    return prjTitleEn;
  }

  public void setPrjTitleEn(String prjTitleEn) {
    this.prjTitleEn = prjTitleEn;
  }

  public String getPrjAuthorNameZh() {
    return prjAuthorNameZh;
  }

  public void setPrjAuthorNameZh(String prjAuthorNameZh) {
    this.prjAuthorNameZh = prjAuthorNameZh;
  }

  public String getPrjAuthorNameEn() {
    return prjAuthorNameEn;
  }

  public void setPrjAuthorNameEn(String prjAuthorNameEn) {
    this.prjAuthorNameEn = prjAuthorNameEn;
  }

  public String getPrjBriefZh() {
    return prjBriefZh;
  }

  public void setPrjBriefZh(String prjBriefZh) {
    this.prjBriefZh = prjBriefZh;
  }

  public String getPrjBriefEn() {
    return prjBriefEn;
  }

  public void setPrjBriefEn(String prjBriefEn) {
    this.prjBriefEn = prjBriefEn;
  }

  public String getPrjUrl() {
    return prjUrl;
  }

  public void setPrjUrl(String prjUrl) {
    this.prjUrl = prjUrl;
  }

  public String getPrjImg() {
    return prjImg;
  }

  public void setPrjImg(String prjImg) {
    this.prjImg = prjImg;
  }

  public String getEncryptedFundId() {
    if (fundId != null && fundId > 0L) {
      encryptedFundId = ServiceUtil.encodeToDes3(fundId.toString());
    }
    return encryptedFundId;
  }

  public void setEncryptedFundId(String encryptedFundId) {
    this.encryptedFundId = encryptedFundId;
  }

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
  private String showDate;
  private String showCreateDate;
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

  // 站内信类型 ： text pub file fulltext
  private String smateInsideLetterType;
  private Long fileId;
  private Long pubId;
  private Integer IAmSender = 0; // 1=是，0=否
  private String contentNewest;
  private boolean ifShowDate = false;

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

  public Long getArchiveFileId() {
    return archiveFileId;
  }

  public void setArchiveFileId(Long archiveFileId) {
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

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getGrpPubId() {
    return grpPubId;
  }

  public void setGrpPubId(Long grpPubId) {
    this.grpPubId = grpPubId;
  }

  public String getContentNewest() {
    return contentNewest;
  }

  public void setContentNewest(String contentNewest) {
    this.contentNewest = contentNewest;
  }

  public String getShowDate() {
    return showDate;
  }

  public void setShowDate(String showDate) {
    this.showDate = showDate;
  }

  public String getPubFulltextPermit() {
    return pubFulltextPermit;
  }

  public void setPubFulltextPermit(String pubFulltextPermit) {
    this.pubFulltextPermit = pubFulltextPermit;
  }

  public Long getRolpubId() {
    return rolpubId;
  }

  public void setRolpubId(Long rolpubId) {
    this.rolpubId = rolpubId;
  }

  public Long getGrpFileId() {
    return grpFileId;
  }

  public void setGrpFileId(Long grpFileId) {
    this.grpFileId = grpFileId;
  }

  public Long getResCount() {
    return resCount;
  }

  public void setResCount(Long resCount) {
    this.resCount = resCount;
  }

  public String getFileDownloadUrl() {
    return fileDownloadUrl;
  }

  public void setFileDownloadUrl(String fileDownloadUrl) {
    this.fileDownloadUrl = fileDownloadUrl;
  }

  public String getDes3PubId() {
    if (pubId != null && pubId > 0L && StringUtils.isBlank(des3PubId)) {
      des3PubId = ServiceUtil.encodeToDes3(pubId.toString());
    }
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public String getFundZhTitle() {
    return fundZhTitle;
  }

  public void setFundZhTitle(String fundZhTitle) {
    this.fundZhTitle = fundZhTitle;
  }

  public String getFundEnTitle() {
    return fundEnTitle;
  }

  public void setFundEnTitle(String fundEnTitle) {
    this.fundEnTitle = fundEnTitle;
  }

  public String getShowFundTitle() {
    return showFundTitle;
  }

  public void setShowFundTitle(String showFundTitle) {
    this.showFundTitle = showFundTitle;
  }

  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  public String getFundAgencyName() {
    return fundAgencyName;
  }

  public void setFundAgencyName(String fundAgencyName) {
    this.fundAgencyName = fundAgencyName;
  }

  public String getFundScienceArea() {
    return fundScienceArea;
  }

  public void setFundScienceArea(String fundScienceArea) {
    this.fundScienceArea = fundScienceArea;
  }

  public String getFundLogoUrl() {
    return fundLogoUrl;
  }

  public void setFundLogoUrl(String fundLogoUrl) {
    this.fundLogoUrl = fundLogoUrl;
  }

  public String getFundApplyTime() {
    return fundApplyTime;
  }

  public void setFundApplyTime(String fundApplyTime) {
    this.fundApplyTime = fundApplyTime;
  }

  public String getShowDesc() {
    return showDesc;
  }

  public void setShowDesc(String showDesc) {
    this.showDesc = showDesc;
  }

  public String getZhFundDesc() {
    return zhFundDesc;
  }

  public void setZhFundDesc(String zhFundDesc) {
    this.zhFundDesc = zhFundDesc;
  }

  public String getEnFundDesc() {
    return enFundDesc;
  }

  public void setEnFundDesc(String enFundDesc) {
    this.enFundDesc = enFundDesc;
  }

  public String getDes3senderId() {
    if (senderId != null && senderId != 0L && StringUtils.isBlank(des3senderId)) {
      des3senderId = ServiceUtil.encodeToDes3(senderId.toString());
    }
    return des3senderId;
  }

  public void setDes3senderId(String des3senderId) {
    this.des3senderId = des3senderId;
  }

  public String getDes3receiverId() {
    if (receiverId != null && receiverId != 0L && StringUtils.isBlank(des3receiverId)) {
      des3senderId = ServiceUtil.encodeToDes3(receiverId.toString());
    }
    return des3receiverId;
  }

  public void setDes3receiverId(String des3receiverId) {
    this.des3receiverId = des3receiverId;
  }

  public String getShowCreateDate() {
    if (createDate != null) {
      showCreateDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(createDate);
    }
    return showCreateDate;
  }

  public void setShowCreateDate(String showCreateDate) {
    this.showCreateDate = showCreateDate;
  }

  public String getZhFundDescBr() {
    return zhFundDescBr;
  }

  public void setZhFundDescBr(String zhFundDescBr) {
    this.zhFundDescBr = zhFundDescBr;
  }

  public String getEnFundDescBr() {
    return enFundDescBr;
  }

  public void setEnFundDescBr(String enFundDescBr) {
    this.enFundDescBr = enFundDescBr;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Integer getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(Integer isFriend) {
    this.isFriend = isFriend;
  }

  public String getDes3MsgRelationId() {
    if (msgRelationId != null && msgRelationId > 0L && StringUtils.isBlank(des3MsgRelationId)) {
      des3MsgRelationId = ServiceUtil.encodeToDes3(msgRelationId.toString());
    }
    return des3MsgRelationId;
  }

  public void setDes3MsgRelationId(String des3MsgRelationId) {
    this.des3MsgRelationId = des3MsgRelationId;
  }

  public Long getMsgChatReadCount() {
    return msgChatReadCount;
  }

  public void setMsgChatReadCount(Long msgChatReadCount) {
    this.msgChatReadCount = msgChatReadCount;
  }

  public boolean isIfShowDate() {
    return ifShowDate;
  }

  public void setIfShowDate(boolean ifShowDate) {
    this.ifShowDate = ifShowDate;
  }

  public Long getSearchCount() {
    return searchCount;
  }

  public void setSearchCount(Long searchCount) {
    this.searchCount = searchCount;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getFileIconPath() {
    return fileIconPath;
  }

  public void setFileIconPath(String fileIconPath) {
    this.fileIconPath = fileIconPath;
  }

  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  /**
   * @return imgThumbUrl
   */
  public String getImgThumbUrl() {
    return imgThumbUrl;
  }

  /**
   * @param imgThumbUrl 要设置的 imgThumbUrl
   */
  public void setImgThumbUrl(String imgThumbUrl) {
    this.imgThumbUrl = imgThumbUrl;
  }

  public String getFileShortDownloadUrl() {
    return fileShortDownloadUrl;
  }

  public void setFileShortDownloadUrl(String fileShortDownloadUrl) {
    this.fileShortDownloadUrl = fileShortDownloadUrl;
  }

  public String getAgencyZhTitle() {
    return agencyZhTitle;
  }

  public void setAgencyZhTitle(String agencyZhTitle) {
    this.agencyZhTitle = agencyZhTitle;
  }

  public String getAgencyEnTitle() {
    return agencyEnTitle;
  }

  public void setAgencyEnTitle(String agencyEnTitle) {
    this.agencyEnTitle = agencyEnTitle;
  }

  public String getShowAgencyTitle() {
    return showAgencyTitle;
  }

  public void setShowAgencyTitle(String showAgencyTitle) {
    this.showAgencyTitle = showAgencyTitle;
  }

  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  public String getAgencyLogoUrl() {
    return agencyLogoUrl;
  }

  public void setAgencyLogoUrl(String agencyLogoUrl) {
    this.agencyLogoUrl = agencyLogoUrl;
  }

  public String getZhAgencyDescBr() {
    return zhAgencyDescBr;
  }

  public void setZhAgencyDescBr(String zhAgencyDescBr) {
    this.zhAgencyDescBr = zhAgencyDescBr;
  }

  public String getEnAgencyDescBr() {
    return enAgencyDescBr;
  }

  public void setEnAgencyDescBr(String enAgencyDescBr) {
    this.enAgencyDescBr = enAgencyDescBr;
  }

  public String getDes3AgencyId() {
    return des3AgencyId;
  }

  public void setDes3AgencyId(String des3AgencyId) {
    this.des3AgencyId = des3AgencyId;
  }

  public String getZhAgencyDesc() {
    return zhAgencyDesc;
  }

  public void setZhAgencyDesc(String zhAgencyDesc) {
    this.zhAgencyDesc = zhAgencyDesc;
  }

  public String getEnAgencyDesc() {
    return enAgencyDesc;
  }

  public void setEnAgencyDesc(String enAgencyDesc) {
    this.enAgencyDesc = enAgencyDesc;
  }

  public String getDes3PubOwnerPsnId() {
    return des3PubOwnerPsnId;
  }

  public void setDes3PubOwnerPsnId(String des3PubOwnerPsnId) {
    this.des3PubOwnerPsnId = des3PubOwnerPsnId;
  }

  public Integer getSendMsg() {
    return sendMsg;
  }

  public void setSendMsg(Integer sendMsg) {
    this.sendMsg = sendMsg;
  }

  public Integer getResDelete() {
    return resDelete;
  }

  public void setResDelete(Integer resDelete) {
    this.resDelete = resDelete;
  }

  public String getNoneHtmlLablepubAuthorName() {
    return noneHtmlLablepubAuthorName;
  }

  public void setNoneHtmlLablepubAuthorName(String noneHtmlLablepubAuthorName) {
    this.noneHtmlLablepubAuthorName = noneHtmlLablepubAuthorName;
  }

  public String getDes3NewsId() {
    return des3NewsId;
  }

  public void setDes3NewsId(String des3NewsId) {
    this.des3NewsId = des3NewsId;
  }

  public String getNewsUrl() {
    return newsUrl;
  }

  public void setNewsUrl(String newsUrl) {
    this.newsUrl = newsUrl;
  }

  public String getNewsTitle() {
    return newsTitle;
  }

  public void setNewsTitle(String newsTitle) {
    this.newsTitle = newsTitle;
  }

  public String getNewsImg() {
    return newsImg;
  }

  public void setNewsImg(String newsImg) {
    this.newsImg = newsImg;
  }

  public String getNewsBrief() {
    return newsBrief;
  }

  public void setNewsBrief(String newsBrief) {
    this.newsBrief = newsBrief;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getPsnProfileUrl() {
    return psnProfileUrl;
  }

  public void setPsnProfileUrl(String psnProfileUrl) {
    this.psnProfileUrl = psnProfileUrl;
  }

  public String getDes3InsId() {
    return des3InsId;
  }

  public void setDes3InsId(String des3InsId) {
    this.des3InsId = des3InsId;
  }

  public String getInsHomeUrl() {
    return insHomeUrl;
  }

  public void setInsHomeUrl(String insHomeUrl) {
    this.insHomeUrl = insHomeUrl;
  }
}
