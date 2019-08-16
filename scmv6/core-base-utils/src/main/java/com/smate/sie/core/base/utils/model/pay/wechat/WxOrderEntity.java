package com.smate.sie.core.base.utils.model.pay.wechat;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付订单实体， 调用微信端统一下单接口需要带的一些必填参数 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxOrderEntity implements Serializable {
  private String appid;// 微信支付分配的公众账号ID（企业号corpid即为此appId）
  private String body;// 商品描述，参数规范具体请看https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
  private String device_info;// 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
  private String mch_id;// 微信支付分配的商户号
  private String nonce_str;// 随机字符串，长度要求在32位以内。推荐https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
  private String sign;// 通过签名算法计算得出的签名值，详见https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
  private String out_trade_no;// 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
  private int total_fee;// 订单总金额，单位为分
  private String trade_type;// 交易类型 JSAPI(JSAPI支付)、NATIVE(Native支付)、APP(APP支付)
  private String spbill_create_ip;// 终端IP,支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
  private String openid;// 用户标识，trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
  private String notify_url;// 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
  private Long product_id;// trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义

  public WxOrderEntity() {
    super();
  }

  public WxOrderEntity(String appid, String body, String device_info, String mch_id, String nonce_str, String sign,
      String out_trade_no, int total_fee, String trade_type, String spbill_create_ip, String openid, String notify_url,
      Long product_id) {
    super();
    this.appid = appid;
    this.body = body;
    this.device_info = device_info;
    this.mch_id = mch_id;
    this.nonce_str = nonce_str;
    this.sign = sign;
    this.out_trade_no = out_trade_no;
    this.total_fee = total_fee;
    this.trade_type = trade_type;
    this.spbill_create_ip = spbill_create_ip;
    this.openid = openid;
    this.notify_url = notify_url;
    this.product_id = product_id;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getDevice_info() {
    return device_info;
  }

  public void setDevice_info(String device_info) {
    this.device_info = device_info;
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

  public int getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(int total_fee) {
    this.total_fee = total_fee;
  }

  public String getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(String trade_type) {
    this.trade_type = trade_type;
  }

  public String getSpbill_create_ip() {
    return spbill_create_ip;
  }

  public void setSpbill_create_ip(String spbill_create_ip) {
    this.spbill_create_ip = spbill_create_ip;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getNotify_url() {
    return notify_url;
  }

  public void setNotify_url(String notify_url) {
    this.notify_url = notify_url;
  }

  public Long getProduct_id() {
    return product_id;
  }

  public void setProduct_id(Long product_id) {
    this.product_id = product_id;
  }

}
