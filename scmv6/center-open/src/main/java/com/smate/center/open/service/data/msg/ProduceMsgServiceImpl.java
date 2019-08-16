package com.smate.center.open.service.data.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.msg.CreateMsgService;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 消息产生服务实现
 * 
 * @author zzx
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ProduceMsgServiceImpl extends ThirdDataTypeBase {

  private Map<String, CreateMsgService> createMsgServiceMap;

  public Map<String, CreateMsgService> getCreateMsgServiceMap() {
    return createMsgServiceMap;
  }

  public void setCreateMsgServiceMap(Map<String, CreateMsgService> createMsgServiceMap) {
    this.createMsgServiceMap = createMsgServiceMap;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();

    if (paramet == null || paramet.size() == 0) {
      logger.error("产生消息服务，paramet为空！paramet=" + paramet);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_704, paramet, "SCM_704 产生消息服务，paramet为空！");
      return temp;
    }
    Map<String, Object> dateMap = JacksonUtils.jsonToMap(paramet.get("data").toString());
    paramet.putAll(dateMap);
    if (dateMap == null || dateMap.size() == 0) {
      logger.error("产生消息服务，dateMap为空！dateMap=" + paramet);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_704, paramet, "SCM_704 产生消息服务，dateMap为空！");
      return temp;
    }
    Object msgType = dateMap.get(MsgConstants.MSG_TYPE);
    if (msgType == null || StringUtils.isBlank(msgType.toString())) {
      logger.error("产生消息服务，消息类型msgType为空！paramet=" + paramet);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_700, paramet, "SCM_700  产生消息服务，消息类型msgType为空！");
      return temp;
    }
    CreateMsgService msgService = createMsgServiceMap.get(msgType);
    if (msgService == null) {
      logger.error("产生消息服务，msgType类型不正确！paramet=" + paramet);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_705, paramet, "SCM_705  产生消息服务，msgType类型不正确");
      return temp;
    }

    Object senderId = dateMap.get(MsgConstants.MSG_SENDER_ID);
    if (senderId == null || !NumberUtils.isNumber(senderId.toString())) {
      logger.error("产生消息服务，senderId为空或不是数字类型！paramet=" + paramet);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_701, paramet, "SCM_701  产生消息服务，senderId为空或不是数字类型！");
      return temp;
    }
    Object receiverIds = dateMap.get(MsgConstants.MSG_RECEIVER_IDS);
    if (receiverIds == null || StringUtils.isBlank(receiverIds.toString())) {
      logger.error("产生消息服务，RECEIVER_IDS为空！paramet=" + paramet);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_702, paramet, "SCM_702  产生消息服务，RECEIVER_IDS为空！");
      return temp;
    }
    // 判断接收者Id是否为数字，只要有一个为数字就可以。
    boolean receiverIdsFlag = false;
    String[] receiverList = receiverIds.toString().split(",");
    for (String receiverId : receiverList) {
      if (NumberUtils.isNumber(receiverId)) {
        receiverIdsFlag = true;
      }
    }
    if (!receiverIdsFlag) {
      logger.error("产生消息服务，RECEIVER_IDS必须要有一个接收者Id 且为数字！paramet=" + paramet);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_702, paramet, "SCM_702  产生消息服务，RECEIVER_IDS必须要有一个接收者Id且为数字！");
      return temp;
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

      Map<String, Object> resultMap = new HashMap<String, Object>();
      resultMap = createMsgServiceMap.get(paramet.get(MsgConstants.MSG_TYPE)).provideHandle(paramet);
      dataList.add(resultMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "成功产生open消息！！！");// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("open产生消息服务出错！paramet=" + paramet, e);
      return super.errorMap(OpenMsgCodeConsts.SCM_703, paramet, "open产生消息服务，构建具体消息出错！！");
    }
    // return super.successMap(OpenConsts.RESULT_STATUS_SUCCESS,
    // "成功产生open消息！！！");
  }

}
