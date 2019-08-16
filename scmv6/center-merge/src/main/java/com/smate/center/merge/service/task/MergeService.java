package com.smate.center.merge.service.task;

/**
 * 合并任务接口.
 * 
 * @author tsz
 *
 */
public interface MergeService {
  /**
   * 检查 是否需要执行合并处理.
   * 
   * @param savePsnId not null
   * @param delPsnId not null
   * @return boolean
   * @throws Exception null
   */
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception;

  /**
   * 处理合并方法.
   * 
   * @param savePsnId not null
   * @param delPsnId not null
   * @return boolean
   * @throws Exception null
   */
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception;

  /**
   * 开始方法.
   * 
   * @param savePsnId not null
   * @param delPsnId not null
   * @throws Exception null
   */
  public void runMerge(Long savePsnId, Long delPsnId) throws Exception;
}
