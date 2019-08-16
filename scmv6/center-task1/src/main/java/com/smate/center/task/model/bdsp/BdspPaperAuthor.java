package com.smate.center.task.model.bdsp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 论文作者
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_PAPER_AUTHOR")
public class BdspPaperAuthor implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BDSP_PAPER_AUTHOR", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "PSN_NAME")
  private String psnName;
  @Column(name = "INS_ID")
  private Long insId;
  @Column(name = "INS_NAME")
  private String insName;

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


}
