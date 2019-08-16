package com.smate.web.dyn.service.pub.rol;

import java.io.Serializable;

import com.smate.core.base.utils.exception.DynException;

/**
 * SNS同步成果相关统计.
 * 
 * @author zk
 * 
 */
public interface SnsSyncPubStatisticsService extends Serializable {

  /**
   * 处理同步的统计信息.
   * 
   * @param siePubId
   * @param snsPubId
   * @param actionType
   * @param syncValue
   * @throws DynException
   */
  void handleSyncStatics(Long siePubId, Long snsPubId, Integer actionType, Integer syncValue) throws DynException;

  /**
   * 保存或更新SNS同步的成果相关统计.
   * 
   * @param siePubId
   * @param snsPubId
   * @param actionType
   * @param syncValue
   * @throws DynException
   */
  public void saveOrUpdatePubStatics(Long siePubId, Long snsPubId, Integer actionType, Integer syncValue)
      throws DynException;

  /**
   * 获取指定统计类型的统计数.
   * 
   * @param siePubId
   * @param statType
   * @return
   * @throws ServiceException
   */
  Long getSnsSyncPubStatistic(Long siePubId, int statType) throws DynException;

}
