package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "INS_ALIAS_NAME")
public class InsAliasName implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5299670396665000150L;
  //
  private Long id;
  // 单位/部门id，参考institution表
  private Long insId;
  // 中文名称
  private String enName;
  //
  private String zhName;
  //
  private String beginYear;
  //
  private String beginMonth;
  //
  private String endYear;
  //
  private String endMonth;
  // 该别名的状态
  private Integer status;
  // 其它补充信息
  private String memo;
  // 单位/部门标识
  private Integer type;

  @Id
  @Column(name = "IAN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_ALIAS_NAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "BEGIN_YEAR")
  public String getBeginYear() {
    return beginYear;
  }

  @Column(name = "BEGIN_MONTH")
  public String getBeginMonth() {
    return beginMonth;
  }

  @Column(name = "END_YEAR")
  public String getEndYear() {
    return endYear;
  }

  @Column(name = "END_MONTH")
  public String getEndMonth() {
    return endMonth;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "MEMO")
  public String getMemo() {
    return memo;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setBeginYear(String beginYear) {
    this.beginYear = beginYear;
  }

  public void setBeginMonth(String beginMonth) {
    this.beginMonth = beginMonth;
  }

  public void setEndYear(String endYear) {
    this.endYear = endYear;
  }

  public void setEndMonth(String endMonth) {
    this.endMonth = endMonth;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
