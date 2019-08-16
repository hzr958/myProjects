package com.smate.center.task.model.sns.pub;

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
 * 重复成果记录组信息表
 * 
 * @author YJ
 * @createTime 2018年1月2日 下午14:00:00
 */

@Entity
@Table(name = "V_PUB_SAME_RECORD")
public class PubSameRecord implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "RECORD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_PUB_SAME_RECORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long recordId; // 分组id
  @Column(name = "PSN_ID")
  private Long psnId; // 人员id
  @Column(name = "PUB_COUNT")
  private Long pubCount; // 成果数
  @Column(name = "DEAL_STATUS")
  private Integer dealStatus; // 0=未处理；1-已处理
  @Column(name = "CREATE_DATE")
  private Date createDate; // 创建时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 更新时间

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPubCount() {
    return pubCount;
  }

  public void setPubCount(Long pubCount) {
    this.pubCount = pubCount;
  }

  public Integer getDealStatus() {
    return dealStatus;
  }

  public void setDealStatus(Integer dealStatus) {
    this.dealStatus = dealStatus;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public PubSameRecord(Long psnId, Long pubCount, Integer dealStatus, Date createDate, Date updateDate) {
    super();
    this.psnId = psnId;
    this.pubCount = pubCount;
    this.dealStatus = dealStatus;
    this.createDate = createDate;
    this.updateDate = updateDate;
  }

}
