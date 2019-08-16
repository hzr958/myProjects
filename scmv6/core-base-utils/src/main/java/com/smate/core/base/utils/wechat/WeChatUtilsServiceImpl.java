package com.smate.core.base.utils.wechat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 微信工具服务类，获取如jsapi ticket
 * 
 * @author zk
 * @since 6.0.1
 */
@Service("weChatUtilsService")
public class WeChatUtilsServiceImpl implements WeChatUtilsService {

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url_forwechat}")
  private String openUrl;

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> handleWxJsApiTicket(String url) throws Exception {
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(this.getWeChatJsApiTicket(url).toString());
    if ("success".equals(resultMap.get("status"))) {
      List<Map<String, String>> resultList = (List<Map<String, String>>) resultMap.get("result");
      if (CollectionUtils.isNotEmpty(resultList)) {
        Map<String, String> map = resultList.get(0);
        Assert.notNull(map.get("nonceStr"));
        Assert.notNull(map.get("timestamp"));
        Assert.notNull(map.get("signature"));
        return map;
      }
    }
    return null;
  }

  // 获取微信js-api票据
  private Object getWeChatJsApiTicket(String url) throws Exception {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("SMATE_URL", url); // 用户请求的url
    mapDate.put("token", "00000000xc410fnd");// 系统默认token
    mapDate.put("openid", 99999999L);// 系统默认openId
    return restTemplate.postForObject(openUrl, mapDate, Object.class);
  }

}
