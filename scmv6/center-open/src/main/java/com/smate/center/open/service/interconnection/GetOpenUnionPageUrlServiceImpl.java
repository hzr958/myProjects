package com.smate.center.open.service.interconnection;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 获取 互联互通的 url 服务
 * 
 * @author AiJiangBin
 *
 */
public class GetOpenUnionPageUrlServiceImpl implements ScmUnionDataHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenCacheService openCacheService;
  @Value("${domainscm}")
  private String domain;
  @Value("${http.domainscm}")
  private String httpDomain;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  /**
   * 得到互联url
   * 
   * @param tokenId
   * @param openId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public String getUnionURL(String tokenId, String openId, String data) throws Exception {
    String key = "";
    Long timeStamp = new Date().getTime();
    key = ServiceUtil.encodeToDes3(tokenId + openId + timeStamp);
    openCacheService.put(OpenConsts.UNION_URL_PARAM_CACHE, 2 * 60, key, data);
    openCacheService.put(OpenConsts.UNION_URL_CACHE, 2 * 60, key, true);
    // 先判断缓存中有木有，
    // String keyCache = (String) openCacheService.get(OpenConsts.UNION_URL_CACHE, key) ;
    // System.out.println("key"+openCacheService.get(OpenConsts.UNION_URL_CACHE, openId));
    Map<String, String> dataMap = JacksonUtils.jsonToMap(data);
    String url = "";
    if (dataMap.get(OpenConsts.PSN_DATA_PROTOCOL) != null
        && OpenConsts.PROTOCOL_HTTPS.equals(dataMap.get(OpenConsts.PSN_DATA_PROTOCOL))) {
      url = domain;
    } else {
      url = httpDomain;
    }
    url += "/open/interconnection/show?token=" + key;
    return buildUnionUrlXmlStr(url, tokenId + openId + timeStamp, "0");
  }


  public void rest() {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    String openUrl = "https://127.0.0.1:49080/center-open/scmopendata";
    mapDate.put("token", "00000000u83nu2n0");// 系统默认token // token ==
    // 00000000 type:服务类型
    // xc410fnd ：为了获取 实现服务的实现类
    mapDate.put("openid", 13141578);// 系统默认openId
    Object obj = restTemplate.postForObject(openUrl, mapDate, Object.class);
  }


  @Override
  public String handleUnionData(Map<String, Object> dataParamet) {
    try {
      String token = dataParamet.get("token").toString();
      String openId = dataParamet.get("openid").toString();
      String data = dataParamet.get("data").toString();
      return getUnionURL(token, openId, data);
    } catch (Exception e) {
      // 吃掉异常，当做open系统的正确请求来处理
      logger.error(e.getMessage() + JacksonUtils.mapToJsonStr(dataParamet), e);
      return buildUnionUrlXmlStr("获取url异常", "5");
    }
  }



  public String buildUnionUrlXmlStr(String url, String key, String status) {
    Document doc = null;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><data></data>");
      Element rootNode = (Element) doc.selectSingleNode("/data");
      rootNode.addElement("url").setText(url);
      rootNode.addElement("key").setText(key);
      rootNode.addElement("getURLStatus").addText(status);
      return doc.getRootElement().asXML();
    } catch (Exception e) {
    }
    return doc.getRootElement().asXML();
  }

  public String buildUnionUrlXmlStr(String result, String status) {
    Document doc = null;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><data></data>");
      Element rootNode = (Element) doc.selectSingleNode("/data");
      rootNode.addElement("result").setText(result);
      rootNode.addElement("getURLStatus").addText(status);
      return doc.getRootElement().asXML();
    } catch (Exception e) {
    }
    return doc.getRootElement().asXML();
  }

}
