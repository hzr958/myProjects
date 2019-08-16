package com.smate.center.batch.service.psn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.dao.sns.psn.PsnWorkHistoryInsInfoDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnWorkHistoryInsInfo;
import com.smate.center.batch.service.institution.InstitutionManager;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;

/**
 * @author zt.
 * 
 */
@Service("personManager")
@Transactional(rollbackFor = Exception.class)
public class PersonManagerImpl implements PersonManager {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String rootFolder;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private FileService fileService;
  @Autowired
  private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private WorkHistoryService workHistoryService;
  @Autowired
  private InstitutionManager institutionManager;
  @Autowired
  private PersonalManager personalManager;
  @Autowired
  private EducationHistoryService educationHistoryService;

  /**
   * 获取用户信息(包含头衔的显示信息).
   */
  @Override
  public Person getPersonByRecommend(Long personId) {
    Person psn = personProfileDao.getPersonByRecommend(personId);
    if (psn == null)
      return null;
    psn.setViewName(this.buildPsnShowName(psn));// 构建显示名称.
    psn.setViewTitolo(this.getPsnViewTitolo(psn));
    return psn;
  }

  @Override
  public String getPsnName(Person psn) {
    return this.getPsnName(psn, LocaleContextHolder.getLocale().toString());
  }

  @Override
  public String getPsnName(Person person, String locale) {
    String psnName = "";
    if ("zh_CN".equals(locale)) {
      psnName = person.getName();
      if (StringUtils.isBlank(psnName)) {
        psnName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) && StringUtils.isBlank(person.getLastName())) {
        psnName = person.getName();
      }
    }

