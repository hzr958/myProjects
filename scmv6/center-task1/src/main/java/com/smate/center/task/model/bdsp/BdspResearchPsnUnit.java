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
 * 人员单位
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_RESEARCH_PSN_UNIT")
public class BdspResearchPsnUnit implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BDSP_RESEARCH_PSN_UNIT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;
  /**
   * 人员id
   */
  @Column(name = "PSN_ID")
  private Long psnId;
  /**
   * 单位id
   */
  @Column(name = "INS_ID")
  private Long insId;

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
