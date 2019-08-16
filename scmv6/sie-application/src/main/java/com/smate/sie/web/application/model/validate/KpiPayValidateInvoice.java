package com.smate.sie.web.application.model.validate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "kpi_pay_validate_invoice")
public class KpiPayValidateInvoice implements Serializable {

  private static final long serialVersionUID = -6245069664622448457L;

  private Long id; // 主键
  private Long logId; // 对应的kpi_pay_validate_user_log表主键
  private Integer invoicesType; // 发票类型， 1：普通发票，0：增值税专用发票
  private String title; // 普通发票时是抬头信息，增值税专用发票时是单位名称
  private String uniformId; // 税号
  private String bank; // 开户银行
  private String bankNO; // 银行账号
  private String addr; // 注册地址
  private String tel; // 电话
  private Integer status; // 发票处理状态，0：未处理，1：已邮寄，2:已邮件通知财务
  private Integer hasPaied; // 是否已支付，0：未支付，1：已支付

  public KpiPayValidateInvoice() {
    super();
    // TODO Auto-generated constructor stub
  }

  public KpiPayValidateInvoice(Long id, Long logId, Integer invoicesType, String title, String uniformId, String bank,
      String bankNO, String addr, String tel, Integer status) {
    super();
    this.id = id;
    this.logId = logId;
    this.invoicesType = invoicesType;
    this.title = title;
    this.uniformId = uniformId;
    this.bank = bank;
    this.bankNO = bankNO;
    this.addr = addr;
    this.tel = tel;
    this.status = status;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_KPI_PAY_VALIDATE_INVOICE", sequenceName = "SEQ_KPI_PAY_VALIDATE_INVOICE",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_KPI_PAY_VALIDATE_INVOICE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "LOG_ID")
  public Long getLogId() {
    return logId;
  }

  public void setLogId(Long logId) {
    this.logId = logId;
  }

  @Column(name = "INVOICES_TYPE")
  public Integer getInvoicesType() {
    return invoicesType;
  }

  public void setInvoicesType(Integer invoicesType) {
    this.invoicesType = invoicesType;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "UNIFORM_ID")
  public String getUniformId() {
    return uniformId;
  }

  public void setUniformId(String uniformId) {
    this.uniformId = uniformId;
  }

  @Column(name = "BANK")
  public String getBank() {
    return bank;
  }

  public void setBank(String bank) {
    this.bank = bank;
  }

  @Column(name = "BANK_NO")
  public String getBankNO() {
    return bankNO;
  }

  public void setBankNO(String bankNO) {
    this.bankNO = bankNO;
  }

  @Column(name = "ADDR")
  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "HAS_PAIED")
  public Integer getHasPaied() {
    return hasPaied;
  }

  public void setHasPaied(Integer hasPaied) {
    this.hasPaied = hasPaied;
  }


}
