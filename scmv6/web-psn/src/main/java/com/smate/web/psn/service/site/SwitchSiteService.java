package com.smate.web.psn.service.site;

import com.smate.web.psn.action.site.UserRolDataForm;

/**
 * 切换站点服务接口
 * 
 * @author Administrator
 *
 */
public interface SwitchSiteService {

  /**
   * 查找用户的机构站点
   * 
   * @throws Exception
   */
  void findUserRolSite(UserRolDataForm form) throws Exception;
}
