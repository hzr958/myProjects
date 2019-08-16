package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "TMP_JIANGXI_PUBADDR")
public class TmpJiangxiPubaddr implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ADDR_ID")
  private Long addrId;
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "PUB_TYPE")
  private Integer pubType;
  @Column(name = "STATUS")
  private Integer status;
  @Column(name = "DB_ID")
  private Integer dbId;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "ADDR_HASH")
  private Long addrHash;
  @Column(name = "LANGUAGE")
  private Integer language;


  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Long getAddrHash() {
    return addrHash;
  }

  public void setAddrHash(Long addrHash) {
    this.addrHash = addrHash;
  }

  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

  public Long getAddrId() {
    return addrId;
  }

  public void setAddrId(Long addrId) {
    this.addrId = addrId;
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
