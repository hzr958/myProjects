package com.smate.center.open.service.msg;

import java.util.Map;

/**
 * 创建消息接口
 * 
 * @author zzx
 *
 */
public interface CreateMsgService {
  /**
   * 处理方法，调用3个抽象方法
   * 
   * @param parameter
   */
  Map<String, Object> provideHandle(Map<String, Object> parameter) throws Exception;

}
