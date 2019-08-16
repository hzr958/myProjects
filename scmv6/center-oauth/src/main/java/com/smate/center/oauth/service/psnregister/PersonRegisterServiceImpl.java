package com.smate.center.oauth.service.psnregister;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.dao.profile.ConstSurNameDao;
import com.smate.center.oauth.dao.profile.OpenUserUnionDao;
import com.smate.center.oauth.dao.profile.PersonEmailDao;
import com.smate.center.oauth.dao.profile.SyncPersonDao;
import com.smate.center.oauth.exception.RegisterException;
import com.smate.center.oauth.model.consts.OpenConsts;
import com.smate.center.oauth.model.profile.ConstSurName;
import com.smate.center.oauth.model.profile.PersonEmail;
import com.smate.center.oauth.model.profile.PersonRegisterForm;
import com.smate.center.oauth.model.profile.SyncPerson;
import com.smate.center.oauth.service.security.UserService;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.psn.dao.AccountEmailCheckLogDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.dao.security.RegisterTempDao;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.reg.RegisterTemp;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.model.wechat.WeChatRelation;
import com.smate.core.base.utils.person.avatars.PersonAvatarsUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.MessageUtil;
import com.smate.core.base.utils.wechat.OAuth2Service;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 人员注册信息服务接口实现类
 * 
 * @author Administrator
 *
 */
