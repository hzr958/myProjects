package com.smate.center.task.model.tmp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CompPdwhKwsWithNsfcKwsTask 任务记录表
 * 
 * @author LIJUN
 * @date 2018年4月12日
 */
@Entity
@Table(name = "Tmp_NsfcKws_Task_Record")
public class TmpNsfcKwsTaskRecord {
  @Id
  @Column(name = "ID")
  private Long Id;// pdwh_pub_keyword_split表id
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "STATUS") // 匹配上则为1
  private Integer status;

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
