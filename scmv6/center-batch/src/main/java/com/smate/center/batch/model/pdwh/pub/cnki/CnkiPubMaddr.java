package com.smate.center.batch.model.pdwh.pub.cnki;

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
 * cnki成果地址匹配结果.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "CNKI_PUB_MADDR")
public class CnkiPubMaddr implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5488979772098065259L;

  private Long maddrId;
  private Long addrId;
  private Long insId;
  // 成果ID
  private Long pubId;
  // 匹配上的机构别名ID
  private Long cnkiNameId;
  // 是否匹配上机构，0/1
  private Integer matched;
  // 匹配上的机构ID
  private Long minsId;
  private String protoAddr;

  public CnkiPubMaddr() {
    super();
  }

  public CnkiPubMaddr(Long maddrId, Long addrId, Long insId, Long pubId, Integer matched, String protoAddr) {
    super();
    this.maddrId = maddrId;
    this.addrId = addrId;
    this.insId = insId;
    this.pubId = pubId;
    this.matched = matched;
    this.protoAddr = protoAddr;
  }

  public CnkiPubMaddr(Long addrId, Long insId, Long pubId, String protoAddr) {
    super();
    this.addrId = addrId;
    this.insId = insId;
    this.pubId = pubId;
    this.protoAddr = protoAddr;
  }

  @Id
  @Column(name = "MADDR_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNKI_PUB_MADDR", allocationSize = 1)
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

  @Column(name = "CNKI_NAME_ID")
  public Long getCnkiNameId() {
    return cnkiNameId;
  }

  @Column(name = "MATCHED")
  public Integer getMatched() {
    return matched;
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

  public void setCnkiNameId(Long cnkiNameId) {
    this.cnkiNameId = cnkiNameId;
  }

  public void setMatched(Integer matched) {
    this.matched = matched;
  }

}
