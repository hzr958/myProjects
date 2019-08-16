package com.smate.center.open.service.data.psnInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.consts.ConstPositionDao;
import com.smate.center.open.dao.profile.PersonEmailDao;
import com.smate.center.open.dao.profile.PersonKnowDao;
import com.smate.center.open.dao.psn.PdwhAddrPsnUpdateRecordDao;
import com.smate.center.open.dao.psnrefreshinfo.PsnRefreshUserInfoDao;
import com.smate.center.open.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.center.open.dao.register.PersonRegisterDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSyncPsnException;
import com.smate.center.open.model.consts.ConstPosition;
import com.smate.center.open.model.profile.PersonEmail;
import com.smate.center.open.model.psn.PdwhAddrPsnUpdateRecord;
import com.smate.center.open.model.psn.PersonKnow;
import com.smate.center.open.model.psnrefresh.PsnRefreshUserInfo;
import com.smate.center.open.model.rcmdsync.RcmdSyncPsnInfo;
import com.smate.center.open.model.register.PersonRegister;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.profile.PersonManager;
import com.smate.center.open.service.user.UserService;
import com.smate.core.base.email.service.MailInitDataService;
import com.smate.core.base.psn.dao.AccountEmailCheckLogDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.AccountEmailCheckLog;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.common.EditValidateUtils;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.dao.consts.ConstDictionaryDao;
import com.smate.core.base.utils.dao.security.SysUserLoginDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.SysUserLogin;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.person.avatars.PersonAvatarsUtils;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.random.RandomNumber;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 根据SIE传的人员信息更新SNS库对应的人员信息 syncfsie
 * 
 * @author wsn
 * @date 2018年7月16日
 */
