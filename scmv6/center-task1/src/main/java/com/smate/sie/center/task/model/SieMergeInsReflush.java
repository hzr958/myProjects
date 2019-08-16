package com.smate.sie.center.task.model;

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
 * 合并单位信息操作表
 * 
 * @author 叶星源
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "MERGE_INS_REFLUSH")
public class SieMergeInsReflush implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MERGE_INS_REFLUSH", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "MAIN_INS_ID")
  private Long mainInsId;

  @Column(name = "MERGE_INS_ID")
  private Long mergeInsId;

  @Column(name = "MERGE_TIME")
  private Date mergeTime;

  @Column(name = "STATUS")
  private Integer status;

  public SieMergeInsReflush() {
    super();
  }

  public Integer getId() {
    return id;
  }

  public Long getMainInsId() {
    return mainInsId;
  }

  public Long getMergeInsId() {
    return mergeInsId;
  }

  public Date getMergeTime() {
    return mergeTime;
  }

  public Integer getStatus() {
    return status;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setMainInsId(Long mainInsId) {
    this.mainInsId = mainInsId;
  }

  public void setMergeInsId(Long mergeInsId) {
    this.mergeInsId = mergeInsId;
  }

  public void setMergeTime(Date mergeTime) {
    this.mergeTime = mergeTime;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public SieMergeInsReflush(Integer id, Long mainInsId, Long mergeInsId, Date mergeTime, Integer status) {
    super();
    this.id = id;
    this.mainInsId = mainInsId;
    this.mergeInsId = mergeInsId;
    this.mergeTime = mergeTime;
    this.status = status;
  }

}
