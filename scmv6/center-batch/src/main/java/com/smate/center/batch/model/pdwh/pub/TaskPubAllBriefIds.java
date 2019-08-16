package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果相关文献推荐任务ID表.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "TASK_PUBALL_BRIEF_IDS")
public class TaskPubAllBriefIds implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2275274681290796143L;
  @Id
  @Column(name = "PUBALL_ID")
  private Long puballId;
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "DBID")
  private Integer dbid;
  @Column(name = "STATUS")
  private Integer status;


  public Long getPuballId() {
    return puballId;
  }

  public void setPuballId(Long puballId) {
    this.puballId = puballId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDbid() {
    return dbid;
  }

  public void setDbid(Integer dbid) {
    this.dbid = dbid;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }



}
