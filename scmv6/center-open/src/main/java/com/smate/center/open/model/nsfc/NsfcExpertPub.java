package com.smate.center.open.model.nsfc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "NSFC_EXPERT_PUB")
public class NsfcExpertPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -9089981516959549468L;
  // Fields
  private Long id;
  private Integer version = null;
  private Integer pubType = null;
  private Integer pubYear = null;
  private Long pubId;
  private String authors;
  private String title;
  private String source;
  private Integer isTag;
  private Integer isOpen;
  private String needSyc = "0";
  private String listInfo;
  private Integer seqNo;
  private String pubTypeName;
  private Integer nodeId;
  private Long pubOwnerPsnId;
  private String pubTypeDes;
  // 是否匹配到V3成果（v2.6数据同步）
  private Integer matched;
  @SuppressWarnings("unused")
  private String listInfoSources;

  @Column(name = "LIST_INFO_SOURCE")
  public String getListInfoSources() {
    return listInfoSources;
  }

  public void setListInfoSources(String listInfoSources) {
    this.listInfoSources = listInfoSources;
  }

  /** default constructor. */
  public NsfcExpertPub() {}

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_NSFC_EXPERT_PUB", allocationSize = 1)
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

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  // Constructors
  @Column(name = "LIST_INFO", precision = 50)
  public String getListInfo() {
    return listInfo;
  }

  public void setListInfo(String listInfo) {
    this.listInfo = listInfo;
  }

  @Transient
  public String getPubTypeName() {
    return pubTypeName;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  @Column(name = "VERSION")
  public Integer getVersion() {
    return this.version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return this.pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  @Column(name = "PUB_OWNER_PSNID")
  public Long getPubOwnerPsnId() {
    return pubOwnerPsnId;
  }

  public void setPubOwnerPsnId(Long pubOwnerPsnId) {
    this.pubOwnerPsnId = pubOwnerPsnId;
  }

  @Column(name = "PUB_YEAR", precision = 4)
  public Integer getPubYear() {
    return this.pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
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

  @Column(name = "AUTHORS", length = 200)
  public String getAuthors() {
    return this.authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  @Column(name = "TITLE", length = 200)
  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "SOURCE", length = 500)
  public String getSource() {
    return this.source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  @Column(name = "IS_TAG")
  public Integer getIsTag() {
    return this.isTag;
  }

  public void setIsTag(Integer isTag) {
    this.isTag = isTag;
  }

  @Column(name = "IS_OPEN")
  public Integer getIsOpen() {
    return this.isOpen;
  }

  /**
   * @return the pubTypeDes
   */
  @Transient
  public String getPubTypeDes() {
    return pubTypeDes;
  }

  /**
   * @param pubTypeDes the pubTypeDes to set
   */
  public void setPubTypeDes(String pubTypeDes) {
    this.pubTypeDes = pubTypeDes;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  @Column(name = "NEED_SYC", length = 1)
  public String getNeedSyc() {
    return this.needSyc;
  }

  @Column(name = "NODE_ID")
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setNeedSyc(String needSyc) {
    this.needSyc = needSyc;
  }

  @Column(name = "MATCHED")
  public Integer getMatched() {
    return matched;
  }

  public void setMatched(Integer matched) {
    this.matched = matched;
  }

}
