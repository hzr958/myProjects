package com.smate.web.v8pub.dom;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 成果详情
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:51
 */
public class PubDetailDOM<T extends PubTypeInfoBean> {

  @Id
  protected Long pubId; // 成果id

  protected String title = new String(); // 标题

  protected Long insId; // 所属机构Id

  protected String summary = new String(); // 摘要，概要

  protected String keywords = new String(); // 关键字

  protected Long countryId; // 国家或地区id

  protected Integer citations;// 引用数

  protected String briefDesc = new String(); // 简短描述

  protected String authorNames = new String(); // 作者名称，抓下来的原始串，拆分后放在PubmemberBean对象name属性中

  protected String doi = new String(); // doi

  protected Long fulltextId; // 全文id

  protected String organization = new String(); // 单位组织

  protected String fundInfo = new String(); // 基金信息

  protected Set<PubSituationBean> situations; // 收录情况

  protected List<PubMemberBean> members; // 成员列表

  protected Integer pubType = 7; // 成果类型id

  protected T typeInfo; // 成果类型信息

  protected String srcFulltextUrl = new String(); // 来源全文路径

  protected Integer srcDbId; // 来源dbid

  protected String sourceUrl = new String(); // 来源sourceUrl

  protected String sourceId = new String(); // 来源sourceId

  protected String publishDate = new String(); // 发表日期

  protected String citedUrl = new String(); // 引用url

  protected String OA = new String(); // Open Access

  protected boolean HCP = false; // Highly Cited Paper 高被引用文章

  protected boolean HP = false; // Hot Paper 热门文章

  /**
   * 成果id
   * 
   * @return
   */
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  /**
   * 成果标题
   * 
   * @return
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * 摘要
   * 
   * @return
   */
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  /**
   * 关键词
   * 
   * @return
   */
  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  /**
   * 国家或地区id
   * 
   * @return
   */
  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

  /**
   * 简短描述
   * 
   * @return
   */
  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  /**
   * 作者名
   * 
   * @return
   */
  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  /**
   * DOI
   * 
   * @return
   */
  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  /**
   * 全文id
   * 
   * @return
   */
  public Long getFulltextId() {
    return fulltextId;
  }

  public void setFulltextId(Long fulltextId) {
    this.fulltextId = fulltextId;
  }

  /**
   * 组织单位
   * 
   * @return
   */
  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  /**
   * 基金标注信息
   * 
   * @return
   */
  public String getFundInfo() {
    return fundInfo;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  public Integer getSrcDbId() {
    return srcDbId;
  }

  public void setSrcDbId(Integer srcDbId) {
    this.srcDbId = srcDbId;
  }

  /**
   * 收录情况集合
   * 
   * @return
   */
  public Set<PubSituationBean> getSituations() {
    return situations;
  }

  public void setSituations(Set<PubSituationBean> situations) {
    this.situations = situations;
  }

  /**
   * 成员列表
   * 
   * @return
   */
  public List<PubMemberBean> getMembers() {
    return members;
  }

  public void setMembers(List<PubMemberBean> members) {
    this.members = members;
  }

  /**
   * 类型详情
   * 
   * @return
   */
  public T getTypeInfo() {
    return typeInfo;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setTypeInfo(T typeInfo) {
    this.typeInfo = typeInfo;
  }

  /**
   * 源全文地址
   *
   * @return
   */
  public String getSrcFulltextUrl() {
    return srcFulltextUrl;
  }

  public void setSrcFulltextUrl(String srcFulltextUrl) {
    this.srcFulltextUrl = srcFulltextUrl;
  }

  public Integer getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  public String getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }

  public String getOA() {
    return OA;
  }

  public void setOA(String oA) {
    OA = oA;
  }

  public boolean isHCP() {
    return HCP;
  }

  public void setHCP(boolean hCP) {
    HCP = hCP;
  }

  public boolean isHP() {
    return HP;
  }

  public void setHP(boolean hP) {
    HP = hP;
  }

  public String getCitedUrl() {
    return citedUrl;
  }

  public void setCitedUrl(String citedUrl) {
    this.citedUrl = citedUrl;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PubDetailDOM that = (PubDetailDOM) o;
    return Objects.equals(pubId, that.pubId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pubId);
  }

  @Override
  public String toString() {
    return "PubDetailDOM{" + "pubId='" + pubId + '\'' + ", title='" + title + '\'' + ", summary='" + summary + '\''
        + ", keywords='" + keywords + '\'' + ", countryId='" + countryId + '\'' + ", briefDesc='" + briefDesc + '\''
        + ", authorNames='" + authorNames + '\'' + ", doi='" + doi + '\'' + ", fulltextId=" + fulltextId
        + ", organization='" + organization + '\'' + ", fundInfo='" + fundInfo + '\'' + ", situations=" + situations
        + ", members=" + members + ", pubType='" + pubType + '\'' + ", typeInfo=" + typeInfo + ", publishDate='"
        + publishDate + '\'' + ", citations='" + citations + '}';
  }
}
