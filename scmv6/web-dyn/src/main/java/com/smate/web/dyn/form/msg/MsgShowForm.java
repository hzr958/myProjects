package com.smate.web.dyn.form.msg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.model.msg.MsgRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;

/**
 * 消息Form
 * 
 * @author zzx
 *
 */
public class MsgShowForm {
  /**
   * ： 0=系统消息、1=请求添加联系人消息、2=成果认领、3=全文认领、4=成果推荐、5=联系人推荐、 6=基金推荐、7=站内信、8=请求加入群组消息、9=邀请加入群组消息、10=群组动向 ,
   * 11=请求全文消息
   */
  private String msgType;

  private String language; // 中文=zh 英文=en

  private Long psnId;

  private Integer databaseType = 1; // 1 个人库成果 2基准库成果
  /**
   * centerMsg \ chatMsg \ reqFullTextMsg
   */
  private String model = "centerMsg";
  /**
   * 聊天对象psnId
   */
  private Long chatPsnId;
  private String des3ChatPsnId;

  private String des3PsnId;
  /**
   * 发送者ID
   */
  private Long senderId;
  private String des3SenderId;
  /**
   * 接收者ID
   */
  private Long receiverId;
  private String des3ReceiverId;
  /**
   * 接收者集合IDs
   */
  private String receiverIds;
  private String des3ReceiverIds;

  // 群组id
  private Long grpId;
  private String des3GrpId;
  // 成果id
  private Long pubId;
  private String des3PubId;
  private String des3PubIds;
  private List<Long> pubIds;
  // 文件id
  private Long fileId;
  private String des3FileId;
  private String fileIds;
  private String belongPerson;
  private Long resCount;
  private Long newsId; // 新闻id
  private String des3NewsId;
  private String des3SharePsnId;
  /**
   * 消息内容
   */
  private String content;
  /**
   * 消息内容
   */
  private String msg;
  /**
   * 消息内容Id
   */
  private Long contentId;
  /**
   * MsgRelation主键ID
   */
  private Long msgRelationId;
  /**
   * 加密的msgRelationId
   */
  private String des3MsgRelationId;
  private String msgRelationIds;
  /**
   * MsgChatRelationId主键ID
   */
  private Long msgChatRelationId;
  private String des3MsgChatRelationId;

  private String des3PrjId;

  // 消息状态：0=未读、1=已读、2=删除
  private Integer status;
  /**
   * 0=未处理、1=同意、2=拒绝/忽略
   */
  private Integer dealStatus;
  private MsgRelation msgRelation;
  private MsgShowInfo msgShowInfo;
  // 站内信类型 ： text prj pub file
  private String smateInsideLetterType;
  private Date msgCreateDate;
  private List<MsgShowInfo> msgShowInfoList;

  private Page<MsgShowInfo> page = new Page<MsgShowInfo>();
  private String searchKey;// 检索条件
  /**
   * 1=单条消息标记已读，2=与某人的会话消息标记为已读，3=消息中心消息全部标记为已读
   */
  private Integer readMsgType;//

  private Map<String, String> resultMap;

  private String pageSource;
  /**
   * 用于显示消息时间判断，存储上一个消息的时间
   */
  private Date lastDate;

  private List<Long> notPermissionPsnIds;//
  private String chatPsnName;
  private String receiverEmails;

  private Long resId;
  private Map<Long, Long> dupReqPubFulltext = new HashMap<>(); // 全文请求去重map

  private String des3InsId;// 机构ID

  public Map<Long, Long> getDupReqPubFulltext() {
    return dupReqPubFulltext;
  }

  public void setDupReqPubFulltext(Map<Long, Long> dupReqPubFulltext) {
    this.dupReqPubFulltext = dupReqPubFulltext;
  }

  public String getMsgRelationIds() {
    return msgRelationIds;
  }

