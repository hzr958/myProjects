package com.smate.core.base.utils.service.security;

import java.util.Date;
import java.util.List;

import com.smate.core.base.utils.model.cas.security.SysUserLogin;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 登录信息接口
 * 
 * @author lhd
 *
 */
public interface SysUserLoginService {

  /**
   * 获取用户登录信息
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  SysUserLogin getSysUserLogin(Long psnId) throws Exception;

  /**
   * 更新用户最后登录时间
   * 
   * @author lhd
   * @param psnId
   * @return
   * @throws Exception
   */
  Boolean updateLoginTime(Long psnId) throws Exception;

  Date getLastLoginTimeById(Long personId);

  /**
   * 获取登录名
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  String getLoginNameByPsnId(Long psnId) throws Exception;

  List<Long> getPsnIdsDayBeforeByLogin();

  /**
   * 近半年是否登录过
   * 
   * @param personId
   * @return
   * @throws Exception
   */

  Boolean isLogin(Long personId) throws Exception;

  /**
   * 获取帐号.
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  User getUserByPsnId(Long psnId) throws Exception;

}
