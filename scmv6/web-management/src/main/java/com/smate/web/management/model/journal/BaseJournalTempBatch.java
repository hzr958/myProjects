package com.smate.web.management.model.journal;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli期刊批量导入临时表.
 */
@Entity
@Table(name = "BASE_JOURNAL_TEMP_BATCH")
public class BaseJournalTempBatch implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3308336164450763930L;
  // 主键
  private Long tempBatchId;
  // 文献库代码
  private Long dbId;
  // 英文刊名,被bpo修改字段
  private String titleEn;
  // 原始刊名,被bpo修改字段
  private String titleXx;
  // 刊名缩写
  private String titleAbbr;
  // Print ISSN,被bpo修改字段
  private String pissn;
  // Electric ISSN
  private String eissn;
  // 国内统一刊号
  private String cn;
  // 出版语言
  private String language;
  // 创刊年
  private String startYear;
  // 停刊年
  private String endYear;
  // 是否OA期刊 1:OA，0:未OA
  private String oaStatus;
  // 是否停刊 1:停刊，0:未停
  private String activeStatus;
  // 期刊主页
  private String journalUrl;
  // 期刊描述(英文)
  private String descriptionEn;
  // 期刊描述(中文或者其它语言)
  private String descriptionXx;
  // 期刊描述(英文)字符串型！
  private String descriptionEns;
  // 期刊描述(中文或者其它语言)字符串型！
  private String descriptionXxs;
  // 期刊影响因子
  private String jouIf;
  // 期刊评价类型
  private String ifType;
  // 期刊评估年份
  private String ifYear;
  // 期刊关键词,中文或者其它语言 如：dan/psn
  private String keyWordXx;
  // 期刊英文关键词。如：dan/psn
  private String keyWordEn;
  // 期刊的处理状态。0：待处理/2：待审核/3：已拒绝/4：已通过
  private Long status;
  // 期刊的处理方式。1：保留原样，2：新增期刊，3：更新至选中期刊
  private Long handleMethod;
  // 更新期刊表的年份,如：2008
  private String updateYear;
  // 关联基础base_journal表JNL_ID
  private Long jnlId;
  // 发行周期
  private String frequencyZh;
  // 发行周期(英文)
  private String frequencyEn;
  // 来源于临时期刊表的分类信息
  private String category;
  // 导入时间
  private Date createDate;
  // 出版商
  private String publisherName;
  // 出版商地址
  private String publisherAddress;
  // 出版商url
  private String publisherUrl;
  // 导入情况。0：成功导入，1：记录重复，2：一号多刊名，3：pissn异常，4：
  private Long throwsCause;
  // 冗余字段用于页面显示，来源数据库缩写
  private String dbCode;
  // 出版国家
  private String region;
  // journal表jid
  private Long jid;
  // 期刊分类排名xml
  private String catRankXml;
  // 在线投稿地址
  private String submissionUrl;

  public BaseJournalTempBatch() {
    super();
    this.status = 0L;
    this.handleMethod = 0L;
    this.throwsCause = 0L;
    this.createDate = new Date();
  }

  @Id
  @Column(name = "TEMP_BATCH_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU_TEMP_BATCH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getTempBatchId() {
    return tempBatchId;
  }

  public void setTempBatchId(Long tempBatchId) {
    this.tempBatchId = tempBatchId;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "TITLE_EN")
  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  @Column(name = "TITLE_XX")
  public String getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

  @Column(name = "TITLE_ABBR")
  public String getTitleAbbr() {
    return titleAbbr;
  }

  public void setTitleAbbr(String titleAbbr) {
    this.titleAbbr = titleAbbr;
  }

  @Column(name = "PISSN")
  public String getPissn() {
    return pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  @Column(name = "EISSN")
  public String getEissn() {
    return eissn;
  }

  public void setEissn(String eissn) {
    this.eissn = eissn;
  }

  @Column(name = "CN")
  public String getCn() {
    return cn;
  }

  public void setCn(String cn) {
    this.cn = cn;
  }

  @Column(name = "LANGUAGE")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Column(name = "START_YEAR")
  public String getStartYear() {
    if (StringUtils.isNotBlank(startYear) && startYear.indexOf(".") != -1)
      startYear = startYear.substring(0, startYear.indexOf("."));
    return startYear;
  }

  public void setStartYear(String startYear) {
    if (StringUtils.isNotBlank(startYear) && startYear.indexOf(".") != -1)
      startYear = startYear.substring(0, startYear.indexOf("."));
    this.startYear = startYear;
  }

  @Column(name = "END_YEAR")
  public String getEndYear() {
    if (StringUtils.isNotBlank(endYear) && endYear.indexOf(".") != -1)
      endYear = endYear.substring(0, endYear.indexOf("."));
    return endYear;
  }

  public void setEndYear(String endYear) {
    if (StringUtils.isNotBlank(endYear) && endYear.indexOf(".") != -1)
      endYear = endYear.substring(0, endYear.indexOf("."));
    this.endYear = endYear;
  }

  @Column(name = "OA_STATUS")
  public String getOaStatus() {
    return oaStatus;
  }

  public void setOaStatus(String oaStatus) {
    this.oaStatus = oaStatus;
  }

  @Column(name = "ACTIVE_STATUS")
  public String getActiveStatus() {
    return activeStatus;
  }

  public void setActiveStatus(String activeStatus) {
    this.activeStatus = activeStatus;
  }

  @Column(name = "JOURNAL_URL")
  public String getJournalUrl() {
    return journalUrl;
  }

  public void setJournalUrl(String journalUrl) {
    this.journalUrl = journalUrl;
  }

  @Column(name = "DESCRIPTION_XX")
  public String getDescriptionXx() {
    return descriptionXx;
  }

  public void setDescriptionXx(String descriptionXx) {
    this.descriptionXx = descriptionXx;
  }

  @Column(name = "DESCRIPTION_EN")
  public String getDescriptionEn() {
    return descriptionEn;
  }

  public void setDescriptionEn(String descriptionEn) {
    this.descriptionEn = descriptionEn;
  }

  @Column(name = "JOU_IF")
  public String getJouIf() {
    return jouIf;
  }

  public void setJouIf(String jouIf) {
    this.jouIf = jouIf;
  }

  @Column(name = "IF_TYPE")
  public String getIfType() {
    return ifType;
  }

  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  @Column(name = "IF_YEAR")
  public String getIfYear() {
    return ifYear;
  }

  public void setIfYear(String ifYear) {
    this.ifYear = ifYear;
  }

  @Column(name = "CATEGORY")
  public String getCategory() {
    return category;
  }

  public void setCategory(String categoryId) {
    this.category = categoryId;
  }

  @Column(name = "KEYWORD_XX")
  public String getKeyWordXx() {
    return keyWordXx;
  }

  public void setKeyWordXx(String keyWordXx) {
    this.keyWordXx = keyWordXx;
  }

  @Column(name = "KEYWORD_EN")
  public String getKeyWordEn() {
    return keyWordEn;
  }

  public void setKeyWordEn(String keyWordEn) {
    this.keyWordEn = keyWordEn;
  }

  @Column(name = "STATUS")
  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  @Column(name = "HANDLE_METHOD")
  public Long getHandleMethod() {
    return handleMethod;
  }

  public void setHandleMethod(Long handleMethod) {
    this.handleMethod = handleMethod;
  }

  @Column(name = "UPDATE_YEAR")
  public String getUpdateYear() {
    if (StringUtils.isNotBlank(updateYear) && updateYear.indexOf(".") != -1)
      updateYear = updateYear.substring(0, updateYear.indexOf("."));
    return updateYear;
  }

  public void setUpdateYear(String updateYear) {
    if (StringUtils.isNotBlank(updateYear) && updateYear.indexOf(".") != -1)
      updateYear = updateYear.substring(0, updateYear.indexOf("."));
    this.updateYear = updateYear;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "FREQUENCY_ZH")
  public String getFrequencyZh() {
    return frequencyZh;
  }

  public void setFrequencyZh(String frequencyZh) {
    this.frequencyZh = frequencyZh;
  }

  @Column(name = "FREQUENCY_EN")
  public String getFrequencyEn() {
    return frequencyEn;
  }

  public void setFrequencyEn(String frequencyEn) {
    this.frequencyEn = frequencyEn;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "PUBLISHER_NAME")
  public String getPublisherName() {
    return publisherName;
  }

  public void setPublisherName(String publisherName) {
    this.publisherName = publisherName;
  }

  @Column(name = "PUBLISHER_ADDRESS")
  public String getPublisherAddress() {
    return publisherAddress;
  }

  public void setPublisherAddress(String publisherAddress) {
    this.publisherAddress = publisherAddress;
  }

  @Column(name = "PUBLISHER_URL")
  public String getPublisherUrl() {
    return publisherUrl;
  }

  public void setPublisherUrl(String publisherUrl) {
    this.publisherUrl = publisherUrl;
  }

  @Column(name = "THROWS_CAUSE")
  public Long getThrowsCause() {
    return throwsCause;
  }

  public void setThrowsCause(Long throwsCause) {
    this.throwsCause = throwsCause;
  }

  @Transient
  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
  }

  @Column(name = "REGION")
  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  @Column(name = "JID")
  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  @Column(name = "CAT_RANK_XML")
  public String getCatRankXml() {
    return catRankXml;
  }

  public void setCatRankXml(String catRankXml) {
    this.catRankXml = catRankXml;
  }

  @Column(name = "SUBMISSION_URL")
  public String getSubmissionUrl() {
    return submissionUrl;
  }

  public void setSubmissionUrl(String submissionUrl) {
    this.submissionUrl = submissionUrl;
  }

  @Transient
  public String getDescriptionEns() {
    return descriptionEns;
  }

  public void setDescriptionEns(String descriptionEns) {
    this.descriptionEns = descriptionEns;
  }

  @Transient
  public String getDescriptionXxs() {
    return descriptionXxs;
  }

  public void setDescriptionXxs(String descriptionXxs) {
    this.descriptionXxs = descriptionXxs;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
