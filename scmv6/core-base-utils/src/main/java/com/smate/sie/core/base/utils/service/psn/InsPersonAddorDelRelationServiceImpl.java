package com.smate.sie.core.base.utils.service.psn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ObjectUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 关联帐号或取消关联帐号接口服务实现
 * 
 * @author hd
 *
 */
@Service("insPersonAddorDelRelationService")
@Transactional(rollbackFor = Exception.class)
public class InsPersonAddorDelRelationServiceImpl implements InsPersonAddorDelRelationService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  @SuppressWarnings({"unchecked"})
  @Override
  public Map<String, Object> addRelation(Long psnId, String targettoken) throws SysServiceException {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      if (psnId != null && StringUtils.isNotBlank(targettoken)) {
        // 构造接口数据
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("psnId", psnId);
        mapData.put("targetToken", targettoken);
        mapData.put("relation", "true");
        // 构造调用接口的参数
        Map<String, Object> mapOpen = new HashMap<String, Object>();
        mapOpen.put("openid", 99999999L);// 系统默认openId
        mapOpen.put("token", "111111115h6jjk9b");
        mapOpen.put("data", JacksonUtils.mapToJsonStr(mapData));
        resultMap = JacksonUtils.jsonToMap(restTemplate.postForObject(SERVER_URL, mapOpen, Object.class).toString());
        if (!"success".equals(resultMap.get("status"))) {
          logger.error("调用接口(5h6jjk9b):添加人员和单位的关联返回错误结果,psnId:{},targettoken{}", new Object[] {psnId, targettoken});
          throw new SysServiceException("调用接口(5h6jjk9b):添加人员和单位的关联返回错误结果,psnId： " + psnId + " ,targettoken:"
              + targettoken + ";" + resultMap.get("msg"));
        }
        return resultMap;
      } else {
        logger.error("添加人员和单位的关联失败，psnId 和 targettoken不能为空；psnId:{},targettoken{}", new Object[] {psnId, targettoken});
        throw new SysServiceException(
            "添加人员和单位的关联失败，psnId 和 targettoken不能为空；psnId： " + psnId + " ,targettoken:" + targettoken);
      }
    } catch (Exception e) {
      logger.error("调用接口5h6jjk9b:添加人员和单位的关联异常，psnId:{},targettoken{} ", new Object[] {psnId, targettoken});
      throw new SysServiceException("调用接口5h6jjk9b:添加人员和单位的关联异常；psnId： " + psnId + " ,targettoken:" + targettoken, e);
    }
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public Map<String, Object> delRelation(Long psnId, String targettoken) throws SysServiceException {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      if (psnId != null && StringUtils.isNotBlank(targettoken)) {
        // 构造接口数据
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("psnId", psnId);
        mapData.put("targetToken", targettoken);
        mapData.put("relation", "false");
        // 构造调用接口的参数
        Map<String, Object> mapOpen = new HashMap<String, Object>();
        mapOpen.put("openid", 99999999L);// 系统默认openId
        mapOpen.put("token", "111111115h6jjk9b");
        mapOpen.put("data", JacksonUtils.mapToJsonStr(mapData));
        resultMap = JacksonUtils.jsonToMap(restTemplate.postForObject(SERVER_URL, mapOpen, Object.class).toString());
        if (!"success".equals(resultMap.get("status"))) {
          logger.error("调用接口(5h6jjk9b):移除人员和单位的关联返回错误结果,psnId:{},targettoken{}", new Object[] {psnId, targettoken});
          throw new SysServiceException(
              "调用接口(5h6jjk9b):移除人员和单位的关联返回错误结果,psnId： " + psnId + " ,targettoken:" + targettoken);
        }
        return resultMap;
      } else {
        logger.error("移除人员和单位的关联失败，psnId 和 targettoken不能为空；psnId:{},targettoken{}", new Object[] {psnId, targettoken});
        throw new SysServiceException("移除人员和单位的关联失败，psnId 和 targettoken不能为空；psnId： " + psnId + " ,targettoken:"
            + targettoken + ";" + resultMap.get("msg"));
      }
    } catch (Exception e) {
      logger.error("调用接口5h6jjk9b:移除人员和单位的关联异常，psnId:{},targettoken{} ", new Object[] {psnId, targettoken});
      throw new SysServiceException("调用接口5h6jjk9b:移除人员和单位的关联异常；psnId： " + psnId + " ,targettoken:" + targettoken, e);
    }
  }

  @SuppressWarnings({"unchecked", "deprecation", "rawtypes"})
  @Override
  public String getInsToken(Long insId) throws SysServiceException {
    String token = null;
    try {
      Map<String, Object> postData = new HashMap<String, Object>();
      postData.put("openid", 99999999L);// 系统默认openId
      postData.put("token", "11111111yung90kk");// 系统默认token
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("insId", insId);
      postData.put("data", JacksonUtils.mapToJsonStr(data));
      Map<String, Object> resultMap =
          JacksonUtils.jsonToMap(restTemplate.postForObject(SERVER_URL, postData, Object.class).toString());
      if ("success".equals(resultMap.get("status"))) {
        List result = (List) resultMap.get("result");
        Map tokenMap = (Map) result.get(0);
        token = ObjectUtils.toString(tokenMap.get("token"));
      } else {
        logger.error("获取单位token接口（yung90kk）返回错误结果,insId:{}", new Object[] {insId});
        throw new SysServiceException("获取单位token接口（yung90kk）返回错误结果,insId:" + insId);
      }
      return token;
    } catch (Exception e) {
      logger.error("获取单位token出现异常,insId={}", insId, e);
      throw new SysServiceException("获取单位token出现异常", e);

    }
  }

}
