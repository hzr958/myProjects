package com.smate.center.open.service.wechat;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.exception.OpenException;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.core.base.utils.wechat.MessageUtil;

/**
 * 接口调用凭据服务.
 * 
 * @author xys
 *
 */
public class AccessTokenServiceImpl implements AccessTokenService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenCacheService openCacheService;
  private String appid;
  private String secret;

  @Override
  public String getAccessToken() throws OpenException {
    return getAccessToken("");
  }

  @SuppressWarnings("rawtypes")
  @Override
  public synchronized String getAccessToken(String type) throws OpenException {
    String accessTokenCacheKey = getAccessTokenCacheKey(type);
    String appid = getAppId(type);
    String secret = getSecret(type);
    String accessToken =
        ObjectUtils.toString(openCacheService.get(WeChatConstant.ACCESS_TOKEN_CACHE_NAME, accessTokenCacheKey));
    if (StringUtils.isBlank(accessToken)) {
      String fullReqUrl = MessageUtil.getFullReqUrl(WeChatConstant.REQ_URL_ACCESS_TOKEN, appid, secret);
      logger.info("缓存的accessToken为空，需要调用接口获取新的accessToken");
      Map map = MessageUtil.httpRequest(fullReqUrl, WeChatConstant.REQ_METHOD_GET, null);
      try {
        accessToken = map.get(WeChatConstant.ACCESS_TOKEN_KEY).toString();
      } catch (Exception e) {
        logger.info("The result from weChat:" + JacksonUtils.jsonObjectSerializer(map));
      }
      logger.info("---------------------获取的access_token为：" + accessToken);
      openCacheService.put(WeChatConstant.ACCESS_TOKEN_CACHE_NAME, WeChatConstant.ACCESS_TOKEN_CACHE_EXPIRES,
          accessTokenCacheKey, accessToken);
    }
    return accessToken;
  }

  private String getAppId(String type) {
    switch (type.toLowerCase()) {
      case "development":
        return WeChatConstant.APPID_DEV;
      case "alpha":
        return WeChatConstant.APPID_ALPHA;
      case "test":
        return WeChatConstant.APPID_TEST;
      case "uat":
        return WeChatConstant.APPID_UAT;
      case "run":
        return WeChatConstant.APPID_RUN;
    }
    String serverName = SpringUtils.getServerName();
    switch (serverName) {
      case "dev.scholarmate.com":
      case "devm.scholarmate.com":
        return WeChatConstant.APPID_DEV;
      case "alpha.scholarmate.com":
      case "alpham.scholarmate.com":
        return WeChatConstant.APPID_ALPHA;
      case "test.scholarmate.com":
      case "testm.scholarmate.com":
        return WeChatConstant.APPID_TEST;
      case "uat.scholarmate.com":
      case "uatm.scholarmate.com":
        return WeChatConstant.APPID_UAT;
      case "www.scholarmate.com":
      case "m.scholarmate.com":
        return WeChatConstant.APPID_RUN;
      default:
        return appid;

    }
  }

  private String getSecret(String type) {
    switch (type.toLowerCase()) {
      case "development":
        return WeChatConstant.SECRET_DEV;
      case "alpha":
        return WeChatConstant.SECRET_ALPHA;
      case "test":
        return WeChatConstant.SECRET_TEST;
      case "uat":
        return WeChatConstant.SECRET_UAT;
      case "run":
        return WeChatConstant.SECRET_RUN;
    }
    String serverName = SpringUtils.getServerName();
    switch (serverName) {
      case "dev.scholarmate.com":
      case "devm.scholarmate.com":
        return WeChatConstant.SECRET_DEV;
      case "alpha.scholarmate.com":
      case "alpham.scholarmate.com":
        return WeChatConstant.SECRET_ALPHA;
      case "test.scholarmate.com":
      case "testm.scholarmate.com":
        return WeChatConstant.SECRET_TEST;
      case "uat.scholarmate.com":
      case "uatm.scholarmate.com":
        return WeChatConstant.SECRET_UAT;
      case "www.scholarmate.com":
      case "m.scholarmate.com":
        return WeChatConstant.SECRET_RUN;
      default:
        return secret;

    }
  }

  private String getAccessTokenCacheKey(String type) {
    switch (type.toLowerCase()) {
      case "development":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_dev";
      case "alpha":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_alpha";
      case "test":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_test";
      case "uat":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_uat";
      case "run":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_run";
    }
    String serverName = SpringUtils.getServerName();
    switch (serverName) {
      case "dev.scholarmate.com":
      case "devm.scholarmate.com":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_dev";
      case "alpha.scholarmate.com":
      case "alpham.scholarmate.com":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_alpha";
      case "test.scholarmate.com":
      case "testm.scholarmate.com":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_test";
      case "uat.scholarmate.com":
      case "uatm.scholarmate.com":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_uat";
      case "www.scholarmate.com":
      case "m.scholarmate.com":
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_run";
      default:
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY;

    }
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

}
