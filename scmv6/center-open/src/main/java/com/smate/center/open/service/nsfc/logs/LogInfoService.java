package com.smate.center.open.service.nsfc.logs;

import com.smate.center.open.model.nsfc.logs.LogInfo;



/**
 * 成果在线日志服务接口
 * 
 * @author tsz
 *
 */
public interface LogInfoService {


  /**
   * 保存日志信息
   * 
   * @param log
   */
  public void saveLogInfo(LogInfo log);

}
