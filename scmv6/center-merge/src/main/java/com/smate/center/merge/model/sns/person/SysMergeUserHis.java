package com.smate.center.merge.model.sns.person;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员合并记录表
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "SYS_MERGE_USER_HIS")
public class SysMergeUserHis implements Serializable {

  private static final long serialVersionUID = 3027294339749096801L;
  private Long id;
  private Long psnId;
  private String loginName;
  private Integer status;// 人员合并后状态：1-保留；99-删除.
  private Date dealTime;
  private Integer mailStatus;// 是否已发邮箱通知0-未发送；1-已发送；2-发送失败.
  private String email;
  private Integer mergeStatus;// 合并状态0-已初始化；1-正在合并中；2-合并失败；3-已合并成功.
  private Long delPsnId;

  public SysMergeUserHis() {
    super();
  }

  public SysMergeUserHis(Long id, Long psnId, String loginName, Integer status, Date dealTime, Integer mailStatus,
      String email, Integer mergeStatus, Long delPsnId) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.loginName = loginName;
    this.status = status;
    this.dealTime = dealTime;
    this.mailStatus = mailStatus;
    this.email = email;
    this.mergeStatus = mergeStatus;
    this.delPsnId = delPsnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SYS_MERGE_USER_HIS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "LOGIN_NAME")
  public String getLoginName() {
    return loginName;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "DEAL_TIME")
  public Date getDealTime() {
    return dealTime;
  }

  @Column(name = "MAIL_STATUS")
  public Integer getMailStatus() {
    return mailStatus;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "MERGE_STATUS")
  public Integer getMergeStatus() {
    return mergeStatus;
  }

  @Column(name = "DEL_PSN_ID")
  public Long getDelPsnId() {
    return delPsnId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setDealTime(Date dealTime) {
    this.dealTime = dealTime;
  }

  public void setMailStatus(Integer mailStatus) {
    this.mailStatus = mailStatus;
  }

  public void setMergeStatus(Integer mergeStatus) {
    this.mergeStatus = mergeStatus;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setDelPsnId(Long delPsnId) {
    this.delPsnId = delPsnId;
  }
}
