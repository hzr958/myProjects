package com.smate.web.psn.model.dyn;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author oyh
 * 
 */
@Entity
@Table(name = "DYN_MESSAGE")
public class DynRecvMessage implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8125250024758177069L;
  private Long dynId;
  private Long senderId;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  private String content;
  private Long groupId;
  private String dynType;
  private Date dynDate;
  private Integer permission;
  private DynTmpConfig dynTmpConfig;
  private Date upateDate;
  private int syncFlag;
  private List<DynReply> dynReplyList = new ArrayList<DynReply>();
  private String dynContent;
  private Boolean isError = false;
  private String dynMsgType = "normal";
  private Integer replyTotal;
  // 针对扩展动态使用
  private String extJson;
  private long awards = 0;
  // 转发次数
  private long forwardCount = 0;
  // 本人是否已经赞过了
  private boolean hasAward = false;
  // 动态类型
  private Integer refKeyType = 0;

  // 抓取动态消息起始位置
  private Integer begin = 0;
  private Integer fetchSize = 20;
  private Boolean myDyn = false;
  private String awardPsns;

  public DynRecvMessage() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @return the dynId
   */
  @Id
  @Column(name = "DYN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYN_MESSAGE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getDynId() {
    return dynId;
  }

  /**
   * @param dynId the dynId to set
   */
  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  /**
   * @return the content
   */
  @Column(name = "CONTENT")
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
   * @return the dynType
   */
  @Column(name = "TYPE")
  public String getDynType() {
    return dynType;
  }

  /**
   * @param dynType the dynType to set
   */
  public void setDynType(String dynType) {
    this.dynType = dynType;
  }

  /**
   * @return the dynDate
   */
  @Column(name = "DYN_DATE")
  public Date getDynDate() {
    return dynDate;
  }

  /**
   * @param dynDate the dynDate to set
   */
  public void setDynDate(Date dynDate) {
    this.dynDate = dynDate;
  }

  /**
   * @return the permission
   */
  @Column(name = "PERMISSION")
  public Integer getPermission() {
    return permission;
  }

  /**
   * @param permission the permission to set
   */
  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  /**
   * @return the dynTmpConfig
   */
  @OneToOne
  @JoinColumn(name = "TMP_ID")
  public DynTmpConfig getDynTmpConfig() {
    return dynTmpConfig;
  }

  /**
   * @param dynTmpConfig the dynTmpConfig to set
   */
  public void setDynTmpConfig(DynTmpConfig dynTmpConfig) {
    this.dynTmpConfig = dynTmpConfig;
  }

  @Transient
  public List<DynReply> getDynReplyList() {
    return dynReplyList;
  }

  /**
   * @param dynReplyList the dynReplyList to set
   */
  public void setDynReplyList(List<DynReply> dynReplyList) {
    this.dynReplyList = dynReplyList;
  }

  /**
   * @return the dynContent
   */
  @Transient
  public String getDynContent() {
    return dynContent;
  }

  /**
   * @param dynContent the dynContent to set
   */
  public void setDynContent(String dynContent) {
    this.dynContent = dynContent;
  }

  /**
   * @return the begin
   */
  @Transient
  public Integer getBegin() {
    return begin;
  }

  /**
   * @return the senderId
   */
  @Column(name = "SENDER")
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
   * @return the psnHeadUrl
   */
  @Column(name = "DYN_HEAD_LOGO")
  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  /**
   * @param psnHeadUrl the psnHeadUrl to set
   */
  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

  /**
   * @return the psnName
   */
  @Column(name = "PSN_NAME")
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
  @Column(name = "FIRST_NAME")
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
  @Column(name = "LAST_NAME")
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
   * @param begin the begin to set
   */
  public void setBegin(Integer begin) {
    this.begin = begin;
  }

  /**
   * @return the fetchSize
   */
  @Transient
  public Integer getFetchSize() {
    return fetchSize;
  }

  /**
   * @param fetchSize the fetchSize to set
   */
  public void setFetchSize(Integer fetchSize) {
    this.fetchSize = fetchSize;
  }

  /**
   * @return the groupId
   */
  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the upateDate
   */
  @Column(name = "UPDATE_DATE")
  public Date getUpateDate() {
    return upateDate;
  }

  /**
   * @param upateDate the upateDate to set
   */
  public void setUpateDate(Date upateDate) {
    this.upateDate = upateDate;
  }

  @Column(name = "SYNC_FLAG")
  public int getSyncFlag() {
    return syncFlag;
  }

  public void setSyncFlag(int syncFlag) {
    this.syncFlag = syncFlag;
  }

  /**
   * @return the isError
   */
  @Transient
  public Boolean getIsError() {
    return isError;
  }

  /**
   * @param isError the isError to set
   */
  public void setIsError(Boolean isError) {
    this.isError = isError;
  }

  /**
   * @return the dynMsgType
   */
  @Transient
  public String getDynMsgType() {
    return dynMsgType;
  }

  /**
   * @param dynMsgType the dynMsgType to set
   */
  public void setDynMsgType(String dynMsgType) {
    this.dynMsgType = dynMsgType;
  }

  /**
   * @return the replyTotal
   */
  @Transient
  public Integer getReplyTotal() {
    return replyTotal;
  }

  /**
   * @param replyTotal the replyTotal to set
   */
  public void setReplyTotal(Integer replyTotal) {
    this.replyTotal = replyTotal;
  }

  /**
   * @return the extJson
   */
  @Transient
  public String getExtJson() {
    return extJson;
  }

  /**
   * @param extJson the extJson to set
   */
  public void setExtJson(String extJson) {
    this.extJson = extJson;
  }

  /**
   * @return the awards
   */
  @Transient
  public long getAwards() {
    return awards;
  }

  /**
   * @param awards the awards to set
   */
  public void setAwards(long awards) {
    this.awards = awards;
  }

  /**
   * @return the hasAward
   */
  @Transient
  public boolean isHasAward() {
    return hasAward;
  }

  /**
   * @param hasAward the hasAward to set
   */
  public void setHasAward(boolean hasAward) {
    this.hasAward = hasAward;
  }

  /**
   * @return the refKeyType
   */
  @Transient
  public Integer getRefKeyType() {
    return refKeyType;
  }

  /**
   * @param refKeyType the refKeyType to set
   */
  public void setRefKeyType(Integer refKeyType) {
    this.refKeyType = refKeyType;
  }

  /**
   * @return the forwardCount
   */
  @Transient
  public long getForwardCount() {
    return forwardCount;
  }

  /**
   * @param forwardCount the forwardCount to set
   */
  public void setForwardCount(long forwardCount) {
    this.forwardCount = forwardCount;
  }

  /**
   * @return the myDyn
   */
  @Transient
  public Boolean getMyDyn() {
    return myDyn;
  }

  /**
   * @param myDyn the myDyn to set
   */
  public void setMyDyn(Boolean myDyn) {
    this.myDyn = myDyn;
  }

  @Transient
  public String getAwardPsns() {
    return awardPsns;
  }

  public void setAwardPsns(String awardPsns) {
    this.awardPsns = awardPsns;
  }

}
