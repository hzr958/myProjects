package com.smate.center.open.service.reschproject;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.prj.SnsNsfcProjectDao;
import com.smate.center.open.dao.prj.SnsNsfcProjectReportDao;
import com.smate.center.open.dao.reschproject.SnsNsfcReschPrjRptDao;
import com.smate.center.open.dao.reschproject.SnsReschProjectDao;
import com.smate.center.open.model.nsfc.NsfcSyncProject;
import com.smate.center.open.model.nsfc.project.NsfcProject;
import com.smate.center.open.model.nsfc.project.NsfcProjectReport;
import com.smate.center.open.model.nsfc.project.NsfcReschProject;
import com.smate.center.open.model.nsfc.project.NsfcReschProjectReport;
import com.smate.core.base.utils.date.DateUtils;

/**
 * nsfc项目ws同步服务.
 * 
 * @author ajb
 * 
 */
@Service("snsNsfcProjectService")
@Transactional(rollbackFor = Exception.class)
public class SnsNsfcProjectServiceImpl implements SnsNsfcProjectService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SnsReschProjectDao snsReschProjectDao;

  @Autowired
  private SnsNsfcReschPrjRptDao snsNsfcReschPrjRptDao;

  @Autowired
  private SnsNsfcProjectDao snsNsfcProjectDao;

  @Autowired
  private SnsNsfcProjectReportDao snsNsfcProjectReportDao;

  @Override
  public NsfcReschProject getSnsRolSyncReschProject(Long nsfcPrjId) throws Exception {
    try {
      return snsReschProjectDao.getNsfcReschProject(nsfcPrjId);
    } catch (Exception e) {
      logger.error("根据nsfcPrjId:{}查询nsfcPrjId表出错", nsfcPrjId, e);
    }
    return null;
  }

  @Override
  public NsfcReschProject saveSyncRolReschProject(NsfcSyncProject nsfcSyncProject) throws Exception {
    try {
      NsfcReschProject nsfcProject = new NsfcReschProject();
      nsfcProject.setPrjId(Long.valueOf(nsfcSyncProject.getPrjcode() + DateUtils.format(new Date(), "HHmmss")));
      nsfcProject.setPiPsnId(Long.valueOf(nsfcSyncProject.getPsnid()));
      nsfcProject.setNsfcPrjId(Long.valueOf(nsfcSyncProject.getPrjcode()));
      if (StringUtils.isNotBlank(nsfcSyncProject.getPno()))
        nsfcProject.setPno(nsfcSyncProject.getPno());
      if (StringUtils.isNotBlank(nsfcSyncProject.getCtitle()))
        nsfcProject.setCtitle(nsfcSyncProject.getCtitle());
      if (StringUtils.isNotBlank(nsfcSyncProject.getDisno1()))
        nsfcProject.setDisNo1(nsfcSyncProject.getDisno1());
      if (StringUtils.isNotBlank(nsfcSyncProject.getDivno()))
        nsfcProject.setDivno(nsfcSyncProject.getDivno());
      if (nsfcSyncProject.getInsId() != null)
        nsfcProject.setInsId(nsfcSyncProject.getInsId());
      if (StringUtils.isNotBlank(nsfcSyncProject.getOrgname()))
        nsfcProject.setInsName(nsfcSyncProject.getOrgname());

      NsfcReschProjectReport nsfcProjectReport = new NsfcReschProjectReport();
      nsfcProjectReport.setRptId(snsNsfcReschPrjRptDao.getReportId());
      nsfcProjectReport.setRptYear(Integer.valueOf(nsfcSyncProject.getRptyear()));
      nsfcProjectReport.setRptType(1);
      nsfcProjectReport.setStatus(0);
      nsfcProjectReport.setDeliverDate(new Date());
      nsfcProjectReport.setNsfcProject(nsfcProject);

      nsfcProject.getNsfcProjectReports().add(nsfcProjectReport);
      snsReschProjectDao.save(nsfcProject);
      return nsfcProject;
    } catch (Exception e) {
      logger.error("同步nsfc项目数据保存出错！", e);
    }
    return null;
  }

  @Override
  public NsfcReschProjectReport findByRptByPrjId(Long nsfcPrjId, Integer year) throws Exception {
    return this.snsNsfcReschPrjRptDao.getNsfcProjectReport(nsfcPrjId, year);
  }

  @Override
  public void updateReschRptStatus(Long rptId, Integer status) throws Exception {
    try {
      this.snsNsfcReschPrjRptDao.updateReschRptStatus(rptId, status);
    } catch (Exception e) {
      logger.error("更新成果研究报告状态出错！rptId=" + rptId, e);
      throw new Exception("更新成果研究报告状态出错！rptId=", e);
    }

  }

  @Override
  public NsfcReschProjectReport saveNsfcReschProjectReport(NsfcReschProjectReport nsfcProjectReport) throws Exception {
    try {
      nsfcProjectReport.setRptId(snsNsfcReschPrjRptDao.getReportId());
      snsNsfcReschPrjRptDao.save(nsfcProjectReport);
      return nsfcProjectReport;
    } catch (Exception e) {
      throw new Exception("sns保存已经存在的项目prjId:" + nsfcProjectReport.getNsfcRptId() + "，不同报告年份出错", e);
    }

  }

  @Override
  public void updateReschPsnId(Long prjId, Long psnId) throws Exception {
    try {
      this.snsReschProjectDao.updateReschPsnId(prjId, psnId);
    } catch (Exception e) {
      logger.error("更新成果研究报告拥有者出错！prjId=" + prjId + " psnId=" + psnId, e);
      throw new Exception("更新成果研究报告拥有者出错", e);
    }

  }

  @Override
  public NsfcProject getSnsRolSyncProject(Long nsfcPrjId) throws Exception {
    try {
      return snsNsfcProjectDao.getNsfcProject(nsfcPrjId);
    } catch (Exception e) {
      logger.error("根据nsfcPrjId:{}查询nsfcPrjId表出错", nsfcPrjId, e);
    }
    return null;
  }

  @Override
  public NsfcProject syncRolProject(NsfcSyncProject nsfcSyncProject) throws Exception {
    try {
      NsfcProject nsfcProject = new NsfcProject();
      nsfcProject.setPrjId(Long.valueOf(nsfcSyncProject.getPrjcode() + DateUtils.format(new Date(), "HHmmss")));
      nsfcProject.setPiPsnId(Long.valueOf(nsfcSyncProject.getPsnid()));
      nsfcProject.setNsfcPrjId(Long.valueOf(nsfcSyncProject.getPrjcode()));
      if (StringUtils.isNotBlank(nsfcSyncProject.getPno()))
        nsfcProject.setPno(nsfcSyncProject.getPno());
      if (StringUtils.isNotBlank(nsfcSyncProject.getCtitle()))
        nsfcProject.setCtitle(nsfcSyncProject.getCtitle());
      if (StringUtils.isNotBlank(nsfcSyncProject.getDisno1()))
        nsfcProject.setDisNo1(nsfcSyncProject.getDisno1());
      if (StringUtils.isNotBlank(nsfcSyncProject.getDivno()))
        nsfcProject.setDivno(nsfcSyncProject.getDivno());
      if (nsfcSyncProject.getInsId() != null)
        nsfcProject.setInsId(nsfcSyncProject.getInsId());
      if (StringUtils.isNotBlank(nsfcSyncProject.getOrgname()))
        nsfcProject.setInsName(nsfcSyncProject.getOrgname());

      NsfcProjectReport nsfcProjectReport = new NsfcProjectReport();
      nsfcProjectReport.setRptId(snsNsfcProjectReportDao.getReportId());
      nsfcProjectReport.setRptYear(Integer.valueOf(nsfcSyncProject.getRptyear()));
      // 结题报告还是进展报告
      nsfcProjectReport.setRptType(Integer.valueOf(nsfcSyncProject.getRptType()));
      nsfcProjectReport.setNsfcRptId(Long.valueOf(nsfcSyncProject.getNsfcRptId()));
      if (NumberUtils.isNumber(nsfcSyncProject.getStatus())) {
        nsfcProjectReport.setStatus(Integer.valueOf(nsfcSyncProject.getStatus()));
      } else {
        nsfcProjectReport.setStatus(0);
      }
      nsfcProjectReport.setDeliverDate(new Date());
      nsfcProjectReport.setNsfcProject(nsfcProject);
      snsNsfcProjectReportDao.save(nsfcProjectReport);
      nsfcProject.getNsfcProjectReports().add(nsfcProjectReport);
      snsNsfcProjectDao.save(nsfcProject);

      return nsfcProject;
    } catch (Exception e) {
      logger.error("同步nsfc项目数据保存在nsfcrol端出错", e);
    }
    return null;
  }

  @Override
  public NsfcProjectReport getNsfcRptByNsfcRptId(Long nsfcRptId) throws Exception {
    try {
      return snsNsfcProjectReportDao.findReportByNsfcRptId(nsfcRptId);
    } catch (Exception e) {
      logger.error("*************************查找报告nsfcRptId：{}出错************************", nsfcRptId);
      throw new Exception(e);
    }
  }

  @Override
  public NsfcProjectReport updateNsfcProjectReport(NsfcProjectReport nsfcProjectReport) throws Exception {
    snsNsfcProjectReportDao.save(nsfcProjectReport);
    return nsfcProjectReport;
  }

  @Override
  public NsfcProjectReport saveNsfcProjectReport(NsfcProjectReport nsfcProjectReport) throws Exception {
    try {
      nsfcProjectReport.setRptId(snsNsfcProjectReportDao.getReportId());
      snsNsfcProjectReportDao.save(nsfcProjectReport);
      return nsfcProjectReport;
    } catch (Exception e) {
      throw new Exception("sns保存已经存在的项目prjId:" + nsfcProjectReport.getNsfcRptId() + "，不同报告年份出错", e);
    }

  }

  @Override
  public NsfcProject updateRolProject(NsfcProject nsfcProject) throws Exception {
    snsNsfcProjectDao.save(nsfcProject);
    return nsfcProject;
  }
}
