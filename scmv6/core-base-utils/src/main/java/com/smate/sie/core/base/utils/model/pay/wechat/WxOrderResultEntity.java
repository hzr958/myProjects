package com.smate.sie.core.base.utils.model.pay.wechat;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信统一下单接口返回结果实体
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxOrderResultEntity implements Serializable {
  private String return_code;// SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
  private String return_msg;// 当return_code为FAIL时返回信息为错误原因
  // 以下参数为return_code为SUCCESS的时候有返回
  private String result_code;// SUCCESS/FAIL
  private String appid;// 调用接口提交的公众账号ID
  private String mch_id;// 调用接口提交的商户号
  private String nonce_str;// 微信返回的随机字符串
  private String sign;// 微信返回的签名值
  // 下字段在return_code 和result_code都为SUCCESS的时候有返回
  private String trade_type;// 交易类型 JSAPI(JSAPI支付)、NATIVE(Native支付)、APP(APP支付)
  private String prepay_id;// 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
  private String code_url;// trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。有效期为2小时

  public WxOrderResultEntity() {
    super();
  }

  public WxOrderResultEntity(String return_code, String return_msg, String result_code, String appid, String mch_id,
      String nonce_str, String sign, String trade_type, String prepay_id, String code_url) {
    super();
    this.return_code = return_code;
    this.return_msg = return_msg;
    this.result_code = result_code;
    this.appid = appid;
    this.mch_id = mch_id;
    this.nonce_str = nonce_str;
    this.sign = sign;
    this.trade_type = trade_type;
    this.prepay_id = prepay_id;
    this.code_url = code_url;
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

  public String getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(String trade_type) {
    this.trade_type = trade_type;
  }

  public String getPrepay_id() {
    return prepay_id;
  }

  public void setPrepay_id(String prepay_id) {
    this.prepay_id = prepay_id;
  }

  public String getCode_url() {
    return code_url;
  }

  public void setCode_url(String code_url) {
    this.code_url = code_url;
  }



}
