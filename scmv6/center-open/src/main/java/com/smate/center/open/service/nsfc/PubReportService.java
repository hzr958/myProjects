package com.smate.center.open.service.nsfc;

import java.util.List;

import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;

public interface PubReportService {

  /**
   * 根据结题或者进展报告的ID获取需统计的成果
   * 
   * @param nsfcRptId
   * @return
   * @throws ServiceException
   */
  List<NsfcPrjRptPub> getPrjFinalPubsForStat(Long nsfcRptId) throws Exception;

  /**
   * 根据结题或者进展报告的ID获取成果信息
   * 
   * @param nsfcRptId
   * @return
   * @throws ServiceException
   */
  List<NsfcPrjRptPub> getProjectFinalPubs(Long nsfcRptId) throws Exception;

}
