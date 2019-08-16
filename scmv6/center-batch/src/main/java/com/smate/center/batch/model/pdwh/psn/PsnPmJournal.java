package com.smate.center.batch.model.pdwh.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户使用过的期刊表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_JOURNAL")
public class PsnPmJournal implements Serializable {

  private static final long serialVersionUID = 1170364305325557407L;
  private Long id;// 主键.
  private Long psnId;// 人员ID.
  private String issn;// 期刊ISSN(小写).
  private Long issnHash;// ISSN Hash.

  public PsnPmJournal() {
    super();
  }

  public PsnPmJournal(Long id, Long psnId, String issn, Long issnHash) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.issn = issn;
    this.issnHash = issnHash;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_JOURNAL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  @Column(name = "ISSN_HASH")
  public Long getIssnHash() {
    return issnHash;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setIssnHash(Long issnHash) {
    this.issnHash = issnHash;
  }

}
