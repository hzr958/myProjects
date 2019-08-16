package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果/人员信息.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_MEMBER")
public class PubMember implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7077367194844390210L;

  // 主键唯一标识
  private Long id;
  // Publication ID
  private Long pubId;
  // Sequence no for display
  private Integer seqNo;
  // member name
  private String name;
  // 成员在ScholarMate中的psn_id
  private Long psnId;
  // 1: 为本人，0: 不是本人
  private Integer owner;
  // 作者所在单位个数，单位详细存放在XML
  private Integer insCount;
  // 邮箱
  private String email;
  // 0:作者,1:通讯作者
  private Integer authorPos;
  // 0或者空：不是第一作者 1:第一作者
  private Integer firstAuthor;

  @Id
  @Column(name = "PM_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_MEMBER", allocationSize = 1)
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

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "MEMBER_NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "MEMBER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {

    this.psnId = psnId;
  }

  @Column(name = "OWNER")
  public Integer getOwner() {
    return owner;
  }

  public void setOwner(Integer owner) {
    this.owner = owner;
  }

  @Column(name = "INS_COUNT")
  public Integer getInsCount() {
    return insCount;
  }

  public void setInsCount(Integer insCount) {
    this.insCount = insCount;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "AUTHOR_POS")
  public Integer getAuthorPos() {
    return authorPos;
  }

  public void setAuthorPos(Integer authorPos) {
    this.authorPos = authorPos;
  }

  @Column(name = "FIRST_AUTHOR")
  public Integer getFirstAuthor() {
    return firstAuthor;
  }

  public void setFirstAuthor(Integer firstAuthor) {
    this.firstAuthor = firstAuthor;
  }

}
