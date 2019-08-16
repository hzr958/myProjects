package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 中国城市的区，比如深圳市南山区.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CONST_CN_DISTRICT")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ConstCnDistrict implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 8167063795978074062L;
  // 区ID
  private Long disId;
  // 区编码
  private String disCode;
  // 区名
  private String zhName;
  // 区拼音
  private String enName;
  // 同级别的中文排序字段
  private Integer zhSeq;
  // 同级别的英文排序字段
  private Integer enSeq;
  // 对应的省份ID
  private Long prvId;
  // 对应的城市ID
  private Long cyId;

  private String districtName;

  @Id
  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  @Column(name = "DIS_CODE")
  public String getDisCode() {
    return disCode;
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

  @Column(name = "PRV_ID")
  public Long getPrvId() {
    return prvId;
  }

  @Column(name = "CY_ID")
  public Long getCyId() {
    return cyId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  public void setDisCode(String disCode) {
    this.disCode = disCode;
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

  public void setPrvId(Long prvId) {
    this.prvId = prvId;
  }

  public void setCyId(Long cyId) {
    this.cyId = cyId;
  }

  @Transient
  public String getDistrictName() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if ("zh".equals(language)) {
      districtName = StringUtils.isNotBlank(zhName) ? zhName : enName;
    } else {
      districtName = StringUtils.isNotBlank(enName) ? enName : zhName;
    }
    return districtName;
  }

  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

}
