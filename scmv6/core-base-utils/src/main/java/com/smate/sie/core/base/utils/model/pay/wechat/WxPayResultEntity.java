package com.smate.sie.core.base.utils.model.pay.wechat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付结果通知实体， 具体接口文档可看https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7&index=8
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxPayResultEntity {
  private String return_code;// 状态码
  private String return_msg;// 返回信息
  // 以下字段在return_code为SUCCESS的时候有返回
  private String result_code;// 业务结果,SUCCESS/FAIL
  private String transaction_id;// 微信支付订单号
  private String out_trade_no;// 商户订单号
  private String total_fee;// 订单金额
  private String cash_fee;// 现金支付金额

  public WxPayResultEntity() {
    super();
  }

  public WxPayResultEntity(String return_code, String return_msg, String result_code, String transaction_id,
      String out_trade_no, String total_fee, String cash_fee) {
    super();
    this.return_code = return_code;
    this.return_msg = return_msg;
    this.result_code = result_code;
    this.transaction_id = transaction_id;
    this.out_trade_no = out_trade_no;
    this.total_fee = total_fee;
    this.cash_fee = cash_fee;
  }

  public String getReturn_code() {
    return return_code;
  }

  public void setReturn_code(String return_code) {
    this.return_code = return_code;
  }

  public String getReturn_msg() {
    return return_msg;
  }

  public void setReturn_msg(String return_msg) {
    this.return_msg = return_msg;
  }

  public String getResult_code() {
    return result_code;
  }

  public void setResult_code(String result_code) {
    this.result_code = result_code;
  }

  public String getTransaction_id() {
    return transaction_id;
  }

  public void setTransaction_id(String transaction_id) {
    this.transaction_id = transaction_id;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public String getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(String total_fee) {
    this.total_fee = total_fee;
  }

  public String getCash_fee() {
    return cash_fee;
  }

  public void setCash_fee(String cash_fee) {
    this.cash_fee = cash_fee;
  }


}
