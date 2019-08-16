package com.smate.center.batch.model.pdwh.pub.isi;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * isi成果表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "ISI_PUBLICATION")
public class IsiPublication implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6341227003487175693L;
  private Long pubId;
  // 出版年份
  private Integer pubYear;
  // 网站ID
  private Integer dbId;
  // 来源ID
  private String sourceId;
  private String doi;
  // 中文标题
  private String title;
  // 成果类型
  private Integer pubType;
  // 引用次数
  private Integer citedTimes;
  // 发表刊物
  private String original;
  // ISSN
  private String issn;
  // ISBN
  private String isbn;
  // 期号
  private String issue;
  // 卷号
  private String volume;
  // 开始页
  private String startPage;
  // 结束页
  private String endPage;
  // article number记录一篇文章在一本期刊集里位置
  private String articleNo;
  // 作者
  private String authorNames;
  // 专利号
  private String patentNo;
  // 会议名称
  private String confName;
  // 文献抓取人员psn_id，批量抓取为空
  private Long createPsn;
  // 文献抓取人员ins_id，批量抓取为空
  private Long createIns;
  // 抓取日期，批量抓取为导入时间
  private Date fetchDate;
  // 单位
  private String organization;
  // source_id hash
  private Long sourceIdHash;
  // 标题hash
  private Long titleHash;
  // 组合hash
  private Long unitHash;
  // 文献地址列表
  private List<IsiPubAddr> pubAddrs;
  private String zhKeywords;
  private String enKeywords;
  private String fundInfo;
  // 研究方向
  private String scienceCategory;

  @Id
  @Column(name = "PUB_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ISI_PUBLICATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUBYEAR")
  public Integer getPubYear() {
    return pubYear;
  }

  @Column(name = "DBID")
  public Integer getDbId() {
    return dbId;
  }

  @Column(name = "SOURCE_ID")
  public String getSourceId() {
    return sourceId;
  }

  @Column(name = "DOI")
  public String getDoi() {
    return doi;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  @Column(name = "ORIGINAL")
  public String getOriginal() {
    return original;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  @Column(name = "ISBN")
  public String getIsbn() {
    return isbn;
  }

  @Column(name = "ISSUE")
  public String getIssue() {
    return issue;
  }

  @Column(name = "VOLUME")
  public String getVolume() {
    return volume;
  }

  @Column(name = "START_PAGE")
  public String getStartPage() {
    return startPage;
  }

  @Column(name = "END_PAGE")
  public String getEndPage() {
    return endPage;
  }

  @Column(name = "ARTICLE_NO")
  public String getArticleNo() {
    return articleNo;
  }

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorNames() {
    return authorNames;
  }

  @Column(name = "PATENT_NO")
  public String getPatentNo() {
    return patentNo;
  }

  @Column(name = "CONF_NAME")
  public String getConfName() {
    return confName;
  }

  @Column(name = "CREATE_PSN")
  public Long getCreatePsn() {
    return createPsn;
  }

  @Column(name = "FETCH_DATE")
  public Date getFetchDate() {
    return fetchDate;
  }

  @Column(name = "CITED_TIMES")
  public Integer getCitedTimes() {
    return citedTimes;
  }

  @Column(name = "CREATE_INS")
  public Long getCreateIns() {
    return createIns;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public void setCreateIns(Long createIns) {
    this.createIns = createIns;
  }

  @Transient
  public String getOrganization() {
    return organization;
  }

  @Transient
  public Long getSourceIdHash() {
    return sourceIdHash;
  }

  @Transient
  public Long getUnitHash() {
    return unitHash;
  }

  @Transient
  public List<IsiPubAddr> getPubAddrs() {
    return pubAddrs;
  }

  @Transient
  public Long getTitleHash() {
    return titleHash;
  }

  @Transient
  public String getZhKeywords() {
    return zhKeywords;
  }

  @Transient
  public String getEnKeywords() {
    return enKeywords;
  }

  @Transient
  public String getFundInfo() {
    return fundInfo;
  }

  @Transient
  public String getScienceCategory() {
    return scienceCategory;
  }

  public void setZhKeywords(String zhKeywords) {
    this.zhKeywords = zhKeywords;
  }

  public void setEnKeywords(String enKeywords) {
    this.enKeywords = enKeywords;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  public void setPubAddrs(List<IsiPubAddr> pubAddrs) {
    this.pubAddrs = pubAddrs;
  }

  public void setSourceIdHash(Long sourceIdHash) {
    this.sourceIdHash = sourceIdHash;
  }

  public void setUnitHash(Long unitHash) {
    this.unitHash = unitHash;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  public void setEndPage(String endPage) {
    this.endPage = endPage;
  }

  public void setArticleNo(String articleNo) {
    this.articleNo = articleNo;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public void setPatentNo(String patentNo) {
    this.patentNo = patentNo;
  }

  public void setConfName(String confName) {
    this.confName = confName;
  }

  public void setCreatePsn(Long createPsn) {
    this.createPsn = createPsn;
  }

  public void setFetchDate(Date fetchDate) {
    this.fetchDate = fetchDate;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  public void setScienceCategory(String scienceCategory) {
    this.scienceCategory = scienceCategory;
  }
}
