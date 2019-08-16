package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;

import com.smate.core.base.exception.ServiceException;

/**
 * 影响力付费表服务
 * 
 * @author hd
 *
 */
public interface SieKpiPayImpactService<T> {

  /**
   * 读取kpi_pay_impact表中需要处理的单位总数
   * 
   * @param nowDate
   * @return
   * @throws ServiceException
   */
  public Long cntNeedDeal(Date nowDate) throws ServiceException;

  /**
   * 设置ST_IMPACT_SYNC_REFRESH表中需要处理的单位
   * 
   * @param now
   * @throws ServiceException
   */
  public void setNeedDealall(Date now) throws ServiceException;

  /**
   * 读取REFRESH表中需要处理的单位
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public List<T> LoadNeedDealRecords(int maxSize) throws ServiceException;

  /**
   * 保存数据
   * 
   * @param refresh
   * @throws ServiceException
   */

  public void saveSieSTImpactSyncRefresh(T refresh) throws ServiceException;

  /**
   * 
   * @return
   * @throws ServiceException
   */
  public Boolean alreadyDealBase(Long insId) throws ServiceException;
}
