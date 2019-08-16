package com.smate.sie.core.base.utils.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 专利xml
 * 
 * @author ztt
 *
 */
@Entity
@Table(name = "PAT_XML")
public class SiePatXml implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 762201648209764503L;
  private Long patId;
  private String patXml;

  public SiePatXml() {
    super();
  }

  public SiePatXml(Long patId) {
    super();
    this.patId = patId;
  }

  public SiePatXml(Long patId, String patXml) {
    super();
    this.patId = patId;
    this.patXml = patXml;
  }

  @Id
  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  @Column(name = "PAT_XML")
  public String getPatXml() {
    return patXml;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  public void setPatXml(String patXml) {
    this.patXml = patXml;
  }
}
