package com.smate.center.oauth.service.profile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.exception.ServiceException;
import com.smate.center.oauth.form.ForgetPwdForm;
import com.smate.center.oauth.service.security.UserService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 人员登录名及密码等服务接口实现类
 * 
 * @author zzx
 *
 */
@Service("personProfileService")
@Transactional(rollbackFor = Exception.class)
public class PersonProfileServiceImpl implements PersonProfileService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  // 链接有效期限
  private final int URLLIMITDAY = 1;
  @Autowired
  private PersonService personService;
  @Autowired
  private UserService userService;
  @Autowired
  private PersonEmailService psnEmailService;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;

  @Override
  public boolean validateEmailIsExist(String email) {
    List<User> userList = this.userService.findUserByLoginNameOrEmail(email);
    return (userList != null && userList.size() > 0);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public String sendForgetPasswordMail(String emailOrLogin) throws OauthException {
    HashSet firstEmailSet = new HashSet();
    Map<String, String> objListMap = new HashMap<String, String>();
    List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
    // 如果需要在objListMap中跟踪对象在便利的时候需要放置参数为url
    List<Map<String, Object>> resetPwdLinks = new ArrayList<Map<String, Object>>();
    Long currentUserId = SecurityUtils.getCurrentUserId();
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    String gen = ServiceUtil.encodeToDes3(Long.toString(System.currentTimeMillis()));
    Integer templateCode = null;
    mailData.put("domainUrl", getSysDomain());
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(getSysDomain());
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    String msg = "重置在科研之友的密码";
    List<User> userList = getUserList(emailOrLogin);
    if (userList == null) {
      logger.error("不存在该用户！");
      throw new OauthException();
    }
    // 去掉不存在的user
    for (int i = 0; i < userList.size(); i++) {
      Person person = personService.getPerson(userList.get(i).getId());
      if (person == null) {
        userList.remove(i);
      }
    }
    if (userList.size() == 0) {
      logger.error("不存在该用户！");
      throw new OauthException();
    }
    // 获取第一个人的收件语言
    User user1 = userList.get(0);
    Person person1 = personService.getPerson(user1.getId());
    String languageVersion = person1.getEmailLanguageVersion();
    if (userList.size() == 1) {
      // 只有一个账号时 使用10009模板
      templateCode = 10009;
      this.userService.resetTokenBit(user1.getId(), (short) 0); // 2重置用户标识位
      String url = "";
      try {
        url = getResetURL(user1, gen, languageVersion); // 忘记密码的URL路径
      } catch (Exception e) {
        return null;
      }
      MailLinkInfo l2 = new MailLinkInfo();
      l2.setKey("resetUrl");
      l2.setUrl(url);
      l2.setUrlDesc("重置密码链接");
      linkList.add(JacksonUtils.jsonObjectSerializer(l2));
      String psnName = this.getUserName(person1, languageVersion);
      mailData.put("resetUrl", url);
      mailData.put("userName", psnName);
      paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
      firstEmailSet.add(getFirstEmail(user1.getEmail()));
    } else {
      // 多个匹配 使用10010模板
      templateCode = 10010;
      // int i = 0;
      for (User user : userList) {
        Person person = personService.getPerson(user.getId());
        this.userService.resetTokenBit(user.getId(), (short) 0); // 重置用户标识位
        String url = "";
        try {
          url = getResetURL(user, gen, languageVersion); // 忘记密码的URL路径
        } catch (Exception e) {
          logger.error("修改了忘记密码的URL路径", e);
          continue;
        }
        // url 固定格式 tempLinkMap.put("url", url); 模板中使用url获取
        HashMap<String, Object> tempLinkMap = new HashMap<String, Object>();
        tempLinkMap.put("url", url);
        tempLinkMap.put("key", "url");
        tempLinkMap.put("urlDesc", "重置密码链接");
        resetPwdLinks.add(tempLinkMap);
        String psnName = this.getUserName(person, languageVersion);
        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.put("userName", psnName);
        tempMap.put("loginName", user.getLoginName());
        tempMap.put("psn_key", user.getId());
        users.add(tempMap);
        firstEmailSet.add(getFirstEmail(user.getEmail()));
      }
    }

    mailData.put("forgetEmail", emailOrLogin.trim().toLowerCase());
    if (CollectionUtils.isNotEmpty(users) && CollectionUtils.isNotEmpty(resetPwdLinks)) {
      objListMap.put("users", JacksonUtils.listToJsonStr(users));
      objListMap.put("links", JacksonUtils.listToJsonStr(resetPwdLinks));
      mailData.put("objListMap", JacksonUtils.mapToJsonStr(objListMap));
      mailData.put("userName", users.get(0).get("userName") + "");
    }
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    // 向v_mail_original_data插入一条数据，status=1为发送
    try {
      MailOriginalDataInfo info = new MailOriginalDataInfo();
      info.setReceiver(emailOrLogin);// 接收邮箱
      info.setSenderPsnId(currentUserId);// 发送人psnId，0=系统邮件
      info.setReceiverPsnId(userList.get(0).getId());// 接收人psnId，0=非科研之友用户
      info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
      info.setMsg(msg);// 描述
      paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
      restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
    } catch (Exception e) {
      return null;
    }
    return StringUtils.join(firstEmailSet.iterator(), ", ");
  }

  private String getResetURL(User user, String gen, String languageVersion) throws Exception {
    StringBuffer url = new StringBuffer(sysDomainConst.getSnsDomain());
    // 修改了忘记密码的URL路径_MJG_SCM-4073.
    /* url.append(sysDomainConst.getSnsContext() + "/forgetpwd/resetPassword?key="); */
    url.append("/oauth/pwd/reset/index?key=");
    url.append(URLEncoder.encode(ServiceUtil.encodeToDes3(user.getId().toString()), "UTF-8"));
    url.append("&gen=").append(java.net.URLEncoder.encode(gen, "UTF-8"));
    url.append("&email=").append(URLEncoder.encode(ServiceUtil.encodeToDes3(user.getLoginName()), "UTF-8"));
    url.append("&locale=").append(languageVersion);
    return url.toString();
  }

  private String getUserName(Person person, String languageVersion) {
    String psnName = "";
    if (Locale.CHINA.toString().equals(languageVersion)) {
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
   * 获取页面显示邮件内容.
   * 
   * @param tempEmail
   * @return
   */
  private String getFirstEmail(String tempEmail) {
    tempEmail = tempEmail.toLowerCase();
    int truncIndex = tempEmail.indexOf("@");
    int subLen = tempEmail.substring(0, truncIndex).length();
    int endIndex = (subLen >= 4) ? (subLen - 4) : subLen;
    String centerCode = (subLen >= 10) ? "******" : "******".substring(0, endIndex);
    // 修改了缩略邮箱地址的显示内容(邮箱名超过4个字符时前后两位字符保留，中间字符用*代替，最多显示六个*)_MJG_2013-03-15_SCM-2002.
    String sendMail = "";
    if (subLen <= 4 && subLen >= 3) {
      sendMail = tempEmail.substring(0, 1) + "******"
          + tempEmail.substring(0, truncIndex).substring(truncIndex - 1, truncIndex) + tempEmail.substring(truncIndex);
    } else {
      sendMail = subLen > 4
          ? tempEmail.substring(0, truncIndex).substring(0, 2) + centerCode + tempEmail.substring(truncIndex - 2)
          : tempEmail;
    }

    return sendMail;
  }

  /**
   * 获取当前系统域名.
   * 
   * @return
   */
  private String getSysDomain() {
    try {
      return sysDomainConst.getSnsDomain();
    } catch (OauthException e) {
      logger.error("获取当前系统域名", e);
    }
    return null;
  }

  /**
   * 根据输入的帐号或首要邮件获取用户信息
   * 
   * @param emailOrLogin
   * @return
   */
  private List<User> getUserList(String emailOrLogin) {
    List<User> userList = null;
    try {
      // 1、根据邮件匹配用户名或首要邮件并返回List<user>
      userList = this.userService.findUserByLoginNameOrEmail(emailOrLogin);
    } catch (Exception e1) {
      logger.error("findUserByLoginNameOrEmail！", e1);
      throw new OauthException("findUserByLoginNameOrEmail！", e1);
    }
    return userList;
  }



  @Override
  public boolean verifyResetPwdReqParam(ForgetPwdForm form) {
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getKey()));
    if (psnId == 0L) {
      return false;
    }
    try {
      String emailOrLoginName = URLDecoder.decode(form.getEmail(), "UTF-8");
      psnEmailService.setPsnEmailVerified(psnId, emailOrLoginName);
      form.setEmail(emailOrLoginName);
    } catch (UnsupportedEncodingException e) {
      logger.error("不支持的编码格式！");
    } catch (ServiceException e) {
      logger.error("设置邮件确认状态出错！", e);
    }
    try {
      // 判断链接是否过期
      Long startTime = Long.valueOf(Des3Utils.decodeFromDes3(form.getGen()));
      // 有效期为一天
      Long endTime = startTime + (URLLIMITDAY * 24 * 60 * 60 * 1000);
      Long nowTime = System.currentTimeMillis();
      if (nowTime > endTime) {
        return false;
      }
      // 查找对应用户，并检查该用户的邮件确认状态，已确认忘记密码邮件的话不能重置密码
      User user = userService.findUserById(psnId);
      if (user == null || user.getTokenChanged() == 1) {
        return false;
      }
      return true;
    } catch (NumberFormatException e) {
      logger.error("重置密码链接无效！gen为空或不正确！");
      return false;
    } catch (Exception e) {
      logger.error("校验重置密码链接失败！", e);
      return false;
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean updateNewPwd(String key, String newPassword) {
    boolean flag = false;
    try {
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(key));
      if (psnId != 0L) {
        User user = userService.findUserById(psnId);
        // 设置新密码
        user.setPassword(DigestUtils.md5Hex(newPassword));
        // 设置忘记密码邮件已确认
        user.setTokenChanged((short) 1);
        userService.updateUserPwd(user);
        flag = true;
      } else {
        logger.error("用户更新密码失败！原因：key为空或不正确！key = ", key);
      }
    } catch (ServiceException e) {
      logger.error("用户更新密码失败！{}", e);
    }
    return flag;
  }
}
