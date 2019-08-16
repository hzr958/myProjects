package com.smate.sie.core.base.utils.model.pay.wechat;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 订单检查参数实体类
 * 
 * @author wsn
 * @date Feb 27, 2019
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderCheckEntity implements Serializable {
  private String appid;// 调用接口提交的公众账号ID
  private String mch_id;// 调用接口提交的商户号
  private String nonce_str;// 微信返回的随机字符串
  private String sign;// 微信返回的签名值
  private String out_trade_no;// 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一

  public OrderCheckEntity() {
    super();
  }

  public OrderCheckEntity(String appid, String mch_id, String nonce_str, String sign, String out_trade_no) {
    super();
    this.appid = appid;
    this.mch_id = mch_id;
    this.nonce_str = nonce_str;
    this.sign = sign;
    this.out_trade_no = out_trade_no;
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

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }


}
