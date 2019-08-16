package com.smate.center.batch.service.friend;

import java.io.Serializable;

import com.smate.center.batch.model.psn.register.PersonRegister;


/**
 * @author tsz
 * 
 */
public interface SystemRecommendService extends Serializable {

  /**
   * 系统智能推荐好友之即时推荐.
   * 
   * @param person
   * @throws ServiceException
   */
  void instantRecommend(PersonRegister person) throws Exception;


}
