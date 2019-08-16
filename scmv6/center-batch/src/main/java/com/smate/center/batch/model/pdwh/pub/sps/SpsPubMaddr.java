package com.smate.center.batch.model.pdwh.pub.sps;

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
 * scopus成果地址匹配结果.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SPS_PUB_MADDR")
public class SpsPubMaddr implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2717297586506599712L;
  private Long maddrId;
  private Long addrId;
  private Long insId;
  // 成果ID
  private Long pubId;
  // 匹配或部分匹配上的成果地址，带加粗字符
  private String addr;
  // 匹配上的机构别名ID
  private Long spsNameId;
  // 1:匹配上机构别名，2部分匹配上机构别名，3不确认是否是机构别名，4不是机构别名，0不需要匹配（有一个地址已经匹配上了机构，其他的不需要匹配）
  private Integer matched;
  // 匹配上的机构ID
  private Long minsId;
  // 原始地址
  private String protoAddr;

  public SpsPubMaddr() {
    super();
  }

  public SpsPubMaddr(Long maddrId, Long addrId, Long insId, Long pubId, String addr, Integer matched,
      String protoAddr) {
    super();
    this.maddrId = maddrId;
    this.addrId = addrId;
    this.insId = insId;
    this.pubId = pubId;
    this.addr = addr;
    this.matched = matched;
    this.protoAddr = protoAddr;
  }

  public SpsPubMaddr(Long addrId, Long insId, Long pubId, String addr, String protoAddr) {
    super();
    this.addrId = addrId;
    this.insId = insId;
    this.pubId = pubId;
    this.addr = addr;
    this.protoAddr = protoAddr;
  }

  @Id
  @Column(name = "MADDR_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SPS_PUB_MADDR", allocationSize = 1)
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

  @Column(name = "ADDR")
  public String getAddr() {
    return addr;
  }

  @Column(name = "SPS_NAME_ID")
  public Long getSpsNameId() {
    return spsNameId;
  }

  @Column(name = "MINS_ID")
  public Long getMinsId() {
    return minsId;
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

  public void setAddr(String addr) {
    this.addr = addr;
  }

  public void setSpsNameId(Long spsNameId) {
    this.spsNameId = spsNameId;
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
