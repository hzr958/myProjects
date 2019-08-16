package com.smate.web.psn.model.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * publication的简化版，在不影响成果功能的情况下,加快成果的显示与录入等操作的速度.具体使用在成果列表,后台任务等。
 * 
 * 
 */
@Entity
@Table(name = "V_PUB_SIMPLE")
public class PubSimple implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2524089531673545109L;

  @Id
  @Column(name = "PUB_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUBLICATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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

  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间

  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 最后编辑时间

  @Column(name = "PUBLISH_YEAR")
  private Integer publishYear;// 出版年份

  @Column(name = "PUBLISH_MONTH")
  private Integer publishMonth;// 出版月份

  @Column(name = "PUBLISH_DAY")
  private Integer publishDay;// 出版日期

  @Column(name = "IMPACT_FACTORS")
  private String impactFactors;// 文章所属的期刊的影响因子（冗余）

  @Column(name = "CITED_TIMES")
  private Integer citedTimes;// 引用次数

  @Column(name = "ZH_TITLE")
  private String zhTitle;// 中文标题

  @Column(name = "EN_TITLE")
  private String enTitle;// 外文标题

  @Column(name = "AUTHOR_NAMES")
  private String authorNames;// 作者

  @Column(name = "BRIEF_DESC")
  private String briefDesc;// 来源

  @Column(name = "FULLTEXT_FILEID")
  private String fullTextField;// 全文，fulltext_fileid为空，则取fulltext_url

  @Column(name = "STATUS")
  private Integer status;// 1: 已删除，0: 未删除 2:未确认(成果确认) 3：他人推荐（共享、推荐）4:代检索成果
                         // (仅在Scholar/SNS使用) 5:从群组跳转而导入录入的成果.

  @Column(name = "FULLTEXT_NODEID")
  private Integer fullTextNodeId;// 全文附件节点ID

  @Column(name = "FULLTEXT_FILEEXT")
  private String fullTextFileExt;// 全文附件后缀

  @Column(name = "IMPACT_FACTORS_SORT")
  private Long impactFactorSort;// 文章所属的期刊的影响因子（排序字段 冗余）

  @Column(name = "BRIEF_DESC_EN")
  private String briefDescEn;// 来源

  @Column(name = "SIMPLE_STATUS")
  private Long simpleStatus;// 简单表数据状态 0:待处理
                            // 1,T表任务成功执行(v_pub_data_store对应记录被删除) 2,被删除
                            // 99:处理失败

  @Column(name = "SIMPLE_VERSION")
  private Long simpleVersion;// 任务使用，保证数据的正确性

  @Column(name = "SIMPLE_TASK")
  private Integer simpleTask = 0;// 标识是否有跑过成果处理任务 0,未跑过 1,跑过

  @Column(name = "CITE_DATE")
  private Date citeDate; // 更新引用的时间

  @Column(name = "UPDATE_MARK")
  private Integer updateMark;// 成果添加方式 1=在线导入未修改；2=在线导入 已修改；3=手工导入

  // @OneToOne(cascade = CascadeType.ALL)
  // @PrimaryKeyJoinColumn
  // private PubStatistics pubStatistics;
  //
  // @Transient
  // private PubDataStore pubDataStore;

  public Integer getUpdateMark() {
    return updateMark;
  }

  public void setUpdateMark(Integer updateMark) {
    this.updateMark = updateMark;
  }

  @Transient
  private PubXmlDocument doc;

  @Transient
  private int permission;

  @Transient
  private String patentStatus;// 专利类型（授权或申请）

  @Transient
  private String confType;// 会议论文类型（特邀报告、墙报展示、）

  @Transient
  private String awardAuthorList; // 奖励类型成果获奖人员

  @Transient
  private String source;

  @Transient
  private String authorFlag;

  @Transient
  private String authors; // 构造后的作者

  @Transient
  private Integer isRepresentPub = 0; // 是否是代表性成果 1：是， 0：不是

  public PubSimple() {
    super();
  }

  public PubSimple(Long pubId, String zhTitle, String enTitle, String authorNames, String briefDesc) {
    super();
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
  }

  public PubSimple(Long pubId) {
    super();
    this.pubId = pubId;
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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getFullTextNodeId() {
    return fullTextNodeId;
  }

  public void setFullTextNodeId(Integer fullTextNodeId) {
    this.fullTextNodeId = fullTextNodeId;
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

  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }

  public Long getSimpleStatus() {
    return simpleStatus;
  }

  public void setSimpleStatus(Long simpleStatus) {
    this.simpleStatus = simpleStatus;
  }

  public Long getSimpleVersion() {
    return simpleVersion;
  }

  public void setSimpleVersion(Long simpleVersion) {
    this.simpleVersion = simpleVersion;
  }

  // public PubDataStore getPubDataStore() {
  // return pubDataStore;
  // }
  //
  // public void setPubDataStore(PubDataStore pubDataStore) {
  // this.pubDataStore = pubDataStore;
  // }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public int getPermission() {
    return permission;
  }

  public void setPermission(int permission) {
    this.permission = permission;
  }

  public PubXmlDocument getDoc() {
    return doc;
  }

  public void setDoc(PubXmlDocument doc) {
    this.doc = doc;
  }

  public Integer getSimpleTask() {
    return simpleTask;
  }

  public void setSimpleTask(Integer simpleTask) {
    this.simpleTask = simpleTask;
  }

  public String getPatentStatus() {
    return patentStatus;
  }

  public void setPatentStatus(String patentStatus) {
    this.patentStatus = patentStatus;
  }

  public String getConfType() {
    return confType;
  }

  public void setConfType(String confType) {
    this.confType = confType;
  }

  public String getAwardAuthorList() {
    return awardAuthorList;
  }

  public void setAwardAuthorList(String awardAuthorList) {
    this.awardAuthorList = awardAuthorList;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getAuthorFlag() {
    return authorFlag;
  }

  public void setAuthorFlag(String authorFlag) {
    this.authorFlag = authorFlag;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public Integer getIsRepresentPub() {
    return isRepresentPub;
  }

  public void setIsRepresentPub(Integer isRepresentPub) {
    this.isRepresentPub = isRepresentPub;
  }

  public Date getCiteDate() {
    return citeDate;
  }

  public void setCiteDate(Date citeDate) {
    this.citeDate = citeDate;
  }

  // public PubStatistics getPubStatistics() {
  // return pubStatistics;
  // }
  //
  // public void setPubStatistics(PubStatistics pubStatistics) {
  // this.pubStatistics = pubStatistics;
  // }
}
