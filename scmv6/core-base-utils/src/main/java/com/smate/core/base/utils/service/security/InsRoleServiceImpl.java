package com.smate.core.base.utils.service.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.InsRoleDao;
import com.smate.core.base.utils.dao.security.role.SieInsRoleDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.security.InsRole;

/**
 * 单位角色管理实现类
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Transactional(rollbackFor = Exception.class)
public class InsRoleServiceImpl implements InsRoleService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InsRoleDao insRoleDao;
  @Autowired
  private SieInsRoleDao sieInsRoleDao;

  @Override
  public void syncSaveInsRole(List<InsRole> insRoles) throws SysServiceException {

    if (insRoles != null) {
      for (InsRole insRole : insRoles) {
        if (insRoleDao.get(insRole.getId()) == null) {
          try {
            insRoleDao.save(insRole);
          } catch (Exception e) {
            logger.error("bpo审核操作同步单位角色到Ro库失败！", e);
            throw new SysServiceException();
          }

        }

      }
    }

  }

  @Override
  public void removeInsRole(Long insId, Long psnId) throws SysServiceException {

    try {
      insRoleDao.removeInsRole(insId, psnId);
    } catch (Exception e) {
      logger.error("清空单位科研人员角色.", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public void removeInsRole(Long insId, Long psnId, Long rolId) throws SysServiceException {

    try {
      insRoleDao.removeInsRole(insId, psnId, rolId);
    } catch (Exception e) {
      logger.error("清空单位科研人员角色.", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 是否有管理员角色.
   * 
   * @param insId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public boolean ownManageRole(Long insId, Long psnId) throws SysServiceException {

    try {
      return insRoleDao.ownManageRole(insId, psnId);
    } catch (Exception e) {
      logger.error("是否有管理员角色.", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public List<Long> ownManageRole(Long insId, List<Long> psnIds) throws SysServiceException {
    try {
      return insRoleDao.ownManageRole(insId, psnIds);
    } catch (Exception e) {
      logger.error("有管理员角色的用户.", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public void syncOldInsRole(Long insId, Long psnId, List<Long> roleIds) throws SysServiceException {
    try {
      for (Long roleId : roleIds) {
        InsRole insRole = new InsRole(psnId, insId, roleId);
        this.insRoleDao.save(insRole);
      }
    } catch (Exception e) {
      logger.error("同步V2.6用户角色数据.", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public boolean hasMultiRole(Long insId, Long psnId) throws SysServiceException {

    try {
      return insRoleDao.hasMultiRole(insId, psnId);
    } catch (Exception e) {
      logger.error("判断用户是否在单位有多个角色.", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public boolean hasSieMultiRole(Long insId, Long psnId) throws SysServiceException {

    try {
      return sieInsRoleDao.hasMultiRole(insId, psnId);
    } catch (Exception e) {
      logger.error("判断用户是否在单位有多个角色.", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 保存用户角色记录.
   * 
   * @param role
   */
  @Override
  public void saveUserRole(InsRole role) {
    insRoleDao.save(role);
  }

  /**
   * 获取用户角色列表.
   * 
   * @param userId
   * @return
   */
  @Override
  public List<InsRole> getRoleListByUserId(Long userId) {
    return insRoleDao.getRoleListByUserId(userId);
  }

  /**
   * 用户是否用户某机构的部门角色(包含部门管理员或者部门秘书).
   * 
   * @param userId
   * @param insId
   * @return
   */
  @Override
  public Boolean hasUnitRole(Long userId, Long insId) {
    return insRoleDao.hasUnitRole(userId, insId);
  }

  @Override
  public List<InsRole> getRolesByUserIdAndInsId(Long userId, Long insId) {
    return this.insRoleDao.getInsRoleWithUserId(userId, insId);
  }

  public List<InsRole> getRolesByUserIdAndInsId(List<Long> userIds, Long insId) {
    return this.insRoleDao.getInsAllRole(insId, userIds);
  }
}
