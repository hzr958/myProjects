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
 * 成果作者匹配表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "ISI_PUB_MATCH_AUTHOR")
public class IsiPubMatchAuthor implements Serializable {

  private static final long serialVersionUID = -403849377183947630L;
  private Long id;// 主键.
  private Long pubId;// 成果ID.
  private String initName;// 用户名简称(小写).
  private String fullName;// 用户名全称(小写).
  private String prefixName;// 用户名简称前缀(小写).
  private String addr;// 作者地址(小写).
  private Integer seqNo;// 用户序号.
  private Integer authorPos;// 通信作者.

  public IsiPubMatchAuthor() {
    super();
  }

  public IsiPubMatchAuthor(Long pubId, String initName, String fullName, String prefixName, String addr, Integer seqNo,
      Integer authorPos) {
    super();
    this.pubId = pubId;
    this.initName = initName;
    this.fullName = fullName;
    this.prefixName = prefixName;
    this.addr = addr;
    this.seqNo = seqNo;
    this.authorPos = authorPos;
  }

  public IsiPubMatchAuthor(Long id, Long pubId, String initName, String fullName, String prefixName, String addr,
      Integer seqNo, Integer authorPos) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.initName = initName;
    this.fullName = fullName;
    this.prefixName = prefixName;
    this.addr = addr;
    this.seqNo = seqNo;
    this.authorPos = authorPos;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ISI_PUB_MATCH_AUTHOR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "INIT_NAME")
  public String getInitName() {
    return initName;
  }

  @Column(name = "FULL_NAME")
  public String getFullName() {
    return fullName;
  }

  @Column(name = "PREFIX_NAME")
  public String getPrefixName() {
    return prefixName;
  }

  @Column(name = "ADDR")
  public String getAddr() {
    return addr;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  @Column(name = "AUTHOR_POS")
  public Integer getAuthorPos() {
    return authorPos;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setInitName(String initName) {
    this.initName = initName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setPrefixName(String prefixName) {
    this.prefixName = prefixName;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setAuthorPos(Integer authorPos) {
    this.authorPos = authorPos;
  }

}
