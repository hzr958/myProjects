package com.smate.center.batch.model.pdwh.pub.isi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * isi成果地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "ISI_PUB_ADDR")
public class IsiPubAddr implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7241478630443206162L;
  // 地址ID
  private Long addrId;
  // 外键
  private Long pubId;
  // 文献地址（全部转成小写）
  private String addr;
  // 成果地址hash
  private Long addrHash;

  public IsiPubAddr() {
    super();
  }

  public IsiPubAddr(Long pubId, String addr, Long addrHash) {
    super();
    this.pubId = pubId;
    this.addr = addr;
    this.addrHash = addrHash;
  }

  @Id
  @Column(name = "ADDR_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ISI_PUB_ADDR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getAddrId() {
    return addrId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "ADDR")
  public String getAddr() {
    return addr;
  }

  @Column(name = "ADDR_HASH")
  public Long getAddrHash() {
    return addrHash;
  }

  public void setAddrHash(Long addrHash) {
    this.addrHash = addrHash;
  }

  public void setAddrId(Long addrId) {
    this.addrId = addrId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

}
