package com.smate.center.batch.service.pub.mq;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acegisecurity.util.EncryptionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import com.smate.center.batch.constant.MessageConstant;
import com.smate.center.batch.constant.PrivacyType;
import com.smate.center.batch.dao.mail.FullTextInboxDao;
import com.smate.center.batch.dao.mail.InsideInboxDao;
import com.smate.center.batch.dao.mail.InsideMailBoxConDao;
import com.smate.center.batch.dao.mail.InsideMailBoxDao;
import com.smate.center.batch.dao.mail.InviteInboxDao;
import com.smate.center.batch.dao.mail.InviteMailBoxConDao;
import com.smate.center.batch.dao.mail.InviteMailBoxDao;
import com.smate.center.batch.dao.mail.MsgNoticeInBoxDao;
import com.smate.center.batch.dao.mail.MsgNoticeOutBoxDao;
import com.smate.center.batch.dao.mail.PsnInsideInboxDao;
import com.smate.center.batch.dao.mail.PsnInsideMailBoxDao;
import com.smate.center.batch.dao.mail.PsnInviteInboxDao;
import com.smate.center.batch.dao.mail.PsnMsgNoticeInBoxDao;
import com.smate.center.batch.dao.mail.PsnShareInboxDao;
import com.smate.center.batch.dao.mail.ReqInboxDao;
import com.smate.center.batch.dao.mail.ReqMailBoxDao;
import com.smate.center.batch.dao.mail.ShareInboxDao;
import com.smate.center.batch.dao.mail.ShareMailBoxDao;
import com.smate.center.batch.dao.mail.SyncInsideMailBoxDao;
import com.smate.center.batch.dao.mail.SyncInviteMailBoxDao;
import com.smate.center.batch.dao.mail.SyncReqMailBoxDao;
import com.smate.center.batch.dao.mail.SyncShareMailBoxDao;
import com.smate.center.batch.dao.sns.friend.GroupInviteDao;
import com.smate.center.batch.dao.sns.prj.FriendDao;
import com.smate.center.batch.dao.sns.psn.FriendTempDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnDao;
import com.smate.center.batch.dao.sns.pub.PrivacySettingsDao;
import com.smate.center.batch.dao.sns.pub.RecordRelationDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InsideInbox;
import com.smate.center.batch.model.mail.InsideMailBoxCon;
import com.smate.center.batch.model.mail.InviteInbox;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.model.mail.InviteMailBoxCon;
import com.smate.center.batch.model.mail.MessageNoticeInBox;
import com.smate.center.batch.model.mail.MessageNoticeOutBox;
import com.smate.center.batch.model.mail.PsnInsideInbox;
import com.smate.center.batch.model.mail.PsnInsideMailBox;
import com.smate.center.batch.model.mail.ReqInbox;
import com.smate.center.batch.model.mail.ReqMailBox;
import com.smate.center.batch.model.mail.ShareInbox;
import com.smate.center.batch.model.mail.ShareMailBox;
import com.smate.center.batch.model.mail.SyncInviteMailBox;
import com.smate.center.batch.model.mail.SyncReqMailBox;
import com.smate.center.batch.model.mail.SyncShareMailBox;
import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.model.sns.pub.FriendTemp;
import com.smate.center.batch.model.sns.pub.GroupInvite;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.model.sns.pub.PrivacySettings;
import com.smate.center.batch.model.sns.pub.PsnResSend;
import com.smate.center.batch.service.dynamic.DynamicShareService;
import com.smate.center.batch.service.emailsimplify.EmailSimplify;
import com.smate.center.batch.service.mail.EmailCommonService;
import com.smate.center.batch.service.mail.InboxService;
import com.smate.center.batch.service.mail.InvitationBusinessMethod;
import com.smate.center.batch.service.mail.MailBoxService;
import com.smate.center.batch.service.mail.PersonEmailManager;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.psn.PsnMailSetService;
import com.smate.center.batch.service.psn.PsnStatisticsService;
import com.smate.center.batch.service.psn.SyncPersonService;
import com.smate.center.batch.service.pub.FriendService;
import com.smate.center.batch.service.pub.GroupPsnSearchService;
import com.smate.center.batch.service.pub.GroupService;
import com.smate.center.batch.service.pub.PubFulltextPsnRcmdService;
import com.smate.center.batch.service.pub.archiveFiles.ArchiveFilesService;
import com.smate.center.batch.service.user.UserService;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import net.sf.json.JSONObject;

