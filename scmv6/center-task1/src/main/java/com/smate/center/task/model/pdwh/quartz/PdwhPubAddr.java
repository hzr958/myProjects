package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库成果地址
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUB_ADDR")
public class PdwhPubAddr implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5880699010158022945L;
  private Long addrId;
  private Long pubId;
  private String address;// 成果地址
  private Long addrHash;// 地址hash
  private Integer language;// (1英文，2中文)

  public PdwhPubAddr() {
    super();
  }

  public PdwhPubAddr(Long pubId, String address, Long addrHash, Integer language) {
    super();
    this.pubId = pubId;
    this.address = address;
    this.addrHash = addrHash;
    this.language = language;
  }

  @Id
  @Column(name = "ADDR_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_APDWH_PUB_ADDR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

}
