package com.smate.center.batch.service;

import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.batch.dao.sns.wechat.WeChatMessageHistoryPsnDao;
import com.smate.center.batch.dao.sns.wechat.WeChatPreProcessPsnDao;
import com.smate.center.batch.dao.sns.wechat.template.SmateWeChatTemplateDao;
import com.smate.center.batch.model.sns.wechat.WeChatMessageHistoryPsn;
import com.smate.center.batch.model.sns.wechat.WeChatPreProcessPsn;
import com.smate.center.batch.service.utils.BatchRestfulUtilsService;
import com.smate.center.batch.service.wechat.template.TemplateMsgService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.wechat.TmpMsgConstant;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.spring.SpringUtils;

/**
 * 微信发送任务service接口实现
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
@Transactional(rollbackFor = Exception.class)
public class WeChatMsgPsnServiceImpl implements WeChatMsgPsnService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private Map<String, WeChatMsgMapBuildService> mapBuilders = null;

  @Autowired
  private WeChatPreProcessPsnDao weChatPreProcessPsnDao;

  @Autowired
  private WeChatMessageHistoryPsnDao weChatMessageHistoryPsnDao;

  @Autowired
  private SmateWeChatTemplateDao smateWeChatTemplateDao;

  @Autowired
  private WeChatRelationDao weChatRelationDao;

  @Autowired
  private BatchRestfulUtilsService batchRestfulUtilsService;

  @Autowired
  private TemplateMsgService templateMsgService;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  private String runEnv = System.getenv("RUN_ENV");

  @Autowired
  private CacheService cacheService;

  /**
   * id查询V_BATCH_PRE_WECHAT_PSN表中未处理的记录
   * 
   * @param Long id V_BATCH_PRE_WECHAT_PSN表中id
   * @return WeChatPreProcessPsn 消息实体
   * @version 6.0.1
   */
  @Override
  public WeChatPreProcessPsn getUnProcessedWeChatPreProcessPsnById(Long id) {
    WeChatPreProcessPsn weChatPreProcessPsn = weChatPreProcessPsnDao.getUnProcessedWeChatPreProcessPsnById(id);
    return weChatPreProcessPsn;
  }

  /**
   * id查询V_BATCH_PRE_WECHAT_PSN表中的记录，无视发送状态
   * 
   * @param Long id V_BATCH_PRE_WECHAT_PSN表中id
   * @return WeChatPreProcessPsn 消息实体
   * @version 6.0.1
   */
  @Override
  public WeChatPreProcessPsn getWeChatPreProcessPsnById(Long id) {
    WeChatPreProcessPsn weChatPreProcessPsn = weChatPreProcessPsnDao.getWeChatPreProcessPsnById(id);
    return weChatPreProcessPsn;
  }

  /**
   * 更新记录状态，发送消息成功
   * 
   * @param Long msgId V_BATCH_PRE_WECHAT_PSN表中id
   * @return WeChatPreProcessPsn 消息实体
   * @version 6.0.1
   */
  @Override
  public void updateStatusSuccess(Long msgId) {
    WeChatPreProcessPsn weChatPreProcessPsn = weChatPreProcessPsnDao.getWeChatPreProcessPsnById(msgId);
    weChatPreProcessPsn.setStatus(1);
    weChatPreProcessPsnDao.save(weChatPreProcessPsn);
    // 也更新V_BATCH_WECHAT_HISTORY_PSN中的状态
    WeChatMessageHistoryPsn weChatMessageHistoryPsn = weChatMessageHistoryPsnDao.getWeChatMessageHistoryPsnById(msgId);
    if (weChatMessageHistoryPsn != null) {
      weChatMessageHistoryPsn.setStatus(1);
      weChatMessageHistoryPsnDao.save(weChatMessageHistoryPsn);
    }
  }

  /**
   * 更新记录状态，发送消息出错
   * 
   * @param Long V_BATCH_PRE_WECHAT_PSN表中id
   * @return
   * @version 6.0.1
   */
  @Override
  public void updateStatusError(Long msgId) {
    WeChatPreProcessPsn weChatPreProcessPsn = weChatPreProcessPsnDao.getWeChatPreProcessPsnById(msgId);
    weChatPreProcessPsn.setStatus(2);
    weChatPreProcessPsn.setUpdateTime(new Date());
    weChatPreProcessPsnDao.save(weChatPreProcessPsn);

  }

  /**
   * 发送消息
   * 
   * @param WeChatPreProcessPsn msgPsn 消息实体
   * @return Map，微信返回发送结果List<Map>
   * @version 6.0.1
   */
  @Override
  public List<Map> sendMsgPsn(WeChatPreProcessPsn msgPsn) throws BatchTaskException, SysServiceException {
    Long openId = msgPsn.getOpenId();
    Long psnId = msgPsn.getPsnId();
    String token = msgPsn.getToken();
    Map<String, Object> map = new HashMap<String, Object>();
    HashMap<Object, Object> dataMap = new HashMap<>();

    // 设置固定值获取微信授权码，详见文档：科研之友开放平台API-2.7获取微信授权码
    map.put("openid", 99999999L);
    map.put("token", "000000007c630c84");
    dataMap.put("type", runEnv);
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    Object restfulFeedback = null;
    String accesstokenForSendMsg;
    String accessTokenCacheKey = getAccessTokenCacheKey(runEnv);
    accesstokenForSendMsg =
        ObjectUtils.toString(cacheService.get(WeChatConstant.ACCESS_TOKEN_CACHE_NAME, accessTokenCacheKey));
    if (StringUtils.isBlank(accesstokenForSendMsg)) {
      // 得到restful的返回值;注意要返回Object类型，如果返回String类型，就会导致字符串被多次转译,转化为map的时候报错
      restfulFeedback =
          restTemplate.postForObject(batchRestfulUtilsService.getInitWeChatMsgSendRestfulUrl(), map, Object.class);
      // 取得accessToken，用于发送微信消息
      accesstokenForSendMsg = this.getRequiredToken(psnId, openId, token, restfulFeedback);

      if (StringUtils.isBlank(accesstokenForSendMsg)) {
        String errorMsg = "WeChatMessagePsn发送信息错误，调用 restful后返回的AccessToken为空。科研之友openId: " + openId + "psnId: " + psnId
            + "token: " + token;
        logger.error(errorMsg);
        throw new BatchTaskException(errorMsg);

      }
      // open系统那边已经存过缓存了，不要再存了，不然本来2个小时就过期的，一直重新存，就一直不会自动过期清空，缓存的access_token就无效了
    }

    List<String> weChatOpenIdList = weChatRelationDao.findWeChatOpenIdList(openId);

    if (CollectionUtils.isEmpty(weChatOpenIdList)) {
      String errorMsg =
          "WeChatMessagePsn发送信息错误，发送对象的微信OpenId为空。科研之友openId: " + openId + "psnId: " + psnId + "token: " + token;
      logger.error(errorMsg);
      throw new BatchTaskException(errorMsg);
    }

    String content = msgPsn.getContent();
    Map msgMap = JacksonUtils.jsonToMap(content);
    // 从msgMap里边获取smateTemplateId，作为 模板Id；统一转换为Long型
    Long templateId = Long.parseLong(String.valueOf(msgMap.get(TmpMsgConstant.SMATE_TEMPLATE_ID_KEY)));
    List<Map> msgSentFeedbackMaps = new ArrayList<Map>();

    // 对应不同的模板组装不同的resultMap
    WeChatMsgMapBuildService builder = mapBuilders.get(TmpMsgConstant.SMATE_TEMPLATE_ + templateId);

    for (String weChatOpenId : weChatOpenIdList) {

      Map<String, Object> resultMap = new HashMap<String, Object>();
      resultMap = builder.buildMap(msgMap, templateId, weChatOpenId, psnId, token, accesstokenForSendMsg);

      // 发送
      Map msgSentFeedback = JacksonUtils.jsonToMap(templateMsgService.sendMessage(resultMap));

      msgSentFeedbackMaps.add(msgSentFeedback);

    }

    return msgSentFeedbackMaps;
  }

  /**
   * 获取发送消息需要的AccessToken
   * 
   * @Parameters Long psnId, Long openId, String token, String restfulFeedback
   * 
   */
  public String getRequiredToken(Long psnId, Long openId, String token, Object restfulFeedback)
      throws BatchTaskException {

    String accesstokenForSendMsg;

    if (restfulFeedback == null) {
      String errorMsg = "WeChatMessagePsn发送信息错误，调用 restful,返回值restfulFeedback为空。科研之友openId: " + openId + "; psnId: "
          + psnId + "; token: " + token;
      logger.error(errorMsg);
      throw new BatchTaskException(errorMsg);

    }
    String restfulFeedbackString = restfulFeedback.toString();

    if (StringUtils.isBlank(restfulFeedbackString)) {
      String errorMsg =
          "WeChatMessagePsn发送信息错误，调用 restful出错。科研之友openId: " + openId + "; psnId: " + psnId + "; token: " + token;
      logger.error(errorMsg);
      throw new BatchTaskException(errorMsg);
    } else {
      Map getMap = JacksonUtils.jsonToMap(restfulFeedbackString);
      // List<Object> getMap = (List<Object>)
      // JacksonUtils.jsonToMap(restfulFeedback);
      if (getMap == null) {
        String errorMsg =
            "1WeChatMessagePsn发送信息错误，转化Jason出错。科研之友openId: " + openId + "; psnId: " + psnId + "; token: " + token;
        logger.error(errorMsg);
        throw new BatchTaskException(errorMsg);
      }
      String msg = (String) getMap.get("msg");
      if (msg.equalsIgnoreCase("error")) {
        String errorMsg =
            "WeChatMessagePsn发送信息错误，调用 restful出错。科研之友openId: " + openId + "; psnId: " + psnId + "; token: " + token;
        logger.error(errorMsg);
        throw new BatchTaskException(errorMsg);
      }

      List str = (List) getMap.get("result");
      List<Map<String, String>> result = str;

      accesstokenForSendMsg = result.get(0).get("accessToken");
    }

    return accesstokenForSendMsg;
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

  public Map<String, WeChatMsgMapBuildService> getMapBuilders() {
    return mapBuilders;
  }

  public void setMapBuilders(Map<String, WeChatMsgMapBuildService> mapBuilders) {
    this.mapBuilders = mapBuilders;
  }

}
