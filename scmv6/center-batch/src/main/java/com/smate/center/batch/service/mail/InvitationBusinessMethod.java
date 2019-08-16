package com.smate.center.batch.service.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.InviteInbox;
import com.smate.center.batch.model.mail.InviteMailBox;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.psn.SysUserLocaleService;
import com.smate.center.batch.service.pub.GroupInviteService;
import com.smate.center.batch.util.pub.DynamicConstant;
import com.smate.core.base.utils.model.local.SysUserLocale;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;


/**
 * 收件箱-站内邀请业务逻辑的资源服务类.
 * 
 * @see 针对当前包com.iris.scm.scmweb.service.message中的其他service实现提供服务，进行参数封装整理或所需参数拼装.
 * @author maojianguo
 * 
 */

@SuppressWarnings("javadoc")
@Repository
public class InvitationBusinessMethod {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupInviteService groupInviteService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private InboxService inboxService;
  @Autowired
  private SysUserLocaleService sysUserLocaleService;


  /**
   * 根据psnId 获取相应的Service.
   * 
   * @param psnId 人员ID.
   * @return
   * @throws ServiceException
   */
  PersonManager getPersonManager(Long psnId) throws ServiceException {
    return personManager;
  }

  /**
   * 封装发送站内邀请MQ相关的发件人和发件箱参数.
   * 
   * @param person
   * @param mailBox
   * @return
   * @throws ServiceException
   */
  Map<String, Object> assemStaticSendParam(Person person, InviteMailBox mailBox, Map<String, Object> mailParam)
      throws ServiceException {
    // 封装发件箱相关记录.
    mailParam.put("mailId", mailBox.getMailId());
    mailParam.put("title", mailBox.getTitle());
    mailParam.put("content", mailBox.getContent());
    return mailParam;
  }

  /**
   * 获取收件人的节点列表.
   * 
   * @param mailParam 业务参数信息.
   * @param messageType 业务类型.
   * @return
   */
  List<Integer> getReceivorNodeList(Map<String, Object> mailParam, Integer messageType) {
    List<Integer> nodeList = new ArrayList<Integer>();
    switch (messageType) {
      /**
       * 群组.
       */
      case DynamicConstant.RES_TYPE_GROUP:
        // 获取群组节点列表.
        nodeList = groupInviteService.getAdminNodeList(mailParam);
        break;
      /**
       * 好友.
       */
      case DynamicConstant.RES_TYPE_FRIEND:

        break;
      default:
        break;
    }
    return nodeList;
  }

  /**
   * 获取站内邀请的收件箱关联记录参数.
   * 
   * @param param
   * @return
   */
  String getInvitePsnId(Map<String, Object> param) {
    String invitePsnId = ObjectUtils.toString(param.get("invitePsnId"));
    return invitePsnId;
  }

  /**
   * 获取站内邀请的邮件标题和正文内容(拼装为json格式).
   * 
   * @param param
   * @return
   */
  String getInviteMailContent(Map<String, Object> param) {
    StringBuilder mailContent = new StringBuilder();
    String title = ObjectUtils.toString(param.get("title"));
    String content = ObjectUtils.toString(param.get("content"));
    mailContent.append("{\"title\":\"" + title + "\",\"content\":\"" + content + "\"}");
    return mailContent.toString();
  }

  /**
   * 封装保存邀请收件箱的相关参数.
   * 
   * @param param
   * @return
   */
  Map<String, Object> assemInviteInParam(Map<String, Object> param) {
    // 封装收件箱信息.
    Map<String, Object> inviteInParam = new HashMap<String, Object>();
    inviteInParam.put("mailId", ObjectUtils.toString(param.get("mailId")));
    inviteInParam.put("status", InvitationBusinessService.REQUEST_JOIN_INVITEIN_STATUS);
    inviteInParam.put("optStatus", InvitationBusinessService.REQUEST_JOIN_INVITEIN_OPTSTATUS);
    return inviteInParam;
  }

  /**
   * 重建收件人列表信息.
   * 
   * @param person 收件人.
   * @param receivor key值对应GroupInvitePsnDao.findGroupAdminsInfo方法.
   * @return
   */
  Map<String, Object> rebuildReceivorInfo(Person person, Map<String, Object> receivor) {
    Map<String, Object> receivorParam = new HashMap<String, Object>();
    receivorParam.put(GroupInviteService.REQUEST_MAIL_RECEIVOR_KEY_PSNID, receivor.get("psnId"));
    // 重置人员名称_MJG_SCM-3221,SCM-3402.
    String nameStr = ObjectUtils.toString(receivor.get("name"));//
    if (StringUtils.isBlank(nameStr)
        || (StringUtils.isNotBlank(nameStr) && nameStr.getBytes().length == nameStr.length())) {
      if (StringUtils.isNotBlank(person.getName())) {
        nameStr = person.getName();
      } else {
        nameStr = person.getLastName() + " " + person.getFirstName();
      }
    }
    String enNameStr = ObjectUtils.toString(receivor.get("enName"));//
    if (StringUtils.isBlank(enNameStr)
        || (StringUtils.isNotBlank(enNameStr) && enNameStr.getBytes().length != enNameStr.length())) {
      if (StringUtils.isNotBlank(person.getLastName()) || StringUtils.isNotBlank(person.getFirstName())) {
        enNameStr = person.getLastName() + " " + person.getFirstName();
      } else {
        enNameStr = person.getName();
      }
    }
    String name = StringUtils.isNotBlank(nameStr) ? nameStr : enNameStr;
    String enName = StringUtils.isNotBlank(enNameStr) ? enNameStr : nameStr;
    receivorParam.put(GroupInviteService.REQUEST_MAIL_RECEIVOR_KEY_NAME, name);
    receivorParam.put(GroupInviteService.REQUEST_MAIL_RECEIVOR_KEY_ENNAME, enName);
    receivorParam.put(GroupInviteService.REQUEST_MAIL_RECEIVOR_KEY_EMAIL, receivor.get("email"));
    return receivorParam;
  }

