package com.smate.core.base.utils.wechat;

import java.util.Map;


public interface WeChatUtilsService {

  /**
   * 处理和检验微信jsApi票据
   * 
   * @param url
   * @return
   * @throws Exception
   */
  Map<String, String> handleWxJsApiTicket(String url) throws Exception;

}
