package com.smate.center.batch.service.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Dynamic;

/**
 * 动态消息同步接口.
 * 
 * @author chenxiangrong
 * 
 */
public interface DynamicSyncService {
  /**
   * 同步初始动态.
   * 
   * @param dynamic
   * @param extJson
   * @throws ServiceException
   */
  void syncDynamic(Dynamic dynamic, String extJson, Integer resType) throws ServiceException;



}
