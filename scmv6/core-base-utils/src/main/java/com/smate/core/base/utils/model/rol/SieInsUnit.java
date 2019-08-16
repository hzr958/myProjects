package com.smate.core.base.utils.model.rol;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 部门实体.
 * 
 * @author yxs
 * 
 */
@Entity
@Table(name = "INS_UNIT")
public class SieInsUnit {

  private Long id;
  private Long insId;
  private String zhName;
  private String enName;
  private String abbr;
  private Long superInsUnitId;
  private String name;
  private String des3UnitId;
  private String unitUrl;
  private String disciName;
  private String mobile;
  private Date creDate;
  private Date updDate;
  private String unitAvatars;// 用户头像地址
  private Integer psnSum; // 人员数
  private Integer prjSum;// 项目数
  private Integer pubSum;// 成果数
  private Integer ptSum;// 专利数

  public SieInsUnit() {
    super();
  }

  public SieInsUnit(Long id, String zhName, String enName) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
  }

  public SieInsUnit(Long insId, String zhName, String enName, String abbr, Long superInsUnitId) {
    super();
    this.insId = insId;
    this.zhName = zhName;
    this.enName = enName;
    this.abbr = abbr;
    this.superInsUnitId = superInsUnitId;
  }

  public SieInsUnit(Long insId, String zhName, String enName, String abbr, Long superInsUnitId, String unitUrl) {
    super();
    this.insId = insId;
    this.zhName = zhName;
    this.enName = enName;
    this.abbr = abbr;
    this.superInsUnitId = superInsUnitId;
    this.unitUrl = unitUrl;
  }

  @Id
  @Column(name = "UNIT_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_UNIT", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "ABBR")
  public String getAbbr() {
    return abbr;
  }

  public void setAbbr(String abbr) {
    this.abbr = abbr;
  }

  @Column(name = "SUPER_UNIT_ID")
  public Long getSuperInsUnitId() {
    return superInsUnitId;
  }

  public void setSuperInsUnitId(Long superInsUnitId) {
    this.superInsUnitId = superInsUnitId;
  }

  /**
   * 目前此方法添加了国际化的支持
   * 
   * @return
   */
  @Transient
  public String getName() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if ("zh".equals(language)) {
      name = StringUtils.isNotBlank(this.zhName) ? this.zhName : this.enName;
    } else {
      name = StringUtils.isNotBlank(this.enName) ? this.enName : this.zhName;
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Transient
  public String getDes3UnitId() {

    if (this.id != null && des3UnitId == null) {
      des3UnitId = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3UnitId;
  }

  public void setDes3UnitId(String des3UnitId) {
    this.des3UnitId = des3UnitId;
  }

  @Column(name = "url")
  public String getUnitUrl() {
    return unitUrl;
  }

  public void setUnitUrl(String unitUrl) {
    this.unitUrl = unitUrl;
  }

  @Column(name = "TEL")
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }


  @Column(name = "CREATE_DATE")
  public Date getCreDate() {
    return creDate;
  }

  public void setCreDate(Date creDate) {
    this.creDate = creDate;
  }


  @Column(name = "UPDATE_DATE")
  public Date getUpdDate() {
    return updDate;
  }

  public void setUpdDate(Date updDate) {
    this.updDate = updDate;
  }


  @Column(name = "UNIT_AVATARS")
  public String getUnitAvatars() {
    return unitAvatars;
  }

  public void setUnitAvatars(String unitAvatars) {
    this.unitAvatars = unitAvatars;
  }

  @Transient
  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  @Transient
  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  @Transient
  public Integer getPtSum() {
    return ptSum;
  }

  public void setPtSum(Integer ptSum) {
    this.ptSum = ptSum;
  }

  @Transient
  public Integer getPsnSum() {
    return psnSum;
  }

  public void setPsnSum(Integer psnSum) {
    this.psnSum = psnSum;
  }

  @Transient
  public String getDisciName() {
    return disciName;
  }

  public void setDisciName(String disciName) {
    this.disciName = disciName;
  }

}
