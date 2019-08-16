package com.smate.web.psn.service.statistics;

import com.smate.web.psn.exception.ServiceException;

/**
 * 访问他人记录
 * 
 * @author zx
 *
 */
public interface VistStatisticsService {

  /**
   * 添加分享记录
   * 
   * @param psnId 分享人PsnID
   * @param VistPsnId 被分享人PsnId
   * @param actionKey 被分享的东西的主键
   * @param actionType 被分享的东西的类型 详情请看DynamicConstant.java
   * @param ip 操作人的IP地址
   * @throws ServiceException
   */
  public void addVistRecord(Long psnId, Long vistPsnId, Long actionKey, Integer actionType, String ip)
      throws ServiceException;
}
