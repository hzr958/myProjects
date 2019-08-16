package com.smate.center.batch.model.pdwh.pub.cnki;

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
@Table(name = "CNKI_PUB_MATCH_AUTHOR")
public class CnkiPubMatchAuthor implements Serializable {

  private static final long serialVersionUID = 3716867565292514471L;
  private Long id;// 主键.
  private Long pubId;// 成果ID.
  private String name;// 用户名全称(小写).
  private String addr;// 作者地址(小写).
  private Integer seqNo;// 用户序号.
  private Integer authorPos;// 是否通信作者：0-不是；1-是.

  public CnkiPubMatchAuthor() {
    super();
  }

  public CnkiPubMatchAuthor(Long pubId, Integer seqNo, Integer authorPos) {
    super();
    this.pubId = pubId;
    this.seqNo = seqNo;
    this.authorPos = authorPos;
  }

  public CnkiPubMatchAuthor(Long pubId, String name, String addr, Integer seqNo, Integer authorPos) {
    super();
    this.pubId = pubId;
    this.name = name;
    this.addr = addr;
    this.seqNo = seqNo;
    this.authorPos = authorPos;
  }

  public CnkiPubMatchAuthor(Long id, Long pubId, String name, String addr, Integer seqNo, Integer authorPos) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.name = name;
    this.addr = addr;
    this.seqNo = seqNo;
    this.authorPos = authorPos;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNKI_PUB_MATCH_AUTHOR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
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

  public void setName(String name) {
    this.name = name;
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
