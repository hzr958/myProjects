package com.smate.center.open.service.wechat.custom;

import java.util.Map;

import com.smate.center.open.exception.OpenException;

/**
 * 客服接口服务.
 * 
 * @author xys
 *
 */
public interface CustomService {
  public String sendMessage(Map<String, Object> paramMap) throws OpenException;
}
