package com.smate.center.task.model.group;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Email_Group_Pub_Psn")
public class EmailGroupPubPsn {
  // 群组ID
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_EMAIL_GROUP_PUB_PSN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;// 把成果添加到群组的人员id
  @Column(name = "GROUP_ID")
  private Long groupId;// 添加成果的群组
  @Column(name = "PUB_ID")
  private Long pubId;// 添加到群组的成果id
  @Column(name = "CREATE_DATE")
  private Date createDate;// 把成果添加到群组的时间
  @Column(name = "STATUS")
  private String status;// 这条成果是否已经处理了 0表示未处理，1表示已经处理

  public EmailGroupPubPsn() {
    super();
  }

  public EmailGroupPubPsn(Long psnId, Long groupId, Long pubId, Date createDate) {
    this.psnId = psnId;
    this.groupId = groupId;
    this.pubId = pubId;
    this.createDate = createDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}

