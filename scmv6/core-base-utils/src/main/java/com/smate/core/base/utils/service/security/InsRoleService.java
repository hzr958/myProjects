package com.smate.core.base.utils.service.security;

import java.util.List;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.security.InsRole;

/**
 * 单位角色管理接口
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public interface InsRoleService {

  void syncSaveInsRole(List<InsRole> insRoles) throws SysServiceException;

  /**
   * 清空单位科研人员角色.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  void removeInsRole(Long insId, Long psnId) throws SysServiceException;

  /**
   * 清空单位科研人员角色.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  void removeInsRole(Long insId, Long psnId, Long rolId) throws SysServiceException;

  /**
   * 是否有管理员角色.
   * 
   * @param insId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean ownManageRole(Long insId, Long psnId) throws SysServiceException;

  /**
   * 有管理员角色的用户.
   * 
   * @param insId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Long> ownManageRole(Long insId, List<Long> psnId) throws SysServiceException;

  /**
   * 同步V2.6用户角色数据.
   * 
   * @param insId
   * @param psnId
   * @param roleId
   * @throws ServiceException
   */
  void syncOldInsRole(Long insId, Long psnId, List<Long> roleId) throws SysServiceException;

  /**
   * 判断用户是否在单位有多个角色.
   * 
   * @param insId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public boolean hasMultiRole(Long insId, Long psnId) throws SysServiceException;

  /**
   * 保存用户角色记录.
   * 
   * @param role
   */
  void saveUserRole(InsRole role);

  /**
   * 获取用户角色列表.
   * 
   * @param userId
   * @return
   */
  List<InsRole> getRoleListByUserId(Long userId);

  /**
   * 用户是否用户某机构的部门角色(包含部门管理员或者部门秘书).
   * 
   * @param userId
   * @param insId
   * @return true-有；false-没有.
   */
  Boolean hasUnitRole(Long userId, Long insId);

  /*
   * 获取用户角色列表.
   * 
   * @param userId
   * 
   * @return
   */
  List<InsRole> getRolesByUserIdAndInsId(Long userId, Long insId);

  public List<InsRole> getRolesByUserIdAndInsId(List<Long> userIds, Long insId);

  /**
   * 判断sie是否有多个角色
   * 
   * @param insId
   * @param psnId
   * @return
   * @throws SysServiceException
   */
  boolean hasSieMultiRole(Long insId, Long psnId) throws SysServiceException;
}
