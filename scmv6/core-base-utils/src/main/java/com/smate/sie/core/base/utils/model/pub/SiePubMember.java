package com.smate.sie.core.base.utils.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 成果成员表
 * 
 * @author jszhou
 */
@Entity
@Table(name = "PUB_MEMBER")
public class SiePubMember implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3641289056563262852L;

  private Long pubId;
  private Long seqNo;
  private String memberName;
  private Long memberPsnid;
  private Integer authorPos;
  private Long pmId;
  private Long insId;
  private String insName;
  private Long unitId;
  private String unitName;
  private String email;
  private Long pmInsId;
  private Integer firstAuthor;

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "SEQ_NO")
  public Long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Long seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "MEMBER_NAME")
  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  @Column(name = "MEMBER_ID")
  public Long getMemberPsnid() {
    return memberPsnid;
  }

  public void setMemberPsnid(Long memberPsnid) {
    this.memberPsnid = memberPsnid;
  }

  @Column(name = "AUTHOR_POS")
  public Integer getAuthorPos() {
    return authorPos;
  }

  public void setAuthorPos(Integer authorPos) {
    this.authorPos = authorPos;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_MEMBER", allocationSize = 1)
  @Column(name = "PM_ID")
  public Long getPmId() {
    return pmId;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  @Column(name = "UNIT_NAME")
  public String getUnitName() {
    return unitName;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Transient
  public Long getPmInsId() {
    return pmInsId;
  }

  public void setPmInsId(Long pmInsId) {
    this.pmInsId = pmInsId;
  }

  @Column(name = "FIRST_AUTHOR")
  public Integer getFirstAuthor() {
    return firstAuthor;
  }

  public void setFirstAuthor(Integer firstAuthor) {
    this.firstAuthor = firstAuthor;
  }



}
