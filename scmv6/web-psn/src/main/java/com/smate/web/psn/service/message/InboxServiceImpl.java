package com.smate.web.psn.service.message;

import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.friend.FriendTempDao;
import com.smate.web.psn.dao.friend.InviteMailBoxDao;
import com.smate.web.psn.dao.inbox.InviteInboxDao;
import com.smate.web.psn.dao.inbox.MsgNoticeInBoxDao;
import com.smate.web.psn.dao.inbox.ReqInboxDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.InviteInbox.InviteInbox;
import com.smate.web.psn.model.friend.FriendTemp;
import com.smate.web.psn.model.mailbox.InviteMailBox;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;
import com.smate.web.psn.service.friend.FriendMailService;
import com.smate.web.psn.service.profile.PersonEmailManager;
import com.smate.web.psn.service.profile.PersonManager;

/**
 * 收件箱.
 * 
 * @author zx
 * 
 */
@Service("inboxService")
@Transactional(rollbackOn = Exception.class)
public class InboxServiceImpl implements InboxService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InviteInboxDao inviteInboxDao;
  @Autowired
  private InviteMailBoxDao inviteMailBoxDao;
  @Autowired
  private FriendTempDao friendTempDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PersonEmailManager personEmailManager;
  @Autowired
  private ReqInboxDao reqInboxDao;
  @Autowired
  private MsgNoticeInBoxDao msgNoticeInboxDao;

  /**
   * 判断是否已经有了相同的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；2.群组邀请 3.请求加入群组
   * @return
   */
  @Override
  public boolean isRepeatInvite(Long recId, Long senderId, String type) {
    return inviteInboxDao.isRepeatInvite(recId, senderId, type);
  }

  /**
   * 忽略重复的邀请.
   * 
   * @param recId 接收者Id
   * @param senderId 发送者Id
   * @param type 1.好友邀请；
   */
  @Override
  public void ignoreRepeatInvite(Long recId, Long senderId, String type) throws ServiceException {
    inviteInboxDao.ignoreRepeatInvite(recId, senderId, type);
  }

  /**
   * 根据请求参数对当前邀请进行处理.
   * 
   * @param paramMap
   * @return 0-成功；1-邀请已被其他用户绑定；2-群组已被删除.
   * @throws ServiceException
   */
  @Override
  public Integer dealInviteBusiness(String key, Map<String, String> paramMap) throws ServiceException {
    Integer result = 0;
    // 获取请求参数.
    Long currentPsnId = SecurityUtils.getCurrentUserId();// 当前用户ID.
    String des3InviteId = null;
    String des3InboxId = null;
    String des3MailId = null;
    String verifyEmail = null;
    if (FriendMailService.FRIEND_INVITE_KEY.equals(key)) {// 好友邀请.
      des3InviteId = paramMap.get(FriendMailService.INVITATION_PARAM_INVITEID);
      des3InboxId = paramMap.get(FriendMailService.INVITATION_PARAM_INBOXID);
      des3MailId = paramMap.get(FriendMailService.INVITATION_PARAM_MAILID);
    }
    // 处理邀请.
    Person person = personManager.getPerson(currentPsnId);// 当前登录用户.
    result = this.dealSameNodeInvite(des3MailId, des3InboxId, des3InviteId, person, 1);
    if (0 == result) {// 如果邀请处理成功且当前用户的邮件地址与邀请的邮件地址相同，则将其置为已确认_2013-05-28_SCM-2545.
      if (person.getEmail().equals(verifyEmail)) {
        // 更新邮件地址的确认状态.
        personEmailManager.updateLoginEmailVerifyStatus(person.getPersonId(), person.getEmail());
      }
    }
    return result;
  }

  /**
   * 处理相同节点的邀请.
   * 
   * @param des3MailId
   * @param des3InboxId
   * @param des3InviteId
   * @param person
   * @param currentNodeId
   * @return
   * @throws ServiceException
   */
  private Integer dealSameNodeInvite(String des3MailId, String des3InboxId, String des3InviteId, Person person,
      Integer currentNodeId) throws ServiceException {
    Long mailId = new Long(ServiceUtil.decodeFromDes3(des3MailId));// 邀请发件箱的主键ID.
    Long inboxId = new Long(ServiceUtil.decodeFromDes3(des3InboxId));// 邀请收件箱的主键ID.
    Long inviteId = new Long(ServiceUtil.decodeFromDes3(des3InviteId));// 邀请记录的主键ID.
    Long currentPsnId = person.getPersonId();
    InviteMailBox mailBox = inviteMailBoxDao.get(mailId);
    InviteInbox inbox = null;
    if (inboxId != null && inboxId != 0L) {
      inbox = this.getInviteInboxById(inboxId);
    } else {// 如果参数收件箱ID为空，则根据发件箱ID查询对应的收件箱记录<psnId的值对应MessageServiceImpl.java类中的dealInviteMessageByEmail方法>.
      inbox = this.getInviteInbox(-1L, mailId);
    }
    if (inbox != null) {
      // 如果邀请信息已被绑定且不是当前用户，则返回1，给出提示“邀请链接已经失效了或已被其他用户使用！”.
      if (inbox.getPsnId() != null && inbox.getPsnId() >= 0
          && !String.valueOf(inbox.getPsnId()).equals(String.valueOf(currentPsnId))) {
        return 1;
      }
    } else {// 如果没有未绑定的收件箱记录，则同样返回1，给出提示“邀请链接已经失效了或已被其他用户使用！”_2012-12-26_SCM-1415.
      return 1;
    }
    // 邀请类型为0-好友请求.
    if (mailBox.getInviteType() != null && mailBox.getInviteType() == 0) {
      // 忽略重复之前的好友邀请
      this.removalRepeatInvite(currentPsnId, mailBox.getSenderId());
      FriendTemp friendTemp = this.friendTempDao.get(inviteId);
      if (friendTemp != null) {
        friendTemp.setTempPsnId(currentPsnId);
        friendTempDao.save(friendTemp);
      }
    }
    // 更新收件箱记录(保存收件人).
    if (inbox != null && inbox.getPsnId() <= 0) {
      inbox.setPsnId(currentPsnId);
      inbox.setPsnName(person.getName());
      inbox.setFirstName(person.getFirstName());
      inbox.setLastName(person.getLastName());
      inviteInboxDao.save(inbox);
    }
    return 0;
  }

  /**
   * 根据收件箱ID检索记录.
   * 
   * @param inboxId
   * @return
   * @throws ServiceException
   */
  @Override
  public InviteInbox getInviteInboxById(Long inboxId) throws ServiceException {
    try {
      return inviteInboxDao.get(inboxId);
    } catch (Exception e) {
      logger.error("查询收件箱数据失败！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public InviteInbox getInviteInbox(Long psnId, Long mailId) throws ServiceException {

    try {
      return inviteInboxDao.getInviteInbox(psnId, mailId);
    } catch (Exception e) {
      logger.error("查询收件箱数据失败！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removalRepeatInvite(Long sendPsnId, Long senderPsnId) throws ServiceException {

    boolean isRepeat = this.isRepeatInvite(sendPsnId, senderPsnId, "friend");
    if (isRepeat) {// 如果重复则忽略之前的好友请求
      this.ignoreRepeatInvite(sendPsnId, senderPsnId, "friend");
    }
  }


}
