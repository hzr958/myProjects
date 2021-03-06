package com.smate.center.batch.model.sns.pub;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author yamingd 成果日志.
 */
@Entity
@Table(name = "LOG_PUB")
public class PublicationLog implements java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6147487188524629014L;

  private Long id;
  private Long pubId;
  private Long opPsnId;
  private Date opDate;
  private Integer opAction;
  private String opDetail;
  private Long insId;

  /**
   * @return the id
   */
  @Id
  @Column(name = "LOG_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_LOG_PUB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the pubId
   */
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  /**
   * @param pubId the pubId to set
   */
  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  /**
   * @return the opPsnId
   */
  @Column(name = "OP_PSN_ID")
  public Long getOpPsnId() {
    return opPsnId;
  }

  /**
   * @param opPsnId the opPsnId to set
   */
  public void setOpPsnId(Long opPsnId) {
    this.opPsnId = opPsnId;
  }

  /**
   * @return the opDate
   */
  @Column(name = "OP_DATE")
  public Date getOpDate() {
    return opDate;
  }

  /**
   * @param opDate the opDate to set
   */
  public void setOpDate(Date opDate) {
    this.opDate = opDate;
  }

  /**
   * @return the opAction
   */
  @Column(name = "OP_ACTION")
  public Integer getOpAction() {
    return opAction;
  }

  /**
   * @param opAction the opAction to set
   */
  public void setOpAction(Integer opAction) {
    this.opAction = opAction;
  }

  /**
   * @return the opDetail
   */
  @Column(name = "OP_DETAIL")
  public String getOpDetail() {
    return opDetail;
  }

  /**
   * @param opDetail the opDetail to set
   */
  public void setOpDetail(String opDetail) {
    this.opDetail = opDetail;
  }

  /**
   * @return the insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }
}