/**
 * 消息服务类.
 * 
 * @author oyh
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("messageService")
public class MessageServiceImpl implements MessageService {

  private static final String ENCODING = "utf-8";
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ArchiveFilesService archiveFilesService;
  @Autowired
  private InsideMailBoxDao insideMailBoxDao;
  @Autowired
  private InsideMailBoxConDao insideMailBoxConDao;
  @Autowired
  private SyncInsideMailBoxDao syncInsideMailBoxDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private InsideInboxDao insideInboxDao;
  @Autowired
  private InviteMailBoxDao inviteMailBoxDao;
  @Autowired
  private InviteMailBoxConDao inviteMailBoxConDao;
  @Autowired
  private ReqInboxDao reqInboxDao;
  @Autowired
  private SyncInviteMailBoxDao syncInviteMailBoxDao;
  @Autowired
  private SyncPersonService syncPersonService;
  @Autowired
  private SnsSyncMessageProducer snsSyncMessageProducer;
  @Autowired
  private FriendTempDao friendTempDao;
  @Autowired
  private Configuration msgFreemarkereConfiguration;
  @Autowired
  private Configuration freemarkerConfiguration;
  @Autowired
  private ReqMailBoxDao reqMailBoxDao;
  @Autowired
  private InviteInboxDao inviteInboxDao;
  @Autowired
  private ShareMailBoxDao shareMailBoxDao;
  @Autowired
  private ShareInboxDao shareInboxDao;

  @Autowired
  private SyncShareMailBoxDao syncShareMailBoxDao;
  @Autowired
  private SyncReqMailBoxDao syncReqMailBoxDao;
  @Autowired
  private PsnInsideInboxDao psnInsideInboxDao;
  @Autowired
  private PsnInsideMailBoxDao psnInsideMailBoxDao;
  @Autowired
  private InsideMailBoxConDao insideMailboxConDao;
  @Autowired
  private PsnInviteInboxDao psnInviteInboxDao;
  @Autowired
  private PsnShareInboxDao psnShareInboxDao;
  @Autowired
  private PsnMsgNoticeInBoxDao psnMsgNoticeInBoxDao;
  @Autowired
  private PrivacySettingsDao privacySettingsDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private GroupPsnDao groupPsnDao;
  @Autowired
  private GroupInviteDao groupInviteDao;
  @Autowired
  private FriendService friendService;
  @Autowired
  private GroupService groupService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private MsgNoticeInBoxDao msgNoticeInBoxDao;// 站内通知收信息
  @Autowired
  private MsgNoticeOutBoxDao msgNoticeOutBoxDao;// 站内通知发信息
  @Autowired
  private PsnMailSetService psnMailSetService;
  @Autowired
  private RecordRelationDao recordRelationDao;
  @Autowired
  private InboxService inboxService;
  @Autowired
  private DynamicShareService dynamicShareService;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private FullTextInboxDao fullTextInboxDao;
  @Autowired
  private InvitationBusinessMethod businessMethod;
  @Autowired
  private PubFulltextPsnRcmdService pubFulltextPsnRcmdService;
  @Autowired
  private PersonEmailManager personEmailManager;
  @Autowired
  private MailBoxService mailBoxService;
  @Autowired
  private EmailCommonService emailCommonService;
  @Autowired
  private EmailSimplify reqFrdUpdateCitedEmailService;
  @Autowired
  private EmailSimplify recommendResEmailService;
  @Autowired
  private EmailSimplify insideReplyEmailService;
  @Autowired
  private EmailSimplify groupInviteEmailService;
  @Autowired
  private EmailSimplify groupDeleteNoticeEmailService;
  @Autowired
  private EmailSimplify groupSharePubEmailService;
  @Autowired
  private EmailSimplify etemplateEmailService;
  @Autowired
  private EmailSimplify groupEmailService;
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private UserService userService;
  @Autowired
  private SysDomainConst sysDomainConst;

  /**
   * 删除群组的邮件通知业务逻辑_MaoJianGuo_SCM-1074_2012-11-02.
   * 
   * @param message 信息参数实体(需包含信息：receiver,content)
   * @param sender 发送者基本信息(当前登录用户).
   * @param groupMemberPnsIdList 群组成员ID列表.
   * @throws ServiceException
   */
  @Override
  public void sendGroupDeleteMail(Message message, Person sender, List<Long> groupMemberPnsIdList)
      throws ServiceException {
    try {
      String reciverZhNameLst = "";// 接收者中文人名列表
      String reciverEnNameLst = "";// 接收者英文人名列表
      String title = message.getTitle();// 站内通知发件箱的通知标题.
      message.setMsgType(7);// 消息类型
      // 7-删除群组(对应信息模版DynMsg_noticeIn_Template).

      // 保存站内通知发件箱记录.
      MessageNoticeOutBox noticeOut = this.saveMessageNoticeOutBox(title, sender, message);
      String psnIds = message.getReceivers();
      if (StringUtils.isEmpty(psnIds)) {
        logger.info("没有收件人！");
        return;
      }
      Map<Integer, String> psnNodes = new HashMap<Integer, String>();
      String groupName = message.getGroupName();
      // 循环向群组成员发送邮件.
      for (Long psnId : groupMemberPnsIdList) {
        // 获取收件箱参数.
        Person recvPerson = this.personManager.getPerson(Long.valueOf(psnId));
        reciverZhNameLst += recvPerson.getName() + ",";
        reciverEnNameLst += recvPerson.getFirstName() + " " + recvPerson.getLastName() + ",";
        // 保存站内通知收件箱记录.
        MessageNoticeInBox noticeIn = this.saveMessageNoticeInBox(noticeOut, recvPerson);
        // 发送站内信邮件.
        groupDeleteNoticeEmailService.syncEmailInfo(groupName, recvPerson, noticeOut, noticeIn);
      }
      // 更新保存站内通知发件箱记录的扩展字段.
      this.updateMsgNoticeOutBoxExt(noticeOut, message, reciverZhNameLst, reciverEnNameLst);
      psnNodes = sortPsnIdsByNodeId(psnIds);
      psnNodes.remove(SecurityUtils.getCurrentAllNodeId().get(0));
      if (psnNodes.size() > 0) {// 判断map结构是否有除本节点以外的人.
        // TODO:同步短消息节点.
        noticeOut.setSyncNodePsn(psnNodes);
        snsSyncMessageProducer.syncMessageNoticeToSns(noticeOut, SecurityUtils.getCurrentAllNodeId().get(0));
      }
    } catch (ServiceException e) {
      logger.error("发送群组通知邮件出错", e);
    }
  }

  /**
   * 判断是否为数字.
   * 
   * @param str
   * @return
   */
  public boolean isNumeric(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
      return false;
    }
    return true;
  }

  /**
   * @修改整理了发送邀请信息的方法：抽离了保存发件箱和收件箱的代码_MaoJianGuo_SCM-1074|SCM-1037_2012-11-02_测试功能时为方便理解，特修改.
   */
  @Override
  public void sendInsideMessageForSys(Message message) throws ServiceException {

    String reciverZhNameLst = "";// 接收者中文人名列表
    String reciverEnNameLst = "";// 接收者英文人名列表
    String psnName = message.getPsnName();
    psnName = psnName == null ? "" : psnName;
    // 往发信箱新增一条消息
    String title = "群组邀请-等待插入";
    Person person = personManager.getPerson(message.getSendPsnId());
    MessageNoticeOutBox noticeOut = this.saveMessageNoticeOutBox(title, person, message);

    String psnIds = message.getReceivers();
    if (StringUtils.isEmpty(psnIds)) {
      logger.info("没有收件人！");
      return;
    }
    Map<Integer, String> psnNodes = sortPsnIdsByNodeId(psnIds);

    // 根据收信人的Id，循环往收信箱新增消息
    for (String psnId : psnIds.split(",")) {
      Person recvPerson = personManager.getPerson(new Long(psnId));
      reciverZhNameLst += recvPerson.getName() + ",";
      reciverEnNameLst += recvPerson.getFirstName() + " " + recvPerson.getLastName() + ",";
      psnId = psnId.trim();
      String personIds = "," + psnNodes.get(SecurityUtils.getCurrentAllNodeId().get(0)) + ",";
      if (personIds.indexOf("," + psnId + ",") == -1) { // 非本节点收件人
        continue;
      }
      // 保存站内通知收件箱记录.
      this.saveMessageNoticeInBox(noticeOut, recvPerson);
    }
    if (!reciverZhNameLst.equals("")) {
      reciverZhNameLst = reciverZhNameLst.substring(0, reciverZhNameLst.lastIndexOf(","));
      reciverEnNameLst = reciverEnNameLst.substring(0, reciverEnNameLst.lastIndexOf(","));
    }
    // 更新保存站内通知发件箱记录的扩展字段.
    this.updateMsgNoticeOutBoxExt(noticeOut, message, reciverZhNameLst, reciverEnNameLst);

    psnNodes.remove(SecurityUtils.getCurrentAllNodeId().get(0));
    if (psnNodes.size() > 0) {// 判断map结构是否有除本节点以外的人
      // TODO:同步短消息节点
      noticeOut.setSyncNodePsn(psnNodes);
      snsSyncMessageProducer.syncMessageNoticeToSns(noticeOut, SecurityUtils.getCurrentAllNodeId().get(0));
    }

  }

  /**
   * 封装保存站内通知发件箱信息.
   * 
   * @param title 通知标题.
   * @param person 发件人信息.
   * @param message 信息参数实体.
   * @return 站内通知发件箱记录实体.
   * @throws ServiceException
   */
  private MessageNoticeOutBox saveMessageNoticeOutBox(String title, Person person, Message message)
      throws ServiceException {

    // 往发信箱新增一条消息
    MessageNoticeOutBox noticeOut = new MessageNoticeOutBox();
    noticeOut.setTitle(title); // 通知标题.
    noticeOut.setContent(message.getContent()); // 通知内容.
    noticeOut.setPsnName(person.getName()); // 发件人姓名.
    noticeOut.setLastName(person.getLastName()); // 名.
    noticeOut.setFirstName(person.getFirstName()); // 姓.
    noticeOut.setSenderId(SecurityUtils.getCurrentUserId()); // 发送人ID.
    noticeOut.setPsnHeadUrl(person.getAvatars()); // 头像图片地址.
    noticeOut.setOptDate(new Date()); // 发送时间.
    noticeOut.setStatus(0); // 发件人状态.(默认为0 ,1-已删除)
    noticeOut.setMsgType(message.getMsgType()); // 消息类型.
    msgNoticeOutBoxDao.save(noticeOut);
    return noticeOut;
  }

  /**
   * 封装保存站内通知收件箱信息.
   * 
   * @param noticeOut 站内通知发件箱记录实体.
   * @param recvPerson 收件人信息.
   * @return
   */
  private MessageNoticeInBox saveMessageNoticeInBox(MessageNoticeOutBox noticeOut, Person recvPerson) {
    MessageNoticeInBox noticeIn = new MessageNoticeInBox();
    noticeIn.setNoticeId(noticeOut.getNoticeId()); // 发件箱记录ID.
    noticeIn.setStatus(0); // 信息状态0-未读.
    noticeIn.setSenderId(recvPerson.getPersonId());// 收件人ID.
    noticeIn.setPsnName(recvPerson.getName()); // 收件人姓名.
    noticeIn.setLastName(recvPerson.getLastName());// 名.
    noticeIn.setFirstName(recvPerson.getFirstName());// 姓.
    noticeIn.setPsnHeadUrl(recvPerson.getAvatars());// 头像图片地址.
    msgNoticeInBoxDao.save(noticeIn);
    return noticeIn;
  }

  /**
   * 更新保存站内通知发件箱记录的扩展字段.
   * 
   * @param noticeOut 站内通知发件箱记录实体.
   * @param message 信息参数实体.
   * @param reciverZhNameLst 接收者中文人名列表.
   * @param reciverEnNameLst 接收者英文人名列表.
   */
  private void updateMsgNoticeOutBoxExt(MessageNoticeOutBox noticeOut, Message message, String reciverZhNameLst,
      String reciverEnNameLst) {
    Map<String, String> noticeMap = new HashMap<String, String>();
    noticeMap.put("psnChineseName", message.getPsnName());
    noticeMap.put("psnFirstName", message.getPsnFirstName());
    noticeMap.put("psnLastName", message.getPsnLastName());
    noticeMap.put("groupName", message.getGroupName());
    noticeMap.put("reciverZhNameLst", reciverZhNameLst);
    noticeMap.put("reciverEnNameLst", reciverEnNameLst);
    noticeOut.setExtOtherInfo(JSONObject.fromObject(noticeMap).toString());
    msgNoticeOutBoxDao.save(noticeOut);
  }

  @Override
  public void sendMessage(Message message) throws ServiceException {

  }

  /**
   * 对人员节点分类.
   * 
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  private Map<Integer, String> sortPsnIdsByNodeId(String psnIds) throws ServiceException {

    try {
      Map<Integer, String> nodePsns = new HashMap<Integer, String>();
      List<User> psnList = userService.findUsersNodeId(psnIds);
      for (User user : psnList) {
        if (nodePsns.containsKey(user.getNodeId())) {
          nodePsns.put(user.getNodeId(), nodePsns.get(user.getNodeId()) + "," + user.getId());
        } else {
          nodePsns.put(user.getNodeId(), user.getId().toString());
        }
      }
      return nodePsns;
    } catch (Exception e) {
      logger.info("对人员节点分类", e);
      throw new ServiceException("对人员节点分类", e);
    }
  }

  /**
   * 修改发送邀请信息的方法——JIRA问题SCM-1037.
   * 
   * @修改逻辑：当邀请的人员不是系统中人员(psnIds为空)时，收件人信息设置为接收邀请的邮件地址(即保存邀请收件箱表INVITE_INBOX的记录)，以满足收件箱-请求菜单“发送的请求”的查询统计需要. @author
   *                                                                                                       Mao
   *                                                                                                       JianGuo
   * @since 2012-10-23
   * @return 发件箱的mailId
   */
  @Override
  public Map<String, Long> sendInviteMessage(Message message) throws ServiceException {

    Long curentUserId = message.getSendPsnId();
    if (curentUserId == null) {
      curentUserId = SecurityUtils.getCurrentUserId();
      message.setSendPsnId(curentUserId);
    }
    // 获取当前登录用户信息.
    Person person = personManager.getPsnNameAndAvatars(curentUserId);

    if (message.getInviteType().equals(0) && message.getTitle() == null) {
      message.setTitle(personManager.getPsnName(person) + "请求成为您的好友");
      message.setEnTitle(personManager.getPsnName(person) + " requested to be friend with you");
    } else if (message.getInviteType().equals(1) && message.getTitle() == null) {
      message.setTitle(personManager.getPsnName(person) + "邀请您加入群组");
      message.setEnTitle(personManager.getPsnName(person) + " invites you to join group");
    }
    // 封装保存发件箱
    /*
     * InviteMailBox inviteMailBox = this.saveInviteMailBox(person, message);
     * message.setMailId(inviteMailBox.getMailId().toString());// 收件人psnId为空,在邮件地址中要保存发件箱的mailId
     * 
     * if (message.getInviteType().equals(0)) {
     * 
     * FriendTemp frdTmp = friendTempDao.get(message.getInviteId());
     * frdTmp.setSendReqId(inviteMailBox.getMailId());// 保存收件箱Id的引用 friendTempDao.save(frdTmp); } else
     * if (message.getInviteType().equals(1)) { GroupInvite groupInvite =
     * groupInviteDao.get(message.getInviteId()); groupInvite.setReqId(inviteMailBox.getMailId());
     * groupInviteDao.save(groupInvite);
     * 
     * }
     */

    Long inviteInId = 0L;
    String psnIds = message.getReceivers();
    // 修改后逻辑:如果收件人ID为空，则获取其收件邮箱地址保存收件信息.//原逻辑： 如果为空，则表示站外邀请只保存发件箱
    if (StringUtils.isEmpty(psnIds)) {
      // inviteInId = this.dealInviteMessageByEmail(message, inviteMailBox);
      inviteInId = this.dealInviteMessageByEmail(message, null);
      // return;
    } else {
      // String inviteIdStr = this.dealInviteMessageById(message, inviteMailBox);
      String inviteIdStr = this.dealInviteMessageById(message, null);
      // 如果收件人只有一位且收件箱记录ID不为空，则获取收件人ID_MaoJianGuo_2013-01-24_SCM-1725.
      if (StringUtils.isNotBlank(inviteIdStr) && psnIds.split(",").length == 1) {
        inviteInId = Long.valueOf(inviteIdStr);
      } else {
        inviteInId = -1L;// 收件人为多位时，设定收件人值为-1.
      }
    }

    Map<String, Long> result = new HashMap<String, Long>();
    // result.put("mailId", inviteMailBox.getMailId());
    result.put("inviteInId", inviteInId);
    return result;
  }

  /**
   * 根据收件箱邮箱地址处理发送邀请信息.
   * 
   * @修改整理了发送邀请信息的方法：抽离了保存发件箱和收件箱的代码_MaoJianGuo_SCM-1074|SCM-1037_2012-11-02_测试功能时为方便理解，特修改.
   * 
   * @param message 消息参数实体.
   * @param inviteMailBox 发件箱记录参数实体.
   * @throws ServiceException
   * @return 收件箱ID.
   */
  private Long dealInviteMessageByEmail(Message message, InviteMailBox inviteMailBox) throws ServiceException {
    try {
      String email = "";// 接收邀请的邮件地址.
      if (message.getInviteType().equals(0)) {
        FriendTemp frdTmp = friendTempDao.get(message.getInviteId());
        email = frdTmp.getSendMail();
      } else if (message.getInviteType().equals(1)) {
        GroupInvite groupInvite = groupInviteDao.get(message.getInviteId());
        email = groupInvite.getEmail();
      }
      // 获取收件箱记录所需参数.
      String psnId = "-1";
      Integer optStatus = 0;
      Integer status = 0;
      if (message.getInviteType().equals(0) && PrivacyPemission.PEMISSION_SELF
          .equals(readReqAddFrdStatus(message.getSendPsnId(), Long.parseLong(psnId)))) {
        // optStatus = 4;
      }
      // 初始化一个空的人员记录.
      Person recvPerson = new Person();
      recvPerson.setName(email);// 接收邀请的人员的名称设置为邮件地址.
      recvPerson.setFirstName(email);// 接收邀请的人员的英文名称设置为邮件地址.
      // 设置默认人员头像.
      String webDomain = sysDomainConst.getSnsDomain();
      String avatars = webDomain + ServiceConstants.DEFAULT_MAN_AVATARS;
      recvPerson.setAvatars(avatars);

      // 保存收件箱记录.
      // InviteInbox inviteInbox = this.saveInviteInbox(psnId, optStatus, status, recvPerson,
      // inviteMailBox.getMailId());
      // message.setRecvId(inviteInbox.getId().toString());
      // 记录引用关系.
      /*
       * if (message.getInvitePsnId() != null) { RecordRelation rel =
       * this.recordRelationDao.findRecord(message.getInvitePsnId(), 1, Long.valueOf(psnId)); if (rel ==
       * null) { rel = new RecordRelation(message.getInvitePsnId(), inviteInbox.getId(), 1);
       * this.recordRelationDao.save(rel); } else { this.inboxService.setStatusOnInviteInbox("success",
       * "neglect", rel); rel.setInviteId(inviteInbox.getId()); this.recordRelationDao.save(rel); } }
       * return inviteInbox.getId();
       */
      return null;
    } catch (Exception e) {
      logger.info("根据收件箱邮箱地址处理发送邀请信息", e);
      throw new ServiceException("根据收件箱邮箱地址处理发送邀请信息", e);
    }
  }

  /**
   * 根据人员ID处理发送邀请信息.
   * 
   * @修改整理了发送邀请信息的方法：抽离了保存发件箱和收件箱的代码_MaoJianGuo_SCM-1074|SCM-1037_2012-11-02_测试功能时为方便理解，特修改.
   * 
   * @param message 消息参数实体.
   * @param inviteMailBox 发件箱记录参数实体.
   * @throws ServiceException
   */
  private String dealInviteMessageById(Message message, InviteMailBox inviteMailBox) throws ServiceException {
    String psnIds = message.getReceivers();
    // 根据ID获取被邀请的人员列表.
    Map<Integer, String> psnNodes = sortPsnIdsByNodeId(psnIds);
    StringBuilder idStr = new StringBuilder();
    String zhReceiver = "";
    String enReceiver = "";
    // 分割被邀请人员ID.
    String[] psnIdArr = psnIds.split(",");
    if (psnIdArr != null && psnIdArr.length >= 1) {
      for (int i = 0, length = psnIdArr.length; i < length; i++) {
        String psnId = psnIdArr[i];
        // 检查是否有重复的好友/群组 邀请，如果有，则将以前的好友/群组邀请 重置为系统忽略状态
        // 被邀请者Id
        if (psnId != null) {
          Long invitederId = Long.valueOf(psnId);// 被邀请者Id
          Long senderId = message.getSendPsnId();// 发送者Id
          String inviteType = "";// 邀请类型：fridend：好友邀请；group:群组邀请
          if (message.getInviteType().equals(0)) {// 好友邀请
            inviteType = "friend";
            boolean isRepeat = this.inboxService.isRepeatInvite(invitederId, senderId, inviteType);
            if (isRepeat) {// 如果重复则忽略之前的好友请求，重新发送请求
              this.inboxService.ignoreRepeatInvite(invitederId, senderId, inviteType);
            }

          } else if (message.getInviteType().equals(1)) {// 群组邀请
            inviteType = "group";
            Long inviteId = message.getInviteId();
            GroupInvite groupInvite = this.groupInviteDao.get(inviteId);
            // GroupPsn groupPsn =
            // this.groupPsnDao.get(groupInvite.getGroupId());

            this.inboxService.ignoreRepeatInvite(invitederId, senderId, inviteType, groupInvite.getGroupId());

          } else if (message.getInviteType().equals(2)) {// 请求加入群组
            inviteType = "request";
          }

        }
        // 获取收件箱记录所需参数.
        psnId = psnId.trim();
        Integer optStatus = 0;// 邮件处理状态.
        if (message.getInviteType().equals(0) && PrivacyPemission.PEMISSION_SELF
            .equals(readReqAddFrdStatus(message.getSendPsnId(), Long.parseLong(psnId)))) {
          // optStatus = 4;
          continue;
        }

        String personIds = "," + psnNodes.get(SecurityUtils.getCurrentAllNodeId().get(0)) + ",";
        Integer status = 0;// 邮件状态.
        if (personIds.indexOf("," + psnId + ",") == -1) { // 非本节点收件人
          status = 2;
        }
        Person recvPerson = personManager.getPerson(new Long(psnId));
        if (i < 10) {
          zhReceiver += "," + recvPerson.getName();
          enReceiver += "," + recvPerson.getFirstName() + " " + recvPerson.getLastName();
        }
        // 保存收件箱记录.
        // InviteInbox inviteInbox = this.saveInviteInbox(psnId, optStatus, status, recvPerson,
        // inviteMailBox.getMailId());
        /*
         * if (personIds.indexOf("," + psnId + ",") != -1) { if (message.getInvitePsnId() != null) {
         * RecordRelation rel = this.recordRelationDao.findRecord(message.getInvitePsnId(), 1,
         * Long.valueOf(psnId)); if (rel == null) { rel = new RecordRelation(message.getInvitePsnId(),
         * inviteInbox.getId(), 1); this.recordRelationDao.save(rel); } else {
         * this.inboxService.setStatusOnInviteInbox("success", "neglect", rel);
         * rel.setInviteId(inviteInbox.getId()); this.recordRelationDao.save(rel); } } } // 追加收件箱记录ID.
         * idStr.append(inviteInbox.getId());
         */
        if (psnIdArr.length > 1) {
          idStr.append(",");
        }
        cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME, MessageConstant.MSG_TIP_CACHE_KEY + psnId);
      } // 遍历收件人ID数组结束.
      if (psnIdArr.length > 1) {
        idStr.deleteCharAt(idStr.length());
      }

    }
    /*
     * inviteMailBox.setZhReceiver(zhReceiver); inviteMailBox.setEnReceiver(enReceiver);
     * this.inviteMailBoxDao.save(inviteMailBox);
     */

    psnNodes.remove(SecurityUtils.getCurrentAllNodeId().get(0));
    if (psnNodes.size() > 0) {// 判断map结构是否有除本节点以外的人
      // TODO:同步短消息节点
      inviteMailBox.setSyncNodePsn(psnNodes);
      snsSyncMessageProducer.syncInviteMsgToSns(inviteMailBox, SecurityUtils.getCurrentAllNodeId().get(0));
    }
    return idStr.toString();
  }

  /**
   * 封装保存发件箱信息.
   * 
   * @param person 当前用户参数实体.
   * @param message 消息参数实体.
   * @return
   * @throws ServiceException
   */
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
    List<WorkHistory> lstWorkEdu = personManager.findWorkAndEdu(person.getPersonId());

    // 封装设置发件人基本信息(json格式).
    JSONObject jsonSerder = new JSONObject();
    jsonSerder.put("titolo", personManager.getPsnViewTitolo(person));
    if (CollectionUtils.isNotEmpty(lstWorkEdu)) {
      for (WorkHistory wh : lstWorkEdu) {
        if (wh.getIsPrimary() != null && wh.getIsPrimary() == 1) {
          jsonSerder.put("primaryUtil", wh.getInsName());
          jsonSerder.put("dept", wh.getDepartment());
          jsonSerder.put("position", wh.getPosition());
          break;
        }
      }
    }

    /*
     * inviteMailBox.setSenderInfo(jsonSerder.toString()); Long receiverId = message.getInvitePsnId();
     * inviteMailBox.setInvitePsnId(receiverId);// 保存请求加入群组的id inviteMailBoxDao.save(inviteMailBox);
     */

    // 保存邀请发件记录内容_MJG_SCM-5910.
    InviteMailBoxCon inviteMailBoxCon = new InviteMailBoxCon();
    inviteMailBoxCon.setMailId(inviteMailBox.getMailId());
    inviteMailBoxCon.setTitleZh(message.getTitle());
    inviteMailBoxCon.setTitleEn(message.getEnTitle());
    inviteMailBoxCon.setContent(message.getContent());
    // 扩展信息.
    inviteMailBoxCon.setExtOtherInfo(message.getExtOtherInfo());
    inviteMailBoxConDao.saveMailBoxCon(inviteMailBoxCon);

    inviteMailBox.setTitle(message.getTitle());
    inviteMailBox.setEnTitle(message.getEnTitle());
    inviteMailBox.setContent(message.getContent());
    // 扩展信息.
    inviteMailBox.setExtOtherInfo(message.getExtOtherInfo());

    return inviteMailBox;
  }

  /**
   * 封装保存收件箱信息.
   * 
   * @param psnId 人员ID.
   * @param optStatus 邮件处理状态.
   * @param personIds
   * @param recvPerson 人员记录信息.
   * @param mailId 发件箱ID.
   * @return 收件箱记录.
   */
  private InviteInbox saveInviteInbox(String psnId, Integer optStatus, Integer status, Person recvPerson, Long mailId) {
    InviteInbox inviteInbox = new InviteInbox(Long.parseLong(psnId), mailId, status, optStatus);
    inviteInbox.setFirstName(recvPerson.getFirstName());
    inviteInbox.setLastName(recvPerson.getLastName());
    inviteInbox.setPsnName(recvPerson.getName());
    inviteInbox.setPsnHeadUrl(recvPerson.getAvatars());
    inviteInboxDao.save(inviteInbox);
    return inviteInbox;
  }

  @Override
  @Deprecated
  public void saveSyncInviteMessage(InviteMailBox inviteMailBox) throws ServiceException {

    if (inviteMailBox == null || inviteMailBox.getSyncNodePsn() == null) {
      logger.error("同步的站内请求消息实体为空！");
      throw new ServiceException();
    }

    String psnIds = inviteMailBox.getSyncNodePsn().get(SecurityUtils.getCurrentAllNodeId().get(0));
    if (StringUtils.isEmpty(psnIds)) {
      logger.error("当前节点不需要同步或同步数据丢失！");
      return;
    }

    // 保存发件箱 （表已弃用）
    // this.syncInviteMailBoxDao.save(renewSyncInviteMailBox(inviteMailBox));
    // 保存邀请发件记录内容_MJG_SCM-5910. 表已弃用
    /*
     * InviteMailBoxCon inviteMailBoxCon = new InviteMailBoxCon();
     * inviteMailBoxCon.setMailId(inviteMailBox.getMailId());
     * inviteMailBoxCon.setTitleZh(inviteMailBox.getTitle());
     * inviteMailBoxCon.setTitleEn(inviteMailBox.getEnTitle());
     * inviteMailBoxCon.setContent(inviteMailBox.getContent());
     * inviteMailBoxCon.setExtOtherInfo(inviteMailBox.getExtOtherInfo());
     * inviteMailBoxConDao.saveMailBoxCon(inviteMailBoxCon);
     */

    for (String psnId : psnIds.split(",")) {
      Long personId = new Long(psnId);
      Integer optStatus = 0;

      if (inviteMailBox.getInviteType().equals(0) && PrivacyPemission.PEMISSION_SELF
          .equals(readReqAddFrdStatus(inviteMailBox.getSenderId(), Long.parseLong(psnId)))) {
        // optStatus = 4;
        continue;
      }

      // 保存收件箱信息
      /*
       * Person person = personManager.getPerson(personId); InviteInbox inbox = new InviteInbox(personId,
       * inviteMailBox.getMailId(), 0, optStatus); inbox.setPsnName(person.getName());
       * inbox.setFirstName(person.getFirstName()); inbox.setLastName(person.getLastName());
       * inbox.setPsnHeadUrl(person.getAvatars()); inviteInboxDao.save(inbox); if
       * (inviteMailBox.getInvitePsnId() != null) { RecordRelation rel =
       * this.recordRelationDao.findRecord(inviteMailBox.getInvitePsnId(), 1, inbox.getPsnId()); if (rel
       * == null) { rel = new RecordRelation(inviteMailBox.getInvitePsnId(), inbox.getId(), 1);
       * this.recordRelationDao.save(rel); } else { this.inboxService.setStatusOnInviteInbox("success",
       * "neglect", rel); rel.setInviteId(inbox.getId()); this.recordRelationDao.save(rel); } }
       */

      cacheService.remove(MessageConstant.MSG_TIP_CACHE_NAME, MessageConstant.MSG_TIP_CACHE_KEY + psnId);
    }

  }

  /**
   * 重新构建发件箱对象.
   * 
   * @param inviteMail
   * @return
   * @throws ServiceException
   */
  @Deprecated
  private SyncInviteMailBox renewSyncInviteMailBox(InviteMailBox inviteMail) throws ServiceException {

    SyncInviteMailBox syncInviteMail = new SyncInviteMailBox();
    syncInviteMail.setMailId(inviteMail.getMailId());
    syncInviteMail.setSenderId(inviteMail.getSenderId());
    syncInviteMail.setPsnName(inviteMail.getPsnName());
    syncInviteMail.setFirstName(inviteMail.getFirstName());
    syncInviteMail.setLastName(inviteMail.getLastName());
    syncInviteMail.setPsnHeadUrl(inviteMail.getPsnHeadUrl());
    syncInviteMail.setOptDate(inviteMail.getOptDate());
    syncInviteMail.setInviteType(inviteMail.getInviteType() == null ? 0 : inviteMail.getInviteType());
    syncInviteMail.setStatus(0);
    syncInviteMail.setZhReceiver(inviteMail.getZhReceiver());
    syncInviteMail.setEnReceiver(inviteMail.getEnReceiver());
    return syncInviteMail;

  }

  @Override
  public void saveGroupReqInbox(Long inviteId) throws ServiceException {

    try {

      GroupInvite groupInvite = groupInviteDao.get(inviteId);

      InviteInbox inbox = new InviteInbox(groupInvite.getPsnId(), groupInvite.getReqId(), 0, 0);
      Person recvPerson = personManager.getPerson(groupInvite.getPsnId());
      inbox.setFirstName(recvPerson.getFirstName());
      inbox.setLastName(recvPerson.getLastName());
      inbox.setPsnName(recvPerson.getName());
      inbox.setPsnHeadUrl(recvPerson.getAvatars());
      inviteInboxDao.save(inbox);
      groupInvite.setReqId(null);
      this.groupInviteDao.save(groupInvite);
    } catch (Exception e) {

      logger.error("保存群组邀请消息失败", e);
      throw new ServiceException(e);

    }
  }

  @Override
  public void saveFrdReqInbox(Long inviteId, Long psnId) throws ServiceException {

    try {

      FriendTemp frdTemp = friendTempDao.get(inviteId);

      InviteInbox inbox = new InviteInbox(frdTemp.getTempPsnId(), frdTemp.getSendReqId(), 0, 0);
      Person recvPerson = personManager.getPerson(frdTemp.getTempPsnId());
      inbox.setFirstName(recvPerson.getFirstName());
      inbox.setLastName(recvPerson.getLastName());
      inbox.setPsnName(recvPerson.getName());
      inbox.setPsnHeadUrl(recvPerson.getAvatars());
      frdTemp.setSendReqId(null);
      this.friendTempDao.save(frdTemp);
      inviteInboxDao.save(inbox);
    } catch (Exception e) {
      logger.error("保存好友邀请消息失败!", e);
      throw new ServiceException(e);

    }
  }

  @Override
  public void sendConfirmMsgForAddFrd(HashMap<String, ?> ctxMap) throws ServiceException {

    Message message = new Message();
    if (ctxMap.size() < 2) {
      logger.error("map对象缺少传入的参数！");
      throw new ServiceException();
    }
    Person person = personManager.getPsnNameAndAvatars((Long) ctxMap.get("psnId"));
    String psnName = personManager.getPsnName(person);
    message.setPsnName(psnName);
    message.setPsnFirstName(person.getFirstName());
    message.setPsnLastName(person.getLastName());
    String psnUrl = getPersonResumeUrl(false, person.getPersonId());
    String content = "";
    if ("zh_CN".equals(LocaleContextHolder.getLocale().toString())) {
      content = "<a href=\"" + psnUrl + "\" target=\"_blank\">" + psnName + "</a>";
    } else {
      content = "<a href=\"" + psnUrl + "\" target=\"_blank\">" + psnName + "</a>";
    }
    message.setContent(content);
    message.setReceivers(ctxMap.get("recvPsnId").toString());
    message.setMsgType(0);// 邀请好友成功
    message.setSendPsnId(SecurityUtils.getCurrentUserId());
    this.sendInsideMessageForSys(message);

  }

  // 构建消息标题
  private String buildTitle(Map<String, String> ctxMap) throws ServiceException {

    ctxMap.put("msgTitle", "title");
    return build(ctxMap);

  }

  // 构建邮件标题

  private String buildEmailTitle(Map<String, Object> map) throws ServiceException {

    String msg;
    try {
      msg = FreeMarkerTemplateUtils.processTemplateIntoString(
          freemarkerConfiguration.getTemplate(ObjectUtils.toString(map.get("tmpUrl")), ENCODING), map);
      return msg;

    } catch (IOException e) {
      logger.error("构建Email标题失败，没有找到对应的Email标题模板！", e);
    } catch (TemplateException e) {
      logger.error("构造Email标题失败,FreeMarker处理失败", e);

    }
    return null;
  }

  private String build(Map<String, String> ctxMap) throws ServiceException {

    try {

      String msg = FreeMarkerTemplateUtils
          .processTemplateIntoString(msgFreemarkereConfiguration.getTemplate(ctxMap.get("tmpUrl"), ENCODING), ctxMap);
      return HtmlUtils.replaceBlank(msg);

    } catch (IOException e) {
      logger.error("构建消息失败，没有找到对应的消息模板！", e);

    } catch (TemplateException e) {

      logger.error("构造消息失败,FreeMarker处理失败", e);

    }

    return null;

  }

  /**
   * 获取人员简历列表
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  private String getPersonResumeUrl(boolean isOutter, Long psnId) throws ServiceException {
    try {
      if (isOutter) {
        String webUrl = "https://" + sysDomainConst.getSnsDomain();
        if (sysDomainConst.getSnsContext() != null) {
          webUrl = webUrl + "/" + sysDomainConst.getSnsContext();
        }
        return webUrl + "/resume/view?des3PsnId=" + ServiceUtil.encodeToDes3(psnId.toString());
      } else {

        return "/scmwebsns/resume/view?des3PsnId=" + ServiceUtil.encodeToDes3(psnId.toString());
      }
    } catch (Exception e) {
      logger.error("读取用户个人链接失败！", e);
      return null;

    }

  }

  @Override
  public void sendMessageForReq(HashMap<String, String> map) throws ServiceException {

    if (map.size() < 3) {

      logger.error("map缺少必要的参数，请检查！");
      throw new ServiceException();

    }

    ReqMailBox reqMail = new ReqMailBox();
    Person person = personManager.getPerson(SecurityUtils.getCurrentUserId());
    map.put("psnName", personManager.getPsnName(person));
    String msgTemplate = "Request_Update_Msg_Template_" + LocaleContextHolder.getLocale().toString() + ".ftl";
    map.put("tmpUrl", msgTemplate);
    reqMail.setTitle(buildTitle(map));
    reqMail.setPsnName(person.getName());
    reqMail.setSenderId(SecurityUtils.getCurrentUserId());
    reqMail.setFirstName(person.getFirstName());
    reqMail.setLastName(person.getLastName());
    reqMail.setPsnHeadUrl(person.getAvatars());
    reqMail.setOptDate(new Date());
    reqMail.setStatus(0);
    reqMailBoxDao.save(reqMail);

    String psnIds = map.get("receivers");
    Map<Integer, String> psnNodes = sortPsnIdsByNodeId(psnIds);

    for (String psnId : psnIds.split(",")) {
      psnId = psnId.trim();
      String personIds = "," + psnNodes.get(SecurityUtils.getCurrentAllNodeId().get(0)) + ",";
      Person recvPerson = personManager.getPerson(new Long(psnId));

      ReqInbox reqInbox = new ReqInbox(Long.parseLong(psnId), reqMail.getMailId(), 0);
      if (personIds.indexOf("," + psnId + ",") == -1) { // 非本节点收件人
        reqInbox.setStatus(2);
      }
      reqInbox.setPsnName(recvPerson.getName());
      reqInbox.setFirstName(recvPerson.getFirstName());
      reqInbox.setLastName(recvPerson.getLastName());
      reqInbox.setPsnHeadUrl(recvPerson.getAvatars());

      reqInboxDao.save(reqInbox);
    }

    psnNodes.remove(SecurityUtils.getCurrentAllNodeId().get(0));
    if (psnNodes.size() > 0) {// 判断map结构是否有除本节点以外的人
      // TODO:同步请求更新文献消息

      reqMail.setSyncNodePsn(psnNodes);
      snsSyncMessageProducer.syncReqMsgToSns(reqMail, SecurityUtils.getCurrentAllNodeId().get(0));
    }

    // 发送邮件通知

    // sendMailForReq(map);
  }

  /**
   * 请求好友更新引用邮件
   */
  @Override
  public void sendMailForReq(HashMap<String, Object> map, Long mailType) throws ServiceException {

    if (!(map.containsKey("viewUrl") && map.containsKey("psnName") && map.containsKey("total")
        && map.containsKey("caseType") && map.containsKey("mailSetUrl"))) {

      logger.error("map缺少必要的参数，请检查！");
      throw new ServiceException();

    }

    try {
      String mailTemplate = null;
      String tempUrl = null;
      String subject = null;
      String email = null;

      String psnIds = ObjectUtils.toString(map.get("receivers"));
      Person person = personManager.getPersonForEmail(SecurityUtils.getCurrentUserId());
      if (psnIds != null) {
        for (String psnId : psnIds.split(",")) {
          Long intPsnId = Long.valueOf(psnId);
          Person recPerson = personManager.getPersonForEmail(intPsnId);
          // 获取用户设置接收邮件的语言
          String languageVersion = recPerson.getEmailLanguageVersion();

          if (languageVersion == null) {
            languageVersion = LocaleContextHolder.getLocale().toString();
          }
          mailTemplate = "Notify_Request_Update_Template_" + languageVersion + ".ftl";
          tempUrl = "Notify_Request_Update_Template_title_" + languageVersion + ".ftl";
          map.put("mailSetUrl", map.get("mailSetUrl") + "&locale=" + languageVersion);
          map.put("viewUrl", map.get("viewUrl") + "?locale=" + languageVersion);
          map.put("subject", "req");
          map.put("tmpUrl", tempUrl);
          map.put("recvName", this.emailCommonService.getPsnNameByEmailLangage(recPerson, languageVersion));// recvName
          map.put("psnName", this.emailCommonService.getPsnNameByEmailLangage(person, languageVersion));// psnName
          subject = this.buildEmailTitle(map);
          email = userService.findUserById(Long.parseLong(psnId)).getEmail();
          map.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
          map.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, email);
          map.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
          map.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, intPsnId);
          reqFrdUpdateCitedEmailService.syncEmailInfo(map);
        }
      }
    } catch (Exception e) {
      logger.info("请求好友更新引用邮件", e);
      throw new ServiceException("请求好友更新引用邮件", e);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void sendMessageForShare(HashMap<String, String> map, Long sendPsnId) throws ServiceException {
    try {
      if (map.size() < 4) {

        logger.error("map缺少必要的参数，请检查！");
        throw new ServiceException();
      }
      ShareMailBox shareMail = new ShareMailBox();
      Person person = null;

      if (map.get("recommendPsnId") != null && NumberUtils.isDigits(map.get("recommendPsnId").toString())) {
        person = personProfileDao.get(Long.valueOf(map.get("recommendPsnId")));
      }
      if (person == null && SecurityUtils.getCurrentUserId() != null) {
        person = personManager.getPerson(sendPsnId);
      }

      String zhPsnName = person.getName();
      if (StringUtils.isBlank(zhPsnName)) {
        zhPsnName = person.getFirstName() + " " + person.getLastName();
      }

      String enPsnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) && StringUtils.isBlank(person.getLastName())) {
        enPsnName = person.getName();
      }
      map.put("zhPsnName", zhPsnName);
      map.put("enPsnName", enPsnName);
      String msgTemplate = "Share_Mail_Title_Template.ftl";
      String deadLine = map.get("deadLine");

      map.put("tmpUrl", msgTemplate);
      map.put("title", buildTitle(map));
      shareMail.setTitle(map.get("title"));
      shareMail.setSenderId(person.getPersonId());
      shareMail.setFirstName(person.getFirstName());
      shareMail.setLastName(person.getLastName());
      shareMail.setPsnName(person.getName());
      shareMail.setPsnHeadUrl(person.getAvatars());
      shareMail.setSendDate(new Date());
      shareMail.setDeadLine(new Date(new Date(deadLine).getTime()));
      shareMail.setStatus(0);

      String psnIds = map.get("receivers");
      Map<Integer, String> psnNodes = sortPsnIdsByNodeId(psnIds);

      String zhReceiver = "";
      String enReceiver = "";
      List<ShareInbox> shareInboxList = new ArrayList<ShareInbox>();
      String[] psnIdArray = psnIds.split(",");
      for (int i = 0, length = psnIdArray.length; i < length; i++) {
        String psnId = psnIdArray[i];
        psnId = psnId.trim();
        String personIds = "," + psnNodes.get(SecurityUtils.getCurrentAllNodeId().get(0)) + ",";
        Person recvPerson = personManager.getPerson(new Long(psnId));
        if (i < 10) {
          zhReceiver += "," + recvPerson.getName();
          enReceiver += "," + recvPerson.getFirstName() + " " + recvPerson.getLastName();
        }
        ShareInbox shareInbox = new ShareInbox(Long.parseLong(psnId), shareMail.getMailId(), 0);
        shareInbox.setPsnId(Long.parseLong(psnId));
        shareInbox.setStatus(0);
        if (personIds.indexOf("," + psnId + ",") == -1) { // 非本节点收件人
          shareInbox.setStatus(2);
        }
        shareInbox.setPsnHeadUrl(recvPerson.getAvatars());
        shareInbox.setPsnName(recvPerson.getName());
        shareInbox.setFirstName(recvPerson.getFirstName());
        shareInbox.setLastName(recvPerson.getLastName());
        shareInboxList.add(shareInbox);
      }

      shareMail.setZhReceiver(zhReceiver);
      shareMail.setEnReceiver(enReceiver);
      shareMailBoxDao.save(shareMail);
      for (ShareInbox shareInboxOut : shareInboxList) {
        shareInboxOut.setMailId(shareMail.getMailId());
        shareInboxDao.save(shareInboxOut);
      }

      if (StringUtils.isNotEmpty(map.get("isSkip")) && map.get("isSkip").equals("yes")) {// 不用同步
        return;
      }
      psnNodes.remove(SecurityUtils.getCurrentAllNodeId().get(0));
      if (psnNodes.size() > 0) {// 判断map结构是否有除本节点以外的人
        // TODO:同步共享消息
        shareMail.setSyncNodePsn(psnNodes);
        snsSyncMessageProducer.syncShareMsgToSns(shareMail, SecurityUtils.getCurrentAllNodeId().get(0));
      }
      // 发送邮件 成果推荐的邮件要分开发
      // sendMailForShare(map);
    } catch (Exception e) {
      logger.error(e.getMessage() + e);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void sendMessageForShare2(HashMap<String, String> map, PsnResSend psnResSend, int type, String email,
      String receiverName, int nodeId) throws ServiceException {
    try {
      if (map.size() < 4) {

        logger.error("map缺少必要的参数，请检查！");
        throw new ServiceException();
      }
      ShareMailBox shareMail = new ShareMailBox();
      Person person = null;

      if (map.get("recommendPsnId") != null && NumberUtils.isDigits(map.get("recommendPsnId").toString())) {
        person = personProfileDao.get(Long.valueOf(map.get("recommendPsnId")));
      }
      if (person == null && SecurityUtils.getCurrentUserId() != null) {
        person = personManager.getPerson(psnResSend.getPsnId());
      }

      String zhPsnName = person.getName();
      if (StringUtils.isBlank(zhPsnName)) {
        zhPsnName = person.getFirstName() + " " + person.getLastName();
      }

      String enPsnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) && StringUtils.isBlank(person.getLastName())) {
        enPsnName = person.getName();
      }
      map.put("zhPsnName", zhPsnName);
      map.put("enPsnName", enPsnName);
      String msgTemplate = "Share_Mail_Title_Template.ftl";
      String deadLine = map.get("deadLine");

      map.put("tmpUrl", msgTemplate);
      map.put("title", buildTitle(map));
      shareMail.setTitle(map.get("title"));
      shareMail.setSenderId(person.getPersonId());
      shareMail.setFirstName(person.getFirstName());
      shareMail.setLastName(person.getLastName());
      shareMail.setPsnName(person.getName());
      shareMail.setPsnHeadUrl(person.getAvatars());
      shareMail.setSendDate(new Date());
      shareMail.setDeadLine(new Date(new Date(deadLine).getTime()));
      shareMail.setStatus(0);

      String psnIds = map.get("receivers");
      Map<Integer, String> psnNodes = sortPsnIdsByNodeId(psnIds);

      String zhReceiver = "";
      String enReceiver = "";
      List<ShareInbox> shareInboxList = new ArrayList<ShareInbox>();
      String[] psnIdArray = psnIds.split(",");
      for (int i = 0, length = psnIdArray.length; i < length; i++) {
        String psnId = psnIdArray[i];
        psnId = psnId.trim();
        String personIds = "," + psnNodes.get(SecurityUtils.getCurrentAllNodeId().get(0)) + ",";
        Person recvPerson = personManager.getPerson(new Long(psnId));
        if (i < 10) {
          zhReceiver += "," + recvPerson.getName();
          enReceiver += "," + recvPerson.getFirstName() + " " + recvPerson.getLastName();
        }
        ShareInbox shareInbox = new ShareInbox();
        shareInbox.setPsnId(Long.parseLong(psnId));
        shareInbox.setStatus(0);
        if (personIds.indexOf("," + psnId + ",") == -1) { // 非本节点收件人
          shareInbox.setStatus(2);
        }
        shareInbox.setPsnHeadUrl(recvPerson.getAvatars());
        shareInbox.setPsnName(recvPerson.getName());
        shareInbox.setFirstName(recvPerson.getFirstName());
        shareInbox.setLastName(recvPerson.getLastName());
        shareInboxList.add(shareInbox);

      }

      shareMail.setZhReceiver(zhReceiver);
      shareMail.setEnReceiver(enReceiver);
      shareMailBoxDao.save(shareMail);

      for (ShareInbox shareInboxOut : shareInboxList) {
        shareInboxOut.setMailId(shareMail.getMailId());
        shareInboxDao.save(shareInboxOut);
      }

      psnNodes.remove(SecurityUtils.getCurrentAllNodeId().get(0));
      if (psnNodes.size() > 0) {// 判断map结构是否有除本节点以外的人
        // TODO:同步共享消息
        shareMail.setSyncNodePsn(psnNodes);
        snsSyncMessageProducer.syncShareMsgToSns(shareMail, SecurityUtils.getCurrentAllNodeId().get(0));
      }
    } catch (Exception e) {
      logger.error(e.getMessage() + e);
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public void saveSendMail(HashMap<String, String> map, PsnResSend psnResSend) throws ServiceException {
    try {
      if (map.size() < 4) {

        logger.error("map缺少必要的参数，请检查！");
        throw new ServiceException();
      }
      ShareMailBox shareMail = new ShareMailBox();
      Person person = null;
      String zhPsnName = "";
      String enPsnName = "";

      person = personManager.getPerson(psnResSend.getPsnId());
      zhPsnName = person.getName();
      if (StringUtils.isBlank(zhPsnName)) {
        zhPsnName = person.getFirstName() + " " + person.getLastName();
      }

      enPsnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) && StringUtils.isBlank(person.getLastName())) {
        enPsnName = person.getName();
      }

      map.put("zhPsnName", zhPsnName);
      map.put("enPsnName", enPsnName);
      String msgTemplate = "Share_Mail_Title_Template.ftl";
      String deadLine = map.get("deadLine");

      map.put("tmpUrl", msgTemplate);
      map.put("title", buildTitle(map));
      shareMail.setTitle(map.get("title"));
      shareMail.setSenderId(person.getPersonId());
      shareMail.setFirstName(person.getFirstName());
      shareMail.setLastName(person.getLastName());
      shareMail.setPsnName(person.getName());
      shareMail.setPsnHeadUrl(person.getAvatars());
      shareMail.setSendDate(new Date());
      shareMail.setDeadLine(new Date(new Date(deadLine).getTime()));
      shareMail.setStatus(0);

      String psnIds = map.get("receivers");
      Map<Integer, String> psnNodes = sortPsnIdsByNodeId(psnIds);

      String zhReceiver = "";
      String enReceiver = "";
      List<ShareInbox> shareInboxList = new ArrayList<ShareInbox>();
      String[] psnIdArray = psnIds.split(",");
      for (int i = 0, length = psnIdArray.length; i < length; i++) {
        String psnId = psnIdArray[i];
        psnId = psnId.trim();
        String personIds = "," + psnNodes.get(SecurityUtils.getCurrentAllNodeId().get(0)) + ",";
        Person recvPerson = personManager.getPerson(new Long(psnId));
        if (i < 10) {
          zhReceiver += "," + recvPerson.getName();
          enReceiver += "," + recvPerson.getFirstName() + " " + recvPerson.getLastName();
        }
        ShareInbox shareInbox = new ShareInbox();
        shareInbox.setPsnId(Long.parseLong(psnId));
        shareInbox.setStatus(0);
        if (personIds.indexOf("," + psnId + ",") == -1) { // 非本节点收件人
          shareInbox.setStatus(2);
        }
        shareInbox.setPsnHeadUrl(recvPerson.getAvatars());
        shareInbox.setPsnName(recvPerson.getName());
        shareInbox.setFirstName(recvPerson.getFirstName());
        shareInbox.setLastName(recvPerson.getLastName());
        shareInboxList.add(shareInbox);
      }

      shareMail.setZhReceiver(zhReceiver);
      shareMail.setEnReceiver(enReceiver);
      shareMailBoxDao.save(shareMail);
      for (ShareInbox shareInboxOut : shareInboxList) {
        shareInboxOut.setMailId(shareMail.getMailId());
        shareInboxDao.save(shareInboxOut);
      }
    } catch (Exception e) {
      logger.error(e.getMessage() + e);
    }

  }

  @Override
  public String genEmailViewResUrl(PsnResSend psnResSend, int type, String email, int nodeId) throws ServiceException {
    try {

      String url = "https://" + sysDomainConst.getSnsDomain();
      if (sysDomainConst.getSnsContext() != null) {
        url = url + "/" + sysDomainConst.getSnsContext();
      }
      String parameters = psnResSend.getSendId() + "," + nodeId + "," + type + "," + email + ","
          + DateFormatUtils.format(psnResSend.getDeadline(), "yyyy/MM/dd");
      parameters = ServiceUtil.encodeToDes3(parameters);
      url += "/commend/emailViewFiles?parameters=" + parameters;// 加密
      return url;
    } catch (Exception e) {
      logger.info("genEmailViewResUrl", e);
      throw new ServiceException("genEmailViewResUrl", e);
    }
  }

  /**
   * 推荐资源邮件
   */
  @Override
  public void sendMailForShare(Map<String, Object> map, Long mailType) throws ServiceException {
    if (!(map.containsKey("viewUrl") && map.containsKey("total") && map.containsKey("type"))) {
      logger.error("map缺少必要的参数，请检查！");
      throw new ServiceException();
    }
    // 语言版本
    String languageVersion = ObjectUtils.toString(map.get("currentLocale"));
    if (StringUtils.isEmpty(languageVersion)) {
      languageVersion = LocaleContextHolder.getLocale().toString();
      if (StringUtils.isEmpty(languageVersion)) {
        languageVersion = "zh_CN";
      }
    }
    try {
      String psnName = null;

      String psnIds = ObjectUtils.toString(map.get("receivers"));
      String targetEmail = ObjectUtils.toString(map.get("email"));
      if ((StringUtils.isEmpty(psnIds) || StringUtils.isBlank(psnIds) || psnIds.toLowerCase().equals("null"))
          && targetEmail != null) {
        // 根据用户录入的邮件查找相应的用户列表
        List<User> userList = userService.findUserByLoginNameOrEmail(targetEmail);
        Long isVerifyUserId = null;
        if (CollectionUtils.isNotEmpty(userList)) {

          int verifyEmailCount = 0;
          for (User user : userList) {
            // 查看该人该邮件是否已经经过确认
            boolean isVerify = personEmailManager.isPsnVerified(user.getId(), targetEmail);
            if (isVerify) {
              isVerifyUserId = user.getId();
              verifyEmailCount++;
            }
          }

          // 如果只有一个用户确认这个邮箱，则才要判断此用户是否设置接收邮件和显示该用户名称
          if (verifyEmailCount == 1) {

            Person psn = this.personManager.getPerson(isVerifyUserId);
            map.put("recvName", this.emailCommonService.getPsnNameByEmailLangage(psn, languageVersion));
          }
        }

        Person sendPerson = personManager.getPersonById(Long.valueOf(ObjectUtils.toString(map.get("recommendPsnId"))));
        String zhPsnName = sendPerson.getName();
        if (StringUtils.isBlank(zhPsnName)) {
          zhPsnName = sendPerson.getFirstName() + " " + sendPerson.getLastName();
        }

        String enPsnName = sendPerson.getFirstName() + " " + sendPerson.getLastName();
        if (StringUtils.isBlank(sendPerson.getFirstName()) || StringUtils.isBlank(sendPerson.getLastName())) {
          enPsnName = sendPerson.getName();
        }
        map.put("zhPsnName", zhPsnName);
        map.put("enPsnName", enPsnName);
        if ("zh_CN".equals(languageVersion)) {
          psnName = zhPsnName;
        } else if ("en_US".equals(languageVersion)) {
          psnName = enPsnName;
        }
        map.put("psnName", psnName);
        String msgTemplate = "Share_Mail_Title_Template_" + languageVersion + ".ftl";
        map.put("tmpUrl", msgTemplate);
        String subject = this.buildEmailTitle(map);
        subject = subject != null ? subject.replace("\r\n", "") : subject;
        map.put("subject", subject);
        map.put("msgTitle", subject);
        map.put("mailContext", "");
        String mailContext = this.buildEmailTitle(map);
        if (mailContext != null) {
          mailContext = mailContext.replace("\r\n", "");
          map.put("mailContext", mailContext);
        }
        String mailTemplate = "Notify_Share_Template_" + languageVersion + ".ftl";
        map.put("viewUrl", map.get("viewUrl") + "&locale=" + languageVersion);
        map.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
        map.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, targetEmail);
        map.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
        map.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, isVerifyUserId);
        recommendResEmailService.syncEmailInfo(map);
      } else {
        for (String psnId : psnIds.split(",")) {
          // 获取用户设置接收邮件的语言
          Person person = null;
          String email = "";
          if (psnId != null) {
            Long receiverPsnId = Long.valueOf(psnId);
            person = this.personManager.getPerson(receiverPsnId);
            email = person.getEmail();
            if (person != null && person.getEmailLanguageVersion() != null) {
              String psnLanguageSet = person.getEmailLanguageVersion();
              if (psnLanguageSet != null) {
                languageVersion = psnLanguageSet;
              }
            }
          }

          Person sendPerson =
              this.personManager.getPersonById(Long.valueOf(ObjectUtils.toString(map.get("recommendPsnId"))));

          psnName = this.emailCommonService.getPsnNameByEmailLangage(sendPerson, languageVersion);
          map.put("psnName", psnName);
          String msgTemplate = "Share_Mail_Title_Template_" + languageVersion + ".ftl";
          map.put("tmpUrl", msgTemplate);
          String subject = this.buildEmailTitle(map);
          map.put("subject", subject);
          map.put("msgTitle", subject);
          map.put("mailContext", "");
          String mailContext = this.buildEmailTitle(map);
          if (mailContext != null) {
            mailContext = mailContext.replace("\r\n", "");
            map.put("mailContext", mailContext);
          }
          String mailTemplate = "Notify_Share_Template_" + languageVersion + ".ftl";
          map.put("recvName", this.emailCommonService.getPsnNameByEmailLangage(person, languageVersion));// person.getName()
          map.put("viewUrl", map.get("viewUrl") + "&locale=" + languageVersion);
          map.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
          map.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, email);
          map.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
          map.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, person.getPersonId());
          recommendResEmailService.syncEmailInfo(map);
        }
      }
    } catch (Exception e) {
      logger.info("推荐资源邮件", e);
      throw new ServiceException("推荐资源邮件", e);
    }
  }

  @Override
  public void saveSyncReqMessage(ReqMailBox reqMailBox) throws ServiceException {
    if (reqMailBox == null || reqMailBox.getSyncNodePsn() == null) {
      logger.error("同步的站内请求消息实体为空！");
      throw new ServiceException();
    }

    String psnIds = reqMailBox.getSyncNodePsn().get(SecurityUtils.getCurrentAllNodeId().get(0));
    if (StringUtils.isEmpty(psnIds)) {
      logger.error("当前节点不需要同步或同步数据丢失！");
      return;
    }

    // 保存发件箱
    this.syncReqMailBoxDao.save(renewSyncReqMailBox(reqMailBox));

    for (String psnId : psnIds.split(",")) {
      Long personId = new Long(psnId);

      // 保存收件箱信息
      Person person = personManager.getPerson(personId);
      ReqInbox inbox = new ReqInbox(personId, reqMailBox.getMailId(), 0);
      inbox.setPsnName(person.getName());
      inbox.setFirstName(person.getFirstName());
      inbox.setLastName(person.getLastName());
      inbox.setPsnHeadUrl(person.getAvatars());
      reqInboxDao.save(inbox);

    }

  }

  private SyncReqMailBox renewSyncReqMailBox(ReqMailBox reqMailBox) throws ServiceException {

    SyncReqMailBox mail = new SyncReqMailBox();
    mail.setMailId(reqMailBox.getMailId());
    mail.setTitle(reqMailBox.getTitle());
    mail.setSenderId(reqMailBox.getSenderId());
    mail.setPsnName(reqMailBox.getPsnName());
    mail.setFirstName(reqMailBox.getFirstName());
    mail.setLastName(reqMailBox.getLastName());
    mail.setPsnHeadUrl(reqMailBox.getPsnHeadUrl());
    mail.setOptDate(reqMailBox.getOptDate());
    mail.setStatus(reqMailBox.getStatus());

    return mail;

  }

  private SyncShareMailBox renewSyncShareMailBox(ShareMailBox shareMailBox) throws ServiceException {

    SyncShareMailBox mail = new SyncShareMailBox();
    mail.setMailId(shareMailBox.getMailId());
    mail.setTitle(shareMailBox.getTitle());
    mail.setSenderId(shareMailBox.getSenderId());
    mail.setPsnName(shareMailBox.getPsnName());
    mail.setFirstName(shareMailBox.getFirstName());
    mail.setLastName(shareMailBox.getLastName());
    mail.setPsnHeadUrl(shareMailBox.getPsnHeadUrl());
    mail.setOptDate(shareMailBox.getSendDate());
    mail.setStatus(shareMailBox.getStatus());
    mail.setDeadLine(shareMailBox.getDeadLine());
    mail.setZhReceiver(shareMailBox.getZhReceiver());
    mail.setEnReceiver(shareMailBox.getEnReceiver());

    return mail;

  }

  @Override
  public void saveSyncShareMessage(ShareMailBox shareMailBox) throws ServiceException {
    if (shareMailBox == null || shareMailBox.getSyncNodePsn() == null) {
      logger.error("同步的站内请求消息实体为空！");
      throw new ServiceException();
    }

    String psnIds = shareMailBox.getSyncNodePsn().get(SecurityUtils.getCurrentAllNodeId().get(0));
    if (StringUtils.isEmpty(psnIds)) {
      logger.error("当前节点不需要同步或同步数据丢失！");
      return;
    }

    // 保存发件箱
    this.syncShareMailBoxDao.save(renewSyncShareMailBox(shareMailBox));

    for (String psnId : psnIds.split(",")) {
      Long personId = new Long(psnId);

      // 保存收件箱信息
      Person person = personManager.getPerson(personId);
      ShareInbox inbox = new ShareInbox(personId, shareMailBox.getMailId(), 0);
      inbox.setFirstName(person.getFirstName());
      inbox.setLastName(person.getLastName());
      inbox.setPsnName(person.getName());
      inbox.setPsnHeadUrl(person.getAvatars());

      shareInboxDao.save(inbox);

    }

  }

  @Override
  public void sendConfirmForReq(Message message) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public String replyForMail(Message message, Long mailType) throws ServiceException {
    try {
      PsnInsideMailBox mailbox = new PsnInsideMailBox();
      Assert.notNull(message, "message不允许为空！");
      String type = message.getType();

      // 设置发信箱内容
      String content = message.getContent();
      // 站内请求
      if ("inside".equals(type)) {
        // 获取当前用户
        Person currUser = personManager.getPersonById(SecurityUtils.getCurrentUserId());
        mailbox.setSenderId(currUser.getPersonId());
        mailbox.setPsnName(currUser.getName());
        mailbox.setFirstName(currUser.getFirstName());
        mailbox.setLastName(currUser.getLastName());
        mailbox.setOptDate(new Date());
        mailbox.setPsnHeadUrl(currUser.getAvatars());
        mailbox.setStatus(0);
        if (content != null) {
          content = content.trim();
          content = content.replaceAll("<p>", "");
          content = content.replaceAll("</p>", "<br/>");
          content = content.replaceAll("<P>", "");
          content = content.replaceAll("</P>", "<br/>");
          content = content.replaceAll("<a",
              "<a onclick='return false' style='text-decoration:none;color:black;cursor: default' ");
          content = content.replaceAll("<A",
              "<A onclick='return false' style='text-decoration:none;color:black;cursor: default' ");
        }
        /******************** 下面设置并保存收信箱内容 ***************************/
        String receiverIds[] = null;
        if (message.getReceivers() != null) {
          receiverIds = message.getReceivers().split(",");
        }
        List<PsnInsideInbox> newInboxList = new ArrayList<PsnInsideInbox>();
        String zhReceiver = "";
        String enReceiver = "";
        for (int i = 0, length = receiverIds.length; i < length; i++) {
          String id = receiverIds[i];
          // 非法Id，则进行下一个循环
          if (id == null || "".equals(id) || !isNumeric(id)) {
            continue;
          }
          PsnInsideInbox newInbox = new PsnInsideInbox();
          Person sender = personManager.getPersonById(Long.valueOf(id));
          if (i < 10) {
            zhReceiver += "," + sender.getName();
            enReceiver += "," + sender.getFirstName() + " " + sender.getLastName();
          }
          newInbox.setPsnId(sender.getPersonId());
          newInbox.setStatus(0);
          newInbox.setPsnName(sender.getName());
          newInbox.setFirstName(sender.getFirstName());
          newInbox.setLastName(sender.getLastName());
          newInbox.setPsnHeadUrl(sender.getAvatars());
          newInbox.setSenderId(SecurityUtils.getCurrentUserId());
          if (message.getDes3FileId() != null) {// 有附件
            newInbox.setInsideFile(message.getDes3FileId());
          }
          newInboxList.add(newInbox);
          insideReplyEmailService.syncEmailInfo(mailbox, newInbox, sender);
        }
        mailbox.setZhReceiver(zhReceiver);
        mailbox.setEnReceiver(enReceiver);

        // 保存发信箱
        this.psnInsideMailBoxDao.save(mailbox);

        // 保存发件箱记录内容_MJG_SCM-5910.
        InsideMailBoxCon insideMailCon = new InsideMailBoxCon();
        insideMailCon.setMailId(mailbox.getMailId());
        if (message.getTitle() != null && message.getTitle().toLowerCase().indexOf("re:") == -1) {
          insideMailCon.setTitleZh("Re:" + message.getTitle());
        } else {
          insideMailCon.setTitleZh(message.getTitle());
        }
        // insideMailCon.setTitleEn(StringUtils.trimToEmpty(ObjectUtils.toString(map.get("enTitle"))));
        // insideMailCon.setExtOtherInfo(ObjectUtils.toString(map.get("extOtherInfo")));
        insideMailCon.setContent(content);
        insideMailBoxConDao.save(insideMailCon);

        // 保存收件箱
        for (PsnInsideInbox insideInbox : newInboxList) {
          insideInbox.setMailBox(mailbox);
          psnInsideInboxDao.save(insideInbox);
        }
        Map<Integer, String> psnNodes = sortPsnIdsByNodeId(message.getReceivers());
        if (psnNodes.size() > 0) {
          // 判断map结构是否有除本节点以外的人
          /*
           * // TODO:同步短消息节点 mailbox.setSyncNodePsn(psnNodes); InsideMailBox mail = new InsideMailBox();
           * mail.setMsgType(3);// 3 消息回复 mail.setMailId(mailbox.getMailId());
           * mail.setSenderId(mailbox.getSenderId()); mail.setTitle(mailbox.getTitle());
           * mail.setContent(mailbox.getContent()); mail.setPsnName(mailbox.getPsnName());
           * mail.setFirstName(mailbox.getFirstName()); mail.setLastName(mailbox.getLastName());
           * mail.setPsnHeadUrl(mailbox.getPsnHeadUrl()); mail.setOptDate(mailbox.getOptDate());
           * mail.setStatus(0); mail.setZhReceiver(zhReceiver); mail.setEnReceiver(enReceiver);
           * mail.setReplyPsnId(SecurityUtils.getCurrentUserId()); String msg = mailbox.getRequestMessage();
           * if (msg != null) { msg = msg.replaceAll("\\n", "<br/>"); mail.setRequestMessage(msg); }
           * mail.setSyncNodePsn(psnNodes); if (message.getDes3FileId() != null) { String des3FileId =
           * message.getDes3FileId();// 文件Id，由“fileId,加密的fileId”组成 mail.setInsideFile(des3FileId); String[]
           * CollFileId = des3FileId.split(","); if (CollFileId.length == 2) { Long fileId =
           * Long.valueOf(des3FileId.split(",")[0]); ArchiveFiles af =
           * archiveFilesService.getArchiveFiles(fileId);// 取得本地的文件，以同步到远处服务器 mail.setArchiveFiles(af); } } //
           * snsSyncMessageProducer.syncInsideMsgToSns(mail, // SecurityUtils.getCurrentAllNodeId().get(0));
           */}

      }
      return mailbox.getContent();

    } catch (Exception e) {

      logger.error("邮件回复失败！", e);
      throw new ServiceException();

    }

  }

  @Override
  public PsnInsideMailBox loadForwardMessage(Message message) throws Exception {
    PsnInsideMailBox mailBox = null;
    if (StringUtils.isNotEmpty(message.getRecvId())) {

      PsnInsideInbox inbox =
          this.psnInsideInboxDao.get(Long.parseLong(ServiceUtil.decodeFromDes3(message.getRecvId())));
      mailBox = inbox != null ? inbox.getMailBox() : null;

    }

    if (StringUtils.isNotEmpty(message.getMailId())) {

      mailBox = this.psnInsideMailBoxDao.get(Long.parseLong(ServiceUtil.decodeFromDes3(message.getMailId())));
    }

    if (mailBox != null) {
      mailBox = this.rebuildPsnInsideMailBox(mailBox);
    }
    if (StringUtils.isNotEmpty(message.getInsideFile()) && mailBox != null) {// 如果存在附件
      String[] collFileIds = message.getInsideFile().split(",");// insideFile的存放格式为："文件Id,加密的文件Id"
      if (collFileIds.length == 2) {
        ArchiveFile af = this.archiveFilesService.getArchiveFile(Long.valueOf(collFileIds[0]));
        mailBox.setInsideFileName(af.getFileName());
        mailBox.setInsideFileId(String.valueOf(af.getFileId()));
        mailBox.setInsideDes3FileId(af.getDes3FileId());
        mailBox.setNodeId(af.getNodeId());
      }
    }
    return mailBox;
  }

  @Override
  public boolean isUserRefuseMsg(Long sender, Long psnId) throws ServiceException {
    boolean refuse = true;

    try {
      PrivacySettings ps = this.privacySettingsDao.loadPsByPsnId(psnId, PrivacyType.SEND_MSG);
      if (ps == null || ps.getPermission().equals(0)) {

        refuse = false;
      } else if (ps.getPermission().equals(1)) {
        refuse = this.friendDao.getFriendId(psnId, sender) != null ? false : true;
      } else if (ps.getPermission().equals(2)) {
        refuse = true;

      }

    } catch (Exception e) {

      logger.error("查询用户短消息配置失败！", e);
      throw new ServiceException(e);
    }
    return refuse;
  }

  @Override
  public boolean cancelSyncMessage(Long sender, String psnIds) throws ServiceException {

    try {
      boolean flag = true;
      List<PrivacySettings> psList = this.privacySettingsDao.getPsnsPs(psnIds, PrivacyType.SEND_MSG);
      if (psList == null) {

        return false;
      }
      for (PrivacySettings ps : psList) {

        if (ps == null || ps.getPermission().equals(0)) {

          flag = false;
          break;
        } else if (ps.getPermission().equals(1)) {
          flag = this.friendDao.getFriendId(ps.getPk().getPsnId(), sender) != null ? false : true;
          if (!flag) {
            break;
          }
        } else if (ps.getPermission().equals(2)) {
          flag = true;

        }

      }
      return flag;

    } catch (Exception e) {
      logger.error("查询用户短消息权限配置失败！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public String getInboxMsgTotalForNew(Long psnId) throws ServiceException {
    try {
      JSONObject obj = new JSONObject();
      obj.accumulate("insideMsgTotal", "\"" + insideInboxDao.getUnreadInsideMsg(psnId) + "\"");
      obj.accumulate("inviteMsgTotal", "\"" + psnInviteInboxDao.getUnreadInviteMsg(psnId) + "\"");
      obj.accumulate("ftrequestMsgTotal", "\"" + fullTextInboxDao.getUnReadCount(psnId) + "\"");
      obj.accumulate("shareMsgTotal", "\"" + psnShareInboxDao.getUnreadShareMsg(psnId) + "\"");
      obj.accumulate("noticeMsgTotal", "\"" + psnMsgNoticeInBoxDao.getUnReadMsgNoticeCount(psnId) + "\"");
      obj.accumulate("insideMsgAll", "\"" + psnInsideInboxDao.getInsideMsg(psnId) + "\"");
      obj.accumulate("inviteMsgAll", "\"" + psnInviteInboxDao.getInviteMsg(psnId) + "\"");
      obj.accumulate("ftrequestMsgAll", "\"" + fullTextInboxDao.getTotalCount(psnId) + "\"");
      obj.accumulate("shareMsgAll", "\"" + psnShareInboxDao.getShareMsg(psnId) + "\"");
      obj.accumulate("noticeMsgAll", "\"" + psnMsgNoticeInBoxDao.getMsgNoticeCount(psnId) + "\"");
      return obj.toString();

    } catch (Exception e) {

      logger.error("获取收件箱统计信息失败！", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 获取消息提示信息.
   * 
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Integer> getInboxForTip() throws ServiceException {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    String cacheKey = MessageConstant.MSG_TIP_CACHE_KEY + currentPsnId;
    try {
      HashMap<String, Integer> map =
          (HashMap<String, Integer>) cacheService.get(MessageConstant.MSG_TIP_CACHE_NAME, cacheKey);
      if (map == null) {
        map = new HashMap<String, Integer>();
        int insideMsgTotal = Integer.valueOf(psnInsideInboxDao.getUnreadInsideMsg(currentPsnId).toString());
        int inviteMsgTotal = Integer.valueOf(psnInviteInboxDao.getUnreadInviteMsg(currentPsnId).toString());
        int ftRequestMsgTotal = fullTextInboxDao.getUnReadCount(currentPsnId);
        int shareMsgTotal = Integer.valueOf(psnShareInboxDao.getUnreadShareMsg(currentPsnId).toString());
        int noticeMsgTotal = Integer.valueOf(psnMsgNoticeInBoxDao.getUnReadMsgNoticeCount(currentPsnId).toString());
        int statePeddingCfmTotal = psnStatisticsService.getPsnPendingConfirmPubNum(currentPsnId);
        int rcmdPubFulltextCount = Integer.valueOf(this.pubFulltextPsnRcmdService.getRcmdFulltextCount().toString());
        int total = insideMsgTotal + inviteMsgTotal + ftRequestMsgTotal + shareMsgTotal + noticeMsgTotal;
        map.put("total", total);
        map.put("insideMsgTotal", insideMsgTotal);
        map.put("inviteMsgTotal", inviteMsgTotal);
        map.put("ftRequestMsgTotal", ftRequestMsgTotal);
        map.put("shareMsgTotal", shareMsgTotal);
        map.put("noticeMsgTotal", noticeMsgTotal);
        map.put("statePeddingCfmTotal", statePeddingCfmTotal);
        map.put("rcmdPubFulltextCount", rcmdPubFulltextCount);
        cacheService.put(MessageConstant.MSG_TIP_CACHE_NAME, CacheService.EXP_HOUR_3, cacheKey, map);
      }
      return map;
    } catch (DaoException e) {
      logger.error("获取消息提示信息时出错啦！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getInboxMsgTotal(String type) throws ServiceException {

    try {
      // 短信inside_inbox
      // 邀请invite_inbox
      // 请求req_inbox
      // 推荐share_inbox
      // 通知msg_notice_in_box
      Long psnId = SecurityUtils.getCurrentUserId();
      if ("inside".equals(type)) { // 站内请求

        return this.getMessageCount(psnId, "inside_inbox");

      } else if ("invite".equals(type)) { // 站内邀请

        return this.getMessageCount(psnId, "invite_inbox");

      } else if ("req".equals(type)) { // 站内请求
        return this.getMessageCount(psnId, "req_inbox");

      } else if ("share".equals(type)) {// 站内推荐

        return this.getMessageCount(psnId, "share_inbox");

      } else if ("msgNoticeStyle".equals(type)) {// 站内通知
        return this.getMessageCount(psnId, "msg_notice_in_box");
      }

    } catch (Exception e) {

      logger.error("统计{}收件箱失败！", type, e);

    }

    return null;
  }

  @Override
  public void genInviteAction(PersonRegister person) throws ServiceException {

    if (StringUtils.isEmpty(person.getDes3InviteId())) {
      logger.error("邀请码为通过验证，非法的请求操作!");
      return;
    }

    String key = ServiceUtil.decodeFromDes3(person.getKey());
    if ("frd_invite".equalsIgnoreCase(key)) {

      genFrdInviteAction(person);

    } else if ("group_invite".equalsIgnoreCase(key)) {
      genGroupInviteAction(person);
    }

  }

  private void genFrdInviteAction(PersonRegister person) throws ServiceException {
    Long psnId = null;
    Long inviteId = null;
    try {
      psnId = new Long(ServiceUtil.decodeFromDes3(person.getDes3PsnId()));
      inviteId = new Long(ServiceUtil.decodeFromDes3(person.getDes3InviteId()));
      person.setPersonId(userService.findIdByLoginName(person.getEmail()));
    } catch (Exception e) {
      logger.info("genFrdInviteAction", e);
      throw new ServiceException("genFrdInviteAction", e);
    }
    if (!friendService.checkInviteIsValid(psnId, inviteId)) {
      logger.error("邀请链接失效！");
      throw new ServiceException();
    }

    friendService.updateFriendInvite(inviteId, person.getPersonId());
    saveFrdReqInbox(inviteId, psnId);
  }

  private void genGroupInviteAction(PersonRegister person) throws ServiceException {

    Long inviteId = new Long(ServiceUtil.decodeFromDes3(person.getDes3InviteId()));
    GroupInvite groupInvite = groupInviteDao.get(inviteId);
    if (this.groupService.checkInviteIsValid(inviteId)) {

      logger.error("邀请链接失效!");
      throw new ServiceException();
    }
    try {
      person.setPersonId(userService.findIdByLoginName(person.getEmail()));
      groupInvite.setPsnId(person.getPersonId());
      groupInvite.setEmail(person.getEmail());
      groupInviteDao.save(groupInvite);
      // 发送邀请
      saveGroupReqInbox(inviteId);
    } catch (Exception e) {
      logger.info("genGroupInviteAction", e);
      throw new ServiceException("genGroupInviteAction", e);
    }
  }

  @Override
  public void saveFrdInviteForLogin(Long inviteId, Long psnId) throws ServiceException {
    // TODO
  }

  @Override
  public void saveGroupInviteForLogin(Long inviteId, Integer nodeId) throws ServiceException {
    // TODO
  }

  /**
   * 群组邀请邮件.
   * 
   * @修改重写了发送群组邀请邮件的方法代码(处理SCM-1037:2012-11-02;SCM-1415:2012-12-25;SCM-1802:2013-02-25).
   */
  @Override
  public void sendInviteAttendGroupMail(Map<String, ?> ctxMap, Long mailType) throws ServiceException {
    if (ctxMap == null) {
      logger.error("发送群组邀请邮件缺少必要参数！");
      throw new ServiceException();
    }
    String subject = null;// 邮件邀请主题.
    String mailTemplate = null;// 邮件模板.
    String groupUrl = null;// 群组名的链接地址.
    String psnName = null;// 发出邀请的人的姓名.
    String inviteUrl = null;// 可点击的链接地址(不带参数).
    String inviteUrlParams = null;// 链接地址的参数.
    Map<String, Object> hashMap = new HashMap<String, Object>();
    Person invitePsn = null;// 被邀请的人.
    String mailId = null;
    String inviteInId = null;
    String operateUrl = null;// 完整的链接地址.
    try {
      String locale = ObjectUtils.toString(ctxMap.get("locale"));
      String languageVersion = locale;// 接收邮件的语言.
      if (StringUtils.isBlank(languageVersion)) {
        languageVersion = LocaleContextHolder.getLocale().toString();
      }
      Long inviteId = Long.valueOf(ctxMap.get("inviteId").toString());

      GroupInvite groupInvite = groupService.getGroupInviteById(inviteId);// 群组邀请邮件记录.
      GroupPsn groupPsn = groupService.getGroupPsnInfo(groupInvite.getGroupId());// 群组.
      Person sendPsn = personManager.getPerson(groupInvite.getSendPsnId());// 发出邀请的人.

      mailId = ctxMap.get("mailId").toString();// 邀请发件箱的主键ID.
      inviteInId = ctxMap.get("inviteInId").toString();// 邀请收件箱的主键ID.
      // 获取群组名的链接地址.
      groupUrl = genGroupHomeUrl(groupPsn.getGroupId()) + "&locale=" + languageVersion;
      String key = java.net.URLEncoder.encode(ServiceUtil.encodeToDes3("group_invite"), "UTF-8");
      // 获取请求地址参数(key-邀请类型;des3InviteId-邀请记录主键;des3NodeId-当前用户节点;des3mailId-发件箱ID;des3inboxId-收件箱ID;locale-语言版本).
      inviteUrlParams = "key=" + key + "&des3InviteId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(inviteId.toString()), "UTF-8") + "&des3NodeId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(SecurityUtils.getCurrentAllNodeId().get(0).toString()),
              "UTF-8")
          + "&des3mailId=" + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(mailId), "UTF-8") + "&des3inboxId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(inviteInId), "UTF-8") + "&locale=" + languageVersion;
      if (groupInvite.getPsnId() != null) { // 站内邀请(人员ID不为空且其邮箱已验证).
        Long psnId = groupInvite.getPsnId();

        invitePsn = personManager.getPersonById(psnId);// 被邀请的人.

        // 获取用户设置接收邮件的语言.
        if (invitePsn != null && StringUtils.isNotBlank(invitePsn.getEmailLanguageVersion())
            && !languageVersion.equals(invitePsn.getEmailLanguageVersion())) {
          languageVersion = invitePsn.getEmailLanguageVersion();
        }
        // 获取发出邀请的人的姓名.
        psnName = this.emailCommonService.getPsnNameByEmailLangage(sendPsn, languageVersion);

        if (Locale.CHINA.toString().equals(languageVersion)) {
          subject = psnName + "邀请您加入群组";
        } else {
          subject = psnName + " has invited you to join group";
        }
        // 获取邮件模版.
        mailTemplate = "Invite_Attend_Group_Template_" + languageVersion + ".ftl";
        inviteUrl = sysDomainConst.getSnsDomain() + "/scmwebsns/msgbox/requestMain?";

        hashMap.put("invitePsnId", groupInvite.getPsnId());
        hashMap.put("invitePsnName", this.emailCommonService.getPsnNameByEmailLangage(invitePsn, languageVersion));
        // 设置连接地址为可自动登录.
        String autoLoginUrl = ObjectUtils.toString(ctxMap.get("autoLoginUrl"));// 请求地址的自动登录前缀部分.
        if (StringUtils.isNotBlank(autoLoginUrl)) {
          String groupOperateUrl = inviteUrl + inviteUrlParams;// 实际请求地址部分.
          operateUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(groupOperateUrl, "utf-8");
        }
      } else { // 其他邀请(被邀请人员邮箱未验证或不是系统用户)
        mailTemplate = "Invite_Outside_Attend_Group_Template_" + languageVersion + ".ftl";
        // 设置请求路径.
        String systemUrl = "https://" + sysDomainConst.getSnsDomain();
        if (sysDomainConst.getSnsContext() != null) {
          systemUrl = systemUrl + "/" + sysDomainConst.getSnsContext();
        }
        String isSysUser = ObjectUtils.toString(ctxMap.get("isSysUser"));
        if (StringUtils.isNotBlank(isSysUser) && "true".equalsIgnoreCase(isSysUser)) {// 被邀请人员已注册系统.
          String targetUrl = systemUrl + "/invite/groupInvite.action?";
          operateUrl = targetUrl + inviteUrlParams;// 获取完整的请求地址.
        } else {// 系统外人员.
          operateUrl = systemUrl + "/register/register?key=" + key + "&forward="
              + java.net.URLEncoder.encode("/invite/groupInvite.action?" + inviteUrlParams, "utf-8");
        }
        psnName = this.personManager.getPsnName(sendPsn);
        if (Locale.CHINA.equals(LocaleContextHolder.getLocale())) {
          subject = psnName + "邀请您加入群组";
        } else {
          subject = psnName + " has invited you to join group";
        }
      }
      hashMap.put("inviteUrl", operateUrl);
      hashMap.put("name", ctxMap.get("name"));
      hashMap.put("groupUrl", groupUrl);
      hashMap.put("groupName", groupPsn.getGroupName());
      hashMap.put("psnName", psnName);
      hashMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
      hashMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, groupInvite.getEmail());
      hashMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
      hashMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, groupInvite.getPsnId());
      groupInviteEmailService.syncEmailInfo(hashMap);
    } catch (Exception e) {
      logger.error("发送群组邀请邮件失败！", e);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void sendOffLineMessageForShare(HashMap<String, String> map) throws ServiceException {
    if (map.size() < 4) {

      logger.error("map缺少必要的参数，请检查！");
      throw new ServiceException();
    }
    Long sendPsnId = MapUtils.getLong(map, "sendPsnId");
    if (sendPsnId == null) {

      logger.error("发件人PsnId为空!");
      throw new ServiceException();
    }
    ShareMailBox shareMail = new ShareMailBox();

    Person person = personManager.getPerson(sendPsnId);
    map.put("psnName", personManager.getPsnName(person));
    String msgTemplate = "Share_Msg_Template_" + LocaleContextHolder.getLocale().toString() + ".ftl";
    map.put("tmpUrl", msgTemplate);
    map.put("title", buildTitle(map));
    shareMail.setTitle(buildTitle(map));
    shareMail.setSenderId(person.getPersonId());
    shareMail.setFirstName(person.getFirstName());
    shareMail.setLastName(person.getLastName());
    shareMail.setPsnName(person.getName());
    shareMail.setPsnHeadUrl(person.getAvatars());
    shareMail.setSendDate(new Date());
    shareMail.setDeadLine(new Date(map.get("deadLine")));
    shareMail.setStatus(0);

    String psnIds = map.get("receivers");
    Map<Integer, String> psnNodes = sortPsnIdsByNodeId(psnIds);

    List<ShareInbox> shareInboxList = new ArrayList<ShareInbox>();
    String[] psnIdArray = psnIds.split(",");
    for (int i = 0, length = psnIdArray.length; i < length; i++) {
      String psnId = psnIdArray[i];
      String personIds = "," + psnNodes.get(SecurityUtils.getCurrentAllNodeId().get(0)) + ",";
      Person recvPerson = personManager.getPerson(new Long(psnId));
      ShareInbox shareInbox = new ShareInbox();
      shareInbox.setPsnId(Long.parseLong(psnId));
      shareInbox.setStatus(0);
      if (personIds.indexOf("," + psnId + ",") == -1) { // 非本节点收件人
        shareInbox.setStatus(2);
      }
      shareInbox.setPsnHeadUrl(recvPerson.getAvatars());
      shareInbox.setPsnName(recvPerson.getName());
      shareInbox.setFirstName(recvPerson.getFirstName());
      shareInbox.setLastName(recvPerson.getLastName());
      shareInboxList.add(shareInbox);
    }

    syncShareMail(shareMail);
    for (ShareInbox shareInboxOut : shareInboxList) {
      shareInboxOut.setMailId(shareMail.getMailId());
      shareInboxDao.save(shareInboxOut);
    }

    psnNodes.remove(SecurityUtils.getCurrentAllNodeId().get(0));
    if (psnNodes.size() > 0) {// 判断map结构是否有除本节点以外的人
      // TODO:同步共享消息
      shareMail.setSyncNodePsn(psnNodes);
      snsSyncMessageProducer.syncShareMsgToSns(shareMail, SecurityUtils.getCurrentAllNodeId().get(0));
    }

  }

  @Override
  public void syncShareMail(ShareMailBox shareMail) throws ServiceException {

    shareMailBoxDao.save(shareMail);

  }

  @Override
  public void sendApproveForAttendGroup(HashMap<String, ?> ctxMap) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendConfirmMsgForAttendGroup(HashMap<String, ?> ctxMap) throws ServiceException {
    // Message message = new Message();
    try {
      String msgTemplate = "Confirm_Attend_Group_Msg_Template_" + LocaleContextHolder.getLocale().toString() + ".ftl";

      Long inviteId = MapUtils.getLong(ctxMap, "inviteId");
      HashMap<String, String> hashMap = new HashMap<String, String>();
      GroupInvite groupInvite = groupService.getGroupInviteById(inviteId);
      Long sendPsnId = groupInvite.getSendPsnId();
      Long groupId = groupInvite.getGroupId();
      GroupPsn groupPsn = groupPsnSearchService.getGroupBaseInfo(groupId);// groupPsnDao.get(groupId);
      String groupName = groupPsn.getGroupName();
      String psnUrl = getPersonResumeUrl(false, sendPsnId);
      // message.setGroupName(groupName);
      if (groupName.length() > 15) {
        groupName = groupName.substring(0, 16);
      }
      String groupUrl = genGroupUrl(groupPsn.getGroupId());
      hashMap.put("groupUrl", groupUrl);
      hashMap.put("groupName", groupName);
      hashMap.put("psnUrl", psnUrl);
      hashMap.put("tmpUrl", msgTemplate);
      // SyncPerson syncPerson = psnManager.getSyncPerson(sendPsnId);
      // message.setPsnFirstName(syncPerson.getPsnFirstName());
      // message.setPsnLastName(syncPerson.getPsnLastName());
      // message.setPsnName(syncPerson.getPsnName());
      // message.setSendPsnId(sendPsnId);
      hashMap.put("psnName", personManager.getPsnName(sendPsnId, null));
      // message.setTitle(buildTitle(hashMap));
      hashMap.remove("msgTitle");
      // message.setContent(build(hashMap));
      // message.setReceivers(groupInvite.getPsnId().toString());
      // message.setMsgType(2);
      // sendInsideMessageForSys(message);

    } catch (Exception e) {

      logger.error("加入群组消息发送失败!", e);
    }

  }

  @Override
  public void sendConfrimMailForAttendGroup(HashMap<String, ?> ctxMap) throws ServiceException {
    // TODO Auto-generated method stub

  }

  private String genGroupHomeUrl(Long groupId) throws ServiceException {
    try {
      String baseUrl = "https://" + sysDomainConst.getSnsDomain();
      if (sysDomainConst.getSnsContext() != null) {
        baseUrl = baseUrl + "/" + sysDomainConst.getSnsContext();
      }
      return baseUrl + "/hp/view?des3GroupId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(groupId.toString()), "UTF-8") + "&des3NodeId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(SecurityUtils.getCurrentAllNodeId().get(0).toString()),
              "UTF-8");
    } catch (Exception e) {

      logger.error("转换群组URL出现异常！", e);
    }

    return null;
  }

  private String genGroupUrl(Long groupId) throws ServiceException {
    try {
      String baseUrl = "https://" + sysDomainConst.getSnsDomain();
      if (sysDomainConst.getSnsContext() != null) {
        baseUrl = baseUrl + "/" + sysDomainConst.getSnsContext();
      }
      return baseUrl + "/group/view?menuId=31&des3GroupId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(groupId.toString()), "UTF-8") + "&des3GroupNodeId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(SecurityUtils.getCurrentAllNodeId().get(0).toString()),
              "UTF-8");
    } catch (Exception e) {

      logger.error("转换群组URL出现异常！", e);
    }

    return null;
  }

  private Integer readReqAddFrdStatus(Long sendPsnId, Long recvPsnId) throws ServiceException {

    PrivacySettings ps;
    try {
      ps = this.privacySettingsDao.loadPsByPsnId(recvPsnId, PrivacyType.ADD_FRD);

      return ps == null ? null : ps.getPermission();
    } catch (DaoException e) {
      // TODO Auto-generated catch block

      logger.error("读取配置信息失败！", e);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings("deprecation")
  @Override
  public void sendMailForSharePub(Map<String, Object> map) throws ServiceException {

    try {
      if (!(map.containsKey("subject") && map.containsKey("content") && map.containsKey("receivers"))) {

        logger.error("map缺少必要的参数，请检查！");
        throw new ServiceException();

      }

      Person sendPerson = personManager.getPerson(SecurityUtils.getCurrentUserId());
      String psnIds = ObjectUtils.toString(map.get("receivers"));
      String languageVersion = null;
      String subject = null;
      String mailTemplate = null;
      String encodePwd = null;
      String loginUrl = null;
      // zzb

      GroupPsnNode groupPsnNode = groupService.findGroup(Long.parseLong(ObjectUtils.toString(map.get("gId"))));
      // zzb
      for (String strPsnId : psnIds.split(",")) {
        Long psnId = NumberUtils.toLong(StringUtils.trim(strPsnId));
        if (psnId <= 0) {
          throw new Exception("psnId=" + psnId + "，系统不存在此用户");
        }
        Person person = this.personManager.getPerson(psnId);
        if (person != null && person.getEmailLanguageVersion() != null) {
          languageVersion = person.getEmailLanguageVersion();

        }

        if (languageVersion == null) {
          languageVersion = LocaleContextHolder.getLocale().toString();
        }

        encodePwd = "CITE|" + psnId.toString();
        // 链接地址的自动登录部分.
        String autoLoginUrl = ObjectUtils.toString(map.get("casUrl")) + "?submit=true&username=SHARE&password="
            + URLEncoder.encode(EncryptionUtils.encrypt("111111222222333333444444", encodePwd), "utf8");
        String targetUrl = null;// 链接地址的跳转地址部分.
        // 系统地址.
        String sysUrl = "https://" + sysDomainConst.getSnsDomain();
        if (sysDomainConst.getSnsContext() != null) {
          sysUrl = sysUrl + "/" + sysDomainConst.getSnsContext();
        }
        String invitor = this.emailCommonService.getPsnNameByEmailLangage(sendPerson, languageVersion);
        if ("11".equals(ObjectUtils.toString(map.get("msgType")).trim())) {
          if (Locale.CHINA.toString().equals(languageVersion)) {
            subject = invitor + "邀请您为群组" + "\"" + map.get("groupName") + "\"" + "添加成果";

          } else {
            subject =
                invitor + " has invited you to add publications(s) for group  " + "\"" + map.get("groupName") + "\"";
          }
          mailTemplate = "Group_Member_Share_Pub_" + languageVersion + ".ftl";
          // 链接地址的跳转地址部分.
          targetUrl = sysUrl + "/group/pub?menuId=31&sharePub=true&isMailReq=true&groupPsn.des3GroupId="
              + java.net.URLEncoder.encode(groupPsnNode.getDes3GroupId(), "UTF-8") + "&groupPsn.des3GroupNodeId="
              + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(groupPsnNode.getNodeId().toString()), "UTF-8");
        } else if ("12".equals(ObjectUtils.toString(map.get("msgType")).trim())) {
          if (Locale.CHINA.toString().equals(languageVersion)) {
            subject = invitor + "邀请您为群组" + "\"" + map.get("groupName") + "\"" + "添加文件";

          } else {
            subject = invitor + " has invited you to add file(s) for group  " + "\"" + map.get("groupName") + "\"";
          }
          mailTemplate = "Group_Member_Share_File_" + languageVersion + ".ftl";
          // 链接地址的跳转地址部分.
          targetUrl = sysUrl + "/group/file?menuId=31&shareFile=true&isMailReq=true&groupPsn.des3GroupId="
              + java.net.URLEncoder.encode(groupPsnNode.getDes3GroupId(), "UTF-8") + "&groupPsn.des3GroupNodeId="
              + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(groupPsnNode.getNodeId().toString()), "UTF-8");
        }
        // 完整链接地址.
        loginUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(targetUrl, "UTF-8");
        String email = person.getEmail();
        map.put("sendName", this.emailCommonService.getPsnNameByEmailLangage(sendPerson, languageVersion));
        map.put("recvName", this.emailCommonService.getPsnNameByEmailLangage(person, languageVersion));
        map.put("loginUrl", loginUrl);
        map.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
        map.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, email);
        map.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
        map.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, person.getPersonId());
        groupSharePubEmailService.syncEmailInfo(map);
      }
    } catch (Exception e) {
      logger.error("发送邮件邀请群组成员共享成果失败！", e);
    }
  }

  /**
   * 同步站内通知.
   * 
   * @author yangpeihai
   */
  @Override
  public void saveSynNoticeMessage(MessageNoticeOutBox notice) throws ServiceException {
    if (notice == null || notice.getSyncNodePsn() == null) {
      logger.error("同步的通知实体为空！");
      throw new ServiceException();
    }

    String psnIds = notice.getSyncNodePsn().get(SecurityUtils.getCurrentAllNodeId().get(0));
    if (StringUtils.isEmpty(psnIds)) {
      logger.error("当前节点不需要同步或同步数据丢失！");
      return;
    }
    notice.setNoticeId(null);
    // 保存发件箱
    this.msgNoticeOutBoxDao.save(notice);

    for (String psnId : psnIds.split(",")) {
      Long personId = new Long(psnId);
      // 保存收件箱信息
      MessageNoticeInBox inbox = new MessageNoticeInBox(notice.getNoticeId(), 0);
      Person person = personManager.getPerson(personId);
      inbox.setPsnName(person.getName());
      inbox.setLastName(person.getLastName());
      inbox.setFirstName(person.getLastName());
      inbox.setPsnHeadUrl(person.getAvatars());
      inbox.setSenderId(person.getPersonId());
      inbox.setStatus(0);
      this.msgNoticeInBoxDao.save(inbox);
    }

  }

  @Override
  public Map<String, Long> getMessageCount(String ids, Long psnId) throws ServiceException {

    try {
      Map<String, Long> map = new HashMap<String, Long>();
      // 短信inside_inbox
      Long count = this.insideInboxDao.getUnreadInsideMsg(psnId);
      map.put("inside_inbox", count);
      // 邀请invite_inbox
      count = this.inviteInboxDao.getUnreadInviteMsg(psnId);
      map.put("invite_inbox", count);
      // 请求req_inbox
      count = this.reqInboxDao.getUnreadReqMsg(psnId);
      map.put("req_inbox", count);
      // 推荐share_inbox
      count = this.shareInboxDao.getUnreadShareMsg(psnId);
      map.put("share_inbox", count);
      // 通知msg_notice_in_box
      count = this.msgNoticeInBoxDao.getUnReadNoticeNumThenUpdateToReaded(ids, psnId);
      map.put("msg_notice_in_box", count);
      // element = new Element(cacheKey, map);
      // this.messageCache.put(element);
      return map;
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Long refreshMessageCount(Long psnId, String type) throws ServiceException {

    Map<String, Long> map = this.getMessageCount("", psnId);
    return map.get(type);
  }

  // 生成系统通知标题
  @SuppressWarnings("unused")
  private String buildSysTitle(Map<String, String> ctxMap) throws ServiceException {

    return null;
  }

  // 生成系统通知正文
  @SuppressWarnings("unused")
  private String buildSysContent(Map<String, String> ctxMap) throws ServiceException {

    return null;
  }

  @Override
  public Long getMessageCount(Long psnId, String type) throws ServiceException {
    // 每次调用，都重新统计该类别的数据，因为更新的点太多了
    return this.refreshMessageCount(psnId, type);
  }

  @Override
  public InsideInbox findInsideRecvMsg(Long mailId, Long psnId) throws ServiceException {
    try {
      return this.insideInboxDao.find(mailId, psnId);
    } catch (Exception e) {

      logger.error("查询mailId={},psnId={}的insideInbox失败", new Object[] {mailId, psnId, e});
      return null;
    }
  }

  @Override
  public void sendArrovalToMem(Map<String, Object> ctxMap) {
    ctxMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, "Request_Join_Group_Appr_Template_" + ctxMap.get("locale") + ".ftl");
    groupEmailService.syncEmailInfo(ctxMap);
  }

  @Override
  public void sendArrovalToMan(Map<String, Object> ctxMap) {
    ctxMap.put(EmailConstants.EMAIL_TEMPLATE_KEY,
        "Request_Join_Group_Appr_Manager_Template_" + ctxMap.get("locale") + ".ftl");
    groupEmailService.syncEmailInfo(ctxMap);
  }

  /**
   * 添加邮件接收设置链接
   */
  @Override
  public void sendMail(String sendTo, String subject, String ftlTemplate, HashMap<String, Object> context, Long psnId)
      throws ServiceException {
    context.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
    context.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, sendTo);
    context.put(EmailConstants.EMAIL_TEMPLATE_KEY, ftlTemplate);
    context.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, psnId);
    etemplateEmailService.syncEmailInfo(context);
  }

  /**
   * 站内短信发件箱.
   * 
   * @param mailBox
   * @return
   */
  private PsnInsideMailBox rebuildPsnInsideMailBox(PsnInsideMailBox mailBox) {
    if (mailBox != null) {
      InsideMailBoxCon mailboxCon = insideMailboxConDao.getMailBoxCon(mailBox.getMailId());
      if (mailboxCon != null) {
        mailBox.setTitle(mailboxCon.getTitleZh());
        mailBox.setEnTitle(mailboxCon.getTitleEn());
        mailBox.setExtOtherInfo(mailboxCon.getExtOtherInfo());
        mailBox.setContent(mailboxCon.getContent());
      }
    }
    return mailBox;
  }

}
