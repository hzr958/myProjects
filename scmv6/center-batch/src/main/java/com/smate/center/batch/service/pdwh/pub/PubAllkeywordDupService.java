package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.exception.pub.ServiceException;


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
