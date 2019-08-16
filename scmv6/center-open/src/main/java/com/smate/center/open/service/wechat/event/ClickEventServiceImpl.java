package com.smate.center.open.service.wechat.event;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.open.constant.wechat.MsgReceivingConstant;
import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.service.wechat.custom.CustomService;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;

/**
 * 点击事件消息服务.
 * 
 * @author xys
 *
 */
public class ClickEventServiceImpl extends AbstractEvent {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private Map<String, CustomService> customServiceMap;

  @Override
  public String giveResponse(Map<String, String> msgMap) throws OpenException {
    try {
      String result = "";
      CustomService customService = customServiceMap.get(msgMap.get(MsgReceivingConstant.EVENT_KEY_KEY));
      if (customService != null) {
        String fromUserName = msgMap.get(MsgReceivingConstant.FROM_USER_NAME);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(MsgSendingConstant.TO_USER, fromUserName);
        paramMap.put(MsgSendingConstant.REQ_URL_KEY, MsgSendingConstant.REQ_URL_COSTOM);
        paramMap.put(MsgSendingConstant.BIND_URL_KEY, msgMap.get(MsgSendingConstant.BIND_URL_KEY));
        paramMap.put(WeChatConstant.MSG_SOURCE_KEY, msgMap.get(WeChatConstant.MSG_SOURCE_KEY));
        paramMap.put(MsgReceivingConstant.EVENT_KEY_KEY, msgMap.get(MsgReceivingConstant.EVENT_KEY_KEY));
        result = customService.sendMessage(paramMap);
      }
      return result;
    } catch (Exception e) {
      logger.error("response click error", e);
      throw new OpenException(e);
    }
  }

  public void setCustomServiceMap(Map<String, CustomService> customServiceMap) {
    this.customServiceMap = customServiceMap;
  }

}
