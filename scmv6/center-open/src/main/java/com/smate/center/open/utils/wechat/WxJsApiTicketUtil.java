package com.smate.center.open.utils.wechat;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.json.JacksonUtils;


/**
 * jsapi-ticket获取工具类
 * 
 * @author zk
 * @since 6.0.1
 */
public class WxJsApiTicketUtil {

  private static String requestUrl = "http://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=";

  public static String getJsapiTicket(String accessToken) throws Exception {
    Assert.notNull(accessToken);
    String ticketStr = HttpConnectionUtil.httpConnection(requestUrl + accessToken, WeChatConstant.REQ_METHOD_GET, null);
    if (StringUtils.isBlank(ticketStr)) {
      throw new Exception("获取jsapi-ticket为空,url=" + requestUrl + accessToken);
    } else {
      Map<String, String> result = JacksonUtils.jsonMapUnSerializer(ticketStr);
      if ("ok".equals(result.get("errmsg"))) {
        return result.get("ticket");
      } else {
        throw new Exception("获取jsapi-ticket失败,ticketStr=" + ticketStr);
      }
    }

  }
}
