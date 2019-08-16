package com.smate.center.open.service.data.psnInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.data.ThirdDataTypeService;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.StrUtils;

/**
 * 科创云 根据 登录帐号和人名来获取人员信息，loginEmail userName 不存在，调注册接口，返回人员信息
 * 
 * @author aijiangbin
 * @date 2018年5月23日
 */
@Transactional(rollbackFor = Exception.class)
public class GetPsnInfoRegisterServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  public final static String SCM_TOKEN = "00000000";// 科研之友的token
  public final static Integer CREATE_TYPE = 2; // 人员注册 创建类型


  @Value("${domainscm}")
  public String domainscm;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private UserDao userDao;

  private ThirdDataTypeService thirdDataTypeService;
  @Autowired()
  @Qualifier("registerPersonServiceImpl")
  private ThirdDataTypeService registerPersonServiceImpl;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }

    Object emailObj = serviceData.get("loginEmail");
    Object userNameObj = serviceData.get("userName");
    Object dataTypeObj = serviceData.get("dataType");
    if (emailObj == null || StringUtils.isBlank(emailObj.toString())) {
      logger.error("具体服务类型参数loginEmail不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2010, paramet, "具体服务类型参数loginEmail不能为空");
      return temp;
    }

    if (!StrUtils.isEmail(emailObj.toString().trim())) {
      logger.error("具体服务类型参数loginEmail格式不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2013, paramet, "具体服务类型参数loginEmail格式不正确");
      return temp;
    }

    if (userNameObj == null || StringUtils.isBlank(userNameObj.toString())) {
      logger.error("具体服务类型参数userName不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2011, paramet, "具体服务类型参数userName不能为空");
      return temp;
    }
    if (dataTypeObj == null || StringUtils.isBlank(dataTypeObj.toString())) {
      logger.error("具体服务类型参数dataType不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2012, paramet, "具体服务类型参数dataType不能为空");
      return temp;
    }
    serviceData.put("loginEmail", emailObj.toString().trim());
    serviceData.put("userName", userNameObj.toString().trim());

    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  // 判断是否存在我们系统 返回psnId
  private Long findPsnIdByEmailAndName(User user, String userName) {
    Person person = personProfileDao.get(user.getId());
    if (userName.equals(person.getName()) || userName.equals(person.getEname())
        || userName.equals(person.getFirstName() + " " + person.getLastName())) {
      return user.getId();
    }
    return null;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    String loginEmail = paramet.get("loginEmail").toString();
    String userName = paramet.get("userName").toString();
    User user = userDao.findByLoginName(loginEmail);
    Long psnId = 0L;
    if (user == null) {
      // 没找到帐号，注册一个帐号
      Map<String, Object> resultMap = registerAccount(paramet, loginEmail, userName);
      // {msg=人员注册成功, data=[], status=success}
      if (resultMap.get("msg").equals("人员注册成功")) {
        user = userDao.findByLoginName(loginEmail);
        if (user != null) {
          psnId = user.getId();
        }
      }
    } else {
      // 匹配到帐号， 判断用户名是否正确 不正确直接返回为空
      psnId = findPsnIdByEmailAndName(user, userName);
    }
    if (psnId == null || psnId == 0L) {
      Map<String, Object> resultMap = new HashMap<>();
      resultMap.put("msg", "没有匹配到帐号");
      dataList.add(resultMap);
      return successMap(OpenMsgCodeConsts.SCM_000, dataList);
    }
    // 调用获取人员信息服务来获取信息
    paramet.put(OpenConsts.MAP_PSNID, psnId);
    Map<String, Object> resultMap = thirdDataTypeService.handleOpenDataForType(paramet);
    return resultMap;
  }

  public Map<String, Object> registerAccount(Map<String, Object> paramet, String loginEmail, String userName) {
    // 没有匹配到帐号，就直接注册
    Map<String, Object> data = new HashMap<>();
    String insName = getMaxString(paramet.get("insName"), 300);
    String tel = getMaxString(paramet.get("tel"), 50);
    // String password ="96e79218965eb72c92a549dd5a330112";//默认密码 6个1
    data.put(OpenConsts.MAP_DATA_FROM_SYS, paramet.get("dataType"));// 来源
    String syncXml = "<root>" + "<person>" + "	<name>" + userName + "</name>" + "	<email>" + loginEmail + "</email>"
        + "	<newpassword></newpassword>" + "	<insName>" + insName + "</insName>" + "	<tel>" + tel + "</tel>"
        + "	<birthday></birthday>" + "	<position></position>" + "	<degreeName></degreeName>"
        + "	<orgnature></orgnature>" + "</person>" + "</root>";
    data.put(OpenConsts.MAP_SYNCXML, syncXml);
    paramet.put(OpenConsts.MAP_DATA, JacksonUtils.mapToJsonStr(data));

    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "000000003djd2x9s");// 系统默认token
    mapDate.put("data", JacksonUtils.mapToJsonStr(data));
    Object result = restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(result.toString());
    return resultMap;
  }

  public ThirdDataTypeService getThirdDataTypeService() {
    return thirdDataTypeService;
  }

  public void setThirdDataTypeService(ThirdDataTypeService thirdDataTypeService) {
    this.thirdDataTypeService = thirdDataTypeService;
  }

  private String getMaxString(Object obj, int length) {
    if (obj == null || StringUtils.isBlank(obj.toString())) {
      return "";
    }
    String str = obj.toString().trim();
    if (str.length() > length) {
      str = str.substring(0, length);
    }
    return str;
  }



}
