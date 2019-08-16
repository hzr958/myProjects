package com.smate.center.open.service.nsfc.continueproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.nsfc.continueproject.ContinueProjectDao;
import com.smate.center.open.dao.nsfc.continueproject.ContinueProjectReportDao;
import com.smate.center.open.model.nsfc.continueproject.ContinueProject;
import com.smate.center.open.model.nsfc.continueproject.ContinueProjectReport;
import com.smate.center.open.model.nsfc.continueproject.SyncConProjectReport;
import com.smate.center.open.service.user.UserService;

/**
 * 相许项目报告 服务
 * 
 * @author tsz
 *
 */
@Service("conPrjRptService")
@Transactional(rollbackFor = Exception.class)
public class ConPrjRptServiceImpl implements ConPrjRptService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;
  @Autowired
  private ContinueProjectDao continueProjectDao;
  @Autowired
  private ContinueProjectReportDao continueProjectReportDao;

  @Override
  public void synConPrjReport(SyncConProjectReport report, Long psnId) throws Exception {
    try {
      // 同步延续项目
      ContinueProject project = this.continueProjectDao.get(report.getNsfcPrjCode());
      if (project == null) {
        project = new ContinueProject(report.getNsfcPrjCode(), report.getPno(), report.getCtitle(), psnId,
            report.getOrgName(), report.getDisno1());
      } else {
        // 项目负责人变更
        project.setPsnId(psnId);
      }
      this.continueProjectDao.save(project);

      // 同步延续项目报告
      ContinueProjectReport conReport = this.continueProjectReportDao.findReportInfo(report.getNsfcRptId());
      if (conReport == null) {
        conReport = new ContinueProjectReport(report.getNsfcRptId(), report.getRptYear(), report.getRptType(),
            report.getStatus(), report.getNsfcPrjCode());
      } else {
        logger.info("基金委报告ID，nsfcRptId=" + report.getNsfcRptId() + "的报告已经存在，将更新报告状态");
        conReport.setStatus(report.getStatus());
      }
      this.continueProjectReportDao.save(conReport);
    } catch (Exception e) {
      logger.error("同步或更新延续报告出错", e);
      throw new Exception("同步或更新延续出错", e);
    }
  }
}
