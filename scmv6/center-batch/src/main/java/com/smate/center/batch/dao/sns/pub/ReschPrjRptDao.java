package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcReschProjectReport;
import com.smate.center.batch.model.sns.pub.SyncNsfcReschProjectReportTemp;

/**
 * 
 * 
 * 功能描述：成果研究报告DAO.
 * 
 * @author oyh
 * 
 */
public interface ReschPrjRptDao {
  /**
   * 结题报告列表.
   * 
   * @return
   */
  public List<?> getProjectReports() throws DaoException;

  /**
   * ID 查询.
   * 
   * @param id
   * @return
   */
  public NsfcReschProjectReport getProjectReport(Long id) throws DaoException;

  /**
   * insert or update Project report.
   * 
   * @param obj
   */
  void saveProjectReport(NsfcReschProjectReport obj) throws DaoException;

  /**
   * remove project.
   * 
   * @param id
   */
  void removeProjectReport(Long id) throws DaoException;

  /**
   * 根据用户ID查询.
   * 
   * @param userId
   * @return
   * @throws DaoException
   */
  List<NsfcReschProjectReport> getProjectReportsByPsnId(Long psnId) throws DaoException;

  /**
   * 根据单位ID查询.
   * 
   * @param insId
   * @return
   * @throws DaoException
   */
  List<NsfcReschProjectReport> getProjectReportsByInsId(Long insId) throws DaoException;

  /**
   * @param nsfcPrjId
   * @param psnId
   * @return
   */
  List<NsfcReschProjectReport> getProjectReportByNsfcPrjId(Long nsfcPrjId, Long psnId) throws DaoException;

  /**
   * 保存V2.6同步数据.
   * 
   * @param obj
   * @throws DaoException
   */
  void saveSyncNsfcProjectReportTemp(SyncNsfcReschProjectReportTemp obj) throws DaoException;

  List<NsfcReschProjectReport> getProjectReportByPrjId(Long nsfcPrjId, Long psnId) throws DaoException;

  /**
   * 按项目编号和项目年度查询.
   * 
   * @param nsfcPrjId
   * @param rptYear
   * @return
   * @throws DaoException
   */
  List<NsfcReschProjectReport> getPrjRptByNsfcPrjIdYear(Long nsfcPrjId, Integer rptYear) throws DaoException;

  /**
   * 查找研究成果报告状态
   * 
   * @param rptId
   * @return
   * @throws DaoException
   */
  public Integer findStatusByRptId(Long rptId) throws DaoException;

}
