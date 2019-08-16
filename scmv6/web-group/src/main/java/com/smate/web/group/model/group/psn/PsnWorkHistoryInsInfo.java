package com.smate.web.group.model.group.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员工作经历单位相关信息
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "PSN_WORKHISTORY_INS_INFO")
public class PsnWorkHistoryInsInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 818581698511692147L;
  private Long id;
  private Long psnId;
  private Long insId;
  private String insNameZh;
  private String insNameEn;
  private String departmentZh;
  private String departmentEn;
  private String positionZh;
  private String positionEn;

  public PsnWorkHistoryInsInfo() {}

  public PsnWorkHistoryInsInfo(Long psnId, Long insId, String insNameZh, String insNameEn, String departmentZh,
      String departmentEn, String positionZh, String positionEn) {
    this.psnId = psnId;
    this.insId = insId;
    this.insNameZh = insNameZh;
    this.insNameEn = insNameEn;
    this.departmentZh = departmentZh;
    this.departmentEn = departmentEn;
    this.positionZh = positionZh;
    this.positionEn = positionEn;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_WORKHISTORY_INS_INFO", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "ins_name_zh")
  public String getInsNameZh() {
    return insNameZh;
  }

  @Column(name = "ins_name_en")
  public String getInsNameEn() {
    return insNameEn;
  }

  @Column(name = "department_zh")
  public String getDepartmentZh() {
    return departmentZh;
  }

  @Column(name = "department_en")
  public String getDepartmentEn() {
    return departmentEn;
  }

  @Column(name = "position_zh")
  public String getPositionZh() {
    return positionZh;
  }

  @Column(name = "position_en")
  public String getPositionEn() {
    return positionEn;
  }

  public void setInsNameZh(String insNameZh) {
    this.insNameZh = insNameZh;
  }

  public void setInsNameEn(String insNameEn) {
    this.insNameEn = insNameEn;
  }

  public void setDepartmentZh(String departmentZh) {
    this.departmentZh = departmentZh;
  }

  public void setDepartmentEn(String departmentEn) {
    this.departmentEn = departmentEn;
  }

  public void setPositionZh(String positionZh) {
    this.positionZh = positionZh;
  }

  public void setPositionEn(String positionEn) {
    this.positionEn = positionEn;
  }

}
