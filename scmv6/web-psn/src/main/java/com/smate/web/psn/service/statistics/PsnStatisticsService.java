package com.smate.web.psn.service.statistics;

import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.web.psn.exception.PsnException;

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
  public PsnStatistics getPsnStatistics(Long psnId) throws PsnException;

  /**
   * 初始化人员统计信息
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  public PsnStatistics initPsnStatistics(Long psnId, Integer frdSum) throws PsnException;

  /**
   * 更新好友统计信息
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void updatePsnStatisticsByFrd(Long psnId, Integer frdSum) throws PsnException;

  /**
   * 更新人员阅读总数
   * 
   * @param psnId
   * @throws PsnException
   */
  public void updatePsnVisitSum(Long psnId) throws PsnException;
}
