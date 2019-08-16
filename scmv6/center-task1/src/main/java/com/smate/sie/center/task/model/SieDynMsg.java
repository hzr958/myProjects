package com.smate.sie.center.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 动态信息表
 */
@Entity
@Table(name = "DYN_MSG")
public class SieDynMsg {

  @Id
  @Column(name = "DYN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYN_MSG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long dynId;
  @Column(name = "producer_psn_id")
  private Long producerPsnId;
  @Column(name = "res_id")
  private Long resId;
  @Column(name = "res_type")
  private Integer resType;
  @Column(name = "dyn_type")
  private Long dynType;
  @Column(name = "producer_name")
  private String producerName;
  @Column(name = "producer_avatars")
  private String producerAvatars;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "STATUS")
  private Long status;
  @Column(name = "DATA_FROM")
  private Long dataFrom;
  @Column(name = "SNS_DYN_ID")
  private Long snsDynId;

  public Long getDynId() {
    return dynId;
  }

  public Long getProducerPsnId() {
    return producerPsnId;
  }

  public Long getResId() {
    return resId;
  }

  public Integer getResType() {
    return resType;
  }

  public Long getDynType() {
    return dynType;
  }

  public String getProducerName() {
    return producerName;
  }

  public String getProducerAvatars() {
    return producerAvatars;
  }

  public Long getStatus() {
    return status;
  }

  public Long getDataFrom() {
    return dataFrom;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public void setProducerPsnId(Long producerPsnId) {
    this.producerPsnId = producerPsnId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

  public void setDynType(Long dynType) {
    this.dynType = dynType;
  }

  public void setProducerName(String producerName) {
    this.producerName = producerName;
  }

  public void setProducerAvatars(String producerAvatars) {
    this.producerAvatars = producerAvatars;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public void setDataFrom(Long dataFrom) {
    this.dataFrom = dataFrom;
  }

  public Long getSnsDynId() {
    return snsDynId;
  }

  public void setSnsDynId(Long snsDynId) {
    this.snsDynId = snsDynId;
  }

}
