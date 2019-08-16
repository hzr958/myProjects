package com.smate.center.open.service.wechat.template;

import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;

/**
 * 模板消息接口服务.
 * 
 * @author xys
 *
 */
public interface TemplateMsgService {

  /**
   * 发送消息.
   * 
   * @param paramMap 必要参数： key：WeChatConstant.ACCESS_TOKEN_KEY,value：access token;
   *        key：WeChatConstant.TEMPLATE_MSG_KEY,value：json格式的template message.
   * @return
   * @throws SysServiceException
   */
  public String sendMessage(Map<String, Object> paramMap) throws SysServiceException;
}
