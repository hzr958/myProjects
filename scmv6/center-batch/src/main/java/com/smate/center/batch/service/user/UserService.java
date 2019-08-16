package com.smate.center.batch.service.user;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * center-batch 系统用户服务接口
 * 
 * @author tsz
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

  public User findSystemUser() throws ServiceException;

  public User findUserByLoginName(String email);

  public Long findIdByLoginName(String loginName);

  /**
   * 保存用户登录日志.
   * 
   * @param userId
   * @param selfLogin
   * @throws ServiceException
   */
  void saveUserLoginLog(Long userId, Long selfLogin, String loginIP) throws ServiceException;

  /**
   * 通过psnId更新solr人员信息.
   * 
   * @param psnId
   * 
   * @return
   */
  public void updateSolrPsnInfo(Long psnId) throws ServiceException;

  /**
   * 通过psnId删除solr人员信息
   * 
   * @param psnId
   * 
   * @return
   */
  public void deleteSolrPsnInfo(Long psnId) throws ServiceException;
}
