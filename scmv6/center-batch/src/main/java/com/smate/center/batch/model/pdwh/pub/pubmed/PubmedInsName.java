package com.smate.center.batch.model.pdwh.pub.pubmed;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 单位pubmed别名表.
 * 
 * @author linyueqin
 * 
 */
@Entity
@Table(name = "PUBMED_INS_NAME")
public class PubmedInsName implements Serializable {

  private static final long serialVersionUID = 4252446012122521856L;
  private Long id;
  private Long insId;
  private String pubmedName;
  private Integer pubmednLength;
  private Integer freq = 0;
  private Date lastUse;

  public PubmedInsName() {
    super();
  }

  public PubmedInsName(Long id, Long insId, String pubmedName, Integer pubmednLength) {
    super();
    this.id = id;
    this.insId = insId;
    this.pubmedName = pubmedName;
    this.pubmednLength = pubmednLength;
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "PUBMED_NAME")
  public String getPubmedName() {
    return pubmedName;
  }

  public void setPubmedName(String pubmedName) {
    this.pubmedName = pubmedName;
  }

  @Column(name = "PUBMEDN_LENGTH")
  public Integer getPubmednLength() {
    return pubmednLength;
  }

  public void setPubmednLength(Integer pubmednLength) {
    this.pubmednLength = pubmednLength;
  }

  @Column(name = "FREQ")
  public Integer getFreq() {
    return freq;
  }

  public void setFreq(Integer freq) {
    this.freq = freq;
  }

  @Column(name = "LAST_USE")
  public Date getLastUse() {
    return lastUse;
  }

  public void setLastUse(Date lastUse) {
    this.lastUse = lastUse;
  }

}
