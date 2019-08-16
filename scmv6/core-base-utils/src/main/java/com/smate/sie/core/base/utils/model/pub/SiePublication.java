package com.smate.sie.core.base.utils.model.pub;

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
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 成果实体
 * 
 * @author jszhou
 */

@Entity
@Table(name = "PUBLICATION")
public class SiePublication implements Serializable {

  private static final long serialVersionUID = 6967144098115648846L;
  /** 成果Id. */
  private Long pubId;
  /** 单位Id. */
  private Long insId;
  // 数据源
  private Long dbId;
  // 成果类型
  private Integer pubType;
  // 文献类型/会议类型/著作类型
  private Integer pubType2;
  // 中文标题
  private String zhTitle;
  // 英文标题
  private String enTitle;
  // 作者
  private String authorNames;
  // 来源（中文）
  private String briefDesc;
  // 来源（英文）
  private String briefDescEn;
  // 出版年份
  private Integer publishYear;
  // 出版月份
  private Integer publishMonth;
  // 出版日期
  private Integer publishDay;
  // 卷号
  private String volume;
  // 期号
  private String issue;
  // 开始页
  private String startPage;
  // 结束页
  private String endPage;
  // 文章号
  private String articleNo;
  // doi
  private String doi;
  // 出版期刊
  private Long jid;
  // 国家地区id
  private Long regionId;
  // 国家地区名称
  private String regionName;
  // 接受状态、出版状态
  private String pubStatus;
  // 流程状态
  private String status;
  // 最后更新时间
  private Date updateDate;
  // 最后编辑人
  private Long updatePsnId;
  // 创建时间
  private Date createDate;
  // 创建人
  private Long createPsnId;
  // 署名单位，文献中信息
  // @Column(name = "ADDRESS")
  private String address;
  // 数据来源：0表单新增，1xls导入，2标准文件导入，3联邦检索，4基准库指派，9脚本修复
  private Integer dateFrom;
  // 记录是否完整：0不完整，不可信，1完整，可信
  private Integer isValid;
  // 领域ID，CONST_FIELD.CATEGRY_ID
  // Private Integer categryId;
  // 部门名称
  private String unitName;
  // 是否公开：0不公开1公开，默认1
  private int isPublic = 1;
  private Long readNum; // 阅读数
  private Long downloadNum; // 下载数
  private Long citeNum;// 引用
  private String name;
  private String title;
  private String brief;
  private String publishDate;
  private String fulltextUrl;
  private Long isiCited; // 引用数
  private Date isiCitedUpdate; // 第三方文献库引用数更新时间

  private String fulltextFileid; // 全文ID
  private boolean pdwhImportStatus = false;

  @Column(name = "FULLTEXT_FILEID")
  public String getFulltextFileid() {
    return fulltextFileid;
  }

