package com.smate.center.batch.service.mail;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.acegisecurity.util.EncryptionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnInsDao;
import com.smate.center.batch.dao.sns.psn.inforefresh.PsnRefreshUserInfoDao;
import com.smate.center.batch.dao.sns.pub.PersonEmailDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.model.sns.psn.PsnRefreshUserInfo;
import com.smate.center.batch.model.sns.pub.PersonEmail;
import com.smate.center.batch.service.emailsimplify.EmailSimplify;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.psn.PsnPrivateService;
import com.smate.center.batch.service.psn.SyncPersonService;
import com.smate.center.batch.service.psn.UserCacheService;
import com.smate.center.batch.service.pub.mq.RcmdSyncFlagMessageProducer;
import com.smate.center.batch.service.user.UserService;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * PersonEmailManagerImpl.
 * 
 * @author new .
 * 
 */
@Service("personEmailManager")
@Transactional(rollbackFor = Exception.class)
public class PersonEmailManagerImpl implements PersonEmailManager {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;
  @Autowired
  private PersonManager personManager;

  @Autowired
  private PsnInsDao psnInsDao;

  @Autowired
  private UserCacheService userCacheService;

  @Autowired
  private SyncPersonService syncPersonService;

  @Autowired
  private RcmdSyncFlagMessageProducer rcmdSyncFlagMessageProducer;

  @Autowired
  EmailSimplify psnEmailConfirmEmailService;

  @Autowired
  EmailSimplify psnFirstEmailService;

  @Autowired
  private PsnPrivateService psnPrivateService;

  @Autowired
  private UserService userService;

  @Autowired
  private SysDomainConst sysDomainConst;

  @Override
  public List<PersonEmail> findPersonEmailList() throws ServiceException {
    Long personId = SecurityUtils.getCurrentUserId();
    try {
      return personEmailDao.findListByPersonId(personId);
    } catch (DaoException e) {
      logger.error("取邮件出错", e);
      throw new ServiceException();
    }
  }

  @Override
  public List<PersonEmail> findPersonEmailList(Long psnId) throws ServiceException, DaoException {
    return personEmailDao.findListByPersonId(psnId);
  }

  PersonManager getPersonManager(Long psnId) throws ServiceException {
    return this.personManager;
  }

