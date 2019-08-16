package com.smate.center.batch.model.rcmd.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 人员参考文献期刊数据(用于阅读的期刊统计).
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_JOURNAL_REFC")
public class RcmdPsnJournalRefc implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6833317413008915024L;
  private Long id;
  // 成果所有人
  private Long psnId;
  // ISSN
  private String issn;
  // ISSN文本
  private String issnTxt;

  public RcmdPsnJournalRefc() {
    super();
  }

  public RcmdPsnJournalRefc(Long psnId, String issn, String issnTxt) {
    super();
    this.psnId = psnId;
    this.issn = issn;
    this.issnTxt = issnTxt;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_JOURNAL_REFC", allocationSize = 1)
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

  @Column(name = "ISSN_TXT")
  public String getIssnTxt() {
    return issnTxt;
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

  public void setIssnTxt(String issnTxt) {
    this.issnTxt = issnTxt;
  }

}
