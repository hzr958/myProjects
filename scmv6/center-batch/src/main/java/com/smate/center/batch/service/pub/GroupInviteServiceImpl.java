package com.smate.center.batch.service.pub;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.friend.GroupInviteDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.pub.GroupInvite;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.Message;
import com.smate.center.batch.service.emailsimplify.EmailSimplify;
import com.smate.center.batch.service.mail.InvitationBusinessService;
import com.smate.center.batch.service.pub.mq.MessageService;
import com.smate.center.batch.util.pub.DynamicConstant;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

import freemarker.template.TemplateException;

/**
 * 群组普通成员邀请和请求加入群组的业务逻辑处理实现类.
 * 
 * @see 本实现仅包含成员邀请或加入群组的主业务逻辑 ；其他逻辑处理拆离到子服务类GroupInviteStaticMethod类中；常量拆离到接口类中。以便维护.
 * 
 * @author maojianguo
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("groupInviteService")
public class GroupInviteServiceImpl implements GroupInviteService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupInviteStaticMethod inviteStaticMethod;
  @Autowired
  private GroupAdminInviteService groupAdminInviteService;
  @Autowired
  private GroupMemberService groupMemberService;
  @Autowired
  private MessageService messageService;
  @Autowired
  private InvitationBusinessService invitBusinessService;
  @Autowired
  private GroupInviteDao groupInviteDao;

  @Autowired
  private EmailSimplify reqJoinGroupEmailService;

  @Autowired
  private EmailSimplify groupInviteMsgEmailService;

  /**
   * 将邀请加入群组的成员列表的参数加以完善：增加查询成员的ID和email(将此方法实现逻辑封装入getInvitePsnList()方法_2013-
   * 03-01_应SCM-1894功能改造实现需要).
   * 
   * @param inviteList 邀请参数列表.
   * @return 匹配到信息的人员列表.
   */
  @Override
  public List<Map<String, String>> getInviteInfoList(List<Map<String, String>> inviteList) {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("isVerify", "true");
    List<Map<String, String>> resultList = groupMemberService.getInvitePsnList(inviteList, paramMap);
    return resultList;
  }

  /**
   * 群组成员-发送邀请信息功能响应的业务逻辑.
   */
  @Override
  public void toSendInviteMsg(List<Map<String, String>> list, String inviteTitle, String inviteBody,
      String inviteBodyUrl) throws ServiceException {

    for (Map<String, String> map : list) {
      String psnId = MapUtils.getString(map, "psn_id");
      String email = MapUtils.getString(map, "email");
      Message message = new Message();
      // 如果匹配到系统人员则发送站内信.
      if (StringUtils.isNotEmpty(psnId)) {
        message.setReceivers(psnId);
        message.setContent(inviteBodyUrl);
        message.setTitle(inviteTitle);
        /**
         * 修正JIRA问题SCM-856：发送站内信时，增加根据“邮件接收设置”菜单的设置结果控制是否发送站内信的邮件通知功能.
         * 
         * @author MaoJianGuo
         * @since 2012-10-24
         */
        // messageService.sendInsideMessage(message,
        // ConstMailTypeDictionary.IGNORE);
        // insideMessageService.sendMessageAndMail(message);
      } else if (StringUtils.isNotEmpty(email)) {// 如果邮件地址不为空则发送邮件邀请.
        // 通过发送邮件邀请信息
        Map<String, Object> ctxMap = new HashMap<String, Object>();
        ctxMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, email);
        ctxMap.put(EmailConstants.EMAIL_SUBJECT_KEY, inviteTitle);
        ctxMap.put("content", inviteBody);
        ctxMap.put("psn_id", psnId);
        ctxMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, psnId);
        groupInviteMsgEmailService.syncEmailInfo(ctxMap);
      }
    }
  }

  /**
   * 发送请求加入群组的请求(发送MQ异步执行).
   * 
   * @throws ServiceException
   */
  @Override
  public void dealInviteSendMessage(GroupPsn groupPsn, GroupInvitePsn groupInvitePsn, Map<String, Object> ctxMap)
      throws ServiceException {
    Long userId = groupInvitePsn.getPsnId();
    if (groupPsn == null || groupInvitePsn == null) {
      return;
    }
    // 获取准备加入群组的用户信息.
    Person person = inviteStaticMethod.getPersonManager(userId).getPerson(userId);
    // 获取cas的登录跳转地址.
    String casUrl = ObjectUtils.toString(ctxMap.get("casUrl"));
    // 封装发件箱InviteMailBox的参数信息.
    String locale = inviteStaticMethod.getLocale(userId);
    Map<String, Object> inviteMailParam;
    try {
      inviteMailParam = this.assemInviteMailBoxParam(groupPsn, groupInvitePsn.getInvitePsnId(), null, locale);
    } catch (IOException e) {
      logger.error("构建请求加入群组的参数失败，没有找到标题或内容对应的Email模板！", e);
      throw new ServiceException(e);
    } catch (TemplateException e) {
      logger.error("构造请求加入群组的失败,FreeMarker处理失败", e);
      throw new ServiceException(e);
    }

    String des3GroupNodeId = groupPsn.getDes3GroupNodeId();
    String des3GroupId = groupPsn.getDes3GroupId();
    Map<String, Object> mailParam = this.assemMainInviteMessMap(des3GroupId, des3GroupNodeId, groupPsn.getGroupName(),
        groupInvitePsn, casUrl, person);
    // userid用于邮件中查看申请人员主页
    mailParam.put("userId", userId);
    // 封装MQ请求参数部分信息.
    invitBusinessService.dealSendInviteMessage(person, inviteMailParam, mailParam, DynamicConstant.RES_TYPE_GROUP);

  }

  /**
   * 处理MQ接收端的群组邀请信息.
   * 
   * @param param 邮件参数信息.
   * @param receivor 收件人信息.
   */
  @Override
  public void sendGroupInviteMail(Map<String, Object> param, Map<String, Object> receivor) throws ServiceException {
    String des3GroupIdStr = ObjectUtils.toString(param.get("des3GroupId"));
    String des3GroupNodeId = ObjectUtils.toString(param.get("des3GroupNodeId"));

    // 获取cas的登录跳转地址.
    String casUrl = ObjectUtils.toString(param.get("casUrl"));
    Map<String, Object> mailParam = param;
    try {
      mailParam = this.buildWholeInviteMessMap(param, receivor, casUrl, des3GroupNodeId, des3GroupIdStr);
      if (StringUtils.isNotBlank(casUrl)) {
        // 发送请求邮件.
        this.sendJoinGroupInfoMailToAdmin(mailParam);
      }
    } catch (Exception e) {
      logger.error("封装MQ请求参数中的收件人信息出错", e);
    }
  }

  /**
   * 请求加入群组时向管理员发送邮件.
   * 
   * @param ctxMap
   * @throws ServiceException
   * @throws TemplateException
   * @throws IOException
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void sendJoinGroupInfoMailToAdmin(Map<String, Object> ctxMap)
      throws ServiceException, IOException, TemplateException {
    String locale = ObjectUtils.toString(ctxMap.get("locale"));
    if (StringUtils.isBlank(locale)) {
      locale = LocaleContextHolder.getLocale().toString();
    }
    String groupName = ObjectUtils.toString(ctxMap.get("groupName"));
    String psnName = ObjectUtils.toString(ctxMap.get("psnName"));
    String subject = this.assemInviteMailTitle(psnName, groupName, locale);// 邮件主题.
    String mailTemplate = REQUEST_JOIN_GROUP + "_" + locale + ".ftl";
    // String toEmail = ObjectUtils.toString(ctxMap.get("email"));
    // 在表 group_invite_psn 中 获取到的mail 没有能及时更新所以导致邮件发送不出去。或者邮件发送到错误的邮箱 tsz
    // 改进为 使用 收件人id去获取首要邮件 如果以后对 该表中的数据 进行了改造 在改回来 tsz 2014-3-11
    String toEmail = groupInviteDao.getEmailByPsnId((Long) ctxMap.get("psnId")).toString();
    // 发送邮件(提供对邮件接收设置的设置接口，暂不提供).
    if (StringUtils.isNotBlank(toEmail)) {
      HashMap hashMap = new HashMap();
      hashMap.put("adminPsnName", ctxMap.get("adminPsnName"));
      hashMap.put("psnName", psnName);
      hashMap.put("groupName", ctxMap.get("groupName"));
      hashMap.put("operateUrl", ctxMap.get("operateUrl"));
      hashMap.put("viewUrl", ctxMap.get("viewUrl"));
      hashMap.put("viewInvitePsnUrl", ctxMap.get("viewInvitePsnUrl"));
      hashMap.put("mailSetUrl", ctxMap.get("mailSetUrl"));

      hashMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
      hashMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, toEmail);
      hashMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
      hashMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, (Long) ctxMap.get("psnId"));
      // mailSendService.sendMail(toEmail, subject, mailTemplate,
      // hashMap);
      reqJoinGroupEmailService.syncEmailInfo(hashMap);
    }
  }

  /**
   * 获取群组管理员的节点列表.
   * 
   * @param param
   * @return
   * @throws ServiceException
   */
  @Override
  public List<Integer> getAdminNodeList(Map<String, Object> param) {
    String des3GroupIdStr = ObjectUtils.toString(param.get("des3GroupId"));
    if (StringUtils.isNotBlank(des3GroupIdStr)) {
      Long groupId = Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupIdStr));
      try {
        return groupMemberService.findAdminNodeListByGroupId(groupId);
      } catch (ServiceException e) {
        logger.error("获取群组管理员的节点列表", e);
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * 获取群组邀请的收件人.
   * 
   * @param param
   * @return
   */
  @Override
  public List<Map<String, Object>> getGroupReceivor(Map<String, Object> param) {
    String des3GroupIdStr = ObjectUtils.toString(param.get("des3GroupId"));
    Long groupId = Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupIdStr));
    String des3GroupNodeId = ObjectUtils.toString(param.get("des3GroupNodeId"));
    // 获取群组管理员列表(包括ID和名称).
    List<Map<String, Object>> adminList = groupAdminInviteService.findGroupAdminsInfo(groupId);
    return adminList;
  }

  @Override
  public GroupInvite getGroupInviteById(Long inviteId) throws ServiceException {
    return groupInviteDao.get(inviteId);
  }

  /**
   * 根据邀请码获取群组邀请记录.
   * 
   * @param inviteCode 邀请码.
   * @return
   */
  @Override
  public GroupInvite getInviteByInviteCode(String inviteCode) {
    if (StringUtils.isNotBlank(inviteCode)) {
      return groupInviteDao.findInviteRecordByInviteCode(inviteCode);
    }
    return null;
  }

  /**
   * 插入邀请记录.
   * 
   * @param psnId
   * @param email
   * @param groupId
   * @param userId
   * @return
   * @throws ServiceException
   */
  @Override
  public GroupInvite saveInviteRecord(String psnId, String email, Long groupId, Long userId) throws ServiceException {
    GroupInvite groupInvite = new GroupInvite();
    groupInvite.setCreateDate(new Date());
    String oldEmail = null;
    if (StringUtils.isEmpty(psnId)) {// 手动输入emial
      groupInvite.setEmail(email);
    } else {// 查询已存在人员email
      oldEmail = inviteStaticMethod.findRemoteUser(NumberUtils.toLong(psnId)).getEmail();
      groupInvite.setEmail(oldEmail);
    }
    groupInvite.setGroupId(groupId);
    if (StringUtils.isNotEmpty(psnId)) {
      groupInvite.setPsnId(NumberUtils.toLong(psnId));
    }
    groupInvite.setSendPsnId(userId);
    try {
      if (StringUtils.isNotEmpty(psnId)) {// 站内人员插入邀请记录
        GroupInvite updateGroupInvite = groupInviteDao.findGroupInviteByUserId(groupInvite.getGroupId(),
            groupInvite.getPsnId(), groupInvite.getSendPsnId());
        if (updateGroupInvite != null) {
          groupInvite = updateGroupInvite;
          groupInvite.setEmail(oldEmail);
        }
      } else {// 站外人员插入邀请记录(如果数据库中已存的邀请记录被其他用户绑定，则新增一条邀请记录)_MJG_2013-05-24_SCM-2550.
        GroupInvite updateGroupInvite =
            groupInviteDao.findGroupInviteByEmail(groupInvite.getGroupId(), email, groupInvite.getSendPsnId());
        if (updateGroupInvite != null && updateGroupInvite.getPsnId() == null) {
          groupInvite = updateGroupInvite;
        }
      }

      groupInviteDao.save(groupInvite);

    } catch (Exception e) {
      logger.error("设置群组管理员出错", e);
      throw new ServiceException(e);
    }
    return groupInvite;
  }

  /**
   * 封装MQ消息中需封装的部分参数.
   * 
   * @param des3GroupId
   * @param des3GroupNodeId
   * @param groupName
   * @param invitePsnId
   * @param casUrl
   * @param locale
   * @return
   */
  private Map<String, Object> assemMainInviteMessMap(String des3GroupId, String des3GroupNodeId, String groupName,
      GroupInvitePsn groupInvitePsn, String casUrl, Person person) {
    Map<String, Object> mailParam = new HashMap<String, Object>();
    mailParam.put("casUrl", casUrl);
    mailParam.put("des3GroupId", des3GroupId);
    mailParam.put("des3GroupNodeId", des3GroupNodeId);
    mailParam.put("groupName", groupName);
    mailParam.put("invitePsnId", groupInvitePsn.getInvitePsnId());
    // 封装发送请求的人的信息.
    String nameStr = groupInvitePsn.getPsnName();//
    if (StringUtils.isBlank(nameStr)
        || (StringUtils.isNotBlank(nameStr) && nameStr.getBytes().length == nameStr.length())) {
      if (StringUtils.isNotBlank(person.getName())) {
        nameStr = person.getName();
      } else {
        nameStr = person.getFirstName() + " " + person.getLastName();
      }
    }

    String enNameStr = "";
    if (StringUtils.isNotBlank(groupInvitePsn.getPsnFirstName())
        && StringUtils.isNotBlank(groupInvitePsn.getPsnLastName())) {
      enNameStr = person.getFirstName() + " " + person.getLastName();
    } else {
      enNameStr = person.getName();
    }

    mailParam.put("psnName_zh", nameStr);
    mailParam.put("psnName_en", enNameStr);
    return mailParam;
  }

  /**
   * 封装完整的MQ请求参数信息.
   * 
   * @param mailParam
   * @param receivor
   * @param casUrl
   * @param des3GroupNodeId
   * @param des3GroupId
   * @return
   * @throws ServiceException
   * @throws UnsupportedEncodingException
   */
  private Map<String, Object> buildWholeInviteMessMap(Map<String, Object> mailParam, Map<String, Object> receivor,
      String casUrl, String des3GroupNodeId, String des3GroupId) throws ServiceException, UnsupportedEncodingException {
    Long receiverId = (Long) receivor.get(REQUEST_MAIL_RECEIVOR_KEY_PSNID);// 管理员ID.
    Long sendPsnId = (Long) mailParam.get("userId");
    String receiverLocale = ObjectUtils.toString(mailParam.get("locale"));
    if (StringUtils.isBlank(receiverLocale)) {
      receiverLocale = inviteStaticMethod.getLocale(receiverId);
    }
    // 发件人名称.
    String psnNameZH =
        (StringUtils.isNotBlank((String) mailParam.get("psnName_zh"))) ? (String) mailParam.get("psnName_zh")
            : (String) mailParam.get("psnName_en");
    String psnNameEN =
        (StringUtils.isNotBlank((String) mailParam.get("psnName_en"))) ? (String) mailParam.get("psnName_en")
            : (String) mailParam.get("psnName_zh");
    String psnName = "zh_CN".equals(receiverLocale) ? psnNameZH : psnNameEN;
    // 删除这两个key，会引起后面收件人邮件无申请人姓名 SCM-3234 by zk 2013-08-10 10:07:28
    // mailParam.remove("psnName_zh");
    // mailParam.remove("psnName_en");
    mailParam.put("psnName", psnName);
    // 收件人名称.
    String adminPsnNameZH = (StringUtils.isNotBlank((String) receivor.get(REQUEST_MAIL_RECEIVOR_KEY_NAME)))
        ? (String) receivor.get(REQUEST_MAIL_RECEIVOR_KEY_NAME)
        : (String) receivor.get(REQUEST_MAIL_RECEIVOR_KEY_ENNAME);
    String adminPsnNameEN = (StringUtils.isNotBlank((String) receivor.get(REQUEST_MAIL_RECEIVOR_KEY_ENNAME)))
        ? (String) receivor.get(REQUEST_MAIL_RECEIVOR_KEY_ENNAME)
        : (String) receivor.get(REQUEST_MAIL_RECEIVOR_KEY_NAME);
    String adminName = "zh_CN".equals(receiverLocale) ? adminPsnNameZH : adminPsnNameEN;
    mailParam.put("adminPsnName", adminName);
    // 收件人ID.
    mailParam.put("psnId", receiverId);
    mailParam.put("locale", receiverLocale);
    mailParam.put("email", receivor.get(REQUEST_MAIL_RECEIVOR_KEY_EMAIL));
    // 获取邮件正文按钮响应链接地址(链接跳转自动登录到待批准成员页面).
    String operateUrl = inviteStaticMethod.getRequestJoinOperatUrl(receiverId, receiverLocale, casUrl, des3GroupNodeId,
        des3GroupId, sendPsnId);
    mailParam.put("operateUrl", operateUrl);
    // 获取邮件正文浏览群组链接地扯
    mailParam.put("viewUrl", inviteStaticMethod.getViewGroupUrl(receiverId, receiverLocale, casUrl, des3GroupNodeId,
        des3GroupId, sendPsnId));
    // 请求加入群组人员url
    mailParam.put("viewInvitePsnUrl", inviteStaticMethod.getViewPsnUrl(receiverId, receiverLocale, casUrl,
        Long.valueOf(mailParam.get("userId").toString()), des3GroupId));
    mailParam.put("mailSetUrl", inviteStaticMethod.getUnsubscribeUrl(receiverId, casUrl));
    return mailParam;
  }

  /**
   * 拼装站内信邀请发件箱invite_mailbox的字段值.
   * 
   * @param senderInfo
   * @param extOtherInfo
   * @param invitePsnId
   * @return
   * @throws ServiceException
   * @throws TemplateException
   * @throws IOException
   */
  private Map<String, Object> assemInviteMailBoxParam(GroupPsn groupPsn, Long invitePsnId, String extOtherInfo,
      String locale) throws ServiceException, IOException, TemplateException {
    Map<String, Object> inviteMailParam = new HashMap<String, Object>();
    Long curentUserId = SecurityUtils.getCurrentUserId();

    inviteMailParam.put("title", "请求加入群组“" + groupPsn.getGroupName() + "”");
    inviteMailParam.put("enTitle", " request to join the group \"" + groupPsn.getGroupName() + "\"");
    String content = assemInviteMessageContent(groupPsn.getGroupId(), groupPsn.getGroupNodeId(), invitePsnId, locale);
    inviteMailParam.put("content", content);
    inviteMailParam.put("inviteType", GROUP_INVITE_TYPE);

    String titolo = null;
    Person recvPerson = inviteStaticMethod.getPersonManager(curentUserId).getPerson(curentUserId);
    if (recvPerson != null) {
      titolo = recvPerson.getTitolo();
    }
    String jsonSenderInfo = assemInviteMailSendInfo(curentUserId, titolo);
    inviteMailParam.put("senderInfo", jsonSenderInfo);
    inviteMailParam.put("extOtherInfo", extOtherInfo);
    inviteMailParam.put("invitePsnId", invitePsnId);
    return inviteMailParam;
  }

  /**
   * 拼装请求加入群组的站内信邀请发件箱invite_mailbox的senderInfo参数值.
   * 
   * @param curentUserId
   * @param titolo
   * @return
   */
  private String assemInviteMailSendInfo(Long curentUserId, String titolo) {
    // 封装设置发件人基本信息(json格式).
    StringBuffer jsonSenderInfo = new StringBuffer();
    try {
      List<WorkHistory> lstWorkEdu = inviteStaticMethod.getPersonManager(curentUserId).findWorkAndEdu(curentUserId);
      jsonSenderInfo.append("{\"titolo\":\"").append(titolo).append("\"");
      for (WorkHistory wh : lstWorkEdu) {
        if (wh.getIsPrimary() != null && wh.getIsPrimary() == 1) {
          jsonSenderInfo.append(",\"primaryUtil\":\"").append(wh.getInsName()).append("\",");
          jsonSenderInfo.append("\"dept\":\"").append(wh.getDepartment()).append("\",");
          jsonSenderInfo.append("\"position\":\"").append(wh.getPosition()).append("\"");
          break;
        }
      }
      jsonSenderInfo.append("}");
    } catch (ServiceException e1) {
      e1.printStackTrace();
    }
    return jsonSenderInfo.toString();
  }

  /**
   * 拼装请求加入群组的站内信标题.
   * 
   * @param groupName
   * @param locale
   * @return
   * @throws ServiceException
   * @throws TemplateException
   * @throws IOException
   */
  private String assemInviteMessageTitle(String groupName, String locale)
      throws ServiceException, IOException, TemplateException {
    String title = null;
    String titleTemplate = REQUEST_JOIN_GROUP_TITLE + "_" + locale + ".ftl";
    Map<String, String> titleMap = new HashMap<String, String>();
    titleMap.put("tmpUrl", titleTemplate);
    titleMap.put("groupName", groupName);
    title = inviteStaticMethod.buildMsg(titleMap);
    return title;
  }

  /**
   * 拼装请求加入群组的邮件标题.
   * 
   * @param psnName
   * @param groupName
   * @param locale
   * @return
   * @throws ServiceException
   * @throws TemplateException
   * @throws IOException
   */
  private String assemInviteMailTitle(String psnName, String groupName, String locale)
      throws ServiceException, IOException, TemplateException {
    String title = null;
    String titleTemplate = REQUEST_JOIN_GROUP_MAIL_TITLE + "_" + locale + ".ftl";
    Map<String, String> titleMap = new HashMap<String, String>();
    titleMap.put("tmpUrl", titleTemplate);
    titleMap.put("groupName", groupName);
    titleMap.put("psnName", psnName);
    title = inviteStaticMethod.buildMsg(titleMap);
    return title;
  }

  /**
   * 拼装请求加入群组的站内信内容.
   * 
   * @param groupPsn
   * @param groupInvitePsn
   * @param locale
   * @return
   * @throws ServiceException
   * @throws TemplateException
   * @throws IOException
   */
  private String assemInviteMessageContent(Long groupId, Integer groupNodeId, Long invitePsnId, String locale)
      throws ServiceException, IOException, TemplateException {
    String content = null;
    String contentTemplate = REQUEST_JOIN_GROUP_CONTENT + "_" + locale + ".ftl";
    Map<String, String> contentMap = new HashMap<String, String>();
    contentMap.put("tmpUrl", contentTemplate);
    contentMap.put("inviteId", invitePsnId.toString());
    contentMap.put("groupId", groupId.toString());
    contentMap.put("groupNodeId", groupNodeId.toString());
    content = inviteStaticMethod.buildMsg(contentMap);
    return content;
  }
}
