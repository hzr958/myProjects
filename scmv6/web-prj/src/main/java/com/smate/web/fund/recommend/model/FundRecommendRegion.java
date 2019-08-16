package com.smate.web.fund.recommend.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.common.LocaleTextUtils;

@Entity
@Table(name = "V_FUND_RECOMMEND_REGION")
public class FundRecommendRegion implements Serializable {
  private static final long serialVersionUID = -7345950806158199813L;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FUND_RECOMMEND_REGION", allocationSize = 1)
  private Long id;

  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "REGION_ID")
  private Long regionId;

  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  @Column(name = "SUPER_REGION_STRID")
  private String superRegionStrId;

  @Column(name = "ZH_NAME")
  private String zhName;

  @Column(name = "EN_NAME")
  private String enName;

  @Transient
  private String showName;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getSuperRegionStrId() {
    return superRegionStrId;
  }

  public void setSuperRegionStrId(String superRegionStrId) {
    this.superRegionStrId = superRegionStrId;
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

  public String getShowName() {
    Locale locale = LocaleContextHolder.getLocale();
    this.showName = LocaleTextUtils.getLocaleText(locale, zhName, enName);
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

}
