package com.smate.center.task.model.rcmd.quartz;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果确认成果数据表.
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "PUB_CONFIRM_ROLPUB")
public class PubConfirmRolPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7431200530884994572L;
  // 单位成果编号
  @Id
  @Column(name = "ROLPUB_ID")
  private Long rolPubId;
  // 成果类型 const_pub_type
  @Column(name = "PUB_TYPE")
  private Integer typeId;
  // 出版年份
  @Column(name = "PUBLISH_YEAR")
  private Integer publishYear;
  // 出版月份
  @Column(name = "PUBLISH_MONTH")
  private Integer publishMonth;
  // 出版日期
  @Column(name = "PUBLISH_DAY")
  private Integer publishDay;
  // 文章所属的期刊的影响因子（冗余）
  @Column(name = "IMPACT_FACTORS")
  private String impactFactors;
  // 成果所属 期刊id
  @Column(name = "JID")
  private Long jid;
  // 中文标题
  @Column(name = "ZH_TITLE")
  private String zhTitle;
  // 外文标题
  @Column(name = "EN_TITLE")
  private String enTitle;
  // "中文标题hash_code，查重时使用，统一调用publicationhash.titlecode(title) 取得hash_code"
  @Column(name = "ZH_TITLE_HASH")
  private Integer zhTitleHash;
  // "英文标题hash_code，查重时使用 统一调用publicationhash.titlecode(title) 取得hash_code"
  @Column(name = "EN_TITLE_HASH")
  private Integer enTitleHash;
  // 成果指纹
  @Column(name = "FINGER_PRINT")
  private Integer fingerPrint;
  // 成果为1,文献为2
  @Column(name = "ARTICLE_TYPE")
  private Integer articleType;
  // 引用次数
  @Column(name = "CITED_TIMES")
  private Integer citedTimes;
  // 引用次数更新次数
  @Column(name = "CITED_DATE")
  private Date citedDate;
  // 查询表格显示用,该字段标记为@Transient
  @Transient
  private String htmlAbstract;

  // 成果对应的外部数据库 refrence to const_ref_db
  @Column(name = "SOURCE_DB_ID")
  private Integer sourceDbId;
  // 创建人
  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;
  // 数据来源：0: 手工输入，1:数据库导入，2:文件导入
  @Column(name = "RECORD_FROM")
  private Integer recordFrom;
  // 类别名称
  @Transient
  private String typeName;
  // 作者
  @Column(name = "AUTHOR_NAMES")
  private String authorNames;
  // 来源
  @Column(name = "BRIEF_DESC")
  private String briefDesc;
  // 全文，fulltext_fileid为空，则取fulltext_url
  @Column(name = "FULLTEXT_FILEID")
  private String fulltextFileid;
  // 全文附件节点ID
  @Column(name = "FULLTEXT_NODEID")
  private Integer fulltextNodeId;
  // 全文
  @Column(name = "FULLTEXT_URL")
  private String fulltextUrl;
  // 全文后缀
  @Column(name = "FULLTEXT_FILEEXT")
  private String fulltextExt;
  // 全文缩略图路径
  @Transient
  private String fulltextImgUrl;
  // 引用情况，用逗号分隔(如：SCI,EI)
  @Column(name = "CITED_LIST")
  private String citedList;
  // 收录情况
  @Transient
  private String listInfo;
  // 引用URL
  @Column(name = "CITED_URL")
  private String citedUrl;
  @Column(name = "START_PAGE")
  private String startPage;
  @Column(name = "END_PAGE")
  private String endPage;
  @Column(name = "ISBN")
  private String isbn;
  @Column(name = "VOLUME")
  private String volume;
  @Column(name = "ISSUE")
  private String issue;
  @Column(name = "DOI")
  private String doi;
  // isi唯一标识，对应V2.6source_id.
  @Column(name = "ISI_ID")
  private String isiId;
  // 收录情况
  @Column(name = "PUB_LIST")
  private String pubList;
  // 成果的收录情况(原始收录，不能更改)，多个用","分隔
  @Column(name = "PUB_LIST_SOURCE")
  private String pubListSource;
  // 最后更新时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate;
  // 成果作者列表
  @Transient
  private List<PubConfirmRolPubMember> memberList;
  @Transient
  private PubConfirmRolPubPdwh pdwh;
  @Transient
  private Long dtId;
  @Transient
  private Float assignScore;

  /**
   * 加密ID.
   */
  @Transient
  private String des3Id;
  @Transient
  private String scmFtUrl;
  @Transient
  private String pubUrl;
  @Transient
  private String pubTypeName;

  public PubConfirmRolPub() {
    super();
  }

  public PubConfirmRolPub(Long rolPubId, String zhTitle, String enTitle, String briefDesc, String fulltextFileid,
      Long dtId, String authorNames) {
    super();
    this.rolPubId = rolPubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.dtId = dtId;
    this.authorNames = authorNames;
  }

  public PubConfirmRolPub(Long rolPubId, String zhTitle, String enTitle, Integer typeId, String briefDesc,
      String fulltextFileid, String authorNames) {
    super();
    this.rolPubId = rolPubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.typeId = typeId;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.authorNames = authorNames;
  }

  public Long getRolPubId() {
    return rolPubId;
  }

  public void setRolPubId(Long rolPubId) {
    this.rolPubId = rolPubId;
  }

  public Integer getFulltextNodeId() {
    return fulltextNodeId;
  }

  public void setFulltextNodeId(Integer fulltextNodeId) {
    this.fulltextNodeId = fulltextNodeId;
  }

  public String getFulltextImgUrl() {
    return fulltextImgUrl;
  }

  public void setFulltextImgUrl(String fulltextImgUrl) {
    this.fulltextImgUrl = fulltextImgUrl;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public Integer getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  public Integer getSourceDbId() {
    return sourceDbId;
  }

  public String getListInfo() {
    return this.listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public Integer getPublishMonth() {
    return publishMonth;
  }

  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  public Integer getPublishDay() {
    return publishDay;
  }

  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
  }

  public String getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public Integer getZhTitleHash() {
    return zhTitleHash;
  }

  public void setZhTitleHash(Integer zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  public Integer getEnTitleHash() {
    return enTitleHash;
  }

  public void setEnTitleHash(Integer enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  public Integer getFingerPrint() {
    return fingerPrint;
  }

  public void setFingerPrint(Integer fingerPrint) {
    this.fingerPrint = fingerPrint;
  }

  /**
   * @return the articleType
   */

  public Integer getArticleType() {
    return articleType;
  }

  public String getCitedUrl() {
    return citedUrl;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
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

  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  public Long getDtId() {
    return dtId;
  }

  public void setDtId(Long dtId) {
    this.dtId = dtId;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedDate(Date citedDate) {
    this.citedDate = citedDate;
  }

  public Date getCitedDate() {
    return citedDate;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public String getCitedList() {
    return citedList;
  }

  public String getFulltextFileid() {
    return fulltextFileid;
  }

  public String getFulltextUrl() {
    return fulltextUrl;
  }

  public String getFulltextExt() {
    return fulltextExt;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public String getStartPage() {
    return startPage;
  }

  public String getEndPage() {
    return endPage;
  }

  public String getIsbn() {
    return isbn;
  }

  public String getVolume() {
    return volume;
  }

  public String getIssue() {
    return issue;
  }

  public String getDoi() {
    return doi;
  }

  public String getIsiId() {
    return isiId;
  }

  public List<PubConfirmRolPubMember> getMemberList() {
    return memberList;
  }

  public void setMemberList(List<PubConfirmRolPubMember> memberList) {
    this.memberList = memberList;
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

  public String getPubList() {
    return pubList;
  }

  public String getPubListSource() {
    return pubListSource;
  }

  public void setPubListSource(String pubListSource) {
    this.pubListSource = pubListSource;
  }

  public void setPubList(String pubList) {
    this.pubList = pubList;
  }

  public PubConfirmRolPubPdwh getPdwh() {
    return pdwh;
  }

  public String getDes3Id() {

    if (this.rolPubId != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.rolPubId.toString());
    }
    return des3Id;
  }

  public void setPdwh(PubConfirmRolPubPdwh pdwh) {
    this.pdwh = pdwh;
  }

  public Float getAssignScore() {
    return assignScore;
  }

  public void setAssignScore(Float assignScore) {
    this.assignScore = assignScore;
  }

  public String getScmFtUrl() {
    return scmFtUrl;
  }

  public void setScmFtUrl(String scmFtUrl) {
    this.scmFtUrl = scmFtUrl;
  }

  public String getPubUrl() {
    return pubUrl;
  }

  public void setPubUrl(String pubUrl) {
    this.pubUrl = pubUrl;
  }

  public String getPubTypeName() {
    return pubTypeName;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

}
