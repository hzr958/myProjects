package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位院系查询表(数据冗余自CONST_INS_UNIT).
 * 
 * @author zym
 * 
 */
@Entity
@Table(name = "CONST_INS_UNIT_SEARCH")
public class AcInsUnit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2346563740843670864L;
  // 主键
  private Long sId;
  // 院(系)名称
  private String collegeName;

  // 系名称
  private String department;

  // 院和系名称（用于检索）
  private String searchName;

  // 单位名称
  private String insName;
  // 排序
  private Long seqNo;

  // 院unit_id,系unit_id
  private String unitIds;

  public AcInsUnit() {}

  @Id
  @Column(name = "SID")
  public Long getsId() {
    return sId;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  @Column(name = "DEPARTMENT")
  public String getDepartment() {
    return department;
  }

  @Column(name = "SEQ_NO")
  public Long getSeqNo() {
    return seqNo;
  }

  @Column(name = "SEARCH_NAME")
  public String getSearchName() {
    return searchName;
  }

  @Column(name = "COLLEGE_NAME")
  public String getCollegeName() {
    return collegeName;
  }

  @Column(name = "UNIT_IDS")
  public String getUnitIds() {
    return unitIds;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setSeqNo(Long seqNo) {
    this.seqNo = seqNo;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public void setSearchName(String searchName) {
    this.searchName = searchName;
  }

  public void setCollegeName(String collegeName) {
    this.collegeName = collegeName;
  }

  public void setsId(Long sId) {
    this.sId = sId;
  }

  public void setUnitIds(String unitIds) {
    this.unitIds = unitIds;
  }

}
