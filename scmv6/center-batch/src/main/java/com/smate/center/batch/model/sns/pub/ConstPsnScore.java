package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员信息完整度计分规则.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "CONST_PSN_SCORE")
public class ConstPsnScore implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8041101594518110738L;
  private int typeId;// 计分类别ID
  private int scoreNum;// 得分
  private String scoreRule;// 得分规则
  private String remark;// 备注

  @Id
  @Column(name = "TYPE_ID")
  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  @Column(name = "SCORE_NUM")
  public int getScoreNum() {
    return scoreNum;
  }

  public void setScoreNum(int scoreNum) {
    this.scoreNum = scoreNum;
  }

  @Column(name = "SCORE_RULE")
  public String getScoreRule() {
    return scoreRule;
  }

  public void setScoreRule(String scoreRule) {
    this.scoreRule = scoreRule;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
