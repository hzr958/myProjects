package com.smate.center.task.model.rol.quartz;

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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.smate.center.task.model.sns.quartz.PublicationList;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 单位成果.
 * 
 * @author yamingd
 * 
 */
@Entity
@Table(name = "PUBLICATION")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class PublicationRol implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2092370568965104178L;
  // 成果编号
  private Long id;
  // 所有者(对应 Institution的insId)
  private Long insId;
  // 存储内容类型 publication=1,reference=2,file=3,project=4
  private Integer articleType;
  // 成果对应的外部数据库 refrence to const_ref_db
  private Integer sourceDbId;
  // 成果类型 const_pub_type
  private Integer typeId;
  // 出版年份
  private Integer publishYear;
  // 出版月份
  private Integer publishMonth;
  // 出版日期
  private Integer publishDay;
  // 创建人
  private Long createPsnId;
  // 创建人姓名
  private String createPsnName;
  // 版本号码
  private Integer versionNo;
  // 1未批准/2已批准/3已删除/4待单位确认
  private Integer status;
  // 国家与地区来源表 const_region
  private Integer regionId;
  // 文章所属的期刊的影响因子（冗余)
  private String impactFactors;
  // 成果所属 期刊ID
  private Long jid;
  // 数据来源：0: 手工输入，1:数据库导入，2:文件导入
  private Integer recordFrom;
  // 0/1数据是否完整
  private Integer dataValidate;
  // 引用次数
  private Integer citedTimes;
  // 最后更新引用次数的时间
  private Date citedDate;
  // 中文标题
  private String zhTitle;
  // 外文标题
  private String enTitle;
  // 中文标题小写，用于查询
  private String zhTitleText;
  // 外文标题小写，用于查询
  private String enTitleText;
  // 中文标题hash_code，查重时使用
  private Integer zhTitleHash;
  // 英文标题hash_code，查重时使用
  private Integer enTitleHash;
  // 成果指纹， 统一调用PublicationHash.finger_print(String[])生成
  private Integer fingerPrint;
  // 最后更新时间
  private Date updateDate;
  // 最后更新人
  private Long updatePsnId;
  // 从SNS提交过来的pub_id，默认为空
  private Long snsPubId;
  // 0未完成认领/1已完成认领，默认0
  private Integer authorState;
  // 第一作者,默认XML第一个
  private Long firstAuthorPsnId;
  // 查询表格显示用,该字段标记为@Transient
  private String htmlAbstract;
  // 类别名称(表格显示用)
  private String typeName;
  // 成果错误字段
  private List<PubErrorFieldRol> errorFields;
  // 所属重复组别
  private Long dupGroupId;
  // 查询申请撤销使用（合并后须从pubrolsubmission为主体反向查询）
  private Long submissionPsnId;
  // 查询申请撤销使用（合并后须从pubrolsubmission为主体反向查询）
  private String submissionPsnName;
  // 查询申请撤销使用（合并后须从pubrolsubmission为主体反向查询）
  private Long submitId;
  // 成果提交状态
  private Integer submitStatus;
  // 查重时，是否新增
  private int isJnlInsert = 0;
  // 创建时间
  private Date createDate;
  /**
   * 查询申请撤销使用（合并后须从pubrolsubmission为主体反向查询）.
   */
  private Date withdrawReqDate;
  /**
   * 加密ID.
   */
  private String des3Id;
  // 成果成员列表.
  private List<PubMemberRol> pubMemebers;
  // 成果指派PMID
  private Long assignPmId;
  // 成果确认SNS查重PUBID列表
  private String dupSnsPubIdStr;
  // 指派方式（供查询成果认领状态使用）
  private Integer assignMode;
  // 人员名称（供查询成果认领状态使用）
  private String pubMemberName;
  // 认领状态（供查询成果认领状态使用）
  private Integer confirmResult;
  /**
   * 提交日期，供查询提交成果使用.
   */
  private Date submitDate;
  private Integer totalAuthors;

  // 作者
  private String authorNames;
  // 来源
  private String briefDesc;
  // 来源
  private String briefDescEn;
  // 全文，fulltext_fileid为空，则取fulltext_url
  private String fulltextFileid;
  // 全文附件后缀
  private String fulltextExt;
  // 全文
  private String fulltextUrl;
  // 全文缩略图路径
  private String fulltextImageUrl;
  // 引用情况，用逗号分隔(如：SCI,EI)
  private String citedList;
  // 成果是否确认
  private Integer confirm;

  // 引用URL
  private String citedUrl;

  // 是否发布
  private Integer isOpen;

  private String startPage;
  private String endPage;
  private String isbn;
  private String volume;
  private String issue;
  private String doi;
  private Long oldPubId;

  // 全文附件节点ID
  private Integer fulltextNodeId;
  // 收录情况
  private String listInfo;
  // isi唯一标识，对应V2.6source_id.
  private String isiId;
  // 引用情况
  private PublicationList pubList;
  // KPI统计是否完整，0/1
  private Integer kpiValid = 0;
  // 成果项目信息
  private String pubFundInfo;
  // 是否显示收录情况
  private boolean showListInfo = false;
  // 是否显示ISI影响因子
  private boolean showImpactFactors = false;
  // 成果赞统计数
  private Integer pubAwardCount;
  // 成果分享统计数
  private Integer pubShareCount;
  // 成果对应的外部数据库名称
  private String dbCode;
  private Long oaTypeId;// 开放存储类型id
  // OA Type(Open Access Type:开放存储类型)
  private String romeoColour;
  // 阅读数
  private Long readNum;
  // 赞数
  private Long awardNum;
  private Long indexId;
  private Long downloadNum;// 下载量

  private String sigName;

  public PublicationRol() {
    super();
  }

  public PublicationRol(Long id) {
    super();
    this.id = id;
  }

  public PublicationRol(Long id, String isiId) {
    super();
    this.id = id;
    this.isiId = isiId;
  }

  public PublicationRol(Long id, Integer typeId) {
    super();
    this.id = id;
    this.typeId = typeId;
  }

  public PublicationRol(Long id, Integer typeId, Long jid) {
    super();
    this.id = id;
    this.typeId = typeId;
    this.jid = jid;
  }

  public PublicationRol(Long id, Integer typeId, Long jid, int isJnlInsert) {
    super();
    this.id = id;
    this.typeId = typeId;
    this.jid = jid;
    this.isJnlInsert = isJnlInsert;
  }

  public PublicationRol(Long id, Long insId, Integer sourceDbId) {
    super();
    this.id = id;
    this.insId = insId;
    this.sourceDbId = sourceDbId;
  }

  public PublicationRol(Integer status) {
    super();
    this.status = status;
  }

  public PublicationRol(Long id, Integer typeId, Integer publishYear, Integer publishMonth, String impactFactors,
      Long jid, Integer citeTimes, Date citeDate, Integer authorState, String authorNames, String briefDesc,
      String fulltextFileid, String fulltextUrl, String citedList, String zhTitle, String enTitle) {
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.impactFactors = impactFactors;
    this.jid = jid;
    this.citedTimes = citeTimes;
    this.citedDate = citeDate;
    this.authorState = authorState;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
    this.citedList = citedList;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.publishMonth = publishMonth;
  }

  public PublicationRol(Long id, Integer typeId, Integer publishYear, Integer publishMonth, String impactFactors,
      Long jid, Integer citeTimes, Date citeDate, Long dupGroupId) {
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.impactFactors = impactFactors;
    this.jid = jid;
    this.citedTimes = citeTimes;
    this.citedDate = citeDate;
    this.publishMonth = publishMonth;
    this.dupGroupId = dupGroupId;
  }

  public PublicationRol(Long id, Integer typeId, Integer publishYear, Integer publishMonth, String impactFactors,
      Integer citedTimes, Date citedDate, Long submissionPsnId, Long submitId, Date withdrawReqDate,
      Integer submitStatus, String authorNames, String briefDesc, String fulltextFileid, String fulltextUrl,
      String zhTitle, String enTitle, String citedUrl) {
    super();
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.impactFactors = impactFactors;
    this.citedTimes = citedTimes;
    this.citedDate = citedDate;
    this.submissionPsnId = submissionPsnId;
    this.submitId = submitId;
    this.withdrawReqDate = withdrawReqDate;
    this.submitStatus = submitStatus;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.publishMonth = publishMonth;
    this.citedUrl = citedUrl;
  }

  public PublicationRol(Long id, Integer typeId, Integer publishYear, Integer publishMonth, String impactFactors,
      Integer citedTimes, Date citedDate, Integer status, Long submissionPsnId, Long submitId, Integer submitStatus,
      Date submitDate, String authorNames, String briefDesc, String fulltextFileid, String fulltextUrl, String zhTitle,
      String enTitle, String citedUrl) {
    super();
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.impactFactors = impactFactors;
    this.citedTimes = citedTimes;
    this.citedDate = citedDate;
    this.submissionPsnId = submissionPsnId;
    this.submitId = submitId;
    this.submitStatus = submitStatus;
    this.submitDate = submitDate;
    this.status = status;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.publishMonth = publishMonth;
    this.citedUrl = citedUrl;
  }

  public PublicationRol(Long id, Integer typeId, Integer publishYear, Integer publishMonth, String impactFactors,
      Integer citeTimes, Date citeDate, Long psnId, String authorNames, String briefDesc, String fulltextFileid,
      String fulltextUrl, String zhTitle, String enTitle) {
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.impactFactors = impactFactors;
    this.citedTimes = citeTimes;
    this.citedDate = citeDate;
    this.createPsnId = psnId;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.publishMonth = publishMonth;
  }

  public PublicationRol(Long id, Integer typeId, Integer publishYear, Integer publishMonth, String impactFactors,
      Integer citeTimes, Date citeDate, Long psnId, Long assignPmId, Integer assignMode, Integer confirmResult,
      String authorNames, String briefDesc, String fulltextFileid, String fulltextUrl, String zhTitle, String enTitle,
      String citedUrl) {
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.impactFactors = impactFactors;
    this.citedTimes = citeTimes;
    this.citedDate = citeDate;
    this.createPsnId = psnId;
    this.assignPmId = assignPmId;
    this.assignMode = assignMode;
    this.confirmResult = confirmResult;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.publishMonth = publishMonth;
    this.citedUrl = citedUrl;
  }

  public PublicationRol(Long pubId, String zhTitle, String enTitle, String authorNames, String fulltextFileid,
      Long insId, String briefDesc) {

    this.id = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.fulltextFileid = fulltextFileid;
    this.authorNames = authorNames;
    this.insId = insId;
    this.briefDesc = briefDesc;
  }

  public PublicationRol(Long pubId, String zhTitle, String enTitle, Long insId) {

    this.id = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.insId = insId;
  }

  public PublicationRol(Long pubId, String zhTitle, String enTitle, Long insId, Long indexId) {
    this.id = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.insId = insId;
    this.indexId = indexId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  @Column(name = "PUB_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUBLICATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "FULLTEXT_NODEID")
  public Integer getFulltextNodeId() {
    return fulltextNodeId;
  }

  public void setFulltextNodeId(Integer fulltextNodeId) {
    this.fulltextNodeId = fulltextNodeId;
  }

  /**
   * @return the insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the status
   */
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
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

  @Column(name = "BRIEF_DESC")
  public String getBriefDesc() {
    return briefDesc;
  }

  @Column(name = "BRIEF_DESC_EN")
  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  @Column(name = "CITED_URL")
  public String getCitedUrl() {
    return citedUrl;
  }

  @Column(name = "KPI_VALID")
  public Integer getKpiValid() {
    return kpiValid;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setKpiValid(Integer kpiValid) {
    this.kpiValid = kpiValid;
  }

  public void setCitedUrl(String citedUrl) {
    this.citedUrl = citedUrl;
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

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the firstAuthorPsnId
   */
  @Column(name = "FIRST_AUTHOR_PSNID")
  public Long getFirstAuthorPsnId() {
    return firstAuthorPsnId;
  }

  /**
   * @param firstAuthorPsnId the firstAuthorPsnId to set
   */
  public void setFirstAuthorPsnId(Long firstAuthorPsnId) {
    this.firstAuthorPsnId = firstAuthorPsnId;
  }

  /**
   * @return the snsPubId
   */
  @Column(name = "SNS_PUB_ID")
  public Long getSnsPubId() {
    return snsPubId;
  }

  /**
   * @param snsPubId the snsPubId to set
   */
  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  /**
   * @return the authorState
   */
  @Column(name = "AUTHOR_STATE")
  public Integer getAuthorState() {
    return authorState;
  }

  /**
   * @param authorState the authorState to set
   */
  public void setAuthorState(Integer authorState) {
    this.authorState = authorState;
  }

  /**
   * @return the articleType
   */
  @Column(name = "ARTICLE_TYPE")
  public Integer getArticleType() {
    return articleType;
  }

  /**
   * @param articleType the articleType to set
   */
  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  /**
   * @return the sourceDbId
   */
  @Column(name = "SOURCE_DB_ID")
  public Integer getSourceDbId() {
    return sourceDbId;
  }

  /**
   * @param sourceDbId the sourceDbId to set
   */
  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  /**
   * @return the typeId
   */
  @Column(name = "PUB_TYPE")
  public Integer getTypeId() {
    return typeId;
  }

  /**
   * @param typeId the typeId to set
   */
  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  /**
   * @return the publishYear
   */
  @Column(name = "PUBLISH_YEAR")
  public Integer getPublishYear() {
    return publishYear;
  }

  /**
   * @param publishYear the publishYear to set
   */
  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  /**
   * @return the publishMonth
   */
  @Column(name = "PUBLISH_MONTH")
  public Integer getPublishMonth() {
    return publishMonth;
  }

  /**
   * @param publishMonth the publishMonth to set
   */
  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  /**
   * @return the publishDay
   */
  @Column(name = "PUBLISH_DAY")
  public Integer getPublishDay() {
    return publishDay;
  }

  /**
   * @param publishDay the publishDay to set
   */
  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
  }

  /**
   * @return the createPsnId
   */
  @Column(name = "CREATE_PSN_ID")
  public Long getCreatePsnId() {
    return createPsnId;
  }

  /**
   * @param createPsnId the createPsnId to set
   */
  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  @Column(name = "VERSION_NO")
  public Integer getVersionNo() {
    return versionNo;
  }

  public void setVersionNo(Integer versionNo) {
    this.versionNo = versionNo;
  }

  /**
   * @return the regionId
   */
  @Column(name = "REGION_ID")
  public Integer getRegionId() {
    return regionId;
  }

  /**
   * @param regionId the regionId to set
   */
  public void setRegionId(Integer regionId) {
    this.regionId = regionId;
  }

  /**
   * @return the impactFactors
   */
  @Column(name = "IMPACT_FACTORS")
  public String getImpactFactors() {
    return impactFactors;
  }

  /**
   * @param impactFactors the impactFactors to set
   */
  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  /**
   * @return the jid
   */
  @Column(name = "JID")
  public Long getJid() {
    return jid;
  }

  /**
   * @param jid the jid to set
   */
  public void setJid(Long jid) {
    this.jid = jid;
  }

  /**
   * @return the recordFrom
   */
  @Column(name = "RECORD_FROM")
  public Integer getRecordFrom() {
    return recordFrom;
  }

  /**
   * @param recordFrom the recordFrom to set
   */
  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  /**
   * @return the dataValidate
   */
  @Column(name = "IS_VALID")
  public Integer getDataValidate() {
    return dataValidate;
  }

  /**
   * @param dataValidate the dataValidate to set
   */
  public void setDataValidate(Integer dataValidate) {
    this.dataValidate = dataValidate;
  }

  /**
   * @return the citedTimes
   */
  @Column(name = "CITED_TIMES")
  public Integer getCitedTimes() {
    return citedTimes;
  }

  /**
   * @param citedTimes the citedTimes to set
   */
  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  /**
   * @return the citedDate
   */
  @Column(name = "CITED_DATE")
  public Date getCitedDate() {
    return citedDate;
  }

  /**
   * @param citedDate the citedDate to set
   */
  public void setCitedDate(Date citedDate) {
    this.citedDate = citedDate;
  }

  /**
   * @return the zhTitle
   */
  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  /**
   * @param zhTitle the zhTitle to set
   */
  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  /**
   * @return the enTitle
   */
  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  /**
   * @param enTitle the enTitle to set
   */
  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  /**
   * @return the zhTitleHash
   */
  @Column(name = "ZH_TITLE_HASH")
  public Integer getZhTitleHash() {
    return zhTitleHash;
  }

  /**
   * @param zhTitleHash the zhTitleHash to set
   */
  public void setZhTitleHash(Integer zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  /**
   * @return the enTtitleHash
   */
  @Column(name = "EN_TITLE_HASH")
  public Integer getEnTitleHash() {
    return enTitleHash;
  }

  /**
   * @param enTtitleHash the enTtitleHash to set
   */
  public void setEnTitleHash(Integer enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  /**
   * @return the fingerPrint
   */
  @Column(name = "FINGER_PRINT")
  public Integer getFingerPrint() {
    return fingerPrint;
  }

  /**
   * @param fingerPrint the fingerPrint to set
   */
  public void setFingerPrint(Integer fingerPrint) {
    this.fingerPrint = fingerPrint;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "UPDATE_PSN_ID")
  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
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

  @Column(name = "FROM_PUB_ID")
  public Long getOldPubId() {
    return oldPubId;
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

  public void setOldPubId(Long oldPubId) {
    this.oldPubId = oldPubId;
  }

  public void setHtmlAbstract(String htmlAbstract) {
    this.htmlAbstract = htmlAbstract;
  }

  @Transient
  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  @Transient
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  /**
   * @return the errorFields
   */
  @Transient
  public List<PubErrorFieldRol> getErrorFields() {
    return errorFields;
  }

  /**
   * @param errorFields the errorFields to set
   */
  public void setErrorFields(List<PubErrorFieldRol> errorFields) {
    this.errorFields = errorFields;
  }

  @Transient
  public String getDes3Id() {

    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  @Transient
  public String getCreatePsnName() {
    return createPsnName;
  }

  public void setCreatePsnName(String createPsnName) {
    this.createPsnName = createPsnName;
  }

  @Transient
  public Long getSubmissionPsnId() {
    return submissionPsnId;
  }

  @Transient
  public Long getSubmitId() {
    return submitId;
  }

  public void setSubmissionPsnId(Long submissionPsnId) {
    this.submissionPsnId = submissionPsnId;
  }

  public void setSubmitId(Long submitId) {
    this.submitId = submitId;
  }

  @Transient
  public Date getWithdrawReqDate() {
    return withdrawReqDate;
  }

  public void setWithdrawReqDate(Date withdrawReqDate) {
    this.withdrawReqDate = withdrawReqDate;
  }

  @Column(name = "DUP_GROUP_ID")
  public Long getDupGroupId() {
    return dupGroupId;
  }

  @Column(name = "ISI_ID")
  public String getIsiId() {
    return isiId;
  }

  @Column(name = "FULLTEXT_FILEEXT")
  public String getFulltextExt() {
    return fulltextExt;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  public void setIsiId(String isiId) {
    this.isiId = isiId;
  }

  public void setDupGroupId(Long dupGroupId) {
    this.dupGroupId = dupGroupId;
  }

  @Transient
  public String getSubmissionPsnName() {
    return submissionPsnName;
  }

  public void setSubmissionPsnName(String submissionPsnName) {
    this.submissionPsnName = submissionPsnName;
  }

  @Transient
  public Integer getSubmitStatus() {
    return submitStatus;
  }

  public void setSubmitStatus(Integer submitStatus) {
    this.submitStatus = submitStatus;
  }

  @Transient
  public List<PubMemberRol> getPubMemebers() {
    return pubMemebers;
  }

  public void setPubMemebers(List<PubMemberRol> pubMemebers) {
    this.pubMemebers = pubMemebers;
  }

  @Transient
  public Long getAssignPmId() {
    return assignPmId;
  }

  public void setAssignPmId(Long assignPmId) {
    this.assignPmId = assignPmId;
  }

  @Transient
  public String getDupSnsPubIdStr() {
    return dupSnsPubIdStr;
  }

  public void setDupSnsPubIdStr(String dupSnsPubIdStr) {
    this.dupSnsPubIdStr = dupSnsPubIdStr;
  }

  @Transient
  public Integer getAssignMode() {
    return assignMode;
  }

  public void setAssignMode(Integer assignMode) {
    this.assignMode = assignMode;
  }

  @Transient
  public String getPubMemberName() {
    return pubMemberName;
  }

  public void setPubMemberName(String pubMemberName) {
    this.pubMemberName = pubMemberName;
  }

  @Transient
  public Integer getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  @Transient
  public Date getSubmitDate() {
    return submitDate;
  }

  public void setSubmitDate(Date submitDate) {
    this.submitDate = submitDate;
  }

  /**
   * @return the totalAuthors
   */
  @Transient
  public Integer getTotalAuthors() {
    return totalAuthors;
  }

  /**
   * @param totalAuthors the totalAuthors to set
   */
  public void setTotalAuthors(Integer totalAuthors) {
    this.totalAuthors = totalAuthors;
  }

  @Column(name = "CONFIRM_RESULT")
  public Integer getConfirm() {
    return confirm;
  }

  public void setConfirm(Integer confirm) {
    this.confirm = confirm;
  }

  @Column(name = "IS_OPEN")
  public Integer getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  @Column(name = "ZH_TITLE_TEXT")
  public String getZhTitleText() {
    return zhTitleText;
  }

  @Column(name = "EN_TITLE_TEXT")
  public String getEnTitleText() {
    return enTitleText;
  }

  public void setZhTitleText(String zhTitleText) {
    this.zhTitleText = zhTitleText;
  }

  public void setEnTitleText(String enTitleText) {
    this.enTitleText = enTitleText;
  }

  @Transient
  public String getListInfo() {
    return listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  @Transient
  public int getIsJnlInsert() {
    return isJnlInsert;
  }

  public void setIsJnlInsert(int isJnlInsert) {
    this.isJnlInsert = isJnlInsert;
  }

  @Transient
  public PublicationList getPubList() {
    return pubList;
  }

  public void setPubList(PublicationList pubList) {
    this.pubList = pubList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Transient
  public int getPmSize() {
    if (pubMemebers == null) {
      return 0;
    }
    return pubMemebers.size();
  }

  @Transient
  public String getPubFundInfo() {
    return pubFundInfo;
  }

  public void setPubFundInfo(String pubFundInfo) {
    this.pubFundInfo = pubFundInfo;
  }

  @Transient
  public boolean isShowListInfo() {
    return showListInfo;
  }

  public void setShowListInfo(boolean showListInfo) {
    this.showListInfo = showListInfo;
  }

  @Transient
  public boolean isShowImpactFactors() {
    return showImpactFactors;
  }

  public void setShowImpactFactors(boolean showImpactFactors) {
    this.showImpactFactors = showImpactFactors;
  }

  @Transient
  public Integer getPubAwardCount() {
    return pubAwardCount;
  }

  @Transient
  public Integer getPubShareCount() {
    return pubShareCount;
  }

  public void setPubAwardCount(Integer pubAwardCount) {
    this.pubAwardCount = pubAwardCount;
  }

  public void setPubShareCount(Integer pubShareCount) {
    this.pubShareCount = pubShareCount;
  }

  @Transient
  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
  }

  @Transient
  public String getFulltextImageUrl() {
    return fulltextImageUrl;
  }

  public void setFulltextImageUrl(String fulltextImageUrl) {
    this.fulltextImageUrl = fulltextImageUrl;
  }

  @Transient
  public Long getOaTypeId() {
    return oaTypeId;
  }

  public void setOaTypeId(Long oaTypeId) {
    this.oaTypeId = oaTypeId;
  }

  @Transient
  public String getRomeoColour() {
    return romeoColour;
  }

  public void setRomeoColour(String romeoColour) {
    this.romeoColour = romeoColour;
  }

  @Transient
  public Long getReadNum() {
    return readNum;
  }

  public void setReadNum(Long readNum) {
    this.readNum = readNum;
  }

  @Transient
  public Long getAwardNum() {
    return awardNum;
  }

  public void setAwardNum(Long awardNum) {
    this.awardNum = awardNum;
  }

  @Transient
  public Long getIndexId() {
    return indexId;
  }

  public void setIndexId(Long indexId) {
    this.indexId = indexId;
  }

  @Transient
  public Long getDownloadNum() {
    return downloadNum;
  }

  public void setDownloadNum(Long downloadNum) {
    this.downloadNum = downloadNum;
  }

  @Transient
  public String getSigName() {
    return sigName;
  }

  public void setSigName(String sigName) {
    this.sigName = sigName;
  }

}
