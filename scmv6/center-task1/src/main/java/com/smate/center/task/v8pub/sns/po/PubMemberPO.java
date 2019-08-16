package com.smate.center.task.v8pub.sns.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果成员
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Entity
@Table(name = "V_PUB_MEMBER")
public class PubMemberPO {

  @Id
  @SequenceGenerator(name = "SEQ_PUB_MEMBER_ID", sequenceName = "SEQ_PUB_MEMBER_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUB_MEMBER_ID")
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "INS_ID")
  private Long insId; // 单位名称id

  @Column(name = "SEQ_NO")
  private Integer seqNo; // 默认 成果所有人将拥有seq_no=0的记录 ，排序使用

  @Column(name = "PSN_ID")
  private Long psnId; // 科研之友对应的人员id 默认为空

  @Column(name = "NAME")
  private String name; // 成员名字

  @Column(name = "INS_NAME")
  private String insName; // 单位名称

  @Column(name = "EMAIL")
  private String email; // 邮箱

  @Column(name = "OWNER")
  private Integer owner; // 1: 为本人，0: 不是本人'

  @Column(name = "POST")
  private Integer communicable; // '0:作者,1:通讯作者'

  @Column(name = "FIRST_AUTHOR")
  private Integer firstAuthor; // 0或者空：不是第一作者 1:第一作者'


  public PubMemberPO() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
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

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
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

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
