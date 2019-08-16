package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

/**
 * 为citewrite准备.
 * 
 * @author LY
 * 
 */
public class PublicationCw implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6214664294152240279L;
  // 成果编号
  private Long id;
  // 所有人ID
  private Long psnId;
  // 成果类型 const_pub_type
  private Integer typeId;
  // 最后更新时间
  private Date updateDate;
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
  private String title;
  // 成果为1,文献为2
  private Integer articleType;
  // 引用次数
  private Integer citedTimes;
  // 引用次数更新次数
  private Date citedDate;
  // 成果收藏夹
  // private List<Folder> pubFolders = new ArrayList<Folder>();
  // 查询表格显示用,该字段标记为@Transient
  private String htmlAbstract;

  // 成果对应的外部数据库 refrence to const_ref_db
  private Integer sourceDbId;

  // 类别名称
  private String typeName;
  // 作者
  private String authorNames;
  // 来源
  private String briefDesc;
  // 来源
  private String briefDescEn;
  // 全文，fulltext_fileid为空，则取fulltext_url
  private String fulltextFileid;
  // 全文
  private String fulltextUrl;
  // 引用情况，用逗号分隔(如：SCI,EI)
  private String citedList;
  // 收录情况
  private String listInfo;
  // 引用URL
  private String citedUrl;
  // 创建时间
  private Date createDate;
  private String jname;
  private String issue;
  private Integer startPage;
  private Integer endPage;
  private String volume;
  private String sourceDbName;
  private String issn;
  private String keywords;
  private String abstractText;
  private Integer nodeId;

  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public String getIssn() {
    return issn;
  }

  public String getKeywords() {
    return keywords;
  }

  public String getAbstractText() {
    return abstractText;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public void setAbstractText(String abstractText) {
    this.abstractText = abstractText;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public Integer getPublishMonth() {
    return publishMonth;
  }

  public Integer getPublishDay() {
    return publishDay;
  }

  public String getImpactFactors() {
    return impactFactors;
  }

  public Long getJid() {
    return jid;
  }

  public Integer getArticleType() {
    return articleType;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public Date getCitedDate() {
    return citedDate;
  }

  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  public Integer getSourceDbId() {
    return sourceDbId;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public String getFulltextFileid() {
    return fulltextFileid;
  }

  public String getFulltextUrl() {
    return fulltextUrl;
  }

  public String getCitedList() {
    return citedList;
  }

  public String getListInfo() {
    return listInfo;
  }

  public String getCitedUrl() {
    return citedUrl;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public String getJname() {
    return jname;
  }

  public String getIssue() {
    return issue;
  }

  public Integer getStartPage() {
    return startPage;
  }

  public Integer getEndPage() {
    return endPage;
  }

  public String getVolume() {
    return volume;
  }

  public String getSourceDbName() {
    return sourceDbName;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public void setCitedDate(Date citedDate) {
    this.citedDate = citedDate;
  }

  public void setHtmlAbstract(String htmlAbstract) {
    this.htmlAbstract = htmlAbstract;
  }

  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
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

  public void setCitedList(String citedList) {
    this.citedList = citedList;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public void setCitedUrl(String citedUrl) {
    this.citedUrl = citedUrl;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setJname(String jname) {
    this.jname = jname;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public void setStartPage(Integer startPage) {
    this.startPage = startPage;
  }

  public void setEndPage(Integer endPage) {
    this.endPage = endPage;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public void setSourceDbName(String sourceDbName) {
    this.sourceDbName = sourceDbName;
  }

  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

}
