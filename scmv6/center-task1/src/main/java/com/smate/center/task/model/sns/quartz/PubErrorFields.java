package com.smate.center.task.model.sns.quartz;

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
 * 成果录入错误数据.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_ERROR_FIELDS")
public class PubErrorFields implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 9009557834705488591L;

  private Long id;
  private String name;
  private Long pubId;
  private Date createAt;
  private Integer errorNo;

  @Id
  @Column(name = "FIELD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ERROR_FIELDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FIELD_NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "CREATE_AT")
  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  @Column(name = "ERROR_NO")
  public Integer getErrorNo() {
    return errorNo;
  }

  public void setErrorNo(Integer errorNo) {
    this.errorNo = errorNo;
  }

}
