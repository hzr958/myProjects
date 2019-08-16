package com.smate.center.batch.service.dynamic;

import java.util.List;

import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;



/**
 * 动态刷新Service
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public interface DynTaskService {

  /**
   * 动态任务执行
   * 
   * @author lxz
   */
  public void executeTask(Integer size) throws Exception;

  /**
   * 动态处理接口
   * 
   * @param object
   * @author lxz
   */
  public void executeDyn(InspgDynamicRefresh object) throws Exception;

  /**
   * 按照Size获取需要刷新的记录
   * 
   * @param size
   * @return
   * @throws Exception
   * @author lxz
   */
  public List<InspgDynamicRefresh> getRefreshListBySize(Integer size) throws Exception;

  /**
   * 保存错误刷新
   * 
   * @param refresh
   * @throws Exception
   * @author lxz
   */
  public void saveErrorData(InspgDynamicRefresh refresh) throws Exception;

}
