package com.smate.web.psn.service.user;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.SysUserLoginDao;
import com.smate.core.base.utils.dao.security.SysUserLoginLogDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.SysUserLogin;
import com.smate.core.base.utils.model.cas.security.SysUserLoginLog;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.security.SysUserLoginService;
import com.smate.web.psn.dao.profile.PersonEmailDao;
import com.smate.web.psn.dao.psn.PdwhAddrPsnUpdateRecordDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.exception.UserServiceException;
import com.smate.web.psn.form.AccountEmailForm;
import com.smate.web.psn.form.PsnSettingForm;
import com.smate.web.psn.model.pdwh.pub.PdwhAddrPsnUpdateRecord;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;

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
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private SysUserLoginService sysUserLoginService;
  @Autowired
  private SysUserLoginDao sysUserLoginDao;
  @Autowired
  private SysUserLoginLogDao sysUserLoginLogDao;
  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PdwhAddrPsnUpdateRecordDao pdwhAddrPsnUpdateRecordDao;

  @Override
  public Boolean isLoginNameExist(String loginName) {
    Assert.hasText(loginName, "loginName不允许为空");
    Long id = this.userDao.findIdByLoginName(loginName);
    if (id != null) {
      return true;
    }
    return false;
  }

  /**
   * 设置更新首要邮件，更新首要邮件将会将登陆名与首要邮件一致lqh.
   * 
   * @param email
   * @param psnId
   * @return 1成功设置邮件为首要邮件/登录帐号；2成果设置邮件为首要邮件，但是登录名已被其他用户占用;0更新失败
   * @throws ServiceException
   */
  public Integer updateFirstEmail(String email, Long psnId) throws Exception {
    Assert.hasText(email, "email不允许为空");
    Assert.notNull(psnId, "psnId不允许为空");
    Boolean result = this.userDao.isLoginNameUsed(email, psnId);
    User user = this.findUserById(psnId);

    // 已被使用
    if (result && user != null) {
      user.setEmail(email);
      this.saveUser(user);
      return 2;
      // 未被使用
    } else if (user != null) {
      user.setLoginName(email);
      user.setEmail(email);
      this.saveUser(user);
      return 1;
    }
    return 0;
  }

  @Override
  public User findUserById(Long psnId) throws Exception {
    Assert.notNull(psnId, "psnId不允许为空");
    User user = userDao.get(psnId);

    return user;
  }

  @Override
  public List<User> findUserByLoginNameOrEmail(String email) {
    Assert.hasText(email, "email不允许为空");
    return this.userDao.findUserByLoginNameOrEmail(email);
  }

  /**
   * 保存用户信息.
   * 
   * @param user
   * @throws ServiceException
   */
  private void saveUser(User user) throws UserServiceException {
    try {
      this.userDao.save(user);

    } catch (Exception e) {
      logger.error("保存用户信出错,psnId={}", user.getId());
      throw new UserServiceException(e);
    }
  }

  @Override
  public List<User> findUsersNodeId(String psnIds) throws Exception {
    Assert.notNull(psnIds, "psnIds不允许为空");
    Assert.hasLength(psnIds, "收件人Id列表不允许为空！");
    try {
      return this.userDao.getUserNodeIds(psnIds);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public User findSystemUser() throws UserServiceException {

    return this.findUserByLoginName("system");

  }

  @Override
  public User findUserByLoginName(String email) {
    Assert.hasText(email, "email不允许为空");
    return userDao.findByLoginName(email);
  }

  @Override
  public Long findIdByLoginName(String loginName) {
    Assert.hasText(loginName, "loginName不允许为空");
    return this.userDao.findIdByLoginName(loginName);
  }

  /** 重置密码 */
  @Override
  public Boolean resetPassword(String newpwd, Long psnId) throws Exception {
    Assert.hasText(newpwd, "新密码不允许为空");
    Assert.notNull(psnId, "psnId不允许为空");
    User user = userDao.get(psnId);
    if (user != null) {
      newpwd = passwordEncoder.encodePassword(newpwd, null);
      user.setPassword(newpwd);
      userDao.save(user);
      // // 修改密码之后在sys_user_login表中记录下时间
      SysUserLogin sysUserLogin = sysUserLoginService.getSysUserLogin(psnId);
      if (sysUserLogin != null) {
        sysUserLogin.setLastPwdChanged(new Date());
      } else {// 新逻辑_SCM-13499
        sysUserLogin = new SysUserLogin();
        sysUserLogin.setId(psnId);
        sysUserLogin.setLastPwdChanged(new Date());
        sysUserLogin.setLastLoginTime(new Date());
      }
      sysUserLoginDao.save(sysUserLogin);
      return true;
    }
    return false;
  }

  @Override
  public Integer changePassword(String oldPassword, String newPassword, Long psnId) throws ServiceException {

    Assert.hasText(oldPassword, "原密码不允许为空");
    Assert.hasText(newPassword, "新密码不允许为空");
    Assert.notNull(psnId, "psnId不允许为空");
    // 修改密码不查缓存，有可能是直接修改了数据库
    User user = userDao.get(psnId);
    oldPassword = passwordEncoder.encodePassword(oldPassword, null);
    newPassword = passwordEncoder.encodePassword(newPassword, null);

    if (user.getPassword().equals(oldPassword)) {

      user.setPassword(newPassword);
      this.saveUser(user);
      // 修改密码之后在sys_user_login表中记录下时间
      SysUserLogin sysUserLogin = sysUserLoginDao.get(psnId);
      if (sysUserLogin != null) {
        sysUserLogin.setLastPwdChanged(new Date());
        sysUserLoginDao.save(sysUserLogin);
      }
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public String findUserName(Long psnId) throws Exception {
    User user = findUserById(psnId);
    return user.getLoginName();
  }

  @Override
  public String findLoginIpByPsnId(Long psnId) throws ServiceException {
    try {
      return this.sysUserLoginLogDao.findLoginIpByPsnId(psnId);
    } catch (Exception e) {
      logger.error("查找用户最近登陆的IP出错,psnId={}", psnId);
      throw new ServiceException(e);
    }
  }

  /**
   * 1 = 更新成功， 2=邮箱被占用 ,3 =格式错误
   */
  @Override
  public Integer editEmailLoginname(AccountEmailForm form) throws ServiceException {
    Integer result = 1;
    if (!checkEmail(form.getNewEmail())) {
      return 3;
    } ;
    boolean used = userDao.isLoginNameUsed(form.getNewEmail(), form.getCurrentPsnId());
    if (used) {
      result = 2;
    } else {
      User user = userDao.get(form.getCurrentPsnId());
      user.setLoginName(form.getNewEmail());
      user.setEmail(form.getNewEmail());
      userDao.save(user);
      // 更新psn_email
      personEmailDao.clearFirstLoginEmail(form.getCurrentPsnId());
      PersonEmailRegister psnEmail = personEmailDao.getPsnEmail(form.getCurrentPsnId(), form.getNewEmail());
      if (psnEmail == null) {
        psnEmail = new PersonEmailRegister();
        psnEmail.setEmail(form.getNewEmail());
        psnEmail.setIsVerify(0L);
        psnEmail.setPerson(personDao.get(form.getCurrentPsnId()));
        psnEmail.setLeftPart(form.getNewEmail().split("@")[0]);
        psnEmail.setRightPart(form.getNewEmail().split("@")[1]);
      }
      psnEmail.setFirstMail(1L);
      psnEmail.setLoginMail(1L);
      personEmailDao.save(psnEmail);
      // 更新 psn表的邮箱
      personDao.updateEmailByPsnId(form.getCurrentPsnId(), form.getNewEmail());
      pdwhAddrPsnUpdateRecordDao.deleteRecordByPsnId(form.getCurrentPsnId());
      PdwhAddrPsnUpdateRecord record = new PdwhAddrPsnUpdateRecord(form.getCurrentPsnId(), 2, 0);
      pdwhAddrPsnUpdateRecordDao.save(record);
    }

    return result;
  }

  @Override
  public Boolean validdateUserPassword(PsnSettingForm form) throws ServiceException {
    String password = passwordEncoder.encodePassword(form.getOldpassword(), null);
    User user = userDao.get(form.getPsnId());
    if (user != null && user.getPassword().equals(password)) {
      return true;
    }
    return false;
  }

  @Override
  public int updateUser(User user) throws ServiceException {
    userDao.saveOrUpdate(user);
    Person person = personDao.get(user.getId());
    person.setTel(user.getMobileNumber());
    personDao.saveOrUpdate(person);
    return 0;
  }

  @Override
  public int updateUser(PsnSettingForm form) throws ServiceException {
    User user = userDao.get(form.getPsnId());
    user.setMobileNumber("");
    userDao.saveOrUpdate(user);
    Person person = personDao.get(form.getPsnId());
    person.setTel("");
    personDao.saveOrUpdate(person);
    return 0;
  }

  @Override
  public SysUserLoginLog findLastLog(Long psnId) {
    SysUserLoginLog log = sysUserLoginLogDao.findLastLog(psnId);
    return log;
  }

  public static boolean checkEmail(String email) {
    String regex = "[a-z0-9A-Z_]+[a-z0-9_\\-.]*@([a-z0-9A-Z_]+\\.)+[a-zA-Z_]{2,10}";
    return Pattern.matches(regex, email);
  }

  @Override
  public User findUserByMobile(String mobile) {

    return userDao.findUserByMobile(mobile);
  }

}
