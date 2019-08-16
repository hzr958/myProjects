package com.smate.center.open.service.register;

import com.smate.center.open.model.register.PersonRegister;

/**
 * 人员注册 成果在线 权限处理服务
 * 
 * @author tsz
 *
 */
public interface RegisterPersonNsfcService {

  /**
   * 处理数据方法
   * 
   * @param personRegister
   */
  public void handleNsfcData(PersonRegister personRegister);

}
