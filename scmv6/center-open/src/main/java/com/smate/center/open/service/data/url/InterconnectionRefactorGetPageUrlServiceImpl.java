package com.smate.center.open.service.data.url;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * 获取 互联互通的 url 服务
 * 
 * @author AiJiangBin
 *
 */
public class InterconnectionRefactorGetPageUrlServiceImpl extends ThirdDataTypeBase {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenCacheService openCacheService;
  @Value("${domain.open.https}")
  private String domain;
  @Value("${domain.open}")
  private String httpDomain;


  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        paramet.putAll(dataMap);
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> infoMap = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
    String openId = paramet.get(OpenConsts.MAP_OPENID).toString();
    String data = paramet.get(OpenConsts.MAP_DATA).toString();

    infoMap = getUnionURL(token, openId, data);
    dataList.add(infoMap);
    temp = super.successMap(OpenMsgCodeConsts.SCM_000, dataList);
    return temp;
  }

  /**
   * 得到互联url
   * 
   * @param tokenId
   * @param openId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> getUnionURL(String tokenId, String openId, String data) {
    String key = "";
    Long timeStamp = new Date().getTime();
    key = ServiceUtil.encodeToDes3(tokenId + openId + timeStamp);
    openCacheService.put(OpenConsts.UNION_URL_PARAM_CACHE, 2 * 60, key, data);
    openCacheService.put(OpenConsts.UNION_URL_CACHE, 2 * 60, key, true);

    Map<String, String> dataMap = JacksonUtils.jsonToMap(data);
    String url = "";
    if (dataMap.get(OpenConsts.PSN_DATA_PROTOCOL) != null
        && OpenConsts.PROTOCOL_HTTPS.equals(dataMap.get(OpenConsts.PSN_DATA_PROTOCOL))) {
      url = domain;
    } else {
      url = httpDomain;
    }
    url += "/open/interconnection/show?token=" + key;

    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put(InterconnectionUrlConst.URL, url);
    resultMap.put(InterconnectionUrlConst.KEY, key);
    return resultMap;
  }



}
