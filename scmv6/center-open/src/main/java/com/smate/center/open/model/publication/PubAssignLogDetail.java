package com.smate.center.open.model.publication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 指派过程匹配内容
 * 
 * @author zzx
 * 
 */
@Entity
@Table(name = "PUB_ASSIGN_LOG_DETAIL")
public class PubAssignLogDetail implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "PUB_MEMBER_NAME")
  private String pubMemberName;// 成果作者
  @Column(name = "PUB_MEMBER_ID")
  private Long pubMemberId;// 成果作者memberId
  @Column(name = "MATCHED_EMAIL")
  private String matchedEmail;
  @Column(name = "MATCHED_NAME")
  private String matchedName;
  @Column(name = "MATCHED_NAME_TYPE")
  private Integer matchedNameType;
  @Column(name = "MATCHED_INSID")
  private Long matchedInsId;

  public PubAssignLogDetail() {
    super();
  }

  public PubAssignLogDetail(Long psnId, Long pubMemberId, String pubMemberName) {
    super();
    this.psnId = psnId;
    this.pubMemberId = pubMemberId;
    this.pubMemberName = pubMemberName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getPubMemberName() {
    return pubMemberName;
  }

  public void setPubMemberName(String pubMemberName) {
    this.pubMemberName = pubMemberName;
  }

  public Long getPubMemberId() {
    return pubMemberId;
  }

  public void setPubMemberId(Long pubMemberId) {
    this.pubMemberId = pubMemberId;
  }

  public String getMatchedEmail() {
    return matchedEmail;
  }

  public void setMatchedEmail(String matchedEmail) {
    this.matchedEmail = matchedEmail;
  }

  public String getMatchedName() {
    return matchedName;
  }

  public void setMatchedName(String matchedName) {
    this.matchedName = matchedName;
  }

  public Integer getMatchedNameType() {
    return matchedNameType;
  }

  public void setMatchedNameType(Integer matchedNameType) {
    this.matchedNameType = matchedNameType;
  }

  public Long getMatchedInsId() {
    return matchedInsId;
  }

  public void setMatchedInsId(Long matchedInsId) {
    this.matchedInsId = matchedInsId;
  }



}
