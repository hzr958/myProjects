package com.smate.center.batch.service.psn;

import com.smate.center.batch.exception.pub.ServiceException;



public interface PsnHtmlService {

  public void updatePsnHtmlRefresh(Long psnId) throws ServiceException;

  /**
   * 刷新人员html
   * 
   * @param psnId
   * @throws ServiceException
   */
  void saveToRefreshTask(Long psnId) throws ServiceException;

}
