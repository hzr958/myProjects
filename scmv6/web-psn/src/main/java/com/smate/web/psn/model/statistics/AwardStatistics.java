package com.smate.web.psn.model.statistics;

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
 * 赞操作统计
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "AWARD_STATISTICS")
public class AwardStatistics implements Serializable {

  private static final long serialVersionUID = -2179522724320634901L;

  // 主键
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_AWARD_STATISTICS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  // 操作人ID
  @Column(name = "PSN_ID")
  private Long psnId;

  // 被赞的人的ID
  @Column(name = "AWARD_PSN_ID")
  private Long awardPsnId;

  // 被赞的东西的主键
  @Column(name = "ACTION_KEY")
  private Long actionKey;

  // 被赞东西的类型 详情请看DynamicConstant.java
  @Column(name = "ACTION_TYPE")
  private Integer actionType;

  // 操作（0:取消赞 1：赞）
  @Column(name = "ACTION")
  private Integer action;

  // 操作日期
  @Column(name = "CREATE_DATE")
  private Date createDate;

  // 格式化的日期，方便查询（不带时分秒）
  @Column(name = "FORMATE_DATE")
  private Long formateDate;

  // IP地址
  @Column(name = "IP")
  private String ip;

  public AwardStatistics() {
    super();
  }

  public AwardStatistics(Long psnId, Long formateDate) {
    super();
    this.psnId = psnId;
    this.formateDate = formateDate;
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

  public Long getAwardPsnId() {
    return awardPsnId;
  }

  public void setAwardPsnId(Long awardPsnId) {
    this.awardPsnId = awardPsnId;
  }

  public Long getActionKey() {
    return actionKey;
  }

  public void setActionKey(Long actionKey) {
    this.actionKey = actionKey;
  }

  public Integer getActionType() {
    return actionType;
  }

  public void setActionType(Integer actionType) {
    this.actionType = actionType;
  }

  public Integer getAction() {
    return action;
  }

  public void setAction(Integer action) {
    this.action = action;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getFormateDate() {
    return formateDate;
  }

  public void setFormateDate(Long formateDate) {
    this.formateDate = formateDate;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

}
