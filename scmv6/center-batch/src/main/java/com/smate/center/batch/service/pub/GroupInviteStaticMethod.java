package com.smate.center.batch.service.pub;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.acegisecurity.util.EncryptionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.GroupMember;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupPsnForm;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.psn.PsnMailSetService;
import com.smate.center.batch.service.psn.SyncPersonService;
import com.smate.center.batch.service.psn.SysUserLocaleService;
import com.smate.core.base.utils.model.local.SysUserLocale;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 群组邀请与请求加入的业务逻辑资源类.
 * 
 * @see 针对当前包com.iris.scm.scmweb.service.group.member中的其他service实现提供服务 ，进行参数封装整理或所需参数拼装.
 * @author maojianguo
 * 
 */

@SuppressWarnings("javadoc")
@Component
class GroupInviteStaticMethod {

  static final String ENCODING = "utf-8";
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private Configuration groupinviteFreemarkereConfiguration;
  @Autowired
  private SyncPersonService syncPersonService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PsnMailSetService psnMailSetService;
  @Autowired
  private GroupAdminInviteService groupAdminInviteService;
  @Autowired
  private GroupMemberService groupMemberService;
  @Autowired
  private GroupService groupService;
  @Autowired
  private SysDomainConst sysDomainConst;
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
   * 根据用户Id获取PsnMailSetService.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  PsnMailSetService getPsnMailSetService(Long psnId) throws ServiceException {
    return psnMailSetService;
  }

  /**
   * 根据群组节点获取GroupAdminInviteService.
   * 
   * @param groupNodeId
   * @return
   * @throws ServiceException
   */
  GroupAdminInviteService getGroupAdminInviteService(Integer groupNodeId) throws ServiceException {
    return groupAdminInviteService;
  }

  /**
   * 根据人员节点获取GroupMemberService.
   * 
   * @param groupNodeId
   * @return
   * @throws ServiceException
   */
  GroupMemberService getGroupMemberService(Long psnId) throws ServiceException {
    return groupMemberService;
  }

