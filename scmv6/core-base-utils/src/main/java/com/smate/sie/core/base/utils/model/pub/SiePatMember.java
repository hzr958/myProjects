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
 * 专利发明人表
 * 
 * @author jszhou
 *
 */
@Entity
@Table(name = "PAT_MEMBER")
public class SiePatMember implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8281620911396834577L;

  private Long patId;
  private Long seqNo;
  private String memberName;
  private Long memberPsnid;
  private Long authorPos;
  private Long pmId;
  private Long insId;
  private Long pmInsId; // 冗余
  private Long unitId;
  private String unitName;
  private String insName;

  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
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
  public Long getAuthorPos() {
    return authorPos;
  }

  public void setAuthorPos(Long authorPos) {
    this.authorPos = authorPos;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PAT_MEMBER", allocationSize = 1)
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

  @Transient
  public Long getPmInsId() {
    return pmInsId;
  }

  public void setPmInsId(Long pmInsId) {
    this.pmInsId = pmInsId;
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  @Column(name = "UNIT_NAME")
  public String getUnitName() {
    return unitName;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

}
