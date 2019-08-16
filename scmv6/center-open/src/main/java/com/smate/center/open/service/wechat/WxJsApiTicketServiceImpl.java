package com.smate.center.open.service.wechat;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.utils.wechat.WxJsApiTicketUtil;
import com.smate.center.open.utils.wechat.WxJsSdkSignUtil;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;

/**
 * 微信Jsapi-ticket服务类
 * 
 * @author zk
 * @since 6.0.1
 */
/**
 * WxJsApiTicketService
 * 
 * @author zk
 * @since 6.0.1
 */
@Service("wxJsApiTicketService")
public class WxJsApiTicketServiceImpl implements WxJsApiTicketService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AccessTokenService accessTokenService;
  @Autowired
  private OpenCacheService openCacheService;

  @Override
  public Map<String, String> handleWxJsSdkSing(String url) throws Exception {
    return WxJsSdkSignUtil.sign(this.getJsapiTicket(null), url);
  }

  @Override
  public String getJsapiTicket(String accessToken) throws Exception {
    String jsapiTicket = ObjectUtils.toString(
        openCacheService.get(WeChatConstant.WX_JSAPI_TICKET_CACHE_NAME, WeChatConstant.WX_JSAPI_TICKET_CACHE_EKY));
    if (StringUtils.isBlank(jsapiTicket)) {
      if (StringUtils.isBlank(accessToken)) {
        accessToken = accessTokenService.getAccessToken();
      }
      jsapiTicket = WxJsApiTicketUtil.getJsapiTicket(accessToken);
      openCacheService.put(WeChatConstant.WX_JSAPI_TICKET_CACHE_NAME, WeChatConstant.ACCESS_TOKEN_CACHE_EXPIRES,
          WeChatConstant.WX_JSAPI_TICKET_CACHE_EKY, jsapiTicket);
    }
    return jsapiTicket;
  }
}
