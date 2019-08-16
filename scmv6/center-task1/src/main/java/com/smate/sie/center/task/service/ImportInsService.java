package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.model.ImportInsDataInfo;
import com.smate.sie.center.task.model.SieInsInfo;

/**
 * 批量创建单位接口
 * 
 * @author hd
 *
 */
public interface ImportInsService {
  /**
   * 查询需要同步信息的单位
   * 
   * @param batchSize
   * @return
   * @throws ServiceException
   */
  public List<ImportInsDataInfo> findSyncInsList(int batchSize) throws ServiceException;

  /**
   * 创建单位
   * 
   * @param tmpInsInfo
   * @return
   * @throws ServiceException
   */
  public Long addIns(SieInsInfo tmpInsInfo) throws ServiceException;

  /**
   * 批量创建单位
   * 
   * @param tmpInsInfo
   * @throws ServiceException
   */
  public void doInsCreate(ImportInsDataInfo tmpInsInfo) throws ServiceException;

  /**
   * 更新状态
   * 
   * @param tmpInsInfo
   * @throws ServiceException
   */
  public void updateTmpInsInfo(ImportInsDataInfo tmpInsInfo) throws ServiceException;

  /**
   * 增加域名配置.
   * 
   * @param info
   * @throws ServiceException
   */
  public void addInsPortal(SieInsInfo info) throws ServiceException;

  /**
   * 增加人员与单位的关系
   * 
   * @param info
   * @throws ServiceException
   */
  public void addPsnIns(SieInsInfo info, Boolean isNewPerson) throws ServiceException;

  /**
   * 增加人员角色表
   * 
   * @param psnId
   * @param roleId
   * @param insId
   * @throws ServiceException
   */
  public void addSysUserRole(Long psnId, Long roleId, Long insId) throws ServiceException;

  /**
   * 获取人员id
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  public Long findPersonId(String email) throws ServiceException;

}
