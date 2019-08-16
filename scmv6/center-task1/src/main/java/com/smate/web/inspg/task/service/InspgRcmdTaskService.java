package com.smate.web.inspg.task.service;

import java.util.List;

import com.smate.core.base.utils.exception.RcmdTaskException;

/**
 * inspg推荐任务
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface InspgRcmdTaskService {

  /**
   * 取得推荐Inspg的需要推荐的用户列表
   * 
   * @param psnId
   * @return
   * @throws InspgException
   * 
   */
  public List<Long> getPsnIdList(Long psnId, Integer size) throws RcmdTaskException;

  /**
   * 为用户List推荐Inspg
   * 
   * @param psnId
   * @return
   * @throws InspgException
   * 
   */
  public void rcmdInspgResult(List<Long> psnId) throws RcmdTaskException;


}
