package com.smate.center.open.service.data.isis;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.data.isis.NsfcPrjReportDao;
import com.smate.center.open.dao.data.isis.NsfcPrjRptPubDao;
import com.smate.center.open.dao.data.isis.NsfcProjectDao;
import com.smate.center.open.dao.data.isis.SyncPrjFinalReportDao;
import com.smate.center.open.isis.model.data.isis.NsfcPrjReport;
import com.smate.center.open.isis.model.data.isis.NsfcPrjRptPub;
import com.smate.center.open.isis.model.data.isis.NsfcProject;
// import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;



/**
 * 获取sns项目结题数据
 * 
 * @author hp
 * @date 2015-10-23
 */
@Service("prjFinalReportService")
@Transactional(rollbackFor = Exception.class)
public class PrjFinalReportServiceImpl implements PrjFinalReportService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SyncPrjFinalReportDao syncPrjFinalReportDao;
  @Autowired
  private NsfcPrjFinalStatService nsfcPrjFinalStatService;
  @Autowired
  private NsfcPrjReportDao nsfcPrjReportDao;
  @Autowired
  private NsfcProjectDao nsfcProjectDao;
  @Autowired
  private NsfcPrjRptPubDao nsfcPrjRptPubDao;
  @Autowired
  private PrjFinalReport2Service prjFinalReport2Service;

  @Override
  public List<Map<String, Object>> getPrjFinalReportList(Date deliverDate) {
    return syncPrjFinalReportDao.getPrjFinalReportList(deliverDate);
  }

  @Override
  public void saveNsfcPrjPubReport(List<Map<String, Object>> reportList, Map<Long, String> jidColorMap,
      Map<Long, Long> hxjMap, List<Long> inCategoryJidList) {
    nsfcPrjFinalStatService.saveNsfcPrjPubReport(reportList, jidColorMap, hxjMap, inCategoryJidList);
  }

  @Override
  public List<NsfcPrjReport> getNsfcPrjReportListByDeliverDate(Date deliverDate) {
    return nsfcPrjReportDao.getNsfcPrjReportListByDeliverDate(deliverDate);
  }

  @Override
  public List<NsfcProject> getNsfcProjectListByPrjId(Set<Long> prjIdSet) {
    return nsfcProjectDao.getNsfcProjectListByRptId(prjIdSet);
  }

  @Override
  public List<Map<String, Object>> getPrjInfoMap(Set<Long> prjIdSet) {
    return syncPrjFinalReportDao.getPrjInfoMap(prjIdSet);
  }

  @Override
  public List<NsfcPrjRptPub> getNsfcPrjRptPubListByRptId(Set<Long> rptIdSet) {
    return nsfcPrjRptPubDao.getNsfcPrjRptPubListByRptId(rptIdSet);
  }

  @Override
  public void saveNsfcPrjReport(List<NsfcPrjReport> nsfcPrjReportList) {
    prjFinalReport2Service.saveNsfcPrjReport(nsfcPrjReportList);
  }

  @Override
  public void saveNsfcProject(List<NsfcProject> nsfcProjectList) {
    prjFinalReport2Service.saveNsfcProject(nsfcProjectList);
  }

  @Override
  public void saveNsfcPrjRptPub(List<NsfcPrjRptPub> nsfcPrjRptPubList) {
    prjFinalReport2Service.saveNsfcPrjRptPub(nsfcPrjRptPubList);
  }

  @Override
  public void updateNsfcProject(List<Map<String, Object>> snsPrjMapList) {
    prjFinalReport2Service.updateNsfcProject(snsPrjMapList);
  }
}
