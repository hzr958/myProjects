package com.smate.center.task.v8pub.pdwh.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_DOI_BAK")
public class PdwhPubDoiBakPO implements Serializable {

  private static final long serialVersionUID = 2388644880725985834L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "DOI")
  private String doi;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "DOI_URL")
  private String doiUrl;

  @Column(name = "FORMAT_DOI")
  private String formatDoi;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDoiUrl() {
    return doiUrl;
  }

  public void setDoiUrl(String doiUrl) {
    this.doiUrl = doiUrl;
  }

  public String getFormatDoi() {
    return formatDoi;
  }

  public void setFormatDoi(String formatDoi) {
    this.formatDoi = formatDoi;
  }


}
