package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_SUBMISSION_STAT")
public class PubRolSubmissionStat implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3892861181745618379L;

  // 主键
  private PubRolSubmissionStatKey key;
  // 最后提交日期
  private Date lastSubmitDate;
  // 提交总数
  private Long submitTotal;
  // 成果总数(缓存到ROL/从个人读取?)
  private Long totalOutputs;
  // 最后发送人
  private Long lastSendBy;
  // 最后发送日期
  private Date lastSendAt;
  // 发送邮件次数
  private Integer sendTotal;
  // 人员姓名
  private String psnName;
  // 人员所在部门Id
  private Long psnUnitId;
  // 人员所在部门名
  private String psnUnitName;
  // 最后发送人姓名
  private String lastSendName;
  // 个人端的成果提交总数，因个人提交成果后，可将成果删除，并不一定与单位的提交数一致
  private Long snsSubmitTotal;

  public PubRolSubmissionStat() {
    super();
  }

  public PubRolSubmissionStat(Long psnId, Long insId, Long submitTotal, Long totalOutputs) {
    super();
    this.key = new PubRolSubmissionStatKey(psnId, insId);
    this.submitTotal = submitTotal;
    this.totalOutputs = totalOutputs;
    this.sendTotal = 0;
  }

  public PubRolSubmissionStat(Long psnId, Long insId, Long submitTotal, Long totalOutputs, Integer sendTotal,
      Long snsSubmitTotal) {
    super();
    this.key = new PubRolSubmissionStatKey(psnId, insId);
    this.submitTotal = submitTotal;
    this.totalOutputs = totalOutputs;
    this.sendTotal = sendTotal;
    this.snsSubmitTotal = snsSubmitTotal;
  }

  public PubRolSubmissionStat(PubRolSubmissionStatKey key, Long totalOutputs, String psnZhName, String psnEnName,
      Long psnUnitId, Long snsSubmitTotal, Date lastSubmitDate, Date lastSendAt, String lang) {
    super();
    this.key = key;
    this.lastSubmitDate = lastSubmitDate;
    this.totalOutputs = totalOutputs;
    this.lastSendAt = lastSendAt;
    this.snsSubmitTotal = snsSubmitTotal;
    this.psnUnitId = psnUnitId;
    if ("en".equals(lang)) {
      this.psnName = psnEnName == null ? psnZhName : psnEnName;
    } else {
      this.psnName = psnZhName == null ? psnEnName : psnZhName;
    }
  }

  /**
   * @return the key
   */
  @EmbeddedId
  public PubRolSubmissionStatKey getKey() {
    return key;
  }

  /**
   * @param key the key to set
   */
  public void setKey(PubRolSubmissionStatKey key) {
    this.key = key;
  }

  /**
   * @return the lastSubmitDate
   */
  @Column(name = "LAST_SUBMIT_DATE")
  public Date getLastSubmitDate() {
    return lastSubmitDate;
  }

  /**
   * @param lastSubmitDate the lastSubmitDate to set
   */
  public void setLastSubmitDate(Date lastSubmitDate) {
    this.lastSubmitDate = lastSubmitDate;
  }

  /**
   * @return the submitTotal
   */
  @Column(name = "SUBMIT_TOTAL")
  public Long getSubmitTotal() {
    return submitTotal;
  }

  /**
   * @param submitTotal the submitTotal to set
   */
  public void setSubmitTotal(Long submitTotal) {
    this.submitTotal = submitTotal;
  }

  /**
   * @return the totalOutputs
   */
  @Column(name = "TOTAL_OUTPUTS")
  public Long getTotalOutputs() {
    return totalOutputs;
  }

  /**
   * @param totalOutputs the totalOutputs to set
   */
  public void setTotalOutputs(Long totalOutputs) {
    this.totalOutputs = totalOutputs;
  }

  /**
   * @return the lastSendBy
   */
  @Column(name = "LAST_SEND_BY")
  public Long getLastSendBy() {
    return lastSendBy;
  }

  /**
   * @param lastSendBy the lastSendBy to set
   */
  public void setLastSendBy(Long lastSendBy) {
    this.lastSendBy = lastSendBy;
  }

  /**
   * @return the lastSendAt
   */
  @Column(name = "LAST_SEND_AT")
  public Date getLastSendAt() {
    return lastSendAt;
  }

  /**
   * @param lastSendAt the lastSendAt to set
   */
  public void setLastSendAt(Date lastSendAt) {
    this.lastSendAt = lastSendAt;
  }

  /**
   * @return the sendTotal
   */
  @Column(name = "SEND_TOTAL")
  public Integer getSendTotal() {
    return sendTotal;
  }

  /**
   * @param sendTotal the sendTotal to set
   */
  public void setSendTotal(Integer sendTotal) {
    this.sendTotal = sendTotal;
  }

  @Transient
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Transient
  public Long getPsnUnitId() {
    return psnUnitId;
  }

  public void setPsnUnitId(Long psnUnitId) {
    this.psnUnitId = psnUnitId;
  }

  @Transient
  public String getPsnUnitName() {
    return psnUnitName;
  }

  public void setPsnUnitName(String psnUnitName) {
    this.psnUnitName = psnUnitName;
  }

  @Transient
  public String getLastSendName() {
    return lastSendName;
  }

  public void setLastSendName(String lastSendName) {
    this.lastSendName = lastSendName;
  }

  @Column(name = "SNS_SUBMIT_TOTAL")
  public Long getSnsSubmitTotal() {
    return snsSubmitTotal;
  }

  public void setSnsSubmitTotal(Long snsSubmitTotal) {
    this.snsSubmitTotal = snsSubmitTotal;
  }

}
