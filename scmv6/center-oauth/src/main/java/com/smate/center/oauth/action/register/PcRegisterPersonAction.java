package com.smate.center.oauth.action.register;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.model.profile.PersonRegisterForm;
import com.smate.center.oauth.service.psnregister.ConstRegionService;
import com.smate.center.oauth.service.psnregister.PersonRegisterService;
import com.smate.center.oauth.service.security.UserService;
import com.smate.center.oauth.utils.EditValidateUtils;
import com.smate.core.base.consts.service.InstitutionService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.msg.MobileMessageForm;
import com.smate.core.base.utils.service.msg.MobileMessageWwxyService;
import com.smate.core.base.utils.service.msg.MobileMessageWwxyServiceImpl;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;

;

/**
 * Pc端人员注册Action
 * 
 * @author AiJiangbin
 */
@Results({@Result(name = "register", location = "/WEB-INF/jsp/pc/pcRegister.jsp"),
    @Result(name = "redirectAction", location = "${forwardUrl}", type = "redirect"),
    @Result(name = "save_error", location = "/WEB-INF/jsp/pc/register_error.jsp"),
    @Result(name = "thirdBind", type = "chain",
        params = {"namespace", "/oauth/thirdlogin", "actionName", "scmregist", "method", "registConnect"})})
public class PcRegisterPersonAction extends ActionSupport implements ModelDriven<PersonRegisterForm>, Preparable {

  protected static final long serialVersionUID = -734066021998629664L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  public static final Integer MAXSIZE = 10;

  private PersonRegisterForm form;

