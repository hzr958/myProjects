package com.smate.sie.center.task.service;

import com.smate.sie.center.task.model.ImportPdwhPubError;

/**
 * 基准库同步成果任务，错误日志service
 * 
 * @author jszhou
 *
 */
public interface ImportPdwhPubErrorService {
  /**
   * 保存
   * 
   * @param error
   */
  public void saveObject(ImportPdwhPubError error);

}
