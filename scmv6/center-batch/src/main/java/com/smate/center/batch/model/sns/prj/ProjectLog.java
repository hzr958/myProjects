package com.smate.center.batch.model.sns.prj;

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
@Table(name = "LOG_PRJ")
public class ProjectLog implements java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2175029504952508659L;
  private Long id;
  private Long prjId;
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
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_LOG_PRJ", allocationSize = 1)
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

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
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
