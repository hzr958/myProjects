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
 * 成果期刊匹配表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "CNKI_PUB_MATCH_JOURNAL")
public class CnkiPubMatchJournal implements Serializable {

  private static final long serialVersionUID = -648371704514839812L;
  private Long pubId;// 成果ID.
  private String jName;// 期刊名称.
  private String issn;// ISSN.
  private Long issnHash;// ISSN的Hash值.
  private Long id;// 主键.

  public CnkiPubMatchJournal() {
    super();
  }

  public CnkiPubMatchJournal(Long pubId, String jName, String issn, Long issnHash) {
    super();
    this.pubId = pubId;
    this.jName = jName;
    this.issn = issn;
    this.issnHash = issnHash;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNKI_PUB_MATCH_JOURNAL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "JNAME")
  public String getjName() {
    return jName;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  @Column(name = "ISSN_HASH")
  public Long getIssnHash() {
    return issnHash;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setjName(String jName) {
    this.jName = jName;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setIssnHash(Long issnHash) {
    this.issnHash = issnHash;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
