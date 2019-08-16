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
 * 人员类别
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_RESEARCH_PSN_CATEGORY")
public class BdspResearchPsnCategory implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 人员id
   */
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BDSP_RESEARCH_PSN_CATEGORY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "SMATE_ID")
  private Long smateId;
  @Column(name = "MOE_ID")
  private Long moeId;
  @Column(name = "NSFC_ID")
  private Long nsfcId;
  @Column(name = "MOST_ID")
  private Long mostId;
  @Column(name = "SEI_ID")
  private Long seiId;
  @Column(name = "TYPE_ID")
  private Integer typeId;
  @Column(name = "FROM_ID")
  private Long fromId;


  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public Long getFromId() {
    return fromId;
  }

  public void setFromId(Long fromId) {
    this.fromId = fromId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getSmateId() {
    return smateId;
  }

  public void setSmateId(Long smateId) {
    this.smateId = smateId;
  }

  public Long getMoeId() {
    return moeId;
  }

  public void setMoeId(Long moeId) {
    this.moeId = moeId;
  }

  public Long getNsfcId() {
    return nsfcId;
  }

  public void setNsfcId(Long nsfcId) {
    this.nsfcId = nsfcId;
  }

  public Long getMostId() {
    return mostId;
  }

  public void setMostId(Long mostId) {
    this.mostId = mostId;
  }

  public Long getSeiId() {
    return seiId;
  }

  public void setSeiId(Long seiId) {
    this.seiId = seiId;
  }

}
