package com.smate.center.open.service.msg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.profile.RecentSelectedDao;
import com.smate.center.open.model.OpenErrorLog;
import com.smate.center.open.model.psn.RecentSelected;
import com.smate.center.open.service.data.OpenErrorLogService;
import com.smate.core.base.privacy.service.PublicPrivacyService;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 消息产生服务父类
 * 
 * @author zzx
 *
 */
abstract class CreateMsgBase implements CreateMsgService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OpenErrorLogService openErrorLogService;
  @Value("${domainscm}")
  public String domainscm;
  @Autowired
  private RecentSelectedDao recentSelectedDao;
  @Autowired
  private PublicPrivacyService publicPrivacyService;
  @Resource(name = "restTemplate")
  public RestTemplate restTemplate;

  /**
   * 参数效验
   * 
   * @param parameter
   */
  abstract Map<String, Object> parameterVeify(Map<String, Object> paramet) throws Exception;

  /**
   * 根据类型构建消息
   * 
   * @param parameter
   * @return
   */
  abstract void buildMsg(Map<String, Object> dateMap, Long senderId, String receiverId) throws Exception;

  /**
   * 产生消息
   * 
   * @param parameter
   */
  abstract void produceMsg(Map<String, Object> dateMap, Long senderId, Long receiverId) throws Exception;

  /**
   * 处理方法，调用3个抽象方法
   */
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> provideHandle(Map<String, Object> paramet) throws Exception {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap = parameterVeify(paramet);
    // 为空则没有错误
    if (resultMap == null || resultMap.size() == 0) {
      Map<String, Object> dateMap = JacksonUtils.jsonToMap(paramet.get("data").toString());
      Long senderId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_SENDER_ID).toString());
      String receiverIdsStr = dateMap.get(MsgConstants.MSG_RECEIVER_IDS).toString();
      buildMsg(dateMap, senderId, receiverIdsStr);
      String[] arrReceiverIds = dateMap.get(MsgConstants.MSG_RECEIVER_IDS).toString().split(",");
      for (String strReceiverId : arrReceiverIds) {
        strReceiverId = StringUtils.trim(strReceiverId);
        if (permissionVeify(resultMap, paramet, senderId, NumberUtils.toLong(strReceiverId))) {
          updateRecentSelected(senderId, NumberUtils.toLong(strReceiverId));
          produceMsg(dateMap, senderId, NumberUtils.toLong(strReceiverId));
          resultMap.put(MsgConstants.MSG_RELATION_ID, dateMap.get(MsgConstants.MSG_RELATION_ID));
        }

      }
    }
    return resultMap;
  };

  /**
   * 验证权限
   * 
   * @param resultMap
   * @param senderId
   * @param receiverId
   * @return
   */
  private Boolean permissionVeify(Map<String, Object> resultMap, Map<String, Object> paramet, Long senderId,
      Long receiverId) {
    // 值校验站内信的
    if (paramet.get("msgType").toString().equals("7")) {
      Boolean canSendMsg = publicPrivacyService.canSendMsg(senderId, receiverId);
      if (!canSendMsg) {
        // 添加没有权限的psnId
        List psnIds = (List) resultMap.get(MsgConstants.MSG_NOT_PERMISSION_PSNIDS);
        if (psnIds == null) {
          psnIds = new ArrayList<Long>();
          resultMap.put(MsgConstants.MSG_NOT_PERMISSION_PSNIDS, psnIds);
        }
        psnIds.add(receiverId);
        return false;
      }
    }
    return true;
  }

  // 更新最近联系人
  public void updateRecentSelected(Long senderId, Long receiverId) {
    RecentSelected recentSelected = recentSelectedDao.getRecentSelected(senderId, receiverId);
    if (recentSelected == null) {
      recentSelected = new RecentSelected();
      recentSelected.setPsnId(senderId);
      recentSelected.setSelectedPsnId(receiverId);
    }
    recentSelected.setSelectedDate(new Date());
    recentSelectedDao.save(recentSelected);
  }

  /**
   * 公用封装错误 保持错误日志方法
   * 
   * @param msg
   * @param openData
   * @param e
   * @return
   */
  protected Map<String, Object> errorMap(String msg, Map<String, Object> openData, String e) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
    temp.put(OpenConsts.RESULT_MSG, msg);
    // 记录错误日志
    OpenErrorLog openErrorLog = new OpenErrorLog();
    openErrorLog.setErrorDate(new Date());
    openErrorLog.setErrorFlag(msg);
    openErrorLog
        .setOpenId(openData.get(OpenConsts.MAP_OPENID) == null ? null : openData.get(OpenConsts.MAP_OPENID).toString());
    openErrorLog
        .setToken(openData.get(OpenConsts.MAP_TOKEN) == null ? null : openData.get(OpenConsts.MAP_TOKEN).toString());
    openErrorLog.setErrorInfo(openData.toString() + e);
    openErrorLogService.saveOpenErrorLog(openErrorLog);
    return temp;
  }

  /**
   * 获取远程成果
   * 
   * @param pubId
   * @param pubQueryDTO
   * @return
   */
  protected Map<String, Object> getRemotePubInfo(Long pubId, PubQueryDTO pubQueryDTO) {
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();

    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity =
        new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(pubQueryDTO), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    if (object != null && object.get("status").equals(V8pubQueryPubConst.SUCCESS)) {
      List<Map<String, Object>> resultList = (List<Map<String, Object>>) object.get("resultList");
      if (resultList != null && resultList.size() > 0) {
        return resultList.get(0);
      }
    }
    return null;
  }


}
