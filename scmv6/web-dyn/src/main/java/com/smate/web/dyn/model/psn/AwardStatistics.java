package com.smate.web.dyn.model.psn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.enums.converter.LikeStatusAttributeConverter;

/**
 * 赞操作统计
 * 
 * @author zk
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
  @Convert(converter = LikeStatusAttributeConverter.class)
  private LikeStatusEnum action;

  // 操作日期
  @Column(name = "CREATE_DATE")
  private Date createDate;

  // 格式化的日期，方便查询（不带时分秒）
  @Column(name = "FORMATE_DATE")
  private Long formateDate;

  // IP地址
  @Column(name = "IP")
  private String ip;

  public AwardStatistics(Long id, Long psnId, Long awardPsnId, Long actionKey, Integer actionType,
      LikeStatusEnum action, Date createDate, Long formateDate, String ip) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.awardPsnId = awardPsnId;
    this.actionKey = actionKey;
    this.actionType = actionType;
    this.action = action;
    this.createDate = createDate;
    this.formateDate = formateDate;
    this.ip = ip;
  }

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

  public LikeStatusEnum getAction() {
    return action;
  }

  public void setAction(LikeStatusEnum action) {
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