  /**
   * 根据设置邮件的接受语言，获取用户名.
   * 
   * @param person
   * @param emailLangage
   * @return personName
   */
  String getPsnNameByEmailLangage(Person person, String emailLangage) {
    if (person == null) {
      return null;
    }
    if (emailLangage == null) {
      emailLangage = LocaleContextHolder.getLocale().toString();
    }
    String psnName = null;
    if (Locale.CHINA.toString().equals(emailLangage)) {
      psnName = person.getName();
      if (StringUtils.isBlank(psnName)) {
        psnName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName())) {
        psnName = person.getName();
      }
    }
    return psnName;
  }

  /**
   * 获取群组主页地址.
   * 
   * @param groupId 群组ID.
   * @return
   * @throws ServiceException
   * @throws UnsupportedEncodingException
   */
  String genGroupHomeUrl(Long groupId) throws ServiceException, UnsupportedEncodingException {
    try {
      Integer currentNodeId = SecurityUtils.getCurrentAllNodeId().get(0);
      String baseUrl = "https://" + sysDomainConst.getSnsDomain();
      if (sysDomainConst.getSnsContext() != null) {
        baseUrl = baseUrl + "/" + sysDomainConst.getSnsContext();
      }
      String groupHomeUrl = baseUrl + "/hp/view?des3GroupId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(groupId.toString()), ENCODING) + "&des3NodeId="
          + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(currentNodeId.toString()), ENCODING);
      return groupHomeUrl;
    } catch (Exception e) {
      logger.error("获取群组主页地址", e);
      throw new ServiceException("获取群组主页地址", e);
    }
  }

  /**
   * 获取群组url
   * 
   * @author scm
   */

  String getViewGroupUrl(Long receiverId, String receiverLocale, String casUrl, String des3GroupNodeId,
      String des3GroupId, Long sendPsnId) throws ServiceException {
    try {
      String domain = getSysDomain();
      String autoLoginUrl = getAutoLoginUrl(receiverId, casUrl);
      // 获取群组url.
      String groupOperateUrl = domain + "/scmwebsns/group/dyn?menuId=31&locale=" + receiverLocale
          + "&groupPsn.des3GroupNodeId=" + des3GroupNodeId + "&groupPsn.des3GroupId=" + des3GroupId;
      // if (sendPsnId != null)
      // groupOperateUrl += "&email2log="
      // + ServiceUtil.encodeToDes3(this.getEmail2Log(receiverId, 3,
      // sendPsnId,
      // Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId))));
      String viewUrl =
          autoLoginUrl + "&service=" + java.net.URLEncoder.encode(groupOperateUrl, GroupInviteStaticMethod.ENCODING);
      return viewUrl;
    } catch (Exception e) {
      logger.error("getViewGroupUrl", e);
      throw new ServiceException("getViewGroupUrl", e);
    }
  }

  /**
   * 获取人员url
   */

  String getViewPsnUrl(Long receiverId, String receiverLocale, String casUrl, Long sendPsnId, String des3GroupId)
      throws ServiceException {
    try {
      String domain = getSysDomain();
      String autoLoginUrl = getAutoLoginUrl(receiverId, casUrl);
      String PsnOperateUrl = domain + "/scmwebsns/resume/psnView?menuId=1100&des3PsnId="
          + ServiceUtil.encodeToDes3(sendPsnId.toString()) + "&locale=" + receiverLocale;// 人员主页
      // +
      // "&email2log="
      // + ServiceUtil.encodeToDes3(this.getEmail2Log(receiverId, 1,
      // sendPsnId,
      // Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId))))
      String viewUrl =
          autoLoginUrl + "&service=" + java.net.URLEncoder.encode(PsnOperateUrl, GroupInviteStaticMethod.ENCODING);
      return viewUrl;
    } catch (Exception e) {
      logger.error("getViewPsnUrl", e);
      throw new ServiceException("getViewPsnUrl", e);
    }
  }

  /**
   * 取消订阅
   * 
   * @throws UnsupportedEncodingException
   */
  String getUnsubscribeUrl(Long receiverId, String casUrl) throws ServiceException {
    try {
      String domain = getSysDomain();
      String autoLoginUrl = getAutoLoginUrl(receiverId, casUrl);
      String PsnOperateUrl = domain + "/scmwebsns/user/setting/getMailTypeList";// 批准加入群组的地址.
      String viewUrl =
          autoLoginUrl + "&service=" + java.net.URLEncoder.encode(PsnOperateUrl, GroupInviteStaticMethod.ENCODING);
      return viewUrl;
    } catch (Exception e) {
      logger.error("getUnsubscribeUrl", e);
      throw new ServiceException("getUnsubscribeUrl", e);
    }
  }

  /**
   * 获取自动登录地址固定部分(获取完整的自动登录地址需在返回值后追加参数service及对应值).
   * 
   * @param personID
   * @param casUrl
   * @return
   * @throws UnsupportedEncodingException
   */
  @SuppressWarnings("deprecation")
  String getAutoLoginUrl(Long personID, String casUrl) throws ServiceException {
    try {
      String userType = "CITE";
      String passStr = userType + "|" + personID;
      String encpassword = EncryptionUtils.encrypt("111111222222333333444444", passStr);
      String citePassword = java.net.URLEncoder.encode(encpassword, ENCODING);
      String inviteUrl = casUrl + "login?submit=true&username=CITE&password=" + citePassword;
      return inviteUrl;
    } catch (Exception e) {
      logger.error("getAutoLoginUrl", e);
      throw new ServiceException("getAutoLoginUrl", e);
    }
  }

  /**
   * 获取当前系统域名.
   * 
   * @return
   */
  String getSysDomain() throws ServiceException {
    try {
      // 获取当前系统域名.
      String domain = this.sysDomainConst.getSnsDomain();
      return domain;
    } catch (Exception e) {
      logger.error("getSysDomain", e);
      throw new ServiceException("getSysDomain", e);
    }
  }

  /**
   * 获取人员的系统名称.
   * 
   * @param psnId 人员ID.
   * @return
   * @throws ServiceException
   */
  String getSystemUrl(Long psnId) throws ServiceException {
    try {
      String systemUrl = "https://" + sysDomainConst.getSnsDomain();
      if (sysDomainConst.getSnsContext() != null) {
        systemUrl = systemUrl + "/" + sysDomainConst.getSnsContext();
      }
      return systemUrl;
    } catch (Exception e) {
      logger.error("getSystemUrl", e);
      throw new ServiceException("getSystemUrl", e);
    }
  }

  /**
   * 根据人员ID获取其语言环境.
   * 
   * @param psnId 人员ID.
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
      logger.error("getLocale", e);
      throw new ServiceException("getLocale", e);
    }
  }

  /**
   * 远程获取获取群组成员记录.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  GroupMember findRemoteUser(Long psnId) throws ServiceException {
    return groupService.findGroupMemberDetail(psnId);
  }

  /**
   * 根据模版构建Email内容.
   * 
   * @param map
   * @return
   * @throws ServiceException
   * @throws TemplateException
   * @throws IOException
   */
  String buildMsg(Map<String, String> map) throws ServiceException, IOException, TemplateException {
    String msg = FreeMarkerTemplateUtils
        .processTemplateIntoString(groupinviteFreemarkereConfiguration.getTemplate(map.get("tmpUrl"), ENCODING), map);
    return msg;
  }

  /**
   * 拼装请求加入群组的邮件按钮链接响应地址.
   * 
   * @param receiverId 收件人ID.
   * @param receiverLocale 收件人语言环境.
   * @param casUrl 当前系统地址.
   * @param des3GroupNodeId 群组节点ID.
   * @param des3GroupId 群组ID.
   * @return
   * @throws UnsupportedEncodingException
   */
  String getRequestJoinOperatUrl(Long receiverId, String receiverLocale, String casUrl, String des3GroupNodeId,
      String des3GroupId, Long sendPsnId) throws ServiceException {
    try {
      String domain = getSysDomain();
      String autoLoginUrl = getAutoLoginUrl(receiverId, casUrl);
      String groupOperateUrl = domain + "/scmwebsns/group/memberNotYet?menuId=31&locale=" + receiverLocale
          + "&groupPsn.des3GroupNodeId=" + des3GroupNodeId + "&groupPsn.des3GroupId=" + des3GroupId;// 批准加入群组的地址.
      // + "&email2log="
      // + ServiceUtil.encodeToDes3(this.getEmail2Log(receiverId, 4,
      // sendPsnId,
      // Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId))))
      String operateUrl =
          autoLoginUrl + "&service=" + java.net.URLEncoder.encode(groupOperateUrl, GroupInviteStaticMethod.ENCODING);
      return operateUrl;
    } catch (Exception e) {
      logger.error("getRequestJoinOperatUrl", e);
      throw new ServiceException("getRequestJoinOperatUrl", e);
    }
  }

  /**
   * 获取邮件重设地址链接.
   * 
   * @param psnId 人员ID.
   * @param languageVersion 语言版本.
   * @return
   * @throws ServiceException
   */
  String getMailSetUrl(Long psnId, String languageVersion) throws ServiceException {
    try {
      String mailSetUrl = null;
      if (psnId != null && psnId > 0) {
        mailSetUrl =
            sysDomainConst.getSnsDomain() + "/scmwebsns/user/setting/getMailTypeList?locale=" + languageVersion;
      }
      return mailSetUrl;
    } catch (Exception e) {
      logger.info("获取邮件重设地址链接", e);
      throw new ServiceException("获取邮件重设地址链接", e);
    }
  }

  /**
   * 获取已验证邮箱的群组邀请链接地址.
   * 
   * @param psnId 被邀请的人员ID.
   * @param casUrl cas的登录跳转地址.
   * @param inviteUrlParams 请求参数.
   * @return
   * @throws ServiceException
   * @throws UnsupportedEncodingException
   */
  String getValidatedInviteUrl(Long psnId, String casUrl, String inviteUrlParams)
      throws ServiceException, UnsupportedEncodingException {
    try {
      String operateUrl = null;
      String inviteUrl = sysDomainConst.getSnsDomain() + "/scmwebsns/msgbox/requestMain?";
      // 设置连接地址为可自动登录.
      String autoLoginUrl = this.getAutoLoginUrl(psnId, casUrl);// 请求地址的自动登录前缀部分.
      if (StringUtils.isNotBlank(autoLoginUrl)) {
        String groupOperateUrl = inviteUrl + inviteUrlParams;// 实际请求地址部分.
        operateUrl =
            autoLoginUrl + "&service=" + java.net.URLEncoder.encode(groupOperateUrl, GroupInviteStaticMethod.ENCODING);
      }
      return operateUrl;
    } catch (Exception e) {
      logger.info("获取已验证邮箱的群组邀请链接地址", e);
      throw new ServiceException("获取已验证邮箱的群组邀请链接地址", e);
    }
  }

  /**
   * 获取未验证邮箱的或系统外人员的群组邀请链接地址.
   * 
   * @param isSysUser 是否系统用户.
   * @param psnId 人员ID.
   * @param inviteUrlParams 邀请链接参数(业务相关).
   * @param inviteUrlParams 邀请码.
   * @return
   * @throws ServiceException
   * @throws UnsupportedEncodingException
   */
  String getUnValidatedInviteUrl(String isSysUser, Long psnId, String inviteUrlParams, String invitationCode,
      String encodeEmail) throws ServiceException, UnsupportedEncodingException {
    String operateUrl = null;
    // 设置请求路径.
    String systemUrl = getSystemUrl(psnId);
    // 被邀请人员已注册系统(邮箱未验证).
    if (StringUtils.isNotBlank(isSysUser) && "true".equalsIgnoreCase(isSysUser)) {
      String targetUrl = systemUrl + "/invite/groupInvite.action?";
      operateUrl = targetUrl + "email=" + encodeEmail + "&" + inviteUrlParams;// 获取完整的请求地址.
    } else {// 系统外人员.
      String key = getGroupInviteKey();
      String forwardUrl =
          java.net.URLEncoder.encode("/invite/groupInvite.action?" + inviteUrlParams, GroupInviteStaticMethod.ENCODING);
      operateUrl = systemUrl + "/register/register?key=" + key + "&email=" + encodeEmail + "&invitCode="
          + java.net.URLEncoder.encode(invitationCode, GroupInviteStaticMethod.ENCODING) + "&forward=" + forwardUrl;
    }
    return operateUrl;
  }

  /**
   * 获取群组邀请链接的key值.
   * 
   * @return
   * @throws UnsupportedEncodingException
   */
  String getGroupInviteKey() throws UnsupportedEncodingException {
    String key = java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(GroupAdminInviteService.GROUP_INVITE_KEY),
        GroupInviteStaticMethod.ENCODING);
    return key;
  }

  /**
   * 添加链接记录 urlId 对应 maildispatch.mail_template_url表url_id
   * 
   */
  String getEmail2Log(Long receiveId, Integer urlId, Long sendPsnId, Long groupId) {
    return "mailEventLogByJoinGroup|psnId=" + receiveId + ",urlId=" + urlId + ",sendPsnId=" + sendPsnId + ",groupId="
        + groupId;
  }

  /**
   * 将form类封装为群组类_MJG_SCM-3789..
   * 
   * @param dataGroupPsn 数据库中的群组记录.
   * @param groupPsn
   * @return
   */
  GroupPsn formToModel(GroupPsn dataGroupPsn, GroupPsnForm groupPsn) {
    if ((dataGroupPsn.getGroupId() == null) || (dataGroupPsn.getGroupId() != null && groupPsn.getGroupId() != null
        && dataGroupPsn.getGroupId().longValue() != groupPsn.getGroupId().longValue())) {
      dataGroupPsn.setGroupId(groupPsn.getGroupId());
    }
    if ((StringUtils.isBlank(dataGroupPsn.getGroupName())) || (StringUtils.isNotBlank(dataGroupPsn.getGroupName())
        && !dataGroupPsn.getGroupName().equals(groupPsn.getGroupName()))) {
      dataGroupPsn.setGroupName(groupPsn.getGroupName());
    }
    if ((StringUtils.isBlank(dataGroupPsn.getGroupDescription()))
        || (StringUtils.isNotBlank(dataGroupPsn.getGroupDescription())
            && !dataGroupPsn.getGroupDescription().equals(groupPsn.getGroupDescription()))) {
      dataGroupPsn.setGroupDescription(groupPsn.getGroupDescription());
    }
    if ((StringUtils.isBlank(dataGroupPsn.getGroupImgUrl())) || (StringUtils.isNotBlank(dataGroupPsn.getGroupImgUrl())
        && !dataGroupPsn.getGroupImgUrl().equals(groupPsn.getGroupImgUrl()))) {
      dataGroupPsn.setGroupImgUrl(groupPsn.getGroupImgUrl());
    }
    if ((StringUtils.isBlank(dataGroupPsn.getGroupPageUrl())) || (StringUtils.isNotBlank(dataGroupPsn.getGroupPageUrl())
        && !dataGroupPsn.getGroupPageUrl().equals(groupPsn.getGroupPageUrl()))) {
      dataGroupPsn.setGroupPageUrl(groupPsn.getGroupPageUrl());
    }
    if ((StringUtils.isBlank(dataGroupPsn.getDes3GroupNodeId()))
        || (StringUtils.isNotBlank(dataGroupPsn.getDes3GroupNodeId())
            && !dataGroupPsn.getDes3GroupNodeId().equals(groupPsn.getDes3GroupNodeId()))) {
      dataGroupPsn.setDes3GroupNodeId(groupPsn.getDes3GroupNodeId());
    }
    if ((StringUtils.isBlank(dataGroupPsn.getDes3GroupId())) || (StringUtils.isNotBlank(dataGroupPsn.getDes3GroupId())
        && !dataGroupPsn.getDes3GroupId().equals(groupPsn.getDes3GroupId()))) {
      dataGroupPsn.setDes3GroupId(groupPsn.getDes3GroupId());
    }
    if ((dataGroupPsn.getGroupNodeId() == null)
        || (dataGroupPsn.getGroupNodeId() != null && groupPsn.getGroupNodeId() != null
            && dataGroupPsn.getGroupNodeId().intValue() != groupPsn.getGroupNodeId().intValue())) {
      dataGroupPsn.setGroupNodeId(groupPsn.getGroupNodeId());
    }
    if ((StringUtils.isBlank(dataGroupPsn.getGroupUrl())) || (StringUtils.isNotBlank(dataGroupPsn.getGroupUrl())
        && !dataGroupPsn.getGroupUrl().equals(groupPsn.getGroupUrl()))) {
      dataGroupPsn.setGroupUrl(groupPsn.getGroupUrl());
    }
    if ((StringUtils.isBlank(dataGroupPsn.getGroupDescriptionClob()))
        || (StringUtils.isNotBlank(dataGroupPsn.getGroupDescriptionClob())
            && !dataGroupPsn.getGroupDescriptionClob().equals(groupPsn.getGroupDescriptionClob()))) {
      dataGroupPsn.setGroupDescriptionClob(groupPsn.getGroupDescriptionClob());
    }

    return dataGroupPsn;
  }
}
