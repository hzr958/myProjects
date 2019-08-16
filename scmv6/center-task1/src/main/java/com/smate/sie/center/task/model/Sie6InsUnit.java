package com.smate.sie.center.task.model;

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
 * 单位部门.
 * 
 * @author xys
 *
 */
@Entity
@Table(name = "INS_UNIT")
public class Sie6InsUnit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1156571062674364622L;

  @Id
  @Column(name = "UNIT_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_UNIT", allocationSize = 1)
  private Long id;// 部门ID
  @Column(name = "INS_ID")
  private Long insId;// 单位ID
  @Column(name = "ZH_NAME")
  private String zhName;// 部门名称（中文）
  @Column(name = "EN_NAME")
  private String enName;// 部门名称（英文）
  @Column(name = "ABBR")
  private String abbr;// 部门缩写
  @Column(name = "SUPER_UNIT_ID")
  private Long superUnitId;// 上级部门ID
  @Column(name = "URL")
  private String url;// 部门网址
  @Column(name = "TEL")
  private String tel;// 部门电话
  @Column(name = "CREATE_DATE")
  private Date creDate;
  @Column(name = "UPDATE_DATE")
  private Date updDate;
  @Column(name = "UNIT_AVATARS")
  private String unitAvatars;// 用户头像地址

  public Sie6InsUnit() {
    super();
  }

  public Sie6InsUnit(Long id) {
    super();
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public String getAbbr() {
    return abbr;
  }

  public void setAbbr(String abbr) {
    this.abbr = abbr;
  }

  public Long getSuperUnitId() {
    return superUnitId;
  }

  public void setSuperUnitId(Long superUnitId) {
    this.superUnitId = superUnitId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public Date getCreDate() {
    return creDate;
  }

  public void setCreDate(Date creDate) {
    this.creDate = creDate;
  }

  public Date getUpdDate() {
    return updDate;
  }

  public void setUpdDate(Date updDate) {
    this.updDate = updDate;
  }

  public String getUnitAvatars() {
    return unitAvatars;
  }

  public void setUnitAvatars(String unitAvatars) {
    this.unitAvatars = unitAvatars;
  }

}
