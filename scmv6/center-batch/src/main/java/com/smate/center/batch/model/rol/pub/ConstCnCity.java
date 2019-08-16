package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 中国地区.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CONST_CN_CITY")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ConstCnCity implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 2446629649950078659L;
  // 地区ID，对应const_region的id
  private Long cyId;
  // 地区代码
  private String cyCode;
  // 所在省份ID
  private Long prvId;
  // 地区名
  private String zhName;
  private String enName;
  // 同级别的中文排序字段
  private Integer zhSeq;
  // 同级别的英文排序字段
  private Integer enSeq;

  private String cityName;

  private ConstCnProvince constCnProvince;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(insertable = false, updatable = false, name = "PRV_ID")
  public ConstCnProvince getConstCnProvince() {
    return constCnProvince;
  }

  public void setConstCnProvince(ConstCnProvince constCnProvince) {
    this.constCnProvince = constCnProvince;
  }

  @Id
  @Column(name = "CY_ID")
  public Long getCyId() {
    return cyId;
  }

  @Column(name = "CY_CODE")
  public String getCyCode() {
    return cyCode;
  }

  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  @Column(name = "ZH_SEQ")
  public Integer getZhSeq() {
    return zhSeq;
  }

  @Column(name = "EN_SEQ")
  public Integer getEnSeq() {
    return enSeq;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
  }

  public void setCyCode(String cyCode) {
    this.cyCode = cyCode;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setZhSeq(Integer zhSeq) {
    this.zhSeq = zhSeq;
  }

  public void setEnSeq(Integer enSeq) {
    this.enSeq = enSeq;
  }

  /**
   * 目前此方法添加了国际化的支持
   * 
   * @return
   */
  @Transient
  public String getCityName() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if ("zh".equals(language)) {
      cityName = StringUtils.isNotBlank(zhName) ? zhName : enName;
    } else {
      cityName = StringUtils.isNotBlank(enName) ? enName : zhName;
    }
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

}
