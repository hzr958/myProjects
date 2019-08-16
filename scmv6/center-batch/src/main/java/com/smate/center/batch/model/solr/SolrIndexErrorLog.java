package com.smate.center.batch.model.solr;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SOLR_INDEX_ERROR_LOG")
public class SolrIndexErrorLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5502241340721956282L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_SOLR_INDEX_ERRORLOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "ERROR_MSG")
  private String errorMsg;

  @Column(name = "RECORD_TIME")
  private Date recordTime;

  public SolrIndexErrorLog() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Date getRecordTime() {
    return recordTime;
  }

  public void setRecordTime(Date recordTime) {
    this.recordTime = recordTime;
  }

}
