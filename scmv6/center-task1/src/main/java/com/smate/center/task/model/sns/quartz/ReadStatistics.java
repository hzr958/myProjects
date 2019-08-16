package com.smate.center.task.model.sns.quartz;

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
 * 阅读统计
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "READ_STATISTICS")
public class ReadStatistics implements Serializable {

  private static final long serialVersionUID = -5919689269699325832L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_READ_STATISTICS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "READ_PSN_ID")
  private Long readPsnId;

  // 被阅读的东西的主键
  @Column(name = "ACTION_KEY")
  private Long actionKey;

  // 被阅读东西的类型 详情请看DynamicConstant.java
  @Column(name = "ACTION_TYPE")
  private Integer actionType;

  // 操作日期
  @Column(name = "CREATE_DATE")
  private Date createDate;

  // 格式化的日期，方便查询（不带时分秒）
  @Column(name = "FORMATE_DATE")
  private Long formateDate;

  // 所有的浏览次数
  @Column(name = "TOTAL_COUNT")
  private Long totalCount;

  // 普通的浏览，例如查看个人主页的时候，看了别人的成果
  @Column(name = "NORMAL_COUNT")
  private Long normalCount;

  // 详情浏览，点进成果详情来浏览
  @Column(name = "DETAIL_COUNT")
  private Long detailCount;

  // IP地址
  @Column(name = "IP")
  private String ip;

  public ReadStatistics() {
    super();
  }

  public ReadStatistics(Long psnId, Long formateDate) {
    super();
    this.psnId = psnId;
    this.formateDate = formateDate;
  }

  public ReadStatistics(Long psnId, Date createDate) {
    super();
    this.psnId = psnId;
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

  public Long getReadPsnId() {
    return readPsnId;
  }

  public void setReadPsnId(Long readPsnId) {
    this.readPsnId = readPsnId;
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

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public Long getNormalCount() {
    if (this.normalCount == null)
      return 0l;
    return normalCount;
  }

  public void setNormalCount(Long normalCount) {
    this.normalCount = normalCount;
  }

  public Long getDetailCount() {
    if (this.detailCount == null)
      return 0l;
    return detailCount;
  }

  public void setDetailCount(Long detailCount) {
    this.detailCount = detailCount;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

}