public class SyncPsnFromSieServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private static final String REFRESH_OPERATE_TYPE = "refreshSolrInfo";
  @Value("${domainscm}")
  private String domainscm;
  @Value("${file.root}")
  private String rootPath;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Autowired
  private BatchJobsService batchJobsService;
  @Autowired
  private PdwhAddrPsnUpdateRecordDao pdwhPubMacthTaskRecordDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PersonKnowDao personKnowDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private UserDao userDao;
  @Autowired
  private UserService userService;
  @Autowired
  private MailInitDataService mailInitDataService;
  @Autowired
  private AccountEmailCheckLogDao accountEmailCheckLogDao;
  @Autowired
  private SysUserLoginDao sysUserLoginDao;
  @Autowired
  private PersonRegisterDao personRegisterDao;
  @Autowired
  private ConstPositionDao constPositionDao;
  @Autowired
  private ConstDictionaryDao constDictionaryDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验数据参数
    try {
      Object data = paramet.get(OpenConsts.MAP_DATA);
      Map<String, Object> serviceData = JacksonUtils.jsonToMap(data.toString());
      // 校验人员同步 具体参数
      Object psnData = serviceData.get(OpenConsts.PSN_DATA);
      if (psnData == null || "".equals(psnData.toString())) {
        logger.error("人员同步 人员基本数据不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_237, paramet, "");
        return temp;
      }
      Map<String, String> psnInfoMap = (Map<String, String>) psnData;
      // 校验是否有人员ID
      boolean psnIsNull = StringUtils.isBlank(Objects.toString(paramet.get("psnId"), ""))
          && StringUtils.isBlank(psnInfoMap.get("psnId"));
      if (psnIsNull) {
        logger.error("缺少人员ID参数");
        temp = super.errorMap("缺少人员ID参数", paramet, "");
        return temp;
      }
      paramet.put(OpenConsts.MAP_DATAPATAMETMAP, serviceData);
      paramet.put(OpenConsts.MAP_DATA_PERSON_SYSCH, psnInfoMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    } catch (Exception e) {
      logger.error("从SIE获取人员信息进行人员同步，参数校验出错", e);
      throw new OpenSyncPsnException("从SIE获取人员信息进行人员同步，参数校验出错", e);
    }
    return temp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      // 获取人员ID
      Map<String, String> psnInfoMap = (Map<String, String>) paramet.get(OpenConsts.MAP_DATA_PERSON_SYSCH);
      String psnIdStr = StringUtils.isNotBlank(psnInfoMap.get("psnId")) ? psnInfoMap.get("psnId")
          : Objects.toString(paramet.get("psnId"), "0");
      Long psnId = NumberUtils.toLong(psnIdStr.trim());
      // 处理人员姓名
      Person psn = personProfileDao.get(psnId);
      if (psn != null) {
        this.refreshPsnName(psn, psnInfoMap.get("name"), psnInfoMap.get("zhFirstName"), psnInfoMap.get("zhLastName"),
            psnInfoMap.get("firstName"), psnInfoMap.get("lastName"), psnInfoMap);
        // 与首要单位一致的就更新首要单位信息，不一致的就新增一条工作经历
        WorkHistory work = this.refreshPsnInsInfo(psn, psnInfoMap);
        // 更新人员邮件信息----暂时SIE改不了Email，所以暂时不处理email
        // refreshPsnEmail(psn, psnInfoMap);
        // 更新人员其他字段信息
        this.refreshOtherInfo(psn, psnInfoMap, work);
        // 更新人员信息完整度
        personManager.refreshComplete(psnId);
        // 更新personKnow表
        this.syncSavePersonKnow(psn);
        // 标记信息变更
        initSyncFlag(psn, psnInfoMap);
      }
      logger.info("从SIE处同步信息--------------处理完成, data=" + paramet.get(OpenConsts.MAP_DATA).toString());
    } catch (Exception e) {
      logger.error("从SIE获取人员信息进行人员同步出错, data = " + paramet.get(OpenConsts.MAP_DATA).toString(), e);
      throw new OpenException(e);
    }
    Map<String, Object> dataMap = new HashMap<String, Object>();
    return successMap(OpenMsgCodeConsts.SCM_000, dataMap);
  }

  /**
   * 处理人员姓名信息
   * 
   * @param psn
   * @param name
   * @param zhFirstName
   * @param zhLastName
   * @param firstName
   * @param lastName
   * @return
   * @throws OpenException
   */
  private String refreshPsnName(Person psn, String name, String zhFirstName, String zhLastName, String firstName,
      String lastName, Map<String, String> psnInfoMap) throws OpenException {
    String result = "skip";
    try {
      boolean needRefreshFirstName = StringUtils.isNotBlank(firstName);
      boolean needRefreshLastName = StringUtils.isNotBlank(lastName);
      boolean needRefreshName = StringUtils.isNotBlank(name);
      Long psnId = psn.getPersonId();
      // 更新中文名
      if (needRefreshName) {
        name = StringUtils.substring(name, 0, 61).trim();
        psn.setName(name);
        Map<String, String> splitName = ServiceUtil.parseZhfirstAndLastNew(name);
        psn.setZhFirstName(splitName.get("firstNameZh"));
        psn.setZhLastName(splitName.get("lastNameZh"));
        psnInfoMap.put("nameChanged", "true");
      }
      // 更新姓和名
      if (needRefreshFirstName || needRefreshLastName) {
        firstName = needRefreshFirstName ? StringUtils.substring(firstName, 0, 50).trim() : psn.getFirstName();
        lastName = needRefreshLastName ? StringUtils.substring(lastName, 0, 49).trim() : psn.getLastName();
        psn.setEname(firstName + " " + lastName);
        // 根据firstname lastname 更新默认头像
        String oldFirstName = psn.getFirstName();
        String oldLastName = psn.getLastName();
        psn.setFirstName(firstName);
        psn.setLastName(lastName);
        if ((StringUtils.isBlank(oldFirstName) || !oldFirstName.equals(firstName))
            || (StringUtils.isBlank(oldLastName) || !oldLastName.equals(lastName))) {
          rebuildAvatars(psn);
          psnInfoMap.put("nameChanged", "true");
        }
      }
      personProfileDao.save(psn);
      // 人员姓名其他处理
      this.otherDealOfPsnName(psnId, needRefreshFirstName, needRefreshLastName, needRefreshName);
      result = "success";
      logger.info("从SIE处同步信息--------------处理人员姓名完成");
    } catch (Exception e) {
      logger.error("保存人员姓名出错， psnId=" + ", name=", e);
      throw new OpenException(e);
    }
    return result;
  }

  /**
   * 人员姓名其他处理
   * 
   * @param psnId
   * @throws CreateBatchJobException
   */
  private void otherDealOfPsnName(Long psnId, boolean needRefreshFirstName, boolean needRefreshLastName,
      boolean needRefreshName) throws CreateBatchJobException {
    if (needRefreshFirstName || needRefreshLastName || needRefreshName) {
      // SCM-13851同步人员姓名
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.PSN_AVATARS_SYNC,
          BatchJobUtil.getContext(psnId), BatchWeightEnum.B.toString());
      batchJobsService.saveJob(job);
      // SCM-17073 更新姓名常量
      pdwhPubMacthTaskRecordDao.deleteRecordByPsnId(psnId);
      PdwhAddrPsnUpdateRecord record = new PdwhAddrPsnUpdateRecord(psnId, 2, 0);
      pdwhPubMacthTaskRecordDao.save(record);
    }
  }

  /**
   * 重新生成默认头像
   *
   * @param psn
   */
  private void rebuildAvatars(Person psn) {
    if (psn.getAvatars().indexOf("A=D") > 0 || psn.getAvatars().indexOf("logo_psndefault.png") > 0
        || psn.getAvatars().indexOf("head_nan_photo.jpg") > 0 || psn.getAvatars().indexOf("head_nv_photo.jpg") > 0) { // 是默认地址
      // 默认地址 重新生成地址
      if (StringUtils.isNotBlank(psn.getFirstName()) || StringUtils.isNotBlank(psn.getLastName())) {
        String a = psn.getFirstName() != null ? psn.getFirstName().substring(0, 1).toUpperCase() : "";
        String b = psn.getLastName() != null ? psn.getLastName().substring(0, 1).toUpperCase() : "";
        try {
          String avatars = PersonAvatarsUtils.personAvatars(b + a, psn.getPersonId(), rootPath + "/avatars");
          psn.setAvatars(domainscm + "/avatars" + avatars);
        } catch (Exception e) {
          logger.error("生成默认头像失败!!", e);
        }
      }
    }
  }

  /**
   * 人员推荐用
   * 
   * @param person
   */
  private void syncSavePersonKnow(Person person) {
    if (person == null || person.getPersonId() == null)
      return;
    PersonKnow personKnow = new PersonKnow();
    personKnow.setPersonId(person.getPersonId());
    personKnow.setComplete(person.getComplete() == null ? 0 : person.getComplete());
    personKnow.setIsAdd(person.getIsAdd() == null ? 0 : person.getIsAdd());
    personKnow.setIsPrivate(person.getIsPrivate() == null ? 0 : person.getIsPrivate());
    personKnow.setIsLogin(person.getIsLogin() == null ? 0 : person.getIsLogin());
    personKnow.setIsAddFrd(0);
    personKnowDao.save(personKnow);
    logger.info("从SIE处同步信息--------------处理人员推荐冗余表信息完成");
  }

  /**
   * 更新人员其他字段信息
   * 
   * @param psn
   * @param psnInfo
   */
  private void refreshOtherInfo(Person psn, Map<String, String> psnInfo, WorkHistory work) {
    // 性别
    if (StringUtils.isNotBlank(psnInfo.get("sex"))) {
      psn.setSex(NumberUtils.toInt(StringUtils.substring(psnInfo.get("sex").trim(), 0, 1)));
    }
    // 电话
    if (StringUtils.isNotBlank(psnInfo.get("tel"))) {
      psn.setTel(StringUtils.substring(psnInfo.get("tel").trim(), 0, 50));
      psnInfo.put("contectChanged", "true");
    }
    // 所在地区
    if (StringUtils.isNotBlank(Objects.toString(psnInfo.get("regionId")))) {
      Long regionId = NumberUtils.toLong(Objects.toString(psnInfo.get("regionId")).trim());
      if (NumberUtils.isNotNullOrZero(regionId)) {
        psn.setRegionId(regionId);
        psnInfo.put("areaChanged", "true");
      }
    }
    // 最高学历
    if (StringUtils.isNotBlank(psnInfo.get("degree"))) {
      String degreeCode = psnInfo.get("degree");
      psn.setDegree(degreeCode);
      ConstDictionary constDictionary = constDictionaryDao.findConstByCategoryAndCode("psn_degree", degreeCode);
      if (constDictionary != null) {
        psn.setDegreeName(constDictionary.getZhCnName());
      }
      psnInfo.put("degreeChanged", "true");
    }
    // 若更新了人员首要工作经历信息，则person表对应的单位信息也要更新
    if (work != null && CommonUtils.compareLongValue(work.getIsPrimary(), 1L)) {
      psn.setInsId(work.getInsId());
      psn.setInsName(work.getInsName());
      psn.setDepartment(work.getDepartment());
      psn.setPosition(work.getPosition());
      psn.setPosId(work.getPosId());
      psn.setPosGrades(work.getPosGrades());
      psnInfo.put("workChanged", "true");
    }
    logger.info("从SIE处同步信息--------------处理人员其他字段信息完成");
  }

  /**
   * 更新人员单位信息
   * 
   * @param psn
   * @param psnInfo
   */
  private WorkHistory refreshPsnInsInfo(Person psn, Map<String, String> psnInfo) {
    WorkHistory work = null;
    if (StringUtils.isNotBlank(psnInfo.get("insName"))) {
      Long insId = 0L;
      Long psnId = psn.getPersonId();
      if (StringUtils.isNotBlank(psnInfo.get("insId"))) {
        insId = NumberUtils.toLong(psnInfo.get("insId"));
      }
      String insName = StringUtils.substring(Objects.toString(psnInfo.get("insName"), ""), 0, 200).trim();
      String unitName = StringUtils.substring(Objects.toString(psnInfo.get("unitName"), ""), 0, 600).trim();
      //String position = StringUtils.substring(Objects.toString(psnInfo.get("position"), ""), 0, 200).trim();
      String position = ""; //SCM-26566  2019-07-23
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
      if (insId > 0) {
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
   * 更新一些同步标记
   * 
   * @throws CreateBatchJobException
   */
  private void initSyncFlag(Person psn, Map<String, String> psnInfo) throws CreateBatchJobException {
    Long psnId = psn.getPersonId();
    boolean needSave = "true".equals(psnInfo.get("nameChanged")) || "true".equals(psnInfo.get("emailChanged"))
        || "true".equals(psnInfo.get("workChanged")) || "true".equals(psnInfo.get("contectChanged"));
    if (needSave) {
      // 标记人员信息有刷新
      PsnRefreshUserInfo refInfo = new PsnRefreshUserInfo(psnId);
      // 推荐系统同步人员信息标记
      RcmdSyncPsnInfo rsp = new RcmdSyncPsnInfo(psnId);
      // 姓名标记
      if ("true".equals(psnInfo.get("nameChanged"))) {
        refInfo.setNameFlag(1);
        rsp.setNameFlag(1);
      }
      // email和联系方式
      if ("true".equals(psnInfo.get("emailChanged"))) {
        refInfo.setEmailFlag(1);
        rsp.setEmailFlag(1);
      }
      if ("true".equals(psnInfo.get("contectChanged"))) {
        rsp.setContactFlag(1);
      }
      // 工作信息
      if ("true".equals(psnInfo.get("workChanged"))) {
        refInfo.setIns(1);
        refInfo.setPosition(1);
        rsp.setWorkFlag(1);
        rsp.setExperienceFlag(1);
        rsp.setInsFlag(1);
        rsp.setWorkFlag(1);
      }
      // 最高学历
      if ("true".equals(psnInfo.get("degreeChanged"))) {
        refInfo.setDegree(1);
      }
      rcmdSyncPsnInfoDao.save(rsp);
      psnRefreshUserInfoDao.save(refInfo);
      // 更新solr个人信息
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.REFRESH_PSN_SOLR_INFO,
          BatchJobUtil.getPsnSolrRefreshContext(psnId.toString(), REFRESH_OPERATE_TYPE), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    }
    logger.info("从SIE处同步信息--------------标记信息更新完成, needSave=" + needSave);
  }

  /**
   * 同步更新人员邮件信息
   * 
   * @throws Exception
   * 
   */
  private void refreshPsnEmail(Person psn, Map<String, String> psnInfoMap) throws Exception {
    // 校验email格式，校验当前人员是否登录过,未登录过的才更新
    Long psnId = psn.getPersonId();
    String email = psnInfoMap.get("email");
    SysUserLogin loginRecord = sysUserLoginDao.get(psnId);
    if (loginRecord == null && StringUtils.isNotBlank(email) && email.matches(EditValidateUtils.MAIL_COAD)) {
      // 判断该邮件是否已被人设置为登录邮箱或首要邮箱
      boolean hasExists = personEmailDao.emailHasOtherUsed(psnId, email);
      boolean hasUsed = userDao.isLoginNameUsed(email, psnId);
      if (!hasUsed && !hasExists) {
        // 若人员邮箱列表不存在该邮箱，则新增一条记录
        PersonEmail psnEmail = personEmailDao.getByPsnId(psnId, email);
        if (psnEmail == null) {
          psnEmail = new PersonEmail();
          PersonRegister psnRegister = personRegisterDao.get(psnId);
          psnEmail.setPerson(psnRegister);
          psnEmail.setEmail(email);
          String[] split = email.split("@");
          if (split != null && split.length == 2) {
            psnEmail.setLeftPart(split[0]);
            psnEmail.setRightPart(split[1]);
          }
          psnEmail.setFirstMail(false);
          psnEmail.setVerify(false);
          psnEmail.setLoginMail(false);
          this.personEmailDao.save(psnEmail);
        }
        Long emailId = psnEmail.getId();
        boolean flag = false;
        // 更新登录账号
        Integer result = userService.updateFirstEmail(email, psnId);
        // 更新首要邮件/登录名成功
        if (result == 1) {
          this.personEmailDao.setFirstLoginMail(emailId);
          psnEmail.setFirstMail(true);
          psnEmail.setLoginMail(true);
          flag = true;
        } else if (result == 2) {
          // 更新首要邮件成功
          this.personEmailDao.setFirstMail(emailId);
          psnEmail.setFirstMail(true);
        }
        psn.setEmail(email);
        this.personEmailDao.save(psnEmail);
        psnInfoMap.put("emailChanged", "true");
        // 发送确认邮箱邮件
        if (!psnEmail.isVerify()) {
          sendEmailCheck(psn, email);
        }
        // 发送首要邮件修改邮件或账号修改邮件
        Map<String, Object> mailMap = new HashMap<>();
        mailMap.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, psnId);
        mailMap.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, email);
        // 构建邮件
        generateEmailData(psn, flag, mailMap);
        // 发送邮件
        mailInitDataService.saveMailInitData(mailMap);
        logger.info("从SIE处同步信息--------------处理人员邮件完成");
      }
    }
  }

  /**
   * 生产邮件数据
   * 
   * @param person
   * @param flag
   * @param mailMap
   * @throws OpenException
   */
  private void generateEmailData(Person person, Boolean flag, Map<String, Object> mailMap) {
    String ftlTemplate = null;
    String psName = null;
    String subject = null;
    // 获取用户设置接收邮件的语言
    String languageVersion = person.getEmailLanguageVersion();
    if (languageVersion == null) {
      languageVersion = LocaleContextHolder.getLocale().toString();
    }
    if (Locale.CHINA.toString().equals(languageVersion)) {
      psName = person.getName();
      if (StringUtils.isBlank(psName)) {
        psName = person.getFirstName() + " " + person.getLastName();
      }
      if (flag) {
        subject = "登录账号设置";
        ftlTemplate = "Person_Email_Login_Template_" + languageVersion + ".ftl";
      } else {
        subject = "成功设置科研之友首要邮件";
        ftlTemplate = "Person_Email_First_Template_" + languageVersion + ".ftl";
      }
    } else {
      if (flag) {
        subject = "Login account change";
        ftlTemplate = "Person_Email_Login_Template_" + languageVersion + ".ftl";

      } else {
        subject = "Successfully setup primary email on ScholarMate";
        ftlTemplate = "Person_Email_First_Template_" + languageVersion + ".ftl";
      }
      psName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) || StringUtils.isBlank(person.getLastName())) {
        psName = person.getName();
      }
    }
    mailMap.put(EmailConstants.EMAIL_SUBJECT_KEY, subject);
    mailMap.put(EmailConstants.EMAIL_TEMPLATE_KEY, ftlTemplate);
    mailMap.put("zh_CN_psnname", psName);
    mailMap.put("toemail", person.getEmail());
    mailMap.put("domainUrl", domainscm);
  }

  /**
   * 构建账户确认邮件
   * 
   * @param map
   */
  public void buildSendEmailMapInfo(Map<String, Object> map, Person receiver, AccountEmailCheckLog accountEmailCheckLog)
      throws Exception {
    // 全文请求使用新模板
    if (receiver == null) {
      throw new Exception("构建账户邮件确认邮件，邮件对象为空" + this.getClass().getName());
    }
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    map.put("template", "Confirm_Email_Address_Template_zh_CN.ftl");
    map.put("domainUrl", domainscm);
    map.put("psnName", PsnInfoUtils.getPersonName(receiver, language));
    map.put("confirmCode", accountEmailCheckLog.getValidateCode());
    String confirmUrl = this.domainscm + "/psnweb/accountvalidate/ajaxdovalidte?" + "des3Id="
        + Des3Utils.encodeToDes3(accountEmailCheckLog.getId().toString());
    map.put("confirmUrl", confirmUrl);
    map.put(EmailConstants.EMAIL_SUBJECT_KEY, "确认邮件地址");
    map.put(EmailConstants.EMAIL_TEMPLATE_KEY, "Confirm_Email_Address_Template_zh_CN.ftl");
    map.put(EmailConstants.EMAIL_RECEIVE_PSNID_KEY, receiver.getPersonId());
    map.put(EmailConstants.EMAIL_RECEIVEEMAIL_KEY, accountEmailCheckLog.getAccount());
  }

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW,
      rollbackForClassName = {"java.lang.Exception"})
  private AccountEmailCheckLog saveAccountEmailCheckLog(String email, Long psnId) {
    // 保存记录
    AccountEmailCheckLog accountEmailCheckLog = new AccountEmailCheckLog();
    accountEmailCheckLog.setPsnId(psnId);
    accountEmailCheckLog.setAccount(email);
    accountEmailCheckLog.setDealStatus(0);
    accountEmailCheckLog.setSendTime(new Date());
    String validateCode = "";
    String validateCodeBig = "";
    validateCode = RandomNumber.getRandomNumber(6).toString();
    String randomStr = UUID.randomUUID().toString() + psnId + RandomNumber.getRandomStr(32);
    validateCodeBig = DigestUtils.md5Hex(randomStr);
    accountEmailCheckLog.setValidateCode(validateCode);
    accountEmailCheckLog.setValidateCodeBig(validateCodeBig);
    // 发送成功，保存记录
    accountEmailCheckLogDao.save(accountEmailCheckLog);
    return accountEmailCheckLog;
  }

  /**
   * 发送确认邮箱邮件
   * 
   * @param psn
   * @param email
   * @return
   */
  private boolean sendEmailCheck(Person psn, String email) {
    AccountEmailCheckLog accountEmailCheckLog = this.saveAccountEmailCheckLog(email, psn.getPersonId());
    // 发送邮件
    Map<String, Object> map = new HashMap<>();
    try {
      buildSendEmailMapInfo(map, psn, accountEmailCheckLog);
      mailInitDataService.saveMailInitData(map);
      return true;
    } catch (Exception e) {
      logger.error("构建账号邮件，确认邮件异常+psnId=" + psn.getPersonId(), e);
    }
    return false;
  }

}
