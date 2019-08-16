package com.smate.center.open.service.data;

import java.util.Map;

import com.smate.center.open.exception.OpenException;

/**
 * 开放数据 按类型取数据接口
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface ThirdDataTypeService {
  /**
   * 按类型取不同数据
   * 
   * @param map
   * @return
   * @throws Exception
   */
  public Map<String, Object> handleOpenDataForType(Map<String, Object> map) throws OpenException;
}
