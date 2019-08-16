package com.smate.sie.center.task.service;

import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.SieCityIp;

public interface SieCityIpService {
  /**
   * 根据常量表解析ip
   * 
   * @param ipStr
   * @return
   * @throws SysServiceException
   */

  public SieCityIp parseIP(String ipStr) throws SysServiceException;

  /**
   * 先使用ParseIpUtils解析IP，如果失败，再查询常量表
   * 
   * @param ipStr
   * @return
   * @throws SysServiceException
   */

  public Map<String, String> parseIP2(String ipStr) throws SysServiceException;
}
