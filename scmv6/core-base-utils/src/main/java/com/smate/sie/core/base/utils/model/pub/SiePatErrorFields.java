package com.smate.sie.core.base.utils.model.pub;

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
 * 专利录入错误数据
 * 
 * @author jszhou
 *
 */
@Entity
@Table(name = "PAT_ERROR_FIELDS")
public class SiePatErrorFields implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2137667574775691532L;
  private Long id;
  private String name;
  private Long patId;
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

  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
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
