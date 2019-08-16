package com.smate.center.batch.service.psn;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
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

  /**
   * 获取用户待认领成果数据.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public Integer getPsnPendingConfirmPubNum(Long psnId);

  /**
   * 获取用户的成果全文推荐总数_MJG_SCM-5991.
   * 
   * @param psnId
   * @return
   */
  Integer getPubFulltextReSum(Long psnId);

  /**
   * 更新用户待认领成果数据.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public void updateStatisticsPcfPub(Long psnId, int pendingCfmPubNum) throws ServiceException;

}
