package com.smate.center.open.service.wechat.event;

import java.util.Map;

import com.smate.center.open.exception.OpenException;

/**
 * 抽象事件消息.
 * 
 * @author xys
 *
 */
public abstract class AbstractEvent implements EventService {

  public abstract String giveResponse(Map<String, String> msgMap) throws OpenException;

  @Override
  public String handleEvent(Map<String, String> msgMap) throws OpenException {
    return giveResponse(msgMap);
  }

}
