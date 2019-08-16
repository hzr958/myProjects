package com.smate.center.open.service.data;

import java.util.Map;

import com.smate.center.open.exception.OpenException;

/**
 * 开放数据 服务接口
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface ThirdDataService {

  /**
   * 取数据统一入口
   * 
   * @param map
   * @return
   * @throws Exception
   */
  public Map<String, Object> handleOpenData(Map<String, Object> map) throws OpenException, Exception;
}
