package com.smate.web.prj.dto;

import java.io.Serializable;
import java.util.Date;

public class ProjectExpenRecordDTO implements Serializable {

  private static final long serialVersionUID = -4177070631277617544L;

  private Long id; // 记录id，逻辑主键

  private Long expenId; // 经费表中主键，记录经费项目id

  private Float expenAmount; // 支出经费

  private String formatAmount; // 格式化的支出经费

  private String remark; // 备注信息

  private Date gmtExpen;// 科目支出日期

  private String formatDate; // 界面显示格式化的支出日期

  private Date gmtModified;// 增加支出记录的操作时间，gmtModified >= gmtExpend 此笔支出为已用，反之则为预支

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getExpenId() {
    return expenId;
  }

  public void setExpenId(Long expenId) {
    this.expenId = expenId;
  }

  public Float getExpenAmount() {
    return expenAmount;
  }

  public void setExpenAmount(Float expenAmount) {
    this.expenAmount = expenAmount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getGmtExpen() {
    return gmtExpen;
  }

  public void setGmtExpen(Date gmtExpen) {
    this.gmtExpen = gmtExpen;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public String getFormatDate() {
    return formatDate;
  }

  public void setFormatDate(String formatDate) {
    this.formatDate = formatDate;
  }

  public String getFormatAmount() {
    return formatAmount;
  }

  public void setFormatAmount(String formatAmount) {
    this.formatAmount = formatAmount;
  }
}
