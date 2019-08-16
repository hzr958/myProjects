package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli基础期刊title表.
 */
@Entity
@Table(name = "BASE_JOURNAL_SEARCH")
public class BaseJournalSearch implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5324027637143606874L;

  private Long jnlId;
  private String searchJnlTitle;
  private String titleXx;
  private String titleEn;
  private String pissn;
  private String dbCodes;
  private String viweCodes;
  private Double impactFactors;
  private String searchKey;
  private String keywordView;
  // 0-100
  private String keyword1;
  // 100-200
  private String keyword2;
  // 200-300
  private String keyword3;
  private String keywordAll;
  private Double startIf;
  private Double endIf;
  private String ifYear;

  private String sci;
  private String ssci;
  private String istp;
  private String ei;
  private List<String> dbids;

  // 其它库
  private String other;;

  public BaseJournalSearch() {}

  public BaseJournalSearch(Long jnlId, String titleXx, String titleEn, String dbCodes, Double impactFactors,
      String ifYear) {
    super();
    this.jnlId = jnlId;
    this.titleXx = titleXx;
    this.titleEn = titleEn;
    this.dbCodes = dbCodes;
    this.impactFactors = impactFactors;
    this.ifYear = ifYear;
  }

  @Id
  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "TITLE_XX")
  public String getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

  @Column(name = "TITLE_EN")
  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  @Column(name = "PISSN")
  public String getPissn() {
    return pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  @Column(name = "DBCODES")
  public String getDbCodes() {
    return dbCodes;
  }

  public void setDbCodes(String dbCodes) {
    this.dbCodes = dbCodes;
  }

  @Column(name = "IMPACT_FACTORS")
  public Double getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(Double impactFactors) {
    this.impactFactors = impactFactors;
  }

  @Column(name = "IF_YEAR")
  public String getIfYear() {
    return ifYear;
  }

  public void setIfYear(String ifYear) {
    this.ifYear = ifYear;
  }

  @Column(name = "KEYWORD1")
  public String getKeyword1() {
    return keyword1;
  }

  public void setKeyword1(String keyword1) {
    this.keyword1 = keyword1;
  }

  @Column(name = "KEYWORD2")
  public String getKeyword2() {
    return keyword2;
  }

  public void setKeyword2(String keyword2) {
    this.keyword2 = keyword2;
  }

  @Column(name = "KEYWORD3")
  public String getKeyword3() {
    return keyword3;
  }

  public void setKeyword3(String keyword3) {
    this.keyword3 = keyword3;
  }

  @Transient
  public String getKeywordAll() {
    String keywordTempAll = "";
    if (StringUtils.isNotBlank(keyword1)) {
      keywordTempAll += keyword1;
    }
    if (StringUtils.isNotBlank(keyword2)) {
      keywordTempAll += "; " + keyword2;
    }
    if (StringUtils.isNotBlank(keyword3)) {
      keywordTempAll += "; " + keyword3;
    }
    if (StringUtils.isNotBlank(keywordTempAll))
      keywordAll = keywordTempAll;
    return keywordAll;
  }

  public void setKeywordAll(String keywordAll) {
    this.keywordAll = keywordAll;
  }

  @Column(name = "KEYWORD_VIEW")
  public String getKeywordView() {
    return keywordView;
  }

  public void setKeywordView(String keywordView) {
    this.keywordView = keywordView;
  }

  @Transient
  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  @Transient
  public String getSearchJnlTitle() {
    return searchJnlTitle;
  }

  public void setSearchJnlTitle(String searchJnlTitle) {
    this.searchJnlTitle = searchJnlTitle;
  }

  @Transient
  public Double getStartIf() {
    return startIf;
  }

  public void setStartIf(Double startIf) {
    this.startIf = startIf;
  }

  @Transient
  public Double getEndIf() {
    return endIf;
  }

  public void setEndIf(Double endIf) {
    this.endIf = endIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Transient
  public String getSci() {
    return sci;
  }

  public void setSci(String sci) {
    this.sci = sci;
  }

  @Transient
  public String getSsci() {
    return ssci;
  }

  public void setSsci(String ssci) {
    this.ssci = ssci;
  }

  @Transient
  public String getIstp() {
    return istp;
  }

  public void setIstp(String istp) {
    this.istp = istp;
  }

  @Transient
  public String getEi() {
    return ei;
  }

  public void setEi(String ei) {
    this.ei = ei;
  }

  @Transient
  public String getViweCodes() {
    if (StringUtils.isNotBlank(dbCodes)) {
      if (dbCodes.endsWith(",")) {
        viweCodes = dbCodes.substring(1, dbCodes.length() - 1).trim();
      } else {
        viweCodes = dbCodes.substring(1, dbCodes.length()).trim();
      }
    }
    return viweCodes;
  }

  public void setViweCodes(String viweCodes) {
    this.viweCodes = viweCodes;
  }

  @Transient
  public String getOther() {
    return other;
  }

  public void setOther(String other) {
    this.other = other;
  }

  @Transient
  public List<String> getDbids() {
    return dbids;
  }

  public void setDbids(List<String> dbids) {
    this.dbids = dbids;
  }

}
