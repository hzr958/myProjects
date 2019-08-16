package com.smate.center.open.service.user;

import java.util.Date;
import java.util.List;

import com.smate.center.open.exception.OpenException;
import com.smate.core.base.utils.model.cas.security.SysRolUser;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * open系统 用户账号密码验证服务
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface UserService {
  /**
   * 通过登录名,密码查找User类.
   * 
   * @param userName，password
   * @return User
   */
  public User getUser(String userName, String password) throws OpenException;

  /**
   * 查询PersonID
   * 
   * @param sysRolUser
   * @return
   */
  public Long getRolUserByGuid(SysRolUser sysRolUser) throws Exception;

  List<Long> getConnectedPsnByGuid(String guid) throws Exception;

  @Deprecated
  SysRolUser getSysRolUser(String guid, Long psnId) throws Exception;

  /**
   * 保存从外部同步scm的人员数据如：nsfc.
   * 
   * @param sysRolUser
   */
  void saveSysRolUser(SysRolUser sysRolUser) throws Exception;

  /**
   * 通过登录名查找User类.
   * 
   * @param userName，password
   * @return User
   */
  public User getUserByUsername(String lowerCase);

  /**
   * 通过psnId更新solr人员信息.
   * 
   * @param psnId
   * 
   * @return
   */
  public void updateSolrPsnInfo(Long psnId) throws Exception;

  /**
   * 通过psnId删除solr人员信息
   * 
   * @param psnId
   * 
   * @return
   */
  public void deleteSolrPsnInfo(Long psnId) throws Exception;

  /**
   * 获取AID
   * 
   * @param openId
   * @param autoLoginType
   * @param overTime
   * @param psnId
   * @param token
   * @return
   */
  public String getAID(Long openId, String autoLoginType, Date overTime, Long psnId, String token);

  /**
   * 设置更新首要邮件，更新首要邮件将会将登陆名与首要邮件一致lqh.
   * 
   * @param email
   * @param psnId
   * @return 1成功设置邮件为首要邮件/登录帐号；2成功设置邮件为首要邮件，但是登录名已被其他用户占用;0更新失败
   * @throws OpenException
   */
  public Integer updateFirstEmail(String email, Long psnId) throws OpenException;

  public User findUserByMobile(String mobile);
}
