package com.smate.center.task.model.sns.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author LJ
 *
 *         2017年7月5日
 */
@Entity
@Table(name = "DES3_PUBID")
public class Des3PubId {

  private String pubId;
  private String des3Id;

  @Id
  @Column(name = "PUB_ID")
  public String getPubId() {
    return pubId;
  }

  public void setPubId(String pubId) {
    this.pubId = pubId;
  }

  @Column(name = "DES3_ID")
  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }



}
