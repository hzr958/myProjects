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
 * 成果院系表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "KPI_PUB_UNIT")
public class KpiPubUnit implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -6864781320830791057L;
  // 主键
  private Long id;
  // 部门ID
  private Long unitId;
  // 成果ID
  private Long pubId;
  // 机构ID
  private Long insId;
  // 市直辖区
  private Long disId;
  // 地区ID
  private Long cyId;
  // 省份ID
  private Long prvId;
  // 发表年月
  private Integer publishYear;
  private Integer publishMonth;
  // 收录情况
  private Integer listEi = 0;
  private Integer listSci = 0;
  private Integer listIstp = 0;
  private Integer listSsci = 0;
  // 成果类别
  private Integer pubType;
  // 所占人员百分比
  private Double percent = 0D;
  // 是否是合作成果
  private Integer copub = 0;
  // 是否是已确认成果
  private Integer isConfirm = 0;
  // 确认成果所占百分比
  private Double cfmPercent;

  public KpiPubUnit() {
    super();
  }

  public KpiPubUnit(Long unitId, Long pubId, Long insId, Long disId, Long cyId, Long prvId) {
    super();
    this.unitId = unitId;
    this.pubId = pubId;
    this.insId = insId;
    this.disId = disId;
    this.cyId = cyId;
    this.prvId = prvId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KPI_PUB_UNIT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PUBLISH_YEAR")
  public Integer getPublishYear() {
    return publishYear;
  }

  @Column(name = "PUBLISH_MONTH")
  public Integer getPublishMonth() {
    return publishMonth;
  }

  @Column(name = "LIST_EI")
  public Integer getListEi() {
    return listEi;
  }

  @Column(name = "LIST_SCI")
  public Integer getListSci() {
    return listSci;
  }

  @Column(name = "LIST_ISTP")
  public Integer getListIstp() {
    return listIstp;
  }

  @Column(name = "LIST_SSCI")
  public Integer getListSsci() {
    return listSsci;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  @Column(name = "PERCENT")
  public Double getPercent() {
    return percent;
  }

  @Column(name = "COPUB")
  public Integer getCopub() {
    return copub;
  }

  @Column(name = "IS_CONFIRM")
  public Integer getIsConfirm() {
    return isConfirm;
  }

  @Column(name = "CFM_PERCENT")
  public Double getCfmPercent() {
    return cfmPercent;
  }

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  @Column(name = "CY_ID")
  public Long getCyId() {
    return cyId;
  }

  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  public void setListEi(Integer listEi) {
    this.listEi = listEi;
  }

  public void setListSci(Integer listSci) {
    this.listSci = listSci;
  }

  public void setListIstp(Integer listIstp) {
    this.listIstp = listIstp;
  }

  public void setListSsci(Integer listSsci) {
    this.listSsci = listSsci;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setPercent(Double percent) {
    this.percent = percent;
  }

  public void setCopub(Integer copub) {
    this.copub = copub;
  }

  public void setIsConfirm(Integer isConfirm) {
    this.isConfirm = isConfirm;
  }

  public void setCfmPercent(Double cfmPercent) {
    this.cfmPercent = cfmPercent;
  }

}
