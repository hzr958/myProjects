package com.smate.center.open.service.register;

import com.smate.center.open.exception.EmailExistException;
import com.smate.center.open.model.register.PersonRegister;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.dao.security.UserRoleDao;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.UserRole;
import com.smate.core.base.utils.model.security.UserRoleId;
import com.smate.core.base.utils.string.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 保存cas帐号信息 服务
 * 
 * @author tsz
 *
 */
@Service("registerPersonCasService")
@Transactional(rollbackFor = Exception.class)
public class RegisterPersonCasServiceImpl implements RegisterPersonCasService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserDao userDao;
  @Autowired
  private UserRoleDao userRolDao;

  // 保存帐号密码
  @Override
  public void saveRegisterSysUser(PersonRegister person) throws Exception {
    User sysUser = new User();
    sysUser.setId(person.getPersonId());
    if (this.isExists(person.getEmail())) {
      logger.error("注册失败,帐号已经存在");
      throw new EmailExistException("注册失败,帐号已经存在");
    }
    if(person.getMobileLogin() && StringUtils.isNotBlank(person.getMobile())){
      if(this.isExistsMobile(person.getMobile())){
        logger.error("注册失败,手机号已经存在");
        throw new EmailExistException("注册失败,手机号号已经存在");
      }
    }
    try {
      sysUser.setLoginName(person.getEmail());
      sysUser.setEmail(person.getEmail());
      sysUser.setEnabled(true);
      sysUser.setNodeId(ServiceConstants.SCHOLAR_NODE_ID_1);
      // 传进来的密码都必须是加密了的密码
      sysUser.setPassword(person.getNewpassword());
      if(person.getMobileLogin() && StringUtils.isNotBlank(person.getMobile())){
        sysUser.setMobileNumber(person.getMobile());
      }

      this.userDao.save(sysUser);
      UserRole sysUserRole01 = new UserRole(new UserRoleId(sysUser.getId(), 1L)); // 初始化登录用户的角色
      UserRole sysUserRole02 = new UserRole(new UserRoleId(sysUser.getId(), 3L)); // 初始化科研人员的角色
      userRolDao.save(sysUserRole01);
      userRolDao.save(sysUserRole02);
    } catch (Exception e) {
      logger.error("保存登陆信息及角色，默认角色为3（具有研究能力和成果的角色） 邮件唯一", e);
      throw new Exception("保存登陆信息及角色出错", e);
    }

  }

  // 登录表人员登录账号查重
  private boolean isExists(String loginName) throws Exception {
    Long id = userDao.findIdByLoginName(loginName);
    if (id != null) {
      return true;
    }
    return false;

  }

  // 登录表人员登录账号查重
  private boolean isExistsMobile(String mobile) throws Exception {
    User user = userDao.getUserByMobile(mobile);
    if (user != null) {
      return true;
    }
    return false;

  }
}
