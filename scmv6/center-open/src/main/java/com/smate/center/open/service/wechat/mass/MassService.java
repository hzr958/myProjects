package com.smate.center.open.service.wechat.mass;

import java.util.Map;

import com.smate.center.open.exception.OpenException;

/**
 * 群发消息接口.
 * 
 * @author xys
 *
 */
public interface MassService {
  public String sendMessage(Map<String, Object> paramMap) throws OpenException;
}
