package com.smate.center.open.service.register;

import com.smate.center.open.model.register.PersonRegister;

/**
 * 人员注册cas数据处理服务
 * 
 * @author tsz
 *
 */
public interface RegisterPersonCasService {

  /**
   * 保存帐号密码
   * 
   * @param personRegister
   */
  public void saveRegisterSysUser(PersonRegister personRegister) throws Exception;

}
