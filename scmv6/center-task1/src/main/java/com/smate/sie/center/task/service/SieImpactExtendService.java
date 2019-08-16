package com.smate.sie.center.task.service;

import java.util.Date;

import com.smate.core.base.exception.ServiceException;

/**
 * 单位分析，拓展表服务
 * 
 * @author hd
 *
 */
public interface SieImpactExtendService {
  /**
   * 计算数据
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */

  public void doCalculate(Long insId, Date date) throws ServiceException;

  /**
   * 从各个方面对数据做计算，以时间主要
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void calByAspects(Long insId, Date date) throws ServiceException;

  /**
   * 从时间方面对数据做计算，以数据主要
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void calByData(Long insId, Date date) throws ServiceException;


  public void calByAspectsWithIP(Long insId, Date date) throws ServiceException;

  /**
   * 判断单位是否在扩展表中存在上个月的数据，避免重复数据
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  public boolean checkRepeat(Long insId) throws ServiceException;
}
