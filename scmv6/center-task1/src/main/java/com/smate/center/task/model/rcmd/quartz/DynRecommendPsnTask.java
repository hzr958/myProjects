package com.smate.center.task.model.rcmd.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 推荐动态人员任务表.
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "DYN_RECOMMEND_PSN_TASK")
public class DynRecommendPsnTask implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8814809849529492628L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_DYN_RECOMMEND_PSN_TASK", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "RECOM_TYPE")
  private Integer recomType;
  @Column(name = "STATUS")
  private Integer status;// 推荐状态0-待更新；1-已更新.

  public DynRecommendPsnTask() {
    super();
  }

  public DynRecommendPsnTask(Long id, Long psnId, Integer recomType, Integer status) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.recomType = recomType;
    this.status = status;
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

  public Integer getRecomType() {
    return recomType;
  }

  public void setRecomType(Integer recomType) {
    this.recomType = recomType;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
