package com.smate.center.task.service.sns.psn;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.exception.ServiceException;

/**
 * @author zll
 * 
 */
public interface SystemRecommendLoginService extends Serializable {


  /**
   * 系统智能推荐好友之定时推荐.
   * 
   * @throws ServiceException
   */
  void timingRecommendLogin(List<Long> psnIds) throws ServiceException;


}
