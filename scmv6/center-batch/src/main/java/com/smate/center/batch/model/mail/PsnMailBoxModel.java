/**
 * 
 */
package com.smate.center.batch.model.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 发件箱
 * 
 * @author oyh
 * 
 */

public class PsnMailBoxModel implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1616282528711107875L;
  private Long mailId;
  private Long senderId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  private String title;
  private String content;
  private Date optDate;
  private String strOptDate;
  private Integer status;
  private Map<Integer, String> syncNodePsn;
  private List<InsideInbox> inboxs = new ArrayList<InsideInbox>();

  private String forwardCtn;

  private String strContent;
  // 请求信息
  private String requestMessage;

  // 附件名称
  private String insideFileName;

  // 附件加密id
  private String insideDes3FileId;
  // 附件id
  private String insideFileId;

  // 节点
  private Integer nodeId;

  // 扩展字段，以json格式存储
  private String extOtherInfo;

  // 消息类型：1 好友评价
  private Integer msgType;

  // 关系
  private String psnRelation;
  // 工作经历
  private String psnWork;
  // 评价内容
  private String evaContent;

  public Long getMailId() {
    return mailId;
  }

  /**
   * @param mailId the mailId to set
   */
  public void setMailId(Long mailId) {
    this.mailId = mailId;
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

  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the optDate
   */
  public Date getOptDate() {
    return optDate;
  }

  /**
   * @param optDate the optDate to set
   */
  public void setOptDate(Date optDate) {
    this.optDate = optDate;
  }

  /**
   * @return the status
   */
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the syncNodePsn
   */
  @Transient
  public Map<Integer, String> getSyncNodePsn() {
    return syncNodePsn;
  }

  /**
   * @param syncNodePsn the syncNodePsn to set
   */
  public void setSyncNodePsn(Map<Integer, String> syncNodePsn) {
    this.syncNodePsn = syncNodePsn;
  }

  /**
   * @return the forwardCtn
   */
  public String getForwardCtn() {
    return forwardCtn;
  }

  /**
   * @param forwardCtn the forwardCtn to set
   */
  public void setForwardCtn(String forwardCtn) {
    this.forwardCtn = forwardCtn;
  }

  /**
   * @return the senderId
   */
  public Long getSenderId() {
    return senderId;
  }

  /**
   * @param senderId the senderId to set
   */
  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  /**
   * @param inboxs the inboxs to set
   */
  public void setInboxs(List<InsideInbox> inboxs) {
    this.inboxs = inboxs;
  }

  /**
   * @return the psnName
   */
  public String getPsnName() {
    return psnName;
  }

  /**
   * @param psnName the psnName to set
   */
  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the psnHeadUrl
   */
  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  /**
   * @param psnHeadUrl the psnHeadUrl to set
   */
  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

  public String getStrContent() {
    return strContent;
  }

  public void setStrContent(String strContent) {
    this.strContent = strContent;
  }

  public String getRequestMessage() {
    return requestMessage;
  }

  public void setRequestMessage(String requestMessage) {
    this.requestMessage = requestMessage;
  }

  public String getInsideFileName() {
    return insideFileName;
  }

  public void setInsideFileName(String insideFileName) {
    this.insideFileName = insideFileName;
  }

  public String getInsideDes3FileId() {
    return insideDes3FileId;
  }

  public void setInsideDes3FileId(String insideDes3FileId) {
    this.insideDes3FileId = insideDes3FileId;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public String getInsideFileId() {
    return insideFileId;
  }

  public void setInsideFileId(String insideFileId) {
    this.insideFileId = insideFileId;
  }

  public String getStrOptDate() {
    if (optDate != null)
      return DateFormatUtils.format(optDate, "yyyy-MM-dd HH:mm:ss");
    return strOptDate;
  }

  public void setStrOptDate(String strOptDate) {
    this.strOptDate = strOptDate;
  }

  public String getExtOtherInfo() {
    return extOtherInfo;
  }

  public void setExtOtherInfo(String extOtherInfo) {
    this.extOtherInfo = extOtherInfo;
  }

  public Integer getMsgType() {
    return msgType;
  }

  public void setMsgType(Integer msgType) {
    this.msgType = msgType;
  }

  public String getPsnRelation() {
    return psnRelation;
  }

  public void setPsnRelation(String psnRelation) {
    this.psnRelation = psnRelation;
  }

  public String getPsnWork() {
    return psnWork;
  }

  public void setPsnWork(String psnWork) {
    this.psnWork = psnWork;
  }

  public String getEvaContent() {
    return evaContent;
  }

  public void setEvaContent(String evaContent) {
    this.evaContent = evaContent;
  }

}
