package com.smate.center.oauth.service.profile;

import com.smate.center.oauth.exception.OauthException;

/**
 * 人员教育经历服务接口
 * 
 * @author Administrator
 *
 */
public interface EducationHistoryService {

  /**
   * 判断用户是否存在教育经历.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isEduHistoryExit(Long psnId) throws OauthException;
}
