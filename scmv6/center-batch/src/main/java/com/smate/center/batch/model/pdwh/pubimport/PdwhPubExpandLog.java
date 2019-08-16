package com.smate.center.batch.model.pdwh.pubimport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库成果扩展日志表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUB_EXPAND_LOG")
public class PdwhPubExpandLog implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -2738062848081884517L;
  private Long id;
  private Long pubId;
  private Integer status;// 拆分状态0-未处理；1-成功；2-失败；-1-作者数超过30个，不拆分
  private String errLog;// 错误信息

  public PdwhPubExpandLog() {
    super();
    // TODO Auto-generated constructor stub
  }

  @Id
  @Column(name = "Id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_EXPAND_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "STAUTS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "ERROR_LOG")
  public String getErrLog() {
    return errLog;
  }

  public void setErrLog(String errLog) {
    this.errLog = errLog;
  }

}
