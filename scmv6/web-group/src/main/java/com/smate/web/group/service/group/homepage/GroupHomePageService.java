package com.smate.web.group.service.group.homepage;

import com.smate.web.group.model.group.homepage.GroupHomePageConfig;

public interface GroupHomePageService {
  /**
   * 获取主页信息配置信息.
   * 
   * @param tmpId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  GroupHomePageConfig getHomePageConfig(Long tmpId, Long groupId) throws Exception;

}
