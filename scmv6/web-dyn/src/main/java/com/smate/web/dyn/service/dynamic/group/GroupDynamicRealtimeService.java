package com.smate.web.dyn.service.dynamic.group;

import java.util.Map;

import com.smate.web.dyn.exception.DynGroupException;

/**
 * 动态生成 处理接口
 * 
 * @author tsz
 *
 */
public interface GroupDynamicRealtimeService {

  /**
   * 产生动态接口
   * 
   * @param paramet
   */
  public void groupDynRealtime(Map<String, Object> paramet) throws DynGroupException;
}
