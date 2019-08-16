package com.smate.center.oauth.service.interconnection;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.center.oauth.consts.OauthConsts;
import com.smate.center.oauth.dao.profile.ConstSurNameDao;
import com.smate.center.oauth.dao.profile.OpenUserUnionDao;
import com.smate.center.oauth.dao.profile.PersonEmailDao;
import com.smate.center.oauth.dao.profile.SyncPersonDao;
import com.smate.center.oauth.dao.security.OpenThirdRegDao;
import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.model.interconnection.AccountInterconnectionForm;
import com.smate.center.oauth.model.profile.ConstSurName;
import com.smate.center.oauth.model.profile.PersonEmail;
import com.smate.center.oauth.model.profile.SyncPerson;
import com.smate.center.oauth.service.profile.psncnf.PsnCnfReBuildService;
import com.smate.center.oauth.service.user.UserSettingsService;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 账号关联服务类实现
 * 
 * @author zll
 */
@Service("accountInterconnectionService")
@Transactional(rollbackFor = Exception.class)
public class AccountInterconnectionServiceImpl implements AccountInterconnectionService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private PersonEmailDao personEmailRegisterDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private UserSettingsService userSettingsService;
  @Autowired
  private PsnCnfReBuildService psnCnfReBuildService;
  @Autowired
  private SyncPersonDao syncPersonDao;
  @Autowired
  private OauthCacheService oauthCacheService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private OpenThirdRegDao openThirdRegDao;
  @Autowired
  private ConstSurNameDao constSurNameDao;

  @Override
  public Long doCreateAndRelateAccount(AccountInterconnectionForm form) {
    // 注册时会生成openId
    return this.doCreateAccount(form);
  }

  /**
   * 注册账号
   * 
   * @param form
   * @return
   */
  private Long doCreateAccount(AccountInterconnectionForm form) {
    Long psnOpenId = 0L;
    try {
      // 调用center-open的人员注册服务,保存人员基本信息
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(saveRegisterInfo(form, "sns").toString());
      if ("success".equals(resultMap.get("status"))) {
        String openId = resultMap.get("openid").toString();
        if (StringUtils.isNotBlank(openId)) {
          psnOpenId = Long.valueOf(openId);
        }
      }
      // 保存人员信息各节点同步表
      Long psnId = openUserUnionDao.getOpenUserUnionPsnIdByOpenId(psnOpenId);
      if (psnId != null) {
        Person psn = personProfileDao.queryPersonForRegisterByPsnId(psnId);
        if (psn != null) {
          // 以下逻辑已移到open那边
          // 处理基础数据
          // baseInfo(psn);
          // 处理邮件信息,该部分已经在任务中处理
          // emailSave(psn);
          // 存储一份用户隐私配置
          // userSettingsService.initPrivacySettingsConfig(psnId);
          // 初始化默认的权限配置
          // psnCnfReBuildService.init(psnId);
          // this.saveSyncPersonInfo(psn);
        } else {
          logger.error("人员注册失败,从open返回的人员id不为空，但从person表获取的person为空，psnId=" + psnId);
        }
      } else {
        logger.error("人员注册失败,从open返回的人员id为空");
      }
    } catch (Exception e) {
      logger.error("人员注册失败", e);
    }
    return psnOpenId;
  }

  /**
   * 调用center-open系统的人员注册服务保存注册人员的基本信息
   * 
   * @return
   */
  private Object saveRegisterInfo(AccountInterconnectionForm form, String fromSys) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token",
        ((StringUtils.isNotBlank(form.getFromSys())
            && form.getFromSys().toString().length() != OauthConsts.TOKEN_MAXLENGTH) ? form.getFromSys() : "00000000")
            + "3djd2x9s");// 系统默认token
    mapDate.put("data", buildRegisterOpenDataParameter(form, fromSys));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  /**
   * 构造调用开放系统所需的参数 密码要经过MD5加密 mobileReg: 原先的一些用任务跑的现在要直接执行
   * 
   * @param person
   * @return
   */
  private String buildRegisterOpenDataParameter(AccountInterconnectionForm form, String fromSys) {
    Map<String, Object> date = new HashMap<String, Object>();
    String ename = "";
    // 中文的姓和名为空的话，用中文的name字段拆一下
    if (StringUtils.isBlank(form.getZhfirstName()) && StringUtils.isBlank(form.getZhlastName())
        && StringUtils.isNotBlank(form.getUserName())) {
      if (!ServiceUtil.isChineseStr(form.getUserName())) {
        String[] splited = form.getUserName().split("\\s+", 2);
        if (splited.length == 2) {
          if (form.getUserName().indexOf("-") > -1) {
            form.setZhlastName(splited[1]);
            form.setZhfirstName(splited[0]);
          } else {
            form.setZhlastName(splited[0]);
            form.setZhfirstName(splited[1]);
          }
        } else {
          form.setZhlastName(splited[0]);
        }
      } else {
        Map<String, String> splitName = this.splitCName(form.getUserName());
        if (splitName != null) {
          form.setZhfirstName(splitName.get("zhFirstName"));
          form.setZhlastName(splitName.get("zhLastName"));
        }
      }
    }
    if (form.getUserName() != null) {// firstName为空时拆分name转成拼音
      Map<String, String> nameMap = ServiceUtil.parsePinYin(form.getUserName());
      if (!StringUtils.isNotBlank(form.getFirstName())) {
        form.setFirstName(nameMap.get("firstName"));
      }
      if (!StringUtils.isNotBlank(form.getLastName())) {
        form.setLastName(nameMap.get("lastName"));
      }
    }
    ename = form.getFirstName() + " " + form.getLastName();
    String syncXml = "<root><person><name>" + (StringUtils.isNotBlank(form.getUserName()) ? form.getUserName() : "")
        + "</name><firstName>" + (StringUtils.isNotBlank(form.getFirstName()) ? form.getFirstName() : "")
        + "</firstName><lastName>" + (StringUtils.isNotBlank(form.getLastName()) ? form.getLastName() : "")
        + "</lastName><ename>" + (StringUtils.isNotBlank(ename) ? ename : "") + "</ename><zhFirstName>"
        + (StringUtils.isNotBlank(form.getZhfirstName()) ? form.getZhfirstName() : "") + "</zhFirstName><zhLastName>"
        + (StringUtils.isNotBlank(form.getZhlastName()) ? form.getZhlastName() : "") + "</zhLastName><email>"
        + (StringUtils.isNotBlank(form.getEmail()) ? form.getEmail() : "") + "</email><newpassword>"
        + (StringUtils.isNotBlank(form.getPassword()) ? form.getPassword() : "") + "</newpassword><insName>"
        + (StringUtils.isNotBlank(form.getInsName()) ? form.getInsName() : "") + "</insName><position>"
        + (StringUtils.isNotBlank(form.getPosition()) ? form.getPosition() : "") + "</position><unit>"
        + (StringUtils.isNotBlank(form.getDepartment()) ? form.getDepartment() : "") + "</unit><birthday>"
        + (StringUtils.isNotBlank(form.getBirthday()) ? form.getBirthday() : "") + "</birthday><degreeName>"
        + (StringUtils.isNotBlank(form.getDegree()) ? form.getDegree() : "") + "</degreeName>"
        + (form.getUnitId() != null ? "<unitId>" + form.getUnitId() + "</unitId>" : "")
        + (form.getInsId() != null ? "<insId>" + form.getInsId() + "</insId>" : "")
        + "<positionType>0</positionType></person></root>";
    String createType = "4";
    date.put("createType", createType);
    date.put("fromSys", fromSys);
    date.put("syncXml", syncXml);
    return JacksonUtils.mapToJsonStr(date);
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
    if (StringUtils.isBlank(person.getAvatars())) {
      person.setAvatars(sysDomainConst.getSnsDomain() + ServiceConstants.DEFAULT_MAN_AVATARS);
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
        email.setLoginMail(true);
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

  @Override
  public String generateSignature(AccountInterconnectionForm form) throws Exception {
    String md5Hex = DigestUtils.md5Hex("" + form.getOpenId() + new Date());
    cacheService.put(SecurityConstants.UNION_URL_CACHE, 5 * 60, form.getOpenId().toString(), md5Hex);
    return md5Hex;
  }

  @Override
  public boolean checkSysToken(String token) throws OauthException {
    boolean flag = false;
    if (StringUtils.isNotBlank(token)) {
      String result = openThirdRegDao.getThirdSysNameByToken(token);
      if (result != null) {
        flag = true;
      }
    }
    return flag;
  }
}
