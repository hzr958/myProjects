package com.smate.web.psn.model.autocomplete;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli期刊影响因子.
 */

public class BaseJnllDetaliIf implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7733780239577755485L;
  private Long id;
  // 影响因子的年份，如：2008
  private String year;
  private Long dbId;
  // 对应的影响因子值
  private String impactFactor;
  // total cites总被引数
  private String totaiCites;
  // Immediacy Index, 即时系数
  private String immediacyIndex;
  // Cited Half Life,被引半衰期
  private String citedHalfLife;
  // citing half life,引用半衰期
  private String citingHalfLife;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  public String getImpactFactor() {
    return impactFactor;
  }

  public void setImpactFactor(String impactFactor) {
    this.impactFactor = impactFactor;
  }

  public String getTotaiCites() {
    return totaiCites;
  }

  public void setTotaiCites(String totaiCites) {
    this.totaiCites = totaiCites;
  }

  public String getImmediacyIndex() {
    return immediacyIndex;
  }

  public void setImmediacyIndex(String immediacyIndex) {
    this.immediacyIndex = immediacyIndex;
  }

  public String getCitedHalfLife() {
    return citedHalfLife;
  }

  public void setCitedHalfLife(String citedHalfLife) {
    this.citedHalfLife = citedHalfLife;
  }

  public String getCitingHalfLife() {
    return citingHalfLife;
  }

  public void setCitingHalfLife(String citingHalfLife) {
    this.citingHalfLife = citingHalfLife;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
