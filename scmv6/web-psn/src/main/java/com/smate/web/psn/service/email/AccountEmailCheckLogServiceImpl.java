package com.smate.web.psn.service.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.psn.dao.AccountEmailCheckLogDao;
import com.smate.core.base.psn.model.AccountEmailCheckLog;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.random.RandomNumber;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.psn.dao.profile.PersonEmailDao;
import com.smate.web.psn.form.AccountEmailForm;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;
import com.smate.web.psn.service.profile.PsnInfoImproveService;

/**
 * 
 * @author aijiangbin
 *
 */
@Service("accountEmailCheckLogService")
@Transactional(rollbackFor = Exception.class)
public class AccountEmailCheckLogServiceImpl implements AccountEmailCheckLogService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final Integer SEND_EMAIL_MAX_TIME_INTERVAL = 60; // 60秒

  public final static Long ONE_DAY = 24 * 60 * 60 * 1000L;

  @Autowired
  private AccountEmailCheckLogDao accountEmailCheckLogDao;

  @Autowired
  private UserDao userDao;
  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private MailInitDataService mailInitDataService;

  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PsnInfoImproveService psnInfoImproveService;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  /**
   * 是否需要验证邮箱
   * 
   * @param form
   * @return
   * @throws Exception
   */
  @Override
  public Boolean needValidateAccount(AccountEmailForm form) throws Exception {



    // 如果没有完善个人信息，先不要验证邮箱
    Map<String, Boolean> needImprove = psnInfoImproveService.psnHasScienceAreaAndKeywords(form.getCurrentPsnId());
    // 需要，则查询人员分类信息，自动选择科技领域和关键词用
    if (needImprove != null && needImprove.get("needArea") != null && needImprove.get("needKeyword") != null
        && needImprove.get("needWorkEdu") != null) {
      boolean needArea = needImprove.get("needArea");
      boolean needKeyWords = needImprove.get("needKeyword");
      boolean needWorkEdu = needImprove.get("needWorkEdu");
      if (needArea || needKeyWords || needWorkEdu) {
        return false;
      }

    }
    // 只有科研只有注册的账号 才要验证， 2019-04-02 SCM-24372
    Person person = personDao.get(form.getCurrentPsnId());
    if (!"sns".equalsIgnoreCase(person.getFromSys()) && StringUtils.isNotBlank(person.getFromSys())) {
      return false;
    }
    // 已经验证了就直接返回
    User user = userDao.get(form.getCurrentPsnId());
    PersonEmailRegister psnEmail = personEmailDao.getPsnEmail(user.getId(), user.getLoginName());
    if (psnEmail != null && psnEmail.getIsVerify().longValue() == 1) {
      return false;
    }
    AccountEmailCheckLog accountEmailCheckLog = getNotDealAccountEmailCheckLog(form.getCurrentPsnId());
    // 有记录就要弹框， 没有就不要弹框
    // 1 没有记录，需要发送邮件记录 ，需要弹框 。 2 有记录的，没有验证，或者验证失败需要弹框
    if (accountEmailCheckLog != null && person != null && person.getEmail().equals(accountEmailCheckLog.getAccount())) {
      form.setNewEmail(accountEmailCheckLog.getAccount());
      form.setSendEmailDate(accountEmailCheckLog.getSendTime().getTime());
      Long intevalTime = new Date().getTime() - accountEmailCheckLog.getSendTime().getTime();
      intevalTime = intevalTime / 1000;
      if (intevalTime < 60) {
        form.setDelaySendDate(SEND_EMAIL_MAX_TIME_INTERVAL - intevalTime);
      }
      return true;
    } else if (accountEmailCheckLog == null) {
      form.setValidatePsnId(form.getCurrentPsnId());
      form.setNewEmail(user.getLoginName());
      sendAccountEmailValidateEmail(form);
      return true;
    } else {
      return false;
    }

  }

  /**
   * 通过邮箱验证吗，验证 , 通过邮箱 弹框验证吗，验证 , 0=未处理 ， 1验证成功 ， 9=验证失败 ， 2=重新发送
   * 
   * @param form
   * @return
   * @throws Exception
   */
  @Override
  public Integer daValidateByEmail(AccountEmailForm form) throws Exception {
    AccountEmailCheckLog accountEmailCheckLog = accountEmailCheckLogDao.get(form.getId());
    Integer result = 0;
    if (accountEmailCheckLog != null && accountEmailCheckLog.getDealStatus() == 0) {
      // 已经验证成功
      result = 1;
      // 1= 邮件链接验证
      updateAccountEmailCheckLog(accountEmailCheckLog, result, 1);
    } else if (accountEmailCheckLog != null
        && (accountEmailCheckLog.getDealStatus() == 2 || accountEmailCheckLog.getDealStatus() == 1)) {
      // 验证成功，或者重新发送的邮件，则该链接失效
      result = 2;
    } else {
      result = 9;
    }
    return result;
  }



  /**
   * 通过邮箱 弹框验证吗，验证 查找未处理的记录
   * 
   * @param currentPsnId
   * @return
   */
  public AccountEmailCheckLog getNotDealAccountEmailCheckLog(Long currentPsnId) {
    String loginName = userDao.getLoginNameById(currentPsnId);
    AccountEmailCheckLog accountEmailCheckLog = accountEmailCheckLogDao.checkValidate(currentPsnId, loginName);
    return accountEmailCheckLog;
  }

  /**
   * 通过邮箱 弹框验证吗，验证 , 0=未处理 ， 1验证成功 ， 9=验证码错误 ， 2=重新发送
   * 
   * @param form
   * @return
   * @throws Exception
   */
  @Override
  public Integer daValidateByCode(AccountEmailForm form) throws Exception {
    AccountEmailCheckLog accountEmailCheckLog = getNotDealAccountEmailCheckLog(form.getCurrentPsnId());
    Integer result = 0;
    if (StringUtils.isNotBlank(form.getValidateCode())
        && form.getValidateCode().equals(accountEmailCheckLog.getValidateCode())) {
      result = 1;
      // 验证成功，更新验证码
      updateAccountEmailCheckLog(accountEmailCheckLog, result, 2);
    } else if (!form.getValidateCode().equals(accountEmailCheckLog.getValidateCode())) {
      result = 9;
    }
    return result;
  }

  /**
   * 个人设置 验证邮箱 0=未处理 ， 1验证成功 ， 9=验证码错误
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public Integer psnSetDoValidateEmail(AccountEmailForm form) throws Exception {
    AccountEmailCheckLog accountEmailCheckLog =
        accountEmailCheckLogDao.checkValidate(form.getCurrentPsnId(), form.getNewEmail());
    Integer result = 0;
    if (StringUtils.isNotBlank(form.getValidateCode())
        && form.getValidateCode().equals(accountEmailCheckLog.getValidateCode())) {
      result = 1;
      // 验证成功，更新验证码
      accountEmailCheckLog.setDealStatus(1);
      accountEmailCheckLog.setDealType(2);
      accountEmailCheckLog.setDealTime(new Date());
      accountEmailCheckLogDao.save(accountEmailCheckLog);
      // 更新确认邮箱
      personEmailDao.updateValidateEmailState(accountEmailCheckLog.getPsnId(), accountEmailCheckLog.getAccount(), 1L);
    } else if (!form.getValidateCode().equals(accountEmailCheckLog.getValidateCode())) {
      result = 9;
    }
    return result;
  }

  /**
   * 
   * @param accountEmailCheckLog
   * @param dealStatus
   * @param dealType 1邮件链接验证， 2系统弹框验证
   */
  public void updateAccountEmailCheckLog(AccountEmailCheckLog accountEmailCheckLog, Integer dealStatus,
      Integer dealType) {
    if (accountEmailCheckLog == null) {
      return;
    }
    accountEmailCheckLog.setDealStatus(dealStatus);
    accountEmailCheckLog.setDealType(dealType);
    accountEmailCheckLog.setDealTime(new Date());
    accountEmailCheckLogDao.save(accountEmailCheckLog);
    // 账户邮件验证成功 ,更新psn_email表
    if (dealStatus == 1) {
      personEmailDao.updateValidateEmailState(accountEmailCheckLog.getPsnId(), accountEmailCheckLog.getAccount(), 1L);
      /*
       * 确认邮件不需要 更新 首要邮件和登录邮件 2018-09-13 ajb //清空首要登录邮件
       * personEmailDao.clearFirstLoginEmail(accountEmailCheckLog.getPsnId()); PersonEmailRegister
       * psnEmail = personEmailDao.getPsnEmail(accountEmailCheckLog.getPsnId(),
       * accountEmailCheckLog.getAccount() ); if(psnEmail == null ) { psnEmail = new
       * PersonEmailRegister(); psnEmail.setEmail(accountEmailCheckLog.getAccount());
       * psnEmail.setPerson(personDao.get(accountEmailCheckLog.getPsnId()));
       * psnEmail.setLeftPart(accountEmailCheckLog.getAccount().split("@")[0]);
       * psnEmail.setRightPart(accountEmailCheckLog.getAccount().split("@")[1]); }
       * psnEmail.setFirstMail(1L); psnEmail.setLoginMail(1L); psnEmail.setIsVerify(1L);;
       * personEmailDao.save(psnEmail); //更新person 表的首要邮箱
       * personDao.updateEmailByPsnId(accountEmailCheckLog.getPsnId(), accountEmailCheckLog.getAccount());
       */
    }
  }

  /**
   * 发送账户邮箱验证邮件
   * 
   * @param form
   */
  @Override
  public Boolean sendAccountEmailValidateEmail(AccountEmailForm form) {

    AccountEmailCheckLog accountEmailCheckLog = saveAccountEmailCheckLog(form);
    Person receiver = personDao.get(form.getValidatePsnId());
    try {
      // 调用接口发送邮件 新的邮件方式
      restTemplateSendMail(receiver, accountEmailCheckLog);
      return true;
    } catch (Exception e) {
      logger.error("构建账号邮件，确认邮件异常+psnId=" + form.getValidatePsnId(), e);
    }
    return false;
  }

  private void restTemplateSendMail(Person receiver, AccountEmailCheckLog accountEmailCheckLog) throws Exception {
    // 全文请求使用新模板
    if (receiver == null) {
      throw new Exception("构建账户邮件确认邮件，邮件对象为空" + this.getClass().getName());
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    Integer templateCode = 10073;
    info.setReceiver(accountEmailCheckLog.getAccount());// 接收邮箱
    info.setSenderPsnId(0L);// 发送人psnId，0=系统邮件
    info.setReceiverPsnId(receiver.getPersonId());// 接收人psnId，0=非科研之友用户
    info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
    info.setMsg("确认邮件地址");// 描述
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    String confirmUrl = this.domainscm + "/psnweb/accountvalidate/ajaxdovalidte?" + "des3Id="
        + Des3Utils.encodeToDes3(accountEmailCheckLog.getId().toString());
    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友主页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("confirmUrl");
    l2.setUrl(confirmUrl);
    l2.setUrlDesc("确认地址");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    mailData.put("psnName", PsnInfoUtils.getPersonName(receiver, language));
    mailData.put("confirmCode", accountEmailCheckLog.getValidateCode());
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);

  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW,
      rollbackForClassName = {"java.lang.Exception"})
  public AccountEmailCheckLog saveAccountEmailCheckLog(AccountEmailForm form) {
    // 保存记录
    AccountEmailCheckLog accountEmailCheckLog = new AccountEmailCheckLog();
    accountEmailCheckLog.setPsnId(form.getValidatePsnId());
    accountEmailCheckLog.setAccount(form.getNewEmail());
    accountEmailCheckLog.setDealStatus(0);
    accountEmailCheckLog.setSendTime(new Date());
    String validateCode = "";
    String validateCodeBig = "";
    if (StringUtils.isNoneBlank(form.getValidateCode()) && form.getValidateCode().length() == 6
        && StringUtils.isNotBlank(form.getValidateCodeBig())) {
      validateCode = form.getValidateCode();
      validateCodeBig = form.getValidateCodeBig();
    } else {
      validateCode = RandomNumber.getRandomNumber(6).toString();
      String randomStr = UUID.randomUUID().toString() + form.getValidatePsnId() + RandomNumber.getRandomStr(32);
      validateCodeBig = DigestUtils.md5Hex(randomStr);
    }
    accountEmailCheckLog.setValidateCode(validateCode);
    accountEmailCheckLog.setValidateCodeBig(validateCodeBig);
    // 发送成功，保存记录
    accountEmailCheckLogDao.save(accountEmailCheckLog);
    form.setSendEmailDate(accountEmailCheckLog.getSendTime().getTime());
    Long intevalTime = new Date().getTime() - accountEmailCheckLog.getSendTime().getTime();
    intevalTime = intevalTime / 1000;
    if (intevalTime < 60) {
      form.setDelaySendDate(SEND_EMAIL_MAX_TIME_INTERVAL - intevalTime);
    }
    return accountEmailCheckLog;
  }

  /**
   * 重新 发送账户邮箱验证邮件
   * 
   * @param form
   */
  @Override
  public Boolean reSendAccountEmailValidateEmail(AccountEmailForm form) {
    if (StringUtils.isBlank(form.getNewEmail())) {
      // 把当前psnId 设置为需要发送邮件的psnId
      String loginName = userDao.getLoginNameById(form.getCurrentPsnId());
      // 重新设置发件账号需要的参数 start
      form.setNewEmail(loginName);
    }
    // 1，先把未处理邮箱账号记录设置为2
    AccountEmailCheckLog accountEmailCheckLog =
        accountEmailCheckLogDao.checkValidate(form.getCurrentPsnId(), form.getNewEmail());

    // 2.2如果当前账号已经验证，就不需要发送邮件,不需要更新记录
    AccountEmailCheckLog hasConfirm =
        accountEmailCheckLogDao.findHasConfirm(form.getCurrentPsnId(), form.getNewEmail());
    if (hasConfirm != null) {
      form.setHasConfirm(true);
      return false;
    }
    // 3 ,相同账号24小时内发送的验证码相同

    if (accountEmailCheckLog != null) {
      Date nowDate = new Date();
      Date sendDate = accountEmailCheckLog.getSendTime();
      if (sendDate != null && (nowDate.getTime() - sendDate.getTime() <= ONE_DAY)) {
        form.setValidateCode(accountEmailCheckLog.getValidateCode());
        form.setValidateCodeBig(accountEmailCheckLog.getValidateCodeBig());
      }

    }

    form.setValidatePsnId(form.getCurrentPsnId());
    Boolean result = sendAccountEmailValidateEmail(form);
    if (result) {
      if (accountEmailCheckLog != null) {
        accountEmailCheckLog.setDealTime(new Date());
        accountEmailCheckLog.setDealStatus(2);
        accountEmailCheckLogDao.save(accountEmailCheckLog);
      }
      return result;
    }
    return false;
  }


  /**
   * 邮箱是否有效 ， true= 没有被使用
   */
  @Override
  public Boolean checkEmailIsValidate(AccountEmailForm form) {
    boolean used = userDao.isLoginNameUsed(form.getNewEmail(), form.getCurrentPsnId());
    return !used;
  }

  @Override
  public Boolean findHasConfirm(Long psnId, String loginName) {
    PersonEmailRegister psnEmail = personEmailDao.getPsnEmail(psnId, loginName);
    if (psnEmail != null && psnEmail.getIsVerify() != null && psnEmail.getIsVerify() == 1L) {
      return true;
    }
    /*
     * AccountEmailCheckLog hasConfirm = accountEmailCheckLogDao.findHasConfirm(psnId, loginName);
     * if(hasConfirm !=null) { return true ; }
     */
    return false;
  }

  /**
   * 发送时间间隔， 小于60秒不能发送。
   */
  @Override
  public Boolean checkAccountSendDate(AccountEmailForm form) {
    AccountEmailCheckLog checkValidate =
        accountEmailCheckLogDao.checkValidate(form.getValidatePsnId(), form.getNewEmail());
    if (checkValidate != null) {
      Long intevalTime = (new Date().getTime() - checkValidate.getSendTime().getTime()) / 1000;
      if (intevalTime < SEND_EMAIL_MAX_TIME_INTERVAL) {
        return false;
      }
    }
    return true;
  }
}
