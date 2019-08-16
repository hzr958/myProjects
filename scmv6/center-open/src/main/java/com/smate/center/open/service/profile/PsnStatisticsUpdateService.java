package com.smate.center.open.service.profile;

import java.io.Serializable;

import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 人员统计信息更新服务.
 * 
 * @author wsn
 * 
 */
public interface PsnStatisticsUpdateService extends Serializable {

  /**
   * 初始化人员统计信息.
   * 
   * @param psnId
   * @return @
   */
  public PsnStatistics initPsnStatics(Long psnId);

}
