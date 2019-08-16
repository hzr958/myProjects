package com.smate.center.task.model.fund.sns;

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
@Table(name = "V_FUND_RECOMMEND_AREA")
public class FundRecommendArea implements Serializable {
  private static final long serialVersionUID = -4754831049248540064L;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_FUND_RECOMMEND_AREA", allocationSize = 1)
  private Long id;

  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "SCIENCE_AREA_ID")
  private Long scienceAreaId;

  @Column(name = "PARENT_CATEGORY_ID")
  private Long parentId;

  @Column(name = "UPDATE_DATE")
  private Date updateDate;

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

  public Long getScienceAreaId() {
    return scienceAreaId;
  }

  public void setScienceAreaId(Long scienceAreaId) {
    this.scienceAreaId = scienceAreaId;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
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
