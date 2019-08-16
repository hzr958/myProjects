package com.smate.center.task.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果指派记录表
 * 
 * @author zll
 *
 */
@Entity
@Table(name = "PUB_ASSIGN_LOG_DETAIL")
public class PubAssignLogDetail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1932374798518152520L;
  private Long id;
  private Long pdwhPubId; // 基准库成果id
  private Long psnId; // 人员id
  private String pubMemberName;// 成果作者
  private Long pubMemberId;// 成果作者memberId
  private String matchedEmail;// 匹配上的Email
  private String matchedName;// 匹配上的全称
  private Integer matchedNameType;// 1,全称，2，简称
  private Long matchedInsId;// 匹配上的InsId
  private Date updateTime;// 更新时间

  public PubAssignLogDetail() {
    super();
  }

  public PubAssignLogDetail(Long pdwhPubId, Long psnId, String matchedEmail, Long matchedInsId, Date updateTime) {
    super();
    this.pdwhPubId = pdwhPubId;
    this.psnId = psnId;
    this.matchedEmail = matchedEmail;
    this.matchedInsId = matchedInsId;
    this.updateTime = updateTime;
  }

  public PubAssignLogDetail(Long psnId, Long pubMemberId, String pubMemberName) {
    super();
    this.psnId = psnId;
    this.pubMemberId = pubMemberId;
    this.pubMemberName = pubMemberName;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_LOG_DETAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PUB_MEMBER_NAME")
  public String getPubMemberName() {
    return pubMemberName;
  }

  public void setPubMemberName(String pubMemberName) {
    this.pubMemberName = pubMemberName;
  }

  @Column(name = "PUB_MEMBER_ID")
  public Long getPubMemberId() {
    return pubMemberId;
  }

  public void setPubMemberId(Long pubMemberId) {
    this.pubMemberId = pubMemberId;
  }

  @Column(name = "MATCHED_EMAIL")
  public String getMatchedEmail() {
    return matchedEmail;
  }

  public void setMatchedEmail(String matchedEmail) {
    this.matchedEmail = matchedEmail;
  }

  @Column(name = "MATCHED_NAME")
  public String getMatchedName() {
    return matchedName;
  }

  public void setMatchedName(String matchedName) {
    this.matchedName = matchedName;
  }

  @Column(name = "MATCHED_NAME_TYPE")
  public Integer getMatchedNameType() {
    return matchedNameType;
  }

  public void setMatchedNameType(Integer matchedNameType) {
    this.matchedNameType = matchedNameType;
  }

  @Column(name = "MATCHED_INSID")
  public Long getMatchedInsId() {
    return matchedInsId;
  }

  public void setMatchedInsId(Long matchedInsId) {
    this.matchedInsId = matchedInsId;
  }

  @Column(name = "UPDATE_TIME")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
