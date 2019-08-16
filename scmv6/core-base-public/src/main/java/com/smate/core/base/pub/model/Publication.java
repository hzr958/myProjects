package com.smate.core.base.pub.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果
 * 
 * @author hzr
 * 
 */
@Entity
@Table(name = "PUBLICATION")
public class Publication implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4120678271869648230L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;// 成果编号

  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;// 所有者(对应 person的psn_Id)

  @Column(name = "ARTICLE_TYPE")
  private Integer articleType;// 存储内容类型
  // publication=1,reference=2,file=3,project=4

  @Column(name = "SOURCE_DB_ID")
  private Integer sourceDbId;// 成果对应的外部数据库 refrence to const_ref_db

  @Column(name = "PUB_TYPE")
  private Integer pubType;// 成果类型 const_pub_type

  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 最后编辑时间

  @Column(name = "UPDATE_PSN_ID")
  private Long updatePsnId;// 最后编辑人

  @Column(name = "PUBLISH_YEAR")
  private Integer publishYear;// 出版年份

  @Column(name = "PUBLISH_MONTH")
  private Integer publishMonth;// 出版月份

  @Column(name = "PUBLISH_DAY")
  private Integer publishDay;// 出版日期

  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;// 创建人

  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建日期

  @Column(name = "VERSION_NO")
  private Integer versionNo;// 版本号码

  @Column(name = "REGION_ID")
  private Long regionId;// 国家与地区来源表 const_region

  @Column(name = "IMPACT_FACTORS")
  private String impactFactors;// 文章所属的期刊的影响因子（冗余）

  @Column(name = "JID")
  private Long JID;// 成果所属 期刊ID 期刊文章使用

  @Column(name = "RECORD_FROM")
  private Integer recordFrom;// 数据来源：0: 手工输入，1:数据库导入，2:文件导入
  // 3离线导入,4R确认提交,6基准库导入

  @Column(name = "IS_VALID")
  private Integer isValid;// 0/1数据是否完整;0：数据不完整，1：数据完整

  @Column(name = "CITED_TIMES")
  private Integer citedTimes;// 引用次数

  @Column(name = "ZH_TITLE")
  private String zhTitle;// 中文标题

  @Column(name = "EN_TITLE")
  private String enTitle;// 外文标题

  @Column(name = "ZH_TITLE_HASH")
  private Integer zhTitleHash;// "中文标题hash_code，查重时使用，统一调用PublicationHash.titleCode(title)取得hash_code"

  @Column(name = "EN_TITLE_HASH")
  private Integer enTitlehash;// "英文标题hash_code，查重时使用统一调用PublicationHash.titleCode(title)取得hash_code"

  @Column(name = "FINGER_PRINT")
  private Integer fingerPrint;// "成果指纹，统一调用PublicationHash.finger_print(String[])生成；满足查重时的类型特定要求：专利为：patent_no的hash_code期刊为：volume/issue/start
  // page/end
  // page四个字段用‘|’号作为分隔符，拼接成长串生成的hash_code"

  @Column(name = "CITED_DATE")
  private Date citedDate;// 最后更新引用次数的时间

  @Column(name = "AUTHOR_NAMES")
  private String authorNames;// 作者

  @Column(name = "BRIEF_DESC")
  private String briefDesc;// 来源

  @Column(name = "FULLTEXT_FILEID")
  private String fullTextField;// 全文，fulltext_fileid为空，则取fulltext_url

  @Column(name = "FULLTEXT_URL")
  private String fullTextUrl;// 全文

  @Column(name = "STATUS")
  private Integer status;// 1: 已删除，0: 未删除 2:未确认(成果确认) 3：他人推荐（共享、推荐）4:代检索成果
  // (仅在Scholar/SNS使用) 5:从群组跳转而导入录入的成果.

  @Column(name = "CITED_LIST")
  private String citedList;// 引用情况，用逗号分隔(如：SCI,EI)

  @Column(name = "PUB_START")
  private Integer pubStart;// 总评分

  @Column(name = "PUB_REVIEWS")
  private Integer pubReviews;// 评价人数

  @Column(name = "PUB_START_PSNS")
  private Integer pubStartPsns;// 评分总人数.

  @Column(name = "CITED_URL")
  private String citedUrl;// 引用URL

  @Column(name = "ISBN")
  private String ISBN;// isbn

  @Column(name = "VOLUME")
  private String volume;// volume of the journal or book, i.e. 13

  @Column(name = "ISSUE")
  private String ISSUE;// issue no. of journal or book

  @Column(name = "DOI")
  private String DOI;// "digital object identifier 10.1109/mmb.2000.893740"

  @Column(name = "FROM_PUB_ID")
  private Long fromPubId;// V2.6导入的成果ID

  @Column(name = "FULLTEXT_NODEID")
  private Integer fullTextNodeId;// 全文附件节点ID

  @Column(name = "START_PAGE")
  private String startPage;

  @Column(name = "END_PAGE")
  private String endPage;

  @Column(name = "ISI_ID")
  private String isiId;// isi的唯一标识，对应2.6的source_id

  @Column(name = "IS_UPDATE_SOURCE")
  private Integer isUpdateSource;// 0:EI,SCI,ISTP,SSCI导入的成果。1:标题，期刊，收录，引用等四项修改后

  @Column(name = "FULLTEXT_FILEEXT")
  private String fullTextFileExt;// 全文附件后缀

  @Column(name = "IMPACT_FACTORS_SORT")
  private Long impactFactorSort;// 文章所属的期刊的影响因子（排序字段 冗余）

  @Column(name = "ARTICLE_NO")
  private String articleNo;// article_number

  @Column(name = "BRIEF_DESC_EN")
  private String briefDescEn;// 来源

  @Column(name = "UPDATE_MARK")
  private Integer updateMark;// 成果添加方式 1=在线导入未修改；2=在线导入 已修改；3=手工导入

  /*
   * 以下Transient
   */
  @Transient
  // 匹配的基础期刊Id
  private Long jnlId;

  @Transient
  private String des3ResRecId;

  @Transient
  private Integer dbid;

  @Transient
  private String typeName;// 类别名称

  @Transient
  private Integer nodeId;

  @Transient
  private String listInfo;// 收录情况

  @Transient
  private String htmlAbstract;

  @Transient
  private Set<ErrorField> errorFields;// 完整性检查字段.

  @Transient
  private String des3Id;

  @Transient
  private Long groupId;// 群组Id

  @Transient
  private String fullTextImagePath; // 成果全文图片路径_Zk

  @Transient
  private boolean isMine;

  // pdwhxml 导入重复.
  @Transient
  private Integer isInsert;
  /** 全文下载权限. */
  @Transient
  private int permission;

  public int getPermission() {
    return permission;
  }

  public void setPermission(int permission) {
    this.permission = permission;
  }

  public Publication() {
    super();
  }

  public Integer getUpdateMark() {
    return updateMark;
  }

  public void setUpdateMark(Integer updateMark) {
    this.updateMark = updateMark;
  }

  public Publication(Integer citedTimes, Long pubId, Integer zhTitleHash) {
    super();
    this.pubId = pubId;
    this.citedTimes = citedTimes;
    this.zhTitleHash = zhTitleHash;
  }

  public Publication(Long pubId, String zhTitle, String enTitle, String authorNames, String briefDesc,
      String briefDescEn) {
    super();
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
  }

  public Publication(Long pubId, String zhTitle, String enTitle, String authorNames, String briefDesc,
      String briefDescEn, Integer publishYear, Long ownerPsnId) {
    super();
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.publishYear = publishYear;
    this.ownerPsnId = ownerPsnId;
  }

  public Publication(Long pubId, String zhTitle, String enTitle, String authorNames, String briefDesc,
      String briefDescEn, Integer publishYear, String fullTextField) {
    super();
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.publishYear = publishYear;
    this.fullTextField = fullTextField;
  }

  public Publication(Long pubId, Long ownerPsnId, Integer status) {
    this.pubId = pubId;
    this.ownerPsnId = ownerPsnId;
    this.status = status;
  }

  public Publication(Integer pubStart, Long pubId, Integer pubStartPsns, Integer pubReviews) {
    this.pubStart = pubStart;
    this.pubId = pubId;
    this.pubStartPsns = pubStartPsns;
    this.pubReviews = pubReviews;
  }

  public Publication(Long pubId, String isiId) {
    super();
    this.pubId = pubId;
    this.isiId = isiId;
  }

  public Publication(Long pubId, Integer publishYear) {
    this.pubId = pubId;
    this.publishYear = publishYear;
  }

  public Publication(Long pubId, Long ownerPsnId, Integer articleType, Integer status) {
    this.pubId = pubId;
    this.ownerPsnId = ownerPsnId;
    this.articleType = articleType;
    this.status = status;
  }

  public Publication(Long ownerPsnId, Integer articleType, Integer zhTitleHash, Integer enTitlehash) {
    super();
    this.ownerPsnId = ownerPsnId;
    this.articleType = articleType;
    this.zhTitleHash = zhTitleHash;
    this.enTitlehash = enTitlehash;
  }

  public Publication(Long pubId, Integer pubType, Integer publishYear, String impactFactors, Long JID) {
    this.pubId = pubId;
    this.pubType = pubType;
    this.publishYear = publishYear;
    this.impactFactors = impactFactors;
    this.JID = JID;
  }

  public Publication(Long pubId, Integer pubType, Integer publishYear, Long createPsnId, Integer isValid) {
    this.pubId = pubId;
    this.pubType = pubType;
    this.publishYear = publishYear;
    this.createPsnId = createPsnId;
    this.isValid = isValid;
  }

  /**
   * 包含成果标题及相关显示信息的控制器_MJG_2013-06-07_SCM-2527.
   * 
   * @param pubId
   * @param ownerPsnId
   * @param impactFactors
   * @param zhTitle
   * @param enTitle
   * @param articleType
   */
  public Publication(Long pubId, Long ownerPsnId, String impactFactors, String zhTitle, String enTitle,
      Integer articleType) {
    super();
    this.pubId = pubId;
    this.ownerPsnId = ownerPsnId;
    this.impactFactors = impactFactors;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.articleType = articleType;
  }

  public Publication(Long pubId, Integer pubType, Integer publishYear) {
    super();
    this.pubId = pubId;
    this.pubType = pubType;
    this.publishYear = publishYear;
  }

  /**
   * 包含的成果的类型，有评论统计.
   * 
   * @param pubId
   * @param pubType
   * @param publishYear
   * @param impactFactors
   * @param jid
   * @param publishMonth
   * @param authorNames
   * @param pubStart
   * @param briefDesc
   * @param fulltextFileid
   * @param fulltextUrl
   * @param pubReviews
   * @param citedList
   * @param zhTitle
   * @param enTitle
   * @param articleType
   */
  public Publication(Long pubId, Integer pubType, Integer publishYear, String impactFactors, Long JID,
      Integer publishMonth, String authorNames, Integer pubStart, Integer pubStartPsns, String briefDesc,
      String fullTextField, String fullTextUrl, Integer pubReviews, String citedList, String zhTitle, String enTitle,
      Integer articleType, String citedUrl) {
    this.pubId = pubId;
    this.pubType = pubType;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.impactFactors = impactFactors;
    this.JID = JID;
    this.authorNames = authorNames;
    this.pubStart = pubStart;
    this.pubStartPsns = pubStartPsns;
    this.briefDesc = briefDesc;
    this.fullTextField = fullTextField;
    this.fullTextUrl = fullTextUrl;
    this.pubReviews = pubReviews;
    this.citedList = citedList;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.articleType = articleType;
    this.citedUrl = citedUrl;
  }

  /**
   * 包含成果类型，成果所有者
   * 
   * @param pubId
   * @param ownerPsnId
   * @param pubType
   * @param publishYear
   * @param impactFactors
   * @param jid
   * @param publishMonth
   * @param authorNames
   * @param pubStart
   * @param pubStartPsns
   * @param briefDesc
   * @param fulltextFileid
   * @param fulltextUrl
   * @param pubReviews
   * @param citedList
   * @param zhTitle
   * @param enTitle
   * @param articleType
   * @param citedUrl
   */
  public Publication(Long pubId, Long ownerPsnId, Integer pubType, Integer publishYear, String impactFactors, Long JID,
      Integer publishMonth, String authorNames, Integer pubStart, Integer pubStartPsns, String briefDesc,
      String fullTextField, String fullTextUrl, Integer pubReviews, String citedList, String zhTitle, String enTitle,
      Integer articleType, String citedUrl) {
    this.pubId = pubId;
    this.ownerPsnId = ownerPsnId;
    this.pubType = pubType;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.impactFactors = impactFactors;
    this.JID = JID;
    this.authorNames = authorNames;
    this.pubStart = pubStart;
    this.pubStartPsns = pubStartPsns;
    this.briefDesc = briefDesc;
    this.fullTextField = fullTextField;
    this.fullTextUrl = fullTextUrl;
    this.pubReviews = pubReviews;
    this.citedList = citedList;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.articleType = articleType;
    this.citedUrl = citedUrl;
  }

  /**
   * 不需要评论，不需要类型不需要引用情况.
   * 
   * @param pubId
   * @param pubType
   * @param publishYear
   * @param impactFactors
   * @param jid
   * @param publishMonth
   * @param authorNames
   * @param pubStart
   * @param briefDesc
   * @param fulltextFileid
   * @param fulltextUrl
   * @param pubReviews
   * @param citedList
   * @param zhTitle
   * @param enTitle
   * @param articleType
   */
  public Publication(Long pubId, Integer pubType, Integer publishYear, String impactFactors, Long JID,
      Integer publishMonth, String authorNames, String briefDesc, String fullTextField, String fullTextUrl,
      String zhTitle, String enTitle, Integer articleType, Integer publishDay) {
    this.pubId = pubId;
    this.pubType = pubType;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.impactFactors = impactFactors;
    this.JID = JID;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.fullTextField = fullTextField;
    this.fullTextUrl = fullTextUrl;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.articleType = articleType;
    this.publishDay = publishDay;
  }

  /**
   * 需要引用次数的情况
   * 
   * @param pubId
   * @param pubType
   * @param publishYear
   * @param impactFactors
   * @param jid
   * @param citedTimes
   * @param publishMonth
   * @param authorNames
   * @param pubStart
   * @param pubStartPsns
   * @param briefDesc
   * @param fulltextFileid
   * @param fulltextUrl
   * @param pubReviews
   * @param citedList
   * @param zhTitle
   * @param enTitle
   * @param articleType
   * @param citedUrl
   */
  public Publication(Long pubId, Integer pubType, Integer publishYear, String impactFactors, Long JID,
      Integer citedTimes, Integer publishMonth, String authorNames, Integer pubStart, Integer pubStartPsns,
      String briefDesc, String fullTextField, String fullTextUrl, Integer pubReviews, String citedList, String zhTitle,
      String enTitle, Integer articleType, String citedUrl) {
    this.pubId = pubId;
    this.pubType = pubType;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.impactFactors = impactFactors;
    this.JID = JID;
    this.citedTimes = citedTimes;
    this.authorNames = authorNames;
    this.pubStart = pubStart;
    this.pubStartPsns = pubStartPsns;
    this.briefDesc = briefDesc;
    this.fullTextField = fullTextField;
    this.fullTextUrl = fullTextUrl;
    this.pubReviews = pubReviews;
    this.citedList = citedList;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.articleType = articleType;
    this.citedUrl = citedUrl;
  }

  public Publication(Long pubId, String zhTitle, String enTitle, String authorNames, Integer pubType, String briefDesc,
      String briefDescEn, String citedList, Integer citedTimes, String DOI, Integer publishYear, Integer publishMonth,
      Integer publishDay, Date updateDate) {
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.pubType = pubType;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.citedList = citedList;
    this.citedTimes = citedTimes;
    this.DOI = DOI;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.publishDay = publishDay;
    this.updateDate = updateDate;
  }

  public Publication(Long pubId, String zhTitle, String enTitle, String briefDesc, String briefDescEn,
      String authorNames, String fullTextField, String fullTextUrl) {
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.authorNames = authorNames;
    this.fullTextField = fullTextField;
    this.fullTextUrl = fullTextUrl;
  }

  public Publication(Long pubId, String zhTitle, String enTitle, String briefDesc, String briefDescEn,
      String authorNames, String fullTextField, String fullTextUrl, Integer sourceDbId) {
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.authorNames = authorNames;
    this.fullTextField = fullTextField;
    this.fullTextUrl = fullTextUrl;
    this.sourceDbId = sourceDbId;
  }

  public Publication(Long ownerPsnId, Long pubId, String zhTitle, String enTitle) {
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.ownerPsnId = ownerPsnId;
  }

  public Publication(Long pubId, Long ownerPsnId, String fullTextField, Integer articleType) {
    this.pubId = pubId;
    this.ownerPsnId = ownerPsnId;
    this.fullTextField = fullTextField;
    this.articleType = articleType;
  }

  public Publication(Long pubId, Long ownerPsnId, Integer pubType, Integer publishYear, Integer publishMonth,
      Integer publishDay, String impactFactors, Integer citedTimes, Integer sourceDbId, String DOI, String isiId) {
    super();
    this.pubId = pubId;
    this.ownerPsnId = ownerPsnId;
    this.pubType = pubType;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.publishDay = publishDay;
    this.impactFactors = impactFactors;
    this.citedTimes = citedTimes;
    this.sourceDbId = sourceDbId;
    this.DOI = DOI;
    this.isiId = isiId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public Integer getSourceDbId() {
    return sourceDbId;
  }

  public void setSourceDbId(Integer sourceDbId) {
    this.sourceDbId = sourceDbId;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
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

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getVersionNo() {
    return versionNo;
  }

  public void setVersionNo(Integer versionNo) {
    this.versionNo = versionNo;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  public Long getJID() {
    return JID;
  }

  public void setJID(Long jID) {
    JID = jID;
  }

  public Integer getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }

  public Integer getIsValid() {
    return isValid;
  }

  public void setIsValid(Integer isValid) {
    this.isValid = isValid;
  }

  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
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

  public Integer getEnTitlehash() {
    return enTitlehash;
  }

  public void setEnTitlehash(Integer enTitlehash) {
    this.enTitlehash = enTitlehash;
  }

  public Integer getFingerPrint() {
    return fingerPrint;
  }

  public void setFingerPrint(Integer fingerPrint) {
    this.fingerPrint = fingerPrint;
  }

  public Date getCitedDate() {
    return citedDate;
  }

  public void setCitedDate(Date citedDate) {
    this.citedDate = citedDate;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getFullTextField() {
    return fullTextField;
  }

  public void setFullTextField(String fullTextField) {
    this.fullTextField = fullTextField;
  }

  public String getFullTextUrl() {
    return fullTextUrl;
  }

  public void setFullTextUrl(String fullTextUrl) {
    this.fullTextUrl = fullTextUrl;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getCitedList() {
    return citedList;
  }

  public void setCitedList(String citedList) {
    this.citedList = citedList;
  }

  public Integer getPubStart() {
    return pubStart;
  }

  public void setPubStart(Integer pubStart) {
    this.pubStart = pubStart;
  }

  public Integer getPubReviews() {
    return pubReviews;
  }

  public void setPubReviews(Integer pubReviews) {
    this.pubReviews = pubReviews;
  }

  public Integer getPubStartPsns() {
    return pubStartPsns;
  }

  public void setPubStartPsns(Integer pubStartPsns) {
    this.pubStartPsns = pubStartPsns;
  }

  public String getCitedUrl() {
    return citedUrl;
  }

  public void setCitedUrl(String citedUrl) {
    this.citedUrl = citedUrl;
  }

  public String getISBN() {
    return ISBN;
  }

  public void setISBN(String iSBN) {
    ISBN = iSBN;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getISSUE() {
    return ISSUE;
  }

  public void setISSUE(String iSSUE) {
    ISSUE = iSSUE;
  }

  public String getDOI() {
    return DOI;
  }

  public void setDOI(String dOI) {
    DOI = dOI;
  }

  public Long getFromPubId() {
    return fromPubId;
  }

  public void setFromPubId(Long fromPubId) {
    this.fromPubId = fromPubId;
  }

  public Integer getFullTextNodeId() {
    return fullTextNodeId;
  }

  public void setFullTextNodeId(Integer fullTextNodeId) {
    this.fullTextNodeId = fullTextNodeId;
  }

  public String getStartPage() {
    return startPage;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  public String getEndPage() {
    return endPage;
  }

  public void setEndPage(String endPage) {
    this.endPage = endPage;
  }

  public String getIsiId() {
    return isiId;
  }

  public void setIsiId(String isiId) {
    this.isiId = isiId;
  }

  public Integer getIsUpdateSource() {
    return isUpdateSource;
  }

  public void setIsUpdateSource(Integer isUpdateSource) {
    this.isUpdateSource = isUpdateSource;
  }

  public String getFullTextFileExt() {
    return fullTextFileExt;
  }

  public void setFullTextFileExt(String fullTextFileExt) {
    this.fullTextFileExt = fullTextFileExt;
  }

  public Long getImpactFactorSort() {
    return impactFactorSort;
  }

  public void setImpactFactorSort(Long impactFactorSort) {
    this.impactFactorSort = impactFactorSort;
  }

  public String getArticleNo() {
    return articleNo;
  }

  public void setArticleNo(String articleNo) {
    this.articleNo = articleNo;
  }

  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public String getListInfo() {
    return this.listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  public void setHtmlAbstract(String htmlAbstract) {
    this.htmlAbstract = htmlAbstract;
  }

  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  public Set<ErrorField> getErrorFields() {
    return errorFields;
  }

  public void setErrorFields(Set<ErrorField> errorFields) {
    this.errorFields = errorFields;
  }

  public String getDes3Id() {

    if (this.pubId != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.pubId.toString());
    }
    return des3Id;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getFullTextImagePath() {
    return fullTextImagePath;
  }

  public void setFullTextImagePath(String fullTextImagePath) {
    this.fullTextImagePath = fullTextImagePath;
  }

  public boolean getIsMine() {
    return this.isMine;
  }

  public void setIsMine(boolean isMine) {
    this.isMine = isMine;
  }

  @Transient
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Transient
  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  @Transient
  public String getDes3ResRecId() {
    return des3ResRecId;
  }

  public void setDes3ResRecId(String des3ResRecId) {
    this.des3ResRecId = des3ResRecId;
  }

  @Transient
  public Integer getIsInsert() {
    return isInsert;
  }

  public void setIsInsert(Integer isInsert) {
    this.isInsert = isInsert;
  }
}
