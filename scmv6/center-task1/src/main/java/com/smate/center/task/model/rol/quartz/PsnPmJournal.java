package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户确认过的期刊.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PM_JOURNAL")
public class PsnPmJournal implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3703352701463690748L;
  private Long id;
  // 期刊ID
  private Long jid;
  // 期刊名称
  private String jname;
  // 期刊ISSN
  private String issn;
  // 用户ID
  private Long psnId;
  // 用户确认期刊次数
  private Integer jcount = 1;

  public PsnPmJournal() {
    super();
  }

  public PsnPmJournal(Long jid, String jname, String issn, Long psnId) {
    super();
    this.jid = jid;
    this.jname = jname;
    this.issn = issn;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_JOURNAL", allocationSize = 1)
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

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "JCOUNT")
  public Integer getJcount() {
    return jcount;
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

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setJcount(Integer jcount) {
    this.jcount = jcount;
  }
}
