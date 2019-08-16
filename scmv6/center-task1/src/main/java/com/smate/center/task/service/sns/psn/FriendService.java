package com.smate.center.task.service.sns.psn;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.Friend;

public interface FriendService {
  /**
   * 智能推荐好友的好友.
   * 
   * @param page
   * @return
   * @throws ServiceException
   */

  List<Friend> findFriendByAutoRecommend(List<Long> psnIds) throws ServiceException;

  /**
   * @param psnId
   * @param freindPsnId
   * @return
   * @throws ServiceException
   */
  List<Friend> findRecommendFriend(Long psnId, Long freindPsnId) throws ServiceException;

  /**
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Friend> findFriend(Long psnId) throws ServiceException;

  /**
   * 判断传入的psnId是否与curPsnId是否是好友，curPsnId为当前节点用户lqh add.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isPsnFirend(Long curPsnId, Long psnId) throws ServiceException;

  List<Long> findFriendPsnId(Long psnId) throws ServiceException;
}
