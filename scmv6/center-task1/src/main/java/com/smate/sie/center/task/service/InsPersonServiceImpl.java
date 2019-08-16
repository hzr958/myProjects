package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.consts.SieConstPosition;
import com.smate.core.base.utils.service.consts.SieConstPositionService;
import com.smate.sie.center.task.dao.ImportThirdUnitsHistoryDao;
import com.smate.sie.center.task.dao.InsPsnDao;
import com.smate.sie.center.task.dao.Sie6InsUnitDao;
import com.smate.sie.center.task.model.ImportThirdPsns;
import com.smate.sie.center.task.model.Sie6InsUnit;
import com.smate.sie.center.task.model.SiePsnIns;
import com.smate.sie.center.task.model.SiePsnInsPk;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;
import com.smate.sie.core.base.utils.model.psn.SieInsPersonPk;
import com.smate.sie.core.base.utils.service.psn.InsPersonAddorDelRelationService;
import com.smate.sie.core.base.utils.service.psn.PsnInfoSyncToSnsService;
import com.smate.sie.core.base.utils.service.psn.SieInsPsnLogService;
import com.smate.sie.core.base.utils.string.JPinyinUtil;

/**
 * 单位人员服务实现.
 * 
 * @author xys
 *
 */
@Service("insPersonService")
@Transactional(rollbackFor = Exception.class)
public class InsPersonServiceImpl implements InsPersonService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private InsPsnDao insPsnDao;
  @Autowired
  private ImportThirdUnitsHistoryDao importThirdUnitsHistoryDao;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private SieConstPositionService sieConstPositionService;
  @Autowired
  private PsnInfoSyncToSnsService psnInfoSyncToSnsService;
  @Autowired
  private SieInsPsnLogService sieInsPsnLogService;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private Sie6InsUnitDao sie6InsUnitDao;

  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Autowired
  private InsPersonAddorDelRelationService insPersonAddorDelRelationService;

  @Autowired
  private SieInsUnitMergeService sieInsUnitMergeService;

  @SuppressWarnings("rawtypes")
  @Override
  public void refreshInsPsn(ImportThirdPsns importThirdPsns) throws ServiceException {
    List result = null;
    Long psnId = null;
    Long insId = importThirdPsns.getPk().getInsId();
    String unitId = importThirdPsns.getUnitId();
    String email = importThirdPsns.getPk().getEmail();
    String zhName = importThirdPsns.getZhName();
    Map<String, String> pinyin = JPinyinUtil.parseFullNamePinYin(zhName);
    String firstName = pinyin.get("firstName");
    String lastName = pinyin.get("lastName");
    importThirdPsns.setFirstName(firstName);
    importThirdPsns.setLastName(lastName);
    String position = importThirdPsns.getPosition();
    Long sieUnitId = null;
    if (unitId != null && StringUtils.isNotBlank(unitId)) {
      sieUnitId = this.importThirdUnitsHistoryDao.getSieUnitId(insId, unitId);
      sieUnitId = sieUnitId != null && sieUnitId > 0 ? sieUnitId : null;
    }
    String sieUnitName = null;
    if (sieUnitId != null) {
      Sie6InsUnit unit = sie6InsUnitDao.get(sieUnitId);


      // sieUnitId !=null ,考虑merge_unit表， 找到最新的finalUnitId
      if (unit == null) {// ins_unit表不存在记录时， 则去merge_unit 表中查询是否被合并了;
        Long finalUnitId = null;
        finalUnitId = sieInsUnitMergeService.getFinalUnitId(insId, sieUnitId);

        // 当finalUnitId 与 sieUnitId 不相等时
        if (finalUnitId != null && !sieUnitId.equals(finalUnitId)) {
          sieUnitId = finalUnitId;
        }

        // 根据最新的sieUnitId 再查下部门
        unit = sie6InsUnitDao.get(sieUnitId);
      }

      sieUnitName = unit == null ? null : unit.getZhName();
    }
    importThirdPsns.setSieUnitName(sieUnitName);
    importThirdPsns.setSieUnitId(sieUnitId);
    try {
      Map<String, Object> resultMap =
          JacksonUtils.jsonToMap(checkEmail(importThirdPsns.getPk().getEmail(), "SIE").toString());
      if ("success".equals(resultMap.get("status"))) {
        result = (List) resultMap.get("result");
        Map psnMap = (Map) result.get(0);
        String psnIdStr = ObjectUtils.toString(psnMap.get("psnid"));
        if (StringUtils.isNotBlank(psnIdStr)) {
          psnId = Long.parseLong(psnIdStr);
        }
      } else {
        logger.error("检查email是否是科研之友账号（oxui3en2）返回错误结果,insId:{},email:{},zhName:{}",
            new Object[] {insId, email, zhName});
        throw new ServiceException("检查email是否是科研之友账号（oxui3en2）返回错误结果：" + resultMap.toString());
      }
      if (psnId != null && psnId > 0) {// 根据邮箱判断人员是否存在，存在则更新单位人员信息/创建单位人员关系
        // 首次加入单位
        Boolean isNewPsnIns = false;
        SiePsnIns psnIns = this.insPsnDao.get(new SiePsnInsPk(psnId, insId));
        SieConstPosition constPosition = this.sieConstPositionService.getPosByName(position);
        Long posId = null;
        String posGrades = null;
        if (constPosition != null) {
          posId = constPosition.getId();
          posGrades = constPosition.getPosGrades();
        }
        if (psnIns != null) {
          psnIns.setEmail(email);
          psnIns.setZhName(zhName);
          psnIns.setFirstName(firstName);
          psnIns.setLastName(lastName);
          if (firstName != null && lastName != null) {
            psnIns.setEnName(firstName + " " + lastName);
          }
          psnIns.setPosition(position);
          psnIns.setPosGrades(posGrades);
          psnIns.setPosId(posId);
          psnIns.setUnitId(sieUnitId);
          psnIns.setStatus(1);
        } else {
          isNewPsnIns = true;
          psnIns = new SiePsnIns(psnId, insId, zhName, firstName, lastName, email, posId, position, posGrades,
              sieUnitId, 1, 1);
        }
        psnIns.setUnitName(sieUnitName);
        this.insPsnDao.save(psnIns);
        // 调用个人版接口同步人员信息至个人版
        SieInsPerson psn = convertToSelf(psnIns);
        String resultmsg = psnInfoSyncToSnsService.doSync(psn);
        String msg = "";
        // 已有账号首次加入单位，添加单位与人员的关联
        if (isNewPsnIns) {
          String token = (String) cacheService.get("ins_token", "ins_token" + insId);
          if (StringUtils.isBlank(token)) {
            token = insPersonAddorDelRelationService.getInsToken(insId);
            if (StringUtils.isNotBlank(token)) {
              cacheService.put("ins_token", CacheService.EXP_DAY_1, "ins_token" + insId, token);
            }
          }
          if (StringUtils.isNotBlank(token)) {
            Map<String, Object> resultMap2 = insPersonAddorDelRelationService.addRelation(psnId, token);
            if (resultMap2 != null && resultMap2.get("msg") != null) {
              msg = resultMap2.get("msg").toString();
            }
          }

        }
        sieInsPsnLogService.log(0L, psnIns.getPk().getInsId(), psnId, "update",
            "导入第三方人员信息任务，更新人员;" + resultmsg + ";" + msg);
      } else {// 不存在则插入 调用个人版注册接口后再插入到psn_ins表
        this.dealRegisterBusi(importThirdPsns);
      }
    } catch (Exception e) {
      logger.error("刷新单位人员信息出现异常了喔,insId:{},email:{},zhName:{}", new Object[] {insId, email, zhName, e});
      throw new ServiceException("刷新单位人员信息出现异常了喔", e);
    }
  }

  private SieInsPerson convertToSelf(SiePsnIns Inspsn) {
    SieInsPerson psnIns = null;
    if (Inspsn != null) {
      psnIns = new SieInsPerson();
      psnIns.setPk(new SieInsPersonPk(Inspsn.getPk().getPsnId(), Inspsn.getPk().getInsId()));
      psnIns.setPosId(Inspsn.getPosId());
      psnIns.setPosition(Inspsn.getPosition());
      psnIns.setZhName(Inspsn.getZhName());
      psnIns.setEnName(Inspsn.getEnName());
      psnIns.setEmail(Inspsn.getEmail());
      psnIns.setFirstName(Inspsn.getFirstName());
      psnIns.setLastName(Inspsn.getLastName());
      psnIns.setSex(Inspsn.getSex());
      psnIns.setMobile(Inspsn.getMobile());
      psnIns.setRegionId(Inspsn.getRegionId());
      psnIns.setUnitId(Inspsn.getUnitId());
      psnIns.setUnitName(Inspsn.getUnitName());

    }
    return psnIns;

  }

  private Object checkEmail(String email, String fromSys) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "11111111oxui3en2");// 系统默认token
    mapDate.put("data", buildEmialCheckParameter(email, fromSys));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  /**
   * 构造调用验证email是否是科研之友账号所需的参数
   * 
   * @param email
   * @param fromSys
   * @return
   */
  private String buildEmialCheckParameter(String email, String fromSys) {
    Map<String, Object> date = new HashMap<String, Object>();
    date.put("fromSys", fromSys);
    date.put("email", email);
    return JacksonUtils.mapToJsonStr(date);
  }

  @SuppressWarnings("rawtypes")
  private void dealRegisterBusi(ImportThirdPsns importThirdPsns) throws ServiceException {
    String avatars;
    Long psnId = null;
    Long insId = importThirdPsns.getPk().getInsId();
    String zhName = importThirdPsns.getZhName();
    String firstName = importThirdPsns.getFirstName();
    String lastName = importThirdPsns.getLastName();
    String email = importThirdPsns.getPk().getEmail();
    String position = importThirdPsns.getPosition();
    Long sieUnitId = importThirdPsns.getSieUnitId();
    String sieUnitName = importThirdPsns.getSieUnitName();
    try {
      // 正常创建人员记录.
      String password = "111111";
      // 调用center-open的人员注册服务
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(
          saveRegisterInfo(insId, zhName, firstName, lastName, null, position, email, password, sieUnitName, "SIE")
              .toString());
      if ("success".equals(resultMap.get("status"))) {
        List result = (List) resultMap.get("result");
        if (result != null && result.size() > 0) {
          Map psnMap = (Map) result.get(0);
          psnId = (Long) psnMap.get("psnId");
          avatars = (String) psnMap.get("avatars");
          SieConstPosition constPosition = sieConstPositionService.getPosByName(position);
          Long posId = null;
          String posGrades = null;
          if (constPosition != null) {
            posId = constPosition.getId();
            posGrades = constPosition.getPosGrades();
          }

          SiePsnIns psnIns = new SiePsnIns();
          psnIns.setZhName(zhName);
          psnIns.setPk(new SiePsnInsPk(psnId, insId));
          psnIns.setEmail(email);
          psnIns.setFirstName(firstName);
          psnIns.setLastName(lastName);
          if (firstName != null && lastName != null) {
            psnIns.setEnName(firstName + " " + lastName);
          }
          psnIns.setPosition(position);
          psnIns.setPosId(posId);
          psnIns.setPosGrades(posGrades);
          psnIns.setAvatars(avatars);
          psnIns.setStatus(1);// 审核通过
          psnIns.setCreateDate(new Date());
          psnIns.setUnitId(sieUnitId);
          psnIns.setUnitName(sieUnitName);
          this.insPsnDao.save(psnIns);
          sieInsPsnLogService.log(0L, psnIns.getPk().getInsId(), psnId, "add", "导入第三方人员信息任务，添加人员;");
        }
      } else {
        logger.error("人员注册接口（3djd2x9s）返回错误结果,insId:{},email:{},zhName:{}", new Object[] {insId, email, zhName});
        throw new ServiceException("人员注册接口（3djd2x9s）返回错误结果：" + resultMap.toString());
      }

    } catch (Exception e) {
      logger.error("处理注册保存人员记录出错,insId:{},email:{},zhName:{}", new Object[] {insId, email, zhName, e});
      throw new ServiceException("处理注册保存人员记录出错", e);
    }
  }

  /**
   * 调用center-open系统的人员注册服务保存注册人员的基本信息
   * 
   * @return
   */

  private Object saveRegisterInfo(Long insId, String name, String firstName, String lastName, String enName,
      String position, String email, String pwd, String unitName, String fromSys) throws ServiceException {
    try {
      Map<String, Object> postData = new HashMap<String, Object>();
      postData.put("openid", 99999999L);// 系统默认openId
      String token = (String) cacheService.get("ins_token", "ins_token" + insId);
      if (StringUtils.isBlank(token)) {
        token = this.getInsToken(insId);
        if (StringUtils.isNotBlank(token)) {
          cacheService.put("ins_token", CacheService.EXP_DAY_1, "ins_token" + insId, token);
        } else {
          token = "11111111";// 系统默认token
        }
      }
      postData.put("token", token + "3djd2x9s");
      Sie6Institution ins = sie6InstitutionDao.get(insId);
      postData.put("data", buildRegisterOpenDataParameter(name, firstName, lastName, enName, position, email, pwd,
          insId, ins == null ? null : ins.getZhName(), unitName, fromSys));
      return restTemplate.postForObject(SERVER_URL, postData, Object.class);
    } catch (Exception e) {
      logger.error("调用人员注册接口保存注册人员出现异常了喔,insId:{},email:{},zhName:{}", new Object[] {insId, email, name, e});
      throw new ServiceException("调用人员注册接口保存注册人员出现异常了喔", e);
    }
  }

  /**
   * 构造调用开放系统所需的参数 密码要经过MD5加密
   * 
   * @param person
   * @return
   */
  private String buildRegisterOpenDataParameter(String name, String firstName, String lastName, String enName,
      String position, String email, String pwd, Long insId, String insName, String unitName, String fromSys) {
    Map<String, Object> date = new HashMap<String, Object>();

    String syncXml = "<root><person><name>" + (StringUtils.isNotBlank(name) ? name : "") + "</name><ename>"
        + (StringUtils.isNotBlank(enName) ? enName : "") + "</ename>" + "<firstName>"
        + (StringUtils.isNotBlank(firstName) ? firstName : "") + "</firstName>" + "<lastName>"
        + (StringUtils.isNotBlank(lastName) ? lastName : "") + "</lastName>" + "<insId>" + (insId == null ? "" : insId)
        + "</insId><email>" + (StringUtils.isNotBlank(email) ? email : "") + "</email><insName>"
        + (StringUtils.isNotBlank(insName) ? insName : "") + "</insName><newpassword>"
        + (StringUtils.isNotBlank(pwd) ? DigestUtils.md5Hex(pwd) : "") + "</newpassword><department>"
        + (StringUtils.isNotBlank(unitName) ? unitName : "") + "</department><position>"
        + (StringUtils.isNotBlank(position) ? position : "") + "</position></person></root>";
    date.put("fromSys", fromSys);
    date.put("syncXml", syncXml);
    return JacksonUtils.mapToJsonStr(date);
  }

  /**
   * 获取单位token.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("rawtypes")
  private String getInsToken(Long insId) throws ServiceException {
    try {
      String token = null;
      Map<String, Object> postData = new HashMap<String, Object>();
      postData.put("openid", 99999999L);// 系统默认openId
      postData.put("token", "11111111yung90kk");// 系统默认token
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("insId", insId);
      postData.put("data", JacksonUtils.mapToJsonStr(data));
      Map<String, Object> resultMap =
          JacksonUtils.jsonToMap(restTemplate.postForObject(SERVER_URL, postData, Object.class).toString());
      if ("success".equals(resultMap.get("status"))) {
        List result = (List) resultMap.get("result");
        Map tokenMap = (Map) result.get(0);
        token = ObjectUtils.toString(tokenMap.get("token"));
      } else {
        logger.error("获取单位token接口（yung90kk）返回错误结果,insId:{}", new Object[] {insId});
        throw new ServiceException("获取单位token接口（yung90kk）返回错误结果：" + resultMap.toString());
      }
      return token;
    } catch (Exception e) {
      logger.error("获取token出现异常了喔,insId={}", insId, e);
      throw new ServiceException("获取token出现异常了喔", e);
    }
  }

}
