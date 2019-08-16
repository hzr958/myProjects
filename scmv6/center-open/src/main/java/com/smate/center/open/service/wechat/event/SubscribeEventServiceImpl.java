package com.smate.center.open.service.wechat.event;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.constant.wechat.MsgReceivingConstant;
import com.smate.center.open.constant.wechat.MsgSendingConstant;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.service.wechat.custom.CustomService;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;

/**
 * 订阅事件消息服务.
 * 
 * @author xys
 *
 */
public class SubscribeEventServiceImpl extends AbstractEvent {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CustomService subscribeCustomService;

  @Override
  public String giveResponse(Map<String, String> msgMap) throws OpenException {
    try {
      String fromUserName = msgMap.get(MsgReceivingConstant.FROM_USER_NAME);
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put(MsgSendingConstant.TO_USER, fromUserName);
      paramMap.put(MsgSendingConstant.REQ_URL_KEY, MsgSendingConstant.REQ_URL_COSTOM);
      paramMap.put(MsgSendingConstant.IS_CHECK_BINDING_KEY, false);
      paramMap.put(WeChatConstant.MSG_SOURCE_KEY, msgMap.get(WeChatConstant.MSG_SOURCE_KEY));
      return subscribeCustomService.sendMessage(paramMap);
    } catch (OpenException e) {
      logger.error("response subscribe error", e);
      throw new OpenException(e);
    }
  }

}
