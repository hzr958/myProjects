package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "NSFC_LAB_GROUP")
public class NsfcLabGroup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7472787245069623877L;
  // Fields
  private Long id;
  private Long labId;
  private Long groupId;
  private Long labPsnId;
  private Integer status = 0;
  private Integer year;
  private String labPrpName;

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_NSFC_LAB_GROUP", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the labId
   */
  @Column(name = "LAB_ID")
  public Long getLabId() {
    return labId;
  }

  /**
   * @param labId the labId to set
   */
  public void setLabId(Long labId) {
    this.labId = labId;
  }

  /**
   * @return the groupId
   */
  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the labPsnId
   */
  @Column(name = "LAB_PSN_ID")
  public Long getLabPsnId() {
    return labPsnId;
  }

  /**
   * @param labPsnId the labPsnId to set
   */
  public void setLabPsnId(Long labPsnId) {
    this.labPsnId = labPsnId;
  }

  /**
   * @return the status
   */
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the year
   */
  @Column(name = "YEAR")
  public Integer getYear() {
    return year;
  }

  /**
   * @param year the year to set
   */
  public void setYear(Integer year) {
    this.year = year;
  }

  /**
   * @return the labPrpName
   */
  @Column(name = "LAB_PRP_NAME")
  public String getLabPrpName() {
    return labPrpName;
  }

  /**
   * @param labPrpName the labPrpName to set
   */
  public void setLabPrpName(String labPrpName) {
    this.labPrpName = labPrpName;
  }

}
