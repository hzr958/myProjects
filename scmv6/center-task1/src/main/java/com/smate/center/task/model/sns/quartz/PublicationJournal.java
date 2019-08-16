package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果期刊信息
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_JOURNAL")
public class PublicationJournal implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6378615680240751917L;

  // 成果ID
  private Long pubId;
  // 成果所有人
  private Long psnId;
  // ISSN
  private String issn;
  // 期刊名
  private String jname;
  // 成果年份
  private Integer pubYear;
  // 期刊基准库ID
  private Long jnlId;

  public PublicationJournal() {
    super();
  }

  public PublicationJournal(Long pubId, Long psnId, String issn, String jname, Long jnlId, Integer pubYear) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.issn = issn;
    this.jname = jname;
    this.jnlId = jnlId;
    this.pubYear = pubYear;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  @Column(name = "JNAME")
  public String getJname() {
    return jname;
  }

  @Column(name = "PUB_YEAR")
  public Integer getPubYear() {
    return pubYear;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setJname(String jname) {
    this.jname = jname;
  }

}