    return psnName;
  }

  /**
   * 获取用户姓名，姓名可能为空，需要转换.
   * 
   * @param locale
   * @param person
   * @return
   */
  private String buildPsnShowName(Person person) {
    String zhName = person.getName();
    String firstName = person.getFirstName();
    String lastName = person.getLastName();
    return this.getPsnViewName(zhName, firstName, lastName);
  }

  /**
   * 获取人员头衔显示信息<显示人员工作单位并取消头衔>_MJG_SCM-5707.
   * 
   * @param psn
   * @return
   */
  public String getPsnViewTitolo(Person psn) {
    String viewTitolo = "";
    PsnWorkHistoryInsInfo psnIns = psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psn.getPersonId());
    if (psnIns != null) {
      Locale locale = LocaleContextHolder.getLocale();
      if (Locale.US.equals(locale)) {
        viewTitolo = (StringUtils.isBlank(psnIns.getInsNameEn())) ? psnIns.getInsNameZh() : psnIns.getInsNameEn();
      } else {
        viewTitolo = (StringUtils.isNotBlank(psnIns.getInsNameZh())) ? psnIns.getInsNameZh() : psnIns.getInsNameEn();
      }
    }
    if (StringUtils.isBlank(viewTitolo) && StringUtils.isNotBlank(psn.getDefaultAffiliation())) {
      viewTitolo = psn.getDefaultAffiliation();
    }
    return viewTitolo;
  }

  /**
   * 构建人员显示名称_MJG_SCM-5707.
   * 
   * @param zhName
   * @param firstName
   * @param lastName
   * @return
   */
  public String getPsnViewName(String zhName, String firstName, String lastName) {
    String showName = null;
    Locale locale = LocaleContextHolder.getLocale();
    if (Locale.US.equals(locale)) {
      showName =
          (StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName)) ? zhName : firstName + " " + lastName;
    } else {
      showName = (StringUtils.isNotBlank(zhName)) ? zhName : firstName + " " + lastName;
    }
    return showName;
  }

  @Override
  public Person getPerson(Long psnId) {
    Person psn = personProfileDao.get(psnId);
    if (psn != null) {
      psn.setViewName(this.buildPsnShowName(psn));// 构建显示名称.
      psn.setViewTitolo(this.getPsnViewTitolo(psn));// 构建显示头衔.
    }
    return psn;
  }

  @Override
  public String getPersonBrief(Long psnId) throws ServiceException {

    try {
      String brief = null;
      String briefRoot = this.getRootFolder() + "/brief";
      brief = this.fileService.readTextTrimEmpty(getBriefFileName(psnId), briefRoot, "utf-8");
      if (StringUtils.isBlank(brief)) {
        brief = "";
      }
      return brief;
    } catch (Exception e) {
      logger.error("读取个人简介出错", e);
      throw new ServiceException(e);
    }
  }

  public String getRootFolder() {
    return rootFolder;
  }

  public void setRootFolder(String rootFolder) {
    Assert.notNull(rootFolder);
    rootFolder = rootFolder.replace("\\", "/");
    if (rootFolder.endsWith("/")) {
      rootFolder = rootFolder.substring(0, rootFolder.length() - 1);
    }
    this.rootFolder = rootFolder;
  }

  private String getBriefFileName(Long psnId) throws ServiceException {
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    String fileName = String.format("brief-%s-%s.txt", nodeId, psnId);
    return fileName;
  }

  /**
   * 获取个人头像信息.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public Person getAvatarsForEdit(Long psnId) throws ServiceException {
    try {
      Person person = personProfileDao.getAvatars(psnId);
      // 无头像，设置默认头像
      if (StringUtils.isBlank(person.getAvatars())) {
        person.setAvatars(this.refreshRemoteAvatars(person.getPersonId(), person.getSex(), null));
      }
      person.setDefaultAvators(this.refreshRemoteAvatars(person.getPersonId(), person.getSex(), null));
      return person;
    } catch (Exception e) {
      logger.error("获取个人头像信息", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String refreshRemoteAvatars(Long psnId, Integer sex, String avator) {

    String domain = "";
    try {
      // FIXME 2015-10-29 取消远程调用 -done
      domain = domainscm + "/";
      // 获取用户头像

      if (avator != null && !(avator.endsWith(ServiceConstants.DEFAULT_MAN_AVATARS)
          || avator.endsWith(ServiceConstants.DEFAULT_WOMAN_AVATARS))) {
        if (avator.startsWith("http://") || avator.startsWith("https://")) {
          return avator;
        }
        return domain + avator;
      } else if (sex != null && sex == 0) {// 女性
        return domain + ServiceConstants.DEFAULT_WOMAN_AVATARS;
      } else {
        return domain + ServiceConstants.DEFAULT_MAN_AVATARS;
      }
    } catch (Exception e) {
      logger.error("读取个人信息出错", e);
      return domain + ServiceConstants.DEFAULT_MAN_AVATARS;
    }
  }

  /**
   * 获取个人联系方式.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public Person getContact(Long psnId) throws Exception {
    try {
      return personProfileDao.getContact(psnId);
    } catch (Exception e) {
      logger.error("获取个人联系方式信息出错", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 根据性别设定人员初始头像_MJG_SCM-3733.
   * 
   * @param nodeId
   * @param sex
   * @return
   */
  @Override
  public String initPersonAvatars(Integer nodeId, Integer sex) {
    String avatars = null;
    String domain = "";
    try {
      domain = domainscm + "/";
      if (sex != null && sex == 0) {// 女性
        avatars = domain + ServiceConstants.DEFAULT_WOMAN_AVATARS;
      } else {
        avatars = domain + ServiceConstants.DEFAULT_MAN_AVATARS;
      }
    } catch (Exception e) {
      logger.error("读取个人信息出错", e);
      avatars = domain + ServiceConstants.DEFAULT_MAN_AVATARS;
    }
    return avatars;
  }

  @Override
  public List<Person> findPersonList(List<Long> psnIdList) throws ServiceException {
    return this.personProfileDao.findByIds(psnIdList);
  }

  @Override
  public Person getPsnNameAndAvatars(Long psnId) throws ServiceException {
    try {
      return this.personProfileDao.queryPsnNameAndAvatars(psnId);
    } catch (Exception e) {
      logger.error("查询人员psnId={}的姓名和投降出现异常：{}", psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<WorkHistory> findWorkAndEdu(Long psnId) throws ServiceException {
    try {
      psnId = psnId == null ? SecurityUtils.getCurrentUserId() : psnId;
      List<WorkHistory> list = new ArrayList<WorkHistory>();
      List<WorkHistory> sortList = new ArrayList<WorkHistory>();
      String primaryInsName = "";
      Long formYear = null;
      Long toYear = null;

      List<WorkHistory> workList = workHistoryService.findWorkHistoryByPsnId(psnId);
      for (WorkHistory workHistory : workList) {
        if (workHistory.getIsPrimary() != null && workHistory.getIsPrimary() == 1) {
          if (workHistory.getInsId() != null && StringUtils.isBlank(workHistory.getInsName())) {
            String insName = institutionManager.getLocaleInsName(workHistory.getInsId());
            if (StringUtils.isNotBlank(insName)) {
              primaryInsName = insName;
              formYear = workHistory.getFromYear();
              toYear = workHistory.getToYear();
            }
          } else {
            primaryInsName = workHistory.getInsName();
            formYear = workHistory.getFromYear();
            toYear = workHistory.getToYear();
          }
        }
        if (workHistory.getIsActive() != null && workHistory.getIsActive() == 1) {
          if (StringUtils.isBlank(primaryInsName)) {
            if (workHistory.getInsId() != null && StringUtils.isBlank(workHistory.getInsName())) {
              String insName = institutionManager.getLocaleInsName(workHistory.getInsId());
              if (StringUtils.isNotBlank(insName)) {
                primaryInsName = insName;
                formYear = workHistory.getFromYear();
                toYear = workHistory.getToYear();
              }
            } else {
              primaryInsName = workHistory.getInsName();
              formYear = workHistory.getFromYear();
              toYear = workHistory.getToYear();
            }
          }
          sortList.add(workHistory);
        } else if (workHistory.getToYear() != null && workHistory.getToMonth() != null) {
          if (workHistory.getInsId() != null && StringUtils.isBlank(workHistory.getInsName())) {
            String insName = institutionManager.getLocaleInsName(workHistory.getInsId());
            if (StringUtils.isNotBlank(insName))
              workHistory.setInsName(insName);
          }
          sortList.add(workHistory);
        }
      }

      List<EducationHistory> eduList = educationHistoryService.findByPsnId(psnId);
      for (EducationHistory educationHistory : eduList) {
        if (educationHistory.getIsPrimary() != null && educationHistory.getIsPrimary() == 1) {
          if (educationHistory.getInsId() != null && StringUtils.isBlank(educationHistory.getInsName())) {
            String insName = institutionManager.getLocaleInsName(educationHistory.getInsId());
            if (StringUtils.isNotBlank(insName)) {
              primaryInsName = insName;
              formYear = educationHistory.getFromYear();
              toYear = educationHistory.getToYear();
            }
          } else {
            primaryInsName = educationHistory.getInsName();
            formYear = educationHistory.getFromYear();
            toYear = educationHistory.getToYear();
          }
        }
        if (educationHistory.getToYear() != null && educationHistory.getToMonth() != null) {
          if (educationHistory.getInsId() != null && StringUtils.isBlank(educationHistory.getInsName())) {
            String insName = institutionManager.getLocaleInsName(educationHistory.getInsId());
            if (StringUtils.isNotBlank(insName))
              educationHistory.setInsName(insName);
          }
          WorkHistory work = new WorkHistory();
          work.setInsName(educationHistory.getInsName());
          work.setFromYear(educationHistory.getFromYear());
          work.setToYear(educationHistory.getToYear());
          sortList.add(work);
        }
      }

      if (StringUtils.isNotBlank(primaryInsName)) {
        WorkHistory work = new WorkHistory();
        work.setInsName(primaryInsName);
        work.setFromYear(formYear);
        work.setToYear(toYear);
        list.add(work);
      }
      for (WorkHistory work : sortList) {
        if (StringUtils.isNotBlank(work.getInsName()) && !work.getInsName().equals(primaryInsName)) {
          boolean isexist = false;
          for (WorkHistory obj : list) {
            if (work.getInsName().equals(obj.getInsName()))
              isexist = true;
          }
          if (!isexist)
            list.add(work);
        }
      }
      return list;
    } catch (Exception e) {
      logger.error("获取人员所有工作单位和教育经历出错", e);
      return null;
    }
  }

  @Override
  public Person getPersonForEmail(Long psnId) throws ServiceException {
    try {
      return personProfileDao.getPersonForEmail(psnId);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Person getPersonById(Long psnId) throws ServiceException {

    return personProfileDao.get(psnId);
  }

  @Override
  public String getPsnName(Long psnId, Locale locale) throws ServiceException {
    Person psn = personProfileDao.get(psnId);
    if (psn == null)
      return null;
    locale = locale == null ? LocaleContextHolder.getLocale() : locale;

    return this.getPsnName(psn, locale.toString());
  }

  @Override
  public void refreshComplete(Long psnId) throws ServiceException {

    try {
      int complete = 0;
      Person person = this.getPerson(psnId);
      if (person == null)
        return;
      // 姓名，姓名一定有
      complete += 20;
      // 联系方式,email一定有
      complete += 15;
      // 所在地
      if (person.getRegionId() != null) {
        complete += 5;
      }

      // 个人简介
      String brief = this.getPersonBrief(psnId);
      if (StringUtils.isNotBlank(brief)) {
        complete += 5;
      }
      // 头像
      if (person.getAvatars() != null && !person.getAvatars().endsWith(ServiceConstants.DEFAULT_MAN_AVATARS)
          && !person.getAvatars().endsWith(ServiceConstants.DEFAULT_WOMAN_AVATARS)) {
        complete += 5;
      }
      // 熟悉的学科
      if (this.personalManager.isPsnDiscExit(psnId)) {
        complete += 10;
      }
      // 工作经历
      if (this.workHistoryService.isWorkHistoryExit(psnId)) {
        complete += 20;
      }
      // 教育经历
      if (this.educationHistoryService.isEduHistoryExit(psnId)) {
        complete += 20;
      }
      person.setComplete(complete);
      this.personProfileDao.save(person);

      // 基金推荐刷新记录
      /**
       * (cxr-暂时不启用)this.fundCommentService.addFundCommentRefresh(psnId);
       */
    } catch (Exception e) {
      logger.error("获取人员同步基本信息出错", e);
      throw new ServiceException("获取人员同步基本信息出错", e);
    }
  }
}
