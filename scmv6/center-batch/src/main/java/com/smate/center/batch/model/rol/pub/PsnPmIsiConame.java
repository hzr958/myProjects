package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户确认成果合作者.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PM_ISICONAME")
public class PsnPmIsiConame implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8446557778277170920L;

  private Long id;
  // 用户名简称(小写)
  private String initName;
  // 用户名全称(小写)
  private String fullName;
  // 用户ID
  private Long psnId;

  public PsnPmIsiConame() {
    super();
  }

  public PsnPmIsiConame(String initName, String fullName, Long psnId) {
    super();
    this.initName = initName;
    this.fullName = fullName;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_ISICONAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "INIT_NAME")
  public String getInitName() {
    return initName;
  }

  @Column(name = "FULL_NAME")
  public String getFullName() {
    return fullName;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setInitName(String initName) {
    this.initName = initName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
