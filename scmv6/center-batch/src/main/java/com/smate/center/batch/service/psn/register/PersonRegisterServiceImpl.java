package com.smate.center.batch.service.psn.register;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.center.batch.dao.pdwh.psn.PdwhAddrPsnUpdateRecordDao;
import com.smate.center.batch.dao.sns.psn.PersonKnowDao;
import com.smate.center.batch.dao.sns.psn.register.EducationHistoryRegisterDao;
import com.smate.center.batch.dao.sns.psn.register.PersonRegisterDao;
import com.smate.center.batch.dao.sns.psn.register.WorkHistoryRegisterDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.psn.PdwhAddrPsnUpdateRecord;
import com.smate.center.batch.model.psn.register.PersonRegister;
import com.smate.center.batch.model.psn.register.WorkHistoryRegister;
import com.smate.center.batch.model.rol.psn.WorkHistoryRol;
import com.smate.center.batch.model.sns.prj.PersonKnow;
import com.smate.center.batch.model.sns.pub.ConstPosition;
import com.smate.center.batch.model.sns.pub.ConstRegion;
import com.smate.center.batch.service.friend.SystemRecommendServiceImpl;
import com.smate.center.batch.service.institution.InstitutionManager;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.center.batch.service.psn.PsnHtmlService;
import com.smate.center.batch.service.psn.WorkHistoryInsInfoService;
import com.smate.center.batch.service.pub.ConstPositionService;
import com.smate.center.batch.service.pub.ConstRegionService;
import com.smate.center.batch.service.user.UserService;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.MapBuilder;

/**
 * 人员注册服务实现
 * 
 * @author tsz
 * 
 */
