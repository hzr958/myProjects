package com.smate.center.batch.service.mail;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.acegisecurity.util.EncryptionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnResSend;
import com.smate.center.batch.service.emailsimplify.EmailSimplify;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.psn.SyncPersonService;
import com.smate.center.batch.service.pub.mq.SyncMailToMailSrvMessage;
import com.smate.center.batch.service.pub.mq.SyncMailToMailSrvProducer;
import com.smate.center.batch.util.pub.BasicRmtSrvModuleConstants;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 科研之友发送邮件.
 * 
 * @author pwl
 * 
 */
@Service("scmEmailService")
@Transactional(rollbackFor = Exception.class)
public class ScmEmailServiceImpl implements ScmEmailService {


  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final String ENCODING = "utf-8";
  @Autowired
  private PersonManager personManager;
  @Autowired
  private SyncPersonService syncPersonService;
  @Autowired
  private InvitationBusinessMethod businessMethod;
  @Autowired
  private Configuration freemarkerConfiguration;
  @Autowired
  private EmailSimplify psnInsideMessageEmailService;
  @Autowired
  private SysDomainConst sysDomainConst;

  @Resource(name = "syncMailToMailSrvProducer")
  private SyncMailToMailSrvProducer syncMailToMailSrvProducer;

  @Autowired
  EmailSimplify psnPubShareEmailService;
  @Autowired
  EmailSimplify psnInsideEmailService;
  @Autowired
  EmailSimplify psnFileShareEmailService;

