package com.smate.sie.web.application.model.validate;

import java.io.Serializable;

import com.smate.core.base.pay.model.PayBaseVo;

/**
 * 人员vip服务VO
 * 
 * @author wsn
 * @date Feb 27, 2019
 */
public class KpiValidateVipVo extends PayBaseVo implements Serializable {

  private static final long serialVersionUID = 8710150191860511372L;
  private Long psnId;
  private String grade; // 收费等级
  private String discount; // 优惠
  private String orderNum; // 订单号
  private String notifyUrl; // 支付结果异步通知地址
  private String returnUrl; // 支付结果同步通知地址
  private String aliPayTradeNo; // 支付宝交易号
  private String tradeStatus;// 交易状态
  private String tradeType; // 交易状态
  private double price; // 订单价格
  private Integer needInvoices; // 是否需要开具发票, 1(需要)，0(不需要)
  private Long logId; // 对应的kpi_pay_validate_user_log表主键
  private Integer invoicesType; // 发票类型， 1：普通发票，0：增值税专用发票
  private String title; // 普通发票时是抬头，增值税专用发票时是单位名称
  private String uniformId; // 税号
  private String bank; // 开户银行
  private String bankNO; // 银行账号
  private String addr; // 注册地址
  private String tel; // 电话

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public String getDiscount() {
    return discount;
  }

  public void setDiscount(String discount) {
    this.discount = discount;
  }

  public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  public String getNotifyUrl() {
    return notifyUrl;
  }

  public void setNotifyUrl(String notifyUrl) {
    this.notifyUrl = notifyUrl;
  }

  public String getReturnUrl() {
    return returnUrl;
  }

  public void setReturnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
  }

  public String getAliPayTradeNo() {
    return aliPayTradeNo;
  }

  public void setAliPayTradeNo(String aliPayTradeNo) {
    this.aliPayTradeNo = aliPayTradeNo;
  }

  public String getTradeStatus() {
    return tradeStatus;
  }

  public void setTradeStatus(String tradeStatus) {
    this.tradeStatus = tradeStatus;
  }

  public String getTradeType() {
    return tradeType;
  }

  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Integer getNeedInvoices() {
    return needInvoices;
  }

  public void setNeedInvoices(Integer needInvoices) {
    this.needInvoices = needInvoices;
  }

  public Long getLogId() {
    return logId;
  }

  public void setLogId(Long logId) {
    this.logId = logId;
  }

  public Integer getInvoicesType() {
    return invoicesType;
  }

  public void setInvoicesType(Integer invoicesType) {
    this.invoicesType = invoicesType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUniformId() {
    return uniformId;
  }

  public void setUniformId(String uniformId) {
    this.uniformId = uniformId;
  }

  public String getBank() {
    return bank;
  }

  public void setBank(String bank) {
    this.bank = bank;
  }

  public String getBankNO() {
    return bankNO;
  }

  public void setBankNO(String bankNO) {
    this.bankNO = bankNO;
  }

  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }



}
