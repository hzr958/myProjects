package com.smate.center.merge.model.sns.grp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 互联互通 关联群组
 */
@Entity
@Table(name = "v_open_group_union")
public class OpenGroupUnion {


  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_OPEN_GROUP_UNION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "GROUP_CODE")
  private String groupCode;

  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;

  @Column(name = "OWNER_OPEN_ID")
  private Long ownerOpenId;

  @Column(name = "GROUP_ID")
  private Long groupId;

  @Column(name = "TOKEN")
  private String token;

  @Column(name = "CREATETIME")
  private Date createTime;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Long getOwnerOpenId() {
    return ownerOpenId;
  }

  public void setOwnerOpenId(Long ownerOpenId) {
    this.ownerOpenId = ownerOpenId;
  }



}
