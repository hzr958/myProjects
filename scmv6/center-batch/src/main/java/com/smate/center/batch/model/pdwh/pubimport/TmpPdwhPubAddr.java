package com.smate.center.batch.model.pdwh.pubimport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TMP_JIANGXI_PUBADDR")
public class TmpPdwhPubAddr implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4714575319800071137L;

  private Long addrId;
  private Long pubId;
  private Integer dbId;
  private Integer pubType;
  private String address;// 成果地址
  private Long addrHash;// 地址hash
  private Integer status;
  private Integer language;// (1英文，2中文)

  public TmpPdwhPubAddr() {
    super();
  }

  public TmpPdwhPubAddr(Long addrId, Long pubId, Integer dbId, Integer pubType, String address, Long addrHash,
      Integer status, Integer language) {
    super();
    this.addrId = addrId;
    this.pubId = pubId;
    this.dbId = dbId;
    this.pubType = pubType;
    this.address = address;
    this.addrHash = addrHash;
    this.status = status;
    this.language = language;
  }

  @Id
  @Column(name = "ADDR_ID")
  public Long getAddrId() {
    return addrId;
  }

  public void setAddrId(Long addrId) {
    this.addrId = addrId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "DB_ID")
  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  @Column(name = "ADDRESS")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Column(name = "ADDR_HASH")
  public Long getAddrHash() {
    return addrHash;
  }

  public void setAddrHash(Long addrHash) {
    this.addrHash = addrHash;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

}