@Service("personRegisterService")
@Transactional(rollbackFor = Exception.class)
public class PersonRegisterServiceImpl implements PersonRegisterService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstPositionService constPositionService;
  @Autowired
  private UserService userService;
  @Autowired
  private EducationHistoryRegisterDao educationHistoryRegisterDao;
  @Autowired
  private WorkHistoryRegisterDao workHistoryRegisterDao;
  @Autowired
  private InstitutionManager institutionManager;
  @Autowired
  private ConstRegionService constRegionService;
  @Autowired
  private PersonRegisterDao personRegisterDao;
  @Autowired
  private PersonKnowDao personKnowDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private SystemRecommendServiceImpl systemRecommendServiceImpl;
  @Autowired
  private PubScoreManagerService pubScoreManagerService;
  @Autowired
  private WorkHistoryInsInfoService workHistoryInsInfoService;
  @Autowired
  private PsnHtmlService snsPsnHtmlService;
  @Autowired
  private PdwhAddrPsnUpdateRecordDao pdwhAddrPsnUpdateRecordDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 人员注册服务
   * 
   * @throws Exception
   */
  @Override
  public Long saveR(PersonRegister person) throws Exception {
    try {
      if (person == null) {
        return 0L;
      }
      // SCM-17073 ，SCM-17461 新注册人员更新姓名常量表
      try {
        pdwhAddrPsnUpdateRecordDao.deleteRecordByPsnId(person.getPersonId());
        PdwhAddrPsnUpdateRecord record = new PdwhAddrPsnUpdateRecord(person.getPersonId(), 2, 0);
        pdwhAddrPsnUpdateRecordDao.save(record);
      } catch (Exception e) {
        logger.error("PdwhAddrPsnUpdateRecord保存更新人员记录出错", e);
      }
      // 同步冗余
      this.syncSavePersonKnow(person);
      // 同步智能推荐好友
      systemRecommendServiceImpl.instantRecommend(person);
      userService.updateSolrPsnInfo(person.getPersonId());
      return person.getPersonId();
    } catch (Exception e) {
      logger.error("初始化人员注册 数据出错", e);
      throw new Exception("初始化人员注册 数据出错！", e);
    }
  }



  // 登录表人员登录账号查重
  private boolean isExists(String loginName) throws Exception {
    boolean isExists = false;
    try {
      isExists = userService.isLoginNameExist(loginName);
    } catch (Exception e) {
      logger.error("判断用户信息异常 ", e);
    }
    return isExists;
  }

  /**
   * 获取人员的头衔.
   * 
   * @param person
   * @return
   * @throws ServiceException
   */
  private String getInitPersonTitolo(PersonRegister person) {
    String titolo = person.getTitolo();
    if (StringUtils.isBlank(titolo)) {
      if (StringUtils.isNotBlank(person.getInsName()) || StringUtils.isNotBlank(person.getPosition())) {
        String position = ObjectUtils.toString(person.getPosition()).trim();
        if (XmlUtil.isChinese(person.getInsName())) {
          titolo = person.getInsName() + position;
        } else {
          titolo = person.getInsName() + (position.length() == 0 ? "" : " " + position);
        }
      } else {
        ConstRegion constRegion = constRegionService.getRegionRolById(person.getRegionId());
        if (constRegion != null) {
          if (Locale.US.equals(LocaleContextHolder.getLocale())) {
            titolo = constRegion.getEnName();
          } else {
            titolo = constRegion.getZhName();
          }
        }

      }
    }
    return titolo;
  }

  /**
   * 同步关键字冗余
   * 
   * @param person
   */
  private void syncSavePersonKnow(PersonRegister person) {
    if (person == null || person.getPersonId() == null) {
      return;
    }
    PersonKnow personKnow = new PersonKnow();
    personKnow.setPersonId(person.getPersonId());
    personKnow.setComplete(0);
    personKnow.setIsAdd(0);
    personKnow.setIsPrivate(0);
    personKnow.setIsLogin(0);
    personKnow.setIsAddFrd(0);
    personKnowDao.save(personKnow);
  }

  /**
   * // 同步用户索引.
   * 
   * @param person
   * @throws ServiceException
   */
  /*
   * private void syncUserIndex(PersonRegister person) throws Exception {
   * userSearchService.saveUserSearch(person.getPersonId(), person.getName(), person.getEname(), 0, 1,
   * ServiceConstants.SCHOLAR_NODE_ID_1, 0, 0); }
   */

  private WorkHistoryRol createWorkHistory(WorkHistoryRegister message) {
    WorkHistoryRol workHistory = new WorkHistoryRol();
    workHistory.setWorkId(message.getWorkId());
    workHistory.setPsnId(message.getPsnId());
    workHistory.setInsId(message.getInsId());
    workHistory.setFromYear(message.getFromYear());
    workHistory.setFromMonth(message.getFromMonth());
    if (message.getToYear() != null) {
      workHistory.setToYear(message.getToYear());
    }
    if (message.getToMonth() != null) {
      workHistory.setToMonth(message.getToMonth());
    }
    // 默认为0
    workHistory.setIsActive(message.getIsActive() != null ? message.getIsActive() : 0);
    return workHistory;
  }

  @Override
  public void matchPdwhPub(PersonRegister person) {
    Map<String, Object> params = this.assembleInfo(person);

    this.pubScoreManagerService.dealExactMatchedPubScore(params);

  }

  /**
   * 封装用户信息为map.
   * 
   * @param message
   * @return
   */
  @SuppressWarnings("unchecked")
  private Map<String, Object> assembleInfo(PersonRegister person) {
    Map<String, Object> result = MapBuilder.getInstance().getMap();
    result.put(PubRegisterMatchService.PARAM_PSN_ID, person.getPersonId());
    result.put(PubRegisterMatchService.PARAM_EMAIL, person.getEmail());
    result.put(PubRegisterMatchService.PARAM_ZHNAME, person.getName());
    result.put(PubRegisterMatchService.PARAM_ENNAME, person.getEname());
    result.put(PubRegisterMatchService.PARAM_FIRST_NAME, person.getFirstName());
    result.put(PubRegisterMatchService.PARAM_LAST_NAME, person.getLastName());
    result.put(PubRegisterMatchService.PARAM_OTHER_NAME, person.getOtherName());
    result.put(PubRegisterMatchService.PARAM_INS_ID, person.getInsId());
    result.put(PubRegisterMatchService.PARAM_INS_NAME, person.getInsName());

    // 根据身份设置时间,在职 ||就读
    result.put(PubRegisterMatchService.PARAM_START_YEAR,
        StringUtils.isNotBlank(person.getStudyFromYear()) ? Long.parseLong(person.getStudyFromYear()) : "");
    result.put(PubRegisterMatchService.PARAM_END_YEAR,
        StringUtils.isNotBlank(person.getStudyToYear()) ? Long.parseLong(person.getStudyToYear()) : "");

    return result;
  }

  @Override
  public PersonRegister getPersonRegisterInfo(Long psnId) {
    PersonRegister person = this.personRegisterDao.get(psnId);
    return person;
  }

  /**
   * 人员注册时，psnHtml列表需要处理的数据
   * 
   * @param zk
   * @since 2014/09/02
   * @param person
   * @param result
   */
  @Override
  public void psnHtmlNeedData(PersonRegister person, Long result) throws ServiceException {

    // 只有工作经历需要处理个人单位历史信息
    if ("0".equals(person.getPositionType())) {
      WorkHistory work = new WorkHistory();
      work.setPsnId(result);
      // 如果用户注册的单位在单位表有相应单位则将其主键插入到工作经历的关联单位id中
      if (person.getInsId() == null) {
        work.setInsName(person.getInsName());
      } else {
        work.setInsId(person.getInsId());
        work.setInsName(person.getInsName());
      }
      if (person.getUnitId() == null && person.getUnit() != null) {
        work.setDepartment(person.getUnit());
      }
      // 职务
      String position = person.getPosition() == null ? null : person.getPosition().trim();
      if (StringUtils.isNotBlank(position)) { // 职务不为空
        if (person.getPosId() == null) { // 职务Id为空
          ConstPosition constPosition = constPositionService.getPosByName(position);
          if (constPosition != null) { // 查询
            work.setPosId(constPosition.getId());
            work.setPosGrades(constPosition.getGrades());
          }
        } else {
          work.setPosId(person.getPosId());
        }
        work.setPosition(position);
      }
      // 1,首要单位
      workHistoryInsInfoService.updateWorkHistoryInsInfo(work, 1);
    }
    // 需刷新人员Html列表
    snsPsnHtmlService.updatePsnHtmlRefresh(result);
  }



  @Override
  public void initPsnFundRecommend(Long psnId) throws ServiceException {
    if (psnId == null) {
      return;
    }
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("psnIds", psnId.toString());
    postUrl(params, domainMobile + "/prjdata/initpsnrecommedfund");
    return;
  }


  @SuppressWarnings({"unchecked", "rawtypes"})
  private Map<String, Object> postUrl(MultiValueMap param, String url) {
    HttpEntity<MultiValueMap> httpEntity = this.getEntity(param);// 创建头部信息
    return restTemplate.postForObject(url, httpEntity, Map.class);
  }

  @SuppressWarnings("rawtypes")
  private HttpEntity getEntity(MultiValueMap param) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return HttpEntity;
  }

}
