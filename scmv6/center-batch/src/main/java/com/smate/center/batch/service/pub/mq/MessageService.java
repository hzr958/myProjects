package com.smate.center.batch.service.pub.mq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InsideInbox;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.model.mail.MessageNoticeOutBox;
import com.smate.center.batch.model.mail.PsnInsideMailBox;
import com.smate.center.batch.model.mail.ReqMailBox;
import com.smate.center.batch.model.mail.ShareMailBox;
import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.model.sns.pub.PsnResSend;
import com.smate.core.base.utils.model.security.Person;


/**
 * 消息服务类
 * 
 * @author oyh
 * 
 */
public interface MessageService {

  /**
   * 系统发送站内短信.
   * 
   * @param message
   * @throws ServiceException
   */
  void sendInsideMessageForSys(Message message) throws ServiceException;

  /**
   * 同步站内通知.
   * 
   * @author yangpeihai
   * @param insideMailbox
   * @throws ServiceException
   */
  void saveSynNoticeMessage(MessageNoticeOutBox notice) throws ServiceException;

  /**
   * 发送短信.
   * 
   * @param message
   * @throws ServiceException
   */
  void sendMessage(Message message) throws ServiceException;

  /**
   * 发送站内邀请 只需传入Content内容、receivers 以逗号分隔、inviteType.
   * 
   * @param message
   * @throws ServiceException
   */
  Map<String, Long> sendInviteMessage(Message message) throws ServiceException;

  /**
   * 同步站内好友邀请
   * 
   * @param inviteMailBox
   * @throws ServiceException
   */
  void saveSyncInviteMessage(InviteMailBox inviteMailBox) throws ServiceException;

  /**
   * 同步请求更新消息.
   * 
   * @param reqMailBox
   * @throws ServiceException
   */

  void saveSyncReqMessage(ReqMailBox reqMailBox) throws ServiceException;

  /**
   * 同步推荐消息.
   * 
   * @param reqMailBox
   * @throws ServiceException
   */
  void saveSyncShareMessage(ShareMailBox shareMailBox) throws ServiceException;

  /**
   * 发送邀请加入群组邮件.
   * 
   * @param ctxMap {inviteId,nodeId}
   * @throws ServiceException
   */
  void sendInviteAttendGroupMail(Map<String, ?> ctxMap, Long mailType) throws ServiceException;

  /**
   * 邀请加为好友，非站内用户注册成功后，发送邀请请求短消息(默认把注册的好友放在同一库里面).
   * 
   * @param inviteId ,psnId
   * @throws ServiceException
   */
  void saveFrdReqInbox(Long inviteId, Long psnId) throws ServiceException;

  /**
   * 邀请加为群组成员，非站内用户注册成功后，发送邀请请求短消息(默认把注册的人员放在同一库里面).
   * 
   * @param inviteId
   * 
   * @throws ServiceException
   */
  void saveGroupReqInbox(Long inviteId) throws ServiceException;

  /**
   * 好友:邀请用户，直接登录用户，发送短消息.
   * 
   * @param inviteId
   * @param psnId
   * @throws ServiceException
   */
  void saveFrdInviteForLogin(Long inviteId, Long psnId) throws ServiceException;

  /**
   * 群组:邀请用户，直接登录用户，发送短消息.
   * 
   * @param inviteId
   * @param nodeId
   * @throws ServiceException
   */
  void saveGroupInviteForLogin(Long inviteId, Integer nodeId) throws ServiceException;

  /**
   * 邀请好友成功后，发送站内短消息通知.
   * 
   * @param map {psnId}
   * @throws ServiceException
   */
  void sendConfirmMsgForAddFrd(HashMap<String, ?> map) throws ServiceException;

  /**
   * 发送站内请更新应用.
   * 
   * @param ctxMap {viewUrl,total,receivers}
   * @throws ServiceException
   */
  void sendMessageForReq(HashMap<String, String> ctxMap) throws ServiceException;

  /**
   * 发送请求更新引用次数邮件.
   * 
   * @param ctxMap
   * @throws ServiceException
   */
  void sendMailForReq(HashMap<String, Object> ctxMap, Long mailType) throws ServiceException;

  /**
   * 站内推荐短消息.
   * 
   * @param ctxMap {total,receivers,deadLine,type,sid,rid}
   * @throws ServiceException
   */
  void sendMessageForShare(HashMap<String, String> ctxMap, Long sendPsnId) throws ServiceException;

  void sendMessageForShare2(HashMap<String, String> ctxMap, PsnResSend psnResSend, int type, String email,
      String receiverName, int nodeId) throws ServiceException;

  void saveSendMail(HashMap<String, String> map, PsnResSend psnResSend) throws ServiceException;

  String genEmailViewResUrl(PsnResSend psnResSend, int type, String email, int nodeId) throws ServiceException;

  /**
   * 站内推荐短消息:非当前节点发送消息.
   * 
   * @param ctxMap {total,receivers,deadLine,type,sid,rid,sendPsnId}
   * @throws ServiceException
   */
  void sendOffLineMessageForShare(HashMap<String, String> ctxMap) throws ServiceException;

