package com.smate.center.task.v8pub.sns.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人库成果DOI数据
 * 
 * @author YJ
 *
 *         2019年3月29日
 */

@Entity
@Table(name = "V_PUB_DOI")
public class PubDoiPO implements Serializable {

  private static final long serialVersionUID = -3090258119672936782L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId; // sns库pubId，主键

  @Column(name = "DOI")
  private String doi; // doi数据

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

}
