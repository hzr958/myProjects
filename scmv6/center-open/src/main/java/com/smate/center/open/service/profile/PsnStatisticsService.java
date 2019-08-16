package com.smate.center.open.service.profile;

import java.io.Serializable;

import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 人员信息统计，该统计会同步sie.
 * 
 * @author lichangwen
 * 
 */
public interface PsnStatisticsService extends Serializable {

  /**
   * @param psnId
   * @return @
   */
  PsnStatistics getPsnStatistics(Long psnId);

}
