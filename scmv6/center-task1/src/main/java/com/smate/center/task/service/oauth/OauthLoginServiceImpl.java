package com.smate.center.task.service.oauth;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.random.RandomNumber;

/**
 * oauth系统 登录验证服务实现
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("thirdLoginService")
@Transactional(rollbackFor = Exception.class)
public class OauthLoginServiceImpl implements OauthLoginService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;


  @Override
  public Long getOpenId(String token, Long psnId, int createType) {
    Long openId = 0L;
    try {
      openId = openUserUnionDao.getOpenIdByPsnId(psnId);
      if (openId == null) {
        openId = RandomNumber.getRandomNumber(8);
        // 查重
        while (true) {
          // 99999999 表示 没有真实用户的 数据交互
          if (openId.longValue() == new Long(99999999).longValue()) {
            continue;
          }
          OpenUserUnion temp = openUserUnionDao.getOpenUserUnionByOpenId(openId);
          if (temp == null) {
            break;
          } else {
            openId = RandomNumber.getRandomNumber(8);
          }
        }
      }
      // 判断是否 有关联的第三方系统记录
      OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnion(openId, token);
      if (openUserUnion == null) {
        openUserUnion = new OpenUserUnion();
        openUserUnion.setOpenId(openId);
        openUserUnion.setPsnId(psnId);
        openUserUnion.setToken(token);
        openUserUnion.setCreateDate(new Date());
        openUserUnion.setCreateType(createType);
        openUserUnionDao.saveOpenUserUnion(openUserUnion);
      }
    } catch (Exception e) {
      logger.error("获取人员openId出错------", e);
    }
    return openId;
  }

  /**
   * 获取自动登录所需的加密串AID
   */
  @SuppressWarnings("unchecked")
  @Override
  public String getAutoLoginAID(Long openId, String autoLoginType) {
    String AID = "";
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(this.getAID(openId, autoLoginType).toString());
    if ("success".equals(resultMap.get("status"))) {
      List<Map<String, String>> resultList = (List<Map<String, String>>) resultMap.get("result");
      if (CollectionUtils.isNotEmpty(resultList)) {
        Map<String, String> map = resultList.get(0);
        if (map.get(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME) != null) {
          AID = map.get(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME).toString();
        }
      }
    }
    return AID;
  }

  /**
   * 获取AID
   * 
   * @param openId
   * @return
   */
  private Object getAID(Long openId, String autoLoginType) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    Map<String, String> dataMap = new HashMap<String, String>();
    dataMap.put("AutoLoginType", autoLoginType);
    mapDate.put("data", JacksonUtils.mapToJsonStr(dataMap));//
    mapDate.put("openid", openId);
    mapDate.put("token", "00000000ime82dt2");// 业务系统默认token
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }



}
