package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author lichangwen
 */
@Entity
@Table(name = "PUB_PSN_STAT")
public class PubRolPsnStat implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6472381743891534693L;
  /** 主键. */
  private PubRolSubmissionStatKey key;
  /** 创建日期. */
  private Date createDate;
  /** 已认领成果总数. */
  private Long confirmTotal;
  /** 指派成果总数(待认领成果条数). */
  private Long pubTotal;
  /** 最后发送人. */
  private Long lastSendBy;
  /** 最后发送日期. */
  private Date lastSendAt;
  /** 发送邮件次数. */
  private Integer sendTotal;
  /** 人员姓名. */
  private String psnName;
  /** 人员所在部门Id. */
  private Long psnUnitId;
  /** 人员所在部门名. */
  private String psnUnitName;
  /** 最后发送人姓名. */
  private String lastSendName;
  /** 人员电子邮件. */
  private String psnEmail;
  /** 人员职称. */
  private String title;
  private Integer cyFlag;
  // 人员html
  private String psnInfoHtml;

  public PubRolPsnStat() {
    super();
  }

  public PubRolPsnStat(Long psnId, Long insId) {
    PubRolSubmissionStatKey nkey = new PubRolSubmissionStatKey();
    nkey.setInsId(insId);
    nkey.setPsnId(psnId);
    this.setCreateDate(new Date());
    this.setPubTotal(0L);
    this.setConfirmTotal(0L);
    this.setSendTotal(0);
    this.key = nkey;
  }

  public PubRolPsnStat(Long psnId, Long insId, Long pubTotal) {
    PubRolSubmissionStatKey nkey = new PubRolSubmissionStatKey();
    nkey.setInsId(insId);
    nkey.setPsnId(psnId);
    this.setPubTotal(pubTotal);
    this.setConfirmTotal(0L);
    this.setSendTotal(0);
    this.key = nkey;
  }

  public PubRolPsnStat(PubRolSubmissionStatKey key, Long confirmTotal, Long pubTotal, String psnZhName,
      String psnEnName, Long psnUnitId, Date lastSendAt, Integer sendTotal, String lang) {
    super();
    this.key = key;
    this.confirmTotal = confirmTotal;
    this.pubTotal = pubTotal;
    this.lastSendAt = lastSendAt;
    this.psnUnitId = psnUnitId;
    this.sendTotal = sendTotal;
    if ("en".equals(lang)) {
      this.psnName = psnEnName == null ? psnZhName : psnEnName;
    } else {
      this.psnName = psnZhName == null ? psnEnName : psnZhName;
    }
  }

  @EmbeddedId
  public PubRolSubmissionStatKey getKey() {
    return key;
  }

  public void setKey(PubRolSubmissionStatKey key) {
    this.key = key;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "CONFIRM_TOTAL")
  public Long getConfirmTotal() {
    return confirmTotal;
  }

  public void setConfirmTotal(Long confirmTotal) {
    this.confirmTotal = confirmTotal;
  }

  @Column(name = "PUB_TOTAL")
  public Long getPubTotal() {
    return pubTotal;
  }

  public void setPubTotal(Long pubTotal) {
    this.pubTotal = pubTotal;
  }

  @Column(name = "LAST_SEND_BY")
  public Long getLastSendBy() {
    return lastSendBy;
  }

  public void setLastSendBy(Long lastSendBy) {
    this.lastSendBy = lastSendBy;
  }

  @Column(name = "LAST_SEND_AT")
  public Date getLastSendAt() {
    return lastSendAt;
  }

  public void setLastSendAt(Date lastSendAt) {
    this.lastSendAt = lastSendAt;
  }

  @Column(name = "SEND_TOTAL")
  public Integer getSendTotal() {
    return sendTotal;
  }

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

  @Transient
  public String getPsnEmail() {
    return psnEmail;
  }

  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

  @Transient
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Transient
  public Integer getCyFlag() {
    return cyFlag;
  }

  public void setCyFlag(Integer cyFlag) {
    this.cyFlag = cyFlag;
  }

  @Transient
  public String getPsnInfoHtml() {
    return psnInfoHtml;
  }

  public void setPsnInfoHtml(String psnInfoHtml) {
    this.psnInfoHtml = psnInfoHtml;
  }

}
