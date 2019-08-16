package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description 会议论文数据统计存储对象
 * @author xiexing
 * @date 2019年2月28日
 */
@Entity
@Table(name = "t_pub_temp")
public class PubTemp implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4320624775997646666L;

  /**
   * 会议论文id
   */
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  /**
   * 组织者id
   */
  @Column(name = "ORGANIZER")
  private String organizer;

  /**
   * 处理状态,默认为0,处理完成为1
   */
  @Column(name = "status")
  private Integer status;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getOrganizer() {
    return organizer;
  }

  public void setOrganizer(String organizer) {
    this.organizer = organizer;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public PubTemp(Long pubId, String organizer, Integer status) {
    super();
    this.pubId = pubId;
    this.organizer = organizer;
    this.status = status;
  }

  public PubTemp() {
    super();
    // TODO Auto-generated constructor stub
  }

  @Override
  public String toString() {
    return "PubTemp [pubId=" + pubId + ", organizer=" + organizer + ", status=" + status + "]";
  }


}
