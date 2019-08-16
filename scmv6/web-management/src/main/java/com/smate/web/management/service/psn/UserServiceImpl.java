package com.smate.web.management.service.psn;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private UserDao userDao;

  @Override
  public String getLoginNameById(Long psnId) {
    return userDao.getLoginNameById(psnId);
  }

  @Override
  public User getpsnIdByEmail(String mergeCount) {

    return userDao.getUserByUsername(mergeCount);
  }

}