  /**
   * 发送共享邮件.
   * 
   * @param ctxMap
   * @throws ServiceException
   */
  void sendMailForShare(Map<String, Object> ctxMap, Long mailType) throws ServiceException;

  /**
   * 处理更新请求后，发送消息和邮件.
   * 
   * @param message
   * @throws ServiceException
   */
  void sendConfirmForReq(Message message) throws ServiceException;

  /**
   * 回复消息.
   * 
   * @param message
   * @param mailType 邮件类型
   * @throws ServiceException
   */
  String replyForMail(Message message, Long mailType) throws ServiceException;

  /**
   * 转发邮件.
   * 
   * @param message
   * @return
   * @throws Exception
   */
  PsnInsideMailBox loadForwardMessage(Message message) throws Exception;

  /**
   * 检查用户是否拒收短消息.
   * 
   * @param sender
   * @param psnId
   * @return
   * @throws ServiceException
   */

  boolean isUserRefuseMsg(Long sender, Long psnId) throws ServiceException;

  /**
   * 查询用户权限，是否同步消息.
   * 
   * @param sender
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  boolean cancelSyncMessage(Long sender, String psnIds) throws ServiceException;

  /**
   * 首页新消息统计.
   * 
   * @return
   * @throws ServiceException
   */

  String getInboxMsgTotalForNew(Long psnId) throws ServiceException;

  /**
   * 消息提示.
   * 
   * @return
   * @throws ServiceException
   */
  Map<String, Integer> getInboxForTip() throws ServiceException;

  /**
   * 收件箱信息统计.
   * 
   * @param type
   * @return
   * @throws ServiceException
   */
  Long getInboxMsgTotal(String type) throws ServiceException;

  /**
   * 发起邀请.
   * 
   * @param person
   * @throws ServiceException
   */
  void genInviteAction(PersonRegister person) throws ServiceException;

  /**
   * 同步一份推荐邮件.
   * 
   * @param shareMail
   * @throws ServiceException
   */

  void syncShareMail(ShareMailBox shareMail) throws ServiceException;

  /**
   * 邀请加入群组成功后系统通知被邀请人.
   * 
   * @param ctxMap
   * @throws ServiceException
   */
  void sendConfirmMsgForAttendGroup(HashMap<String, ?> ctxMap) throws ServiceException;

  /**
   * 发送通过邀请加入群组的邮件通知.
   * 
   * @param ctxMap
   * @throws ServiceException
   */
  void sendConfrimMailForAttendGroup(HashMap<String, ?> ctxMap) throws ServiceException;

  /**
   * 群组邀请加入群组申请(管理员批准通过后邮件通知).
   * 
   * @param ctxMap
   * @throws ServiceException
   */
  void sendApproveForAttendGroup(HashMap<String, ?> ctxMap) throws ServiceException;

  /**
   * 获取收信箱统计数.
   * 
   * <pre>
   * ids:消息的Id集合，用逗号隔开
   * type:
   * 短信inside_inbox
   * 邀请invite_inbox
   * 请求req_inbox
   * 推荐share_inbox
   * 通知msg_notice_in_box
   * </pre>
   * 
   * @return
   * @throws ServiceException
   */
  Map<String, Long> getMessageCount(String ids, Long psnId) throws ServiceException;

  /**
   * 更新收信箱统计数.
   * 
   * <pre>
   * type:
   * 短信inside_inbox
   * 邀请invite_inbox
   * 请求req_inbox
   * 推荐share_inbox
   * 通知msg_notice_in_box
   * </pre>
   * 
   * @param psnId
   * @param type
   * @throws ServiceException
   */
  Long refreshMessageCount(Long psnId, String type) throws ServiceException;

  /**
   * 获取具体收信箱的统计数.
   * 
   * @param type
   * @return
   * @throws ServiceException
   */
  Long getMessageCount(Long psnId, String type) throws ServiceException;

  /**
   * 发送邮件邀请群组成员共享成果/文件
   * 
   * @param map
   * @throws ServiceException
   */
  void sendMailForSharePub(Map<String, Object> map) throws ServiceException;

  InsideInbox findInsideRecvMsg(Long mailId, Long psnId) throws ServiceException;

  /**
   * 删除群组后给群组成员的邮件通知.
   * 
   * @param message 信息参数实体(需包含信息：receiver,content)
   * @param sender 发出邮件的人员参数实体.
   * @param groupMemberPnsIdList 接收邮件的群组成员ID列表.
   * @throws ServiceException
   */
  public void sendGroupDeleteMail(Message message, Person sender, List<Long> groupMemberPnsIdList)
      throws ServiceException;

  /**
   * 发送申请同意邮件--被邀请者
   */
  public void sendArrovalToMem(Map<String, Object> ctxMap) throws ServiceException;

  /**
   * 发送申请同意邮件--管理员和创建人
   */
  public void sendArrovalToMan(Map<String, Object> ctxMap) throws ServiceException;

  void sendMail(String sendTo, String subject, String ftlTemplate, HashMap<String, Object> context, Long psnId)
      throws ServiceException;
}