  /**
   * 添加电子邮件，如果邮件已存在，则返回-1，正确放回EMAIL_ID.
   * 
   * @param email
   * @throws ServiceException
   */
  public Long addEmail(String email, Long psnId, Integer isFirstMail, Integer isLoginMail) throws ServiceException {
    try {
      email = email.toLowerCase();
      // 如果该用户已存在该邮件
      if (this.isEmailExit(psnId, email)) {
        return -1L;
      }
      PersonEmail personEmail = new PersonEmail(psnId, email, isFirstMail, isLoginMail, 0);
      this.personEmailDao.save(personEmail);
      rcmdSyncFlagMessageProducer.syncPsnEmail(psnId);
      PsnRefreshUserInfo psnRefInfo = psnRefreshUserInfoDao.get(psnId);
      if (psnRefInfo == null) {
        psnRefInfo = new PsnRefreshUserInfo(psnId);
      }
      psnRefInfo.setEmailFlag(1);
      psnRefreshUserInfoDao.save(psnRefInfo);
      return personEmail.getId();
    } catch (Exception e) {
      logger.error("添加邮件出错", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PersonEmail findEmailById(Long emailId) throws ServiceException {
    try {
      return personEmailDao.get(emailId);
    } catch (Exception e) {
      logger.error("取邮件出错", e);
      throw new ServiceException();
    }
  }

  @Override
  public void delete(PersonEmail pe) throws ServiceException {
    personEmailDao.delete(pe);
  }

  @Override
  public int delete(Long emailId, Long psnId) throws ServiceException {
    // TODO
    return 0;
  }

  /**
   * 发送确认邮件.
   * 
   * @param url
   * 
   * @return int
   * @throws ServiceException
   */
  public int sendConfirm(Long emailId) throws ServiceException {

    PersonEmail personEmail = null;
    try {
      if (emailId != null) {
        personEmail = personEmailDao.get(emailId);
      }
      if (personEmail == null) {
        return 0;
      }
      Person person = this.personManager.getPerson(SecurityUtils.getCurrentUserId());
      psnEmailConfirmEmailService.syncEmailInfo(personEmail, person);
      return 1;
    } catch (Exception e) {
      throw new ServiceException(e);
    }

  }

  @Override
  public int confirmEmail(Long emailId) throws ServiceException {

    PersonEmail personEmail = personEmailDao.get(emailId);

    return this.confirmEmailByPsnEmailObj(personEmail);
  }

  @Override
  public int confirmEmailByPsnEmailObj(PersonEmail personEmail) throws ServiceException {
    // TODO
    return 0;
  }

  @Override
  public List<String> getConfirmEmail(Long psnId) throws ServiceException {
    try {
      return personEmailDao.getConfirmEmail(psnId);
    } catch (Exception e) {
      logger.error("获取确认的邮件列表", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 更新邮件，注意，该方法不能用于更新首要邮件.
   * 
   * @param email
   * @throws ServiceException
   */
  public void updateEmail(PersonEmail email) throws ServiceException {

    try {
      personEmailDao.save(email);
    } catch (Exception e) {
      logger.error("确认邮件", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 设置个人首要/登录邮件(同时更新个人email和系统user login).
   * 
   * @param email
   * @return int
   * @throws ServiceException
   */
  public int updateFirstEmail(Long emailId) throws ServiceException {

    return this.updateFirstEmail(emailId, true);
  }

  @Override
  public int updateFirstEmail(Long emailId, boolean needMail) throws ServiceException {
    // TODO
    return 0;
  }

  @Override
  public List<PersonEmail> findListByEmail(String email) throws ServiceException {

    try {
      return this.personEmailDao.findListByEmail(email);
    } catch (DaoException e) {
      logger.error("通过EMAIL查询用户ID，用户名列表.email: " + email, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 通过邮件用户邮件.
   * 
   * @param email
   * @return
   * @throws ServiceException
   */
  @Override
  public PersonEmail findPersonEmailByEmail(Long psnId, String email) throws ServiceException {

    try {
      return this.personEmailDao.findPersonEmailByEmail(psnId, email);
    } catch (DaoException e) {
      logger.error("通过邮件用户邮件.email: " + email, e);
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

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.profile.PersonEmailManager#findPersonEmailByPsnId
   * (java.lang.Long)
   */
  @Override
  public PersonEmail findPersonEmailByPsnId(Long psnId) throws ServiceException {
    try {
      return this.personEmailDao.findUniqueBy("psnId", psnId);
    } catch (Exception e) {
      logger.error("通过PSNID查询用户ID，用户名列表.psnId: " + psnId, e);
      throw new ServiceException(e);
    }
  }

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
  public PersonEmail getFirstPersonEmail(Long psnId) throws ServiceException {

    try {
      return personEmailDao.getfirstPersonEmail(psnId);
    } catch (DaoException e) {
      logger.error("通过PSNID查获取用户的首要邮件出错 psnId: " + psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PersonEmail getFirstAndNotVerifyPersonEmail(Long psnId) throws ServiceException {
    try {
      return personEmailDao.getFirstAndNotVerifyPersonEmail(psnId);
    } catch (DaoException e) {
      logger.error("通过PSNID查询用户未确认的首要邮件出错 psnId: " + psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> isExitEmailVerify(String email) throws ServiceException {
    try {
      return personEmailDao.isExitEmailVerify(email);
    } catch (DaoException e) {
      logger.error("查询邮件是否存在于系统中验证过的邮件出错", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void syncOldPersonEmail(Long psnId, List<Map<String, Object>> oldList) throws ServiceException {
    try {

      if (oldList != null && oldList.size() > 0) {
        User user = this.userCacheService.get(psnId);
        String loginName = null;
        if (user != null) {
          loginName = user.getLoginName();
        }
        for (Map<String, Object> map : oldList) {
          String email = map.get("EMAIL") == null ? null : map.get("EMAIL").toString();
          String leftPart = map.get("LEFT_PART") == null ? null : map.get("LEFT_PART").toString();
          String rightPart = map.get("RIGHT_PART") == null ? null : map.get("RIGHT_PART").toString();
          Integer firstMail = map.get("FIRST_MAIL") == null ? 0 : 1;
          Integer isVerify = map.get("IS_VERIFY") == null ? 0 : 1;
          // 是否是登录邮件
          PersonEmail personEmail = new PersonEmail();
          personEmail.setEmail(email);
          personEmail.setFirstMail(firstMail);
          personEmail.setIsVerify(isVerify);
          personEmail.setLeftPart(leftPart);
          personEmail.setLoginMail(loginName == null ? 0 : 1);
          personEmail.setPsnId(psnId);
          personEmail.setRightPart(rightPart);
          this.personEmailDao.save(personEmail);
        }
      }
    } catch (Exception e) {
      logger.error("同步V2.6用户EMAIL数据出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 检索用户是否已对该邮件进行过确认
   * 
   * @param psnId
   * @param email
   * @return
   * @throws ServiceException
   * @throws DaoException
   */
  @Override
  public boolean isPsnVerified(Long psnId, String email) throws ServiceException {
    try {
      return personEmailDao.isPsnVerified(psnId, email);
    } catch (DaoException e) {
      logger.error("检索用户是否已对邮件:" + email + "进行过确认时出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 更新首要邮件的确认状态
   */
  public void updateLoginEmailVerifyStatus(Long psnId, String email) throws ServiceException {
    try {
      PersonEmail personEmail = personEmailDao.findPersonEmailByEmail(psnId, email);
      if (personEmail != null) {
        if (personEmail.getIsVerify() != null && personEmail.getIsVerify() != 1) {
          personEmail.setIsVerify(1);
          personEmailDao.save(personEmail);
        }
      }
    } catch (DaoException e) {
      logger.error("更新首要邮件的确认状态出错,用户ID:+" + psnId + ",邮件:" + email, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getPersonEmailCountByEmail(String email) throws ServiceException {
    try {
      return this.personEmailDao.queryPersonEmailCountByEmail(email);
    } catch (DaoException e) {
      logger.error("查询此邮箱设为首要邮件的人员总数出现异常:" + email, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 处理注册时同步执行逻辑.
   * 
   * @param key 请求类型.
   * @params params 需配置传递的参数.
   * @return
   * @throws ServiceException
   */
  @Override
  public String dealRegistAffairByType(PersonRegister person, Map<String, String> params) throws ServiceException {
    String casUrl = ObjectUtils.toString(params.get("casUrl"));
    String forwardAction = ObjectUtils.toString(params.get("forwardAction"));
    String verifyEmail = ObjectUtils.toString(params.get("verifyEmail"));
    String key = ObjectUtils.toString(params.get("key"));
    String keyType = ServiceUtil.decodeFromDes3(key);
    if ("group_invite".equals(keyType) || "pub_share".equals(keyType)) {// 判断是否群组邀请.
      Long psnId = person.getPersonId();
      String email = person.getEmail();
      // 更新邮件地址的确认状态(提交注册的邮件与邀请的邮件地址相同时才予以绑定)_MJG_2013-05-24_SCM-2575.
      Boolean isConfirm = this.isPsnVerified(psnId, email);
      if (!isConfirm && email.equals(verifyEmail)) {
        this.updateLoginEmailVerifyStatus(psnId, email);
      }
      String languageVersion = LocaleContextHolder.getLocale().toString();
      try {
        String autoUrl = this.getAutoLoginUrl(psnId, casUrl, languageVersion);
        // 设置请求路径.
        // webUrl
        String systemUrl = "https://" + sysDomainConst.getSnsDomain();
        if (sysDomainConst.getSnsContext() != null) {
          systemUrl = systemUrl + "/" + sysDomainConst.getSnsContext();
        }
        String inviteUrl = systemUrl + forwardAction;
        forwardAction = autoUrl + "&service=" + java.net.URLEncoder.encode(inviteUrl, "utf-8");
      } catch (NumberFormatException e) {
        logger.error("处理注册时同步执行逻辑", e);
        return forwardAction;
      } catch (Exception e) {
        logger.error("处理注册时同步执行逻辑", e);
        return forwardAction;
      }
    }
    return forwardAction;
  }

  /**
   * 获取自动登录地址固定部分(获取完整的自动登录地址需在返回值后追加参数service及对应值).
   * 
   * @param receiverId
   * @param domain
   * @param languageVersion
   * @return
   * @throws UnsupportedEncodingException
   */
  @SuppressWarnings("deprecation")
  public String getAutoLoginUrl(Long personID, String casUrl, String languageVersion)
      throws UnsupportedEncodingException {
    String userType = "CITE";
    String passStr = userType + "|" + personID;
    String encpassword = EncryptionUtils.encrypt("111111222222333333444444", passStr);
    String cite_password = java.net.URLEncoder.encode(encpassword, "utf-8");
    String inviteUrl = casUrl + "login?submit=true&username=CITE&password=" + cite_password;
    return inviteUrl;
  }
}
