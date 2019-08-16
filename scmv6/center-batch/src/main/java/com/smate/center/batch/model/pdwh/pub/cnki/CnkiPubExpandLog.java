package com.smate.center.batch.model.pdwh.pub.cnki;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CNKI成果拆分日志表实体_MJG.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "CNKI_PUB_EXPAND_LOG")
public class CnkiPubExpandLog implements Serializable {

  private static final long serialVersionUID = -7506922244576579839L;
  private Long id;
  private Long pubId;
  private int status;
  private String errLog;

  public CnkiPubExpandLog() {
    super();
  }

  public CnkiPubExpandLog(Long id, Long pubId, int status, String errLog) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.status = status;
    this.errLog = errLog;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CNKI_PUB_EXPAND_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "STATUS")
  public int getStatus() {
    return status;
  }

  @Column(name = "ERR_LOG")
  public String getErrLog() {
    return errLog;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setErrLog(String errLog) {
    this.errLog = errLog;
  }
}
