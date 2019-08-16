package com.smate.center.oauth.service.profile;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.oauth.dao.profile.PsnScienceAreaDao;
import com.smate.center.oauth.dao.psnwork.PsnWorkHistoryInsInfoDao;
import com.smate.center.oauth.exception.DaoException;
import com.smate.center.oauth.exception.OauthException;
import com.smate.center.oauth.exception.ServiceException;
import com.smate.center.oauth.model.workhistory.PsnWorkHistoryInsInfo;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;

/**
 * 人员信息服务接口实现类
 * 
 * @author Administrator
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PersonServiceImpl implements PersonService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String rootFolder;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;
  @Autowired
  private FileService fileService;
  @Autowired
  private PersonalManager personalManager;
  @Autowired
  private WorkHistoryService workHistoryService;
  @Autowired
  private EducationHistoryService educationHistoryService;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;

  /**
   * 更新信息完整度百分比.
   * 
   * 完整度标准:
   * 
   * 工作经历 20% 教育经历 10% 姓名20%科技领域10%.
   * 
   * 联系方式 15%（填写一项） 所在地 5% .
   * 
   * 头像 5% 关键词10% 个人简介5%.
   * 
   * @param psnId
   * @throws ServiceException
   */
  @Override
  public void refreshComplete(Long psnId) throws OauthException {

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
      if (this.personalManager.isPsnDiscExit(psnId)) {
        complete += 10;
      }
      // 工作经历
      if (this.workHistoryService.isWorkHistoryExit(psnId)) {
        complete += 20;
      }
      // 教育经历
      if (this.educationHistoryService.isEduHistoryExit(psnId)) {
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
    } catch (Exception e) {
      logger.error("获取人员同步基本信息出错", e);
    }
  }

  @Override
  public Person getPerson(Long psnId) throws OauthException {
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
   * 构建人员显示名称
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
   * 获取人员头衔显示信息<显示人员工作单位并取消头衔>_MJG_SCM-5707.
   * 
   * @param psn
   * @return
   * @throws ServiceException
   */
  public String getPsnViewTitolo(Person psn) throws OauthException {
    String viewTitolo = "";
    try {
      PsnWorkHistoryInsInfo psnIns = psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psn.getPersonId());
      if (psnIns != null) {
        Locale locale = LocaleContextHolder.getLocale();
        if (Locale.US.equals(locale)) {
          viewTitolo = (StringUtils.isBlank(psnIns.getInsNameEn())) ? psnIns.getInsNameZh() : psnIns.getInsNameEn();
        } else {
          viewTitolo = (StringUtils.isNotBlank(psnIns.getInsNameZh())) ? psnIns.getInsNameZh() : psnIns.getInsNameEn();
        }
      }
    } catch (OauthException e) {
      logger.error("查询人员工作信息出错", e);
    }
    if (StringUtils.isBlank(viewTitolo) && StringUtils.isNotBlank(psn.getDefaultAffiliation())) {
      viewTitolo = psn.getDefaultAffiliation();
    }
    return viewTitolo;
  }

  @Override
  public String getPersonBrief(Long psnId) throws OauthException {

    try {
      // 先读取缓存
      // String brief = (String)
      // this.cacheService.get(PersonManager.PSN_BRIEF_CACHE,
      // psnId.toString());
      // if (brief == null) {
      String briefRoot = this.getRootFolder() + "/brief";
      String brief = this.fileService.readTextTrimEmpty(getBriefFileName(psnId), briefRoot, "utf-8");
      if (StringUtils.isBlank(brief)) {
        brief = "";
      }
      // 加入缓存
      // this.cacheService.put(PersonManager.PSN_BRIEF_CACHE,
      // PersonManager.PSN_BRIEF_CACHE_TIMEOUT,
      // psnId.toString(), brief);
      return brief;
      // }
      // return brief;
    } catch (Exception e) {
      logger.error("读取个人简介出错", e);
      throw new OauthException(e);
    }
  }

  private String getBriefFileName(Long psnId) throws OauthException {
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
  public List<Person> queryPersonForPsnHtml(List<Long> psnIds) throws OauthException {
    try {
      return personProfileDao.queryPersonForPsnHtml(psnIds);
    } catch (OauthException e) {
      logger.error("查询人员psnIds={}出现异常：{}", psnIds, e);
      throw new OauthException(e);
    }
  }

  @Override
  public String refreshRemoteAvatars(Long psnId, Integer sex, String avator) {

    String domain = "";
    try {
      // FIXME 2015-10-29 取消远程调用 -done
      domain = sysDomainConst.getSnsContext() + "/";
      // 获取用户头像
      if (avator != null && !(avator.endsWith(ServiceConstants.DEFAULT_MAN_AVATARS)
          || avator.endsWith(ServiceConstants.DEFAULT_WOMAN_AVATARS))) {
        if (avator.startsWith("http://")) {
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
    } catch (DaoException e) {
      logger.error("获取个人联系方式信息出错", e);
      throw new ServiceException(e);
    }
  }
}
