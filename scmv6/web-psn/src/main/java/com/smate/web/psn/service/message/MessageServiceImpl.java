package com.smate.web.psn.service.message;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.dao.friend.FriendTempDao;
import com.smate.web.psn.dao.friend.InviteMailBoxDao;
import com.smate.web.psn.dao.inbox.InviteInboxDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.InviteInbox.InviteInbox;
import com.smate.web.psn.model.friend.FriendTemp;
import com.smate.web.psn.model.mailbox.InviteMailBox;
import com.smate.web.psn.model.message.Message;
import com.smate.web.psn.service.profile.PersonManager;

/**
 * 消息服务类.
 * 
 * @author oyh
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("messageService")
public class MessageServiceImpl implements MessageService {

  // private static final String ENCODING = "utf-8";
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonManager personManager;
  @Autowired
  private InviteMailBoxDao inviteMailBoxDao;
  @Autowired
  private InviteInboxDao inviteInboxDao;
  @Autowired
  private FriendTempDao friendTempDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public Map<String, Long> sendInviteMessage(Message message) throws ServiceException {
    // map.put("mailId", Long.valueOf(message.getMailId()));
    // map.put("inboxId", Long.valueOf(message.getRecvId()));
    Long curentUserId = message.getSendPsnId();
    if (curentUserId == null) {
      curentUserId = SecurityUtils.getCurrentUserId();
      message.setSendPsnId(curentUserId);
    }
    // 获取当前登录用户信息.
    Person person = personManager.getPsnNameAndAvatars(curentUserId);
    // 封装保存发件箱
    // InviteMailBox inviteMailBox = this.saveInviteMailBox(person, message);
    // =================================MailId======================================
    /*
     * if (inviteMailBox != null) { message.setMailId(inviteMailBox.getMailId().toString());//
     * 收件人psnId为空,在邮件地址中要保存发件箱的mailId }
     */
    if (message.getInviteId() != null && message.getInviteId() > 0l) {
      FriendTemp frdTmp = friendTempDao.get(message.getInviteId());
      if (frdTmp != null) {
        // frdTmp.setSendReqId(inviteMailBox.getMailId());// 保存收件箱Id的引用
        friendTempDao.save(frdTmp);
      }
    }
    Long inviteInId = 0L;
    String psnIds = message.getReceivers();
    // 修改后逻辑:如果收件人ID为空，则获取其收件邮箱地址保存收件信息.//原逻辑： 如果为空，则表示站外邀请只保存发件箱
    if (StringUtils.isEmpty(psnIds)) {
      // inviteInId = this.dealInviteMessageByEmail(message, inviteMailBox);
      // inviteMailBox所对应的表已弃用，以下方法内已注释inviteMailBox对象的调用
      inviteInId = this.dealInviteMessageByEmail(message, null);
    }
    /*
     * Map<String, Long> result = new HashMap<String, Long>(); if (inviteMailBox != null) {
     * result.put("mailId", inviteMailBox.getMailId()); } result.put("inviteInId", inviteInId); return
     * result;
     */
    return null;
  }

  private Long dealInviteMessageByEmail(Message message, InviteMailBox inviteMailBox) {
    FriendTemp frdTmp = friendTempDao.get(message.getInviteId());
    String email = "";// 接收邀请的邮件地址.
    if (frdTmp != null) {
      email = frdTmp.getSendMail();
    }
    // 获取收件箱记录所需参数.
    String psnId = "-1";
    Integer optStatus = 0;
    Integer status = 0;
    // 初始化一个空的人员记录.
    Person recvPerson = new Person();
    recvPerson.setName(email);// 接收邀请的人员的名称设置为邮件地址.
    recvPerson.setFirstName(email);// 接收邀请的人员的英文名称设置为邮件地址.
    String avatars = domainscm + ServiceConstants.DEFAULT_MAN_AVATARS;
    recvPerson.setAvatars(avatars);
    // 保存收件箱记录.
    /*
     * InviteInbox inviteInbox = this.saveInviteInbox(psnId, optStatus, status, recvPerson,
     * inviteMailBox.getMailId()); //
     * =================================RecvId====================================== if (inviteInbox !=
     * null) { message.setRecvId(inviteInbox.getId().toString()); return inviteInbox.getId(); }
     */
    return null;
  }

  private InviteInbox saveInviteInbox(String psnId, Integer optStatus, Integer status, Person recvPerson, Long mailId) {
    InviteInbox inviteInbox = new InviteInbox(Long.parseLong(psnId), mailId, status, optStatus);
    inviteInbox.setFirstName(recvPerson.getFirstName());
    inviteInbox.setLastName(recvPerson.getLastName());
    inviteInbox.setPsnName(recvPerson.getName());
    inviteInbox.setPsnHeadUrl(recvPerson.getAvatars());
    inviteInboxDao.save(inviteInbox);
    return inviteInbox;
  }

  private InviteMailBox saveInviteMailBox(Person person, Message message) throws ServiceException {
    InviteMailBox inviteMailBox = new InviteMailBox();
    inviteMailBox.setSenderId(message.getSendPsnId());
    inviteMailBox.setOptDate(new Date());
    inviteMailBox.setStatus(0);
    inviteMailBox.setPsnName(person.getName());
    inviteMailBox.setFirstName(person.getFirstName());
    inviteMailBox.setLastName(person.getLastName());
    inviteMailBox.setPsnHeadUrl(person.getAvatars());
    inviteMailBox.setInviteType(message.getInviteType() == null ? 0 : message.getInviteType());
    Long receiverId = message.getInvitePsnId();
    inviteMailBox.setInvitePsnId(receiverId);
    // inviteMailBox.setSenderInfo(jsonSerder.toString());
    inviteMailBoxDao.save(inviteMailBox);
    return inviteMailBox;
  }

}