  /**
   * 验证是否已执行保存请求的收件箱记录.
   * 
   * @param psnId
   * @param params
   * @param inboxService
   * @return true-已保存；false-未保存.
   * @throws NumberFormatException
   * @throws ServiceException
   */
  Boolean checkInviteReceivRecord(Long psnId, Map<String, Object> params, Integer nodeId, InboxService inboxService)
      throws NumberFormatException, ServiceException {
    boolean flag = false;
    String mailId = ObjectUtils.toString(params.get("mailId"));
    InviteInbox inbox = inboxService.getInviteInbox(psnId, Long.valueOf(mailId));
    if (inbox != null) {
      flag = true;
    }
    // 遍历其他节点，查询是否保存收件箱表记录.
    List<Integer> nodeList = SecurityUtils.getCurrentAllNodeId();
    if (!flag && CollectionUtils.isNotEmpty(nodeList)) {
      for (Integer iNode : nodeList) {
        if (iNode.intValue() != nodeId.intValue()) {
          inboxService = this.getInboxService(iNode);
          inbox = inboxService.getInviteInbox(psnId, Long.valueOf(mailId));
          if (inbox != null) {
            flag = true;
            break;
          }
        }
      }
    }
    return flag;
  }

  /**
   * 获取站内邀请的收件人清单(Map中收件人ID的key必须为 psnId ).
   * 
   * @param messageType 邀请类型.
   * @param param 业务相关的请求参数.
   * @param nodeId 发送请求的节点.
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getReceivorList(Integer messageType, Map<String, Object> param, Integer nodeId)
      throws ServiceException {
    List<Map<String, Object>> receivorList = null;
    switch (messageType) {
      /**
       * 群组.
       */
      case DynamicConstant.RES_TYPE_GROUP:
        // 获取群组管理员列表(包括ID和名称).
        // 在表 group_invite_psn 中 获取到的mail 没有能及时更新所以导致邮件发送不出去。或者邮件发送到错误的邮箱 tsz

        groupInviteService = this.getGroupInviteService(nodeId);
        receivorList = groupInviteService.getGroupReceivor(param);
        break;
      /**
       * 好友.
       */
      case DynamicConstant.RES_TYPE_FRIEND:

        break;
      default:
        receivorList = new ArrayList<Map<String, Object>>();
        break;
    }
    return receivorList;
  }

  /**
   * 处理发送站内邀请的邮件.
   * 
   * @param messageType 邀请类型.
   * @param param 业务相关的请求参数.
   * @param receivor 收件人相关信息.
   * @param nodeId 发送请求的节点.
   * @throws ServiceException
   */
  void sendInviteMail(Integer messageType, Map<String, Object> param, Map<String, Object> receivor, Integer nodeId)
      throws ServiceException {
    switch (messageType) {
      /**
       * 群组.
       */
      case DynamicConstant.RES_TYPE_GROUP:
        groupInviteService = this.getGroupInviteService(nodeId);
        groupInviteService.sendGroupInviteMail(param, receivor);
        break;
      /**
       * 好友.
       */
      case DynamicConstant.RES_TYPE_FRIEND:

        break;
      default:
        break;
    }
  }

  /**
   * 根据nodeId 获取相应的Service
   * 
   * @param nodeId 节点ID.
   * @return
   * @throws ServiceException
   */
  private GroupInviteService getGroupInviteService(Integer nodeId) throws ServiceException {
    return groupInviteService;
  }

  /**
   * 根据nodeId 获取相应的Service
   * 
   * @param nodeId 节点ID.
   * @return
   * @throws ServiceException
   */
  InboxService getInboxService(Integer nodeId) throws ServiceException {
    return inboxService;
  }

  /**
   * 根据人员ID获取其语言环境.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String getLocale(Long psnId) throws ServiceException {
    try {
      String locale = null;
      SysUserLocale sysuserLocale = sysUserLocaleService.getSysUserLocaleByPsnId(psnId);
      // 修正完善了获取locale值的代码_MJG_2012-11-28_SCM-1341.
      if (sysuserLocale != null) {
        locale = sysuserLocale.getLocale();
      }
      if (locale == null || "".equals(locale)) {
        locale = LocaleContextHolder.getLocale().toString();
      }
      return locale;
    } catch (Exception e) {
      logger.error("根据人员ID获取其语言环境", e);
      throw new ServiceException("根据人员ID获取其语言环境", e);
    }
  }
}
