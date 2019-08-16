package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * PUBMED成果匹配-成果指派拆分成果期刊名称.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_ASSIGN_PUBMEDJOURNAL")
public class PubAssignPubMedJournal implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3721281744916972308L;

  private Long id;
  // 期刊ID
  private Long jid;
  // 期刊名称(小写)
  private String jname;
  // 期刊ISSN
  private String issn;
  // 成果ID
  private Long pubId;
  // 成果所属单位ID
  private Long pubInsId;

  public PubAssignPubMedJournal() {
    super();
  }

  public PubAssignPubMedJournal(Long jid, String jname, String issn, Long pubId, Long pubInsId) {
    super();
    this.jid = jid;
    this.jname = jname;
    this.issn = issn;
    this.pubId = pubId;
    this.pubInsId = pubInsId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_PUBMEDJOURNAL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "JID")
  public Long getJid() {
    return jid;
  }

  @Column(name = "JNAME")
  public String getJname() {
    return jname;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUB_INS_ID")
  public Long getPubInsId() {
    return pubInsId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public void setJname(String jname) {
    this.jname = jname;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubInsId(Long pubInsId) {
    this.pubInsId = pubInsId;
  }

}
