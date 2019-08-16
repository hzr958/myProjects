package com.smate.center.open.service.reschproject;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.model.nsfc.NsfcSyncProject;
import com.smate.center.open.model.nsfc.project.NsfcProject;
import com.smate.center.open.model.nsfc.project.NsfcProjectReport;
import com.smate.center.open.model.nsfc.project.NsfcReschProject;
import com.smate.center.open.model.nsfc.project.NsfcReschProjectReport;


@Service("snsSyncRolService")
@Transactional(rollbackFor = Exception.class)
public class SnsSyncRolServiceImpl implements SnsSyncRolService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SnsNsfcProjectService snsNsfcProjectService;

  @Autowired
  private InstitutionManager institutionManager;

  @Override
  public NsfcReschProject saveNsfcSyncReschProject(NsfcSyncProject nsfcSyncProject) throws Exception {
    NsfcReschProject nsfcReschProject = null;
    Long insId = null;
    try {
      // TODO:OYH 同步项目前,预处理异常
      long curSyncPsnId = NumberUtils.toLong(nsfcSyncProject.getPsnid());
      // 通过nsfcSyncProject的 projcode 找到 nsfcReschProject
      nsfcReschProject = snsNsfcProjectService.getSnsRolSyncReschProject(Long.valueOf(nsfcSyncProject.getPrjcode()));
      // 存在这个项目
      if (nsfcReschProject != null) {
        // 当前同步的人员与项目的所属人不匹配 ， // psnId 和同步项目的 psnId不同 ，更新
        if (nsfcReschProject.getPiPsnId().longValue() != curSyncPsnId) {
          snsNsfcProjectService.updateReschPsnId(Long.valueOf(nsfcSyncProject.getPrjcode()),
              nsfcReschProject.getPiPsnId().longValue());

        }
        NsfcReschProjectReport rpt = snsNsfcProjectService.findByRptByPrjId(Long.valueOf(nsfcSyncProject.getPrjcode()),
            Integer.valueOf(nsfcSyncProject.getRptyear()));
        if (rpt != null) {
          Integer status = NumberUtils.createInteger(nsfcSyncProject.getStatus());
          snsNsfcProjectService.updateReschRptStatus(rpt.getRptId(), status);
          logger.info("sns已经存在nsfcPrjId为：{},repyear为：{}的同步项目", nsfcSyncProject.getPrjcode(),
              nsfcSyncProject.getRptyear());
        } else {
          NsfcReschProjectReport nsfcProjectReport = new NsfcReschProjectReport();
          nsfcProjectReport.setRptYear(Integer.valueOf(nsfcSyncProject.getRptyear()));
          nsfcProjectReport.setNsfcRptId(Long.valueOf(nsfcSyncProject.getNsfcRptId()));
          nsfcProjectReport.setRptType(1);
          nsfcProjectReport.setStatus(0);
          nsfcProjectReport.setDeliverDate(new Date());
          nsfcProjectReport.setNsfcProject(nsfcReschProject);
          nsfcProjectReport = snsNsfcProjectService.saveNsfcReschProjectReport(nsfcProjectReport);
          nsfcReschProject.getNsfcProjectReports().add(nsfcProjectReport);
          logger.info("sns成功同步相同nsfcPrjId:{},不同repyear:{}的项目", nsfcSyncProject.getPrjcode(),
              nsfcSyncProject.getRptyear());
        }
        // 不存在
      } else {
        if (StringUtils.isNotBlank(nsfcSyncProject.getOrgname())) {
          // 通过单位名获取单位Id.
          insId = institutionManager.getInsIdByName(nsfcSyncProject.getOrgname(), nsfcSyncProject.getOrgname());
          if (insId != null) {
            nsfcSyncProject.setInsId(insId);
          }
        }
        // 保存研究成果啦
        nsfcReschProject = snsNsfcProjectService.saveSyncRolReschProject(nsfcSyncProject);
      }
    } catch (Exception e) {
      logger.error("sns同步NSFC项目数据保存在scm出错", e);
      throw new Exception(e);
    }
    return nsfcReschProject;
  }

  /**
   * 同步结题、进展报告的项目
   * 
   * @param nsfcSyncProject
   * @return
   * @throws ServiceException
   */
  @Override
  public NsfcProject syncNsfcProject(NsfcSyncProject nsfcSyncProject) throws Exception {
    NsfcProject nsfcProject = null;
    Long insId = null;

    try {

      long curSyncPsnId = NumberUtils.toLong(nsfcSyncProject.getPsnid());
      // 通过项目ID---》 nsfcProject
      nsfcProject = snsNsfcProjectService.getSnsRolSyncProject(Long.valueOf(nsfcSyncProject.getPrjcode()));
      if (nsfcProject != null) {
        // 当前同步的人员与项目的所属人不匹配
        if (nsfcProject.getPiPsnId().longValue() != curSyncPsnId) {
          nsfcProject.setPiPsnId(curSyncPsnId);
          // 更新负责人的时候要调用保存方法
          snsNsfcProjectService.updateRolProject(nsfcProject);
        }

        NsfcProjectReport nsfcPrt =
            snsNsfcProjectService.getNsfcRptByNsfcRptId(Long.valueOf(nsfcSyncProject.getNsfcRptId()));

        if (nsfcPrt != null) {
          if (NumberUtils.isNumber(nsfcSyncProject.getStatus())) {
            nsfcPrt.setStatus(Integer.valueOf(nsfcSyncProject.getStatus()));
          }
          snsNsfcProjectService.updateNsfcProjectReport(nsfcPrt);
          logger.info("sns已经存在nsfcRptId为：{},repyear为：{}的同步项目,更新状态为：" + nsfcSyncProject.getStatus(),
              nsfcSyncProject.getNsfcRptId(), nsfcSyncProject.getRptyear());
        } else {
          NsfcProjectReport nsfcProjectReport = new NsfcProjectReport();
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

          nsfcProjectReport = snsNsfcProjectService.saveNsfcProjectReport(nsfcProjectReport);

          // nsfcProject.getNsfcProjectReports().add(nsfcProjectReport);

          logger.info("sns成功同步相同nsfcRptId:{},不同repyear:{}的项目,报告类型为：" + nsfcSyncProject.getRptType(),
              nsfcSyncProject.getNsfcRptId(), nsfcSyncProject.getRptyear());
        }
      } else {
        if (StringUtils.isNotBlank(nsfcSyncProject.getOrgname())) {
          insId = institutionManager.getInsIdByName(nsfcSyncProject.getOrgname(), nsfcSyncProject.getOrgname());
          // 存在单位，并且有单位id
          if (insId != null) {
            nsfcSyncProject.setInsId(insId);
          }
        }
        // 保存啦
        nsfcProject = snsNsfcProjectService.syncRolProject(nsfcSyncProject);
      }
    } catch (Exception e) {
      logger.error("sns同步NSFC项目数据保存在scm出错", e);
      throw new Exception("sns同步NSFC项目数据保存在scm出错", e);
    }
    return nsfcProject;
  }



}
