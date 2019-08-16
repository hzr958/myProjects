package com.smate.web.v8pub.service.handler;

import java.util.Map;

import com.smate.web.v8pub.exception.PubHandlerException;

/**
 * 
 * @author tsz
 *
 * @date 2018年6月6日
 */
public interface PubHandlerService {

  /**
   * 成果处理方法
   * 
   * @throws PubHandlerException
   */
  public Map<String, String> pubHandle(PubDTO pub) throws PubHandlerException;

}