  @Autowired
  private PersonRegisterService personRegisterService;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainoauth}")
  private String domainoauth;
  @Resource
  private ConstRegionService constRegionService;
  @Resource
  private InstitutionService institutionService;
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private UserService userService;
  @Autowired
  private MobileMessageWwxyService mobileMessageWwxyService;

  /**
   * 好友邀请，群组邀请的逻辑 太复杂已经删除。如果出现问题请沟通解决2017-10-13-ajb
   * 
   * @return
   */
  @Actions(@Action("/oauth/pc/register"))
  public String pcRegister() {
    logger.debug("进入注册页面");
    SecurityUtils.getCurrentUserId();
    HttpSession session = Struts2Utils.getSession();
    session.removeAttribute("rPerson");
    try {
      // -----SCM-14879---------处理回调状态------------
      personRegisterService.doRegusterBack(form);
      String SYSID = Struts2Utils.getSession().getId();
      String service = Struts2Utils.getRequest().getParameter("service");
      Object object = oauthCacheService.get(SecurityConstants.USER_DETAILS_INFO_CACHE, SYSID);
      if (object != null) {// 已登录
        Map<String, Object> userDetails = (Map<String, Object>) object;
        Long psnId = 0L;
        if (userDetails.get("userId") != null) {
          form.setPersonId(NumberUtils.toLong(userDetails.get("userId").toString()));
        }
        // -----SCM-14879---------注册回调入口----
        if (personRegisterService.doRegusterBackByCheck(form)) {
          return null;
        }
        if (StringUtils.isNotBlank(service)) {
          // 有携带跳转地址
          String targetUrl = ServiceUtil.decodeFromDes3(service);
          if (StringUtils.isNotBlank(targetUrl)) {
            Struts2Utils.getResponse().sendRedirect(targetUrl);
            return null;
          }
        } else {
          // 无携带跳转地址
          if (StringUtils.isNoneBlank(form.getLocale())) {
            Struts2Utils.redirect("/dynweb/main?locale=" + form.getLocale());
          } else {
            Struts2Utils.redirect("/dynweb/main");
          }
          return null;
        }
      }
    } catch (Exception e) {
      logger.error("进入注册页面执行回调出错", e);
    }
    return "register";
  }

  /**
   * 保存注册的信息
   * 
   * @return
   */
  @Actions(@Action("/oauth/pc/register/sava"))
  public String pcRegisterSave() {
    if (StringUtils.isBlank(form.getZhlastName())) {
      if (StringUtils.isNotBlank(form.getZhlastNameNoPhone())) {
        form.setZhlastName(form.getZhlastNameNoPhone());
      } else {
        form.setZhlastName("");
      }
    }
    if (StringUtils.isBlank(form.getZhfirstName())) {
      if (StringUtils.isNotBlank(form.getZhfirstNameNoPhone())) {
        form.setZhfirstName(form.getZhfirstNameNoPhone());
      } else {
        form.setZhfirstName("");
      }
    }
    if (StringUtils.isNotBlank(form.getLastNameNoPhone())) {
      form.setLastName(form.getLastNameNoPhone());
    }
    if (StringUtils.isNotBlank(form.getFirstNameNoPhone())) {
      form.setFirstName(form.getFirstNameNoPhone());
    }
    // 转换姓名中允许使用的特殊字符，避免长度限制等问题
    rebuildName(form);
    if (StringUtils.isNotBlank(form.getZhlastName()) && StringUtils.isNotBlank(form.getZhfirstName())) {
      form.setName(form.getZhlastName() + form.getZhfirstName());
    } else {
      form.setName(form.getFirstName() + form.getLastName());
      form.setZhlastName(form.getLastName());
      form.setZhfirstName(form.getFirstName());
    }
    logger.info("进入注册保存");
    try {
      boolean autoLogin = true;
      // 先进行注册填写参数的校验
      if (!validateMsg()) {
        // 封装注册人员的信息.
        form = this.assemPersonInfo();
        // 注册保存人员信息.
        Long openId = personRegisterService.registerPerson(form);
        // 注册成功.
        if (openId != 0L) {
          HttpServletResponse response = Struts2Utils.getResponse();
          response.setCharacterEncoding("UTF-8");
          // 如果是关联注册 携带绑定所需信息
          String des3ThirdId = StringUtils.isNotBlank(Struts2Utils.getParameter("des3ThirdId"))
              ? Struts2Utils.getParameter("des3ThirdId")
              : Struts2Utils.getCookieVal(Struts2Utils.getRequest(), "des3ThirdId");
          if (StringUtils.isNotBlank(des3ThirdId)) {
            Struts2Utils.getSession().setAttribute("des3PsnId", form.getPersonDes3Id());
            return "thirdBind";
          }
          if (StringUtils.isNotBlank(form.getBack())) {
            logger.info("人员注册后跳转的url=" + form.getBack());
            // 跳转到第三方 可能要添加动态id
            personRegisterService.addDynamicOpenId(form);
            response.sendRedirect(form.getBack());
            return null;
          }
          // 需要自动登录时
          if (autoLogin) {
            String AID = personRegisterService.getAutoLoginAID(openId, "SNSRememberMe");
            logger.info("获取到的AID=" + AID);
            String targetUrl = "";
            if (StringUtils.isNotBlank(form.getService())) {
              targetUrl = ServiceUtil.decodeFromDes3(form.getService());
              if (StringUtils.isBlank(targetUrl)) {
                targetUrl = form.getService();
              }
            } else if (StringUtils.isNotBlank(form.getTargetUrl())) {
              targetUrl = form.getTargetUrl();
            } else {
              targetUrl = this.domainscm + "/dynweb/main?sysType=" + form.getSysType();
            }
            if (StringUtils.isNotBlank(AID)) {
              if (targetUrl.indexOf("?") > 0) {
                targetUrl += "&" + SecurityConstants.AUTO_LOGIN_PARAMETER_NAME + "=" + AID;
              } else {
                targetUrl += "?" + SecurityConstants.AUTO_LOGIN_PARAMETER_NAME + "=" + AID;
              }
            }
            // -----SCM-14879---------注册回调入口--------------------------------------------------------------
            form.setAid(AID);
            if (personRegisterService.doRegusterBackByCheck(form)) {
              return null;
            }
            logger.info("人员注册后跳转的url=" + targetUrl);
            response.sendRedirect(targetUrl);
            return null;
          }
        } else {
          addActionMessage("邮箱注册失败");
        }
      } else {
        Struts2Utils.getRequest().setAttribute("person", form);
      }
    } catch (Exception e) {
      logger.error("邮箱注册出错", e);
      return "save_error";
    }
    return "save_error";
  }

  /**
   * 转换名称中出现的允许使用的特殊字符,避免长度校验出错
   * 
   * @param form
   */
  public void rebuildName(PersonRegisterForm form) {
    form.setFirstName(form.getFirstName() == null ? "" : form.getFirstName().replaceAll("&middot;", "·"));
    form.setLastName(form.getLastName() == null ? "" : form.getLastName().replaceAll("&middot;", "·"));
    form.setZhfirstName(form.getZhfirstName() == null ? "" : form.getZhfirstName().replaceAll("&middot;", "·"));
    form.setZhlastName(form.getZhlastName() == null ? "" : form.getZhlastName().replaceAll("&middot;", "·"));
  }

  /**
   * 校验注册填写的信息
   * 
   * @return
   */
  public boolean validateMsg() {
    boolean flag = false;

    // 验证邮箱不为空
    String msg = getText("注册信息验证");
    if (EditValidateUtils.hasParam(form.getEmail(), 50, EditValidateUtils.MAIL_COAD)) {
      addActionMessage("邮箱" + msg);
      flag = true;
    }
    // 验证邮箱未被注册过
    try {
      boolean emailNotUsed = personRegisterService.findIsEmail(form.getEmail());
      if (!emailNotUsed) {
        addActionMessage("邮箱已被注册");
        flag = true;
      }
    } catch (Exception e) {
      logger.error("校验EMAIL错误", e);
      addActionMessage("邮箱已被注册");
      flag = true;
    }
    // 验证名字不为空
    if (EditValidateUtils.hasParam(form.getName(), 61, null)) {
      addActionMessage("姓名" + msg);
      flag = true;
    }

    // 验证密码不为空
    if (EditValidateUtils.hasParam(form.getNewpassword(), 40, null)) {
      addActionMessage("密码" + msg);
      flag = true;
    }
    if (StringUtils.isNotBlank(form.getQqName()) || StringUtils.isNotBlank(form.getWechatName())) {
      // 不要验证手机的条件
    } else {
      if (StringUtils.isNotBlank(form.getMobileNumber())) {
        flag = verifyMobile(flag, msg);
      }
    }
    return flag;
  }

  private boolean verifyMobile(boolean flag, String msg) {
    // 验证手机号
    if (EditValidateUtils.hasParam(form.getMobileNumber(), 11, null)) {
      addActionMessage("手机号" + msg);
      flag = true;
    }
    // 手机号验证码
    Object obj = cacheService.get(MobileMessageWwxyService.CACHE_NAME, form.getMobileNumber());
    if (EditValidateUtils.hasParam(form.getMobileVerifyCode(), 6, null)) {
      addActionMessage("手机验证码" + msg);
      flag = true;
    } else if (obj == null || !obj.toString().equals(form.getMobileVerifyCode())) {
      addActionMessage("手机验证码" + msg);
      flag = true;
    }
    return flag;
  }


  /**
   * 封装注册人员的信息.
   * 
   * @return
   */
  private PersonRegisterForm assemPersonInfo() {
    form.setIsRegisterR(true);
    // 对密码进行MD5加密，后面调用center-open的人员注册服务传参需要
    form.setNewpassword(DigestUtils.md5Hex(form.getNewpassword()));
    form.setEmail(form.getEmail().toLowerCase());
    form.setEmailLanguageVersion(this.getLocale().toString());// 设置个人邮件接收的语言版本，默认为当前系统的语言版本
    return form;
  }

  @Action("/oauth/pc/register/ajaxRegion")
  public String ajaxRegion() throws Exception {
    String jsonRes = "";
    try {
      Long superRegionId = null;
      if (NumberUtils.isNumber(form.getSuperRegionId())) {
        superRegionId = Long.valueOf(form.getSuperRegionId());
      }
      jsonRes = constRegionService.findRegionJsonData(superRegionId);
    } catch (Exception e) {

      String resultJson = "{'result':'error'}";
      Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
      return null;
    }
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;
  }

  /**
   * 加载单位
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/pc/register/ajaxInstitution")
  public String ajaxInstitution() throws Exception {
    String startWith = form.getQueryInstitution();
    startWith = startWith.trim();
    String data = "";
    try {
      data = institutionService.getInstitution(startWith, form.getExcludes(), MAXSIZE);
    } catch (Exception e) {
      String resultJson = "{'result':'error'}";
      Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
      return null;
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }


  /**
   * 发送手机验证码
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/pc/register/ajaxsendmobilecode")
  public String ajaxSendMobileCode() throws Exception {
    Boolean isMobile = com.smate.core.base.utils.string.StringUtils.isMobileNumber(form.getMobileNumber());
    Map resMap = new HashMap();
    if (isMobile) {
      User user = userService.findUserByMobile(form.getMobileNumber());
      if (user == null) {
        String code = RandomStringUtils.randomNumeric(6);
        MobileMessageForm messsage = new MobileMessageForm();
        messsage.setDestId(form.getMobileNumber());
        // 注册验证码
        messsage.setSmsType(MobileMessageWwxyServiceImpl.REG_TYPE);
        messsage.setContent(MobileMessageWwxyServiceImpl.buildRegMessage(code));
        cacheService.put(MobileMessageWwxyService.CACHE_NAME, MobileMessageWwxyService.EXPIRE_DATE,
            form.getMobileNumber(), code);
        mobileMessageWwxyService.initSendMessage(messsage);
        resMap.put("result", "success");
      } else {
        resMap.put("result", "exist");
      }
    } else {
      resMap.put("result", "error");
    }
    String str = Struts2Utils.getHttpReferer();
    if (StringUtils.isNotBlank(str)) {
      URL url = new URL(str);
      String domain = url.getProtocol() + "://" + url.getHost();
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domain);
    }
    Struts2Utils.renderJson(resMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 发送手机登录验证码
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/pc/register/ajaxsendmobilelogincode")
  public String ajaxSendMobileLoginCode() throws Exception {
    Boolean isMobile = com.smate.core.base.utils.string.StringUtils.isMobileNumber(form.getMobileNumber());
    Map resMap = new HashMap();
    if (isMobile) {
      User user = userService.findUserByMobile(form.getMobileNumber());
      if (user != null) {
        String code = RandomStringUtils.randomNumeric(6);
        MobileMessageForm messsage = new MobileMessageForm();
        messsage.setDestId(form.getMobileNumber());
        // 登录验证码
        messsage.setSmsType(MobileMessageWwxyServiceImpl.LOGIN_TYPE);
        messsage.setContent(MobileMessageWwxyServiceImpl.buildLoginMessage(code));
        cacheService.put(MobileMessageWwxyService.CACHE_NAME_LOGIN, MobileMessageWwxyService.EXPIRE_DATE,
            form.getMobileNumber(), code);
        mobileMessageWwxyService.initSendMessage(messsage);
        resMap.put("result", "success");
      } else {
        resMap.put("result", "notExist");
      }
    } else {
      resMap.put("result", "error");
    }
    String str = Struts2Utils.getHttpReferer();
    if (StringUtils.isNotBlank(str)) {
      URL url = new URL(str);
      String domain = url.getProtocol() + "://" + url.getHost();
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domain);
    }
    Struts2Utils.renderJson(resMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 检查手机号
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/pc/register/ajaxcheckmobile")
  public String ajaxCheckMobile() throws Exception {
    Boolean isMobile = com.smate.core.base.utils.string.StringUtils.isMobileNumber(form.getMobileNumber());
    String result = "false";
    if (isMobile) {
      User user = userService.findUserByMobile(form.getMobileNumber());
      if (user == null) {
        result = "true";
      }
    }
    String str = Struts2Utils.getHttpReferer();
    if (StringUtils.isNotBlank(str)) {
      URL url = new URL(str);
      String domain = url.getProtocol() + "://" + url.getHost();
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domain);
    }
    Struts2Utils.renderText(result);
    return null;
  }

  /**
   * 检查验证码
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/pc/register/ajaxcheckmobilecode")
  public String ajaxCheckMobileCode() throws Exception {
    Boolean isMobile = com.smate.core.base.utils.string.StringUtils.isMobileNumber(form.getMobileNumber());
    String result = "false";
    if (isMobile && StringUtils.isNotBlank(form.getMobileVerifyCode())
        && form.getMobileVerifyCode().trim().length() == 6) {
      Object obj = cacheService.get(MobileMessageWwxyService.CACHE_NAME, form.getMobileNumber());
      if (obj != null && obj.toString().equals(form.getMobileVerifyCode().trim())) {
        result = "true";
      }
    }
    String str = Struts2Utils.getHttpReferer();
    if (StringUtils.isNotBlank(str)) {
      URL url = new URL(str);
      String domain = url.getProtocol() + "://" + url.getHost();
      Struts2Utils.getResponse().addHeader("Access-Control-Allow-Origin", domain);
    }
    Struts2Utils.renderText(result);
    return null;
  }


  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PersonRegisterForm();
    }
  }

  @Override
  public PersonRegisterForm getModel() {

    return form;
  }

}
