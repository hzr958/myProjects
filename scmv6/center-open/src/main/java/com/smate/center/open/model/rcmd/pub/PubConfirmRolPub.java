package com.smate.center.open.model.rcmd.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 成果确认ROL成果数据表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_CONFIRM_ROLPUB")
public class PubConfirmRolPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8951674403794663531L;
  // 单位成果编号
  private Long rolPubId;
  // 成果类型 const_pub_type
  private Integer typeId;
  // 出版年份
  private Integer publishYear;
  // 出版月份
  private Integer publishMonth;
  // 出版日期
  private Integer publishDay;
  // 文章所属的期刊的影响因子（冗余）
  private String impactFactors;
  // 成果所属 期刊id
  private Long jid;
  // 中文标题
  private String zhTitle;
  // 外文标题
  private String enTitle;
  // "中文标题hash_code，查重时使用，统一调用publicationhash.titlecode(title) 取得hash_code"
  private Integer zhTitleHash;
  // "英文标题hash_code，查重时使用 统一调用publicationhash.titlecode(title) 取得hash_code"
  private Integer enTitleHash;
  // 成果指纹
  private Integer fingerPrint;
  // 成果为1,文献为2
  private Integer articleType;
  // 引用次数
  private Integer citedTimes;
  // 引用次数更新次数
  private Date citedDate;
  // 查询表格显示用,该字段标记为@Transient
  private String htmlAbstract;

  // 成果对应的外部数据库 refrence to const_ref_db
  private Integer sourceDbId;
  // 创建人
  private Long createPsnId;
  // 数据来源：0: 手工输入，1:数据库导入，2:文件导入
  private Integer recordFrom;
  // 类别名称
  private String typeName;
  // 作者
  private String authorNames;
  // 来源
  private String briefDesc;
  // 全文，fulltext_fileid为空，则取fulltext_url
  private String fulltextFileid;
  // 全文附件节点ID
  private Integer fulltextNodeId;
  // 全文
  private String fulltextUrl;
  // 全文后缀
  private String fulltextExt;
  // 引用情况，用逗号分隔(如：SCI,EI)
  private String citedList;
  // 收录情况
  private String listInfo;
  // 引用URL
  private String citedUrl;
  private String startPage;
  private String endPage;
  private String isbn;
  private String volume;
  private String issue;
  private String doi;
  // isi唯一标识，对应V2.6source_id.
  private String isiId;
  // 收录情况
  private String pubList;
  // 成果的收录情况(原始收录，不能更改)，多个用","分隔
  private String pubListSource;
  // 最后更新时间
  private Date updateDate;
  // 创建时间
  private Date createDate;

  private Long dtId;

  private Float assignScore;

  private Long insId;// 单位id

  @Transient
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Id
  @Column(name = "ROLPUB_ID")
  public Long getRolPubId() {
    return rolPubId;
  }

  public void setRolPubId(Long rolPubId) {
    this.rolPubId = rolPubId;
  }

  @Column(name = "FULLTEXT_NODEID")
  public Integer getFulltextNodeId() {
    return fulltextNodeId;
  }

  public void setFulltextNodeId(Integer fulltextNodeId) {
    this.fulltextNodeId = fulltextNodeId;
  }

  @Column(name = "CREATE_PSN_ID")
  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  @Column(name = "RECORD_FROM")
  public Integer getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  @Column(name = "SOURCE_DB_ID")
  public Integer getSourceDbId() {
    return sourceDbId;
  }

  @Transient
  public String getListInfo() {
    return this.listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  @Column(name = "PUB_TYPE")
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  @Column(name = "PUBLISH_YEAR")
  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  @Column(name = "PUBLISH_MONTH")
  public Integer getPublishMonth() {
    return publishMonth;
  }

  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  @Column(name = "PUBLISH_DAY")
  public Integer getPublishDay() {
    return publishDay;
  }

  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
  }

  @Column(name = "IMPACT_FACTORS")
  public String getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  @Column(name = "JID")
  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  @Column(name = "ZH_TITLE_HASH")
  public Integer getZhTitleHash() {
    return zhTitleHash;
  }

  public void setZhTitleHash(Integer zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  @Column(name = "EN_TITLE_HASH")
  public Integer getEnTitleHash() {
    return enTitleHash;
  }

  public void setEnTitleHash(Integer enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  @Column(name = "FINGER_PRINT")
  public Integer getFingerPrint() {
    return fingerPrint;
  }

  public void setFingerPrint(Integer fingerPrint) {
    this.fingerPrint = fingerPrint;
  }

  /**
   * @return the articleType
   */
  @Column(name = "ARTICLE_TYPE")
  public Integer getArticleType() {
    return articleType;
  }

  @Column(name = "CITED_URL")
  public String getCitedUrl() {
    return citedUrl;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setCitedUrl(String citedUrl) {
    this.citedUrl = citedUrl;
  }

  /**
   * @param articleType the articleType to set
   */
  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public void setHtmlAbstract(String htmlAbstract) {
    this.htmlAbstract = htmlAbstract;
  }

  @Transient
  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  @Transient
  public Long getDtId() {
    return dtId;
  }

  public void setDtId(Long dtId) {
    this.dtId = dtId;
  }

  @Transient
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  @Column(name = "CITED_TIMES")
  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedDate(Date citedDate) {
    this.citedDate = citedDate;
  }

  @Column(name = "CITED_DATE")
  public Date getCitedDate() {
    return citedDate;
  }

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorNames() {
    return authorNames;
  }

  @Column(name = "CITED_LIST")
  public String getCitedList() {
    return citedList;
  }

  @Column(name = "FULLTEXT_FILEID")
  public String getFulltextFileid() {
    return fulltextFileid;
  }

  @Column(name = "FULLTEXT_URL")
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  @Column(name = "FULLTEXT_FILEEXT")
  public String getFulltextExt() {
    return fulltextExt;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  @Column(name = "BRIEF_DESC")
  public String getBriefDesc() {
    return briefDesc;
  }

  @Column(name = "START_PAGE")
  public String getStartPage() {
    return startPage;
  }

  @Column(name = "END_PAGE")
  public String getEndPage() {
    return endPage;
  }

  @Column(name = "ISBN")
  public String getIsbn() {
    return isbn;
  }

  @Column(name = "VOLUME")
  public String getVolume() {
    return volume;
  }

  @Column(name = "ISSUE")
  public String getIssue() {
    return issue;
  }

  @Column(name = "DOI")
  public String getDoi() {
    return doi;
  }

  @Column(name = "ISI_ID")
  public String getIsiId() {
    return isiId;
  }

  public void setIsiId(String isiId) {
    this.isiId = isiId;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  public void setEndPage(String endPage) {
    this.endPage = endPage;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public void setFulltextFileid(String fulltextFileid) {
    this.fulltextFileid = fulltextFileid;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public void setCitedList(String citedList) {
    this.citedList = citedList;
  }

  @Column(name = "PUB_LIST")
  public String getPubList() {
    return pubList;
  }

  @Column(name = "PUB_LIST_SOURCE")
  public String getPubListSource() {
    return pubListSource;
  }

  public void setPubListSource(String pubListSource) {
    this.pubListSource = pubListSource;
  }

  public void setPubList(String pubList) {
    this.pubList = pubList;
  }

  @Transient
  public Float getAssignScore() {
    return assignScore;
  }

  public void setAssignScore(Float assignScore) {
    this.assignScore = assignScore;
  }

  public PubConfirmRolPub() {}

  public PubConfirmRolPub(Long rolPubId, String zhTitle, String enTitle, String authorNames, String briefDesc,
      String fulltextExt, String fulltextFileid) {
    this.rolPubId = rolPubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.fulltextExt = fulltextExt;
    this.fulltextFileid = fulltextFileid;
  }

}
