package com.smate.center.open.service.wechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.constant.wechat.MsgReceivingConstant;
import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.service.wechat.event.EventService;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.wechat.MessageUtil;

/**
 * 消息处理服务.
 * 
 * @author xys
 *
 */
@Transactional(rollbackFor = Exception.class)
public class MessageHandlerServiceImpl implements MessageHandlerService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private Map<String, EventService> eventServiceMap;
  private String bindUrl;

  @Override
  public String handleMessage(HttpServletRequest req, boolean fromWeChat, Map<String, String> msgMap)
      throws OpenException {
    String result = "";
    try {
      if (fromWeChat) {
        msgMap = MessageUtil.parseXml(req);
      }
      if (msgMap == null) {
        return result;
      }
      String msgType = msgMap.get(MsgReceivingConstant.MSG_TYPE_KEY);
      switch (msgType) {
        case MsgReceivingConstant.MSG_TYPE_EVENT:
          result = this.handleEvent(msgMap, fromWeChat);
          break;
        case MsgReceivingConstant.MSG_TYPE_TEXT:

          break;
        case MsgReceivingConstant.MSG_TYPE_IMAGE:

          break;
        case MsgReceivingConstant.MSG_TYPE_VOICE:

          break;
        case MsgReceivingConstant.MSG_TYPE_VIDEO:

          break;
        case MsgReceivingConstant.MSG_TYPE_SHORTVIDEO:

          break;
        case MsgReceivingConstant.MSG_TYPE_LOCATION:

          break;
        case MsgReceivingConstant.MSG_TYPE_LINK:

          break;

        default:
          break;
      }
    } catch (Exception e) {
      logger.error("handle message error");
      throw new OpenException(e);
    }
    return result;
  }

  /**
   * 处理事件消息.
   * 
   * @param msgMap
   * @param fromWeChat
   * @return
   */
  private String handleEvent(Map<String, String> msgMap, boolean fromWeChat) {
    String result = "";
    EventService eventService = eventServiceMap.get(msgMap.get(MsgReceivingConstant.EVENT_TYPE_KEY));
    if (eventService != null) {
      msgMap.put(MsgSendingConstant.BIND_URL_KEY, bindUrl);
      msgMap.put(WeChatConstant.MSG_SOURCE_KEY,
          fromWeChat ? WeChatConstant.MSG_SOURCE_WECHAT : WeChatConstant.MSG_SOURCE_SMATE);
      result = eventService.handleEvent(msgMap);
    }
    return result;
  }

  @Override
  public String handleMsgFromSmate(Map<String, String> msgMap) throws OpenException {
    msgMap.put(MsgReceivingConstant.MSG_TYPE_KEY, MsgReceivingConstant.MSG_TYPE_EVENT);
    msgMap.put(MsgReceivingConstant.EVENT_TYPE_KEY, MsgReceivingConstant.EVENT_TYPE_CLICK);
    String result = handleMessage(null, false, msgMap);
    return result;
  }

  public void setEventServiceMap(Map<String, EventService> eventServiceMap) {
    this.eventServiceMap = eventServiceMap;
  }


  public void setBindUrl(String bindUrl) {
    this.bindUrl = bindUrl;
  }

}
