package com.smate.center.task.single.service.person;

import com.smate.center.task.exception.ServiceException;

/**
 * @author zt
 * 
 */
public interface PersonalManager {

  /**
   * 获取刷新用户信息完整度的数据.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isPsnDiscExit(Long psnId) throws ServiceException;

  Long getPsnInsRegionId(Long psnId);

}
