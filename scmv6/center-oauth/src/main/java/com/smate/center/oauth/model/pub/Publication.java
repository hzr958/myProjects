package com.smate.center.oauth.model.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果、文献修改实体.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUBLICATION")
public class Publication implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7367083007789922705L;

  // 成果编号
  private Long id;
  // 所有人ID
  private Long psnId;
  // 成果类型 const_pub_type
  private Integer typeId;
  // 最后编辑人
  private Long updatePsnId;
  // 最后更新时间
  private Date updateDate;
  // 出版年份
  private Integer publishYear;
  // 出版月份
  private Integer publishMonth;
  // 出版日期
  private Integer publishDay;
  // 版本号码
  private Integer versionNo;
  // 国家与地区来源表 const_region
  private Integer regionId;
  // 文章所属的期刊的影响因子（冗余）
  private String impactFactors;
  // 影响因子排序字段
  private Double impactFactorsSort;
  // 成果所属 期刊id
  private Long jid;
  // 匹配的基础期刊Id
  private Long jnlId;
  // 0/1数据是否完整
  private Integer dataValidate;
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
  // 成果收藏夹
  // private List<Folder> pubFolders = new ArrayList<Folder>();
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
  // 完整性检查字段.
  private Set<ErrorField> errorFields;
  // 作者
  private String authorNames;
  // 来源
  private String briefDesc;
  // 来源
  private String briefDescEn;
  // 全文，fulltext_fileid为空，则取fulltext_url
  private String fulltextFileid;
  // 全文附件节点ID
  private Integer fulltextNodeId;
  // 全文附件后缀
  private String fulltextExt;
  // 全文
  private String fulltextUrl;
  // 1: 已删除，0: 未删除 2:未确认(成果确认) 3：他人推荐（共享、推荐）4:代检索成果 (仅在Scholar/SNS使用)
  private Integer status;
  // 引用情况，用逗号分隔(如：SCI,EI)
  private String citedList;
  // 总评分
  private Integer pubStart;
  // 评分总人数
  private Integer pubStartPsns;
  // 评价人数
  private Integer pubReviews;
  // 收录情况
  private String listInfo;
  // 引用URL
  private String citedUrl;
  // 收藏夹，仅用于删除、查询操作，关系维护由PubFolder管理
  private List<PubFolderItems> pubFolderItems = new ArrayList<PubFolderItems>();
  // 创建时间
  private Date createDate;
  // 检索来源数据的完整性默认为0，如果标题，期刊，收录，引用等四项修改了则为1
  private Integer isUpdateSource;

  private Integer nodeId;

  // 文章号
  private String articleNo;
  // 阅读数
  private Integer readCount;
  // pdwhxml 导入重复.
  private Integer isInsert;
  /**
   * 加密ID.
   */
  private String des3Id;

  private String startPage;
  private String endPage;
  private String isbn;
  private String volume;
  private String issue;
  private String doi;
  private Long oldPubId;
  // isi唯一标识，对应V2.6source_id.
  private String isiId;
  private int averageStar;
  private boolean isAward;
  private Long awardIds;
  private Long shareTimes;
  private boolean isMine;

  /** 推荐读者总数. */
  private Integer pubReaderCmdCount;
  /** 成果发送推广邮件总数. */
  private Long pubCmdCount;
  // 群组ID
  private Long groupId;
  private String des3ResRecId;
  // 成果分享人名
  private String pubSharePsnName;
  private Integer dbid;
  private Long dupPubId;
  private int pubIsShowComment = 0;// "评论"是否要显示链接
  private String issn;
  private String confName;
  /** 全文下载权限. */
  private int permission;

  private String fullTextImagePath; // 成果全文图片路径_Zk

  public Publication() {
    super();
  }

  public Publication(Long id) {
    super();
    this.id = id;
  }

  public Publication(Long id, String isiId) {
    super();
    this.id = id;
    this.isiId = isiId;
  }

  public Publication(Long id, Integer publishYear) {
    this.id = id;
    this.publishYear = publishYear;
  }

  public Publication(Long id, Long psnId, Integer status) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.status = status;
  }

  public Publication(Long id, Long psnId, Integer articleType, Integer status) {
    this.id = id;
    this.psnId = psnId;
    this.articleType = articleType;
    this.status = status;
  }

  public Publication(Long psnId, Integer articleType, Integer zhTitleHash, Integer enTitleHash) {
    super();
    this.psnId = psnId;
    this.articleType = articleType;
    this.zhTitleHash = zhTitleHash;
    this.enTitleHash = enTitleHash;
  }

  public Publication(Long id, Integer typeId, Integer publishYear, String impactFactors, Long jid) {
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.impactFactors = impactFactors;
    this.jid = jid;
  }

  public Publication(Long id, Integer typeId, Integer publishYear, Long createPsnId, Integer dataValidate) {
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.createPsnId = createPsnId;
    this.dataValidate = dataValidate;
  }

  /**
   * 包含成果标题及相关显示信息的控制器_MJG_2013-06-07_SCM-2527.
   * 
   * @param id
   * @param psnId
   * @param impactFactors
   * @param zhTitle
   * @param enTitle
   * @param articleType
   */
  public Publication(Long id, Long psnId, String impactFactors, String zhTitle, String enTitle, Integer articleType) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.impactFactors = impactFactors;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.articleType = articleType;
  }

  public Publication(Long id, Integer typeId, Integer publishYear) {
    super();
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
  }

  /**
   * 包含的成果的类型，有评论统计.
   * 
   * @param id
   * @param typeId
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
  public Publication(Long id, Integer typeId, Integer publishYear, String impactFactors, Long jid, Integer publishMonth,
      String authorNames, Integer pubStart, Integer pubStartPsns, String briefDesc, String fulltextFileid,
      String fulltextUrl, Integer pubReviews, String citedList, String zhTitle, String enTitle, Integer articleType,
      String citedUrl) {
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.impactFactors = impactFactors;
    this.jid = jid;
    this.authorNames = authorNames;
    this.pubStart = pubStart;
    this.pubStartPsns = pubStartPsns;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
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
   * @param id
   * @param psnId
   * @param typeId
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
  public Publication(Long id, Long psnId, Integer typeId, Integer publishYear, String impactFactors, Long jid,
      Integer publishMonth, String authorNames, Integer pubStart, Integer pubStartPsns, String briefDesc,
      String fulltextFileid, String fulltextUrl, Integer pubReviews, String citedList, String zhTitle, String enTitle,
      Integer articleType, String citedUrl) {
    this.id = id;
    this.psnId = psnId;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.impactFactors = impactFactors;
    this.jid = jid;
    this.authorNames = authorNames;
    this.pubStart = pubStart;
    this.pubStartPsns = pubStartPsns;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
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
   * @param id
   * @param typeId
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
  public Publication(Long id, Integer typeId, Integer publishYear, String impactFactors, Long jid, Integer publishMonth,
      String authorNames, String briefDesc, String fulltextFileid, String fulltextUrl, String zhTitle, String enTitle,
      Integer articleType, Integer publishDay) {
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.impactFactors = impactFactors;
    this.jid = jid;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.articleType = articleType;
    this.publishDay = publishDay;
  }

  /**
   * 需要引用次数的情况
   * 
   * @param id
   * @param typeId
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
  public Publication(Long id, Integer typeId, Integer publishYear, String impactFactors, Long jid, Integer citedTimes,
      Integer publishMonth, String authorNames, Integer pubStart, Integer pubStartPsns, String briefDesc,
      String fulltextFileid, String fulltextUrl, Integer pubReviews, String citedList, String zhTitle, String enTitle,
      Integer articleType, String citedUrl) {
    this.id = id;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.impactFactors = impactFactors;
    this.jid = jid;
    this.citedTimes = citedTimes;
    this.authorNames = authorNames;
    this.pubStart = pubStart;
    this.pubStartPsns = pubStartPsns;
    this.briefDesc = briefDesc;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
    this.pubReviews = pubReviews;
    this.citedList = citedList;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.articleType = articleType;
    this.citedUrl = citedUrl;
  }

  public Publication(Long id, String zhTitle, String enTitle, String authorNames, Integer typeId, String briefDesc,
      String briefDescEn, String citedList, Integer citedTimes, String doi, Integer publishYear, Integer publishMonth,
      Integer publishDay, Date updateDate) {
    this.id = id;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.typeId = typeId;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.citedList = citedList;
    this.citedTimes = citedTimes;
    this.doi = doi;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.publishDay = publishDay;
    this.updateDate = updateDate;
  }

  public Publication(Long id, String zhTitle, String enTitle, String briefDesc, String briefDescEn, String authorNames,
      String fulltextFileid, String fulltextUrl) {
    this.id = id;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.authorNames = authorNames;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
  }

  public Publication(Long id, String zhTitle, String enTitle, String briefDesc, String briefDescEn, String authorNames,
      String fulltextFileid, String fulltextUrl, Integer sourceDbId) {
    this.id = id;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.authorNames = authorNames;
    this.fulltextFileid = fulltextFileid;
    this.fulltextUrl = fulltextUrl;
    this.sourceDbId = sourceDbId;
  }

  public Publication(Long psnId, Long id, String zhTitle, String enTitle) {
    this.id = id;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.psnId = psnId;
  }

  public Publication(Integer citedTimes, Long id, Integer zhTitleHash) {
    this.citedTimes = citedTimes;
    this.id = id;
    this.zhTitleHash = zhTitleHash;
  }

  public Publication(Long id, Long psnId, String fulltextFileid, Integer articleType) {
    this.id = id;
    this.psnId = psnId;
    this.fulltextFileid = fulltextFileid;
    this.articleType = articleType;
  }

  public Publication(Long id, Long psnId, Integer typeId, Integer publishYear, Integer publishMonth, Integer publishDay,
      String impactFactors, Integer citedTimes, Integer sourceDbId, String doi, String isiId) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.typeId = typeId;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.publishDay = publishDay;
    this.impactFactors = impactFactors;
    this.citedTimes = citedTimes;
    this.sourceDbId = sourceDbId;
    this.doi = doi;
    this.isiId = isiId;
  }

  @Id
  @Column(name = "PUB_ID")
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
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

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "PUB_TYPE")
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  @Column(name = "UPDATE_PSN_ID")
  public Long getUpdatePsnId() {
    return updatePsnId;
  }

  public void setUpdatePsnId(Long updatePsnId) {
    this.updatePsnId = updatePsnId;
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

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "PUBLISH_DAY")
  public Integer getPublishDay() {
    return publishDay;
  }

  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
  }

  @Column(name = "VERSION_NO")
  public Integer getVersionNo() {
    return versionNo;
  }

  public void setVersionNo(Integer versionNo) {
    this.versionNo = versionNo;
  }

  @Column(name = "REGION_ID")
  public Integer getRegionId() {
    return regionId;
  }

  public void setRegionId(Integer regionId) {
    this.regionId = regionId;
  }

  @Column(name = "IMPACT_FACTORS")
  public String getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(String impactFactors) {
    if (StringUtils.isBlank(impactFactors))
      this.impactFactorsSort = null;
    this.impactFactors = impactFactors;
  }

  @Column(name = "IMPACT_FACTORS_SORT")
  public Double getImpactFactorsSort() {
    String factors = "";
    if (StringUtils.isNotBlank(StringUtils.trimToEmpty(impactFactors))) {
      factors = StringUtils.substring(impactFactors, 0, impactFactors.indexOf("("));
      if (StringUtils.isNotBlank(factors))
        impactFactorsSort = Double.parseDouble(factors);
    } else {
      impactFactorsSort = null;
    }
    return impactFactorsSort;
  }

  public void setImpactFactorsSort(Double impactFactorsSort) {
    this.impactFactorsSort = impactFactorsSort;
  }

  @Column(name = "JID")
  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  @Column(name = "IS_VALID")
  public Integer getDataValidate() {
    return dataValidate;
  }

  public void setDataValidate(Integer dataValidate) {
    this.dataValidate = dataValidate;
  }

  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = IrisStringUtils.filterSupplementaryChars(zhTitle);
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = IrisStringUtils.filterSupplementaryChars(enTitle);
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

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
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

  @Transient
  public Set<ErrorField> getErrorFields() {
    return errorFields;
  }

  public void setErrorFields(Set<ErrorField> errorFields) {
    this.errorFields = errorFields;
  }

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorNames() {
    return authorNames;
  }

  @Column(name = "CITED_LIST")
  public String getCitedList() {
    return citedList;
  }

  @Column(name = "PUB_START")
  public Integer getPubStart() {
    return pubStart;
  }

  @Column(name = "PUB_REVIEWS")
  public Integer getPubReviews() {
    return pubReviews;
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
    this.briefDescEn = IrisStringUtils.filterSupplementaryChars(briefDescEn);
  }

  @Column(name = "PUB_START_PSNS")
  public Integer getPubStartPsns() {
    return pubStartPsns;
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

  @Column(name = "ISI_ID")
  public String getIsiId() {
    return isiId;
  }

  @Column(name = "IS_UPDATE_SOURCE")
  public Integer getIsUpdateSource() {
    return isUpdateSource;
  }

  @Column(name = "FULLTEXT_FILEEXT")
  public String getFulltextExt() {
    return fulltextExt;
  }

  @Column(name = "ARTICLE_NO")
  public String getArticleNo() {
    return articleNo;
  }

  public void setArticleNo(String articleNo) {
    this.articleNo = articleNo;
  }

  public void setFulltextExt(String fulltextExt) {
    this.fulltextExt = fulltextExt;
  }

  public void setIsUpdateSource(Integer isUpdateSource) {
    this.isUpdateSource = isUpdateSource;
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

  public void setOldPubId(Long oldPubId) {
    this.oldPubId = oldPubId;
  }

  public void setPubStartPsns(Integer pubStartPsns) {
    this.pubStartPsns = pubStartPsns;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = IrisStringUtils.filterSupplementaryChars(briefDesc);
  }

  public void setFulltextFileid(String fulltextFileid) {
    this.fulltextFileid = fulltextFileid;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = IrisStringUtils.filterSupplementaryChars(authorNames);
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCitedList(String citedList) {
    this.citedList = citedList;
  }

  public void setPubStart(Integer pubStart) {
    this.pubStart = pubStart;
  }

  public void setPubReviews(Integer pubReviews) {
    this.pubReviews = pubReviews;
  }

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "publication")
  public List<PubFolderItems> getPubFolderItems() {
    return pubFolderItems;
  }

  public void setPubFolderItems(List<PubFolderItems> pubFolderItems) {
    this.pubFolderItems = pubFolderItems;
  }



  @Transient
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Transient
  public String getDes3Id() {

    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  @Transient
  public int getAverageStar() {
    if (pubStartPsns != 0) {
      averageStar = pubStart / pubStartPsns;
      int remainder = pubStart % pubStartPsns;
      if (remainder != 0)
        averageStar = averageStar + 1;
    } else
      averageStar = 0;
    return averageStar;
  }

  @Transient
  public boolean getIsAward() {
    return isAward;
  }

  public void setIsAward(boolean isAward) {
    this.isAward = isAward;
  }

  @Transient
  public Long getAwardIds() {
    return awardIds;
  }

  public void setAwardIds(Long awardIds) {
    this.awardIds = awardIds;
  }

  @Transient
  public Long getShareTimes() {
    return shareTimes;
  }

  public void setShareTimes(Long shareTimes) {
    this.shareTimes = shareTimes;
  }

  @Transient
  public boolean getIsMine() {
    return this.isMine;
  }

  public void setIsMine(boolean isMine) {
    this.isMine = isMine;
  }

  @Transient
  public Integer getPubReaderCmdCount() {
    return pubReaderCmdCount;
  }

  public void setPubReaderCmdCount(Integer pubReaderCmdCount) {
    this.pubReaderCmdCount = pubReaderCmdCount;
  }

  @Transient
  public Long getPubCmdCount() {
    return pubCmdCount;
  }

  public void setPubCmdCount(Long pubCmdCount) {
    this.pubCmdCount = pubCmdCount;
  }

  @Transient
  public Integer getReadCount() {
    return readCount;
  }

  public void setReadCount(Integer readCount) {
    this.readCount = readCount;
  }

  @Transient
  public Integer getIsInsert() {
    return isInsert;
  }

  public void setIsInsert(Integer isInsert) {
    this.isInsert = isInsert;
  }

  @Transient
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Transient
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Transient
  public String getDes3ResRecId() {
    return des3ResRecId;
  }

  public void setDes3ResRecId(String des3ResRecId) {
    this.des3ResRecId = des3ResRecId;
  }

  @Transient
  public String getPubSharePsnName() {
    return pubSharePsnName;
  }

  public void setPubSharePsnName(String pubSharePsnName) {
    this.pubSharePsnName = pubSharePsnName;
  }

  @Transient
  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  @Transient
  public Long getDupPubId() {
    return dupPubId;
  }

  public void setDupPubId(Long dupPubId) {
    this.dupPubId = dupPubId;
  }

  @Transient
  public int getPubIsShowComment() {
    return pubIsShowComment;
  }

  public void setPubIsShowComment(int pubIsShowComment) {
    this.pubIsShowComment = pubIsShowComment;
  }

  @Transient
  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  @Transient
  public String getConfName() {
    return confName;
  }

  public void setConfName(String confName) {
    this.confName = confName;
  }

  @Transient
  public int getPermission() {
    return permission;
  }

  public void setPermission(int permission) {
    this.permission = permission;
  }

  @Transient
  public String getFullTextImagePath() {
    return fullTextImagePath;
  }

  public void setFullTextImagePath(String fullTextImagePath) {
    this.fullTextImagePath = fullTextImagePath;
  }

}
