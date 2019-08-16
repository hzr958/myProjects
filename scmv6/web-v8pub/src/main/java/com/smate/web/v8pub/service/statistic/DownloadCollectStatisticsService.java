package com.smate.web.v8pub.service.statistic;

import com.smate.web.v8pub.exception.ServiceException;

public interface DownloadCollectStatisticsService {


  /**
   * 添加记录
   * 
   * @param psnId 东西拥有者的PsnId
   * @param actionKey
   * @param actionType
   * @param dcPsnId 下载或者收藏者的ID
   * @param countType 操作类型 详情请看 StatisticsContant.java
   * @param ip TODO
   * @param resNode TODO
   * @throws ServiceException
   */
  public void addRecord(Long psnId, Long actionKey, Integer actionType, Long dcPsnId, String countType,
      Integer resNodeId, String ip) throws ServiceException;

}
