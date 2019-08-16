package com.smate.center.open.service.interconnection;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenThirdRegDao;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.interconnection.AccountInterconnectionForm;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.center.open.service.user.UserService;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.security.Des3Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
  private OpenPersonManager openPersonManager;
  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private ThirdLoginService thirdLoginService;
  @Autowired
  private OpenThirdRegDao openThirdRegDao;
  @Autowired
  private UserService userService;
  @Autowired
  private UserDao userDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  // 人员信息匹配
  public void dofindMatchUser(AccountInterconnectionForm form) {
    if (StringUtils.isNotBlank(form.getEmail()) && StringUtils.isNotBlank(form.getInsName())
        && (StringUtils.isNotBlank(form.getUserName())
            || (StringUtils.isNotBlank(form.getFirstName()) && StringUtils.isNotBlank(form.getLastName())))) {
      openPersonManager.findMatchPerson(form);
    }
  }

  /**
   * 判断第三方系统的登录邮箱是否在系统中已存在
   * 
   * @param email
   * @return
   */
  @Override
  public boolean findEmailIsExist(String email) throws OpenException {
    boolean exist = false;
    try {
      Long psnId = userDao.findIdByLoginName(email);
      if (psnId != null) {
        exist = true;
      }
    } catch (Exception e) {
      logger.error("互联互通--------校验邮箱是否存在出错", e);
      throw new OpenException();
    }
    return exist;
  }

  @Override
  public boolean checkUrl(AccountInterconnectionForm form) throws OpenException {
    try {
      String token = form.getToken();
      if (StringUtils.isNotBlank(token)) {
        token = URLEncoder.encode(token, "utf-8");
        Serializable serializable = openCacheService.get(OpenConsts.UNION_URL_PARAM_CACHE, token);
        if (serializable != null) {
          String paramStr = serializable.toString();
          buildFormParm(paramStr, form);
        }
        openCacheService.remove(OpenConsts.UNION_URL_PARAM_CACHE, token);
        if (openCacheService.get(OpenConsts.UNION_URL_CACHE, token) != null
            && (boolean) openCacheService.get(OpenConsts.UNION_URL_CACHE, token)) {
          return true;
        }
      }
    } catch (Exception e) {
      logger.error("互联互通----检验url有效性出错", e);
      throw new OpenException();
    }
    return false;
  }

  /**
   * 把缓存中带来的参数赋给form
   * 
   * @param paramStr
   * @param form
   */
  private void buildFormParm(String paramStr, AccountInterconnectionForm form) {
    Map<String, String> pMap = JacksonUtils.jsonToMap(paramStr);
    if (pMap != null) {
      form.setInsName(pMap.get("insName"));
      form.setUserName(pMap.get("userName"));
      form.setPosition(pMap.get("position"));
      form.setDepartment(pMap.get("department"));
      form.setFromSys(pMap.get("fromSys"));
      form.setEmail(pMap.get("email"));
      form.setFirstName(pMap.get("firstName"));
      form.setLastName(pMap.get("lastName"));
      form.setDepartment(pMap.get("department"));
      form.setDegree(pMap.get("degree"));
      form.setDisciplineCode(pMap.get("disciplineCodes"));
      form.setResearchArea(pMap.get("researchArea"));
      form.setCountry(pMap.get("country"));
      form.setProvince(pMap.get("province"));
      form.setCity(pMap.get("city"));
      form.setBirthdate(pMap.get("birthdate"));
      form.setSysName(pMap.get("sysName"));
      form.setCrossUrl(pMap.get("crossUrl"));
      form.setKeyWordsZh(pMap.get("zhKeyWords"));
      form.setKeyWordsEn(pMap.get("enKeyWords"));
      form.setDemoOpenId(pMap.get("openId"));
    }
  }

  /**
   * @Description: 快速关联登录
   * @param @param form
   * @param @return
   * @param @throws Exception
   * @return
   * @author ltl
   */
  @Override
  public Long doFastRelateAccount(AccountInterconnectionForm form) throws Exception {
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
    // 保存关联并获取
    Long openId = thirdLoginService.getOpenId(form.getToken(), psnId, OpenConsts.OPENID_CREATE_TYPE_4);
    if (openId == null) {
      logger.error("关联失败");
      form.setMsg("关联失败");
    }
    return openId;
  }



  /**
   * @Description: 已有账号登录
   * @param @param form
   * @param @return
   * @param @throws Exception
   * @return
   * @author ltl
   */
  @Override
  public Long doRelateExistAccount(AccountInterconnectionForm form) throws Exception {
    // TODO Auto-generated method stub
    Long openId = 0L;
    if (StringUtils.isNotBlank(form.getToken()) && this.checkSysToken(form.getToken())) {
      User user =
          userService.getUser(form.getEmail().trim().toLowerCase(), DigestUtils.md5Hex(form.getPassword().trim()));
      if (user == null) { // 找不到人员
        logger.error("帐号/科研号或密码错误");
        form.setMsg("帐号/科研号或密码错误");
        return null;
      }
      /*
       * // 判断是否该用户关联过 openId = openUserUnionDao.getOpenIdByPsnId(user.getId()); if (openId != null) {
       * OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnion(openId, form.getToken()); if
       * (openUserUnion != null) {// 已经关联过了 form.setMsg("该账号已经关联过了"); form.setRelationExist(true); return
       * openId; } }
       */
      // 保存关联并获取
      openId = thirdLoginService.getOpenId(form.getToken(), user.getId(), OpenConsts.OPENID_CREATE_TYPE_4);
      if (openId == null) {
        logger.error("关联失败");
        form.setMsg("关联失败");
      }
    } else {
      logger.error("登录失败, 互联互通---来源系统未开放权限");
      form.setMsg("登录失败, 来源系统未开放权限");
      return null;
    }
    return openId;
  }

  @Override
  public String generateSignature(AccountInterconnectionForm form) throws Exception {
    String md5Hex = "";
    if (form.getOpenId() != null) {
      md5Hex = DigestUtils.md5Hex("" + form.getOpenId() + UUID.randomUUID().toString());
      openCacheService.put(OpenConsts.UNION_URL_CACHE, 5 * 60, form.getOpenId().toString(), md5Hex);
    }
    return md5Hex;
  }

  @Override
  public boolean CheckRelate(AccountInterconnectionForm form) throws Exception {
    String s = (String) openCacheService.get(OpenConsts.UNION_URL_CACHE, form.getOpenId().toString());
    if (StringUtils.isNotBlank(form.getSignature()) && form.getSignature().equals(s)) {
      openCacheService.remove(OpenConsts.UNION_URL_CACHE, form.getOpenId().toString());
      return true;
    }
    return false;
  }

  @Override
  public boolean checkSysToken(String token) throws OpenException {
    boolean flag = false;
    if (StringUtils.isNotBlank(token)) {
      String result = openThirdRegDao.getThirdSysNameByToken(token);
      if (result != null) {
        flag = true;
      }
    }
    return flag;
  }

  /**
   * 同步人员信息
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean syncPersonInfo(AccountInterconnectionForm form) throws Exception {
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(savePersonInfo(form).toString());
    if ("success".equals(resultMap.get("status"))) {
      return true;
    }
    return false;
  }

  /**
   * 调restful,保存人员信息到third_sys_psn_info表
   */
  private Object savePersonInfo(AccountInterconnectionForm form) {
    Map<String, Object> mapData = new HashMap<String, Object>();
    mapData.put("openid", form.getOpenId());
    mapData.put("token",
        ((StringUtils.isNotBlank(form.getFromSys()) && form.getFromSys().length() != 16) ? form.getFromSys()
            : "00000000") + "3djd2x2x");
    mapData.put("data", buildPersonDataParameter(form));
    return restTemplate.postForObject(SERVER_URL, mapData, Object.class);
  }

  /**
   * 构造人员的 xml
   */
  private Object buildPersonDataParameter(AccountInterconnectionForm form) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> psnDataMap = new HashMap<String, Object>();
    psnDataMap.put("name", (StringUtils.isNotBlank(form.getUserName()) ? form.getUserName() : ""));
    psnDataMap.put("email", (StringUtils.isNotBlank(form.getEmail()) ? form.getEmail() : ""));
    psnDataMap.put("insName", (StringUtils.isNotBlank(form.getInsName()) ? form.getInsName() : ""));
    psnDataMap.put("department", (StringUtils.isNotBlank(form.getDepartment()) ? form.getDepartment() : ""));
    psnDataMap.put("position", (StringUtils.isNotBlank(form.getPosition()) ? form.getPosition() : ""));
    psnDataMap.put("degree", (StringUtils.isNotBlank(form.getDegree()) ? form.getDegree() : ""));
    // 改字段名
    psnDataMap.put("disciplineCodes",
        (StringUtils.isNotBlank(form.getDisciplineCode()) ? form.getDisciplineCode() : ""));
    psnDataMap.put("researchDirection", (StringUtils.isNotBlank(form.getResearchArea()) ? form.getResearchArea() : ""));// 实体用的是researchDirection
    psnDataMap.put("country", (StringUtils.isNotBlank(form.getCountry()) ? form.getCountry() : ""));
    psnDataMap.put("province", (StringUtils.isNotBlank(form.getProvince()) ? form.getProvince() : ""));
    psnDataMap.put("city", (StringUtils.isNotBlank(form.getCity()) ? form.getCity() : ""));
    psnDataMap.put("birthdate", (StringUtils.isNotBlank(form.getBirthdate()) ? form.getBirthdate() : ""));
    psnDataMap.put("keyWordsEn", (StringUtils.isNotBlank(form.getKeyWordsEn()) ? form.getKeyWordsEn() : ""));
    psnDataMap.put("keyWordsZh", (StringUtils.isNotBlank(form.getKeyWordsZh()) ? form.getKeyWordsZh() : ""));
    map.put("psnData", JacksonUtils.mapToJsonStr(psnDataMap));
    // <entry key="ISIS" value-ref="isisRegisterPersonNsfcIsisService" />
    // 因为key是ISIS,所以添加系统名称字段,fromSys为ISIS
    map.put("fromSys", form.getSysName());
    return JacksonUtils.mapToJsonStr(map);
  }
}