  public void setFulltextFileid(String fulltextFileid) {
    this.fulltextFileid = fulltextFileid;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  // 查重时，是否新增
  private int isJnlInsert = 0;

  public SiePublication() {
    super();
  }

  public SiePublication(Integer publishYear) {
    super();
    this.publishYear = publishYear;
  }

  public SiePublication(Long pubId) {
    super();
    this.pubId = pubId;
  }

  public SiePublication(Long pubId, String zhTitle, String enTitle) {
    super();
    this.pubId = pubId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
  }

  public SiePublication(Long pubId, Long insId, String zhTitle, String enTitle, String authorNames, String briefDesc,
      String briefDescEn, Integer publishYear, Integer publishMonth, Integer publishDay) {
    super();
    this.pubId = pubId;
    this.insId = insId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.authorNames = authorNames;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.publishYear = publishYear;
    this.publishMonth = publishMonth;
    this.publishDay = publishDay;
    this.title = getTitle();
    this.brief = getBrief();
    this.publishDate = getPublishDate();
  }

  @Column(name = "DATA_FROM")
  public Integer getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(Integer dateFrom) {
    this.dateFrom = dateFrom;
  }

  @Column(name = "DB_ID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  @Column(name = "PUB_TYPE2")
  public Integer getPubType2() {
    return pubType2;
  }

  public void setPubType2(Integer pubType2) {
    this.pubType2 = pubType2;
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

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  @Column(name = "BRIEF_DESC")
  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  @Column(name = "BRIEF_DESC_EN")
  public String getBriefDescEn() {
    return briefDescEn;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
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

  @Column(name = "VOLUME")
  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  @Column(name = "ISSUE")
  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  @Column(name = "START_PAGE")
  public String getStartPage() {
    return startPage;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  @Column(name = "END_PAGE")
  public String getEndPage() {
    return endPage;
  }

  public void setEndPage(String endPage) {
    this.endPage = endPage;
  }

  @Column(name = "ARTICLE_NO")
  public String getArticleNo() {
    return articleNo;
  }

  public void setArticleNo(String articleNo) {
    this.articleNo = articleNo;
  }

  @Column(name = "DOI")
  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  @Column(name = "JID")
  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  @Column(name = "PUB_STATUS")
  public String getPubStatus() {
    return pubStatus;
  }

  public void setPubStatus(String pubStatus) {
    this.pubStatus = pubStatus;
  }

  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "CREATE_PSN_ID")
  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  @Column(name = "ADDRESS")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Column(name = "IS_VALID")
  public Integer getIsValid() {
    return isValid;
  }

  public void setIsValid(Integer isValid) {
    this.isValid = isValid;
  }

  @Transient
  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  @Transient
  public Long getReadNum() {
    return readNum;
  }

  public void setReadNum(Long readNum) {
    this.readNum = readNum;
  }

  @Transient
  public Long getDownloadNum() {
    return downloadNum;
  }

  public void setDownloadNum(Long downloadNum) {
    this.downloadNum = downloadNum;
  }

  @Transient
  public Long getCiteNum() {
    return citeNum;
  }

  public void setCiteNum(Long citeNum) {
    this.citeNum = citeNum;
  }

  @Transient
  public int getIsJnlInsert() {
    return isJnlInsert;
  }

  public void setIsJnlInsert(int isJnlInsert) {
    this.isJnlInsert = isJnlInsert;
  }

  @Transient
  public String getName() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if ("zh".equals(language)) {
      name = StringUtils.isNotBlank(this.zhTitle) ? this.zhTitle : this.enTitle;
    } else {
      name = StringUtils.isNotBlank(this.enTitle) ? this.enTitle : this.enTitle;
    }
    return name;
  }

  @Id
  @Column(name = "PUB_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUBLICATION", allocationSize = 1)
  public Long getPubId() {
    return pubId;
  }

  /**
   * @param pubId
   */
  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  /** @return insId */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  @Column(name = "REGION_NAME")
  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  @Column(name = "IS_PUBLIC")
  public int getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(int isPublic) {
    this.isPublic = isPublic;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Transient
  public String getTitle() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if (StringUtils.isBlank(title)) {
      if ("zh".equals(language)) {
        this.title = StringUtils.isNotBlank(zhTitle) ? zhTitle : enTitle;
      } else {
        this.title = StringUtils.isNotBlank(enTitle) ? enTitle : zhTitle;
      }
    }
    return title;
  }

  @Transient
  public String getBrief() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if (StringUtils.isBlank(brief)) {
      if ("zh".equals(language)) {
        this.brief = StringUtils.isNotBlank(briefDesc) ? briefDesc : briefDescEn;
      } else {
        this.brief = StringUtils.isNotBlank(briefDescEn) ? briefDescEn : briefDesc;
      }
    }
    return brief;
  }

  @Transient
  public String getPublishDate() {
    StringBuffer date = new StringBuffer();
    if (publishYear == null) {
      this.publishDate = date.toString();
    } else if (publishMonth != null && publishDay != null) {
      this.publishDate = date.append(", " + publishYear + "-").append(publishMonth + "-").append(publishDay).toString();
    } else {
      date.append(", ");
      this.publishDate = date.append(publishYear).toString();

    }
    return publishDate;
  }

  @Column(name = "FULLTEXT_URL")
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  @Column(name = "ISI_CITED")
  public Long getIsiCited() {
    return isiCited;
  }

  @Column(name = "ISI_CITED_UPDATE")
  public Date getIsiCitedUpdate() {
    return isiCitedUpdate;
  }

  public void setIsiCited(Long isiCited) {
    this.isiCited = isiCited;
  }

  public void setIsiCitedUpdate(Date isiCitedUpdate) {
    this.isiCitedUpdate = isiCitedUpdate;
  }

  @Transient
  public boolean getPdwhImportStatus() {
    return pdwhImportStatus;
  }

  public void setPdwhImportStatus(boolean pdwhImportStatus) {
    this.pdwhImportStatus = pdwhImportStatus;
  }
}
