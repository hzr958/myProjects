package com.smate.center.batch.service.pdwh.pubimport;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.batch.dao.pdwh.pub.PdwhPubIndexUrlDao;
import com.smate.center.batch.model.pdwh.pub.PdwhPubIndexUrl;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.ServiceUtil;

@Service("pubShortUrlSaveService")
@Transactional(rollbackFor = Exception.class)
public class PubShortUrlSaveServiceImpl implements PubShortUrlSaveService {
  @Autowired
  private PdwhPubIndexUrlDao pubIndexUrlDao;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  @Override
  @SuppressWarnings("unchecked")
  public void producePubShortUrl(Long pubId, String type) throws Exception {
    String shortUrl = "";
    // 构建参数
    Map<String, Object> parameters = buildParameters(pubId, type);
    // 访问Open系统接口获取ShortUrl
    Object obj = restTemplate.postForObject(SERVER_URL, parameters, Object.class);
    // 接口返回数据处理
    Map<String, Object> objMap = JacksonUtils.jsonToMap(obj.toString());
    // 获取短地址值
    if (objMap.get("result") != null) {
      List<Map<String, Object>> list = (List<Map<String, Object>>) objMap.get("result");
      if (list != null && list.size() > 0 && list.get(0).get("shortUrl") != null) {
        shortUrl = list.get(0).get("shortUrl").toString();
      }
    }
    savePubShortUrl(pubId, shortUrl);
  }

  /**
   * 构建参数
   * 
   * @param Id
   * @param type
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> buildParameters(Long pubId, String type) throws Exception {
    Map<String, Object> map = new HashedMap();
    Map<String, Object> dataMap = new HashedMap();
    Map<String, Object> shortUrlParametMap = new HashedMap();

    map.put("openid", "99999999");
    map.put("token", "00000000sht22url");

    dataMap.put("createPsnId", "0");
    dataMap.put("type", type);
    shortUrlParametMap.put("des3PubId", ServiceUtil.encodeToDes3(pubId.toString()));
    dataMap.put("shortUrlParamet", JacksonUtils.mapToJsonStr(shortUrlParametMap));
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  /**
   * 保存成果短地址
   * 
   * @param pubId
   */
  public void savePubShortUrl(Long pubId, String url) throws Exception {
    if (pubIndexUrlDao.get(pubId) == null) {
      PdwhPubIndexUrl pub = new PdwhPubIndexUrl();
      pub.setPubId(pubId);
      pub.setPubIndexUrl(url);
      pub.setUpdateDate(new Date());
      pubIndexUrlDao.save(pub);
    }

  }

}
