package com.smate.web.management.model.journal;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.math.NumberUtils;

/**
 * cwli基础期刊详情.
 */
public class BaseJournalDetail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1954029114650635240L;
  private Long jnlId;
  private String titleXx;
  private String titleEn;
  // 期刊名缩写
  private String titleAbbr;
  private String pissn;
  // 领域
  private String category;
  // 出版社
  private String publisher;
  // 出版社地址
  private String publisherAddress;
  // 关键词
  private String keywordAll;
  // 影响因子
  private List<BaseJnllDetaliIf> ifList;
  // 期刊最近一年是影响因子
  private String lastIfYear;
  private Double lastIf;
  // 期刊质量
  private String quality;
  private Integer intQuality;
  private double doubleQuality;
  private double qualityNum;
  private String difficulty;
  private Integer intDifficulty;
  private double doubleDifficulty;
  private double difficultyNum;
  private String cycle;
  private Integer jctPsnCount;
  private String eissn;
  // 国内统一刊号
  private String cn;
  private String frequency;
  private String publisherUrl;
  // 期刊主页
  private String journalUrl;
  // 出版语言
  private String language;
  // 创刊年
  private String startYear;
  private String dbCodes;
  private String regionName;
  private List<Long> jnlJids;
  // 在线投稿地址
  private String submissionUrl;

  public BaseJournalDetail() {}

  public BaseJournalDetail(Long jnlId, String titleXx, String titleEn, String pissn, String lastIfYear, Double lastIf,
      String difficulty, String quality, String cycle, Integer jctPsnCount) {
    this.jnlId = jnlId;
    this.titleXx = titleXx;
    this.titleEn = titleEn;
    this.pissn = pissn;
    this.lastIfYear = lastIfYear;
    this.lastIf = lastIf;
    this.difficulty = difficulty;
    this.quality = quality;
    this.cycle = cycle;
    this.jctPsnCount = jctPsnCount;
  }

  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  public String getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  public String getTitleAbbr() {
    return titleAbbr;
  }

  public void setTitleAbbr(String titleAbbr) {
    this.titleAbbr = titleAbbr;
  }

  public String getPissn() {
    return pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  public String getKeywordAll() {
    return keywordAll;
  }

  public void setKeywordAll(String keywordAll) {
    this.keywordAll = keywordAll;
  }

  public List<BaseJnllDetaliIf> getIfList() {
    return ifList;
  }

  public void setIfList(List<BaseJnllDetaliIf> ifList) {
    this.ifList = ifList;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getPublisherAddress() {
    return publisherAddress;
  }

  public void setPublisherAddress(String publisherAddress) {
    this.publisherAddress = publisherAddress;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public Integer getJctPsnCount() {
    return jctPsnCount;
  }

  public void setJctPsnCount(Integer jctPsnCount) {
    this.jctPsnCount = jctPsnCount;
  }

  public String getQuality() {
    return quality;
  }

  public void setQuality(String quality) {
    this.quality = quality;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getCycle() {
    return cycle;
  }

  public void setCycle(String cycle) {
    this.cycle = cycle;
  }

  public String getLastIfYear() {
    return lastIfYear;
  }

  public void setLastIfYear(String lastIfYear) {
    this.lastIfYear = lastIfYear;
  }

  public Double getLastIf() {
    return lastIf;
  }

  public void setLastIf(Double lastIf) {
    this.lastIf = lastIf;
  }

  public double getQualityNum() {
    if (StringUtils.isNotBlank(this.quality)) {
      qualityNum = NumberUtils.toDouble(this.quality);
    }
    return qualityNum;
  }

  public void setQualityNum(double qualityNum) {
    this.qualityNum = qualityNum;
  }

  public double getDifficultyNum() {
    if (StringUtils.isNotBlank(this.difficulty)) {
      difficultyNum = NumberUtils.toDouble(this.difficulty);
    }
    return difficultyNum;
  }

  public void setDifficultyNum(double difficultyNum) {
    this.difficultyNum = difficultyNum;
  }

  public Integer getIntQuality() {
    if (quality != null && quality.indexOf(".") > -1) {
      this.intQuality = Integer.parseInt(quality.substring(0, quality.indexOf(".")));
    } else if (quality != null) {
      this.intQuality = Integer.parseInt(quality);
    } else {
      this.intQuality = 0;
    }
    return intQuality;
  }

  public void setIntQuality(Integer intQuality) {
    this.intQuality = intQuality;
  }

  public double getDoubleQuality() {
    if (quality != null && quality.indexOf(".") > -1) {
      this.doubleQuality = Double.parseDouble(quality.substring(quality.indexOf("."), quality.length()));
    } else {
      this.doubleQuality = 0.0;
    }
    return doubleQuality;
  }

  public void setDoubleQuality(double doubleQuality) {
    this.doubleQuality = doubleQuality;
  }

  public Integer getIntDifficulty() {
    if (difficulty != null && difficulty.indexOf(".") > -1) {
      this.intDifficulty = Integer.parseInt(difficulty.substring(0, difficulty.indexOf(".")));
    } else if (difficulty != null) {
      this.intDifficulty = Integer.parseInt(difficulty);
    }
    return intDifficulty;
  }

  public void setIntDifficulty(Integer intDifficulty) {
    this.intDifficulty = intDifficulty;
  }

  public double getDoubleDifficulty() {
    if (difficulty != null && difficulty.indexOf(".") > -1) {
      this.doubleDifficulty = Double.parseDouble(difficulty.substring(difficulty.indexOf("."), difficulty.length()));
    } else {
      this.doubleDifficulty = 0.0;
    }
    return doubleDifficulty;
  }

  public void setDoubleDifficulty(double doubleDifficulty) {
    this.doubleDifficulty = doubleDifficulty;
  }

  public String getEissn() {
    return eissn;
  }

  public void setEissn(String eissn) {
    this.eissn = eissn;
  }

  public String getCn() {
    return cn;
  }

  public void setCn(String cn) {
    this.cn = cn;
  }

  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public String getPublisherUrl() {
    return publisherUrl;
  }

  public void setPublisherUrl(String publisherUrl) {
    this.publisherUrl = publisherUrl;
  }

  public String getJournalUrl() {
    return journalUrl;
  }

  public void setJournalUrl(String journalUrl) {
    this.journalUrl = journalUrl;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getStartYear() {
    return startYear;
  }

  public void setStartYear(String startYear) {
    this.startYear = startYear;
  }

  public String getDbCodes() {
    return dbCodes;
  }

  public void setDbCodes(String dbCodes) {
    this.dbCodes = dbCodes;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public List<Long> getJnlJids() {
    return jnlJids;
  }

  public void setJnlJids(List<Long> jnlJids) {
    this.jnlJids = jnlJids;
  }

  public String getSubmissionUrl() {
    return submissionUrl;
  }

  public void setSubmissionUrl(String submissionUrl) {
    this.submissionUrl = submissionUrl;
  }

}
