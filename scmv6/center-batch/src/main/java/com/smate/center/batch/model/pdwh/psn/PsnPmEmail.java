package com.smate.center.batch.model.pdwh.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户邮件表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_EMAIL")
public class PsnPmEmail implements Serializable {

  private static final long serialVersionUID = -5380648019927209260L;
  private Long id;// 主键.
  private Long psnId;// 人员ID.
  private String email;// 邮件地址.

  public PsnPmEmail() {
    super();
  }

  public PsnPmEmail(Long id, Long psnId, String email) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.email = email;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_EMAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
