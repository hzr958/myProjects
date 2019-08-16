package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_SUBMISSION")
public class PubRolSubmission implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8280437389254406087L;

  /**
   * 主键.
   */
  private Long id;
  /**
   * 提交人.
   */
  private Long psnId;
  /**
   * 提交的成果ID.
   */
  private Long submitPubId;
  /**
   * 提交到的单位ID.
   */
  private Long insId;
  /**
   * 提交日期.
   */
  private Date submitDate;
  /**
   * 提交的状态.
   */
  private Integer submitStatus;
  /**
   * 提交人.
   */
  private Long submitPsnId;
  /**
   * 单位成果ID,提交时拆分出来的成果ID,R在提交成果界面看到的成果ID.
   */
  private PublicationRol insPub;
  /**
   * 单位成果ID,批准后RO看到的记录ID（如有合并操作的话，则为合并后的ID,如没有则为ins_pub_id的值）.
   */
  private PublicationRol realInsPub;
  /**
   * 申请撤销日期.
   */
  private Date withdrawReqDate;
  /**
   * 单位对申请撤销的确认/拒绝日期.
   */
  private Date withdrawReqConfirmDate;
  /**
   * 单位对提交的确认/拒绝日期.
   */
  private Date insConfirmDate;
  /**
   * 个人重新提交日期.
   */
  private Date reSubmitDate;
  /**
   * 申请撤销请求的状态(0/1/2).
   */
  private Integer withdrawStatus;

  /**
   * @return the id
   */
  @Id
  @Column(name = "SUBMIT_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_SUBMISSION", allocationSize = 1)
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
   * 成果拥有人.
   * 
   * @return the psnId
   */
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the submitPubId
   */
  @Column(name = "SUBMIT_PUB_ID")
  public Long getSubmitPubId() {
    return submitPubId;
  }

  /**
   * @param submitPubId the submitPubId to set
   */
  public void setSubmitPubId(Long submitPubId) {
    this.submitPubId = submitPubId;
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

  /**
   * @return the submitDate
   */
  @Column(name = "SUBMIT_DATE")
  public Date getSubmitDate() {
    return submitDate;
  }

  /**
   * @param submitDate the submitDate to set
   */
  public void setSubmitDate(Date submitDate) {
    this.submitDate = submitDate;
  }

  /**
   * @return the submitStatus
   */
  @Column(name = "SUBMIT_STATUS")
  public Integer getSubmitStatus() {
    return submitStatus;
  }

  /**
   * @param submitStatus the submitStatus to set
   */
  public void setSubmitStatus(Integer submitStatus) {
    this.submitStatus = submitStatus;
  }

  /**
   * @return the submitPsnId
   */
  @Column(name = "SUBMIT_PSN_ID")
  public Long getSubmitPsnId() {
    return submitPsnId;
  }

  /**
   * @param submitPsnId the submitPsnId to set
   */
  public void setSubmitPsnId(Long submitPsnId) {
    this.submitPsnId = submitPsnId;
  }

  @OneToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "INS_PUB_ID", referencedColumnName = "PUB_ID")
  public PublicationRol getInsPub() {
    return insPub;
  }

  public void setInsPub(PublicationRol insPub) {
    this.insPub = insPub;
  }

  @OneToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "REAL_PUB_ID", referencedColumnName = "PUB_ID")
  public PublicationRol getRealInsPub() {
    return realInsPub;
  }

  public void setRealInsPub(PublicationRol realInsPub) {
    this.realInsPub = realInsPub;
  }

  /**
   * @return the withdrawReqDate
   */
  @Column(name = "WITHDRAW_REQ_DATE")
  public Date getWithdrawReqDate() {
    return withdrawReqDate;
  }

  /**
   * @param withdrawReqDate the withdrawReqDate to set
   */
  public void setWithdrawReqDate(Date withdrawReqDate) {
    this.withdrawReqDate = withdrawReqDate;
  }

  /**
   * @return the withdrawReqConfirmDate
   */
  @Column(name = "WITHDRAW_REQ_CONFIRM_DATE")
  public Date getWithdrawReqConfirmDate() {
    return withdrawReqConfirmDate;
  }

  /**
   * @param withdrawReqConfirmDate the withdrawReqConfirmDate to set
   */
  public void setWithdrawReqConfirmDate(Date withdrawReqConfirmDate) {
    this.withdrawReqConfirmDate = withdrawReqConfirmDate;
  }

  /**
   * @return the insConfirmDate
   */
  @Column(name = "INS_CONFIRM_DATE")
  public Date getInsConfirmDate() {
    return insConfirmDate;
  }

  /**
   * @param insConfirmDate the insConfirmDate to set
   */
  public void setInsConfirmDate(Date insConfirmDate) {
    this.insConfirmDate = insConfirmDate;
  }

  /**
   * @return the reSubmitDate
   */
  @Column(name = "RE_SUBMIT_DATE")
  public Date getReSubmitDate() {
    return reSubmitDate;
  }

  /**
   * @param reSubmitDate the reSubmitDate to set
   */
  public void setReSubmitDate(Date reSubmitDate) {
    this.reSubmitDate = reSubmitDate;
  }

  /**
   * @return the withdrawStatus
   */
  @Column(name = "WITHDRAW_STATUS")
  public Integer getWithdrawStatus() {
    return withdrawStatus;
  }

  /**
   * @param withdrawStatus the withdrawStatus to set
   */
  public void setWithdrawStatus(Integer withdrawStatus) {
    this.withdrawStatus = withdrawStatus;
  }

}
