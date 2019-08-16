package com.smate.center.batch.model.pdwh.pub.cnipr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * CNIPR成果地址匹配结果.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNIPR_PUB_MADDR")
public class CniprPubMaddr implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2383200930974286598L;
  private Long maddrId;
  private Long addrId;
  private Long insId;
  // 成果ID
  private Long pubId;
  // 匹配上的机构别名ID
  private Long cniprNameId;
  // 是否匹配上机构，0/1
  private Integer matched;
  // 匹配上的机构ID
  private Long minsId;
  private String protoAddr;

  public CniprPubMaddr() {
    super();
  }

  public CniprPubMaddr(Long maddrId, Long addrId, Long insId, Long pubId, Integer matched, String protoAddr) {
    super();
    this.maddrId = maddrId;
    this.addrId = addrId;
    this.insId = insId;
    this.pubId = pubId;
    this.matched = matched;
    this.protoAddr = protoAddr;
  }

  public CniprPubMaddr(Long addrId, Long insId, Long pubId, String protoAddr) {
    super();
    this.addrId = addrId;
    this.insId = insId;
    this.pubId = pubId;
    this.protoAddr = protoAddr;
  }

  @Id
  @Column(name = "MADDR_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNIPR_PUB_MADDR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getMaddrId() {
    return maddrId;
  }

  @Column(name = "ADDR_ID")
  public Long getAddrId() {
    return addrId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "MATCHED")
  public Integer getMatched() {
    return matched;
  }

  @Column(name = "MINS_ID")
  public Long getMinsId() {
    return minsId;
  }

  @Column(name = "CNIPR_NAME_ID")
  public Long getCniprNameId() {
    return cniprNameId;
  }

  public void setCniprNameId(Long cniprNameId) {
    this.cniprNameId = cniprNameId;
  }

  @Transient
  public String getProtoAddr() {
    return protoAddr;
  }

  public void setProtoAddr(String protoAddr) {
    this.protoAddr = protoAddr;
  }

  public void setMinsId(Long minsId) {
    this.minsId = minsId;
  }

  public void setMaddrId(Long maddrId) {
    this.maddrId = maddrId;
  }

  public void setAddrId(Long addrId) {
    this.addrId = addrId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setMatched(Integer matched) {
    this.matched = matched;
  }

}
