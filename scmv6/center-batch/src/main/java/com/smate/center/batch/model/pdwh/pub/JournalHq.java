package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CSSCI核心期刊跟ISI的期刊.
 * 
 * @author lichangwne
 * 
 */
@Entity
@Table(name = "JOURNAL_HQ")
public class JournalHq implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -7738498057437435505L;
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "ISSN")
  private String issn;
  @Column(name = "ISSN_TXT")
  private String issnTxt;
  @Column(name = "ISI")
  private Integer isi;
  @Column(name = "CSCD")
  private Integer cscd;
  @Column(name = "CSSCI")
  private Integer cssci;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public String getIssnTxt() {
    return issnTxt;
  }

  public void setIssnTxt(String issnTxt) {
    this.issnTxt = issnTxt;
  }

  public Integer getIsi() {
    return isi;
  }

  public void setIsi(Integer isi) {
    this.isi = isi;
  }

  public Integer getCscd() {
    return cscd;
  }

  public void setCscd(Integer cscd) {
    this.cscd = cscd;
  }

  public Integer getCssci() {
    return cssci;
  }

  public void setCssci(Integer cssci) {
    this.cssci = cssci;
  }

}
