package com.smate.center.batch.service.psn;

import java.util.List;
import java.util.Locale;

import com.smate.center.batch.exception.pub.ServiceException;

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

}
