package com.smate.web.v8pub.service.sns.indexurl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.exception.ServiceException;

@Service(value = "pubIndexUrlService")
@Transactional(rollbackFor = Exception.class)
public class PubIndexUrlServiceImpl implements PubIndexUrlService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;

  @Override
  public PubIndexUrl get(Long pubId) throws ServiceException {
    PubIndexUrl pubIndexUrl = null;
    try {
      pubIndexUrl = pubIndexUrlDao.get(pubId);
      return pubIndexUrl;
    } catch (Exception e) {
      logger.error("成果短地址服务：获取成果短地址对象异常！PubIndexUrl={}", pubIndexUrl, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubIndexUrl pubIndexUrl) throws ServiceException {
    try {
      pubIndexUrlDao.save(pubIndexUrl);
    } catch (Exception e) {
      logger.error("成果短地址服务：保存成果短地址对象异常！PubIndexUrl={}", pubIndexUrl, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubIndexUrl pubIndexUrl) throws ServiceException {
    try {
      pubIndexUrlDao.update(pubIndexUrl);
    } catch (Exception e) {
      logger.error("成果短地址服务：更新成果短地址对象异常！PubIndexUrl={}", pubIndexUrl, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubIndexUrl pubIndexUrl) throws ServiceException {
    try {
      pubIndexUrlDao.saveOrUpdate(pubIndexUrl);
    } catch (Exception e) {
      logger.error("成果短地址服务：更新或保存成果短地址对象异常！PubIndexUrl={}", pubIndexUrl, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long pubId) throws ServiceException {
    try {
      pubIndexUrlDao.delete(pubId);
    } catch (Exception e) {
      logger.error("成果短地址服务：删除成果短地址对象异常！pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubIndexUrl pubIndexUrl) throws ServiceException {
    try {
      pubIndexUrlDao.delete(pubIndexUrl);
    } catch (Exception e) {
      logger.error("成果短地址服务：删除短地址对象异常！PubIndexUrl={}", pubIndexUrl, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String producePubShortUrl(Long pubId, String type) throws ServiceException {
    String shortUrl = "";
    // 构建参数(个人成果设置grpId=NULL)
    Map<String, Object> parameters = buildParameters(pubId, null, type);
    // 访问Open系统接口获取ShortUrl
    Object obj = restTemplate.postForObject(SERVER_URL, parameters, Object.class);
    // 接口返回数据处理
    Map<String, Object> objMap = JacksonUtils.jsonToMap(obj.toString());
    // 获取短地址值
    if (objMap.get("result") != null) {
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> list = (List<Map<String, Object>>) objMap.get("result");
      if (list != null && list.size() > 0 && list.get(0).get("shortUrl") != null) {
        shortUrl = list.get(0).get("shortUrl").toString();
      }
    }
    return shortUrl;
  }


  /**
   * 构建参数
   * 
   * @param Id
   * @param type
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> buildParameters(Long pubId, Long grpId, String type) throws ServiceException {
    Map<String, Object> map = new HashedMap();
    Map<String, Object> dataMap = new HashedMap();
    Map<String, Object> shortUrlParametMap = new HashedMap();

    map.put("openid", "99999999");
    map.put("token", "00000000sht22url");

    dataMap.put("createPsnId", "0");
    dataMap.put("type", type);
    shortUrlParametMap.put("des3PubId", ServiceUtil.encodeToDes3(pubId.toString()));
    if (grpId != null) {
      shortUrlParametMap.put("des3GrpId", ServiceUtil.encodeToDes3(grpId.toString()));
    }
    dataMap.put("shortUrlParamet", JacksonUtils.mapToJsonStr(shortUrlParametMap));
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }


}
