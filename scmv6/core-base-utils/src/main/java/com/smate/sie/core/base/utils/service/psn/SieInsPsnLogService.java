package com.smate.sie.core.base.utils.service.psn;

/**
 * 
 * @author yxs
 * @descript 保存日志
 */
public interface SieInsPsnLogService {
  /**
   * 保存操作日志.
   * 
   * 
   */
  public void log(Long opPsn, Long opIns, Long targetPsn, String action, String detail);
}
