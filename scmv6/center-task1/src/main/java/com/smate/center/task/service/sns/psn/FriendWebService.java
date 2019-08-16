package com.smate.center.task.service.sns.psn;

import com.smate.center.task.exception.ServiceException;

public interface FriendWebService {
  /**
   * tempPsnId是psnId多少个好友的好友.
   * 
   * @param psnId
   * @param tempPsnId
   * @return
   * @throws ServiceException
   */
  int getMatchPsnFriendsCount(Long psnId, Long tempPsnId) throws ServiceException;
}
