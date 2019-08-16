package com.smate.center.batch.service.user;

import java.io.Serializable;
import java.util.Date;

import com.smate.center.batch.exception.pub.ServiceException;

public interface SysUserLoginLogService extends Serializable {
  /**
   * 查找上一次登陆时间
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Date findLastTimeByPsnId(Long psnId) throws ServiceException;
}
