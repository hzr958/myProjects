package com.smate.core.base.utils.wechat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 网页授权服务实现.
 * 
 * @author xys
 *
 */
@Service("oAuth2Service")
@Transactional(rollbackFor = Exception.class)
public class OAuth2ServiceImpl implements OAuth2Service {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private static final String REQ_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

  @Value("${wechat.appid}")
  private String appid;
  @Value("${wechat.secret}")
  private String secret;
  @Value("${wechat.open.appid}")
  private String openAppid;
  @Value("${wechat.open.secret}")
  private String openSecret;
  @Autowired
  private SnsCacheService snsCacheService;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Value("${initOpen.restful.url_forwechat}")
  private String SERVER_URL_FOR_WECHAT;

  @SuppressWarnings("rawtypes")
  @Override
  public String getWeChatOpenId(String code) throws Exception {
    StringBuffer fullReqUrl = new StringBuffer();
    fullReqUrl.append(REQ_URL);
    fullReqUrl.append("?appid=");
    fullReqUrl.append(appid);
    fullReqUrl.append("&secret=");
    fullReqUrl.append(secret);
    fullReqUrl.append("&code=");
    fullReqUrl.append(code);
    fullReqUrl.append("&grant_type=authorization_code");
    Map map = MessageUtil.httpRequest(fullReqUrl.toString(), WeChatConstant.REQ_METHOD_POST, null);
    Object obj = map.get("openid");
    if (obj != null) {
      return obj.toString();
    } else {
      logger.info("The result from weChat:" + JacksonUtils.jsonObjectSerializer(map));
      return null;
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Map<String, Object> getOpenWeChatUnionInfo(String code) throws Exception {
    StringBuffer fullReqUrl = new StringBuffer();
    fullReqUrl.append(REQ_URL);
    fullReqUrl.append("?appid=");
    fullReqUrl.append(openAppid);
    fullReqUrl.append("&secret=");
    fullReqUrl.append(openSecret);
    fullReqUrl.append("&code=");
    fullReqUrl.append(code);
    fullReqUrl.append("&grant_type=authorization_code");
    Map map = MessageUtil.httpRequest(fullReqUrl.toString(), WeChatConstant.REQ_METHOD_POST, null);
    return map;
  }

  @Override
  public Map<String, Object> getWeChatInfo(String token, String openId) throws Exception {
    if (StringUtils.isBlank(token) || StringUtils.isBlank(openId)) {
      return null;
    }
    StringBuffer fullReqUrl = new StringBuffer();
    fullReqUrl.append("https://api.weixin.qq.com/cgi-bin/user/info?access_token=");
    fullReqUrl.append(token);
    fullReqUrl.append("&openid=");
    fullReqUrl.append(openId);
    fullReqUrl.append("&lang=zh_CN");
    Map map = MessageUtil.httpRequest(fullReqUrl.toString(), WeChatConstant.REQ_METHOD_GET, null);
    return map;
  }

  @Override
  public Map<String, Object> getOpenWeChatInfo(String token, String openId) throws Exception {
    if (StringUtils.isBlank(token) || StringUtils.isBlank(openId)) {
      return null;
    }
    StringBuffer fullReqUrl = new StringBuffer();
    fullReqUrl.append("https://api.weixin.qq.com/sns/userinfo?access_token=");
    fullReqUrl.append(token);
    fullReqUrl.append("&openid=");
    fullReqUrl.append(openId);
    Map map = MessageUtil.httpRequest(fullReqUrl.toString(), WeChatConstant.REQ_METHOD_GET, null);
    return map;
  }

  @Override
  public String getWeChatToken() throws Exception {
    String runEnv = System.getenv("RUN_ENV");
    String accessTokenCacheKey = getAccessTokenCacheKey(runEnv);
    String accessToken =
        ObjectUtils.toString(snsCacheService.get(WeChatConstant.ACCESS_TOKEN_CACHE_NAME, accessTokenCacheKey));
    if (StringUtils.isBlank(accessToken)) {
      Map<String, Object> map = new HashMap<String, Object>();
      HashMap<Object, Object> dataMap = new HashMap<>();
      // 设置固定值获取微信授权码，详见文档：科研之友开放平台API-2.7获取微信授权码
      map.put("openid", 99999999L);
      map.put("token", "000000007c630c84");
      dataMap.put("type", runEnv);
      map.put("data", JacksonUtils.mapToJsonStr(dataMap));
      String accessTokenObj = (restTemplate.postForObject(SERVER_URL_FOR_WECHAT, map, Object.class)).toString();
      if (StringUtils.isNotBlank(accessTokenObj)) {
        Map jsonMap = JacksonUtils.jsonToMap(accessTokenObj);
        List<Object> mapList = (List<Object>) jsonMap.get("result");
        if (CollectionUtils.isNotEmpty(mapList)) {
          Map map2 = (Map) mapList.get(0);
          if (map2.get("accessToken") != null) {
            accessToken = map2.get("accessToken").toString();
          }
        }
      }
      // open系统那边已经存过缓存了，不要再存了，不然本来2个小时就过期的，一直重新存，就一直不会自动过期清空，缓存的access_token就无效了
    }
    return accessToken;
  }

  @Override
  public Map<String, Object> getWeChatInfos(String token, String userList) throws Exception {
    StringBuffer fullReqUrl = new StringBuffer();
    fullReqUrl.append("https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=");
    fullReqUrl.append(token);
    Map map = MessageUtil.httpRequest(fullReqUrl.toString(), WeChatConstant.REQ_METHOD_POST, userList);
    return map;
  }

  @Override
  public Map<String, Object> getWeChatInfoSingle(String token, String user) throws Exception {
    StringBuffer fullReqUrl = new StringBuffer();
    fullReqUrl.append("https://api.weixin.qq.com/cgi-bin/user/info?access_token=");
    fullReqUrl.append(token);
    fullReqUrl.append("&openid=");
    fullReqUrl.append(user);
    fullReqUrl.append("&lang=zh_CN");
    Map map = MessageUtil.httpRequest(fullReqUrl.toString(), WeChatConstant.REQ_METHOD_POST, null);
    return map;
  }

  /**
   * 获取accessToken缓存的key
   * 
   * @param type
   * @return
   */
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
      default:
        return WeChatConstant.ACCESS_TOKEN_CACHE_KEY + "_run";
    }
  }

}
