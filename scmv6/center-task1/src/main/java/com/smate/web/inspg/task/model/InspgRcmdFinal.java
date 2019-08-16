package com.smate.web.inspg.task.model;

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
 * 发现结构主页推荐-最终表
 * 
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "V_INSPG_RCMD_FINAL")
public class InspgRcmdFinal implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5073816030006216977L;

  private Long id;
  private Long psnId;
  private Long inspgId;
  private String insZhName;
  private String insEnName;
  private Integer rcmdScore;
  private Date createTime;

  public InspgRcmdFinal() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_INSPG_RCMD_FINAL", allocationSize = 1)
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

  @Column(name = "INSPG_ID")
  public Long getInspgId() {
    return inspgId;
  }

  public void setInspgId(Long inspgId) {
    this.inspgId = inspgId;
  }

  @Column(name = "INS_ZHNAME")
  public String getInsZhName() {
    return insZhName;
  }

  public void setInsZhName(String insZhName) {
    this.insZhName = insZhName;
  }

  @Column(name = "INS_ENNAME")
  public String getInsEnName() {
    return insEnName;
  }

  public void setInsEnName(String insEnName) {
    this.insEnName = insEnName;
  }

  @Column(name = "RCMD_SCORE")
  public Integer getRcmdScore() {
    return rcmdScore;
  }

  public void setRcmdScore(Integer rcmdScore) {
    this.rcmdScore = rcmdScore;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }



}