@Service("PersonRegisterService")
@Transactional(rollbackFor = Exception.class)
public class PersonRegisterServiceImpl implements PersonRegisterService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  @Autowired
  private UserService userService;
  @Autowired
  private SyncPersonDao syncPersonDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Autowired
  private PersonEmailDao personEmailRegisterDao;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private ConstSurNameDao constSurNameDao;

  @Autowired
  private MailInitDataService mailInitDataService;

  @Autowired
  private RegisterTempDao registerTempDao;

  @Autowired
  private AccountEmailCheckLogDao accountEmailCheckLogDao;

  @Autowired
  private OauthCacheService oauthCacheService;

  @Autowired
  private OAuth2Service oAuth2Service;

  /**
   * 文件根路径
   */
  @Value("${file.root}")
  private String rootPath;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 人员注册邮箱是否已被占用
   */
  @Override
  public boolean findIsEmail(String email) throws RegisterException {
    try {
      boolean flag = true;
      // 注册时只需查看人员注册填写的邮箱是否在sys_user表中已存在一样的loginName
      Long psnId = userService.findIdByLoginName(email);
      if (psnId != null) {
        flag = false;
      }
      return flag;
    } catch (Exception e) {
      logger.info("findIsEmail", e);
      throw new RegisterException("findIsEmail", e);
    }
  }

  /**
   * 注册----保存人员信息
   */
  @SuppressWarnings("unchecked")
  @Override
  public Long registerPerson(PersonRegisterForm form) {
    Long psnOpenId = 0L;
    Long startTime = new Date().getTime();
    try {
      // 调用center-open的人员注册服务,保存人员基本信息
      String temp = saveRegisterInfo(form, "sns");
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(temp);
      if ("success".equals(resultMap.get("status"))) {
        String openId = resultMap.get("openid").toString();
        if (StringUtils.isNotBlank(openId)) {
          psnOpenId = Long.valueOf(openId);
        }
      }
      // 保存人员信息各节点同步表
      Long psnId = openUserUnionDao.getOpenUserUnionPsnIdByOpenId(psnOpenId);
      if (psnId != null) {
        // SCM APP注册成功返回id用于直接登陆
        form.setPersonId(psnId);
        form.setPersonDes3Id(ServiceUtil.encodeToDes3(psnId.toString()));
        Person psn = personProfileDao.get(psnId);
        if (psn != null) {
          // 发送一封账号邮箱验证的邮件 ,在open系统完成 2018-06-05
          // sendAccountEmailValidateEmail(psnId, form.getEmail());
        } else {
          logger.error("人员注册失败,从open返回的人员id不为空，但从person表获取的person为空，psnId=" + psnId);
        }
      } else {
        logger.error("人员注册失败,从open返回的人员id为空");
      }
    } catch (Exception e) {
      Long endTime = new Date().getTime();
      logger.error("人员注册失败,time={}, 尝试次数={}, s;参数={}", (endTime - startTime) / 1000, form.getTryCount(),
          form.toString(), e);
      // psnOpenId = this.tryRegisterAgain(form);
    }
    return psnOpenId;
  }



  /**
   * 调用center-open系统的人员注册服务保存注册人员的基本信息
   * 
   * @return
   */
  private String saveRegisterInfo(PersonRegisterForm form, String fromSys) {

    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "000000003djd2x9s");// 系统默认token
    mapDate.put("data", buildRegisterOpenDataParameter(form, fromSys));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class).toString();
  }

  /**
   * http 请求
   * 
   * @param data
   * @param connectTimeoutMs
   * @param readTimeoutMs
   * @return
   * @throws Exception
   */
  private String httpRequest(String data, int connectTimeoutMs, int readTimeoutMs) throws Exception {

    HttpClient httpClient = HttpClientBuilder.create().build();

    HttpPost httpPost = new HttpPost(SERVER_URL);

    /*
     * RequestConfig requestConfig =
     * RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build(
     * ); httpPost.setConfig(requestConfig);
     */

    StringEntity postEntity = new StringEntity(data, "UTF-8");
    httpPost.addHeader("Content-Type", "application/json");
    httpPost.addHeader("Accept", "application/json");
    httpPost.setEntity(postEntity);

    HttpResponse httpResponse = httpClient.execute(httpPost);
    HttpEntity httpEntity = httpResponse.getEntity();

    String temp = EntityUtils.toString(httpEntity, "UTF-8");
    return temp;
  }


  private Object getRegisterInfo(PersonRegisterForm form, String fromSys) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "000000003djd2x9s");// 系统默认token
    mapDate.put("data", buildRegisterOpenDataParameter(form, fromSys));
    return mapDate;
  }

  /**
   * 有时候调用open系统会超时，重试一次
   * 
   * @param form
   * @return
   */
  public Long tryRegisterAgain(PersonRegisterForm form) {
    form.setPsnId(null);
    form.setDes3PsnId("");
    Integer tryCount = form.getTryCount();
    Long psnOpenId = 0L;
    form.setTryCount(tryCount == null ? 2 : ++tryCount);
    if (CommonUtils.compareIntegerValue(form.getTryCount(), 2)) {
      psnOpenId = registerPerson(form);
    }
    return psnOpenId;
  }

  /**
   * 构造调用开放系统所需的参数 密码要经过MD5加密
   * 
   * @param form
   * @return
   */
  private String buildRegisterOpenDataParameter(PersonRegisterForm form, String fromSys) {
    Map<String, Object> date = new HashMap<String, Object>();
    String ename = "";
    // 中文的姓和名为空的话，用中文的name字段拆一下
    if (StringUtils.isBlank(form.getZhfirstName()) && StringUtils.isBlank(form.getZhlastName())
        && StringUtils.isNotBlank(form.getName())) {
      Map<String, String> splitName = this.splitCName(form.getName());
      if (splitName != null) {
        form.setZhfirstName(splitName.get("zhFirstName"));
        form.setZhlastName(splitName.get("zhLastName"));
      }
    }
    ename = form.getFirstName() + " " + form.getLastName();
    String syncXml = "<root><person><name>" + (StringUtils.isNotBlank(form.getName()) ? form.getName() : "")
        + "</name><ename>" + (StringUtils.isNotBlank(ename) ? ename : "") + "</ename><zhFirstName>"
        + (StringUtils.isNotBlank(form.getZhfirstName()) ? form.getZhfirstName() : "") + "</zhFirstName><zhLastName>"
        + (StringUtils.isNotBlank(form.getZhlastName()) ? form.getZhlastName() : "") + "</zhLastName>" + "<firstName>"
        + (StringUtils.isNotBlank(form.getFirstName()) ? form.getFirstName() : "") + "</firstName>" + "<lastName>"
        + (StringUtils.isNotBlank(form.getLastName()) ? form.getLastName() : "") + "</lastName>" + "<email>"
        + (StringUtils.isNotBlank(form.getEmail()) ? form.getEmail() : "") + "</email><newpassword>"
        + (StringUtils.isNotBlank(form.getNewpassword()) ? form.getNewpassword() : "") + "</newpassword><des3InviteId>"
        + (StringUtils.isNotBlank(form.getDes3InviteId()) ? form.getDes3InviteId() : "") + "</des3InviteId><key>"
        + (StringUtils.isNotBlank(form.getKey()) ? form.getKey() : "") + "</key><mobileReg>" + form.getMobileReg()
        + "</mobileReg>" + "<mobile>" + (StringUtils.isNotBlank(form.getMobileNumber()) ? form.getMobileNumber() : "")
        + "</mobile>" + "<emailLanguageVersion>" + LocaleContextHolder.getLocale().getLanguage()
        + "</emailLanguageVersion>" + "</person></root>";
    date.put("fromSys", fromSys);
    date.put("syncXml", syncXml);
    return JacksonUtils.mapToJsonStr(date);
  }

  public Map<String, String> splitCName(String cname) {
    try {
      List<ConstSurName> list = constSurNameDao.findAllSurName();// 查找所有复姓
      Map<String, String> map = new HashMap<String, String>();
      String lastName = "";
      try {
        if (StringUtils.isNotBlank(cname)) {
          char[] names = cname.trim().toCharArray();
          if (names.length >= 2) {// 姓名长度要大于2
            lastName = "" + names[0] + names[1];
            if (!isExistSurName(list, lastName)) {// 不存在复姓
              lastName = "" + names[0];
            }
            map.put("zhLastName", lastName);
            map.put("zhFirstName", cname.replace(lastName, ""));
          } else {
            map.put("zhLastName", "" + names[0]);
            map.put("zhFirstName", "");
          }

        }
      } catch (Exception e) {
        logger.warn("解析复姓的拼音失败:" + cname, e);
      }
      return map;
    } catch (Exception e) {
      return null;
    }
  }

  // 判断是否存在复姓
  public boolean isExistSurName(List<ConstSurName> surNameList, String lastName) {
    boolean flag = false;
    for (ConstSurName surName : surNameList) {
      if (lastName.equals(surName.getName().trim())) {
        flag = true;
        break;
      }
    }
    return flag;

  }

  /**
   * 获取自动登录所需的加密串AID
   */
  @SuppressWarnings("unchecked")
  @Override
  public String getAutoLoginAID(Long openId, String autoLoginType) {
    String AID = "";
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(getAID(openId, autoLoginType).toString());
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
    Map<String, String> data = new HashMap<String, String>();
    data.put("AutoLoginType", autoLoginType);// 注册成功后自动登陆
    mapDate.put("data", JacksonUtils.mapToJsonStr(data));
    mapDate.put("openid", openId);
    mapDate.put("token", "00000000ime82dt2");// 系统默认token
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  /**
   * 保存人员信息各节点同步表
   */
  private void saveSyncPersonInfo(Person person) {
    SyncPerson syncPerson = syncPersonDao.get(person.getPersonId());
    if (syncPerson == null) {
      syncPerson = new SyncPerson();
      syncPerson.setPsnId(person.getPersonId());
    }
    syncPerson.setPsnName(person.getName());
    // 提取姓名首字母
    Character c = this.getFirstLetterByName(syncPerson.getPsnName());
    syncPerson.setFirstLetter(c != null ? c.toString() : "0");
    syncPerson.setPsnFirstName(person.getFirstName());
    syncPerson.setPsnLastName(person.getLastName());
    syncPerson.setPsnOtherName(person.getOtherName());
    syncPerson.setPsnHeadUrl(sysDomainConst.getSnsDomain() + ServiceConstants.DEFAULT_MAN_AVATARS);
    syncPerson.setPsnEmail(person.getEmail());
    syncPerson.setPsnNode(1L);
    syncPersonDao.save(syncPerson);
  }

  /**
   * 提取名称首字母.
   * 
   * @param name
   * @return
   */
  private Character getFirstLetterByName(String name) {
    if (name != null && name.length() > 0) {
      HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
      format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

      char[] names = name.trim().toCharArray();
      for (char c : names) {
        if (Character.isLetter(c)) {
          if (String.valueOf(c).matches("^[a-zA-Z]{1}$")) {
            return Character.toUpperCase(c);
          }
          try {
            String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
            if (pinyin != null && pinyin.length > 0) {
              return (StringUtils.upperCase((pinyin[0])).toCharArray())[0];
            }
          } catch (Exception e) {
            logger.error("提取名称首字母出错", e);
          }
        }
      }
    }

    return null;
  }

  /**
   * 处理基础数据
   * 
   * @param person
   */
  private void baseInfo(Person person) {
    if (StringUtils.isNotBlank(person.getLastName()) && StringUtils.isNotBlank(person.getFirstName())) {
      person.setEname(person.getFirstName() + " " + person.getLastName());
    } else {
      Map<String, String> pinyin = ServiceUtil.parsePinYin(person.getName());
      if (pinyin != null) {
        person.setFirstName(pinyin.get("firstName"));
        person.setLastName(pinyin.get("lastName"));
        person.setEname(person.getFirstName() + " " + person.getLastName());
      }
    }

    // 随机生成头像
    if (StringUtils.isNotBlank(person.getFirstName()) || StringUtils.isNotBlank(person.getLastName())) {
      String a = person.getFirstName() != null ? person.getFirstName().substring(0, 1).toUpperCase() : "";
      String b = person.getLastName() != null ? person.getLastName().substring(0, 1).toUpperCase() : "";
      try {
        String avatars = PersonAvatarsUtils.personAvatars(b + a, person.getPersonId(), rootPath + "/avatars");
        person.setAvatars(domainscm + "/avatars" + avatars);
      } catch (Exception e) {
        logger.error("根据英文名随机产生默认头像失败!");
      }
    }

    if (StringUtils.isBlank(person.getAvatars())) {
      person.setAvatars(ServiceConstants.DEFAULT_MAN_AVATARS);
    }
  }

  /**
   * 注册时保存用户的邮件信息，默认将该邮件设为首要。 邮件唯一.
   * 
   * @param person
   */
  private void emailSave(Person person) throws Exception {
    try {
      if (person != null) {
        PersonEmail email = new PersonEmail();
        email.setEmail(person.getEmail());
        String[] emailPart = person.getEmail().split("@");
        email.setLeftPart(emailPart[0]);
        email.setRightPart(emailPart[1]);
        email.setFirstMail(true);
        // 由于注册时调用了center-open系统里面的逻辑，在open系统里会保存sys_user记录，设置注册邮箱为登录邮箱
        // 所以以注册邮箱去sys_user表查记录永远存在----------------scm-8868
        /*
         * Long userId = userService.findIdByLoginName(person.getEmail()); if
         * (!userId.equals(person.getPersonId())) { email.setLoginMail(0L); } else {
         */
        email.setLoginMail(true);
        /* } */
        // 是否验证过的邮件
        if (person.getIsEmailVerify()) {
          email.setVerify(true);
        } else {
          email.setVerify(false);
        }
        email.setPerson(person);
        personEmailRegisterDao.save(email);
      }
    } catch (Exception e) {

      logger.error("注册时保存用户邮件信息出错", e);
    }
  }

  /**
   * 登录表人员登录账号查重
   */
  private boolean isExists(String loginName) throws Exception {
    boolean isExists = false;
    try {
      isExists = userService.isLoginNameExist(loginName);
    } catch (Exception e) {
      logger.error("判断用户信息异常 ", e);
    }
    return isExists;
  }

  @Override
  public void autoBindWeixin(String wxOpenId, String wxUniobId, Integer bindType, Long psnOpenId) {
    try {
      if (StringUtils.isNotBlank(wxOpenId) && psnOpenId != null && psnOpenId > 0) {
        String weChatToken = oAuth2Service.getWeChatToken();
        StringBuilder url = new StringBuilder();
        url.append("https://api.weixin.qq.com/cgi-bin/user/info?");
        url.append("access_token=" + URLEncoder.encode(weChatToken, "UTF-8"));
        url.append("&openid=" + URLEncoder.encode(wxOpenId, "UTF-8"));
        Map map = MessageUtil.httpRequest(url.toString(), "GET", null);
        String nickname = map.get("nickname").toString();
        WeChatRelation wr = weChatRelationDao.getByWxOpenId(wxOpenId);
        if (wr == null) {
          wr = new WeChatRelation();
          wr.setWebChatOpenId(wxOpenId);
        }
        wr.setWeChatUnionId(StringUtils.isNotBlank(wxUniobId) ? wxUniobId : "0");
        wr.setBindType(bindType);
        wr.setSmateOpenId(psnOpenId);
        wr.setNickName(nickname);
        wr.setCreateTime(new Date());
        weChatRelationDao.save(wr);
      }
    } catch (Exception e) {
      logger.error("注册时自动绑定微信出错", e);
    }

  }

  @Override
  public String produceShortUrl(Long psnId) {
    String shortUrl = "";
    Map<String, Object> map = new HashedMap();
    Map<String, Object> dataMap = new HashedMap();
    Map<String, Object> shortUrlParametMap = new HashedMap();

    map.put("openid", "99999999");
    map.put("token", "00000000sht22url");

    dataMap.put("createPsnId", "0");
    dataMap.put("type", ShortUrlConst.P_TYPE);
    shortUrlParametMap.put("des3PsnId", ServiceUtil.encodeToDes3(psnId.toString()));
    dataMap.put("shortUrlParamet", JacksonUtils.mapToJsonStr(shortUrlParametMap));
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));

    Object obj = restTemplate.postForObject(this.SERVER_URL, map, Object.class);
    Map<String, Object> objMap = JacksonUtils.jsonToMap(obj.toString());
    if (objMap.get("result") != null) {
      List<Map<String, Object>> list = (List<Map<String, Object>>) objMap.get("result");
      if (list != null && list.size() > 0 && list.get(0).get("shortUrl") != null) {
        shortUrl = list.get(0).get("shortUrl").toString();
      }
    }

    PsnProfileUrl psnProfileUrl;
    psnProfileUrl = psnProfileUrlDao.find(psnId);
    if (psnProfileUrl == null) {
      psnProfileUrl = new PsnProfileUrl();
      psnProfileUrl.setPsnId(psnId);
    }
    psnProfileUrl.setPsnIndexUrl(shortUrl);
    psnProfileUrlDao.save(psnProfileUrl);
    return shortUrl;
  }

  @Override
  public void doRegusterBack(PersonRegisterForm form) {
    if (StringUtils.isNotBlank(form.getToken())) {
      RegisterTemp temp = registerTempDao.get(Long.parseLong(Des3Utils.decodeFromDes3(form.getToken())));
      if (temp != null && temp.getStatus() == 0) {
        temp.setStatus(1);
        temp.setUpdateDate(new Date());
      }
    }
  }

  @Override
  public boolean doRegusterBackByCheck(PersonRegisterForm form) throws Exception {
    // 效验token
    if (StringUtils.isNotBlank(form.getToken())) {
      RegisterTemp temp = registerTempDao.get(Long.parseLong(Des3Utils.decodeFromDes3(form.getToken())));
      if (temp != null && temp.getStatus() == 1) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/groupweb/grpmember/ajaxregosterback?token="
            + form.getToken() + "&" + SecurityConstants.AUTO_LOGIN_PARAMETER_NAME + "=" + form.getAid());
        return true;
      }
      if (temp != null && temp.getStatus() == 2 && form.getPersonId().equals(temp.getPsnId())) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/groupweb/mygrp/main?model=myGrp");
        return true;
      }
    }
    // 效验email
    if (StringUtils.isNotBlank(form.getEmail())) {
      Long count = registerTempDao.getCountByEmail(form.getEmail(), 1);
      if (count != null && count > 0) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/groupweb/grpmember/ajaxregosterback?email="
            + form.getEmail() + "&" + SecurityConstants.AUTO_LOGIN_PARAMETER_NAME + "=" + form.getAid());
        return true;
      }
    }
    return false;
  }

  /**
   * 添加动态id
   */
  @Override
  public void addDynamicOpenId(PersonRegisterForm form) throws Exception {
    if (!form.getNeedDynamicOpenId()) {
      return;
    }
    Long psnId = Long.parseLong(Des3Utils.decodeFromDes3(form.getPersonDes3Id()));
    String dynOpenid = DigestUtils.md5Hex(UUID.randomUUID().toString() + psnId);
    // 保存缓存
    oauthCacheService.put(OpenConsts.DYN_OPENID_CACHE, 1 * 60, dynOpenid + "_" + form.getToken(), psnId);

    String back = form.getBack();
    if (back.indexOf("?") > 0) {
      back = back + "&openId=" + dynOpenid;
    } else {
      back = back + "?openId=" + dynOpenid;
    }
    form.setBack(back);
  }

  public static void main(String[] args) {
    String temp = "{\"msg\":\"人员注册成功\",\"result\":[],\"openid\":10006640,\"time\":5722,\"status\":\"success\"}";
    Map<String, String> map = JacksonUtils.jsonToMap(temp);
    System.out.println(map);

  }


}
