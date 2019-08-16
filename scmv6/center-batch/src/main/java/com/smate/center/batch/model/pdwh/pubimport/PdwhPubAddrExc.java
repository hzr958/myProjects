package com.smate.center.batch.model.pdwh.pubimport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * 排除的机构匹配地址.
 * 
 * @author zll
 * 
 */
@Entity
@Table(name = "PDWH_PUB_ADDR_EXC")
public class PdwhPubAddrExc implements Serializable {

  private static final long serialVersionUID = 9138980788034111681L;
  private Long id;
  private Long insId;
  // "排除的基准库成果地址 "
  private String addr;
  // 成果地址HASH，用于判断成果地址是否相同
  private Long addrHash;

  public PdwhPubAddrExc() {
    super();
  }

  public PdwhPubAddrExc(Long id, Long insId, Long addrHash) {
    super();
    this.id = id;
    this.insId = insId;
    this.addrHash = addrHash;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_ADDR_EXC", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "ADDR")
  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  @Column(name = "ADDR_HASH")
  public Long getAddrHash() {
    return addrHash;
  }

  public void setAddrHash(Long addrHash) {
    this.addrHash = addrHash;
  }


}
