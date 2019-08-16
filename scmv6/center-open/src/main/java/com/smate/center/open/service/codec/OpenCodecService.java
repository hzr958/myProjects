package com.smate.center.open.service.codec;

import java.util.Map;

/**
 * open加密解密服务接口.
 * 
 * @author tsz
 *
 */
public interface OpenCodecService {

  /**
   * 检查解密情况.
   * 
   * @param map
   */
  public void checkParameterDecode(Map<String, Object> map);

  /**
   * 检查加密情况.
   * 
   * @param map
   */
  public void checkParameterEncode(Map<String, Object> map, Map<String, Object> resutlMap);
}
