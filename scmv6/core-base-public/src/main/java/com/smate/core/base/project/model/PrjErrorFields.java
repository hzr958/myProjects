package com.smate.core.base.project.model;

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
 * 项目录入错误数据.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_ERROR_FIELDS")
public class PrjErrorFields implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 9009557834705488591L;

  private Long id;
  private String name;
  private Long prjId;
  private Date createAt;
  private Integer errorNo;

  @Id
  @Column(name = "FIELD_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_ERROR_FIELDS", allocationSize = 1)
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

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
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
