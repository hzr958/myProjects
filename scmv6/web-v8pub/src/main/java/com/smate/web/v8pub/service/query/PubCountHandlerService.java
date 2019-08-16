package com.smate.web.v8pub.service.query;

import java.util.Map;

import com.smate.core.base.exception.ServiceException;

/**
 * 成果统计数处理service
 * 
 * @author aijiangbin
 * @date 2018年7月24日
 */
public interface PubCountHandlerService {

  /**
   * 处理个人成果的统计数
   * 
   * @throws ServiceException
   */
  public Map<String, Object> querySnsPubCount(PubQueryDTO pubQueryDTO) throws ServiceException;

  /**
   * 处理群组成果的统计数
   * 
   * @throws ServiceException
   */
  public Map<String, Object> queryGrpPubCount(PubQueryDTO pubQueryDTO) throws ServiceException;

}
