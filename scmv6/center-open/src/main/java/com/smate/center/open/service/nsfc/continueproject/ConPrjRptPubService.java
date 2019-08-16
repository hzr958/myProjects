package com.smate.center.open.service.nsfc.continueproject;

import java.util.List;

import com.smate.center.open.model.nsfc.continueproject.ConPrjRptPub;


/**
 * 延续报告成果服务
 * 
 * @author tsz
 *
 */

public interface ConPrjRptPubService {

  /**
   * 查找某个人的报告成果
   * 
   * @param psnId
   * @param nsfcRptId
   * @return
   * @throws Exception
   */
  public List<ConPrjRptPub> findRptPubs(Long psnId, Long nsfcRptId) throws Exception;


}
