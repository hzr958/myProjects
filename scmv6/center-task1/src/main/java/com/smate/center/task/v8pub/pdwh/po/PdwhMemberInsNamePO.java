package com.smate.center.task.v8pub.pdwh.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "V_PUB_PDWH_MEMBER_INSNAME")
public class PdwhMemberInsNamePO implements Serializable {

  private static final long serialVersionUID = -5152634657252514126L;

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_MEMBER_INSNAME_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id; // 主键id

  @Column(name = "MEMBER_ID")
  private Long memberId; // V_PUB_PDWH_MEMBER表的主键id

  @Column(name = "DEPT")
  private String dept; // 部门所在的全地址，对应xml中pub_author节点的dept数据

  @Column(name = "INS_ID")
  private Long insId; // 单位/机构的id

  @Column(name = "INS_NAME")
  private String insName; // 人员所在的单位/机构


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

}
