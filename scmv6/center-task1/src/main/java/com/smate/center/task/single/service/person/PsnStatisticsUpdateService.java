package com.smate.center.task.single.service.person;

import java.io.Serializable;

import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 人员统计信息更新服务.
 * 
 * @author lichangwen
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

  public void setPsnPendingConfirmPubNum(Long psnId, Integer pcfPubNum);

}
