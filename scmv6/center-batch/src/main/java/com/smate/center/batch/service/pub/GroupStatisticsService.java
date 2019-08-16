package com.smate.center.batch.service.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.mq.SnsSyncGroupStatisticsMessage;

/**
 * 合作分析：群组统计信息service.
 */
public interface GroupStatisticsService {

  /**
   * 不存在的单位.
   */
  final Long NOTEXIST_INSID = -1L;

  /**
   * 合作分析：添加或更新群组统计信息.
   * 
   * @param msg
   * @throws ServiceException
   */
  public void handleAddOrUpdateSync(SnsSyncGroupStatisticsMessage msg) throws ServiceException;

  /**
   * 按群组ID删除合作分析数据.
   * 
   * @param groupId
   * @throws ServiceException
   */
  public void handleDelSync(Long groupId) throws ServiceException;


}
