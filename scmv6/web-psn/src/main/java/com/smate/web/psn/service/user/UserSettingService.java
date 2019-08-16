package com.smate.web.psn.service.user;

import com.smate.web.psn.exception.ServiceException;

/**
 * 用户设置服务
 *
 * @author wsn
 * @createTime 2017年7月6日 下午5:03:15
 *
 */
public interface UserSettingService {

  /**
   * 构建并保存关注记录
   * 
   * @param psnId
   * @param reqPsnId
   * @throws ServiceException
   */
  public void buildAndSaveAttPerson(Long psnId, Long reqPsnId) throws ServiceException;
}
