package com.smate.web.management.service.psn;

import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 人员信息统计 服务接口
 * 
 * @author tsz
 *
 */
public interface PsnStatisticsService {

  /**
   * 通过人员id 获取统计数
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  public PsnStatistics getPsnStatistics(Long psnId);
}
