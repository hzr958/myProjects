package com.smate.web.prj.dto;

import java.io.Serializable;

public class PrjExpenditureDTO implements Serializable {

  private static final long serialVersionUID = 6912211369656904280L;

  private Long id; // 逻辑主键id

  private Long prjId; // 项目id，project表主键

  private String expenItem; // 经费科目名称

  private String seqNo;// 序号，决定显示的顺序

  private String schemeAmount;// 资助金额（A）

  private String allocatedAmount;// 已拨金额（B）

  private String usedAmount;// 已用金额（C）

  private String advanceAmount;// 预支金额（D）

  private String availableAmount; // 可用金额（E）：E=A-C-D

  private Float supportAmount;// 配置金额（万元）

  private Float selfAmount;// 自筹金额（万元）

  private String parentSeqNo;// 父科目序号，记录上一级科目，顶层科目默认为空


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

  public String getSchemeAmount() {
    return schemeAmount;
  }

  public void setSchemeAmount(String schemeAmount) {
    this.schemeAmount = schemeAmount;
  }

  public String getAllocatedAmount() {
    return allocatedAmount;
  }

  public void setAllocatedAmount(String allocatedAmount) {
    this.allocatedAmount = allocatedAmount;
  }

  public String getUsedAmount() {
    return usedAmount;
  }

  public void setUsedAmount(String usedAmount) {
    this.usedAmount = usedAmount;
  }

  public String getAdvanceAmount() {
    return advanceAmount;
  }

  public void setAdvanceAmount(String advanceAmount) {
    this.advanceAmount = advanceAmount;
  }

  public String getAvailableAmount() {
    return availableAmount;
  }

  public void setAvailableAmount(String availableAmount) {
    this.availableAmount = availableAmount;
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}

