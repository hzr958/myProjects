package com.smate.center.open.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.SysRolUserDao;
import com.smate.core.base.utils.model.cas.security.SysRolUser;

/**
 * open系统 通过juid确定用户服务
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("sysRolUserService")
@Transactional(rollbackFor = Exception.class)
public class SysRolUserServiceImpl implements SysRolUserService {

  @Autowired
  private SysRolUserDao sysRolUserDao;

  @Override
  public SysRolUser getSysRolUserByGuid(String guid) {
    return sysRolUserDao.getSysRolUserByGuid(guid);
  }

}
