package com.smate.center.batch.model.pdwh.pub.isi;

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
 * 成果人员邮件通知记录表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "ISI_PUB_MAIL")
public class IsiPubMail implements Serializable {

  private static final long serialVersionUID = -2774463234172947127L;
  private Long id;// 地址ID.
  private Long pubId;// 成果ID.
  private Long psnId;// 人员ID.
  private Date mailAt;// 邮件通知时间.

  public IsiPubMail() {
    super();
  }

  public IsiPubMail(Long id, Long pubId, Long psnId, Date mailAt) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.psnId = psnId;
    this.mailAt = mailAt;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ISI_PUB_MAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "MAIL_AT")
  public Date getMailAt() {
    return mailAt;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setMailAt(Date mailAt) {
    this.mailAt = mailAt;
  }

}
