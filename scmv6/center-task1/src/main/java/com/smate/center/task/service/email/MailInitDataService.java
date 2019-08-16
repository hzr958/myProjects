package com.smate.center.task.service.email;

import java.util.Map;

public interface MailInitDataService {

  /**
   * 保存初始邮件数据
   * 
   * @param dataMap
   * @throws Exception
   */
  void saveMailInitData(Map<String, Object> dataMap) throws Exception;

}
