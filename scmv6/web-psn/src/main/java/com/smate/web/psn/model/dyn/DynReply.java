package com.smate.web.psn.model.dyn;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author oyh
 * 
 */
@Entity
@Table(name = "DYN_REPLY")
@Deprecated
public class DynReply implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7544888359707513832L;

  private Long replyId;
  private Long replyer;
  private String psnName;
  private String firstName;
  private String lastName;
  private String psnHeadUrl;
  private String replyContent;
  private Long parentId;
  private Date replyDate;
  private String psnUrl;
  private Long keyId;
  // 默认为成果类型
  private Integer refKeyType = 1;

  /**
   * @return the replyId
   */
  @Id
  @Column(name = "REPLY_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_REPLY_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @OrderBy("replyId DESC")
  public Long getReplyId() {
    return replyId;
  }

  /**
   * @param replyId the replyId to set
   */
  public void setReplyId(Long replyId) {
    this.replyId = replyId;
  }

  /**
   * @return the content
   */

  /**
   * @return the replyContent
   */
  @Column(name = "CONTENT")
  public String getReplyContent() {
    return replyContent;
  }

  /**
   * @param replyContent the replyContent to set
   */
  public void setReplyContent(String replyContent) {
    this.replyContent = replyContent;
  }

  /**
   * @return the parentId
   */
  @Column(name = "PARENT_ID")
  public Long getParentId() {
    return parentId;
  }

  /**
   * @param parentId the parentId to set
   */
  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  /**
   * @return the replyDate
   */
  @Column(name = "REPLY_DATE")
  public Date getReplyDate() {
    return replyDate;
  }

  /**
   * @param replyDate the replyDate to set
   */
  public void setReplyDate(Date replyDate) {
    this.replyDate = replyDate;
  }

  /**
   * @return the psnUrl
   */
  @Transient
  public String getPsnUrl() {
    return psnUrl;
  }

  /**
   * @param psnUrl the psnUrl to set
   */
  public void setPsnUrl(String psnUrl) {
    this.psnUrl = psnUrl;
  }

  /**
   * @return the replyer
   */
  @Column(name = "REPLYER")
  public Long getReplyer() {
    return replyer;
  }

  /**
   * @param replyer the replyer to set
   */
  public void setReplyer(Long replyer) {
    this.replyer = replyer;
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
   * @return the psnHeadUrl
   */
  @Column(name = "PSN_HEAD_URL")
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
   * @return the keyId
   */
  @Column(name = "REF_KEY_ID")
  public Long getKeyId() {
    return keyId;
  }

  /**
   * @param keyId the keyId to set
   */
  public void setKeyId(Long keyId) {
    this.keyId = keyId;
  }

  /**
   * @return the refKeyType
   */
  @Column(name = "REF_KEY_TYPE")
  public Integer getRefKeyType() {
    return refKeyType;
  }

  /**
   * @param refKeyType the refKeyType to set
   */
  public void setRefKeyType(Integer refKeyType) {
    this.refKeyType = refKeyType;
  }

}
