package com.smate.center.task.single.service.person;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * center-batch 系统用户 服务实现
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
  @Autowired
  private UserCacheService userCacheService;
  @Autowired
  private UserDao userDao;

  @Override
  public User findUserById(Long psnId) throws ServiceException {
    Assert.notNull(psnId, "psnId不允许为空");
    User user = userCacheService.getCacheUser(psnId);
    if (user == null) {
      user = userDao.get(psnId);
      if (user != null) {
        userCacheService.put(psnId, user);
      }
    }
    return user;
  }
}
