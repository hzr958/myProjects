package com.smate.center.open.isis.model.data.isis;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the JOURNAL_CORE database table.
 * 
 */
@Entity
@Table(name = "JOURNAL_CORE")
public class JournalCore implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "CORE_FLAG")
  private Long coreFlag;
  @Column(name = "CSCI")
  private Long csci;
  @Column(name = "ISSN")
  private String issn;

  @Id
  @Column(name = "JID")
  private Long jid;

  @Column(name = "JOURNAL_NAME")
  private String journalName;

  @Column(name = "ZH_CORE")
  private Long zhCore;

  public JournalCore() {}



  public String getIssn() {
    return this.issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }



  public Long getCoreFlag() {
    return coreFlag;
  }



  public void setCoreFlag(Long coreFlag) {
    this.coreFlag = coreFlag;
  }



  public Long getCsci() {
    return csci;
  }



  public void setCsci(Long csci) {
    this.csci = csci;
  }



  public Long getJid() {
    return jid;
  }



  public void setJid(Long jid) {
    this.jid = jid;
  }



  public Long getZhCore() {
    return zhCore;
  }



  public void setZhCore(Long zhCore) {
    this.zhCore = zhCore;
  }



  public String getJournalName() {
    return this.journalName;
  }

  public void setJournalName(String journalName) {
    this.journalName = journalName;
  }


}
