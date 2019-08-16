package com.smate.sie.center.task.service;

import java.util.Date;

import com.smate.core.base.exception.ServiceException;

/**
 * 单位分析，**EXTEND2 服务
 * 
 * @author hd
 *
 */
public interface SieImpactCalDataService {

  /**
   * 从业务方面对阅读数据做分析
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void calViewByData(Long insId, Date date) throws ServiceException;

  /**
   * 从业务方面对赞数据做分析
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void calAwardByData(Long insId, Date date) throws ServiceException;

  /**
   * 从业务方面对下载数据做分析
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void calDownloadByData(Long insId, Date date) throws ServiceException;

  /**
   * 从业务方面对分析数据做分析
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void calShareByData(Long insId, Date date) throws ServiceException;

  /**
   * 删除两个月之前的数据
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void delTwoMonBeforeData(Long insId, Date date) throws ServiceException;

  /**
   * 查重
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  public boolean checkRepeatWithData(Long insId) throws ServiceException;

}
