package com.smate.center.oauth.service.login;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.consts.OauthConsts;
import com.smate.center.oauth.dao.profile.OpenUserUnionDao;
import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.service.security.UserService;
import com.smate.core.base.oauth.model.OauthLoginForm;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.dao.security.AutoLoginOauthInfoDao;
import com.smate.core.base.utils.dao.security.SysUserLoginLogDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.AutoLoginOauthInfo;
import com.smate.core.base.utils.model.cas.security.SysUserLoginLog;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.random.RandomNumber;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.service.msg.MobileMessageWwxyService;
import com.smate.core.base.utils.string.SystemUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * oauth系统 登录验证服务实现
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("thirdLoginService")
@Transactional(rollbackFor = Exception.class)
public class OauthLoginServiceImpl implements OauthLoginService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private AutoLoginOauthInfoDao autoLoginOauthInfoDao;
  @Autowired
  private SysUserLoginLogDao sysUserLoginLogDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${snsContext}")
  private String snsContext;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Resource(name = "restTemplate3sTimeout")
  private RestTemplate restTemplate3sTimeout;


  @Autowired
  private MobileMessageWwxyService mobileMessageWwxyService;

  /**
   * 登录验证 方法
   * 
   * @param form
   * @throws OauthException
   */
  @Override
  public void oauthLogin(OauthLoginForm form) throws OauthException {
    boolean checkResult = true;
    if (form.getMobileCodeLogin()) {
      checkResult = this.checkMobileCodeLogin(form);
    } else {
      checkResult = this.checkUserNameAndPasswordLogin(form);
    }
    // 先rsa解密，由于数据库存储的密码是md5加密的所有之后得解密后，再用md5加密一次
    // TODO 验证帐号密码 时，密码 需要 做加密解密 处理
    User user = null;
    if (checkResult) {
      if (form.getMobileCodeLogin()) {
        user = userService.findUserByMobile(form.getMobileNum());
      } else {
        user =
            userService.getUser(form.getUserName().trim().toLowerCase(), DigestUtils.md5Hex(form.getPassword().trim()));
      }
    }
    // 帐号或密码错误 需要记录日志 并记录 错误次数
    if (user == null) {
      setLoginErrorMsg(form, checkResult);
      form.setLoginStatus(0);
      loginError(form);
    } else {
      if (!user.getEnabled()) {// 校验是否可用
        form.setLoginStatus(0);
        form.setMsg("账号不可用");
        loginError(form);
      } else {// 成功
        loginSucess(form, user);
      }
    }
  }

  /**
   * 登录出错时设置状态和消息 一般的参数格式和短信验证码的校验，设置错误信息的前面做了, 这个地方是用账号/手机号和密码或手机号和短信验证码找不到对应账号后设置的错误消息
   * 
   * @param form
   * @param checkResult
   */
  protected void setLoginErrorMsg(OauthLoginForm form, boolean checkResult) {
    boolean isEn = LocaleContextHolder.getLocale().equals(Locale.US);
    if (checkResult) {
      boolean isMobileLogin = form.getMobileCodeLogin();
      String msg = isMobileLogin ? "输入的手机号码未注册" : "邮箱/手机号/科研号或密码有误";
      if (isEn) {
        msg = isMobileLogin ? "输入的手机号码未注册" : "Please make sure the email/mobile/Scholar ID and password are correct.";
      }
      form.setMsg(msg);
      form.setErrMsgPosition(1);
    }
  }

  /**
   * 登录成功处理
   * 
   * @param form
   * @param user
   * @throws OauthException
   */
  private void loginSucess(OauthLoginForm form, User user) throws OauthException {
    // 正确清除错误次数
    oauthCacheService.remove(OauthConsts.OAUTH_LOGIN_ERROR_NUM, Struts2Utils.getSession().getId());
    form.setLoginStatus(1);
    // TODO 登录日志 以及一些其他的登录成功后续 处理
    // TODO 根据 用户 信息 或者根据来源单位 或者连接 能不能唯一确定 一个单位，如果不能 就重定向到 单位选择页面
    // TODO 先判断是不是有多个角色 如果有 先进入角色选择页面 选择完角色后 在来 取权限 并重定向到 目标页面
    // 不一样的系统 需要去不一样的 数据库里面获取权限
    // 必须记录登录日志 (是保留新的 还是新加一个 日志表 )
    try {
      userService.saveUserLoginLog(user.getId(), 1L, null);
      // 需要保存来源系统
      int loginType = 8;
      if (form.getMobileCodeLogin()) {
        loginType = OauthConsts.LOGIN_TYPE_MOBILE_CODE;
      }
      userService.saveSysUserLoginInfo(user.getId(), Struts2Utils.getRemoteAddr(), form.getSys(), loginType);
    } catch (Exception e) {
      logger.error("保存登录日志出错", e);
    }

    form.setPsnId(user.getId());
  }

  /**
   * 账号或密码错误 处理
   * 
   * @param form
   * @throws OauthException
   */
  private void loginError(OauthLoginForm form) throws OauthException {
    try {
      form.setLoginStatus(0);
      /*
       * 校验的放外面或actiton中校验 if (LocaleContextHolder.getLocale().equals(Locale.US)) {
       * form.setMsg("Please make sure the email/mobile and password are correct."); if
       * (form.getMobileCodeLogin()) { Object obj =
       * cacheService.get(MobileMessageWwxyService.CACHE_NAME_LOGIN, form.getMobileNum()); Boolean
       * sendMessage = mobileMessageWwxyService.isSendMessageTheDay(form.getMobileNum(),
       * mobileMessageWwxyService.LOGIN_TYPE); if (obj == null && sendMessage) {
       * form.setMsg("Validation code failed. Please reacquire."); } else {
       * form.setMsg("Please make sure the mobile and verification code are correct."); } } } else {
       * form.setMsg("您输入的登录邮箱/手机号或密码有误，请重新输入。"); if (form.getMobileCodeLogin()) { Object obj =
       * cacheService.get(MobileMessageWwxyService.CACHE_NAME_LOGIN, form.getMobileNum()); Boolean
       * sendMessage = mobileMessageWwxyService.isSendMessageTheDay(form.getMobileNum(),
       * mobileMessageWwxyService.LOGIN_TYPE); if (obj == null && sendMessage) {
       * form.setMsg("验证码已失效，请重新获取。"); } else { form.setMsg("您输入的手机号或验证码有误，请重新输入。"); } } }
       */
      String sessionId = findServletRequestSessionId(Struts2Utils.getRequest());
      // 记录错误次数
      Object errorNum = oauthCacheService.get(OauthConsts.OAUTH_LOGIN_ERROR_NUM, sessionId);
      // 需要 重新生成 sessionId, 防止session固话
      sessionId = this.changeSessionId();
      // sessionId = Struts2Utils.getRequest().changeSessionId();

      int tempNum = 0;
      if (errorNum == null) {
        tempNum = 1;
        oauthCacheService.put(OauthConsts.OAUTH_LOGIN_ERROR_NUM, sessionId, tempNum);
      } else {
        tempNum = (int) errorNum + 1;
        oauthCacheService.put(OauthConsts.OAUTH_LOGIN_ERROR_NUM, sessionId, tempNum);
      }
      if (tempNum >= 3) {
        form.setNeedValidateCode("1");
      } else {
        form.setNeedValidateCode("0");
      }
    } catch (Exception e) {
      logger.error("记录错误次数异常" + e);
      throw new OauthException("登录验证，帐号密码错误后处理 异常!", e);
    }
  }

  /**
   * 重新构造 连接
   * 
   * @param token
   * @return
   * @throws UnsupportedEncodingException
   */
  @Override
  public String oauthRebuildTargetUrl(String token) {
    StringBuilder targetUrl = new StringBuilder();
    if (token == null || "".equals(token)) { // 如果目标页面为空，默认 进去科研之友个人主页
      targetUrl.append(domainscm); // 需要重配置文件中读取
    } else {
      targetUrl.append(Des3Utils.decodeFromDes3(token));
    }
    // 如果解密失败，可能是目标页面未加密
    if ("null".equals(targetUrl.toString())) {
      targetUrl = new StringBuilder();
      targetUrl.append(token);
    }
    String locale = Struts2Utils.getRequest().getParameter("locale");
    String localeParam = "locale=";
    if (StringUtils.isNotBlank(locale)) {
      localeParam += locale;
    } else {
      localeParam += LocaleContextHolder.getLocale().toString();
    }
    if (targetUrl.indexOf("?") > -1) {
      targetUrl.append("&" + localeParam + "&" + SecurityConstants.FROM_OAUTH + "=true");
    } else {
      targetUrl.append("?" + localeParam + "&" + SecurityConstants.FROM_OAUTH + "=true");
    }

    return targetUrl.toString();
  }

  @Override
  public String oauthRebuildTargetUrl(String url, String sessionId) {
    StringBuilder targetUrl = new StringBuilder();
    if (url == null || "".equals(url)) { // 如果目标页面为空，默认 进去科研之友个人主页
      targetUrl.append(domainscm); // 需要重配置文件中读取
    } else {
      targetUrl.append(Des3Utils.decodeFromDes3(url));
    }
    // 如果解密失败，可能是目标页面未加密
    if ("null".equals(targetUrl.toString())) {
      targetUrl = new StringBuilder();
      targetUrl.append(url);
    }
    // targetUrl.append("http://dev.scholarmate.com/scmwebsns/main");
    if (targetUrl.indexOf("?") > -1) {
      targetUrl.append("&" + SecurityConstants.URL_REDIRECT_SESSION_ID_PARAMETER + "="
          + (StringUtils.isNotBlank(sessionId) ? sessionId : Struts2Utils.getRequest().getSession().getId()));
    } else {
      targetUrl.append("?" + SecurityConstants.URL_REDIRECT_SESSION_ID_PARAMETER + "="
          + (StringUtils.isNotBlank(sessionId) ? sessionId : Struts2Utils.getRequest().getSession().getId()));
    }
    return targetUrl.toString();
  }

  @Override
  public Long getOpenId(String token, Long psnId, int createType) {
    Long openId = 0L;
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        openId = openUserUnionDao.getOpenIdByPsnId(psnId);
        if (openId == null) {
          openId = RandomNumber.getRandomNumber(8);
          // 查重
          while (true) {
            // 99999999 表示 没有真实用户的 数据交互
            if (openId.longValue() == new Long(99999999).longValue()) {
              continue;
            }
            OpenUserUnion temp = openUserUnionDao.getOpenUserUnionByOpenId(openId);
            if (temp == null) {
              break;
            } else {
              openId = RandomNumber.getRandomNumber(8);
            }
          }
        }
        // 判断是否 有关联的第三方系统记录
        OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnion(openId, token);
        if (openUserUnion == null) {
          openUserUnion = new OpenUserUnion();
          openUserUnion.setOpenId(openId);
          openUserUnion.setPsnId(psnId);
          openUserUnion.setToken(token);
          openUserUnion.setCreateDate(new Date());
          openUserUnion.setCreateType(createType);
          openUserUnionDao.saveOpenUserUnion(openUserUnion);
        }
      }
    } catch (Exception e) {
      logger.error("获取人员openId出错------", e);
    }
    return openId;
  }

  /**
   * 获取自动登录所需的加密串AID
   */
  @SuppressWarnings("unchecked")
  @Override
  public String getAutoLoginAID(Long openId, String autoLoginType) {
    String AID = "";
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(this.getAID(openId, autoLoginType).toString());
    if ("success".equals(resultMap.get("status"))) {
      List<Map<String, String>> resultList = (List<Map<String, String>>) resultMap.get("result");
      if (CollectionUtils.isNotEmpty(resultList)) {
        Map<String, String> map = resultList.get(0);
        if (map.get(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME) != null) {
          AID = map.get(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME).toString();
        }
      }
    }
    return AID;
  }

  /**
   * 获取AID
   * 
   * @param openId
   * @return
   */
  private Object getAID(Long openId, String autoLoginType) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    Map<String, String> dataMap = new HashMap<String, String>();
    dataMap.put("AutoLoginType", autoLoginType);
    mapDate.put("data", JacksonUtils.mapToJsonStr(dataMap));//
    mapDate.put("openid", openId);
    mapDate.put("token", "00000000ime82dt2");// 业务系统默认token
    return restTemplate3sTimeout.postForObject(SERVER_URL, mapDate, Object.class);
  }

  /**
   * 更新自动登录的使用时间和次数
   */
  @Override
  public void updateAutoLoinUserTime(String AID) {
    Date currentDate = new Date();
    AutoLoginOauthInfo autoInfo = autoLoginOauthInfoDao.getAutoLoginOauthInfoByTime(AID, currentDate);
    if (autoInfo != null) {
      autoInfo.setUseTimes(autoInfo.getUseTimes() + 1);
      autoInfo.setLastUseTime(currentDate);
      autoLoginOauthInfoDao.save(autoInfo);
    }
  }

  /**
   * @param psnId 人员id
   * @param remoteIp 远程ip
   * @param sys 来源系统
   * @param type 登录类型
   */

  @Override
  public void saveAutoLoginLog(Long psnId, String remoteIp, String sys, int type) {
    // sys 去掉来源带的参数
    if (StringUtils.isNotBlank(sys)) {
      int index = sys.indexOf("?");
      if (index > 0) {
        sys = sys.substring(0, index);
      }
    }
    SysUserLoginLog sysUserLoginLog = new SysUserLoginLog(psnId, new Date(), remoteIp, sys, type);
    HttpServletRequest request = Struts2Utils.getRequest();
    sysUserLoginLog.setBrowserInfo(SystemUtils.getRequestBrowserInfo(request));
    sysUserLoginLog.setSystemInfo(SystemUtils.getRequestSystemInfo(request));
    sysUserLoginLogDao.save(sysUserLoginLog);
  }

  @Override
  public void checkOauthLogin(OauthLoginForm form) throws OauthException {
    boolean checkResult = true;
    if (form.getMobileCodeLogin()) {
      checkResult = this.checkMobileCodeLogin(form);
    } else {
      checkResult = this.checkUserNameAndPasswordLogin(form);
    }
    // 先rsa解密，由于数据库存储的密码是md5加密的所有之后得解密后，再用md5加密一次
    // TODO 验证帐号密码 时，密码 需要 做加密解密 处理
    User user = null;
    if (checkResult) {
      if (form.getMobileCodeLogin()) {
        user = userService.findUserByMobile(form.getMobileNum());
      } else {
        user =
            userService.getUser(form.getUserName().trim().toLowerCase(), DigestUtils.md5Hex(form.getPassword().trim()));
      }
    }
    // 帐号或密码错误 需要记录日志 并记录 错误次数
    if (user == null) {
      setLoginErrorMsg(form, checkResult);
      form.setLoginStatus(0);
    } else {
      if (!user.getEnabled()) {// 校验是否可用
        form.setLoginStatus(0);
        form.setMsg("账号不可用");
        loginError(form);
      } else {// 成功
        form.setLoginStatus(1);
      }
    }

  }



  /**
   * 校验手机验证码登录
   * 
   * @param form
   */
  @Override
  public boolean checkMobileCodeLogin(OauthLoginForm form) {
    boolean isEn = LocaleContextHolder.getLocale().equals(Locale.US);
    if (StringUtils.isBlank(form.getMobileNum())) {
      if (isEn) {
        form.setMsg("Please enter a valid phone number.");
      } else {
        form.setMsg("请输入手机号");
      }
      loginError(form);
      return false;
    }
    if (StringUtils.isBlank(form.getMobileCode())) {
      if (isEn) {
        form.setMsg("Please enter a valid SMS verification code");
      } else {
        form.setMsg("请输入短信验证码");
      }
      loginError(form);
      return false;
    }

    if (!com.smate.core.base.utils.string.StringUtils.isMobileNumber(form.getMobileNum())) {
      if (isEn) {
        form.setMsg("Please enter a valid phone number.");
      } else {
        form.setMsg("请输入正确的手机号");
      }
      loginError(form);
      return false;
    }
    // 校验账号是否已注册
    User user = userService.findUserByMobile(form.getMobileNum());
    if (user == null) {
      setLoginErrorMsg(form, true);
      loginError(form);
      return false;
    }
    Object obj = cacheService.get(MobileMessageWwxyService.CACHE_NAME_LOGIN, form.getMobileNum());
    if (obj == null || !obj.toString().equals(form.getMobileCode())) {
      if (isEn) {
        form.setMsg("Please enter a valid SMS verification code.");
      } else {
        form.setMsg("短信验证码错误或已过期");
      }
      loginError(form);
      return false;
    }
    // 验证码 SCM-24999
    /*
     * Object errorNum = oauthCacheService.get(OauthConsts.OAUTH_LOGIN_ERROR_NUM,
     * Struts2Utils.getSession().getId()); if (errorNum != null && (int) errorNum >= 3) { if
     * (StringUtils.isBlank(form.getValidateCode())) { if (isEn) {
     * form.setMsg("The system verification code is empty !"); } else { form.setMsg("请输入系统验证码"); }
     * loginError(form); return false; } String validateCode = (String)
     * oauthCacheService.get(SecurityConstants.OAUTH_VALIDATE_CODE, Struts2Utils.getSession().getId());
     * if (!form.getValidateCode().trim().equalsIgnoreCase(validateCode)) { if (isEn) {
     * form.setMsg("The system verification code is error !"); } else { form.setMsg("系统验证码输入错误或已过期"); }
     * loginError(form); return false; } }
     */
    return true;
  }


  /**
   * 校验账号密码登录
   * 
   * @param form
   */
  protected boolean checkUserNameAndPasswordLogin(OauthLoginForm form) {
    boolean isEn = LocaleContextHolder.getLocale().equals(Locale.US);
    if (StringUtils.isBlank(form.getUserName())) {
      if (isEn) {
        form.setMsg("Please enter a valid email address , phone number or Scholar ID.");
      } else {
        form.setMsg("请输入邮箱/手机号/科研号");
      }
      form.setErrMsgPosition(1);
      return false;
    }
    if (StringUtils.isBlank(form.getPassword())) {
      if (isEn) {
        form.setMsg("Please enter a password.");
      } else {
        form.setMsg("请输入密码");
      }
      form.setErrMsgPosition(2);
      return false;
    }
    // 验证码
    Object errorNum = oauthCacheService.get(OauthConsts.OAUTH_LOGIN_ERROR_NUM, Struts2Utils.getSession().getId());
    if (errorNum != null && (int) errorNum >= 3) {
      if (StringUtils.isBlank(form.getValidateCode())) {
        if (isEn) {
          form.setMsg("The system verification code is empty !");
        } else {
          form.setMsg("请输入验证码");
        }
        form.setErrMsgPosition(3);
        return false;
      }
      String validateCode = (String) oauthCacheService.get(SecurityConstants.OAUTH_VALIDATE_CODE,
          findServletRequestSessionId(Struts2Utils.getRequest()));
      if (!form.getValidateCode().trim().equalsIgnoreCase(validateCode)) {
        if (isEn) {
          form.setMsg("The system verification code is error !");
        } else {
          form.setMsg("验证码输入错误或已过期");
        }
        form.setErrMsgPosition(3);
        return false;
      }
    }
    if (form.getUserName().length() > 50 || form.getPassword().length() > 40) {
      if (isEn) {
        form.setMsg("Please make sure the email and password are correct.");
      } else {
        form.setMsg("邮箱/手机号或密码错误");
      }
      form.setErrMsgPosition(1);
      return false;
    }
    return true;
  }

  /**
   * 优先从请求头中获取sessionId，因为直接用ServletRequest强转成的HttpServletRequest获取到的sessionId会改变
   *
   * @param request
   * @return
   */
  private String findServletRequestSessionId(HttpServletRequest request) {
    String cookieStr = request.getHeader("Cookie");
    String sessionId = "";
    if (StringUtils.isNotBlank(cookieStr)) {
      String[] cookies = cookieStr.split(";");
      if (ArrayUtils.isNotEmpty(cookies)) {
        for (String str : cookies) {
          if (str.contains("JSESSIONID=")) {
            sessionId = str.replace("JSESSIONID=", "").trim();
            break;
          }
        }
      }
      if (org.codehaus.plexus.util.StringUtils.isBlank(sessionId)) {
        sessionId = request.getSession().getId();
      }
    }
    return sessionId;
  }


  private String changeSessionId() {
    HttpSession session = Struts2Utils.getRequest().getSession(false);
    if (session != null && !session.isNew()) {
      session.invalidate();
    }
    HttpSession newSession = Struts2Utils.getRequest().getSession(true);
    // TODO 可以复制一些需要的信息到新的session中
    return newSession.getId();
  }

}
