package com.smate.center.batch.service.confirm.pubft;

import com.smate.core.base.psn.model.PsnStatistics;

public interface PsnStatisticsMessageService {
  /**
   * 更新rol和rcmd库的psnStatic和rol的PSN_HTML_REFRESH
   * 
   * @param psnId
   * @throws ServiceException
   */
  void updatePsnStatics(PsnStatistics statistics) throws Exception;
}
