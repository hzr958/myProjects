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
 * 用户合作者邮件表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_CO_EMAIL")
public class PsnPmCoEmail implements Serializable {

  private static final long serialVersionUID = -2241013161597206377L;
  private Long id;// 主键.
  private Long psnId;// 人员ID.
  private String coEmail;// 合作者邮件.

  public PsnPmCoEmail() {
    super();
  }

  public PsnPmCoEmail(Long psnId, String coEmail) {
    super();
    this.psnId = psnId;
    this.coEmail = coEmail;
  }

  public PsnPmCoEmail(Long id, Long psnId, String coEmail) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.coEmail = coEmail;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_CO_EMAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "CO_EMAIL")
  public String getCoEmail() {
    return coEmail;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setCoEmail(String coEmail) {
    this.coEmail = coEmail;
  }

}
