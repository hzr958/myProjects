package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果、文献冗余表.
 * 
 * @author cwli
 * 
 */
@Entity
@Table(name = "PUB_KNOW")
public class PubKnow implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7367083007789922705L;

  // 成果编号
  private Long id;
  // 所有人ID
  private Long psnId;
  // 成果为1,文献为2
  private Integer articleType;
  // 成果类型 pub_type
  private Integer typeId;
  // 中文标题hash_code，查重时使用
  private Integer zhTitleHash;
  // 英文标题hash_code，查重时使用
  private Integer enTitleHash;
  // 1: 已删除，0: 未删除 2:未确认(成果确认) 3：他人推荐（共享、推荐）4:代检索成果 (仅在Scholar/SNS使用)
  private Integer status;
  // 成果拥有者的名字是否在成果的作者中
  private Integer isPubAuthors;
  // 成果期刊匹配的基础期刊id
  private Long jnlId;
  // 匹配的作者序列
  private Integer seqNo;

  public PubKnow() {
    super();
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PUB_TYPE")
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  @Column(name = "ARTICLE_TYPE")
  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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

  @Column(name = "IS_PUB_AUTHORS")
  public Integer getIsPubAuthors() {
    return isPubAuthors;
  }

  public void setIsPubAuthors(Integer isPubAuthors) {
    this.isPubAuthors = isPubAuthors;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

}
