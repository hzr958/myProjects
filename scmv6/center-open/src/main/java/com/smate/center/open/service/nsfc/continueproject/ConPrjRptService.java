package com.smate.center.open.service.nsfc.continueproject;

import com.smate.center.open.model.nsfc.continueproject.SyncConProjectReport;

/**
 * 同步延续报告
 * 
 * @author tsz
 *
 */

public interface ConPrjRptService {


  /**
   * 同步或更新延续报告
   * 
   * @param report
   * @throws ServiceException
   */
  public void synConPrjReport(SyncConProjectReport report, Long psnId) throws Exception;
}
