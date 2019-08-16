package com.smate.center.batch.service.mail;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InsideInbox;
import com.smate.center.batch.model.mail.InsideInboxCon;
import com.smate.center.batch.model.mail.InsideMailBox;
import com.smate.center.batch.model.mail.InviteBoxCon;
import com.smate.center.batch.model.mail.InviteInbox;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.model.mail.PsnInsideInbox;
import com.smate.center.batch.model.mail.PsnInviteInbox;
import com.smate.center.batch.model.mail.PsnInviteMailBox;
import com.smate.center.batch.model.mail.PsnMessageNoticeInBox;
import com.smate.center.batch.model.mail.PsnMessageNoticeOutBox;
import com.smate.center.batch.model.mail.PsnReqInbox;
import com.smate.center.batch.model.mail.PsnShareInbox;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.model.sns.pub.RecordRelation;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;

/**
 * 收件箱
 * 
 * @author oyh
 * 
 */
public interface InboxService {

  final Long INVALID_MAIL_PSN_ID = -1L;// 人员合并后被删除用户的ID_MJG_人员合并_20130930.
  final Integer INVITE_DEAL_RESULT_OTHER = 1;
  final Integer INVITE_DEAL_RESULT_NONE = 2;
  final Integer INVITE_DEAL_RESULT_SUCCESS = 0;

  /**
   * 加载站内短消息收件箱.
   * 
   * @param page
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  Page<PsnInsideInbox> loadInsideInbox(Page<PsnInsideInbox> page, Map paramMap) throws ServiceException;

  /**
   * 站内搜索加载站内短消息收件箱.
   * 
   * @param page
   * @param searchKey
   * @return
   * @throws ServiceException
   */
  Page<PsnInsideInbox> loadInsideInbox(Page<PsnInsideInbox> page, String searchKey) throws ServiceException;

  /**
   * 加载站内通知 收信箱.
   * 
   * @author yangpeihai
   * @return
   */
  @SuppressWarnings("rawtypes")
  Page<PsnMessageNoticeInBox> loadMessageNoticeInBox(Page<PsnMessageNoticeInBox> page, Map paramMap)
      throws ServiceException;

  /**
   * 站内搜索,加载站内通知 收件箱.
   * 
   * @author yangpeihai
   * @param page
   * @param searchKey
   * @return
   * @throws ServiceException
   */
  Page<PsnMessageNoticeInBox> loadMessageNoticeInBox(Page<PsnMessageNoticeInBox> page, String searchKey)
      throws ServiceException;

  /**
   * 删除站内通知.
   * 
   * @author yangpeihai
   * @param ids
   * @param type
   * @throws ServiceException
   */
  void deleteMessageNoticeById(String ids, String type) throws ServiceException;

  /**
   * 根据Id获取站内通知发信箱实体.
   * 
   * @author yangpeihai
   * @param msg
   * @return
   * @throws ServiceException
   */
  public PsnMessageNoticeOutBox getNoticeOutDetailById(Message msg) throws ServiceException;

  /**
   * 加载请求更新收件箱.
   * 
   * @param page
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  Page<PsnReqInbox> loadReqInbox(Page<PsnReqInbox> page, Map paramMap) throws ServiceException;

  /**
   * 搜索请求更新收件箱.
   * 
   * @param page
   * @param searchKey
   * @return
   * @throws ServiceException
   */
  Page<PsnReqInbox> loadReqInbox(Page<PsnReqInbox> page, String searchKey) throws ServiceException;

  /**
   * 加载推荐收件箱.
   * 
   * @param page
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  Page<PsnShareInbox> loadShareInbox(Page<PsnShareInbox> page, Map paramMap) throws ServiceException;

  /**
   * 搜索推荐收件箱.
   * 
   * @param page
   * @param searchKey
   * @return
   * @throws ServiceException
   */

  Page<PsnShareInbox> loadShareInbox(Page<PsnShareInbox> page, String searchKey) throws ServiceException;

  /**
   * 加载站内邀请收件箱.
   * 
   * @param page
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  Page<PsnInviteInbox> loadInviteInbox(Page<PsnInviteInbox> page, Map paramMap) throws ServiceException;

  /**
   * 搜索站内邀请收件箱.
   * 
   * @param page
   * @param searchKey
   * @return
   * @throws ServiceException
   */
  Page<PsnInviteInbox> loadInviteInbox(Page<PsnInviteInbox> page, String searchKey) throws ServiceException;

