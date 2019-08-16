package com.smate.center.open.service.wechat.mass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.wechat.mass.Filter;
import com.smate.center.open.model.wechat.mass.Voice;
import com.smate.center.open.model.wechat.mass.VoiceMessage;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 语音消息服务.
 * 
 * @author xys
 *
 */
@Service("voiceMessageService")
@Transactional(rollbackFor = Exception.class)
public class VoiceMessageServiceImpl extends AbstractMass {

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> buildMessage(Map<String, Object> paramMap) throws OpenException {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    boolean isToGroup = (boolean) paramMap.get(MsgSendingConstant.IS_TO_GROUP_KEY);
    VoiceMessage voiceMessage = new VoiceMessage();
    if (isToGroup) {// 根据分组进行群发
      voiceMessage.setFilter((Filter) paramMap.get(MsgSendingConstant.FILTER_KEY));
    } else {
      voiceMessage.setTouser((List<String>) paramMap.get(MsgSendingConstant.LIST_TOUSER_KEY));
    }
    String mediaId = "";
    voiceMessage.setVoice(new Voice(mediaId));
    voiceMessage.setMsgtype(MsgSendingConstant.MSG_TYPE_VOICE);
    resultMap.put(MsgSendingConstant.MSG_TOBESENT_KEY, JacksonUtils.jsonObjectSerializer(voiceMessage));
    return resultMap;
  }

}
