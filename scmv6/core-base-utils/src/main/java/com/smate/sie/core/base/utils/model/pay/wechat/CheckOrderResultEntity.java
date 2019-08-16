package com.smate.sie.core.base.utils.model.pay.wechat;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 校验微信支付订单状态，具体可看https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_2
 * 
 * @author wsn
 * @date Feb 27, 2019
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckOrderResultEntity implements Serializable {

  private String return_code;
  private String return_msg;
  private String appid;
  private String mch_id;
  private String device_info;
  private String nonce_str;
  private String sign;
  private String result_code;
  private String openid;
  private String is_subscribe;
  private String trade_type;
  private String bank_type;
  private String total_fee;
  private String fee_type;
  private String transaction_id;
  private String out_trade_no;
  private String attach;
  private String time_end;
  private String trade_state;

  public CheckOrderResultEntity() {
    super();
  }

  public CheckOrderResultEntity(String return_code, String return_msg, String appid, String mch_id, String device_info,
      String nonce_str, String sign, String result_code, String openid, String is_subscribe, String trade_type,
      String bank_type, String total_fee, String fee_type, String transaction_id, String out_trade_no, String attach,
      String time_end, String trade_state) {
    super();
    this.return_code = return_code;
    this.return_msg = return_msg;
    this.appid = appid;
    this.mch_id = mch_id;
    this.device_info = device_info;
    this.nonce_str = nonce_str;
    this.sign = sign;
    this.result_code = result_code;
    this.openid = openid;
    this.is_subscribe = is_subscribe;
    this.trade_type = trade_type;
    this.bank_type = bank_type;
    this.total_fee = total_fee;
    this.fee_type = fee_type;
    this.transaction_id = transaction_id;
    this.out_trade_no = out_trade_no;
    this.attach = attach;
    this.time_end = time_end;
    this.trade_state = trade_state;
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

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getMch_id() {
    return mch_id;
  }

  public void setMch_id(String mch_id) {
    this.mch_id = mch_id;
  }

  public String getDevice_info() {
    return device_info;
  }

  public void setDevice_info(String device_info) {
    this.device_info = device_info;
  }

  public String getNonce_str() {
    return nonce_str;
  }

  public void setNonce_str(String nonce_str) {
    this.nonce_str = nonce_str;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getResult_code() {
    return result_code;
  }

  public void setResult_code(String result_code) {
    this.result_code = result_code;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getIs_subscribe() {
    return is_subscribe;
  }

  public void setIs_subscribe(String is_subscribe) {
    this.is_subscribe = is_subscribe;
  }

  public String getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(String trade_type) {
    this.trade_type = trade_type;
  }

  public String getBank_type() {
    return bank_type;
  }

  public void setBank_type(String bank_type) {
    this.bank_type = bank_type;
  }

  public String getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(String total_fee) {
    this.total_fee = total_fee;
  }

  public String getFee_type() {
    return fee_type;
  }

  public void setFee_type(String fee_type) {
    this.fee_type = fee_type;
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

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public String getTime_end() {
    return time_end;
  }

  public void setTime_end(String time_end) {
    this.time_end = time_end;
  }

  public String getTrade_state() {
    return trade_state;
  }

  public void setTrade_state(String trade_state) {
    this.trade_state = trade_state;
  }



}
