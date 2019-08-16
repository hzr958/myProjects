package com.smate.center.task.v8pub.pdwh.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果DOI数据
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Entity
@Table(name = "V_PUB_PDWH_DOI")
public class PdwhPubDoiPO implements Serializable {

  private static final long serialVersionUID = 562388937969878702L;

  @Id
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // pdwh库pubId，主键

  @Column(name = "DOI")
  private String doi; // doi数据


  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

}
