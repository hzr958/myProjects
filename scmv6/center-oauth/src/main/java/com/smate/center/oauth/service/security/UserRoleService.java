package com.smate.center.oauth.service.security;

import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.model.profile.PsnSieRoleForm;

/**
 * 用户角色服务
 * 
 * @author wsn
 * @date 2018年6月7日
 */
public interface UserRoleService {

  /**
   * 检查用户是否在某个机构有多个角色
   * 
   * @param form
   * @return
   * @throws OauthException
   */
  boolean HasMultiRoleInSie(PsnSieRoleForm form) throws OauthException;
}
