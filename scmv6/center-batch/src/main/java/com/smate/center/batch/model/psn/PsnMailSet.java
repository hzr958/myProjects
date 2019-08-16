package com.smate.center.batch.model.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PSN_MAIL_SET")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class PsnMailSet implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 4544169043991253724L;
  // Id
  private Long id;
  // personId
  private Long psnId;
  // 邮件类型Id
  private Long mailTypeId;
  // 是否接收邮件
  private Long isReceive;
  // 是否发送邮件
  private Long isSend;

  private String ids;

  // 选择接收邮件的语言
  private String languageVersion;

  public PsnMailSet() {

  }

  public PsnMailSet(Long psnId, Long mailTypeId, Long isReceive) {
    super();
    this.psnId = psnId;
    this.mailTypeId = mailTypeId;
    this.isReceive = isReceive;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "seq_psn_mail_set", allocationSize = 1)
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

  @Column(name = "MAIL_TYPE_ID")
  public Long getMailTypeId() {
    return mailTypeId;
  }

  public void setMailTypeId(Long mailTypeId) {
    this.mailTypeId = mailTypeId;
  }

  @Column(name = "ISRECEIVE")
  public Long getIsReceive() {
    return isReceive;
  }

  public void setIsReceive(Long isReceive) {
    this.isReceive = isReceive;
  }

  @Column(name = "ISSEND")
  public Long getIsSend() {
    return isSend;
  }

  public void setIsSend(Long isSend) {
    this.isSend = isSend;
  }

  @Transient
  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  @Transient
  public String getLanguageVersion() {
    return languageVersion;
  }

  public void setLanguageVersion(String languageVersion) {
    this.languageVersion = languageVersion;
  }

}
