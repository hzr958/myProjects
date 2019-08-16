package com.smate.web.dyn.service.pub.rol;

import java.io.Serializable;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.model.pub.rol.SiePubStatistics;

/**
 * SIE成果统计.
 * 
 * @author xys
 * 
 */
public interface SiePubStatisticsService extends Serializable {


  /**
   * 获取指定成果的相关统计.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public SiePubStatistics getSiePubStatistics(Long pubId) throws DynException;

  /**
   * 获取指定统计类型的统计数.
   * 
   * @param pubId
   * @param statType
   * @return
   * @throws ServiceException
   */
  Long getSiePubStatistic(Long pubId, int statType) throws DynException;

}
