package com.smate.center.open.service.data.log;

/**
 * open系统 接口 请求处理统计日志
 * 
 * @author tsz
 *
 */
public interface OpenDataHandleLogService {

  public void saveLog(String token, String serviceType, String disc);
}
