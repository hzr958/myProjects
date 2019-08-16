package com.smate.center.batch.service.psn;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.core.base.utils.model.security.UserRole;

/**
 * Rol人员管理模块接口服务类.
 * 
 * @author lichangwen
 * 
 */
public interface InsPersonService {

  List<UserRole> getSysAdministrator() throws ServiceException;

  /**
   * 获取单位人员姓名以及所在机构组合.
   * 
   * @param insId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String getPsnUnitNameCompose(Long insId, Long psnId) throws ServiceException;

  /**
   * 获取本单位指定人员.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  RolPsnIns findRolPsnIns(Long psnId) throws ServiceException;

  /**
   * 获取指定单位指定人员.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  RolPsnIns findRolPsnIns(Long insId, Long psnId) throws ServiceException;

  /**
   * 获取人员所在部门名称.
   * 
   * @param insId TODO
   * @param psnIds
   * 
   * @return String[0:unitname,1:parentUnitName]
   * @throws ServiceException
   */
  public Map<Long, String[]> getPsnUnitName(Long insId, Collection<Long> psnIds) throws ServiceException;
}
