package com.smate.center.open.service.wechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.smate.center.open.exception.OpenException;

/**
 * 消息处理服务.
 * 
 * @author xys
 *
 */
public interface MessageHandlerService {

  /**
   * 处理消息.
   * 
   * @param req 用于获取微信服务器推送的消息,若消息来源并非微信服务器，则可设置为null.
   * @param fromWeChat 消息是否来自微信服务器.
   * @param msgMap 若消息来源并非微信服务器，则通过此参数设置需要的数据，若消息来源为微信服务器，则可设置为null.
   * @return
   * @throws OpenException
   */
  public String handleMessage(HttpServletRequest req, boolean fromWeChat, Map<String, String> msgMap)
      throws OpenException;

  /**
   * 处理来自smate的消息.
   * 
   * @param msgMap 必要参数:key：MsgReceivingConstant.FROM_USER_NAME,value：openid;
   *        可填参数:key：MsgReceivingConstant.EVENT_KEY_KEY,value：事件KEY值.
   * @return
   * @throws OpenException
   */
  public String handleMsgFromSmate(Map<String, String> msgMap) throws OpenException;
}
