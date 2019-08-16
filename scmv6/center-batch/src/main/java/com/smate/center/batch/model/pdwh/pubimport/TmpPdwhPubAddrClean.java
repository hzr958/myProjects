package com.smate.center.batch.model.pdwh.pubimport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TMP_JIANGXI_PUBADDR_CLEAN")
public class TmpPdwhPubAddrClean implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4714575319800071137L;

  private Long id;
  private Long addrId;
  private Long pubId;
  private String address;// 成果地址
  private Long addrHash;// 地址hash
  private String address1;// 成果地址
  private String address2;// 成果地址

  public TmpPdwhPubAddrClean() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ISI_PUB_MADDR", allocationSize = 1)
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

  @Column(name = "ADDRESS_HASH")
  public Long getAddrHash() {
    return addrHash;
  }

  public void setAddrHash(Long addrHash) {
    this.addrHash = addrHash;
  }

  @Column(name = "ADDRESS1")
  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  @Column(name = "ADDRESS2")
  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

}