  /**
   * 标记邮件未读状态.
   * 
   * @param ids
   * @param type
   * @throws ServiceException
   */
  void setUnReadStatus(String ids, String type) throws ServiceException;

  /**
   * 
   * @param ids
   * @param type
   * @throws ServiceException
   */
  void deleteInboxMailById(String ids, String type) throws ServiceException;

  void deleteInboxMailByIdx(String ids, String type, String rid, String rType) throws ServiceException;

  /**
   * 更改邮件状态.
   * 
   * @param id
   * @param status
   * @param type
   * @throws ServiceException
   */
  void updateInboxStatus(Long id, Integer status, String type) throws ServiceException;

  /**
   * 获取上一条 下一条 链接状态.
   * 
   * @param msg
   * @return
   * @throws ServiceException
   */
  String getLinkInfo(Message msg) throws ServiceException;

  /**
   * 处理好友请求操作.
   * 
   * @param message
   * @return
   * @throws ServiceException
   */
  String disposeFrdInvite(Message message) throws ServiceException;

  /**
   * 处理群组请求操作.
   * 
   * @param message
   * @return
   * @throws ServiceException
   */
  String disposeGroupInvite(Message message) throws ServiceException;

  /**
   * 处理请求加入群组的操作.
   * 
   * @param message
   * @return
   * @throws ServiceException
   */
  String disposeApplyJoinGroup(Message message) throws ServiceException;

  /**
   * 批量处理请求加入群组的操作.
   * 
   * 
   * @param message []
   * @return
   * @throws ServiceException
   * 
   */
  String batchDisposeApplyJoinGroup(Message[] message, String type) throws ServiceException;

  /**
   * 批量处理群组请求操作.
   * 
   * @param message []
   * @return
   * @throws ServiceException
   * 
   * 
   */
  String batchDisposeGroupInvite(Message[] message, String type) throws ServiceException;

  /**
   * 批量处理好友请求操作.
   * 
   * @param message []
   * @return
   * @throws ServiceException
   */
  String batchDisposeFrdInvite(Message[] message, String type) throws ServiceException;

  /**
   * 批量处理操作的控制分发.
   * 
   * @param String []..
   * @return
   * @throws ServiceException
   */
  String batchControl(String[] ids, String[] groupNodeIds, String[] inviteIds, String[] groupIds, String[] inviteTypes,
      String type, String casUrl) throws ServiceException;

  /**
   * 加载邀请邮件详细信息.
   * 
   * @param msg
   * @return
   * @throws ServiceException
   */
  PsnInviteMailBox getInviteMailDetailById(Message msg) throws ServiceException;

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
   * 根据收件箱ID检索记录.
   * 
   * @param inboxId
   * @return
   * @throws ServiceException
   */
  InviteInbox getInviteInboxById(Long inboxId) throws ServiceException;

  /**
   * 获取站内信息内容.
   * 
   * @param inboxId
   * @return
   */
  InsideInboxCon getInsideInboxCon(Long inboxId);

  /**
   * 获取站内邀请内容.
   * 
   * @param inboxId
   * @return
   */
  InviteBoxCon getInviteInboxCon(Long inboxId);

  /**
   * 保存收件箱数据.
   * 
   * @param inbox
   * @throws ServiceException
   */
  void saveInviteInbox(InviteInbox inbox) throws ServiceException;

  void syncPersonInfo(Person person) throws ServiceException;

  void setReadStatus(String ids, String type) throws ServiceException;

  void setMailStatus(String ids, String type, String statusType) throws ServiceException;

  /**
   * 判断是否已经有了同样的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；
   * @return
   */
  boolean isRepeatInvite(Long recId, Long senderId, String type) throws ServiceException;

  /**
   * 判断是否已经有了同样的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；
   * @return
   */

  boolean isRepeatInvite(Long recId, Long senderId, String type, Long groupId) throws ServiceException;

  /**
   * 根据接收者Id和被邀请加入组的组名，更改收信箱的信息状态为已读.
   * 
   * @param psnId 个人Id
   * @param groupName 组名
   */
  void updateInvitedGroupInboxStatus(Long recId, String groupName);

  /**
   * 忽略重复的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；
   */
  void ignoreRepeatInvite(Long recId, Long senderId, String type) throws ServiceException;

  /**
   * 忽略重复的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 2.群组邀请
   */
  void ignoreRepeatInvite(Long recId, Long senderId, String type, Long groupId) throws ServiceException;

