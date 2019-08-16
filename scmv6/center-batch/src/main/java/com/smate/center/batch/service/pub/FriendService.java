package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.FriendTemp;
import com.smate.core.base.utils.model.security.Person;

/**
 * 工作经历服务接口.
 * 
 * @author lichangwen
 * 
 */
public interface FriendService extends Serializable {
  /**
   * 判断传入的psnId是否与登录者是好友<br/>
   * psnId如不在当前登录节点调用该方法,请使用getFriendService(psnId)服务.
   * 
   * @param psnId
   * @param isLocale TODO
   * @return
   * @throws ServiceException
   */
  int isPsnFirend(Long psnId, boolean isLocale) throws ServiceException;

  /**
   * 判断传入的psnId是否与curPsnId是否是好友，curPsnId为当前节点用户lqh add.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean isPsnFirend(Long curPsnId, Long psnId) throws ServiceException;

  /**
   * 判断传入的psnIds是否与curPsnId是否是好友，curPsnId为当前节点用户lqh add.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Map<Long, Integer> isPsnFirend(Long curPsnId, List<Long> psnIds) throws ServiceException;

  /**
   * 根据邀请Id查找关系.
   * 
   * @param inviteId
   * @return
   * @throws ServiceException
   */
  FriendTemp getFriendTempByInviteId(Long inviteId) throws ServiceException;

  /**
   * 检查邀请链接是否失效.
   * 
   * @param inboxId
   * @param inviteId
   * @return
   * @throws ServiceException
   */
  boolean checkInviteIsValid(Long inboxId, Long inviteId) throws ServiceException;

  /**
   * 更新被邀请人PsnId.
   * 
   * @param inviteId
   * @param tempPsnId
   * @throws ServiceException
   */
  void updateFriendInvite(Long inviteId, Long tempPsnId) throws ServiceException;

  /**
   * 更新人员的评价信息.
   * 
   * @param message
   * @throws ServiceException
   */
  void syncPersonFappraisal(Person person);

  /**
   * 同步数据.
   * 
   * @param inviteId
   * @param tempPsnId
   * @throws ServiceException
   */
  void updatePersonInfo(Person person);

}
