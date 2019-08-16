package com.smate.web.v8pub.service.sns;

import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 人员信息统计，该统计会同步sie.
 * 
 * @author lichangwen
 * 
 */
public interface PsnStatisticsService {
  /**
   * @param psnId
   * @return @
   */
  PsnStatistics getPsnStatistics(Long psnId) throws ServiceException;

  /**
   * 保存成果人员统计数记录
   * 
   * @param psnStatistics
   * @throws ServiceException
   */
  void savePsnStatistics(PsnStatistics psnStatistics) throws ServiceException;

  boolean hasPrivatePub(Long psnId);

  int getHindex(Long psnId) throws Exception;

  /**
   * 更新跑数据同步任务的状态.
   * 
   * @param psnId not null
   */
  void updatePsnStatisticsRefresh(Long psnId);
}
