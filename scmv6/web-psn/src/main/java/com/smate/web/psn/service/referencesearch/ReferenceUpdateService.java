package com.smate.web.psn.service.referencesearch;

import com.smate.web.psn.exception.ServiceException;

/**
 * 更新引用服务---------其他成果相关操作请去pub项目
 * 
 * @author WSN
 *
 */
public interface ReferenceUpdateService {
  /**
   * 获取第三方数据库登录url.
   * 
   * @return
   * @throws ServiceException
   */
  String getDBurl() throws ServiceException;
}
