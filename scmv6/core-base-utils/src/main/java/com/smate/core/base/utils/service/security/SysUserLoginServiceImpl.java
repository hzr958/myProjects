package com.smate.core.base.utils.service.security;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.SysUserLoginDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.SysUserLogin;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 登录信息实现类
 * 
 * @author lhd
 *
 */
@Service("sysUserLoginService")
@Transactional(rollbackFor = Exception.class)
public class SysUserLoginServiceImpl implements SysUserLoginService {

  @Autowired
  private SysUserLoginDao sysUserLoginDao;
  @Autowired
  private UserDao userDao;

  /**
   * 获取用户登录信息
   */
  @Override
  public SysUserLogin getSysUserLogin(Long psnId) throws Exception {
    return sysUserLoginDao.get(psnId);
  }

  /**
   * 更新用户最后登录时间
   */
  @Override
  public Boolean updateLoginTime(Long psnId) throws Exception {
    SysUserLogin sysUserLogin = sysUserLoginDao.get(psnId);
    if (sysUserLogin != null) {
      sysUserLogin.setLastLoginTime(new Date());
    } else {// 新逻辑_SCM-13499
      sysUserLogin = new SysUserLogin();
      sysUserLogin.setId(psnId);
      sysUserLogin.setLastLoginTime(new Date());
    }
    sysUserLoginDao.save(sysUserLogin);
    return true;
  }

  @Override
  public Date getLastLoginTimeById(Long psnId) {
    return sysUserLoginDao.getLastLoginTimeById(psnId);
  }

  @Override
  public String getLoginNameByPsnId(Long psnId) throws Exception {
    return userDao.getLoginNameById(psnId);
  }

  @Override
  public User getUserByPsnId(Long psnId) throws Exception {
    return userDao.get(psnId);
  }


  @Override
  public List<Long> getPsnIdsDayBeforeByLogin() {
    return sysUserLoginDao.getPsnIdsDayBeforeByLogin();
  }

  @Override
  public Boolean isLogin(Long psnId) throws Exception {
    if (sysUserLoginDao.getIsLogin(psnId) != null) {
      return true;
    }
    return false;
  }
}
