package com.smate.center.open.service.data.register;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.center.mail.connector.service.MailHandleOriginalDataService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.consts.ConstPositionDao;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.institution.InstitutionDao;
import com.smate.center.open.dao.profile.PersonEmailDao;
import com.smate.center.open.dao.psn.PdwhAddrPsnUpdateRecordDao;
import com.smate.center.open.dao.psnrefreshinfo.PsnRefreshUserInfoDao;
import com.smate.center.open.dao.rcmd.pub.ImpactGuideRecordDAO;
import com.smate.center.open.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.center.open.exception.EmailExistException;
import com.smate.center.open.exception.OpenSerPersonRegisterException;
import com.smate.center.open.model.consts.ConstPosition;
import com.smate.center.open.model.profile.PersonEmail;
import com.smate.center.open.model.psn.PdwhAddrPsnUpdateRecord;
import com.smate.center.open.model.psnrefresh.PsnRefreshUserInfo;
import com.smate.center.open.model.rcmd.pub.ImpactGuideRecord;
import com.smate.center.open.model.rcmdsync.RcmdSyncPsnInfo;
import com.smate.center.open.model.register.PersonRegister;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.data.autologin.AutoLoginTypeEnum;
import com.smate.center.open.service.open.PersonOpenService;
import com.smate.center.open.service.profile.UserSettingsService;
import com.smate.center.open.service.psnconf.PsnCnfReBuildService;
import com.smate.center.open.service.register.PsnMailSetService;
import com.smate.center.open.service.register.RegisterPersonBaseInfoService;
import com.smate.center.open.service.register.RegisterPersonCasService;
import com.smate.center.open.service.register.RegisterPersonNsfcService;
import com.smate.center.open.service.register.RegisterPersonTaskService;
import com.smate.center.open.service.reschproject.InstitutionManager;
import com.smate.center.open.service.user.SysRolUserService;
import com.smate.center.open.service.user.UserService;
import com.smate.center.open.utils.xml.WebServiceUtils;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.psn.dao.AccountEmailCheckLogDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.AccountEmailCheckLog;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.SysRolUser;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.random.RandomNumber;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 人员注册 服务实现
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class RegisterPersonServiceImpl extends ThirdDataTypeBase {

  // 具体来源系统类型 ： 区分调用什么系统的服务实现 eg ISIS SCM GX....
  private Map<String, RegisterPersonNsfcService> nsfcServiceMap;
  @Autowired
  private RegisterPersonBaseInfoService registerPersonBaseInfoService;
  @Autowired
  private RegisterPersonCasService registerPersonCasService;
  @Autowired
  private RegisterPersonTaskService registerPersonTaskService;
  @Autowired
  private SysRolUserService sysRolUserService;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value(value = "${initOpen.restful.url}")
  private String openRestfulUrl;
  @Autowired
  private PsnCnfReBuildService psnCnfReBuildService;
  @Autowired
  private UserSettingsService userSettingsService;
  @Autowired
  private PersonOpenService personOpenService;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PdwhAddrPsnUpdateRecordDao pdwhAddrPsnUpdateRecordDao;
  /**
   * 更新工作经历 start
   */
  @Autowired
  private InstitutionManager institutionManager;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;


  @Autowired
  private MailHandleOriginalDataService mailHandleOriginalDataService;
  @Autowired
  private UserService userService;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PersonEmailDao personEmailDao;
  @Value("${file.root}")
  private String rootPath;// 文件根路径
  @Autowired
  private AccountEmailCheckLogDao accountEmailCheckLogDao;
  @Autowired
  private PsnMailSetService psnMailSetService;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private ConstPositionDao constPositionDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private ImpactGuideRecordDAO impactGuideRecordDao;

  /**
   * 校验人员注册 具体参数
   */
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验数据参数
    try {
      Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
      if (temp.get(OpenConsts.RESULT_STATUS) != null
          && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
        return temp;
      }
      // 校验 来源系统参数
      Object fromSys = serviceData.get(OpenConsts.MAP_DATA_FROM_SYS);
      if (fromSys == null || "".equals(fromSys.toString())) {
        logger.error("人员注册 来源系统不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_229, paramet, "");
        return temp;
      }
      // 校验人员注册 具体参数
      Object syncXml = serviceData.get(OpenConsts.MAP_SYNCXML);
      if (syncXml == null || "".equals(syncXml.toString())) {
        logger.error("人员注册 人员基本数据不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_230, paramet, "");
        return temp;
      }

      PersonRegister personRegister = WebServiceUtils.toRegisterPerson(syncXml.toString());
      if (personRegister == null) {
        logger.error("人员注册 人员基本数据 构造对象失败");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_231, paramet, "");
        return temp;
      }
      if (StringUtils.isEmpty(personRegister.getName())) {
        logger.error("人员注册 人员基本数据 人员姓名 name不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_232, paramet, "");
        return temp;
      }
      if (StringUtils.isEmpty(personRegister.getEmail())) {
        logger.error("人员注册 人员基本数据 人员邮件 email不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_233, paramet, "");
        return temp;
      }
      if (StringUtils.isNotEmpty(personRegister.getGuid())) {
        // 判断是否已经同步
        SysRolUser sysRolUser = sysRolUserService.getSysRolUserByGuid(personRegister.getGuid());
        if (sysRolUser != null) {
          logger.error("人员注册 guid已经同步过了");
          temp = super.errorMap(OpenMsgCodeConsts.SCM_235, paramet, "");
          return temp;
        }
      }
      paramet.put(OpenConsts.MAP_DATAPATAMETMAP, serviceData);
      // 人员注册信息太长要截断
      truncatePersonRegisterField(personRegister);
      // 邮件名转小写
      personRegister.setEmail(personRegister.getEmail().toLowerCase());
      paramet.put(OpenConsts.MAP_DATA_PERSON_REGISTER, personRegister);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    } catch (Exception e) {
      logger.error("人员注册，参数校验出错", e);
      throw new OpenSerPersonRegisterException("人员注册，参数校验出错", e);
    }
    return temp;
  }

  /**
   * 处理人员注册 数据
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    @SuppressWarnings("unchecked")
    Map<String, Object> serviceData = (Map<String, Object>) paramet.get(OpenConsts.MAP_DATAPATAMETMAP);

    PersonRegister personRegister = (PersonRegister) paramet.get(OpenConsts.MAP_DATA_PERSON_REGISTER);
    // 新添加from_sys字段，标识人员是从哪个系统注册的
    personRegister.setFromSys(serviceData.get(OpenConsts.MAP_DATA_FROM_SYS).toString());
    List<Map<String, Object>> dataList = new ArrayList<>();
    try {
      boolean sendResetPwd = false; // 发送重置密码邮件标识
      if (StringUtils.isBlank(personRegister.getNewpassword())) {
        sendResetPwd = true;
      }
      // 调整注册 有一场孙弱抛出 并且不在支持id+@scholarmate.com的帐号
      // 保存个人基本信息
      registerPersonBaseInfoService.savePersonBaseInfo(personRegister);
      // 保存用户登录权限信息
      registerPersonCasService.saveRegisterSysUser(personRegister);
      // 存储一份用户隐私配置
      userSettingsService.initPrivacySettingsConfig(personRegister.getPersonId());
      // 初始化默认的权限配置
      psnCnfReBuildService.init(personRegister.getPersonId());
      // 保存用户后台任务数据处理信息
      registerPersonTaskService.saveTaskJob(personRegister);
      // 生成个人主页短地址
      produceShortUrl(personRegister.getPersonId());
      // 手机注册
      if (!"0".equals(personRegister.getMobileReg())) {
        registerPersonBaseInfoService.saveMobileRegisgerInfo(personRegister);
      }
      // 默认初始化用户所有的邮件接收设置
      psnMailSetService.iniPsnMailSet(personRegister.getPersonId());
      // 初始化统计表信息
      buildPsnStatistics(personRegister.getPersonId());
      // 处理首要邮箱
      this.emailSave(personRegister);
      // 更新工作经历
      this.refreshPsnInsInfo(personRegister);
      // 初始化影响力引导指引记录(刚注册不弹出影响力框)
      this.initImpactGuide(personRegister.getPersonId());
      // 判断处理 其他系统同步帐号
      RegisterPersonNsfcService registerPersonNsfcService =
          nsfcServiceMap.get(serviceData.get(OpenConsts.MAP_DATA_FROM_SYS));
      if (registerPersonNsfcService != null) {
        registerPersonNsfcService.handleNsfcData(personRegister);
      }
      // 保存产生一个openid 并且 关联上sns与sie tsz
      Long openId = unionOpenId(paramet, personRegister);
      // 发送重置密码的邮件
      if (sendResetPwd) {
        try {
          sendResetPasswordEmail(personRegister, openId);
        } catch (Exception e) {
          logger.error("第三方系统，注册帐号发送重置密码邮件异常:psnId=" + personRegister.getPersonId(), e);
        }
      }
      // 科研之友的用户要发邮箱验证
      if ("sns".equalsIgnoreCase(personRegister.getFromSys())) {
        // 发送一封账号邮箱验证的邮件
        sendAccountEmailValidateEmail(personRegister);
      }
      paramet.put(OpenConsts.MAP_OPENID, openId);
      Map<String, Object> dataMap = new HashMap<>();
      if (OpenConsts.FROM_SIE.equalsIgnoreCase(serviceData.get(OpenConsts.MAP_DATA_FROM_SYS).toString())) {
        // sie
        dataMap.put(OpenConsts.MAP_PSNID, personRegister.getPersonId());
        dataMap.put(OpenConsts.MAP_AVATARS, personRegister.getAvatars());
        dataList.add(dataMap);
      }else{
        dataMap.put(OpenConsts.MAP_PSNID, personRegister.getPersonId());
        dataList.add(dataMap);
      }
      return super.successMap("人员注册成功", dataList);
    } catch (EmailExistException e) {
      throw new EmailExistException("注册失败,帐号已经存在");
      // return super.successMap("注册失败,帐号已经存在", dataList);
    } catch (Exception e) {
      logger.error("人员注册，初始化数据处理出错 " + personRegister.toString(), e);
      throw new OpenSerPersonRegisterException("人员注册，初始化数据处理出错", e);
    }
  }

  /**
   * 更新人员单位信息
   * 
   * @param psn
   * @param psnInfo
   */
  private WorkHistory refreshPsnInsInfo(PersonRegister psn) {
    WorkHistory work = null;
    Long insId = psn.getInsId();
    String insName = psn.getInsName();
    if ((insId == null || insId == 0L) && StringUtils.isNoneBlank(insName)) {
      insId = institutionDao.getInsIdByName(insName, insName);
    }
    if (StringUtils.isBlank(insName) && insId != null) {
      Institution institution = institutionDao.findById(insId);
      insName = institution.getName();
    }
    if (StringUtils.isNotBlank(insName)) {
      Long psnId = psn.getPersonId();

      String unitName = StringUtils.substring(Objects.toString(psn.getUnit(), ""), 0, 600).trim();
      String position = StringUtils.substring(Objects.toString(psn.getPosition(), ""), 0, 200).trim();
      work = workHistoryDao.getWorkHistoryByInsIdOrInsInfo(psnId, insId, insName, unitName);
      if (work == null) {
        work = new WorkHistory();
        work.setInsName(insName);
        work.setPsnId(psnId);
      }
      // 部门
      if (StringUtils.isNotBlank(unitName)) {
        work.setDepartment(unitName);
      }
      // 单位ID
      if (insId != null && insId > 0) {
        work.setInsId(insId);
      }
      // 职称
      if (StringUtils.isNotBlank(position)) {
        // 职称ID
        work.setPosition(position);
        ConstPosition posConst = constPositionDao.getPosByName(position);
        if (posConst != null) {
          work.setPosId(posConst.getId());
          work.setPosGrades(posConst.getGrades());
        } else {
          work.setPosId(null);
          work.setPosGrades(null);
        }
      }
      // 若不存在首要单位，则将该工作经历设置为首要单位
      if (!workHistoryDao.isExitPrmIns(psnId)) {
        work.setIsPrimary(1L);
      }
      workHistoryDao.save(work);
      logger.info("从SIE处同步信息--------------处理人员单位信息完成");
    }
    return work;
  }

  /**
   * 创建账号的时候产生 openid并且 关联默认关联上sns 与sie 以及注册来源系统
   * 
   * @param paramet
   * @param personRegister
   * @return
   * @throws Exception
   */
  private Long unionOpenId(Map<String, Object> paramet, PersonRegister personRegister) throws Exception {
    Long openId = personOpenService.createOpenId(personRegister.getPersonId());
    if (openId != null) {
      OpenUserUnion openUserUnion = new OpenUserUnion();
      openUserUnion.setOpenId(openId);
      openUserUnion.setPsnId(personRegister.getPersonId());
      openUserUnion.setToken(OpenConsts.SMATE_TOKEN);
      openUserUnion.setCreateDate(new Date());
      openUserUnion.setCreateType(OpenConsts.OPENID_CREATE_TYPE_2);
      openUserUnionDao.saveOpenUserUnion(openUserUnion);

      OpenUserUnion openUserUnion1 = new OpenUserUnion();
      openUserUnion1.setOpenId(openId);
      openUserUnion1.setPsnId(personRegister.getPersonId());
      openUserUnion1.setToken(OpenConsts.SIE_TOKEN);
      openUserUnion1.setCreateDate(new Date());
      openUserUnion1.setCreateType(OpenConsts.OPENID_CREATE_TYPE_2);
      openUserUnionDao.saveOpenUserUnion(openUserUnion1);

      if (!OpenConsts.SMATE_TOKEN.equals(paramet.get(OpenConsts.MAP_TOKEN).toString())
          && !OpenConsts.SIE_TOKEN.equals(paramet.get(OpenConsts.MAP_TOKEN).toString())) {
        OpenUserUnion openUserUnion2 = new OpenUserUnion();
        openUserUnion2.setOpenId(openId);
        openUserUnion2.setPsnId(personRegister.getPersonId());
        openUserUnion2.setToken(paramet.get(OpenConsts.MAP_TOKEN).toString());
        openUserUnion2.setCreateDate(new Date());
        openUserUnion2.setCreateType(OpenConsts.OPENID_CREATE_TYPE_2);
        openUserUnionDao.saveOpenUserUnion(openUserUnion2);
      }
    }
    return openId;
  }


  private WorkHistory initWorkHistory(PersonRegister personRegister) {
    WorkHistory workHistory = new WorkHistory();
    workHistory.setPsnId(personRegister.getPersonId());
    workHistory.setInsName(personRegister.getInsName());
    // 查找单位ID
    if (workHistory.getInsId() == null) {
      Long insId = null;
      try {
        insId = institutionManager.getInsIdByName(workHistory.getInsName(), workHistory.getInsName());
      } catch (Exception e) {
        logger.error("根据机构名获取insId出错", e);
      }
      workHistory.setInsId(insId);
    }
    // 初始化值
    workHistory.setPosition(null);
    workHistory.setPosId(null);
    workHistory.setPosGrades(null);
    // 新账号，有机构单位就设置为首要单位
    workHistory.setIsPrimary(1L);
    return workHistory;
  }

  private void savaPsnInfoSyncAndRefresh(Long psnId) {
    RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
    if (rsp == null) {
      rsp = new RcmdSyncPsnInfo(psnId);
    }
    rsp.setWorkFlag(1);
    rsp.setInsFlag(1);
    rsp.setExperienceFlag(1);
    rcmdSyncPsnInfoDao.save(rsp);
    PsnRefreshUserInfo psnRefInfo = psnRefreshUserInfoDao.get(psnId);
    if (psnRefInfo == null) {
      psnRefInfo = new PsnRefreshUserInfo(psnId);
    }
    psnRefInfo.setIns(1);
    psnRefInfo.setDegree(1);
    psnRefInfo.setPosition(1);
    psnRefreshUserInfoDao.save(psnRefInfo);
  }

  private void savaPersonInfo(WorkHistory workHistory, Institution ins, PersonRegister personRegister) {
    Person psn = personProfileDao.get(workHistory.getPsnId());
    if (psn == null) {
      psn = new Person();
      psn.setPersonId(workHistory.getPsnId());
      psn.setRegData(new Date());
    }
    psn.setInsName(workHistory.getInsName());
    psn.setName(personRegister.getName());
    psn.setEmail(personRegister.getEmail());
    psn.setInsId(workHistory.getInsId());
    if (ins != null) {
      psn.setRegionId(ins.getRegionId());
    }
    personProfileDao.save(psn);
  }

  private void initImpactGuide(Long psnId) {
    ImpactGuideRecord impact = impactGuideRecordDao.findRecordByPsnId(psnId);
    if (impact == null) {
      impact = new ImpactGuideRecord();
      impact.setGmtCreate(new Date());
    }
    impact.setPsnId(psnId);
    impact.setStatus(1);
    impactGuideRecordDao.saveOrUpdate(impact);
  }

  private void buildPsnStatistics(Long psnId) {
    PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
    if (psnStatistics == null) {
      psnStatistics = new PsnStatistics();
    }
    psnStatistics.setPsnId(psnId);
    // 属性为null的保存为0
    buildZero(psnStatistics);
    psnStatisticsDao.save(psnStatistics);
  }

  private void buildZero(PsnStatistics p) {
    // 1.成果总数
    if (p.getPubSum() == null) {
      p.setPubSum(0);
    }
    // 2.成果引用次数总数
    if (p.getCitedSum() == null) {
      p.setCitedSum(0);
    }
    // 3.hindex指数
    if (p.getHindex() == null) {
      p.setHindex(0);
    }
    // 4.中文成果数
    if (p.getZhSum() == null) {
      p.setZhSum(0);
    }
    // 5.英文成果数
    if (p.getEnSum() == null) {
      p.setEnSum(0);
    }
    // 6.项目总数
    if (p.getPrjSum() == null) {
      p.setPrjSum(0);
    }
    // 7.好友总数
    if (p.getFrdSum() == null) {
      p.setFrdSum(0);
    }
    // 8.群组总数
    if (p.getGroupSum() == null) {
      p.setGroupSum(0);
    }
    // 9.成果被赞的总数
    if (p.getPubAwardSum() == null) {
      p.setPubAwardSum(0);
    }
    // 10.专利数
    if (p.getPatentSum() == null) {
      p.setPatentSum(0);
    }
    // 11.待认领成果数
    if (p.getPcfPubSum() == null) {
      p.setPcfPubSum(0);
    }
    // 12.成果全文推荐数
    if (p.getPubFullTextSum() == null) {
      p.setPubFullTextSum(0);
    }
    // 13.公开成果总数
    if (p.getOpenPubSum() == null) {
      p.setOpenPubSum(0);
    }
    // 14.公开项目总数
    if (p.getOpenPrjSum() == null) {
      p.setOpenPrjSum(0);
    }
    // 15.访问总数
    if (p.getVisitSum() == null) {
      p.setVisitSum(0);
    }
  }

  public void setNsfcServiceMap(Map<String, RegisterPersonNsfcService> nsfcServiceMap) {
    this.nsfcServiceMap = nsfcServiceMap;
  }

  private void truncatePersonRegisterField(PersonRegister personRegister) {

    personRegister.setName(truncateString(personRegister.getName(), 61));
    personRegister.setInsName(truncateString(personRegister.getInsName(), 300));
    personRegister.setPosition(truncateString(personRegister.getPosition(), 300));
    personRegister.setDegreeName(truncateString(personRegister.getDegreeName(), 50));
  }

  /**
   * 截断字符
   * 
   * @param source
   * @param length
   * @return
   */
  public String truncateString(String source, Integer length) {
    if (StringUtils.isNotBlank(source) && length > 0 && source.length() > length) {
      return source.substring(0, length);
    }
    return source;
  }

  private String produceShortUrl(Long psnId) {
    String shortUrl = "";
    Map<String, Object> map = new HashMap<>();
    Map<String, Object> dataMap = new HashMap<>();
    Map<String, Object> shortUrlParametMap = new HashMap<>();

    map.put("openid", "99999999");
    map.put("token", "00000000sht22url");

    dataMap.put("createPsnId", "0");
    dataMap.put("type", "P");
    shortUrlParametMap.put("des3PsnId", ServiceUtil.encodeToDes3(psnId.toString()));
    dataMap.put("shortUrlParamet", JacksonUtils.mapToJsonStr(shortUrlParametMap));
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));

    Object obj = restTemplate.postForObject(openRestfulUrl, map, Object.class);
    Map<String, Object> objMap = JacksonUtils.jsonToMap(obj.toString());
    if (objMap.get("result") != null) {
      List<Map<String, Object>> list = (List<Map<String, Object>>) objMap.get("result");
      if (list != null && list.size() > 0 && list.get(0).get("shortUrl") != null) {
        shortUrl = list.get(0).get("shortUrl").toString();
      }
    }

    PsnProfileUrl psnProfileUrl;
    try {
      psnProfileUrl = psnProfileUrlDao.find(psnId);
      if (psnProfileUrl == null) {
        psnProfileUrl = new PsnProfileUrl();
        psnProfileUrl.setPsnId(psnId);
      }
      psnProfileUrl.setPsnIndexUrl(shortUrl);
      psnProfileUrl.setUpdateDate(new Date());
      psnProfileUrlDao.save(psnProfileUrl);
    } catch (Exception e) {
      logger.error("注册人员生成短地址异常：psnId=" + psnId, e.toString());
      throw e;
    }
    return shortUrl;
  }

  /**
   * 注册时保存用户的邮件信息，默认将该邮件设为首要。 邮件唯一.
   * 
   * @param person
   */
  private void emailSave(PersonRegister person) throws Exception {
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
        personEmailDao.save(email);
        pdwhAddrPsnUpdateRecordDao.deleteRecordByPsnId(person.getPersonId());
        PdwhAddrPsnUpdateRecord record = new PdwhAddrPsnUpdateRecord(person.getPersonId(), 2, 0);
        pdwhAddrPsnUpdateRecordDao.save(record);
      }
    } catch (Exception e) {

      logger.error("注册时保存用户邮件信息出错", e);
    }
  }

  public void sendResetPasswordEmail(PersonRegister person, Long openId) throws Exception {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    String email = person.getEmail();
    Long psnId = person.getPersonId();
    Integer templateCode = 10104;
    String msg = "调用接口注册帐号，重置密码邮件";
    mailHandleOriginalDataService.buildNecessaryParam(email, currentUserId, psnId, templateCode, msg, paramData);

    String AID = buildAid(person, openId);
    String des3email = Des3Utils.encodeToDes3(email);
    String reset_password_url =
        this.domainscm + "/psnweb/homepage/show?AID=" + AID + "&needresetpwd=true&from=email&email=" + des3email;
    String psnname = getPsnName(person);
    // ---------------------------------如果有链接参数-start---------------------------------
    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();
    // 获取链接过期时间

    // 构造跟踪链接需要的属性
    MailLinkInfo linkInfo = new MailLinkInfo();
    linkInfo.setKey("reset_password_url");// 链接的标识，保持唯一
    linkInfo.setUrl(reset_password_url);// 真实的链接
    linkInfo.setLimitCount(0);// 链接的限制访问次数，0=不限制 可以不设置
    linkInfo.setTimeOutDate(null);// 链接的过期时间设置，null=不限制 可以不设置
    linkInfo.setUrlDesc("注册账号，重置密码邮件");// 链接描述 尽量设置 利于统计链接跟踪
    linkList.add(JacksonUtils.jsonObjectSerializer(linkInfo));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    // ---------------------------------如果有链接参数-end---------------------------------

    // ---------------------------------如果有主题参数-start---------------------------------
    // 主题参数，添加如下：
    List<String> subjectParamLinkList = new ArrayList<String>();
    subjectParamLinkList.add("科研之友–重置密码");
    mailData.put("subjectParamList", JacksonUtils.listToJsonStr(subjectParamLinkList));
    // ---------------------------------如果有主题参数-end---------------------------------

    mailData.put("psnname", psnname);

    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    // 调用服务
    Map<String, String> resutlMap = mailHandleOriginalDataService.doHandle(paramData);
    if ("success".equals(resutlMap.get("result"))) {
      // 构造邮件成功
    } else {
      // 构造邮件失败
      logger.error(resutlMap.get("msg"));
    }
  }

  /**
   * 构建aid
   * 
   * @param person
   * @param openId
   * @return
   */
  private String buildAid(PersonRegister person, Long openId) throws Exception {
    String autoLoginType = person.getFromSys();
    if (StringUtils.isBlank(autoLoginType)) {
      autoLoginType = "sns";
    }
    long overTimeMill = AutoLoginTypeEnum.valueOf("SNSRememberMe").toLong();
    Date now = new Date();
    Date overTime = new Date(now.getTime() + overTimeMill);
    String token = "00000000";
    String aid = userService.getAID(openId, autoLoginType, overTime, person.getPersonId(), token);
    return aid;
  }

  public String getPsnName(PersonRegister person) {
    if (StringUtils.isNotBlank(person.getName())) {
      return person.getName();
    } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
      return person.getFirstName() + " " + person.getLastName();
    } else {
      return person.getEname();
    }
  }

  /**
   * 发送账户邮箱验证邮件
   * 
   * @param form
   */
  public Boolean sendAccountEmailValidateEmail(PersonRegister personRegister) {

    // 保存记录
    AccountEmailCheckLog accountEmailCheckLog =
        saveAccountEmailCheckLog(personRegister.getPersonId(), personRegister.getEmail());
    try {
      // 调用接口发送邮件 新的邮件方式
      restTemplateSendMail(personRegister, accountEmailCheckLog);
      // 发送成功，保存记录
      return true;
    } catch (Exception e) {
      logger.error("构建账号邮件，确认邮件异常+psnId=" + personRegister.getPersonId(), e);
    }
    return false;
  }

  private void restTemplateSendMail(PersonRegister personRegister, AccountEmailCheckLog accountEmailCheckLog)
      throws Exception {
    if (personRegister == null) {
      throw new Exception("构建账户邮件确认邮件，邮件对象为空" + this.getClass().getName());
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, String> mailData = new HashMap<String, String>();
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    String language = "";
    language = personRegister.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    Integer templateCode = 10073;
    info.setReceiver(personRegister.getEmail());// 接收邮箱
    info.setSenderPsnId(0L);// 发送人psnId，0=系统邮件
    info.setReceiverPsnId(personRegister.getPersonId());// 接收人psnId，0=非科研之友用户
    info.setMailTemplateCode(templateCode);// 模版标识，参考V_MAIL_TEMPLATE
    info.setMsg("确认邮件地址");// 描述
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializer(info));
    String confirmUrl = this.domainscm + "/psnweb/accountvalidate/ajaxdovalidte?" + "des3Id="
        + Des3Utils.encodeToDes3(accountEmailCheckLog.getId().toString());
    // 定义要跟踪的链接集，系统会生成短地址并跟踪访问记录
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友主页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("confirmUrl");
    l2.setUrl(confirmUrl);
    l2.setUrlDesc("确认地址");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    mailData.put("psnName", getPersonName(personRegister, language));
    mailData.put("confirmCode", accountEmailCheckLog.getValidateCode());
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW,
      rollbackForClassName = {"java.lang.Exception"})
  private AccountEmailCheckLog saveAccountEmailCheckLog(Long psnId, String email) {
    // 保存记录
    AccountEmailCheckLog accountEmailCheckLog = new AccountEmailCheckLog();
    accountEmailCheckLog.setPsnId(psnId);
    accountEmailCheckLog.setAccount(email);
    accountEmailCheckLog.setDealStatus(0);
    accountEmailCheckLog.setSendTime(new Date());
    String validateCode = RandomNumber.getRandomNumber(6).toString();
    accountEmailCheckLog.setValidateCode(validateCode);
    String randomStr = UUID.randomUUID().toString() + psnId + RandomNumber.getRandomStr(32);
    String validateCodeBig = DigestUtils.md5Hex(randomStr);
    accountEmailCheckLog.setValidateCodeBig(validateCodeBig);
    // 发送成功，保存记录
    accountEmailCheckLogDao.save(accountEmailCheckLog);
    return accountEmailCheckLog;
  }

  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  public static String getPersonName(PersonRegister person, String language) {
    if ("en".equalsIgnoreCase(language) || "en_US".equalsIgnoreCase(language)) {
      if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getName();
      }
    } else {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getEname();
      }

    }
  }

}
