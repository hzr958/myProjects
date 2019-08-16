package com.smate.sie.center.task.service;

import com.smate.sie.center.task.model.ImportInsDataError;

/**
 * 批量创建单位，错误日志service
 * 
 * @author hd
 *
 */
public interface ImportInsDataErrorService {
  /**
   * 保存
   * 
   * @param error
   */
  public void saveObject(ImportInsDataError error);

}
