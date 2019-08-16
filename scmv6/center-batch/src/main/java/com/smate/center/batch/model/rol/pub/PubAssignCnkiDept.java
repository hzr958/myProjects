package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CNKI成果匹配-成果指派拆分部门.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_ASSIGN_CNKIDEPT")
public class PubAssignCnkiDept implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3852724084434977908L;

  private Long id;
  // 部门名称(小写)
  private String deptName;
  // 成果ID
  private Long pubId;
  // 成果所属单位ID
  private Long pubInsId;
  // 用户序号
  private Integer seqNo;
  // 部门所在单位
  private Long insId;

  public PubAssignCnkiDept() {
    super();
  }

  public PubAssignCnkiDept(String deptName, Long pubId, Long pubInsId, Integer seqNo, Long insId) {
    super();
    this.deptName = deptName;
    this.pubId = pubId;
    this.pubInsId = pubInsId;
    this.seqNo = seqNo;
    this.insId = insId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_CNKIDEPT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "DEPT_NAME")
  public String getDeptName() {
    return deptName;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUB_INS_ID")
  public Long getPubInsId() {
    return pubInsId;
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

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubInsId(Long pubInsId) {
    this.pubInsId = pubInsId;
  }
}
