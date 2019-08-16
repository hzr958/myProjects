package com.smate.center.task.model.sns.psn;

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
 * 下载和收藏统计
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "DOWNLOAD_COLLECT_STATISTICS")
public class DownloadCollectStatistics implements Serializable {

  private static final long serialVersionUID = 7251749263758411685L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DOWNLOAD_COLLECT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "DC_PSN_ID")
  private Long dcPsnId;

  // 被下载或收藏的东西的主键
  @Column(name = "ACTION_KEY")
  private Long actionKey;

  // 被下载或收藏的东西的类型 详情请看DynamicConstant.java
  @Column(name = "ACTION_TYPE")
  private Integer actionType;

  // 操作日期
  @Column(name = "DC_DATE")
  private Date dcdDate;

  // 格式化的日期，方便查询（不带时分秒）
  @Column(name = "FORMATE_DATE")
  private Long formateDate;

  @Column(name = "DCOUNT")
  private Long dcount;

  @Column(name = "CPUB_COUNT")
  private Long cpubCount;

  @Column(name = "CREF_COUNT")
  private Long crefCount;

  @Column(name = "IP")
  private String ip;

  public DownloadCollectStatistics() {
    super();
  }

  public DownloadCollectStatistics(Long dcPsnId, Long formateDate) {
    super();
    this.dcPsnId = dcPsnId;
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

  public Long getDcPsnId() {
    return dcPsnId;
  }

  public void setDcPsnId(Long dcPsnId) {
    this.dcPsnId = dcPsnId;
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

  public Date getDcdDate() {
    return dcdDate;
  }

  public void setDcdDate(Date dcdDate) {
    this.dcdDate = dcdDate;
  }

  public Long getFormateDate() {
    return formateDate;
  }

  public void setFormateDate(Long formateDate) {
    this.formateDate = formateDate;
  }

  public Long getDcount() {
    if (dcount == null) {
      return 0l;
    }
    return dcount;
  }

  public void setDcount(Long dcount) {
    this.dcount = dcount;
  }

  public Long getCpubCount() {
    if (this.cpubCount == null) {
      return 0l;
    }
    return cpubCount;
  }

  public void setCpubCount(Long cpubCount) {
    this.cpubCount = cpubCount;
  }

  public Long getCrefCount() {
    if (this.crefCount == null) {
      return 0l;
    }
    return crefCount;
  }

  public void setCrefCount(Long crefCount) {
    this.crefCount = crefCount;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

}
