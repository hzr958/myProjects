package com.smate.center.open.service.register;

import com.smate.center.open.model.register.PersonRegister;

/**
 * 人员注册 同步其他数据 任务服务
 * 
 * @author tsz
 *
 */
public interface RegisterPersonTaskService {

  /**
   * 保存任务处理数据
   * 
   * @param personRegister
   */
  public void saveTaskJob(PersonRegister personRegister);

}
