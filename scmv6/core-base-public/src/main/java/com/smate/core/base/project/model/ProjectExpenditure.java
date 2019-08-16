package com.smate.core.base.project.model;

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
 * 项目经费表
 * 
 * @author YJ
 *
 *         2019年8月3日
 */

@Entity
@Table(name = "V_PRJ_EXPENDITURE")
public class ProjectExpenditure implements Serializable {

  private static final long serialVersionUID = 1119162937701800207L;

  @Id
  @SequenceGenerator(sequenceName = "SEQ_V_PRJ_EXPENDITURE", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Long id; // 逻辑主键id]

  @Column(name = "PRJ_ID")
  private Long prjId; // 项目id，project表主键

  @Column(name = "EXPEN_ITEM")
  private String expenItem; // 经费科目名称

  @Column(name = "SEQ_NO")
  private String seqNo;// 序号，决定显示的顺序

  @Column(name = "SCHEME_AMOUNT")
  private Float schemeAmount;// 资助金额（A）（万元）

  @Column(name = "ALLOCATED_AMOUNT")
  private Float allocatedAmount;// 已拨金额（B）（万元）

  @Column(name = "USED_AMOUNT")
  private Float usedAmount;// 已用金额（C）（万元）

  @Column(name = "ADVANCE_AMOUNT")
  private Float advanceAmount;// 预支金额（D）（万元）

  @Column(name = "SUPPORT_AMOUNT")
  private Float supportAmount;// 配置金额（万元）

  @Column(name = "SELF_AMOUNT")
  private Float selfAmount;// 自筹金额（万元）

  @Column(name = "STATUS")
  private Integer status; // 科目状态，0为正常，1为删除

  @Column(name = "PARENT_SEQ_NO")
  private String parentSeqNo;// 父科目序号，记录上一级科目，顶层科目默认为空

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 修改时间



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getExpenItem() {
    return expenItem;
  }

  public void setExpenItem(String expenItem) {
    this.expenItem = expenItem;
  }

  public String getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(String seqNo) {
    this.seqNo = seqNo;
  }

  public Float getSchemeAmount() {
    return schemeAmount;
  }

  public void setSchemeAmount(Float schemeAmount) {
    this.schemeAmount = schemeAmount;
  }

  public Float getAllocatedAmount() {
    return allocatedAmount;
  }

  public void setAllocatedAmount(Float allocatedAmount) {
    this.allocatedAmount = allocatedAmount;
  }

  public Float getUsedAmount() {
    return usedAmount;
  }

  public void setUsedAmount(Float usedAmount) {
    this.usedAmount = usedAmount;
  }

  public Float getAdvanceAmount() {
    return advanceAmount;
  }

  public void setAdvanceAmount(Float advanceAmount) {
    this.advanceAmount = advanceAmount;
  }

  public Integer getStatus() {
    return status;
  }

  public Float getSupportAmount() {
    return supportAmount;
  }

  public void setSupportAmount(Float supportAmount) {
    this.supportAmount = supportAmount;
  }

  public Float getSelfAmount() {
    return selfAmount;
  }

  public void setSelfAmount(Float selfAmount) {
    this.selfAmount = selfAmount;
  }

  public String getParentSeqNo() {
    return parentSeqNo;
  }

  public void setParentSeqNo(String parentSeqNo) {
    this.parentSeqNo = parentSeqNo;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
