package com.smate.center.task.service.pdwh.quartz;

import com.smate.center.task.exception.ServiceException;


/**
 * 关键词词频统计
 * 
 * @author warrior
 * 
 */
public interface PubAllkeywordDupService {

  /**
   * 刷新频数
   * 
   * @param keywordHash
   * @throws ServiceException
   */
  public void updateTf(Long keywordHash) throws ServiceException;
}
