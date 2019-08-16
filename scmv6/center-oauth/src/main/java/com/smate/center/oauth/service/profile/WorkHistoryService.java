package com.smate.center.oauth.service.profile;

import com.smate.center.oauth.exception.OauthException;



/**
 * 人员工作经历服务接口
 * 
 * @author Administrator
 *
 */
public interface WorkHistoryService {


  /**
   * 判断用户是否存在教育经历.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isWorkHistoryExit(Long psnId) throws OauthException;
}
