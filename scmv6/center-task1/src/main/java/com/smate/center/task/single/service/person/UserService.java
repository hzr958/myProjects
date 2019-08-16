package com.smate.center.task.single.service.person;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.model.cas.security.User;



/**
 * 系统用户服务接口
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface UserService {
  /**
   * 查询用户.
   * 
   * @param psnId
   * @return
   */
  public User findUserById(Long psnId) throws ServiceException;
}
