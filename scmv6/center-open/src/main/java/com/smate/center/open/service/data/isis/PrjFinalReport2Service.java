package com.smate.center.open.service.data.isis;



import java.util.List;
import java.util.Map;

import com.smate.center.open.isis.model.data.isis.NsfcPrjReport;
import com.smate.center.open.isis.model.data.isis.NsfcPrjRptPub;
import com.smate.center.open.isis.model.data.isis.NsfcProject;
// import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;

/**
 * save项目结题数据
 * 
 * @author hp
 * @date 2015-10-23
 */
public interface PrjFinalReport2Service {


  public void saveNsfcPrjReport(List<NsfcPrjReport> nsfcPrjReportList);

  public void saveNsfcProject(List<NsfcProject> nsfcProjectList);

  public void saveNsfcPrjRptPub(List<NsfcPrjRptPub> nsfcPrjRptPubList);

  public void updateNsfcProject(List<Map<String, Object>> snsPrjMapList);
}
