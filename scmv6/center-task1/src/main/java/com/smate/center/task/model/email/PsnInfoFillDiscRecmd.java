package com.smate.center.task.model.email;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PSN_FILL_DISC_RECOMMEND")
public class PsnInfoFillDiscRecmd implements Serializable {

  /**
   * 人员信息补充关键词推荐
   */
  private static final long serialVersionUID = -4169864472025241339L;

  private Long id;
  private Long psnId;
  private Integer discId;;
  private String discName;
  private String discEnName;
  private Integer status;
  private Date updateDate;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FILL_disc_RECOMMEND", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "disc_id")
  public Integer getDiscId() {
    return discId;
  }

  public void setDiscId(Integer discId) {
    this.discId = discId;
  }

  @Column(name = "disc_name")
  public String getDiscName() {
    return discName;
  }

  public void setDiscName(String discName) {
    this.discName = discName;
  }

  @Column(name = "disc_enname")
  public String getDiscEnName() {
    return discEnName;
  }

  public void setDiscEnName(String discEnName) {
    this.discEnName = discEnName;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "udate")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
