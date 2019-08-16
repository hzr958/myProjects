package com.smate.center.open.service.register;

import com.smate.center.open.model.register.PersonRegister;

/**
 * 人员注册基本信息服务
 * 
 * @author tsz
 *
 */
public interface RegisterPersonBaseInfoService {

  /**
   * 保存个人基本信息
   * 
   * @param person
   */
  public void savePersonBaseInfo(PersonRegister person) throws Exception;

  /**
   * 保存手机端注册信息
   * 
   * @param person
   */
  public void saveMobileRegisgerInfo(PersonRegister person);

}
