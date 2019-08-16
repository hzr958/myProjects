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
 * IRIS业务系统与SNS用户关联的验证码.
 * 
 * @author pwl
 * 
 */
@Entity
@Table(name = "IRIS_EXCLUDED_PUB")
public class IrisExcludedPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2498480450462635765L;

  private Long id;
  private Long pubId;
  private String uuid;

  public IrisExcludedPub() {}

  public IrisExcludedPub(Long pubId, String uuid) {
    this.pubId = pubId;
    this.uuid = uuid;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_IRIS_EXCLUDED_PUB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "UUID")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

}
