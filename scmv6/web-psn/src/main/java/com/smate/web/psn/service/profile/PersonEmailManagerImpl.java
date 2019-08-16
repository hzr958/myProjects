package com.smate.web.psn.service.profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.psn.dao.AccountEmailCheckLogDao;
import com.smate.core.base.psn.model.AccountEmailCheckLog;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.cache.PsnCacheService;
import com.smate.web.psn.dao.profile.PersonEmailDao;
import com.smate.web.psn.dao.psn.PdwhAddrPsnUpdateRecordDao;
import com.smate.web.psn.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.web.psn.dao.rol.PsnPmEmailDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.AccountEmailForm;
import com.smate.web.psn.form.PersonEmailInfo;
import com.smate.web.psn.model.pdwh.pub.PdwhAddrPsnUpdateRecord;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;
import com.smate.web.psn.model.rcmdsync.RcmdSyncPsnInfo;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;
import com.smate.web.psn.service.email.AccountEmailCheckLogService;
import com.smate.web.psn.service.personsync.PersonSyncService;
import com.smate.web.psn.service.psnhtml.PsnHtmlRefreshService;
import com.smate.web.psn.service.user.UserService;

/**
 * PersonEmailManagerImpl.
 * 
 * @author new .
 *
 */
@Service("personEmailManager")
@Transactional(rollbackFor = Exception.class)
public class PersonEmailManagerImpl implements PersonEmailManager {

  private static final Integer SEND_EMAIL_MAX_TIME_INTERVAL = 60; // 60秒
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final String CACHE_NAME = "sys_user_cache";
  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private PsnPmEmailDao psnPmEmailDao;
  @Autowired
  private UserService userService;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnCacheService psnCacheService;
  @Autowired
  private PsnHtmlRefreshService psnHtmlRefreshService;
  @Autowired
  private PsnPrivateService psnPrivateService;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private PersonSyncService personSyncService;
  @Value("${domainscm}")
  private String domainscm;
  @Resource
  private AccountEmailCheckLogService accountEmailCheckLogService;
  @Autowired
  private AccountEmailCheckLogDao accountEmailCheckLogDao;
  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;
  @Autowired
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;
  @Autowired
  private PdwhAddrPsnUpdateRecordDao pdwhAddrPsnUpdateRecordDao;

