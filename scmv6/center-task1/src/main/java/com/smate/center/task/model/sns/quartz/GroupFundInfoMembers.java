package com.smate.center.task.model.sns.quartz;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组关键词
 * 
 * @author hzr
 *
 */
@Entity
@Table(name = "GROUP_FUNDINFO_MEMBERS")
public class GroupFundInfoMembers implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -252010444098834089L;

  private Long groupId; // 群组id
  private Long psnId; // 基金负责人id
  private String fundInfo; // 群组基金
  private Integer status; // 处理状态
  private String members; // 项目人员

  @Id
  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Column(name = "FUND_INFO")
  public String getFundInfo() {
    return fundInfo;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "MEMBERS")
  public String getMembers() {
    return members;
  }

  public void setMembers(String members) {
    this.members = members;
  }


}
