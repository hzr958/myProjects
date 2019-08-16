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
 * 产权人（单位）
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_PATENT_UNIT")
public class BdspPatentUnit implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BDSP_PATENT_UNIT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;
  @Column(name = "PUB_ID")
  private Long pubId;
  /**
   * 单位
   */
  @Column(name = "INS_ID")
  private Long insId;
  @Column(name = "INS_NAME")
  private String insName;

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
