package com.smate.sie.core.base.utils.pay.wechat;

import java.io.InputStream;

/**
 * 微信支付配置
 * 
 * @author wsn
 * @date Mar 1, 2019
 */
public class WXPayConfigImpl extends WXPayConfig {

  // appId
  public static final String APPID = "wx470ee6855bdd417a";
  // 商户号
  public static final String MCH_ID = "1283737501";
  // API密匙
  public static final String KEY = "c8d421Ca9ef44Ddc9e6985c5fa5ef09a";
  // 交易类型 (JSAPI支付)
  public static final String TRADE_TYPE_JSAPI = "JSAPI";
  // 交易类型 (Native支付)
  public static final String TRADE_TYPE_NATIVE = "NATIVE";
  // 交易类型 (APP支付)
  public static final String TRADE_TYPE_APP = "APP";

  @Override
  String getAppID() {
    return APPID;
  }

  @Override
  String getMchID() {
    return MCH_ID;
  }

  @Override
  String getKey() {
    return KEY;
  }

  /**
   * 证书是需要调用退款才需要
   */
  @Override
  InputStream getCertStream() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  IWXPayDomain getWXPayDomain() {
    IWXPayDomain iwxPayDomain = new IWXPayDomain() {
      @Override
      public void report(String domain, long elapsedTimeMillis, Exception ex) {

      }

      @Override
      public DomainInfo getDomain(WXPayConfig config) {
        return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
      }
    };
    return iwxPayDomain;
  }

  public WXPayConfigImpl() {
    super();
  }

}
