package com.smate.center.batch.model.pdwh.pub.sps;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SCOPUS排除的机构匹配地址.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SPS_PUB_ADDR_EXC")
public class SpsPubAddrExc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7060756185979788633L;
  private Long id;
  private Long insId;
  // "排除的scopus成果地址 "
  private String addr;
  // 成果地址HASH，用于判断成果地址是否相同
  private Long addrHash;

  public SpsPubAddrExc() {
    super();
  }

  public SpsPubAddrExc(Long id, Long insId, Long addrHash) {
    super();
    this.id = id;
    this.insId = insId;
    this.addrHash = addrHash;
  }

  public SpsPubAddrExc(Long insId, String addr, Long addrHash) {
    super();
    this.insId = insId;
    this.addr = addr;
    this.addrHash = addrHash;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SPS_PUB_ADDR_EXC", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "ADDR")
  public String getAddr() {
    return addr;
  }

  @Column(name = "ADDR_HASH")
  public Long getAddrHash() {
    return addrHash;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  public void setAddrHash(Long addrHash) {
    this.addrHash = addrHash;
  }

}
