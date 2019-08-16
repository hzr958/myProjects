package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "NSFC_LAB_PUB")
public class NsfcLabPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5633238878555641166L;
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
  private Integer pubMonth;
  private Integer pubDay;
  // 是否匹配到V3成果（v2.6数据同步）
  private Integer matched;

  private Long labPsnId;

  private Integer labPubType;
  private Integer pubRefTotal;
  private String pubAttMembers;
  private Integer isInsAtt;
  private Integer insOrder;
  private String pubTypeDes;
  // 刊物、出版社或授权单位名称
  private String pubDes1;
  // 年、卷、期、页或专利号
  private String pubDes2;

  private String awardDate;
  private Long refId;

  /** default constructor. */
  public NsfcLabPub() {}

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_NSFC_LAB_PUB", allocationSize = 1)
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

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  /**
   * @return the refId
   */
  @Column(name = "PID")
  public Long getRefId() {
    return refId;
  }

  /**
   * @param refId the refId to set
   */
  public void setRefId(Long refId) {
    this.refId = refId;
  }

  /**
   * @return the pId
   */

  /**
   * @return the pubDes1
   */
  @Transient
  public String getPubDes1() {
    return pubDes1;
  }

  /**
   * @param pubDes1 the pubDes1 to set
   */
  public void setPubDes1(String pubDes1) {
    this.pubDes1 = pubDes1;
  }

  /**
   * @return the pubDes2
   */
  @Transient
  public String getPubDes2() {
    return pubDes2;
  }

  /**
   * @param pubDes2 the pubDes2 to set
   */
  public void setPubDes2(String pubDes2) {
    this.pubDes2 = pubDes2;
  }

  /**
   * @return the awardDate
   */
  @Transient
  public String getAwardDate() {
    return awardDate;
  }

  /**
   * @param awardDate the awardDate to set
   */
  public void setAwardDate(String awardDate) {
    this.awardDate = awardDate;
  }

  /**
   * @return the labPsnId
   */
  @Column(name = "LAB_PSN_ID")
  public Long getLabPsnId() {
    return labPsnId;
  }

  /**
   * @param labPsnId the labPsnId to set
   */
  public void setLabPsnId(Long labPsnId) {
    this.labPsnId = labPsnId;
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

  /**
   * @return the labPubType
   */
  @Column(name = "LAB_PUB_TYPE")
  public Integer getLabPubType() {
    return labPubType;
  }

  /**
   * @param labPubType the labPubType to set
   */
  public void setLabPubType(Integer labPubType) {
    this.labPubType = labPubType;
  }

  /**
   * @return the pubRefTotal
   */
  @Column(name = "PUB_REF_TOTAL")
  public Integer getPubRefTotal() {
    return pubRefTotal;
  }

  /**
   * @param pubRefTotal the pubRefTotal to set
   */
  public void setPubRefTotal(Integer pubRefTotal) {
    this.pubRefTotal = pubRefTotal;
  }

  /**
   * @return the pubAttMembers
   */
  @Column(name = "PUB_ATT_MEMBERS")
  public String getPubAttMembers() {
    return pubAttMembers;
  }

  /**
   * @param pubAttMembers the pubAttMembers to set
   */
  public void setPubAttMembers(String pubAttMembers) {
    this.pubAttMembers = pubAttMembers;
  }

  /**
   * @return the isInsAtt
   */
  @Column(name = "IS_INS_ATT")
  public Integer getIsInsAtt() {
    return isInsAtt;
  }

  /**
   * @param isInsAtt the isInsAtt to set
   */
  public void setIsInsAtt(Integer isInsAtt) {
    this.isInsAtt = isInsAtt;
  }

  /**
   * @return the insOrder
   */
  @Column(name = "INS_ORDER")
  public Integer getInsOrder() {
    return insOrder;
  }

  /**
   * @param insOrder the insOrder to set
   */
  public void setInsOrder(Integer insOrder) {
    this.insOrder = insOrder;
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

}
