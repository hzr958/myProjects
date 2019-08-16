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
 * 群组检索过滤表(封装常用查询条件,检索时过滤群组).
 * 
 * @author mjg
 * @since 2014-11-28
 */
@Entity
@Table(name = "GROUP_FILTER")
public class GroupFilter implements Serializable {

  private static final long serialVersionUID = 200587394130191667L;
  // 群组ID
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_FILTER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "GROUP_ID")
  private Long groupId;
  // 群组认证码
  @Column(name = "GROUP_CODE")
  private String groupCode;
  // 公开类型[O=开放,H=半开放,P=保密]
  @Column(name = "OPEN_TYPE")
  private String openType = "H";
  // 群组状态[01=正常,99=删除]
  @Column(name = "STATUS")
  private String status = "01";
  // 群组拥有者的psn_id
  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;

  public GroupFilter() {
    super();
  }

  public GroupFilter(Long groupId, String groupCode, String openType, String status, Long ownerPsnId) {
    super();
    this.groupId = groupId;
    this.groupCode = groupCode;
    this.openType = openType;
    this.status = status;
    this.ownerPsnId = ownerPsnId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }
}
