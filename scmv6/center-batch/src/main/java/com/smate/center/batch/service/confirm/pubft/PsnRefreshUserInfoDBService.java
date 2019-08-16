package com.smate.center.batch.service.confirm.pubft;

import com.smate.center.batch.service.pub.mq.PsnRefreshInfoMessage;

public interface PsnRefreshUserInfoDBService {
  /**
   * 初始化人员配置.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void saveRefreshInfo(PsnRefreshInfoMessage msg) throws Exception;
}
