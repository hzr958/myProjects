package com.smate.web.dyn.model.pdwhpub;

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
 * 基准库成果赞记录表
 * 
 * @author lhd
 *
 */
@Entity
@Table(name = "PDWH_PUB_AWARD")
public class PdwhPubAward implements Serializable {

  private static final long serialVersionUID = 3696941512668278103L;
  @Id
  @Column(name = "RECORD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_AWARD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long recordId;// 主键
  @Column(name = "PUB_ID")
  private Long pubId;// 基准库成果id
  @Column(name = "DB_ID")
  private Integer dbId; // 基准库dbId
  @Column(name = "AWARD_PSN_ID")
  private Long awardPsnId;// 赞/取消赞人员Id
  @Column(name = "AWARD_DATE")
  private Date awardDate;// 赞时间
  @Column(name = "STATUS")
  private Integer status;// 赞还是取消赞(默认0) 0:赞 1:取消赞

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Long getAwardPsnId() {
    return awardPsnId;
  }

  public void setAwardPsnId(Long awardPsnId) {
    this.awardPsnId = awardPsnId;
  }

  public Date getAwardDate() {
    return awardDate;
  }

  public void setAwardDate(Date awardDate) {
    this.awardDate = awardDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public PdwhPubAward() {}

}
