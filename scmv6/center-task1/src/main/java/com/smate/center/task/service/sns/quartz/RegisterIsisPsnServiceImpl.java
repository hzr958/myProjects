package com.smate.center.task.service.sns.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.center.task.dao.sns.psn.ConstSurNameDao;
import com.smate.center.task.dao.sns.psn.RegisterIsisPersonTmpDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.model.sns.psn.ConstSurName;
import com.smate.center.task.model.sns.psn.RegisterIsisPersonTmp;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * 
 * @author zzx
 *
 */
@Service("registerIsisPsnService")
@Transactional(rollbackFor = Exception.class)
public class RegisterIsisPsnServiceImpl implements RegisterIsisPsnService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private ConstSurNameDao constSurNameDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private RegisterIsisPersonTmpDao registerIsisPersonTmpDao;
  @Autowired
  private UserDao userDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private RegisterIsisgetOpenIdService registerIsisgetOpenIdService;

  @Override
  public List<RegisterIsisPersonTmp> getList(int batchSize) {
    return registerIsisPersonTmpDao.getList(batchSize);
  }

  @Override
  public void doRegister(RegisterIsisPersonTmp r) throws Exception {
    // 效验token
    if (StringUtils.isBlank(r.getFromToken())) {
      throw new Exception("调用open接口注册FromToken为null,isisPsnId=" + r.getIsisPsnId());
    }
    Long psnOpenId = null;
    Long currentScmPsnId = null;
    // 查询是否存在帐号，存在则直接以这个帐号去获取openId
    Long userId = userDao.findIdByLoginName(r.getEmail());
    if (userId != null) {
      // 取OPENID的方式有调整
      // psnOpenId=registerIsisgetOpenIdService.getOpenId("00000000",
      // userId, 5);

      OpenUserUnion union = openUserUnionDao.getOpenUserUnion(psnOpenId, "00000000");
      if (union == null) {
        throw new Exception("isis注册token00000000的关联关系为null,isisPsnId=" + r.getIsisPsnId());
      } else {
        currentScmPsnId = union.getPsnId();
      }
    }
    // 调用open接口注册获取openId
    if (psnOpenId == null) {
      Object result = openRegister(r);
      if (result != null) {
        Map<String, Object> resultMap = JacksonUtils.jsonToMap(result.toString());
        if ("success".equals(resultMap.get("status"))) {
          String openId = resultMap.get("openid").toString();
          if (StringUtils.isNotBlank(openId)) {
            psnOpenId = Long.valueOf(openId);

            OpenUserUnion union = openUserUnionDao.getOpenUserUnion(psnOpenId, "00000000");
            if (union == null) {
              throw new Exception("isis注册token00000000的关联关系为null,isisPsnId=" + r.getIsisPsnId());
            } else {
              currentScmPsnId = union.getPsnId();
            }
          }
        }
      }
    }
    if (psnOpenId == null || psnOpenId == 0L) {
      throw new Exception("获取的openId为null,isisPsnId=" + r.getIsisPsnId());
    }
    // 1、保存新关联关系

    OpenUserUnion union2 = openUserUnionDao.getOpenUserUnion(psnOpenId, r.getFromToken());
    if (union2 == null) {
      OpenUserUnion newunion = new OpenUserUnion();
      newunion.setCreateType(5);
      newunion.setPsnId(currentScmPsnId);
      newunion.setCreateDate(new Date());
      newunion.setToken(r.getFromToken());
      newunion.setOpenId(psnOpenId);
      openUserUnionDao.save(newunion);
    }
    r.setScmOpenId(psnOpenId);
  }

  private boolean checkEmail(String email) {
    boolean flag = false;
    if (StringUtils.isNotBlank(email)) {
      String check = "^([a-z0-9A-Z_]+[-|\\.]?)+[a-z0-9A-Z_]@([a-z0-9A-Z]+(-[a-z0-9A-Z_]+)?\\.)+[a-zA-Z_]{2,}$";
      Pattern regex = Pattern.compile(check);
      Matcher matcher = regex.matcher(email);
      flag = matcher.matches();
    }
    return flag;
  }

  private Object openRegister(RegisterIsisPersonTmp r) throws Exception {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "000000003djd2x9s");// 系统默认token
    mapDate.put("data", buildRegisterOpenDataParameter(r, "sns"));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  /**
   * 构造调用开放系统所需的参数 密码要经过MD5加密
   * 
   * @param person
   * @return
   */
  private String buildRegisterOpenDataParameter(RegisterIsisPersonTmp r, String fromSys) throws Exception {
    Random random = new Random();
    Integer s = random.nextInt(999999) % (999999 - 100000 + 1) + 100000;
    String newpassword = DigestUtils.md5Hex(s.toString());
    String des3InviteId = "";
    String key = "";
    Map<String, Object> date = new HashMap<String, Object>();
    String ename = "";
    // 中文的姓和名为空的话，用中文的name字段拆一下
    if (StringUtils.isBlank(r.getFirstName()) && StringUtils.isBlank(r.getLastName())
        && StringUtils.isNotBlank(r.getName())) {
      Map<String, String> splitName = this.splitCName(r.getName());
      if (splitName != null) {
        r.setFirstName(splitName.get("zhFirstName"));
        r.setLastName(splitName.get("zhLastName"));
      }
    }
    // 3、保存城市地址
    String regionId = "";
    ConstRegion constRegion = null;
    if (constRegion == null && StringUtils.isNotBlank(r.getCity())) {
      constRegion = constRegionDao.getConstRegionByName(r.getCity());
    }
    if (constRegion == null && StringUtils.isNotBlank(r.getPosition())) {
      constRegion = constRegionDao.getConstRegionByName(r.getPosition());
    }
    if (constRegion == null && StringUtils.isNotBlank(r.getCountry())) {
      constRegion = constRegionDao.getConstRegionByName(r.getCountry());
    }
    if (constRegion != null) {
      regionId = constRegion.getId().toString();
    }
    ename = r.getFirstName() + " " + r.getLastName();
    String syncXml = "<root><person><name>" + (StringUtils.isNotBlank(r.getName()) ? r.getName() : "")
        + "</name><ename>" + (StringUtils.isNotBlank(ename) ? ename : "") + "</ename><zhFirstName>"
        + (StringUtils.isNotBlank(r.getFirstName()) ? r.getFirstName() : "") + "</zhFirstName><zhLastName>"
        + (StringUtils.isNotBlank(r.getLastName()) ? r.getLastName() : "") + "</zhLastName>" + "<firstName>"
        + (StringUtils.isNotBlank(r.getFirstName()) ? r.getFirstName() : "") + "</firstName>" + "<lastName>"
        + (StringUtils.isNotBlank(r.getLastName()) ? r.getLastName() : "") + "</lastName>" + "<email>"
        + (StringUtils.isNotBlank(r.getEmail()) ? r.getEmail() : "") + "</email><newpassword>"
        + (StringUtils.isNotBlank(newpassword) ? newpassword : "") + "</newpassword><insName>"
        + (StringUtils.isNotBlank(r.getInsName()) ? r.getInsName() : "") + "</insName><position>"
        + (StringUtils.isNotBlank(r.getPosition()) ? r.getPosition() : "") + "</position><department>"
        + (StringUtils.isNotBlank(r.getDepartment()) ? r.getDepartment() : "") + "</department>" + "<unit>"
        + (StringUtils.isNotBlank(r.getDepartment()) ? r.getDepartment() : "") + "</unit><birthday>"
        + (StringUtils.isNotBlank(r.getBirthDate()) ? (r.getBirthDate().replaceAll("-", "/")) : "")
        + "</birthday><degreeName>" + (StringUtils.isNotBlank(r.getDegree()) ? r.getDegree() : "")
        + "</degreeName><des3InviteId>" + (StringUtils.isNotBlank(des3InviteId) ? des3InviteId : "")
        + "</des3InviteId><key>" + (StringUtils.isNotBlank(key) ? key : "")
        + "</key><mobileReg>1</mobileReg><positionType>0</positionType>" + "<regionId2>"
        + (StringUtils.isNotBlank(regionId) ? regionId : "") + "</regionId2>" + "</person></root>";
    String createType = "4";
    date.put("createType", createType);
    date.put("fromSys", fromSys);
    date.put("syncXml", syncXml);
    return JacksonUtils.mapToJsonStr(date);
  }

  private Map<String, String> splitCName(String cname) throws Exception {
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
  }

  // 判断是否存在复姓
  public boolean isExistSurName(List<ConstSurName> surNameList, String lastName) throws Exception {
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
  public void save(RegisterIsisPersonTmp r) {
    registerIsisPersonTmpDao.save(r);
  }

}
