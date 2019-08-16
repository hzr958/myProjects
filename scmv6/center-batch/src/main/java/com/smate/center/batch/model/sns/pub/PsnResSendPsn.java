package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * cwli推荐资源之向谁推荐.
 */
@Entity
@Table(name = "PSN_RES_SEND_PSN")
public class PsnResSendPsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8728599117807962620L;
  private Long id;
  private Long resSendId;
  private PsnResSend resSend;
  private Long receivePsnId;
  private String email;
  private String des3Id;
  private Integer status;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_RES_SEND_PSN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "RECEIVE_PSN_ID")
  public Long getReceivePsnId() {
    return receivePsnId;
  }

  public void setReceivePsnId(Long receivePsnId) {
    this.receivePsnId = receivePsnId;
  }

  @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "RES_SEND_ID", insertable = false, updatable = false)
  @JsonIgnore
  public PsnResSend getResSend() {
    return resSend;
  }

  public void setResSend(PsnResSend resSend) {
    this.resSend = resSend;
  }

  @Column(name = "RES_SEND_ID")
  public Long getResSendId() {
    return resSendId;
  }

  public void setResSendId(Long resSendId) {
    this.resSendId = resSendId;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Transient
  public String getDes3Id() {
    return des3Id;
  }

}
