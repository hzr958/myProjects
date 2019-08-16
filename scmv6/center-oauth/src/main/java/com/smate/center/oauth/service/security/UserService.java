package com.smate.center.oauth.service.security;

import java.util.List;

import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.exception.ServiceException;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 
 * oauth系统 需要用到的用户 服务
 * 
 * @author tsz
 * 
 */
public interface UserService {

  /**
   * 查询用户.
   * 
   * @param psnId
   * @return
   */
  public User findUserById(Long psnId);

  /**
   * 通过 登录名 密码 查询用户
   * 
   * @param userName
   * @param password
   * @return
   * @throws OpenException
   */

  public User getUser(String userName) throws OauthException;

  /**
   * 通过登录名查找ID.
   * 
   * @param loginName
   * @return Long
   */
  Long findIdByLoginName(String loginName);

  /**
   * 重置TokenBit
   * 
   * @param psnId
   * @param tokenBit
   */
  public void resetTokenBit(Long psnId, Short tokenBit);

  /**
   * 通过邮件或帐号查找用户
   * 
   * @param email
   * @return
   */
  public List<User> findUserByLoginNameOrEmail(String email);

  /**
   * 保存用户登录日志.
   * 
   * @param userId
   * @param selfLogin
   * 
   */
  void saveUserLoginLog(Long userId, Long selfLogin, String loginIP) throws Exception;

  /**
   * 保存用户的登录信息
   * 
   * @param psnId
   * @param sysRootPath
   * @throws ServiceException
   */
  void saveSysUserLoginInfo(Long userId, String loginIp, String sysRootPath, Integer loginType) throws Exception;

  /**
   * 判断用户名是否存在
   * 
   * @param loginName
   * @return
   */
  public Boolean isLoginNameExist(String loginName);

  User getUser(String userName, String password) throws OauthException;

  /**
   * 根据url和sys获取用户的insId
   * 
   * @param url
   * @return
   * @throws OauthException
   */
  Long findUserInsIdByDomainAndSys(String url, String sys) throws OauthException;

  /**
   * 更新用户密码
   * 
   * @author ChuanjieHou
   * @date 2017年10月23日
   * @param user
   */
  public void updateUserPwd(User user) throws ServiceException;

  public User findUserByMobile(String mobile) ;

}
