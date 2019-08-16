package com.smate.center.open.service.data.isis;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public interface PrjFinalReportService {

  public List<Map<String, Object>> getPrjFinalReportList(Date deliverDate);

  /**
   * sie save NsfcPrjPubReport
   */
  public void saveNsfcPrjPubReport(List<Map<String, Object>> reportList, Map<Long, String> jidMap,
      Map<Long, Long> hxjMap, List<Long> inCategoryjidList);

  public List<NsfcPrjReport> getNsfcPrjReportListByDeliverDate(Date deliverDate);

  public List<NsfcProject> getNsfcProjectListByPrjId(Set<Long> prjIdSet);

  public List<NsfcPrjRptPub> getNsfcPrjRptPubListByRptId(Set<Long> rptIdSet);

  public void saveNsfcPrjReport(List<NsfcPrjReport> nsfcPrjReportList);

  public void saveNsfcProject(List<NsfcProject> nsfcProjectList);

  public void saveNsfcPrjRptPub(List<NsfcPrjRptPub> nsfcPrjRptPubList);

  /** 从SNS获取邮箱和姓名 **/
  List<Map<String, Object>> getPrjInfoMap(Set<Long> prjIdSet);

  public void updateNsfcProject(List<Map<String, Object>> snsPrjMapList);
}
