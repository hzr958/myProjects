package com.smate.center.task.v8pub.pdwh.po;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库成果成员
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Entity
@Table(name = "V_PUB_PDWH_MEMBER")
public class PdwhPubMemberPO implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 8210287433242019135L;

  @Id
  @SequenceGenerator(name = "SEQ_PDWH_MEMBER_ID", sequenceName = "SEQ_PDWH_MEMBER_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PDWH_MEMBER_ID")
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 成果id

  @Column(name = "SEQ_NO")
  private Integer seqNo; // 默认 成果所有人将拥有seq_no=0的记录 ，排序使用

  @Column(name = "psn_id")
  private Long psnId; // 科研之友对应的人员id 默认为空

  @Column(name = "NAME")
  private String name; // 成员名字

  @Column(name = "INS_COUNT")
  private Integer insCount; // 单位名称

  @Column(name = "EMAIL")
  private String email; // 邮箱

  @Column(name = "OWNER")
  private Integer owner; // 1: 为本人，0: 不是本人'

  @Column(name = "POST")
  private Integer communicable; // '0:作者,1:通讯作者'

  @Column(name = "FIRST_AUTHOR")
  private Integer firstAuthor; // 0或者空：不是第一作者 1:第一作者'

  public PdwhPubMemberPO() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getInsCount() {
    return insCount;
  }

  public void setInsCount(Integer insCount) {
    this.insCount = insCount;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getOwner() {
    return owner;
  }

  public void setOwner(Integer owner) {
    this.owner = owner;
  }

  public Integer getCommunicable() {
    return communicable;
  }

  public void setCommunicable(Integer communicable) {
    this.communicable = communicable;
  }

  public Integer getFirstAuthor() {
    return firstAuthor;
  }

  public void setFirstAuthor(Integer firstAuthor) {
    this.firstAuthor = firstAuthor;
  }
}
