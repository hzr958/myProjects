package com.smate.web.psn.model.rol;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户确认email.
 * 
 * @author liqinghua
 */
@Entity
@Table(name = "PSN_PM_EMAIL")
public class PsnPmEmail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6780832469644472919L;

  private Long id;
  // 用户EMAIL
  private String email;
  // 用户ID
  private Long psnId;

  public PsnPmEmail() {
    super();
  }

  public PsnPmEmail(String email, Long psnId) {
    super();
    this.email = email;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_EMAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
