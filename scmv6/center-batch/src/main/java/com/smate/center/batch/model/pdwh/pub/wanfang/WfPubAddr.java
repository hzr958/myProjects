package com.smate.center.batch.model.pdwh.pub.wanfang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 万方成果地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "WF_PUB_ADDR")
public class WfPubAddr implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2472252595443346437L;
  // 地址ID
  private Long addrId;
  // 外键
  private Long pubId;
  // 文献地址（全部转成小写）
  private String addr;

  public WfPubAddr() {
    super();
  }

  public WfPubAddr(Long pubId, String addr) {
    super();
    this.pubId = pubId;
    this.addr = addr;
  }

  @Id
  @Column(name = "ADDR_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_WF_PUB_ADDR", allocationSize = 1)
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
