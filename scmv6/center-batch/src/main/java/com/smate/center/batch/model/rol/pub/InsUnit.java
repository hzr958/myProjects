package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.List;

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

import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 部门实体.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "INS_UNIT")
public class InsUnit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8157002840991435759L;
  private Long id;
  private Long insId;
  private String zhName;
  private String enName;
  private String abbr;
  private Long superInsUnitId;
  private String name;
  private List<InsUnit> subInsUnit;
  private String des3UnitId;
  private String unitUrl;
  private List<RolPsnIns> psnInsList;

  // 部门资源数统计
  private InsUnitResourcesCount insUnitResourcesCount;

  public InsUnit() {
    super();
  }

  public InsUnit(Long id, String zhName, String enName) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
  }

  public InsUnit(Long insId, String zhName, String enName, String abbr, Long superInsUnitId) {
    super();
    this.insId = insId;
    this.zhName = zhName;
    this.enName = enName;
    this.abbr = abbr;
    this.superInsUnitId = superInsUnitId;
  }

  public InsUnit(Long insId, String zhName, String enName, String abbr, Long superInsUnitId, String unitUrl) {
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

  @Transient
  public List<InsUnit> getSubInsUnit() {
    return subInsUnit;
  }

  public void setSubInsUnit(List<InsUnit> subInsUnit) {
    this.subInsUnit = subInsUnit;
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

  @Transient
  public List<RolPsnIns> getPsnInsList() {
    return psnInsList;
  }

  public void setPsnInsList(List<RolPsnIns> psnInsList) {
    this.psnInsList = psnInsList;
  }

  @Column(name = "unit_url")
  public String getUnitUrl() {
    return unitUrl;
  }

  public void setUnitUrl(String unitUrl) {
    this.unitUrl = unitUrl;
  }

  @Transient
  public InsUnitResourcesCount getInsUnitResourcesCount() {
    return insUnitResourcesCount;
  }

  public void setInsUnitResourcesCount(InsUnitResourcesCount insUnitResourcesCount) {
    this.insUnitResourcesCount = insUnitResourcesCount;
  }

}
