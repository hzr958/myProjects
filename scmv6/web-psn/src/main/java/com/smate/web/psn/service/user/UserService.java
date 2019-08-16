package com.smate.web.psn.service.user;

import java.util.List;

import com.smate.core.base.utils.model.cas.security.SysUserLoginLog;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.exception.UserServiceException;
import com.smate.web.psn.form.AccountEmailForm;
import com.smate.web.psn.form.PsnSettingForm;

/**
 * 系统用户服务接口
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface UserService {

  /**
   * 判断用户名是否存在
   * 
   * @param loginName
   * @return
   */
  public Boolean isLoginNameExist(String loginName);

  public User findUserById(Long psnId) throws Exception;

  public List<User> findUserByLoginNameOrEmail(String email);

  public List<User> findUsersNodeId(String psnIds) throws Exception;

  public User findSystemUser() throws UserServiceException;

  public User findUserByLoginName(String email);

  /**
   * 通过登录名查找ID.
   * 
   * @param loginName
   * @return Long
   */
  Long findIdByLoginName(String loginName);

  /**
   * 重置密码
   * 
   * @param newpwd
   * @param confirmpwd
   * @param psnId
   * @return
   */
  Boolean resetPassword(String newpwd, Long psnId) throws Exception;

  /**
   * 修改用户登录密码.
   * 
   * @param oldPassword
   * @param newPassword
   * @return 1成功 or 0原密码错误
   */
  Integer changePassword(String oldPassword, String newPassword, Long psnId) throws ServiceException;

  /**
   * 设置更新首要邮件，更新首要邮件将会将登陆名与首要邮件一致lqh.
   * 
   * @param email
   * @param psnId
   * @return 1成功设置邮件为首要邮件/登录帐号；2成果设置邮件为首要邮件，但是登录名已被其他用户占用;0更新失败
   * @throws ServiceException
   */
  public Integer updateFirstEmail(String email, Long psnId) throws Exception;

  /**
   * 获取用户的登录名
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public String findUserName(Long psnId) throws Exception;

  /**
   * 根据psnId查找最近的登陆IP
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public String findLoginIpByPsnId(Long psnId) throws ServiceException;


  /**
   * 校验邮箱是否被其他人使用（排除自己）更新psn_email biao
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  public Integer editEmailLoginname(AccountEmailForm form) throws ServiceException;

  /**
   * 验证用户名密码是否正确
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  public Boolean validdateUserPassword(PsnSettingForm form) throws ServiceException;

  public int  updateUser(User user)throws ServiceException;

  public int updateUser(PsnSettingForm form)throws ServiceException;

  /**
   * 查找这个人的最后一登录日志
   * @param psnId
   * @return
   */
  public SysUserLoginLog findLastLog(Long psnId);

  public User findUserByMobile(String mobile) ;
}
