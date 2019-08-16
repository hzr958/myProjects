package com.smate.web.management.model.journal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli期刊审核表，bpo对临时期刊表的修改及系统管理员审核bpo状态表.
 */
@Entity
@Table(name = "BASE_JOURNAL_TEMP_CHECK")
public class BaseJournalTempCheck implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -457456384025207417L;
  // 主键
  private Long tempCheckId;
  // 临时表BASE_JOURNAL_TEMP_BATCH中的主键
  private Long tempBatchId;
  // 临时表BASE_JOURNAL_TEMP_ISI_IF中的主键
  private Long tempIsiIfId;
  // admin id
  private Long adminPsnId;
  // bpo id
  private Long bpoPsnId;
  // 管理员审核bpo修改临时期刊的状态。0：未审核/1：批准/2：返回
  private Long status;
  // 修改后的PISSN，如果审核通过，将此字段值同步修改到临时表
  private String pissn;
  // 修改后的EISSN，如果审核通过，将此字段值同步修改到临时表
  private String eissn;
  // 修改后的中文刊名或者其它语言刊名，如果审核通过，将此字段值同步修改到临时表
  private String titleXx;
  // 修改后的英文刊名，如果审核通过，将此字段值同步修改到临时表
  private String titleEn;
  // 处理方式。1：保留原样，2：新增期刊，3：更新至选中期刊
  private Long handleMethod;
  // 更新至选中期刊，实际存放的是期刊表的BASE_JOURNAL_TITLE表主键JOU_TITLE_ID
  private Long tuttiJouId;
  // 标识是否删除数据。0未删除，1删除
  private Long isActive;
  // 页面查询用，冗余字段
  private Long tempJuId;
  // 冗余字段，判断是审核批量还是审核手工，或者影响因子等
  private String isTemp;
  // 数据来源
  private Long dbId;
  // 修改提交时间
  private Date sbumitDate;
  // 审核处理时间
  private Date auditDate;
  // 冗余字段用于页面显示，来源数据库缩写
  private String dbCode;

  public BaseJournalTempCheck() {
    super();
    this.status = 0L;
    this.isActive = 0L;
  }

  @Id
  @Column(name = "TEMP_CHECK_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU_TEMP_CHECK", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getTempCheckId() {
    return tempCheckId;
  }

  public void setTempCheckId(Long tempCheckId) {
    this.tempCheckId = tempCheckId;
  }

  @Column(name = "TEMP_BATCH_ID")
  public Long getTempBatchId() {
    return tempBatchId;
  }

  public void setTempBatchId(Long tempBatchId) {
    this.tempBatchId = tempBatchId;
  }

  @Column(name = "TEMP_ISI_IF_ID")
  public Long getTempIsiIfId() {
    return tempIsiIfId;
  }

  public void setTempIsiIfId(Long tempIsiIfId) {
    this.tempIsiIfId = tempIsiIfId;
  }

  @Column(name = "ADMIN_PSN_ID")
  public Long getAdminPsnId() {
    return adminPsnId;
  }

  public void setAdminPsnId(Long adminPsnId) {
    this.adminPsnId = adminPsnId;
  }

  @Column(name = "BPO_PSN_ID")
  public Long getBpoPsnId() {
    return bpoPsnId;
  }

  public void setBpoPsnId(Long bpoPsnId) {
    this.bpoPsnId = bpoPsnId;
  }

  @Column(name = "STATUS")
  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  @Column(name = "PISSN")
  public String getPissn() {
    return pissn;
  }

  public void setPissn(String pissn) {
    this.pissn = pissn;
  }

  @Column(name = "TITLE_XX")
  public String getTitleXx() {
    return titleXx;
  }

  public void setTitleXx(String titleXx) {
    this.titleXx = titleXx;
  }

  @Column(name = "TITLE_EN")
  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  @Column(name = "HANDLE_METHOD")
  public Long getHandleMethod() {
    return handleMethod;
  }

  public void setHandleMethod(Long handleMethod) {
    this.handleMethod = handleMethod;
  }

  @Column(name = "TUTTI_JOU_ID")
  public Long getTuttiJouId() {
    return tuttiJouId;
  }

  public void setTuttiJouId(Long tuttiJouId) {
    this.tuttiJouId = tuttiJouId;
  }

  @Column(name = "IS_ACTIVE")
  public Long getIsActive() {
    return isActive;
  }

  public void setIsActive(Long isActive) {
    this.isActive = isActive;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "SUBMIT_DATE")
  public Date getSbumitDate() {
    return sbumitDate;
  }

  public void setSbumitDate(Date sbumitDate) {
    this.sbumitDate = sbumitDate;
  }

  @Column(name = "AUDIT_DATE")
  public Date getAuditDate() {
    return auditDate;
  }

  public void setAuditDate(Date auditDate) {
    this.auditDate = auditDate;
  }

  @Transient
  public Long getTempJuId() {
    return tempJuId;
  }

  public void setTempJuId(Long tempJuId) {
    this.tempJuId = tempJuId;
  }

  @Transient
  public String getIsTemp() {
    return isTemp;
  }

  public void setIsTemp(String isTemp) {
    this.isTemp = isTemp;
  }

  @Transient
  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "EISSN")
  public String getEissn() {
    return eissn;
  }

  public void setEissn(String eissn) {
    this.eissn = eissn;
  }

}
