package com.smate.sie.center.task.model;

import java.io.Serializable;

/**
 * 成果查重参数.
 * 
 * @author liqinghua
 * 
 */
public class PatDupParam implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7718717272953140847L;
  // 成果对应的外部数据库 refrence to const_ref_db
  private Integer sourceDbId;
  // 专利申请年份
  private Integer pubYear;
  // 中文标题
  private String zhTitle;
  // 英文标题
  private String enTitle;
  // 专利号
  private String patentNo;

  public Integer getSourceDbId() {
    return sourceDbId;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public String getPatentNo() {
    return patentNo;
  }

  public void setPatentNo(String patentNo) {
    this.patentNo = patentNo;
  }

}
