package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 存储基准库的分类成果
 * 
 * 
 */
@Entity
@Table(name = "PUB_CATEGORY_NSFC_BY_JOURNAL")
public class PubCategoryNsfcByJournal implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6096011572337110449L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_FUND_AGENCY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long Id;

  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "NSFC_CATEGORY_ID")
  private String nsfcCategoryId;

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getNsfcCategoryId() {
    return nsfcCategoryId;
  }

  public void setNsfcCategoryId(String nsfcCategoryId) {
    this.nsfcCategoryId = nsfcCategoryId;
  }

}
