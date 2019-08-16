package com.smate.center.batch.service.dynamic;

import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;



/**
 * 动态构建执行链-接口
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public interface ExecuteTaskService {
  /**
   * 判断
   * 
   * @param obj
   * @return
   * @throws Exception
   * @author lxz
   */
  public boolean isThisDyn(InspgDynamicRefresh obj) throws Exception;

  /**
   * 执行
   * 
   * @param obj
   * @throws Exception
   * @author lxz
   */
  public void build(InspgDynamicRefresh obj) throws Exception;
}
