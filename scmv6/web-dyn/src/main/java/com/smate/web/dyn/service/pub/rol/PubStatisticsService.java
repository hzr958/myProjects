package com.smate.web.dyn.service.pub.rol;

import java.io.Serializable;

import com.smate.core.base.utils.exception.DynException;

/**
 * 成果统计.
 * 
 * @author zk
 * 
 */
public interface PubStatisticsService extends Serializable {


  /**
   * 保存或更新指定统计类型的成果统计.
   * 
   * @param pubId
   * @param statType
   * @throws ServiceException
   */
  public void saveOrUpdatePubStatistic(Long pubId, int statType) throws DynException;

  /**
   * 保存或更新指定统计类型的成果统计. 由于新环境中从数据库中查出数据后进行了修改，再保存，然后再查询，得到的数据仍然是旧数据 所以直接传入一个数字进行操作
   * 
   * @param pubId
   * @param statType
   * @throws ServiceException
   */
  public void saveOrUpdatePubStatisticNew(Long pubId, int statType, Long Num) throws DynException;

}
