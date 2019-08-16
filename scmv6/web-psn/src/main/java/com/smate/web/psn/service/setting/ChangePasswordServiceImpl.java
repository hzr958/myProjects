package com.smate.web.psn.service.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 个人设置，修改密码服务
 * 
 * @author aijiangbin
 */

@Service("changePasswordService")
@Transactional(rollbackFor = Exception.class)
public class ChangePasswordServiceImpl implements ChangePasswordService {

  @Autowired
  UserDao userDao;

  @Override
  public User get(Long psnId) {
    return userDao.get(psnId);
  }

}
