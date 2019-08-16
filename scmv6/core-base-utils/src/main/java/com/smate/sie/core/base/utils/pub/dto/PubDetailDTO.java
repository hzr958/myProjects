package com.smate.sie.core.base.utils.pub.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.Id;

/**
 * 成果详情（公共） 注意：一个节点多个内容统一英文分号分割，比如关键词、作者、领域等
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
public class PubDetailDTO<T extends PubTypeInfoDTO> {

  @Id
  protected Long id; // 成果id

  protected String title; // 标题

  protected Long insId; // 所属机构Id

  protected String summary; // 摘要，概要

  protected String keywords; // 关键字

  protected Integer citations;// 引用数

  protected String citationsUpdateTime;// 引用数更新时间

  protected String briefDesc; // 简短描述

  protected String authorNames;

  protected String doi; // doi

  protected String organization; // 署名单位

  protected String fundInfo; // 基金信息

  protected Set<PubSituationDTO> situations; // 收录情况

  protected List<PubMemberDTO> members; // 成员列表

  protected List<PubAttachmentsDTO> attachments; // 附件信息

  protected Integer pubType; // 成果类型id

  protected String pubTypeName;

  protected T typeInfo; // 成果类型信息

  protected Long fulltextId; // 全文id

  protected String fulltexName;

  protected String srcFulltextUrl; // 来源全文路径

  protected Integer srcDbId; // 来源dbid

  protected String sourceUrl; // 来源sourceUrl

  protected String sourceId; // 来源sourceId

  protected String publishDate; // 发表日期

  protected String citedUrl; // 引用url

  protected Integer dataFrom; // 数据来源：0表单新增，1xls导入，2标准文件导入，3联邦检索，4基准库指派，9脚本修复

  protected String disciplineCode; // 领域

  protected String disciplineName; // 领域

  protected Integer isPublicCode = 1; // 是否公开 默认公开

  protected String isPublicName = "公开"; // 领域

  protected String OA; // Open Access

  protected boolean HCP = false; // Highly Cited Paper 高被引用文章

  protected boolean HP = false; // Hot Paper 热门文章

  /**
   * 成果id
   * 
   * @return
   */
  public Long getid() {
    return id;
  }

  public void setid(Long id) {
    this.id = id;
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
  public Set<PubSituationDTO> getSituations() {
    return situations;
  }

  public void setSituations(Set<PubSituationDTO> situations) {
    this.situations = situations;
  }

  /**
   * 成员列表
   * 
   * @return
   */
  public List<PubMemberDTO> getMembers() {
    return members;
  }

  public void setMembers(List<PubMemberDTO> members) {
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
    PubDetailDTO that = (PubDetailDTO) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public String getCitationsUpdateTime() {
    return citationsUpdateTime;
  }

  public List<PubAttachmentsDTO> getAttachments() {
    return attachments;
  }

  public String getPubTypeName() {
    return pubTypeName;
  }

  public String getFulltexName() {
    return fulltexName;
  }

  public String getDisciplineCode() {
    return disciplineCode;
  }

  public String getDisciplineName() {
    return disciplineName;
  }

  public Integer getDataFrom() {
    return dataFrom;
  }

  public Integer getIsPublicCode() {
    return isPublicCode;
  }

  public void setCitationsUpdateTime(String citationsUpdateTime) {
    this.citationsUpdateTime = citationsUpdateTime;
  }

  public void setAttachments(List<PubAttachmentsDTO> attachments) {
    this.attachments = attachments;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  public void setFulltexName(String fulltexName) {
    this.fulltexName = fulltexName;
  }

  public void setDisciplineCode(String disciplineCode) {
    this.disciplineCode = disciplineCode;
  }

  public void setDisciplineName(String disciplineName) {
    this.disciplineName = disciplineName;
  }

  public void setDataFrom(Integer dataFrom) {
    this.dataFrom = dataFrom;
  }

  public void setIsPublicCode(Integer isPublicCode) {
    this.isPublicCode = isPublicCode;
  }

  @Override
  public String toString() {
    return "PubDetailDOM [id=" + id + ", title=" + title + ", insId=" + insId + ", summary=" + summary + ", keywords="
        + keywords + ", citations=" + citations + ", citationsUpdateTime=" + citationsUpdateTime + ", briefDesc="
        + briefDesc + ", authorNames=" + authorNames + ", doi=" + doi + ", organization=" + organization + ", fundInfo="
        + fundInfo + ", situations=" + situations + ", members=" + members + ", attachments=" + attachments
        + ", pubType=" + pubType + ", pubTypeName=" + pubTypeName + ", typeInfo=" + typeInfo + ", fulltextId="
        + fulltextId + ", fulltexName=" + fulltexName + ", srcFulltextUrl=" + srcFulltextUrl + ", srcDbId=" + srcDbId
        + ", sourceUrl=" + sourceUrl + ", sourceId=" + sourceId + ", publishDate=" + publishDate + ", citedUrl="
        + citedUrl + ", disciplineCode=" + disciplineCode + ", disciplineName=" + disciplineName + ", dataFrom="
        + dataFrom + ", isPublicCode=" + isPublicCode + ", isPublicName=" + isPublicName + ", OA=" + OA + ", HCP=" + HCP
        + ", HP=" + HP + "]";
  }
}
