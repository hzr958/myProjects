package com.smate.center.batch.model.pdwh.pubimport;

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
 * 成果地址匹配
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUB_MADDR")
public class PdwhPubMaddr implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 5909963057954425164L;
  private Long id;
  private Long addrId;
  private Long insId;// 拆分出xml名称对应的ins_id
  private Long pubId;
  private String addr;// isi,ei机构名称有加粗处理
  private Long pdwhInsNameId;// 对应pdwh_ins_name匹配上的Id
  private Integer matched; // 1:匹配上机构别名，2部分匹配上机构别名，3不确认是否是机构别名，4不是机构别名，0不需要匹配（有一个地址已经匹配上了机构，其他的不需要匹配）
  private Long matchedInsId;// 匹配上的机构Id
  // 原始地址
  private String protoAddr;

  public PdwhPubMaddr() {
    super();
  }

  public PdwhPubMaddr(Long addrId, Long insId, Long pubId, String addr, String protoAddr) {
    super();
    this.addrId = addrId;
    this.pubId = pubId;
    this.insId = insId;
    this.addr = addr;
    this.protoAddr = protoAddr;
  }

  @Id
  @Column(name = "Id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_APDWH_PUB_MADDR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "ADDR_ID")
  public Long getAddrId() {
    return addrId;
  }

  public void setAddrId(Long addrId) {
    this.addrId = addrId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "ADDR")
  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  @Column(name = "PDWH_INSNAME_ID")
  public Long getPdwhInsNameId() {
    return pdwhInsNameId;
  }

  public void setPdwhInsNameId(Long pdwhInsNameId) {
    this.pdwhInsNameId = pdwhInsNameId;
  }

  @Column(name = "MATCHED")
  public Integer getMatched() {
    return matched;
  }

  public void setMatched(Integer matched) {
    this.matched = matched;
  }

  @Column(name = "MATCHED_INS_ID")
  public Long getMatchedInsId() {
    return matchedInsId;
  }

  public void setMatchedInsId(Long matchedInsId) {
    this.matchedInsId = matchedInsId;
  }

  @Transient
  public String getProtoAddr() {
    return protoAddr;
  }

  public void setProtoAddr(String protoAddr) {
    this.protoAddr = protoAddr;
  }

}