  /**
   * 根据操作结果设置InviteInbox中的opt_status.
   * 
   * @param rtn
   * 
   * @param actionType
   * 
   * @param recvId
   * 
   * 
   * @throws ServiceException
   */
  void setStatusOnInviteInbox(String rtn, String actionType, Long inviteId) throws ServiceException;

  /**
   * 根据RecordRelation的实体类设置InviteInbox中的opt_status.
   * 
   * @param rtn
   * 
   * @param actionType
   * 
   * @param rel
   * 
   * @throws ServiceException
   */

  void setStatusOnInviteInbox(String rtn, String actionType, RecordRelation rel) throws ServiceException;

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
   * 根据当前用户的des3MailId,des3InboxId,nodeIDdes和des3InviteId 初始化Invite_inbox FriendTemp
   * 
   * @param des3MailId ,des3InboxId,nodeIDdes,des3InviteId
   * 
   * @return 0-成功；1-邀请已被其他用户绑定.
   * 
   * @throws ServiceException
   */

  Integer initFrdInviteInbox(String des3MailId, String des3InboxId, Integer nodeId, String des3InviteId)
      throws ServiceException;

  /**
   * 同步Invite_mailbox invite_inbox
   * 
   * @param paramMap
   * 
   * @throws ServiceException
   */
  InviteMailBox syncInitFrdInviteInbox(Map<String, String> paramMap) throws ServiceException;

  /**
   * 同步不同节点的邀请发件箱和收件箱的信息.
   * 
   * @param paramMap
   * @return
   * @throws ServiceException
   */
  public InviteMailBox syncInitGrpInviteInbox(InviteMailBox mailBox, Map<String, String> paramMap)
      throws ServiceException;

  /**
   * 去除重复的好友邀请
   * 
   * @param sendPsnId
   * @param senderPsnId
   * @throws ServiceException
   */
  void removalRepeatInvite(Long sendPsnId, Long senderPsnId) throws ServiceException;

  String getInviteInboxJsonStrById(Long[] ids) throws ServiceException;

  /**
   * 根据查看邮件改变收件箱状态.
   * 
   * @param senderId
   * @param receiverId
   * @param des3Sid
   * @throws ServiceException
   */
  void updateEmailShareStatus(Long senderId, Long receiverId, String des3Sid) throws ServiceException;

  /**
   * 拼装保存收件箱记录.
   * 
   * @param param
   * @param recvPerson
   * @return
   * @throws ServiceException
   */
  InviteInbox saveInviteInbox(Map<String, Object> param, Person recvPerson) throws ServiceException;

  /**
   * 保存站内邀请的收件箱相关记录.
   * 
   * @param inviteInParam 收件箱表所需参数.
   * @param recvPerson 收件人.
   * @param dataParam 其他相关记录所需参数.
   * @throws ServiceException
   */
  void saveInviteRelationRecord(Map<String, Object> inviteInParam, Person recvPerson, Map<String, Object> dataParam)
      throws ServiceException;

  /**
   * 查询某个人的某种邀请信息
   * 
   * @param psnId
   * @param inviteType
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public List<PsnInviteInbox> findInviteReg(Long psnId, Integer inviteType, int maxSize) throws ServiceException;

  /**
   * 保存站内消息收件箱记录.
   * 
   * @param recieverId
   * @param insideMailBox
   * @throws ServiceException
   */
  void saveInsideInbox(Long recieverId, InsideMailBox insideMailBox) throws ServiceException;

  /**
   * 保存站内消息收件箱记录_MJG_SCM-5910。
   * 
   * @param insideInbox
   */
  void saveInsideInboxCon(InsideInbox insideInbox);

  /**
   * 获取站内信收件箱记录.
   * 
   * @param page
   * @param paramMap
   * @return
   * @throws ServiceException
   */
  Page<InsideInbox> getInsideInboxByPage(Page<InsideInbox> page, Map<String, String> paramMap) throws ServiceException;

  /**
   * 获取站内信详情.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  String getInsideInboxDetail(Long id) throws ServiceException;

  /**
   * 获取站内信内容.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  String getInsideInboxContent(Long id) throws ServiceException;

  /**
   * 清除站内消息收件箱内容记录_MJG_SCM-6097.
   * 
   * @param id
   */
  void cleanInsideInboxCon(InsideInbox inbox);

}
