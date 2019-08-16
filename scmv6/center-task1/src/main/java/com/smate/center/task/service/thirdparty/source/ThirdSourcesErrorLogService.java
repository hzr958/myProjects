package com.smate.center.task.service.thirdparty.source;

import com.smate.center.task.model.thirdparty.ThirdSourcesErrorLog;

/**
 * 数据错误记录服务接口
 * 
 * @author tsz
 *
 */
public interface ThirdSourcesErrorLogService {


  public void saveLog(ThirdSourcesErrorLog log);
}
