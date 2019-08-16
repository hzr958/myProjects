package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 暂时存储页面保存成果的数据,等待后台任务将这些临时数据转化为真实的数据. 目的是存储成果数据,为后台任务提供原始数据.使用在成果保存等.
 * 
 */
@Entity
@Table(name = "V_PUB_DATA_STORE")
public class PubDataStore implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2333209263452861523L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId; // 成果编号

  @Column(name = "DATA")
  private String data; // 原始数据<xml>

  public PubDataStore() {
    super();
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

}
