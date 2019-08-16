package com.smate.center.open.service.data;

import com.smate.center.open.model.OpenErrorLog;

/**
 * 取数据日志 服务
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface OpenErrorLogService {
  public void saveOpenErrorLog(OpenErrorLog openErrorLog);
}
