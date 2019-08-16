package com.smate.sie.web.application.model.validate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户支付记录
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
@Entity
@Table(name = "KPI_PAY_VALIDATE_USER_LOG")
public class KpiPayValidateUserLog implements Serializable {

  private Long id; // 主键
  private Long psnId; // 支付的账号人员ID
  private Date createDate; // 生成记录时间
  private Date payDate; // 支付时间
  private String tradeType; // 支付类型
  private String orderNum; // 订单号
  private Integer status; // 支付状态，1（已支付），0（未支付）
  private String grade; // 付费服务等级，A（12个月），B（24个月）
  private Double orderTotalAmount; // 订单价格
  private Integer needInvoices; // 是否需要开具发票, 1(需要)，0(不需要)


  public KpiPayValidateUserLog() {
    super();
  }



  public KpiPayValidateUserLog(Long id, Long psnId, Date createDate, Date payDate, String tradeType, String orderNum,
      Integer status, String grade) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.createDate = createDate;
    this.payDate = payDate;
    this.tradeType = tradeType;
    this.orderNum = orderNum;
    this.status = status;
    this.grade = grade;
  }



  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_KPI_VALIDATE_USER_LOG", sequenceName = "SEQ_KPI_VALIDATE_USER_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_KPI_VALIDATE_USER_LOG")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }


  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "TRADE_TYPE")
  public String getTradeType() {
    return tradeType;
  }

  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }

  @Column(name = "ORDER_NUM")
  public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


  @Column(name = "PAY_DATE")
  public Date getPayDate() {
    return payDate;
  }



  public void setPayDate(Date payDate) {
    this.payDate = payDate;
  }


  @Column(name = "GRADE")
  public String getGrade() {
    return grade;
  }



  public void setGrade(String grade) {
    this.grade = grade;
  }


  @Column(name = "ORDER_TOTAL_AMOUNT")
  public Double getOrderTotalAmount() {
    return orderTotalAmount;
  }



  public void setOrderTotalAmount(Double orderTotalAmount) {
    this.orderTotalAmount = orderTotalAmount;
  }


  @Column(name = "NEED_INVOICES")
  public Integer getNeedInvoices() {
    return needInvoices;
  }



  public void setNeedInvoices(Integer needInvoices) {
    this.needInvoices = needInvoices;
  }



}
