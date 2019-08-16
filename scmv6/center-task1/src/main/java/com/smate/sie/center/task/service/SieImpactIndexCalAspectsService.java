package com.smate.sie.center.task.service;

import java.util.Date;

import com.smate.core.base.exception.ServiceException;

public interface SieImpactIndexCalAspectsService {

  /**
   * 从各个方面对主页数据做分析
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */

  public void calIndexByAspects(Long insId, Date date) throws ServiceException;

  public void calIndexByItem(Long insId, Date date) throws ServiceException;

  public void calIndexByDate(Long insId, Date date) throws ServiceException;

  /**
   * 删除两个月之前的数据
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void delTwoMonBeforeData(Long insId, Date date) throws ServiceException;

  /**
   * 删除两个月之前的访客数据
   * 
   * @param insId
   * @param date
   */
  public void delTwoMonBeforeIPData(Long insId, Date date);

  public boolean checkRepeatByIndex(Long insId, Date startDate, Date endDate) throws ServiceException;
}
