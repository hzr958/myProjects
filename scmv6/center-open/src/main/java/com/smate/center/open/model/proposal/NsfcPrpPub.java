package com.smate.center.open.model.proposal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.IrisStringUtils;


/**
 * 申报书 成果表
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "NSFC_PRP_PUB")
public class NsfcPrpPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2045237995040763721L;
  private Long id;
  private Long pubId;
  private Long pubOwnerPsnId;
  private String des3Id;
  private String des3PubId;
  private String isisGuid;
  private String title;
  private String authors;
  private String source;
  private Integer pubYear;
  // 论著
  private Integer treatiseType;

  // 成果类型、
  private Integer pubType;
  // 类型名字
  private String pubTypeName;
  // 类型中文名字
  private String pubTypeZhName;
  // 类型英文名字
  private String pubTypeEnName;

  // 自定义类型中文名字
  private String treatiseTypeZhName;
  // 自定义类型英文名字
  private String treatiseTypeEnName;

  private Integer needSync;
  private Integer nodeId;
  private Integer seqNo;
  private String listInfo;
  private Integer status;
  private Integer pubMonth;
  private Integer pubDay;
  private String listInfoSource;

  private String url;

  // 引用次数
  private Integer citedTimes;
  // 作者标识
  private String authorFlag;

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_NSFC_PRP_PUB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  @Transient
  public String getDes3Id() {
    if (StringUtils.isBlank(this.des3Id)) {
      this.des3Id = Des3Utils.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  @Transient
  public String getDes3PubId() {
    if (StringUtils.isBlank(this.des3PubId)) {
      this.des3PubId = Des3Utils.encodeToDes3(this.pubId.toString());
    }
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  /**
   * @return the pubId
   */
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  /**
   * @param pubId the pubId to set
   */
  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  /**
   * @return the isisGuid
   */
  @Column(name = "ISIS_GUID")
  public String getIsisGuid() {
    return isisGuid;
  }

  /**
   * @param isisGuid the isisGuid to set
   */
  public void setIsisGuid(String isisGuid) {
    this.isisGuid = isisGuid;
  }

  /**
   * @return the title
   */
  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = IrisStringUtils.filterSupplementaryChars(title);
  }

  /**
   * @return the authors
   */
  @Column(name = "AUTHORS")
  public String getAuthors() {
    return authors;
  }

  /**
   * @param authors the authors to set
   */
  public void setAuthors(String authors) {
    this.authors = IrisStringUtils.filterSupplementaryChars(authors);
  }

  /**
   * @return the source
   */
  @Column(name = "SOURCE")
  public String getSource() {
    return source;
  }

  /**
   * @param source the source to set
   */
  public void setSource(String source) {
    this.source = IrisStringUtils.filterSupplementaryChars(source);
  }

  /**
   * @return the pubYear
   */
  @Column(name = "PUB_YEAR")
  public Integer getPubYear() {

    if (pubYear == null) {
      this.pubYear = 0;
    }
    return pubYear;
  }

  /**
   * @param pubYear the pubYear to set
   */
  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  /**
   * @return the needSync
   */
  @Column(name = "NEED_SYNC")
  public Integer getNeedSync() {
    return needSync;
  }

  /**
   * @param needSync the needSync to set
   */
  public void setNeedSync(Integer needSync) {
    this.needSync = needSync;
  }

  /**
   * @return the nodeId
   */
  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  /**
   * @param nodeId the nodeId to set
   */
  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * @return the seqNo
   */
  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  /**
   * @param seqNo the seqNo to set
   */
  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  /**
   * @return the pubType
   */
  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  /**
   * @param pubType the pubType to set
   */
  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  /**
   * @return the pubTypeName
   */
  @Transient
  public String getPubTypeName() {
    return pubTypeName;
  }

  /**
   * @param pubTypeName the pubTypeName to set
   */
  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  /**
   * @return the pubTypeZhName
   */
  @Transient
  public String getPubTypeZhName() {
    return pubTypeZhName;
  }

  /**
   * @param pubTypeZhName the pubTypeZhName to set
   */
  public void setPubTypeZhName(String pubTypeZhName) {
    this.pubTypeZhName = pubTypeZhName;
  }

  /**
   * @return the pubTypeEnName
   */
  @Transient
  public String getPubTypeEnName() {
    return pubTypeEnName;
  }

  /**
   * @param pubTypeEnName the pubTypeEnName to set
   */
  public void setPubTypeEnName(String pubTypeEnName) {
    this.pubTypeEnName = pubTypeEnName;
  }

  @Transient
  public String getTreatiseTypeZhName() {
    return treatiseTypeZhName;
  }

  public void setTreatiseTypeZhName(String treatiseTypeZhName) {
    this.treatiseTypeZhName = treatiseTypeZhName;
  }

  @Transient
  public String getTreatiseTypeEnName() {
    return treatiseTypeEnName;
  }

  public void setTreatiseTypeEnName(String treatiseTypeEnName) {
    this.treatiseTypeEnName = treatiseTypeEnName;
  }

  /**
   * @return the listInfo
   */
  @Column(name = "LIST_INFO")
  public String getListInfo() {
    return listInfo;
  }

  /**
   * @param listInfo the listInfo to set
   */
  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  /**
   * @return the status
   */
  @Transient
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the pubMonth
   */
  @Column(name = "PUB_MONTH")
  public Integer getPubMonth() {
    return pubMonth;
  }

  /**
   * @param pubMonth the pubMonth to set
   */
  public void setPubMonth(Integer pubMonth) {
    this.pubMonth = pubMonth;
  }

  /**
   * @return the pubDay
   */
  @Column(name = "PUB_DAY")
  public Integer getPubDay() {
    return pubDay;
  }

  /**
   * @param pubDay the pubDay to set
   */
  public void setPubDay(Integer pubDay) {
    this.pubDay = pubDay;
  }

  /**
   * @return the treatiseType
   */
  @Column(name = "PTYPE")
  public Integer getTreatiseType() {
    return treatiseType;
  }

  /**
   * @param treatiseType the treatiseType to set
   */
  public void setTreatiseType(Integer treatiseType) {
    this.treatiseType = treatiseType;
  }

  @Column(name = "LIST_INFO_SOURCE")
  public String getListInfoSource() {
    return listInfoSource;
  }

  public void setListInfoSource(String listInfoSource) {
    this.listInfoSource = listInfoSource;
  }

  // @Column(name = "URL")
  @Transient
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Column(name = "CITED_TIMES")
  public Integer getCitedTimes() {
    return citedTimes;
  }

  public void setCitedTimes(Integer citedTimes) {
    this.citedTimes = citedTimes;
  }

  @Column(name = "PUB_OWNER_PSN_ID")
  public Long getPubOwnerPsnId() {
    return pubOwnerPsnId;
  }

  public void setPubOwnerPsnId(Long pubOwnerPsnId) {
    this.pubOwnerPsnId = pubOwnerPsnId;
  }

  @Column(name = "AUTHOR_FLAG")
  public String getAuthorFlag() {
    return authorFlag;
  }

  public void setAuthorFlag(String authorFlag) {
    this.authorFlag = authorFlag;
  }

}
