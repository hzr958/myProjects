package com.smate.center.oauth.service.mobile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.dao.profile.OpenUserUnionDao;
import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.model.consts.SmateLoginConsts;
import com.smate.center.oauth.service.login.OauthLoginService;
import com.smate.core.base.utils.common.BeanUtils;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.dao.shorturl.OpenShortUrlDao;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.dao.wechat.WeChatRelationHisDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.shorturl.OpenShortUrl;
import com.smate.core.base.utils.model.wechat.WeChatRelation;
import com.smate.core.base.utils.model.wechat.WeChatRelationHis;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.wechat.OAuth2Service;
import com.smate.core.base.utils.wechat.WeChatRelationService;

/**
 * 移动端登录服务实现类
 *
 * @author wsn
 * @createTime 2017年6月6日 下午6:32:47
 *
 */
@Service("mobileLoginService")
@Transactional(rollbackFor = Exception.class)
public class MobileLoginServiceImpl implements MobileLoginService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WeChatRelationService weChatRelationService;
  @Autowired
  private OauthLoginService oauthLoginService;
  @Autowired
  private OAuth2Service oAuth2Service;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Value("${initOpen.restful.url_forwechat}")
  private String SERVER_URL_FOR_WECHART;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  private String runEnv = System.getenv("RUN_ENV");
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private OpenShortUrlDao openShortUrlDao;
  @Autowired
  private WeChatRelationHisDao weChatRelationHisDao;

  // 尝试通过wxopenid获取人员id
  @Override
  public Map<String, Object> buildAIDByWxOpenId(String wxOpenId) throws OauthException {
    String AID = null;
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(wxOpenId)) {
        // 获取人员id
        Long psnId = weChatRelationService.querypsnIdByWeChatOpenid(wxOpenId);
        if (psnId != null) {
          Long openId = oauthLoginService.getOpenId(SmateLoginConsts.SNS_DEFAULT_TOKEN, psnId,
              SmateLoginConsts.SNS_OPENID_TYPE_SIX);
          if (openId != null && openId != 0L) {
            AID = oauthLoginService.getAutoLoginAID(openId, SmateLoginConsts.SNS_REMEMBER_ME);
          }
        } else {
          result.put("msg", "can_not_find_psnId");
          result.put("des3WxOpenId", Des3Utils.encodeToDes3(wxOpenId));
        }
        result.put("AID", AID);
      }
    } catch (Exception e) {
      logger.error("通过wxopenId获取AID出错，wxopenId=" + wxOpenId, e);
      throw new OauthException(e);
    }
    return result;
  }

  // 尝试通过wxunionid构建AID
  @Override
  public Map<String, Object> buildAIDByWxUnionId(String wxUnionId) throws OauthException {
    String AID = null;
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(wxUnionId)) {
        // 获取关联的账号的openId
        Long smateOpenId = weChatRelationService.findSmateOpenIdByWxUnionId(wxUnionId);
        if (smateOpenId != null && smateOpenId != 0L) {
          AID = oauthLoginService.getAutoLoginAID(smateOpenId, SmateLoginConsts.SNS_REMEMBER_ME);
        } else {
          result.put("msg", "can_not_find_relation_smate_openId");
          result.put("des3WxUnionId", Des3Utils.encodeToDes3(wxUnionId));
        }
        result.put("AID", AID);
      }
    } catch (Exception e) {
      logger.error("通过wxUnionId获取AID出错，wxUnionId=" + wxUnionId, e);
      throw new OauthException(e);
    }
    return result;
  }

  // 尝试通过微信code参数获取AID
  @Override
  public Map<String, Object> buildAIDByWxCode(String code) throws OauthException {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(code)) {
        // 通过code获取wxOpenId
        String weChatOpenId = oAuth2Service.getWeChatOpenId(code);
        // 通过公众号access_token和wxOpenId获取用户信息，获取unionId
        String weChatToken = this.getAccessToken();
        logger.warn("获取微信openId为：" + weChatOpenId + ", accessToken为：" + weChatToken);
        Map<String, Object> weChatInfo = oAuth2Service.getWeChatInfo(weChatToken, weChatOpenId);
        if (weChatInfo != null && weChatInfo.get("unionid") != null) {
          String wxUnionId = ObjectUtils.toString(weChatInfo.get("unionid"));
          // 用wxUnionId查询关联的科研之友账号，构建AID自动登录
          result = this.buildAIDByWxUnionId(wxUnionId);
          result.put("des3WxOpenId", Des3Utils.encodeToDes3(ObjectUtils.toString(weChatOpenId)));
          result.put("des3WxUnionId", Des3Utils.encodeToDes3(wxUnionId));
        } else {
          logger.info("获取微信unionId失败或为空， wechatToken=" + weChatToken + ", wxOpenId=" + weChatOpenId + ", errorMsg = "
              + (weChatInfo != null ? ObjectUtils.toString(weChatInfo.get("errmsg")) : ""));
        }
      }
    } catch (Exception e) {
      logger.error("通过微信code获取AID出错， code=" + code, e);
      throw new OauthException(e);
    }
    return result;
  }

  @Override
  public void cancelBlind(Long psnId, String wxUnionId) throws OauthException {
    try {
      if (StringUtils.isNotBlank(wxUnionId)) {
        WeChatRelation weChatRelation = weChatRelationDao.getByUnionId(wxUnionId);
        if (weChatRelation != null) {
          WeChatRelationHis weChatRelationHis = new WeChatRelationHis();
          BeanUtils.copyProperties(weChatRelationHis, weChatRelation);
          weChatRelationDao.cancelBindByUnionId(wxUnionId);
          weChatRelationHis.setDelTime(new Date());
          weChatRelationHisDao.insertRelData(weChatRelationHis);
        }
      } else {
        Long openId = openUserUnionDao.getOpenIdByPsnId(psnId);
        if (openId != null) {
          WeChatRelation weChatRelation = weChatRelationDao.getBySmateOpenId(openId);
          if (weChatRelation != null) {
            WeChatRelationHis weChatRelationHis = new WeChatRelationHis();
            BeanUtils.copyProperties(weChatRelationHis, weChatRelation);
            weChatRelationDao.cancelBindBySmateId(openId);
            weChatRelationHis.setDelTime(new Date());
            weChatRelationHisDao.insertRelData(weChatRelationHis);
          }
        }
      }

    } catch (Exception e) {
      logger.error("解除绑定失败" + psnId, e);
      throw new OauthException(e);
    }
  }

  private String getAccessToken() throws Exception {
    String accessToken = ObjectUtils
        .toString(oauthCacheService.get(WeChatConstant.ACCESS_TOKEN_CACHE_NAME, getAccessTokenCacheKey(runEnv)));
    if (StringUtils.isBlank(accessToken)) {
      Map<String, Object> map = new HashMap<String, Object>();
      HashMap<Object, Object> dataMap = new HashMap<>();
      // 设置固定值获取微信授权码，详见文档：科研之友开放平台API-2.7获取微信授权码
      map.put("openid", 99999999L);
      map.put("token", "000000007c630c84");
      dataMap.put("type", runEnv);
      map.put("data", JacksonUtils.mapToJsonStr(dataMap));
      String accessTokenObj = (restTemplate.postForObject(SERVER_URL_FOR_WECHART, map, Object.class)).toString();
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

  private String getAccessTokenCacheKey(String runEnv) {
    switch (runEnv.toLowerCase()) {
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

  @Override
  public String buildTargetUrlInWechat(String wxCode) throws OauthException {
    try {

    } catch (Exception e) {
      logger.error("从微信端进入科研之友出错", e);
      throw new OauthException(e);
    }
    return null;
  }

  @Override
  public OpenShortUrl getOpenShortUrlByShortUrl(String shortUrl) {
    try {
      return openShortUrlDao.getOpenShortUrlByShortUrl(shortUrl);
    } catch (Exception e) {
      logger.error("移动端获取短地址实体类异常", e);
      throw new OauthException(e);
    }
  }
}
