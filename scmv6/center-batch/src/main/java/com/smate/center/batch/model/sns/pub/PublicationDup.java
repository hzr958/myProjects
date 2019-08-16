package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 成果查重使用类.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUBLICATION")
public class PublicationDup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 867504306111969864L;

  // 成果编号
  private Long id;
  // 所有者(对应 person的psn_Id)
  private Long psnId;
  // 中文标题hash_code，查重时使用
  private Integer zhTitleHash;
  // 英文标题hash_code，查重时使用
  private Integer enTitleHash;
  // 成果指纹
  private Integer figerPrint;
  // 成果所属 期刊ID
  private Long jid;
  // 成果类型 CONST_PUB_type
  private Integer pubType;
  // 1: 已删除，0: 未删除 (仅在Scholar/SNS使用)
  private Integer status;
  //
  private Integer articleType;
  // 查重时，是否新增[0：不显示，1：显示]
  private int isJnlInsert = 0;// 默认不显示新增

  public PublicationDup() {
    super();
  }

  public PublicationDup(Long id, Integer pubType) {
    super();
    this.id = id;
    this.pubType = pubType;
  }

  public PublicationDup(Long id, Integer pubType, Long jid) {
    super();
    this.id = id;
    this.pubType = pubType;
    this.jid = jid;
  }

  public PublicationDup(Long id, Integer pubType, Long jid, int isJnlInsert) {
    super();
    this.id = id;
    this.pubType = pubType;
    this.jid = jid;
    this.isJnlInsert = isJnlInsert;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
  public Integer getFigerPrint() {
    return figerPrint;
  }

  public void setFigerPrint(Integer figerPrint) {
    this.figerPrint = figerPrint;
  }

  @Column(name = "JID")
  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "ARTICLE_TYPE")
  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  @Transient
  public int getIsJnlInsert() {
    return isJnlInsert;
  }

  public void setIsJnlInsert(int isJnlInsert) {
    this.isJnlInsert = isJnlInsert;
  }

}
