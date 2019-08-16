package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchForm implements Serializable {

  private static final long serialVersionUID = 8430755247488527751L;

  // 领域关键词,多个，隔开
  private String areaCodeStr;
  // 点击父节点的时候，存贮父节点
  private String areaCode;
  // 检索条件
  private String searchKey;
  private String title;
  private String pubAbstract;
  private Integer type;
  private String authorNames;
  private String keywords;
  private Integer startYear;
  private Integer endYear;
  // 论文检索，单位或地址信息
  private String organization;
  // 论文检索，期刊或会议出版物等刊物名称
  private String original;
  private String pubYear;
  private String pubType;
  private String keyword;
  private Long psnId;
  private String language;

  public String getAreaCodeStr() {
    return areaCodeStr;
  }

  public void setAreaCodeStr(String areaCodeStr) {
    this.areaCodeStr = areaCodeStr;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public String getSearchKey() {
    if (StringUtils.isNotBlank(searchKey)) {
      return searchKey.trim().toLowerCase();
    }
    return "";
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPubAbstract() {
    return pubAbstract;
  }

  public void setPubAbstract(String pubAbstract) {
    this.pubAbstract = pubAbstract;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Integer getStartYear() {
    return startYear;
  }

  public void setStartYear(Integer startYear) {
    this.startYear = startYear;
  }

  public Integer getEndYear() {
    return endYear;
  }

  public void setEndYear(Integer endYear) {
    this.endYear = endYear;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getPubYear() {
    return pubYear;
  }

  public void setPubYear(String pubYear) {
    this.pubYear = pubYear;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
