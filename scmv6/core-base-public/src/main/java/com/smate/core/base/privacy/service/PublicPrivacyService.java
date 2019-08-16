package com.smate.core.base.privacy.service;

/**
 * 公共的隐私权限服务类，获取用户操作权限
 * 
 * @author aijiangbin
 *
 */
public interface PublicPrivacyService {

  /**
   * 能否给用户发送消息
   * 
   * @param currentPsnId 当前人
   * @param consumerPsnId 发消息的用户
   * @return
   */
  public Boolean canSendMsg(Long currentPsnId, Long consumerPsnId);

  /**
   * 能否添加用户为好友
   * 
   * @param currentPsnId 当前人
   * @param reconsumerPsnId 添加的用户ceivePsnId
   * @return
   */
  public Boolean canAddFriend(Long currentPsnId, Long consumerPsnId);

  /**
   * 能否查看用户的用户为好友
   * 
   * @param sendPsnId 当前人
   * @param consumerPsnId 用户
   * @return
   */
  public Boolean canLookConsumerFriends(Long currentPsnId, Long consumerPsnId);

  /**
   * 能否查看用户的添加文献的动态
   * 
   * @param sendPsnId 当前人
   * @param consumerPsnId 用户
   * @return
   */
  public Boolean canLookConsumerAddRefDyn(Long currentPsnId, Long consumerPsnId);

}
