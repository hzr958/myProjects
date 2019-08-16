package com.smate.center.task.service.thirdparty.source;

import com.smate.center.task.model.thirdparty.ThirdSourcesGetLog;

/**
 * 接口调用日志服务接口.
 * 
 * @author tsz
 *
 */
public interface ThirdSourcesGetLogService {

  /**
   * 保存日志.
   * 
   * @throws Exception
   */
  public void saveLog(ThirdSourcesGetLog thirdSourcesGetLog) throws Exception;
}
