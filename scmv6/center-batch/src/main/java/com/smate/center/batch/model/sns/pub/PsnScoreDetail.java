package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员信息完整度计分详情.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "PSN_SCORE_DETAIL")
public class PsnScoreDetail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7376133624100968600L;
  private Long id;
  private Long psnId;
  private int typeId;
  private String typeValue;
  private int scoreNum;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_SCORE_DETAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "TYPE_ID")
  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  @Column(name = "TYPE_VALUE")
  public String getTypeValue() {
    return typeValue;
  }

  public void setTypeValue(String typeValue) {
    this.typeValue = typeValue;
  }

  @Column(name = "SCORE_NUM")
  public int getScoreNum() {
    return scoreNum;
  }

  public void setScoreNum(int scoreNum) {
    this.scoreNum = scoreNum;
  }

}
