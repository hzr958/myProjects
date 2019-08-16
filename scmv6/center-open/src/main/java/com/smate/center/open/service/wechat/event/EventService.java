package com.smate.center.open.service.wechat.event;

import java.util.Map;

import com.smate.center.open.exception.OpenException;

/**
 * 事件消息服务.
 * 
 * @author xys
 *
 */
public interface EventService {

  /**
   * 处理事件消息.
   * 
   * @param msgMap
   * @return
   * @throws OpenException
   */
  public String handleEvent(Map<String, String> msgMap) throws OpenException;
}
