package com.smate.core.base.email.service;

import java.util.Map;

/**
 * 新系统发邮箱service
 * 
 * @author zk
 *
 */
public interface MailInitDataService {

  /**
   * 保存初始邮件数据
   * 
   * @param dataMap
   * @throws Exception
   */
  void saveMailInitData(Map<String, Object> dataMap) throws Exception;

}
