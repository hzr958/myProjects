package com.smate.core.base.psn.service.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 个人账号信息服务
 * 
 * @author wsn
 *
 */
@Service("commonUserService")
@Transactional(rollbackFor = Exception.class)
public class CommonUserServiceImpl implements CommonUserService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserDao userDao;

  @Override
  public User findUserById(Long psnId) {
    Assert.notNull(psnId, "psnId不允许为空");
    User user = userDao.get(psnId);
    return user;
  }

}
