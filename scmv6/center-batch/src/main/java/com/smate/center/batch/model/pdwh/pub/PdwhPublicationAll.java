package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 基准库各文献库冗余成果实体.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PUBLICATION_ALL")
public class PdwhPublicationAll implements Serializable {
  private static final long serialVersionUID = -6541302313583392110L;
  // 成果编号
  private Long id;
  private Long pubId;
  private Integer dbid;
  private String des3Id;
  // 成果类型 const_pub_type
  private Integer typeId;

  private String pubYear;
  // 出版年份
  private Integer publishYear;
  // 出版月份
  private Integer publishMonth;
  // 出版日期
  private Integer publishDay;
  // 中文标题
  private String zhTitle;
  // 外文标题
  private String enTitle;
  // 中文标题去特殊字符后的hash值用于查重
  private Long zhTitleHash;
  // 外文标题去特殊字符后的hash值用于查重
  private Long enTitleHash;
  // 中文关键词
  private String zhKeyword;
  // 英文关键词
  private String enKeyword;
  // 引用次数
  private Integer citedTimes;
  // 作者
  private String authorNames;
  // 来源
  private String briefDescZh;
  // 来源
  private String briefDescEn;
  // 全文
  private String fulltextUrl;
  // 收录情况
  private String listInfo;
  // 推荐关键词中的个数
  private Long count;
  // 查询表格显示用,该字段标记为@Transient
  private String htmlAbstract;
  // 被阅读的次数
  private Long readCount;
  // sns全文附件的个数
  private Long fullTextCount;

  // 成果期刊匹配基准库期刊base_journal表id
  private Long jnlId;
  private Integer scoreNum;// 结果排序的分值_MJG_SCM-3906.
  // 人员关键词匹配基准文献关键词
  private String psnMatchKeywords;
  // 文章所属的期刊的影响因子（冗余）
  private String impactFactors;
  // 论文作者
  List<Map<String, Object>> pubAuthorList;

  public PdwhPublicationAll() {
    super();
  }

