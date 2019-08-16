package com.smate.web.dyn.model.dynamic;



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
 * 资源赞详情model.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "DYNAMIC_AWARD_RES")
public class DynamicAwardRes implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7988794193626979807L;
  private Long awardId;
  private int resType;
  private Long resId;
  private int resNode;
  private Long awardTimes;
  private Date updateDate;

  public DynamicAwardRes() {}

  public DynamicAwardRes(Long awardId, Long awardTimes) {
    this.awardId = awardId;
    this.awardTimes = awardTimes;
  }

  @Id
  @Column(name = "AWARD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYNAMIC_AWARD_RES", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getAwardId() {
    return awardId;
  }

  public void setAwardId(Long awardId) {
    this.awardId = awardId;
  }

  @Column(name = "RES_TYPE")
  public int getResType() {
    return resType;
  }

  public void setResType(int resType) {
    this.resType = resType;
  }

  @Column(name = "RES_ID")
  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  @Column(name = "RES_NODE")
  public int getResNode() {
    return resNode;
  }

  public void setResNode(int resNode) {
    this.resNode = resNode;
  }

  @Column(name = "AWARD_TIMES")
  public Long getAwardTimes() {
    return awardTimes;
  }

  public void setAwardTimes(Long awardTimes) {
    this.awardTimes = awardTimes;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
