package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 推荐成果文献--成果文献详细列表.
 * 
 * @author LY
 * 
 */
@Entity
@Table(name = "PSN_RES_SEND_PUB_DETAIL")
public class PsnResSendPubDetail implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -4432136474438265325L;
  private Long id;
  private PsnResSend resSend;
  private Long sendId;
  private Long pubId;
  private Integer nodeId;
  // 所有人ID
  private Long psnId;
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
  // 中文标题
  private String zhTitle;
  // 外文标题
  private String enTitle;
  // 引用次数
  private Integer citedTimes;
  // 引用次数更新次数
  private Date citedDate;
  // 成果收藏夹
  // private List<Folder> pubFolders = new ArrayList<Folder>();
  // 查询表格显示用,该字段标记为@Transient
  private String htmlAbstract;
  // 类别名称
  private String typeName;
  // 作者
  private String authorNames;
  // 来源
  private String briefDesc;
  // 全文，fulltext_fileid为空，则取fulltext_url
  private String fulltextFileid;
  // 全文
  private String fulltextUrl;
  // 引用情况
  private String citedInfo;
  // 引用URL
  private String citedUrl;
  private String pubXmlData;
  /**
   * 加密ID.
   */
  private String des3Id;

  public PsnResSendPubDetail() {}

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_RES_SEND_PUB_DETAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "RES_SEND_ID", insertable = false, updatable = false)
  @JsonIgnore
  public PsnResSend getResSend() {
    return resSend;
  }

  public void setResSend(PsnResSend resSend) {
    this.resSend = resSend;
  }

  @Transient
  public String getPubXmlData() {
    return pubXmlData;
  }

  public void setPubXmlData(String pubXmlData) {
    this.pubXmlData = pubXmlData;
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

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Transient
  public String getCitedInfo() {
    return citedInfo;
  }

  public void setCitedInfo(String citedInfo) {
    this.citedInfo = citedInfo;
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

  @Column(name = "RES_SEND_ID")
  public Long getSendId() {
    return sendId;
  }

  public void setSendId(Long sendId) {
    this.sendId = sendId;
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

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "CITED_URL")
  public String getCitedUrl() {
    return citedUrl;
  }

  public void setCitedUrl(String citedUrl) {
    this.citedUrl = citedUrl;
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

  @Column(name = "AUTHOR_NAMES")
  public String getAuthorNames() {
    return authorNames;
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

  @Transient
  public String getDes3Id() {

    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

}
