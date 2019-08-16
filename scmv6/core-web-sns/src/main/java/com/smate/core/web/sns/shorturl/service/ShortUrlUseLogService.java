package com.smate.core.web.sns.shorturl.service;

import com.smate.core.base.utils.model.shorturl.OpenShortUrlUseLog;

/**
 * 短地址使用日志服务服务
 * 
 * @author tsz
 *
 */
public interface ShortUrlUseLogService {

  /**
   * 记录使用日志
   * 
   * @param log
   */
  public void addUseLog(OpenShortUrlUseLog log);
}
