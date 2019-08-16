package com.smate.center.oauth.service.profile;


import com.smate.center.oauth.exception.OauthException;

/**
 * 个人专长、研究领域服务接口
 * 
 * @author Administrator
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
  boolean isPsnDiscExit(Long psnId) throws OauthException;


}
