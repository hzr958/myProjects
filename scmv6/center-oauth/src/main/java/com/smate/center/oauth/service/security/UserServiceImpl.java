package com.smate.center.oauth.service.security;

import com.smate.center.oauth.dao.profile.OpenUserUnionDao;
import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.exception.ServiceException;
import com.smate.core.base.utils.dao.security.*;
import com.smate.core.base.utils.dao.security.role.SieInsPortalDao;
import com.smate.core.base.utils.model.cas.security.SysUserLogin;
import com.smate.core.base.utils.model.cas.security.SysUserLoginLog;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.rol.SieInsPortal;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.SystemUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * oauth权限系统用户管理服务
 * 
 * @author tsz
 *
 */
@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserDao userDao;
  @Autowired
  private SysUserLoginDao sysUserLoginDao;
  @Autowired
  private SysUserLoginLogDao sysUserLoginLogDao;
  @Autowired
  private SieInsPortalDao sieInsPortalDao;
  @Autowired
  private PersonDao personDao ;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PersonKnowSynDao personKnowSynDao ;
  // @Autowired
  // private UserCacheService userCacheService;

  @Override
  public User findUserById(Long psnId) {
    Assert.notNull(psnId, "psnId不允许为空");
    User user = userDao.get(psnId);
    return user;
  }

  @Override
  public User getUser(String userName, String password) throws OauthException {
    try {
      User user = userDao.getUser(userName, password);
      if(user != null) {
        return  user;
      }
      return getUserByOpenId(userName,  password);
    } catch (Exception e) {
      logger.error("根据用户名密码 查找数据库失败", e);
      throw new OauthException("根据用户名密码 查找数据库失败", e);
    }
  }

  /**
   * 通过openId 查询
   * @param userName
   * @param password
   * @return
   */
  public User getUserByOpenId(String userName, String password){
    if(NumberUtils.isDigits(userName)){
      OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnionByOpenId(NumberUtils.toLong(userName));
      if(openUserUnion != null){
        User user = userDao.getUserByPsnId(openUserUnion.getPsnId(), password);
        return user;
      }
    }
    return null ;
  }

  @Override
  public User getUser(String userName) throws OauthException {
    try {
      return userDao.getUserByUsername(userName);
    } catch (Exception e) {
      logger.error("根据用户名密码 查找数据库失败", e);
      throw new OauthException("根据用户名密码 查找数据库失败", e);
    }
  }

  /**
   * 通过登录名查找ID.
   */
  @Override
  public Long findIdByLoginName(String loginName) {
    Assert.hasText(loginName, "loginName不允许为空");
    return this.userDao.findIdByLoginName(loginName);
  }

  @Override
  public void resetTokenBit(Long psnId, Short tokenBit) {
    try {
      User user = findUserById(psnId);
      if (user != null) {
        user.setTokenChanged(tokenBit);
        this.saveUser(user);
        // userCacheService.put(psnId, user);
      }
    } catch (Exception e) {
      logger.error("忘记密码重置用户标志位失败！", e);
    }

  }

  /**
   * 保存用户信息.
   * 
   * @param user
   * @throws ServiceException
   */
  private void saveUser(User user) throws OauthException {
    try {
      this.userDao.save(user);
    } catch (Exception e) {
      logger.error("保存用户信出错,psnId={}", user.getId());
      throw new OauthException(e);
    }
  }

  @Override
  public List<User> findUserByLoginNameOrEmail(String email) {
    Assert.hasText(email, "email不允许为空");
    return this.userDao.findUserByLoginNameOrEmail(email);
  }

  /**
   * 保存用户登录日志.
   * 
   * @param userId
   * @param selfLogin
   * @throws Exception
   * 
   */
  @Override
  public void saveUserLoginLog(Long userId, Long selfLogin, String loginIP) throws Exception {
    Assert.notNull(userId, "personId不允许为空");
    try {
      // 查找，不存在，更新
      SysUserLogin login = sysUserLoginDao.get(userId);
      if (login == null) {
        login = new SysUserLogin(userId);
      } else {
        login.setLastLoginTime(new Date());
      }
      if (selfLogin != null) {
        login.setSelfLogin(selfLogin);
      }
      if (loginIP != null) {
        login.setLoginIP(loginIP);
      }
      sysUserLoginDao.save(login);
      sysIsLogin(login.getId());
    } catch (Exception e) {
      throw new Exception("保存登录日志 失败");

    }
  }

  @Override
  public void saveSysUserLoginInfo(Long userId, String loginIp, String sysRootPath, Integer loginType)
      throws Exception {
    Assert.notNull(userId, "userId不允许为空");
    try {
      HttpServletRequest request = Struts2Utils.getRequest();
      sysUserLoginLogDao.save(new SysUserLoginLog(userId, new Date(), loginIp, sysRootPath, loginType,
          SystemUtils.getRequestBrowserInfo(request), SystemUtils.getRequestSystemInfo(request)));
    } catch (Exception e) {
      throw new Exception();
    }
  }

  @Override
  public Boolean isLoginNameExist(String loginName) {
    Assert.hasText(loginName, "loginName不允许为空");
    Long id = this.userDao.findIdByLoginName(loginName);
    if (id != null) {
      return true;
    }
    return false;
  }

  @Override
  public Long findUserInsIdByDomainAndSys(String url, String sys) throws OauthException {
    Long insId = null;
    // 获取要跳转到的系统的domain
    if (StringUtils.isNotBlank(url)) {
      if (url.indexOf("http://") > -1) {
        url = url.replace("http://", "");
      }
      if (url.indexOf("https://") > -1) {
        url = url.replace("https://", "");
      }
      if (url.indexOf("/") > -1) {
        url = url.substring(0, url.indexOf("/"));
      }
    }
    if ("SIE".equals(sys)) {
      if (StringUtils.isNotBlank(url)) {
        SieInsPortal insPortal = sieInsPortalDao.getEntity(url);
        if (insPortal != null) {
          insId = insPortal.getInsId();
        }
      } else {
        insId = SecurityUtils.getCurrentInsId();
      }
    }
    return insId;
  }

  @Override
  public void updateUserPwd(User user) {
    try {
      this.saveUser(user);
      // userCacheService.put(user.getId(), user);
      // 修改密码之后在sys_user_login表中记录下时间
      SysUserLogin sysUserLogin = sysUserLoginDao.get(user.getId());
      if (sysUserLogin != null) {
        sysUserLogin.setLastPwdChanged(new Date());
        sysUserLoginDao.save(sysUserLogin);
      }
    } catch (Exception e) {
      logger.error("更新用户密码出错！用户id：{}", user.getId(), e);
      throw new ServiceException(e);
    }
  }

  /**
   * 同步登陆状态
   * @param psnId
   */
  public void sysIsLogin(Long psnId){
    personDao.syncUserLoginToPerson(psnId);
    personKnowSynDao.syncPersonKnowByIsLogin(psnId);
  }

  @Override
  public User findUserByMobile(String mobile) {

    return userDao.findUserByMobile(mobile);
  }

}