  public PdwhPublicationAll(Long id, Long pubId, Integer dbid) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.dbid = dbid;
  }

  public PdwhPublicationAll(Long id, Long pubId, Integer dbid, Long jnlId) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.dbid = dbid;
    this.jnlId = jnlId;
  }

  public PdwhPublicationAll(Long id, Long pubId, Integer dbid, Integer typeId, String pubYear, String zhTitle,
      String enTitle, String authorNames, String briefDescZh, String briefDescEn) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.dbid = dbid;
    this.typeId = typeId;
    this.pubYear = pubYear;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDescZh = briefDescZh;
    this.briefDescEn = briefDescEn;
  }

  public PdwhPublicationAll(Long id, String pubYear, String zhTitle, String enTitle, String authorNames,
      String briefDescZh, String briefDescEn) {
    super();
    this.id = id;
    this.pubYear = pubYear;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDescZh = briefDescZh;
    this.briefDescEn = briefDescEn;
  }

  public PdwhPublicationAll(Long id, Integer dbId, Long pubId, String zhTitle, String enTitle, String authorNames,
      String briefDescZh, String briefDescEn) {
    super();
    this.id = id;
    this.dbid = dbId;
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDescZh = briefDescZh;
    this.briefDescEn = briefDescEn;
  }

  public PdwhPublicationAll(Long id, Long pubId, Integer dbid, Integer typeId, String pubYear, String zhTitle,
      String enTitle, String authorNames, String briefDescZh, String briefDescEn, Long count) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.dbid = dbid;
    this.typeId = typeId;
    this.pubYear = pubYear;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDescZh = briefDescZh;
    this.briefDescEn = briefDescEn;
    this.count = count;
  }

  public PdwhPublicationAll(Long id, Long pubId, Integer dbid, String zhTitle, String enTitle, String authorNames,
      String briefDescZh, String briefDescEn, String psnMatchKeywords) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.dbid = dbid;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDescZh = briefDescZh;
    this.briefDescEn = briefDescEn;
    this.psnMatchKeywords = psnMatchKeywords;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUBLICATION_ALL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "DBID")
  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  @Transient
  public String getDes3Id() {
    if (this.pubId != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.pubId.toString());
    }
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  @Column(name = "PUB_TYPE")
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  @Column(name = "PUB_YEAR")
  public String getPubYear() {
    return pubYear;
  }

  public void setPubYear(String pubYear) {
    this.pubYear = pubYear;
  }

  @Transient
  public Integer getPublishYear() {
    if (StringUtils.isNotBlank(pubYear)) {
      publishYear = NumberUtils.toInt(StringUtils.split(pubYear, "-|/")[0]);
    }
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  @Transient
  public Integer getPublishMonth() {
    if (StringUtils.isNotBlank(pubYear)) {
      String[] years = StringUtils.split(pubYear, "-|/");
      if (years.length > 1)
        publishMonth = NumberUtils.toInt(years[1]);
    }
    return publishMonth;
  }

  public void setPublishMonth(Integer publishMonth) {
    this.publishMonth = publishMonth;
  }

  @Transient
  public Integer getPublishDay() {
    if (StringUtils.isNotBlank(pubYear)) {
      String[] years = StringUtils.split(pubYear, "-|/");
      if (years.length > 2)
        publishDay = NumberUtils.toInt(years[2]);
    }
    return publishDay;
  }

  public void setPublishDay(Integer publishDay) {
    this.publishDay = publishDay;
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
  public Long getZhTitleHash() {
    return zhTitleHash;
  }

  public void setZhTitleHash(Long zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  @Column(name = "EN_TITLE_HASH")
  public Long getEnTitleHash() {
    return enTitleHash;
  }

  public void setEnTitleHash(Long enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  @Transient
  public String getZhKeyword() {
    return zhKeyword;
  }

  public void setZhKeyword(String zhKeyword) {
    this.zhKeyword = zhKeyword;
  }

  @Transient
  public String getEnKeyword() {
    return enKeyword;
  }

  public void setEnKeyword(String enKeyword) {
    this.enKeyword = enKeyword;
  }

  @Transient
  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorNames() {
    // 截取1000字符以内的作者
    if (StringUtils.isNotBlank(authorNames) && authorNames.length() > 1000) {
      authorNames = authorNames.substring(0, 1000);
    }
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  @Column(name = "ZH_BRIEF_DESC")
  public String getBriefDescZh() {
    return briefDescZh;
  }

  public void setBriefDescZh(String briefDescZh) {
    this.briefDescZh = IrisStringUtils.filterSupplementaryChars(briefDescZh);
  }

  @Column(name = "EN_BRIEF_DESC")
  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = IrisStringUtils.filterSupplementaryChars(briefDescEn);
  }

  @Transient
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  @Transient
  public String getListInfo() {
    return listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Transient
  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  @Transient
  public String getHtmlAbstract() {
    return htmlAbstract;
  }

  public void setHtmlAbstract(String htmlAbstract) {
    this.htmlAbstract = htmlAbstract;
  }

  @Transient
  public Long getReadCount() {
    return readCount;
  }

  public void setReadCount(Long readCount) {
    this.readCount = readCount;
  }

  @Transient
  public Long getFullTextCount() {
    return fullTextCount;
  }

  public void setFullTextCount(Long fullTextCount) {
    this.fullTextCount = fullTextCount;
  }

  @Transient
  public Integer getScoreNum() {
    return scoreNum;
  }

  public void setScoreNum(Integer scoreNum) {
    this.scoreNum = scoreNum;
  }

  @Transient
  public String getPsnMatchKeywords() {
    return psnMatchKeywords;
  }

  public void setPsnMatchKeywords(String psnMatchKeywords) {
    this.psnMatchKeywords = psnMatchKeywords;
  }

  @Transient
  public String getImpactFactors() {
    return impactFactors;
  }

  public void setImpactFactors(String impactFactors) {
    this.impactFactors = impactFactors;
  }

  @Transient
  public List<Map<String, Object>> getPubAuthorList() {
    return pubAuthorList;
  }

  public void setPubAuthorList(List<Map<String, Object>> pubAuthorList) {
    this.pubAuthorList = pubAuthorList;
  }

}
