package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.center.task.exception.ServiceException;

public interface SieStInsService<T> {
  /**
   * 获取统计的Id
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public List<T> loadNeedCountKeyId(int maxSize) throws ServiceException;

  /**
   * 根据Id统计相应数据
   * 
   * @param InsId
   */
  public void doStatistics(Long keyId) throws ServiceException;

  /**
   * 重置处理状态为未处理
   * 
   * @throws ServiceException
   */
  public void updateStatus() throws ServiceException;

  /**
   * 获取需要统计的记录数
   * 
   * @return
   * @throws ServiceException
   */
  public Long countNeedCountKeyId() throws ServiceException;

  /**
   * 初始化需要统计的记录数
   * 
   * @return
   * @throws ServiceException
   */
  public void setNeedCountKeyId() throws ServiceException;

  /**
   * 保存
   * 
   * @param insStatRefresh
   * @throws ServiceException
   */
  public void saveStRefresh(T stRefresh) throws ServiceException;



}
