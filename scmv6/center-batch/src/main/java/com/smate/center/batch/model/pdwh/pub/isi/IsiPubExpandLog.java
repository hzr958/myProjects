package com.smate.center.batch.model.pdwh.pub.isi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * ISI成果拆分日志表实体_MJG.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "ISI_PUB_EXPAND_LOG")
public class IsiPubExpandLog implements Serializable {

  private static final long serialVersionUID = -5170655190233695577L;
  private Long id;
  private Long pubId;
  private int status;
  private String errLog;

  public IsiPubExpandLog() {
    super();
    // TODO 自动生成的构造函数存根
  }

  public IsiPubExpandLog(Long id, Long pubId, int status, String errLog) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.status = status;
    this.errLog = errLog;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ISI_PUB_EXPAND_LOG", allocationSize = 1)
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
