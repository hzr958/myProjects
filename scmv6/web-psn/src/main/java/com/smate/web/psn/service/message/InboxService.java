package com.smate.web.psn.service.message;

import java.util.Map;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.InviteInbox.InviteInbox;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

/**
 * 收件箱.
 * 
 * @author zx
 * 
 */
public interface InboxService {

  /**
   * 判断是否已经有了相同的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；2.群组邀请 3.请求加入群组
   * @return
   */
  boolean isRepeatInvite(Long recId, Long senderId, String type);

  /**
   * 忽略重复的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；
   */
  void ignoreRepeatInvite(Long recId, Long senderId, String type) throws ServiceException;

  /**
   * 根据请求参数对当前邀请进行处理.
   * 
   * @param key 邀请类型.
   * @param paramMap 请求参数.
   * @return 0-成功；1-邀请已被其他用户绑定.
   * @throws ServiceException
   */
  Integer dealInviteBusiness(String key, Map<String, String> paramMap) throws ServiceException;

  /**
   * 根据收件箱ID检索记录.
   * 
   * @param inboxId
   * @return
   * @throws ServiceException
   */
  InviteInbox getInviteInboxById(Long inboxId) throws ServiceException;

  /**
   * 获取邀请收件箱.
   * 
   * @param psnId
   * @param mailId
   * @return
   * @throws ServiceException
   */
  InviteInbox getInviteInbox(Long psnId, Long mailId) throws ServiceException;

  /**
   * 去除重复的好友邀请
   * 
   * @param sendPsnId
   * @param senderPsnId
   * @throws ServiceException
   */
  void removalRepeatInvite(Long sendPsnId, Long senderPsnId) throws ServiceException;


}
