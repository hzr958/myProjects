package com.smate.center.open.model.snscode;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * IRIS业务系统与SNS用户关联的验证码.
 * 
 * @author pwl
 * 
 */
@Entity
@Table(name = "IRIS_SNS_CODE")
public class IrisSnsCode implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2498480450462635765L;

  private Long id;
  private Long psnId;
  private String guid;
  private Long insId;
  private String code;
  private Date createdDate;

  public IrisSnsCode() {}

  public IrisSnsCode(Long psnId, String guid) {
    this.psnId = psnId;
    this.guid = guid;
  }

  public IrisSnsCode(Long psnId, String guid, Long insId) {
    this.psnId = psnId;
    this.guid = guid;
    this.insId = insId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_IRIS_SNS_CODE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "GUID")
  public String getGuid() {
    return guid;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  @Column(name = "CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(name = "CREATED_DATE")
  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

}
