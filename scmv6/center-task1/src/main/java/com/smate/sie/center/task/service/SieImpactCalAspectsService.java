package com.smate.sie.center.task.service;

import java.text.ParseException;
import java.util.Date;

import com.smate.core.base.exception.ServiceException;

/**
 * 单位分析，**EXTEND1 服务
 * 
 * @author hd
 *
 */
public interface SieImpactCalAspectsService {
  /**
   * 从各个方面对阅读数据做分析
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */

  public void calViewByAspects(Long insId, Date date) throws ServiceException;

  public void calViewByItem(Long insId, Date date, Integer keyType) throws ServiceException;

  public void calViewByDate(Long insId, Date date) throws ServiceException;

  /**
   * 从各个方面对赞数据做分析
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void calAwardByAspects(Long insId, Date date) throws ServiceException;

  public void calAwardByItem(Long insId, Date date, Integer keyType) throws ServiceException;

  public void calAwardByDate(Long insId, Date date) throws ServiceException;

  /**
   * 从各个方面对下载数据做分析
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void calDownLoadByAspects(Long insId, Date date) throws ServiceException;

  public void calDownLoadByItem(Long insId, Date date, Integer keyType) throws ServiceException;

  public void calDownLoadByDate(Long insId, Date date) throws ServiceException;

  /**
   * 从各个方面对分析数据做分析
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void calShareByAspects(Long insId, Date date) throws ServiceException;

  public void calShareByItem(Long insId, Date date, Integer keyType) throws ServiceException;

  public void calShareByDate(Long insId, Date date) throws ServiceException;

  /**
   * 删除两个月之前的数据
   * 
   * @param insId
   * @param date
   * @throws ServiceException
   */
  public void delTwoMonBeforeData(Long insId, Date date) throws ServiceException;


  /**
   * 根据ip访问情况获取访客数
   * 
   * @param insId
   * @param date
   */
  public void calViewIpCntByAspects(Long insId, Date date);

  public void calViewIpCntByItem(Long insId, Date date, Integer batch, Integer keyType)
      throws ServiceException, ParseException;

  public boolean checkRepeatWithAspects(Long insId, Date startDate, Date endDate) throws ServiceException;
}
