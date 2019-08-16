package com.smate.center.open.service.wechat.mass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.wechat.mass.Filter;
import com.smate.center.open.model.wechat.mass.MPVideo;
import com.smate.center.open.model.wechat.mass.MPVideoMessage;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 视频消息服务.
 * 
 * @author xys
 *
 */
@Service("mpvideoMessageService")
@Transactional(rollbackFor = Exception.class)
public class MPVideoMessageServiceImpl extends AbstractMass {

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> buildMessage(Map<String, Object> paramMap) throws OpenException {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    boolean isToGroup = (boolean) paramMap.get(MsgSendingConstant.IS_TO_GROUP_KEY);
    MPVideoMessage mpvideoMessage = new MPVideoMessage();
    if (isToGroup) {// 根据分组进行群发
      mpvideoMessage.setFilter((Filter) paramMap.get(MsgSendingConstant.FILTER_KEY));
    } else {
      mpvideoMessage.setTouser((List<String>) paramMap.get(MsgSendingConstant.LIST_TOUSER_KEY));
    }
    String mediaId = "";
    mpvideoMessage.setMpvideo(new MPVideo(mediaId));
    mpvideoMessage.setMsgtype(MsgSendingConstant.MSG_TYPE_MPVIDEO);
    resultMap.put(MsgSendingConstant.MSG_TOBESENT_KEY, JacksonUtils.jsonObjectSerializer(mpvideoMessage));
    return resultMap;
  }

}