  public void setMsgRelationIds(String msgRelationIds) {
    this.msgRelationIds = msgRelationIds;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Page<MsgShowInfo> getPage() {
    return page;
  }

  public void setPage(Page<MsgShowInfo> page) {
    this.page = page;
  }

  public MsgShowInfo getMsgShowInfo() {
    return msgShowInfo;
  }

  public void setMsgShowInfo(MsgShowInfo msgShowInfo) {
    this.msgShowInfo = msgShowInfo;
  }

  public List<MsgShowInfo> getMsgShowInfoList() {
    return msgShowInfoList;
  }

  public void setMsgShowInfoList(List<MsgShowInfo> msgShowInfoList) {
    this.msgShowInfoList = msgShowInfoList;
  }

  public MsgRelation getMsgRelation() {
    return msgRelation;
  }

  public void setMsgRelation(MsgRelation msgRelation) {
    this.msgRelation = msgRelation;
  }

  public Long getMsgRelationId() {
    if ((msgRelationId == null || msgRelationId == 0L) && StringUtils.isNotBlank(des3MsgRelationId)) {
      msgRelationId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3MsgRelationId));
    }
    return msgRelationId;
  }

  public void setMsgRelationId(Long msgRelationId) {
    this.msgRelationId = msgRelationId;
  }

  public String getDes3SenderId() {
    return des3SenderId;
  }

  public void setDes3SenderId(String des3SenderId) {
    this.des3SenderId = des3SenderId;
  }

  public String getDes3ReceiverId() {
    return des3ReceiverId;
  }

  public void setDes3ReceiverId(String des3ReceiverId) {
    this.des3ReceiverId = des3ReceiverId;
  }

  public Long getGrpId() {
    if (grpId == null || grpId == 0L) {
      if (StringUtils.isNotBlank(des3GrpId)) {
        grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
      }
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Long getPubId() {
    if (pubId == null || pubId == 0L) {
      if (StringUtils.isNotBlank(des3PubId)) {
        pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PubId));
      }
    }
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Long getFileId() {
    if (fileId == null || fileId == 0L) {
      if (StringUtils.isNotBlank(des3FileId)) {
        fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3FileId));
      }
    }
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getDes3FileId() {
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }

  public String getReceiverIds() {
    return receiverIds;
  }

  public void setReceiverIds(String receiverIds) {
    this.receiverIds = receiverIds;
  }

  public Long getSenderId() {
    if (senderId == null || senderId == 0L) {
      if (StringUtils.isNotBlank(des3SenderId)) {
        senderId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3SenderId));
      }
    }
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public Long getReceiverId() {
    if (receiverId == null && StringUtils.isNotBlank(this.des3ReceiverId)) {
      this.receiverId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3ReceiverId));
    }
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public Long getChatPsnId() {
    if (chatPsnId == null || chatPsnId == 0L) {
      if (StringUtils.isNotBlank(this.getDes3ChatPsnId())) {
        chatPsnId = Long.parseLong(Des3Utils.decodeFromDes3(des3ChatPsnId));
      }
    }
    return chatPsnId;
  }

  public void setChatPsnId(Long chatPsnId) {
    this.chatPsnId = chatPsnId;
  }

  public String getSmateInsideLetterType() {
    return smateInsideLetterType;
  }

  public void setSmateInsideLetterType(String smateInsideLetterType) {
    this.smateInsideLetterType = smateInsideLetterType;
  }

  public String getLanguage() {
    if (StringUtils.isBlank(language)) {
      Locale locale = LocaleContextHolder.getLocale();
      language = locale.getLanguage();
    }
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getDes3ChatPsnId() {
    if (chatPsnId != null && chatPsnId != 0L && StringUtils.isBlank(des3ChatPsnId)) {
      des3ChatPsnId = Des3Utils.encodeToDes3(chatPsnId.toString());
    }
    return des3ChatPsnId;
  }

  public void setDes3ChatPsnId(String des3ChatPsnId) {
    this.des3ChatPsnId = des3ChatPsnId;
  }

  public String getFileIds() {
    return fileIds;
  }

  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setFileIds(String fileIds) {
    this.fileIds = fileIds;
  }

  public String getBelongPerson() {
    return belongPerson;
  }

  public void setBelongPerson(String belongPerson) {
    this.belongPerson = belongPerson;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

  public Date getMsgCreateDate() {
    return msgCreateDate;
  }

  public void setMsgCreateDate(Date msgCreateDate) {
    this.msgCreateDate = msgCreateDate;
  }

  public Long getMsgChatRelationId() {
    if (msgChatRelationId == null || msgChatRelationId == 0L) {
      if (StringUtils.isNotBlank(des3MsgChatRelationId)) {
        msgChatRelationId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3MsgChatRelationId));
      }
    }
    return msgChatRelationId;
  }

  public void setMsgChatRelationId(Long msgChatRelationId) {
    this.msgChatRelationId = msgChatRelationId;
  }

  public String getDes3ReceiverIds() {
    return des3ReceiverIds;
  }

  public void setDes3ReceiverIds(String des3ReceiverIds) {
    this.des3ReceiverIds = des3ReceiverIds;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Long getContentId() {
    return contentId;
  }

  public void setContentId(Long contentId) {
    this.contentId = contentId;
  }

  /**
   * 1=单条消息标记已读，2=与某人的会话消息标记为已读，3=消息中心消息全部标记为已读
   */
  public Integer getReadMsgType() {
    return readMsgType;
  }

  public void setReadMsgType(Integer readMsgType) {
    this.readMsgType = readMsgType;
  }

  public Long getResCount() {
    return resCount;
  }

  public void setResCount(Long resCount) {
    this.resCount = resCount;
  }

  public Integer getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(Integer databaseType) {
    this.databaseType = databaseType;
  }

  public Map<String, String> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, String> resultMap) {
    this.resultMap = resultMap;
  }

  public String getDes3PubIds() {
    return des3PubIds;
  }

  public void setDes3PubIds(String des3PubIds) {
    this.des3PubIds = des3PubIds;
  }

  public List<Long> getPubIds() {
    if (pubIds == null && StringUtils.isNotBlank(des3PubIds)) {
      String[] split = des3PubIds.split(",");
      if (split.length > 0) {
        pubIds = new ArrayList<Long>();
        for (String s : split) {
          pubIds.add(Long.parseLong(Des3Utils.decodeFromDes3(s)));
        }
      }
    }
    return pubIds;
  }

  public void setPubIds(List<Long> pubIds) {
    this.pubIds = pubIds;
  }

  public String getDes3MsgChatRelationId() {
    return des3MsgChatRelationId;
  }

  public void setDes3MsgChatRelationId(String des3MsgChatRelationId) {
    this.des3MsgChatRelationId = des3MsgChatRelationId;
  }

  /**
   * @return des3MsgRelationId
   */
  public String getDes3MsgRelationId() {
    return des3MsgRelationId;
  }

  /**
   * @param des3MsgRelationId 要设置的 des3MsgRelationId
   */
  public void setDes3MsgRelationId(String des3MsgRelationId) {
    this.des3MsgRelationId = des3MsgRelationId;
  }

  public String getPageSource() {
    return pageSource;
  }

  public void setPageSource(String pageSource) {
    this.pageSource = pageSource;
  }

  public Date getLastDate() {
    return lastDate;
  }

  public void setLastDate(Date lastDate) {
    this.lastDate = lastDate;
  }

  public List<Long> getNotPermissionPsnIds() {
    return notPermissionPsnIds;
  }

  public void setNotPermissionPsnIds(List<Long> notPermissionPsnIds) {
    this.notPermissionPsnIds = notPermissionPsnIds;
  }

  public String getChatPsnName() {
    return chatPsnName;
  }

  public void setChatPsnName(String chatPsnName) {
    this.chatPsnName = chatPsnName;
  }

  public String getReceiverEmails() {
    return receiverEmails;
  }

  public void setReceiverEmails(String receiverEmails) {
    this.receiverEmails = receiverEmails;
  }

  public String getDes3PrjId() {
    return des3PrjId;
  }

  public void setDes3PrjId(String des3PrjId) {
    this.des3PrjId = des3PrjId;
  }

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public Long getNewsId() {
    return newsId;
  }

  public void setNewsId(Long newsId) {
    this.newsId = newsId;
  }

  public String getDes3NewsId() {
    return des3NewsId;
  }

  public void setDes3NewsId(String des3NewsId) {
    this.des3NewsId = des3NewsId;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getDes3SharePsnId() {
    return des3SharePsnId;
  }

  public void setDes3SharePsnId(String des3SharePsnId) {
    this.des3SharePsnId = des3SharePsnId;
  }

  public String getDes3InsId() {
    return des3InsId;
  }

  public void setDes3InsId(String des3InsId) {
    this.des3InsId = des3InsId;
  }
}
