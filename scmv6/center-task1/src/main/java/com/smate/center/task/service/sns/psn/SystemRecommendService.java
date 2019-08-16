package com.smate.center.task.service.sns.psn;

import java.io.Serializable;

import com.smate.center.task.exception.ServiceException;


/**
 * @author tsz
 * 
 */
public interface SystemRecommendService extends Serializable {

  /**
   * 清空所有好友智能推荐得分表.
   * 
   * @throws ServiceException
   */
  void removeAllRecommendScore() throws ServiceException;

}
