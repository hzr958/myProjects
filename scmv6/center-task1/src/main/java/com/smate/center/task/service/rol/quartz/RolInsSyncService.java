package com.smate.center.task.service.rol.quartz;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.bpo.InsInfo;
import com.smate.center.task.model.rol.quartz.InsRegion;

/**
 * sie 单位同步服务
 * 
 * @author hd
 *
 */
public interface RolInsSyncService {
  /**
   * 添加单位信息
   * 
   * @param info
   * @return
   * @throws ServiceException
   */
  public Long addIns(InsInfo info) throws ServiceException;

  /**
   * 添加单位域名
   * 
   * @param info
   * @throws ServiceException
   */
  public void addInsPortal(InsInfo info) throws ServiceException;

  /**
   * 增加人员与单位的关系
   * 
   * @param info
   * @throws ServiceException
   */
  public void addPsnIns(InsInfo info) throws ServiceException;

  /**
   * 同步失败，清除脏数据
   * 
   * @param psnId
   * @param insId
   * @throws ServiceException
   */
  public void deleteRol(Long psnId, Long insId) throws ServiceException;

  /**
   * 向ins_regoin表中添加数据
   * 
   * @param insId
   * @param prvId
   * @param cyId
   * @param disId
   * @throws ServiceException
   */
  public InsRegion addInsReg(Long insId, Long prvId, Long cyId, Long disId) throws ServiceException;

  /**
   * 向ins_status表中添加数据
   * 
   * @param insId
   * @throws ServiceException
   */
  public void addInsStatus(Long insId) throws ServiceException;

  /**
   * 增加人员与单位的角色表
   * 
   * @param psnId
   * @param insId
   * @throws ServiceException
   */
  public void addSieRole(Long psnId, Long insId) throws ServiceException;

  /**
   * 增加人员与单位的角色表
   * 
   * @param psnId
   * @param roleId
   * @param insId
   * @throws ServiceException
   */
  void addSysUserRole(Long psnId, Long roleId, Long insId) throws ServiceException;

}
