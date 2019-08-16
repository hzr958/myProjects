package com.smate.sie.center.task.service;

import com.smate.core.base.exception.ServiceException;

/**
 * 
 * @author hd
 *
 */
public interface SieImpactBaseService {

  /**
   * 处理基表数据
   * 
   * @param insId
   * @throws ServiceException
   */
  public void doDeal(Long insId) throws ServiceException;

  /**
   * 处理基表成果数据
   * 
   * @param insId
   * @throws ServiceException
   */

  public void doDealPUB(Long insId) throws ServiceException;

  /**
   * 处理基表专利数据
   * 
   * @param insId
   * @throws ServiceException
   */

  public void doDealPAT(Long insId) throws ServiceException;

  /**
   * 处理基表项目数据
   * 
   * @param insId
   * @throws ServiceException
   */

  public void doDealPRJ(Long insId) throws ServiceException;

  /**
   * 处理基表主页数据
   * 
   * @param insId
   * @throws ServiceException
   */

  public void doDealIndex(Long insId) throws ServiceException;

  /**
   * 处理基表阅读数据
   * 
   * @param insId
   * @param title 数据标题
   * @param keyId 数据主键
   * @param keyType 数据类型
   * @throws ServiceException
   */

  public void doDealView(Long insId, String title, Long keyId, Integer keyType) throws ServiceException;

  /**
   * 处理基表赞数据
   * 
   * @param insId
   * @param title 数据标题
   * @param keyId 数据主键
   * @param keyType 数据类型
   * @throws ServiceException
   */

  public void doDealAward(Long insId, String title, Long keyId, Integer keyType) throws ServiceException;

  /**
   * 处理基表下载数据
   * 
   * @param insId
   * @param title 数据标题
   * @param keyId 数据主键
   * @param keyType 数据类型
   * @throws ServiceException
   */
  public void doDealDownload(Long insId, String title, Long keyId, Integer keyType) throws ServiceException;

  /**
   * 处理基表分享数据
   * 
   * @param insId
   * @param title 数据标题
   * @param keyId 数据主键
   * @param keyType 数据类型
   * @throws ServiceException
   */

  public void doDealShare(Long insId, String title, Long keyId, Integer keyType) throws ServiceException;

  /**
   * 判断单位是否在基表中存在上个月的数据，避免重复数据
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  public boolean checkRepeat(Long insId) throws ServiceException;
}
