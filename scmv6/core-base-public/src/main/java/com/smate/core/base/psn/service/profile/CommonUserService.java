package com.smate.core.base.psn.service.profile;

import com.smate.core.base.utils.model.cas.security.User;

/**
 * 人员账号信息服务接口
 * 
 * @author wsn
 *
 */
public interface CommonUserService {
  /**
   * 查询用户.
   * 
   * @param psnId
   * @return
   */
  public User findUserById(Long psnId);
}
