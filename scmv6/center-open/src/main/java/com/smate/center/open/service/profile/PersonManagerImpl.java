package com.smate.center.open.service.profile;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.smate.center.open.dao.profile.PsnDisciplineKeyDao;
import com.smate.center.open.dao.profile.PsnScienceAreaDao;
import com.smate.center.open.dao.profile.psnwork.PsnWorkHistoryInsInfoDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.profile.psnwork.PsnWorkHistoryInsInfo;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;

@Service("personManager")
public class PersonManagerImpl implements PersonManager {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;
  private String rootFolder;
  @Autowired
  private FileService fileService;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;

  @Override
  public String getPsnName(Long psnId, Locale locale) throws Exception {
    Person psn = personProfileDao.get(psnId);
    if (psn == null)
      return null;
    locale = locale == null ? LocaleContextHolder.getLocale() : locale;

    return this.getPsnName(psn, locale.toString());
  }

  @Override
  public String getPsnName(Person person, String locale) throws Exception {
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

  @Override
  public Person getPerson(Long psnId) throws Exception {
    Person psn = personProfileDao.get(psnId);
    if (psn != null) {
      psn.setViewName(this.buildPsnShowName(psn));// 构建显示名称.
      psn.setViewTitolo(this.getPsnViewTitolo(psn));// 构建显示头衔.
    }
    return psn;
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
   * 
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

  /**
   * 获取人员头衔显示信息<显示人员工作单位并取消头衔>
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

  @Override
  public Person getPersonById(Long psnId) throws Exception {
    return personProfileDao.get(psnId);
  }

  @Override
  public List<Person> getPersonList(String psnName, String insName, String email) throws Exception {

    try {
      return this.personProfileDao.queryPersonList(psnName, insName, email);
    } catch (Exception e) {
      logger.error("按照姓名和单位或邮件地址检索系统人员出现异常：", e);
      throw new Exception(e);
    }
  }

  /**
   * 获取个人联系方式.
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  @Override
  public Person getContact(Long psnId) throws OpenException {
    try {
      return personProfileDao.getContact(psnId);
    } catch (Exception e) {
      logger.error("获取个人联系方式信息出错", e);
      throw new OpenException(e);
    }
  }

  @Override
  public String getPersonBrief(Long psnId) throws OpenException {

    try {
      String briefRoot = this.getRootFolder() + "/brief";
      String brief = this.fileService.readTextTrimEmpty(getBriefFileName(psnId), briefRoot, "utf-8");
      if (StringUtils.isBlank(brief)) {
        brief = "";
      }
      return brief;
    } catch (Exception e) {
      logger.error("读取个人简介出错", e);
      throw new OpenException(e);
    }
  }

  private String getBriefFileName(Long psnId) throws OpenException {
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    String fileName = String.format("brief-%s-%s.txt", nodeId, psnId);
    return fileName;
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

  @Override
  public boolean checkPsnId(Long psnId) throws Exception {
    Person person = personProfileDao.getContact(psnId);
    if (person == null)
      return false;
    return true;
  }

  /**
   * 更新信息完整度百分比.
   *
   * 完整度标准:
   *
   * 工作经历 20% 教育经历 10% 姓名20%科研领域10%.
   *
   * 联系方式 15%（填写一项） 所在地 5% .
   *
   * 头像 5% 关键词10% 个人简介5%.
   *
   * @param psnId
   * @throws PsnException
   */
  @Override
  public void refreshComplete(Long psnId) throws OpenException {

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
      // 关键词
      if (this.psnDisciplineKeyDao.countPsnDisciplineKey(psnId) > 0l) {
        complete += 10;
      }
      // 工作经历
      if (this.workHistoryDao.isWorkHistoryExit(psnId)) {
        complete += 20;
      }
      // 教育经历
      if (this.educationHistoryDao.isEduHistoryExit(psnId)) {
        complete += 10;
      }
      // 研究领域
      if (psnScienceAreaDao.findPsnHasScienceArea(psnId, 1) > 0) {
        complete += 10;
      }
      person.setComplete(complete);
      this.personProfileDao.save(person);
      // 基金推荐刷新记录
      /**
       * (cxr-暂时不启用)this.fundCommentService.addFundCommentRefresh(psnId);
       */
      logger.info("从SIE处同步信息--------------处理人员信息完整度完成");
    } catch (Exception e) {
      logger.error("获取人员同步基本信息出错", e);
    }
  }
}
