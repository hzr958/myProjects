package com.smate.web.psn.service.dynamic;

import com.smate.web.psn.exception.ServiceException;

/**
 * 动态服务
 *
 * @author wsn
 * @createTime 2017年6月21日 下午2:15:28
 *
 */
public interface DynamicService {

  /**
   * 删除好友关系时对动态的处理
   * 
   * @param psnId 本人ID
   * @param friendId 好友ID
   * @throws ServiceException
   */
  public void minusFriendVisible(Long psnId, Long friendId) throws ServiceException;

  /**
   * 更新动态关系
   * 
   * @param syncFlag
   * @param producer
   * @param receiver
   * @throws ServiceException
   */
  public void syncRelation(int syncFlag, Long producer, Long receiver) throws ServiceException;
}
