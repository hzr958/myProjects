package com.smate.center.open.service.reschproject;

import com.smate.center.open.model.nsfc.NsfcSyncProject;
import com.smate.center.open.model.nsfc.project.NsfcProject;
import com.smate.center.open.model.nsfc.project.NsfcProjectReport;
import com.smate.center.open.model.nsfc.project.NsfcReschProject;
import com.smate.center.open.model.nsfc.project.NsfcReschProjectReport;


/**
 * nsfc项目ws同步服务接口.
 * 
 * @author ajb
 * 
 */
public interface SnsNsfcProjectService {

  /**
   * 查找成果研究报告
   * 
   * @param nsfcPrjId
   * @return
   * @throws ServiceException
   */
  NsfcReschProject getSnsRolSyncReschProject(Long nsfcPrjId) throws Exception;

  /**
   * 保存成果研究项目
   * 
   * @param nsfcSyncProject
   * @return
   * @throws ServiceException
   */
  NsfcReschProject saveSyncRolReschProject(NsfcSyncProject nsfcSyncProject) throws Exception;

  /**
   * 查找某个项目的报告个数
   * 
   * @param prjId
   * @param year
   * @return
   * @throws ServiceException
   */
  public NsfcReschProjectReport findByRptByPrjId(Long nsfcPrjId, Integer year) throws Exception;

  /**
   * 更新研究报告状态
   * 
   * @param rptId
   * @param status
   * @throws ServiceException
   */
  void updateReschRptStatus(Long rptId, Integer status) throws Exception;

  /**
   * 保存成果研究报告
   * 
   * @param nsfcProjectReport
   * @return
   * @throws ServiceException
   */
  NsfcReschProjectReport saveNsfcReschProjectReport(NsfcReschProjectReport nsfcProjectReport) throws Exception;


  /**
   * 更新成果研究报告的拥有者ID
   * 
   * @param prjId
   * @param psnId
   * @throws ServiceException
   */
  void updateReschPsnId(Long prjId, Long psnId) throws Exception;

  /**
   * @param nsfcProjectId
   * @return
   * @throws ServiceException
   */
  NsfcProject getSnsRolSyncProject(Long nsfcProjectId) throws Exception;

  /**
   * 同步结题、进展报告的项目
   * 
   * @param nsfcSyncProject
   * @return
   * @throws ServiceException
   */
  NsfcProject syncRolProject(NsfcSyncProject nsfcSyncProject) throws Exception;

  /**
   * 根据基金委的报告Id查找报告
   * 
   * @param nsfcRptId
   * @return
   * @throws ServiceException
   */
  NsfcProjectReport getNsfcRptByNsfcRptId(Long nsfcRptId) throws Exception;

  /**
   * 更新结题、进展报告
   * 
   * @param nsfcProjectReport
   * @throws ServiceException
   */
  NsfcProjectReport updateNsfcProjectReport(NsfcProjectReport nsfcProjectReport) throws Exception;

  /**
   * @param nsfcProjectReport
   * @throws ServiceException
   */
  NsfcProjectReport saveNsfcProjectReport(NsfcProjectReport nsfcProjectReport) throws Exception;

  /**
   * 更新结题、进展报告的项目
   * 
   * @param nsfcSyncProject
   * @return
   * @throws ServiceException
   */
  NsfcProject updateRolProject(NsfcProject nsfcProject) throws Exception;
}