  /** 获取首要邮件 */
  @Override
  public String getFristMail(Long psnId) throws ServiceException {
    try {
      return personEmailDao.getfirstMail(psnId);
    } catch (DaoException e) {
      logger.error("通过PSNID查获取用户的首要邮件出错 psnId: " + psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> isExitEmailVerify(String email) throws ServiceException {
    return personEmailDao.isExitEmailVerify(email);
  }

  /** 更新首要邮件的确认状态 */
  @Override
  public void updateLoginEmailVerifyStatus(Long psnId, String email) throws ServiceException {
    PersonEmailRegister personEmail = personEmailDao.getPsnEmail(psnId, email);
    if (personEmail != null) {
      if (personEmail.getIsVerify() != null && personEmail.getIsVerify() != 1) {
        personEmail.setIsVerify(1L);
        personEmailDao.save(personEmail);
      }
    }
  }

  /**
   * 添加电子邮件，如果邮件已存在，则返回-1， -2表示被人占用正确放回EMAIL_ID.
   * 
   * @param email
   * @throws ServiceException
   */
  public Long addEmail(String email, Long psnId, Long isFirstMail, Long isLoginMail) throws ServiceException {
    try {
      email = email.toLowerCase();

      List<Long> psnIdList = findPsnIdByEmail(email);
      // 如果该用户已存在该邮件
      if (psnIdList != null && psnIdList.contains(psnId)) {
        return -1L;
      }
      // isUsedByOther
      if (psnIdList != null && psnIdList.size() > 0) {
        return -2L;
      }
      Person person = personDao.get(psnId);
      PersonEmailRegister personEmail = new PersonEmailRegister();
      personEmail.setEmail(email);
      String[] split = email.split("@");
      if (split != null && split.length == 2) {
        personEmail.setLeftPart(split[0]);
        personEmail.setRightPart(split[1]);
      }
      personEmail.setFirstMail(0L);
      personEmail.setIsVerify(0L);
      personEmail.setLoginMail(0L);
      personEmail.setPerson(person);

      this.personEmailDao.save(personEmail);
      // 保存同步信息 标记用户邮箱需刷新
      flagUserNeedFreshEmail(psnId);
      // 添加邮件，要发送验证码邮件
      AccountEmailForm form = new AccountEmailForm();
      form.setValidatePsnId(psnId);
      form.setNewEmail(email);
      accountEmailCheckLogService.reSendAccountEmailValidateEmail(form);
      if (form.getHasConfirm()) {
        personEmail.setIsVerify(1L);
        this.personEmailDao.save(personEmail);
      }
      pdwhAddrPsnUpdateRecordDao.deleteRecordByPsnId(psnId);
      PdwhAddrPsnUpdateRecord record = new PdwhAddrPsnUpdateRecord(psnId, 2, 0);
      pdwhAddrPsnUpdateRecordDao.save(record);
      return personEmail.getId();
    } catch (Exception e) {
      logger.error("添加邮件出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 检索用户是否已存在具体邮件.
   * 
   * @param psnId
   * @param email
   * @return
   * @throws ServiceException
   */
  public Boolean isEmailExit(Long psnId, String email) throws ServiceException {

    try {
      return this.personEmailDao.isEmailExit(psnId, email);
    } catch (DaoException e) {
      logger.error("检索用户是否已存在具体邮件.email: " + email, e);
      throw new ServiceException(e);
    }
  }


  /**
   * 检索邮件被使用的psnId
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  public List<Long> findPsnIdByEmail(String email) throws ServiceException {

    try {
      return this.personEmailDao.findPsnIdByEmail(email);
    } catch (DaoException e) {
      logger.error("检索邮件被使用的psnId.email: " + email, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public int delete(Long emailId, Long psnId) throws ServiceException {
    try {

      PersonEmailRegister mail = personEmailDao.get(emailId);
      // 不是该用户的或者是首要邮件或者是登录帐号，不能删除
      if (mail == null || !mail.getPerson().getPersonId().equals(psnId) || mail.getFirstMail() == 1
          || mail.getLoginMail() == 1) {
        return 0;
      }
      int result = personEmailDao.deletePersonEmail(emailId);
      // 删除同步到ROL的psn-email表记录
      if (mail != null && mail.getIsVerify() != null && mail.getIsVerify() == 1) {
        if (!StringUtils.isBlank(mail.getEmail())) {
          String email = mail.getEmail().toLowerCase();
          psnPmEmailDao.removePsnPmEmail(email, psnId);
        }
      }
      // 保存同步信息 标记用户邮箱需刷新
      flagUserNeedFreshEmail(psnId);
      return 1;
    } catch (Exception e) {
      logger.error("删除邮件出错", e);
      throw new ServiceException(e);
    }
  }

  // 保存同步信息 标记用户邮箱需刷新
  private void flagUserNeedFreshEmail(Long psnId) {
    RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
    if (rsp == null) {
      rsp = new RcmdSyncPsnInfo(psnId);
    }
    rsp.setEmailFlag(1);
    rcmdSyncPsnInfoDao.save(rsp);
  }

  @Override
  public int updateFirstEmail(Long emailId, boolean needMail) throws ServiceException {
    Boolean flag = false;
    Long psnId = SecurityUtils.getCurrentUserId();
    // 更新person email字段.
    try {
      PersonEmailRegister email = personEmailDao.get(emailId);
      // 1: 不是自己的邮箱
      if (psnId == null || psnId == 0L || psnId.longValue() != email.getPerson().getPersonId().longValue()) {
        return 0;
      }
      // 2:在判断该邮箱是否被其他人设置为首要邮件或者登录帐号
      Boolean used = personEmailDao.emailHasOtherUsed(psnId, email.getEmail());
      if (used) {
        return -2;
      }
      // 3：在判断该邮箱 是否被验证
      Boolean hasConfirm = accountEmailCheckLogService.findHasConfirm(psnId, email.getEmail());
      if (!hasConfirm) {
        return -1;
      }

      String strEmail = email.getEmail();
      // 同步sys_user表
      int result = userService.updateFirstEmail(strEmail, psnId);
      if (result == 0) {
        return result;
      }
      // 更新首要邮件/登录名成功
      if (result == 1) {
        this.personEmailDao.setFirstLoginMail(emailId);
        flag = true;
      } else {
        // 更新首要邮件成功
        this.personEmailDao.setFirstMail(emailId);
      }

      Person person = personDao.get(psnId);
      String oldAccount = person.getEmail();
      person.setEmail(strEmail);
      personDao.save(person);
      // 标记用户邮箱需刷新
      // 保存同步信息 标记用户邮箱需刷新
      flagUserNeedFreshEmail(psnId);
      // 同步psn_ins表数据
      int isPrivate = psnPrivateService.isPsnPrivate(person.getPersonId()) ? 1 : 0;
      person.setIsPrivate(isPrivate);
      // 构造同步信息
      Integer curNodeId = SecurityUtils.getCurrentAllNodeId().get(0);
      Locale locale = LocaleContextHolder.getLocale();
      SnsPersonSyncMessage message =
          new SnsPersonSyncMessage(person, locale.getLanguage(), curNodeId, curNodeId, person.getSex());
      // message.setInsId(person.getSyncInsId());
      // 开始同步
      this.personSyncService.snsPersonSync(message);
      // 清除本地缓存
      psnCacheService.remove(CACHE_NAME, psnId.toString());
      // 是否需要发送邮件
      if (needMail) {
        // 发送邮件
        restSendUpdateFirstEmail(person, flag);
      }
      // 给原来帐号发送一封通知邮件
      sendAccountModifyNotify(person, oldAccount);
      // 邮箱变动时，需刷新人员Html列表
      // psnHtmlRefreshService.updatePsnHtmlRefresh(psnId);
      return result;
    } catch (Exception e) {
      logger.error("设置首要邮件出错: ", e);
      throw new ServiceException(e);
    }
  }

  public void restSendUpdateFirstEmail(Person person, Boolean flag) {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String psName = null;
    // 获取用户设置接收邮件的语言
    String languageVersion = person.getEmailLanguageVersion();
    if (languageVersion == null) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }
    Integer tempcode = flag ? 10001 : 10000;
    info.setSenderPsnId(0L);// 0是系统发送
    info.setReceiverPsnId(person.getPersonId());
    info.setReceiver(person.getEmail());
    info.setMsg("更新首要邮件通知邮件");
    info.setMailTemplateCode(tempcode);
    if (Locale.CHINA.toString().equals(languageVersion)) {
      psName = person.getName();
      if (StringUtils.isBlank(psName)) {
        psName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName())) {
        psName = person.getName();
      }
    }
    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    mailData.put("zh_CN_psnname", psName);
    mailData.put("toemail", person.getEmail());
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  private void sendAccountModifyNotify(Person person, String oldAccount) {
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    info.setReceiver(oldAccount);// 接收邮箱
    info.setReceiverPsnId(person.getPersonId());// 接收人psnId，0=非科研之友用户
    info.setSenderPsnId(0L);// 发送人psnId，0=系统邮件
    info.setPriorLevel("A");// 优先级 ABCD
    info.setMailTemplateCode(10105);// 模版标识，参考V_MAIL_TEMPLATE
    info.setMsg("登录帐号修改通知");// 描述
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    // TODO 邮件模版需要的其他参数可以继续往mailData添加,如：
    mailData.put("username", getPersonName(person, "zh_CN"));
    mailData.put("oldAccount", oldAccount);
    SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    String modifyDate = df.format(new Date());
    mailData.put("modifyDate", modifyDate);
    mailData.put("newAccount", person.getEmail());

    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    // 调用服务
    Map<String, String> resutlMap = null;
    try {
      resutlMap = mailHandleOriginalDataService.doHandle(paramData);
    } catch (Exception e) {
      logger.error("构建修改登录帐号邮件异常:psnId=" + person.getPersonId(), e);
    }
    if ("success".equals(resutlMap.get("result"))) {
      // 构造邮件成功
    } else {
      // 构造邮件失败
      logger.error(resutlMap.get("msg"));
    }
  }


  @Override
  public List<PersonEmailRegister> findPersonEmailList() throws ServiceException {
    Long personId = SecurityUtils.getCurrentUserId();
    try {
      List<PersonEmailRegister> list = personEmailDao.findListByPersonId(personId);
      return list;
    } catch (DaoException e) {
      logger.error("取邮件出错", e);
      throw new ServiceException();
    }
  }

  @Override
  public List<PersonEmailInfo> findPersonEmailInfoList() throws ServiceException {
    Long personId = SecurityUtils.getCurrentUserId();
    try {
      List<PersonEmailInfo> listInfo = new ArrayList<>();
      List<PersonEmailRegister> list = personEmailDao.findListByPersonId(personId);
      if (list != null && list.size() > 0) {
        for (PersonEmailRegister email : list) {
          PersonEmailInfo info = new PersonEmailInfo();
          info.setId(email.getId());
          info.setEmail(email.getEmail());
          info.setFirstMail(email.getFirstMail());
          info.setLoginMail(email.getLoginMail());
          info.setIsVerify(email.getIsVerify());
          // 未验证的，判断是否发送了验证邮件
          if (info.getIsVerify() == null || info.getIsVerify() != 1) {
            AccountEmailCheckLog emailCheckLog =
                accountEmailCheckLogDao.checkValidate(email.getPerson().getPersonId(), email.getEmail());
            if (emailCheckLog != null) {
              info.setResend(true);
              info.setSendDate(emailCheckLog.getSendTime().getTime());
              Long intevalTime = new Date().getTime() - emailCheckLog.getSendTime().getTime();
              intevalTime = intevalTime / 1000;
              if (intevalTime < 60) {
                info.setDelaySendDate(SEND_EMAIL_MAX_TIME_INTERVAL - intevalTime);
              }
            }
          }
          listInfo.add(info);
        }
      }
      return listInfo;
    } catch (DaoException e) {
      logger.error("取邮件出错", e);
      throw new ServiceException();
    }
  }

  @Override
  public void sendConfirmEmail(Long mailId) throws ServiceException {
    Map<String, Object> map = new HashMap<String, Object>();
    Long personId = SecurityUtils.getCurrentUserId();
    Person person = personDao.get(personId);
    PersonEmailRegister personEmail = personEmailDao.get(mailId);
    if (personEmail == null || personEmail.getPerson().getPersonId().longValue() != personId.longValue()) {
      logger.error("该邮件不是你的 psnId=" + personId + " ,email=" + personEmail.getEmail());
      throw new ServiceException();
    }
    // 构建确认邮件
    try {
      buildSendEmailMapInfo(map, person, personEmail);
      // 发送确认邮件
      mailInitDataService.saveMailInitData(map);
    } catch (Exception e) {
      logger.error("发送确认邮件异常psnId=" + personId, e);
      throw new ServiceException();
    }

  }

  /**
   * {"email_receive_psnId":1000000733255,"email_receiveEmail":"kity@163.com",
   * "email_Template":"Person_Email_Affirm_Template_zh_CN.ftl", "keycode_app_baseUrl":
   * "https://dev.scholarmate.com/scmwebsns/confirmEmail/confirm?des3Id=DvZAhf1SNOAqe%2BCbmOK98g%3D%3D&locale=zh_CN"
   * , "email_subject":"邮件验证","zh_CN_psnname":"艾江斌3", "toemail":"kity@163.com"}
   * 
   * @param map
   */
  public void buildSendEmailMapInfo(Map<String, Object> map, Person receiver, PersonEmailRegister personEmail)
      throws Exception {
    if (receiver == null) {
      throw new Exception("构建全文回复，邮件对象为空" + this.getClass().getName());
    }
    String languageVersion = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(languageVersion)) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }

    map.put("zh_CN_psnname", getPersonName(receiver, languageVersion));

    map.put("keycode_app_baseUrl", this.domainscm + "/scmwebsns/confirmEmail/confirm?des3Id="
        + ServiceUtil.encodeToDes3(personEmail.getId().toString()) + "&locale=" + languageVersion);

    if ("zh".equalsIgnoreCase(languageVersion) || "zh_CN".equalsIgnoreCase(languageVersion)) {
      map.put(EmailConstants.EMAIL_SUBJECT_KEY, "请确认您的电子邮件地址 – 科研之友");
      map.put(EmailConstants.EMAIL_TEMPLATE_KEY, "Person_Email_Affirm_Template_zh_CN.ftl");
    } else {
      map.put(EmailConstants.EMAIL_SUBJECT_KEY, "Please confirm your email address - ScholarMate");
      map.put(EmailConstants.EMAIL_TEMPLATE_KEY, "Person_Email_Affirm_Template_en_US.ftl");
    }
    map.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, receiver.getPersonId());
    map.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, personEmail.getEmail());
  }

  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  String getPersonName(Person person, String language) {
    if ("zh".equals(language) || "zh_CN".equals(language)) {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getEname();
      }
    } else {
      if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getName();
      }

    }
  }

  @Override
  public PersonEmailRegister findPsnEmailById(Long mailId) throws ServiceException {
    PersonEmailRegister personEmailRegister = personEmailDao.get(mailId);
    return personEmailRegister;
  }

  @Override
  public boolean validateEmailHasUsed(Long psnId, Long mailId) throws ServiceException {
    // 在判断该邮箱是否被其他人设置为首要邮件或者登录帐号
    PersonEmailRegister psnEmail = personEmailDao.get(mailId);
    Boolean used = personEmailDao.emailHasOtherUsed(psnId, psnEmail.getEmail());
    return used;
  }

  public static void main(String[] args) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日hh时mm分");
    String modifyDate = df.format(new Date());
    System.out.println(modifyDate);
  }

}
