package com.smate.sie.core.base.utils.model.prj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PRJ_MEMBER")
public class SiePrjMember {
  // 主键唯一标识
  private Long id;
  // 项目ID
  private Long prjId;
  // member name
  private String name;
  // 成员在ScholarMate中的psn_id （该成员通过查找选择本机构人员进行添加）
  private Long psnId;
  // 项目负责人0/1
  private Integer notifyAuthor;
  // 顺序
  private Integer seqNo;
  private Long insId;
  private String insName;
  private Long unitId;
  private String unitName;

  @Id
  @Column(name = "PM_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_MEMBER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  @Column(name = "MEMBER_NAME")
  public String getName() {
    return name;
  }

  @Column(name = "MEMBER_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "NOTIFY_AUTHOR")
  public Integer getNotifyAuthor() {
    return notifyAuthor;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setNotifyAuthor(Integer notifyAuthor) {
    this.notifyAuthor = notifyAuthor;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  @Column(name = "UNIT_NAME")
  public String getUnitName() {
    return unitName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

}
