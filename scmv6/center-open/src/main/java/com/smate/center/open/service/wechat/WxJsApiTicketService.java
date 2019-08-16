package com.smate.center.open.service.wechat;

import java.util.Map;

public interface WxJsApiTicketService {

  /**
   * 获取微信jsapi签名
   * 
   * @param url
   * @return
   * @throws Exception
   */
  Map<String, String> handleWxJsSdkSing(String url) throws Exception;

  /**
   * 获取微信JS-API Ticket
   * 
   * @param accessToken
   * @return
   * @throws Exception
   */
  String getJsapiTicket(String accessToken) throws Exception;

}