  @Override
  public void sendInsideMail(Long receiverId, Map<String, Object> mailParam) throws ServiceException {
    try {
      Person receiver = personManager.getPerson(receiverId);
      String languageVersion = this.getReceiverLanguage(receiver);
      String locale =
          StringUtils.isEmpty(receiver.getLocalLanguage()) || "zh_CN".equals(receiver.getLocalLanguage()) ? "zh_CN"
              : "en_US";
      String subject = "";
      String receiverName = "";
      if ("zh_CN".endsWith(languageVersion)) {
        receiverName = StringUtils.isBlank(receiver.getName()) ? receiver.getFirstName() + " " + receiver.getLastName()
            : receiver.getName();
        subject = ObjectUtils.toString(mailParam.get("zhSubject"));
        mailParam.put("senderName", mailParam.get("senderZhName"));
      } else {
        receiverName = receiver.getFirstName() + " " + receiver.getLastName();
        if (StringUtils.isBlank(receiver.getFirstName()) || StringUtils.isBlank(receiver.getLastName())) {
          receiverName = receiver.getName();
        }
        subject = ObjectUtils.toString(mailParam.get("enSubject"));
        mailParam.put("senderName", mailParam.get("senderEnName"));
      }
      mailParam.put("psnName", receiverName);

      String psnWebDomain = sysDomainConst.getSnsDomain();

      String viewUrl = psnWebDomain + mailParam.get("viewUrl") + "?locale=" + locale;
      if (StringUtils.isNotEmpty(ObjectUtils.toString(mailParam.get("casUrl")))) {
        String encodePwd = "CITE|" + receiverId;
        // webUrl
        String url = "https://" + sysDomainConst.getSnsDomain();
        if (sysDomainConst.getSnsContext() != null) {
          url = url + "/" + sysDomainConst.getSnsContext();
        }
        String targetUrl = url + ObjectUtils.toString(mailParam.get("targetUrl"));
        viewUrl = ObjectUtils.toString(mailParam.get("casUrl")) + "?submit=true&username=SHARE&password="
            + URLEncoder.encode(EncryptionUtils.encrypt("111111222222333333444444", encodePwd), "utf8") + "&service="
            + java.net.URLEncoder.encode(targetUrl, "UTF-8");
        mailParam.put("viewUrl", viewUrl);
        mailParam.remove("targetUrl");
        mailParam.remove("casUrl");
      }
      mailParam.put("viewUrl", viewUrl);

      if (StringUtils.isNotEmpty(ObjectUtils.toString(mailParam.get("mailSetUrl")))) {
        String mailSetUrl = psnWebDomain + mailParam.get("mailSetUrl") + "?locale=" + locale;
        mailParam.put("mailSetUrl", mailSetUrl);
      }

      String template = ObjectUtils.toString(mailParam.get("template")).replace("${locale}", languageVersion);
      mailParam.remove("msgType");
      mailParam.remove("template");
      mailParam.remove("isSendMsgMail");
      mailParam.remove("notSysUserTemplate");
      mailParam.remove("zhSubject");
      mailParam.remove("enSubject");
      mailParam.remove("senderZhName");
      mailParam.remove("senderEnName");
      mailParam.remove("extOtherInfo");
      String[] attachFileNames =
          mailParam.get("emailAttachFileName") == null ? null : (String[]) mailParam.get("emailAttachFileName");
      String[] attachFileUrl =
          mailParam.get("emailAttachFileUrl") == null ? null : (String[]) mailParam.get("emailAttachFileUrl");
      mailParam.remove("emailAttachFileName");
      mailParam.remove("emailAttachFileUrl");
      Map<String, Object> ctxMap = new HashMap<String, Object>();
      for (String key : mailParam.keySet()) {
        ctxMap.put(key, mailParam.get(key));
      }

      ctxMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
      ctxMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, receiver.getEmail());
      ctxMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, template);
      ctxMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, receiver.getPersonId());

      ctxMap.put(EmailConstants.EMAIL_FILENAME_KEY, attachFileNames);
      ctxMap.put(EmailConstants.Email_ATTACHNAME_KEY, attachFileUrl);

      this.sendInsideMail(ctxMap);

      // 老方法，先注释psnInsideEmailService.syncEmailInfo(ctxMap);
      // mailSendService.sendMail(receiver.getEmail(), subject, template,
      // attachFileNames, attachFileUrl, ctxMap);
    } catch (Exception e) {
      logger.error("发送短消息邮件出现异常：", e);
      throw new ServiceException();
    }
  }

  // TODO 发送站内信临时方法，不再调用反射
  public void sendInsideMail(Map<String, Object> ctxMap) {
    SyncMailToMailSrvMessage syncMessage = null;
    try {
      if (MapUtils.isNotEmpty(ctxMap)) {
        syncMessage = new SyncMailToMailSrvMessage();

        syncMessage.setMailType(EmailConstants.COMMON_EMAIL);
        // 邮件标识，成功或失败
        syncMessage.setMailFlag(EmailConstants.SUCCESS);
        // 邮件数据
        syncMessage.setMailJson(JacksonUtils.jsonObjectSerializer(ctxMap));
        syncMailToMailSrvProducer.syncMessage(syncMessage);
      }
    } catch (Exception e) {
      try {
        logger.error("整理／同步邮件数据到邮件服务时出错", e);
        syncMessage = new SyncMailToMailSrvMessage();
        // syncMessage.setFromNodeId(ServiceConstants.SCHOLAR_NODE_ID_1);
        syncMessage.setMailFlag(EmailConstants.FAILURE);
        syncMessage.setMailJson(JacksonUtils.jsonObjectSerializer(ctxMap));
        syncMailToMailSrvProducer.syncMessage(syncMessage);
      } catch (Exception e1) {
        logger.error("整理／同步邮件数据到邮件服务出错时，同步记录到邮件服务出错", e1);
      }
    }



  }


  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void sendAppTypeMail(String email, Map<String, Object> mailParam) throws ServiceException {
    try {
      String locale = LocaleContextHolder.getLocale().toString();
      String subject = "Suggested Application";
      String senderName = ObjectUtils.toString(mailParam.get("senderEnName"));
      if ("zh_CN".equals(locale)) {
        subject = "应用推荐";
        senderName = ObjectUtils.toString(mailParam.get("senderZhName"));
      }
      mailParam.put("senderName", senderName);
      String template = ObjectUtils.toString(mailParam.get("notSysUserTemplate")).replace("${locale}", locale);
      mailParam.remove("msgType");
      mailParam.remove("template");
      mailParam.remove("isSendMsgMail");
      mailParam.remove("notSysUserTemplate");
      mailParam.remove("zhSubject");
      mailParam.remove("enSubject");
      mailParam.remove("senderZhName");
      mailParam.remove("senderEnName");
      mailParam.remove("content");
      mailParam.remove("title");
      mailParam.remove("enTitle");
      mailParam.remove("viewMailUrl");
      mailParam.remove("targetUrl");
      mailParam.remove("casUrl");
      mailParam.remove("extOtherInfo");
      mailParam.remove("emailAttachFileName");
      mailParam.remove("emailAttachFileUrl");
      HashMap ctxMap = new HashMap();
      for (String key : mailParam.keySet()) {
        ctxMap.put(key, mailParam.get(key));
      }
      ctxMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
      ctxMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, email);
      ctxMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, template);
      psnInsideMessageEmailService.syncEmailInfo(ctxMap);
    } catch (Exception e) {
      logger.error("发送应用推广类站外邮件出现异常：", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void sendSharePubMail(Long receiverId, String email, String languageVersion, Long resRecId,
      PsnResSend psnResSend) throws ServiceException {
    try {
      HashMap ctxMap = new HashMap();
      Long senderId = psnResSend.getPsnId();
      Person sender = this.personManager.getPerson(senderId);
      Person receiver = null;
      if (receiverId != null) {
        receiver = personManager.getPerson(receiverId);
        if (receiver != null) {
          email = receiver.getEmail();
          if (StringUtils.isBlank(languageVersion)) {
            languageVersion = this.getReceiverLanguage(receiver);
          }
          ctxMap.put("recvName", this.personManager.getPsnName(receiver, languageVersion));
        }
      }

      ctxMap.put("psnName", this.personManager.getPsnName(sender, languageVersion));
      int resTotal = psnResSend.getPsnResSendResList().size();
      ctxMap.put("total", String.valueOf(resTotal));
      ctxMap.put("type", ObjectUtils.toString(psnResSend.getResType()));
      String pubZhTitle = psnResSend.getPsnResSendResList().get(0).getResZhTitle();
      String pubEnTitle = psnResSend.getPsnResSendResList().get(0).getResEnTitle();
      if ("zh_CN".equals(languageVersion)) {
        // SCM-3891 解决标题中自带了双引号会显示成乱码, --zk_2013-11-16
        pubZhTitle = StringEscapeUtils.unescapeHtml4(StringUtils.isEmpty(pubZhTitle) ? pubEnTitle : pubZhTitle);
        String minZhShareTitle =
            (StringUtils.length(pubZhTitle) > 30 && resTotal > 1) ? ((StringUtils.substring(pubZhTitle, 0, 30)) + "...")
                : pubZhTitle;
        ctxMap.put("minZhShareTitle", "“" + minZhShareTitle + "”");
      } else {
        // SCM-3891 解决标题中自带了双引号会显示成乱码, --zk_2013-11-16
        pubEnTitle = StringEscapeUtils.unescapeHtml4(StringUtils.isEmpty(pubEnTitle) ? pubZhTitle : pubEnTitle);
        String minEnShareTitle =
            (StringUtils.length(pubEnTitle) > 30 && resTotal > 1) ? ((StringUtils.substring(pubEnTitle, 0, 30)) + "...")
                : pubEnTitle;
        ctxMap.put("minEnShareTitle", "\"" + minEnShareTitle + "\"");
      }
      ctxMap.put("tmpUrl", "Share_Mail_Title_Template_" + languageVersion + ".ftl");
      String subject = this.buildEmailTitle(ctxMap);
      ctxMap.put("mailContext", "");
      if (StringUtils.isNotEmpty(psnResSend.getRecommendReason())) {
        ctxMap.put("recommendReason", psnResSend.getRecommendReason());
      }
      String sid = this.getEncodeEmailSid(psnResSend.getSendId(), resRecId,
          BasicRmtSrvModuleConstants.SNS_MODULE_ID.intValue(), senderId, email, receiverId);
      // webUrl
      String url = "https://" + sysDomainConst.getSnsDomain();
      if (sysDomainConst.getSnsContext() != null) {
        url = url + "/" + sysDomainConst.getSnsContext();
      }
      String emailUrl = url + "/commend/publication/pubview.action?sid=" + sid + "&locale=" + languageVersion;
      ctxMap.put("viewUrl", emailUrl);
      String mailTemplate = "Notify_Share_Template_" + languageVersion + ".ftl";

      ctxMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
      ctxMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, email);
      ctxMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
      if (receiver != null && receiver.getPersonId() != null) {
        ctxMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, receiver.getPersonId());
      }

      psnPubShareEmailService.syncEmailInfo(ctxMap);
      // mailSendService.sendMail(email, subject, mailTemplate, ctxMap);
    } catch (Exception e) {
      logger.error("分享成果发送成果分享邮件出现异常：", e);
      throw new ServiceException();
    }
  }

  @Override
  public void sendShareFileMail(Long receiverId, String email, String languageVersion, Long resRecId,
      PsnResSend psnResSend) throws ServiceException {
    try {
      Map<String, Object> ctxMap = new HashMap<String, Object>();
      Long senderId = psnResSend.getPsnId();
      Person sender = this.personManager.getPerson(senderId);
      Person receiver = null;
      if (receiverId != null) {
        receiver = personManager.getPerson(receiverId);
        if (receiver != null) {
          email = receiver.getEmail();
          if (StringUtils.isBlank(languageVersion)) {
            languageVersion = this.getReceiverLanguage(receiver);
          }
          ctxMap.put("recvName", this.personManager.getPsnName(receiver, languageVersion));
        }
      }

      ctxMap.put("psnName", this.personManager.getPsnName(sender, languageVersion));
      int resTotal = psnResSend.getPsnResSendResList().size();
      ctxMap.put("total", String.valueOf(resTotal));
      ctxMap.put("type", ObjectUtils.toString(psnResSend.getResType()));
      String fileName = psnResSend.getPsnResSendResList().get(0).getResZhTitle();
      if ("zh_CN".equals(languageVersion)) {
        ctxMap.put("minZhShareTitle", "“" + fileName + "”");
      } else {
        ctxMap.put("minEnShareTitle", "\"" + fileName + "\"");
      }
      ctxMap.put("tmpUrl", "Share_Mail_Title_Template_" + languageVersion + ".ftl");
      String subject = this.buildEmailTitle(ctxMap);
      ctxMap.put("mailContext", "");
      if (StringUtils.isNotEmpty(psnResSend.getRecommendReason())) {
        ctxMap.put("recommendReason", psnResSend.getRecommendReason());
      }

      String sid = this.getEncodeEmailSid(psnResSend.getSendId(), resRecId,
          BasicRmtSrvModuleConstants.SNS_MODULE_ID.intValue(), senderId, email, receiverId);
      // webUrl
      String url = "https://" + sysDomainConst.getSnsDomain();
      if (sysDomainConst.getSnsContext() != null) {
        url = url + "/" + sysDomainConst.getSnsContext();
      }
      String emailUrl = url + "/commend/emailViewFiles?sid=" + sid + "&locale=" + languageVersion;
      ctxMap.put("viewUrl", emailUrl);
      String mailTemplate = "Notify_Share_Template_" + languageVersion + ".ftl";

      ctxMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
      ctxMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, email);
      ctxMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, mailTemplate);
      if (receiver != null && receiver.getPersonId() != null) {
        ctxMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, receiver.getPersonId());
      }

      psnFileShareEmailService.syncEmailInfo(ctxMap);
      // mailSendService.sendMail(email, subject, mailTemplate, ctxMap);
    } catch (Exception e) {
      logger.error("分享文件发送文件分享邮件出现异常：", e);
      throw new ServiceException();
    }
  }

  /**
   * 获取用户邮件接收设置的语言环境.
   * 
   * @param person
   * @return
   * @throws ServiceException
   */
  @Override
  public String getReceiverLanguage(Person person) throws ServiceException {
    String language = person.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = businessMethod.getLocale(person.getPersonId());
    }
    return language;
  }

  private String getEncodeEmailSid(Long resSendId, Long resRecId, Integer nodeId, Long senderId, String email,
      Long receiverId) throws ServiceException {
    String tmp = String.valueOf(resSendId) + "," + nodeId + "," + senderId + "," + email + ","
        + (receiverId == null ? "" : String.valueOf(receiverId)) + "," + (resRecId == null ? "" : resRecId);
    return ServiceUtil.encodeToDes3(tmp);
  }

  private String buildEmailTitle(Map<String, Object> map) throws ServiceException {
    String msg = "";
    try {
      msg = FreeMarkerTemplateUtils.processTemplateIntoString(
          freemarkerConfiguration.getTemplate(ObjectUtils.toString(map.get("tmpUrl")), ENCODING), map);
    } catch (IOException e) {
      logger.error("构建Email标题失败，没有找到对应的Email标题模板！", e);
    } catch (TemplateException e) {
      logger.error("构造Email标题失败,FreeMarker处理失败", e);
    }
    return msg;
  }
}
