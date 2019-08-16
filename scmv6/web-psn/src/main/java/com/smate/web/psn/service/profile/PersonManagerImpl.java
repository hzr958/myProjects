package com.smate.web.psn.service.profile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.qq.connect.utils.http.BASE64Encoder;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.ConstPosition;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.consts.service.ConstPositionService;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.person.avatars.PersonAvatarsUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.web.psn.cache.PsnCacheService;
import com.smate.web.psn.dao.attention.AttPersonDao;
import com.smate.web.psn.dao.friend.FriendDao;
import com.smate.web.psn.dao.friend.PersonKnowDao;
import com.smate.web.psn.dao.keywords.KeywordsDicDao;
import com.smate.web.psn.dao.keywork.PsnScienceAreaDao;
import com.smate.web.psn.dao.open.PersonOpenDao;
import com.smate.web.psn.dao.profile.PersonEmailDao;
import com.smate.web.psn.dao.profile.PsnSidDao;
import com.smate.web.psn.dao.profile.ReadStatisticsDao;
import com.smate.web.psn.dao.psn.PdwhAddrPsnUpdateRecordDao;
import com.smate.web.psn.dao.psnrefreshinfo.PsnRefreshUserInfoDao;
import com.smate.web.psn.dao.psnwork.PsnWorkHistoryInsInfoDao;
import com.smate.web.psn.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.PsnInfoDaoException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.mobile.PsnHomepageMobileForm;
import com.smate.web.psn.model.attention.AttPerson;
import com.smate.web.psn.model.friend.PersonKnow;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.model.pdwh.pub.PdwhAddrPsnUpdateRecord;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.model.psninfo.PsnRefreshUserInfo;
import com.smate.web.psn.model.psninfo.PsnSid;
import com.smate.web.psn.model.rcmdsync.RcmdSyncPsnInfo;
import com.smate.web.psn.model.search.PersonSearch;
import com.smate.web.psn.model.third.user.PersonOpen;
import com.smate.web.psn.model.workhistory.PsnWorkHistoryInsInfo;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.psncnf.build.PsnCnfBuildService;
import com.smate.web.psn.service.psnemail.PsnFirstEmailService;
import com.smate.web.psn.service.psnhtml.PsnHtmlRefreshService;
import com.smate.web.psn.service.user.UserService;

/**
 * 人员信息服务接口实现类
 *
 * @author Administrator
 *
 */
@Service("personManager")
@Transactional(rollbackFor = Exception.class)
public class PersonManagerImpl implements PersonManager {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static final String regionName = "中国";
  private static final String regionNameEn = "China";
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
  private PsnSolrInfoModifyService psnSolrInfoModifyService;
  @Autowired
  private SysDomainConst sysDomainConst;
  @Autowired
  private PsnSidDao psnSidDao;
  @Autowired
  private PsnPrivateService psnPrivateService;
  @Autowired
  private PersonKnowDao personKnowDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PsnCnfBuildService cnfBuildService;
  @Autowired
  private PsnCacheService psnCacheService;
  @Autowired
  private ReadStatisticsDao readStatisticsDao;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;
  @Autowired
  private PdwhAddrPsnUpdateRecordDao pdwhPubMacthTaskRecordDao;
  @Autowired
  private PsnHtmlRefreshService psnHtmlRefreshService;
  @Autowired
  private ConstPositionService constPositionService;
  @Autowired
  private PsnCnfService psnCnfService;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private UserService userService;
  @Autowired
  private UserDao userDao;
  @Autowired
  private PsnFirstEmailService psnFirstEmailService;
  @Autowired
  BatchJobsService batchJobsService;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Autowired
  private HibernateTemplate hibernateTemplate;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private WorkHistoryInsInfoService workHistoryInsInfoService;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${file.root}")
  private String rootPath;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private FriendService friendService;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private ConstSurNameService constSurNameService;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao;
  @Autowired
  private AttPersonDao attPersonDao;
  @Autowired
  private KeywordsDicDao keywordsDicDao;
  @Autowired
  private PersonOpenDao personOpenDao;
  @Autowired
  private PdwhAddrPsnUpdateRecordDao pdwhAddrPsnUpdateRecordDao;

  @Override
  public Person getPersonInfo(Long psnId) {
    return personProfileDao.getPersonInfoByPsnIdForFriend(psnId);
  }

  @Override
  public String getPsnName(Person person, String locale) throws PsnException {
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
   * 由于暂时Person表中只有中文的单位部门职称字段，故中英文处理逻辑相同
   */
  @Override
  public String getPsnWrokInfo(Person psn, String locale) throws PsnException {
    String workInfo = "";
    String insName = psn.getInsName();
    String department = psn.getDepartment();
    String position = psn.getPosition();
    if ("en_US".equals(locale)) {
      if (StringUtils.isNotBlank(insName)) {
        workInfo += insName;
        if (StringUtils.isNotBlank(department)) {
          workInfo += "," + department;
        }
        if (StringUtils.isNotBlank(position)) {
          workInfo += "," + position;
        }
      } else {
        if (StringUtils.isNotBlank(department)) {
          workInfo += department;
          if (StringUtils.isNotBlank(position)) {
            workInfo += "," + position;
          }
        } else {
          workInfo += position;
        }
      }
    } else {
      if (StringUtils.isNotBlank(insName)) {
        workInfo += insName;
        if (StringUtils.isNotBlank(department)) {
          workInfo += "," + department;
        }
        if (StringUtils.isNotBlank(position)) {
          workInfo += "," + position;
        }
      } else {
        if (StringUtils.isNotBlank(department)) {
          workInfo += department;
          if (StringUtils.isNotBlank(position)) {
            workInfo += "," + position;
          }
        } else {
          if (StringUtils.isNotBlank(position)) {
            workInfo = position;
          }
        }
      }
    }
    return workInfo;
  }

  @Override
  public String getPsnRegionInfo(Person psn, String locale) throws PsnException {
    String zhPsnRegionName = "";
    String enPsnRegionName = "";
    if (psn != null && psn.getRegionId() != null) {
      Long psnRegionId = psn.getRegionId();
      // 根据地区ID和上一级地区ID构建人员所在地区信息
      int count = 0;
      while (psnRegionId != null) {
        count++;
        if (count > 5) {
          break;
        }
        ConstRegion cre = this.constRegionDao.findRegionNameById(psnRegionId);
        if (cre != null) {
          psnRegionId = cre.getSuperRegionId();
          if (StringUtils.isNotBlank(zhPsnRegionName)) {
            zhPsnRegionName = cre.getZhName() + ", " + zhPsnRegionName;
          } else {
            zhPsnRegionName = cre.getZhName();
          }
          if (StringUtils.isNotBlank(enPsnRegionName)) {
            enPsnRegionName = cre.getEnName() + ", " + enPsnRegionName;
          } else {
            enPsnRegionName = cre.getEnName();
          }
        } else {
          break;
        }
      }
    } else {
      zhPsnRegionName = regionName;
      enPsnRegionName = regionNameEn;
    }
    if ("zh_CN".equals(locale)) {
      return StringUtils.isNotBlank(zhPsnRegionName) ? zhPsnRegionName : enPsnRegionName;
    } else {
      return StringUtils.isNotBlank(enPsnRegionName) ? enPsnRegionName : zhPsnRegionName;
    }
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
  public void refreshComplete(Long psnId) throws PsnException {

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
  public Person getPerson(Long psnId) throws PsnException {
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
   * @throws PsnException
   */
  public String getPsnViewTitolo(Person psn) throws PsnException {
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
    } catch (PsnInfoDaoException e) {
      logger.error("查询人员工作信息出错", e);
    }
    if (StringUtils.isBlank(viewTitolo) && StringUtils.isNotBlank(psn.getDefaultAffiliation())) {
      viewTitolo = psn.getDefaultAffiliation();
    }
    return viewTitolo;
  }

  @Override
  public String getPersonBrief(Long psnId) throws PsnException {
    try {
      // 优先从缓存中取
      String brief = (String) this.psnCacheService.get(PersonManager.PSN_BRIEF_CACHE, Objects.toString(psnId));
      if (brief == null) {
        String briefRoot = this.getRootFolder() + "/brief";
        brief = this.fileService.readTextTrimEmpty(getBriefFileName(psnId), briefRoot, "utf-8");
        if (StringUtils.isBlank(brief)) {
          brief = "";
        }
        // 加入缓存
        this.psnCacheService.put(PersonManager.PSN_BRIEF_CACHE, PersonManager.PSN_BRIEF_CACHE_TIMEOUT, psnId.toString(),
            brief);
      }
      return brief;
    } catch (Exception e) {
      logger.error("读取个人简介出错", e);
      throw new PsnException(e);
    }
  }

  private String getBriefFileName(Long psnId) throws PsnException {
    String fileName = String.format("brief-%s-%s.txt", 1, psnId);
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
  public List<Person> queryPersonForPsnHtml(List<Long> psnIds) throws PsnException {
    try {
      return personProfileDao.queryPersonForPsnHtml(psnIds);
    } catch (PsnInfoDaoException e) {
      logger.error("查询人员psnIds={}出现异常：{}", psnIds, e);
      throw new PsnException(e);
    }
  }

  @Override
  public String refreshRemoteAvatars(Long psnId, Integer sex, String avator) {

    String domain = "";
    try {
      domain = sysDomainConst.getSnsDomain();
      // 获取用户头像
      if (avator != null && !(avator.endsWith(ServiceConstants.DEFAULT_MAN_AVATARS)
          || avator.endsWith(ServiceConstants.DEFAULT_WOMAN_AVATARS))) {
        if (avator.startsWith("http://")) {
          return avator;
        }
        return domain + avator;
        // return avator;
      } else if (sex != null && sex == 0) {// 女性
        return domain + ServiceConstants.DEFAULT_WOMAN_AVATARS;
        // return ServiceConstants.DEFAULT_WOMAN_AVATARS;
      } else {
        return domain + ServiceConstants.DEFAULT_MAN_AVATARS;
        // return ServiceConstants.DEFAULT_MAN_AVATARS;
      }
    } catch (Exception e) {
      logger.error("读取个人信息出错", e);
      return domain + ServiceConstants.DEFAULT_MAN_AVATARS;
      // return ServiceConstants.DEFAULT_MAN_AVATARS;
    }
  }

  /**
   * 更新工作单位和头衔
   */
  @Override
  public int updateInsAndTitle(Long insId, String insName, Long personId, String title) throws PsnException {
    return personProfileDao.updateInsAndTitle(insId, insName, personId, title);

  }

  /**
   * 更新首要工作单位
   */
  @Override
  public int updateIns(Long insId, String insName, Long personId) throws PsnException {
    return this.personProfileDao.updateIns(insId, insName, personId);
  }

  /**
   * 取得个人基本信息
   */
  @Override
  public Person getPersonBaseInfo(Long personId) throws PsnException {
    try {
      Person person = this.getPerson(personId);
      if (person != null) {
        // 无头像，设置默认头像
        if (StringUtils.isBlank(person.getAvatars())) {
          person.setAvatars(this.refreshRemoteAvatars(person.getPersonId(), person.getSex(), null));
        }
        String brief = this.getPersonBrief(personId);
        person.setBrief(brief);
      }
      return person;
    } catch (Exception e) {
      throw new PsnException("读取个人基本信息出错,psnId=" + personId, e);
    }
  }

  @Override
  public int save(Person person) throws PsnException {
    try {
      personProfileDao.save(person);
      Long sid = psnSidDao.findSidByPsnId(person.getPersonId());
      if (sid == null) {
        // 生成SID
        sid = psnSidDao.getSidSequence();
        PsnSid psnSid = new PsnSid(person.getPersonId(), sid);
        psnSidDao.save(psnSid);
      }

      this.refreshComplete(person.getPersonId());
      // 更新人员solr信息
      personalManager.refreshPsnSolrInfoByTask(person.getPersonId());
      // 同步保存冗余
      int isPrivate = psnPrivateService.isPsnPrivate(person.getPersonId()) ? 1 : 0;
      person.setIsPrivate(isPrivate);
      this.syncSavePersonKnow(person);
      return 1;
    } catch (Exception e) {
      logger.error("保存个人信息出错", e);
      return 0;
    }
  }

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
    personKnowDao.saveOrUpdate(personKnow);
  }

  /**
   * 手机版主页 获取人员相关信息
   */
  @Override
  public void getBaseInfoForMobilHomepage(PsnHomepageMobileForm form) throws PsnException {
    Person psn = personProfileDao.getUnifiedPsnInfoByPsnId(form.getPsnId());
    // 删除头像链接中的域名部分，不然在生成二维码的时候有跨域问题
    if (psn != null && StringUtils.isNotBlank(psn.getAvatars())) {
      boolean isBase64 = false;
      if (psn.getAvatars().contains("http")) {// 网络的图片
        String imgUrl = GetImageStrFromUrl(psn.getAvatars());
        isBase64 = StringUtils.isNotBlank(imgUrl);
        form.setPsnAvatarsUrl(Objects.toString(imgUrl, "/resmod/smate-pc/img/logo_psndefault.png"));
      } else {
        form.setPsnAvatarsUrl(psn.getAvatars());
      }
      form.setPicIsBase64(isBase64);
    }
    form.setPerson(psn);
    PersonProfileForm psnPCForm = new PersonProfileForm();
    psnPCForm.setPsnInfo(psn);
    this.buildPsnTitolo(psnPCForm);
    PsnInfo psnInfo = form.getPsnInfo();
    if (psnInfo == null) {
      psnInfo = new PsnInfo();
    }
    psnInfo.setInsAndDep(psnPCForm.getInsAndDepInfo());
    psnInfo.setPosAndTitolo(psnPCForm.getPositionAndTitolo());
    form.setPsnInfo(psnInfo);
  }

  /*
   * 将一张网络图片转化成Base64字符串 图片转成Base64编码用于移动端解决跨域生成不了二维码中头像问题
   */
  private String GetImageStrFromUrl(String imgURL) {
    if (imgURL == null) {
      return "";
    }
    byte[] data = null;
    try {
      // 创建URL
      URL url = new URL(imgURL);
      // 创建链接
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setConnectTimeout(5 * 1000);
      InputStream inStream = conn.getInputStream();
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = inStream.read(buffer)) != -1) {
        outStream.write(buffer, 0, len);
      }
      inStream.close();
      data = outStream.toByteArray();// 得到图片的二进制数据
    } catch (FileNotFoundException e) {
      logger.error("将一张网络图片转化成Base64字符串出错，未找到网络图片，  imgUrl = " + imgURL);
    } catch (IOException e) {
      logger.error("将一张网络图片转化成Base64字符串出错， imgUrl = " + imgURL, e);
    }
    // 对字节数组Base64编码
    BASE64Encoder encoder = new BASE64Encoder();
    // 返回Base64编码过的字节数组字符串
    return data != null ? encoder.encode(data) : "";
  }

  @Override
  public Person getPersonInfoByPsnIdForFriend(Long psnId) throws PsnException {
    return personProfileDao.getPersonInfoByPsnIdForFriend(psnId);
  }

  @Override
  public String getPsnViewWorkHisInfo(Long psnId) throws PsnException {
    StringBuffer viewInfo = new StringBuffer();
    try {
      PsnWorkHistoryInsInfo psnWorkInfo = psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psnId);
      if (psnWorkInfo != null) {
        // 单位
        String insName = getNotBlankString(psnWorkInfo.getInsNameZh(), psnWorkInfo.getInsNameEn());
        // 部门
        String department = getNotBlankString(psnWorkInfo.getDepartmentZh(), psnWorkInfo.getDepartmentEn());
        // 职称
        String position = getNotBlankString(psnWorkInfo.getPositionZh(), psnWorkInfo.getPositionEn());

        viewInfo.append(insName);
        if (StringUtils.isNotBlank(department)) {
          viewInfo.append(StringUtils.isNotBlank(insName) ? ", " + department : department);
        }
        if (StringUtils.isNotBlank(position)) {
          if (StringUtils.isNotBlank(insName) || StringUtils.isNotBlank(department)) {
            viewInfo.append(", " + position);
          } else {
            viewInfo.append(position);
          }
        }
      }
    } catch (Exception e) {
      logger.error("构造人员工作单位信息出错，psnId=" + psnId);
      throw new PsnException("构造人员工作单位信息出错，psnId=" + psnId, e);
    }
    return viewInfo.toString();
  }

  /**
   * 优先获取中文名称，中文名为空则获取英文名称-------人员列表中人员姓名下面的工作经历信息显示用
   *
   * @param zhName
   * @param eName
   * @return
   */
  private String getNotBlankString(String zhName, String eName) {
    String viewEName = StringUtils.isNotBlank(eName) ? eName : "";
    return StringUtils.isNotBlank(zhName) ? zhName : viewEName;

  }

  public String getDomain() throws PsnException {
    return sysDomainConst.getSnsDomain();
  }

  /**
   * 更新信息完整度百分比.
   *
   * 完整度标准:
   *
   * 工作经历 20% 教育经历 10% 姓名20%.研究领域10%
   *
   * 联系方式 15%（填写一项） 所在地 5% .
   *
   * 头像 5% 熟悉学科10% 个人简介5%.
   *
   * @param psnId
   * @throws PsnException
   */
  @Override
  public Person refreshCompleteByPsnId(Long psnId) throws PsnException {
    Person person = null;
    try {
      int complete = 0;
      person = this.getPerson(psnId);
      if (person == null)
        return person;
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
    return person;
  }

  /**
   * 根据人员ID和语言环境获取人员姓名进行显示
   */
  @Override
  public String getPsnNameByIdAndLocal(Long psnId, String local) {
    Person psn = this.personProfileDao.getPsnAllName(psnId);
    return this.getPsnName(psn, local);
  }

  /**
   * 获取名字头像的信息queryPsnNameAndAvatars
   */
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
  public PersonProfileForm getUnifiedPsnInfoByPsnId(PersonProfileForm form) {
    Person psn = this.personProfileDao.getUnifiedPsnInfoByPsnId(form.getPsnId());
    String languageVersion = LocaleContextHolder.getLocale().toString();
    if (form.getPsnId() != null) {
      form.setDes3PsnId(Des3Utils.encodeToDes3(form.getPsnId().toString()));
    }
    if (psn != null) {
      if (Locale.CHINA.toString().equals(languageVersion)) {

        form.setName(StringUtils.isNotBlank(psn.getName()) ? psn.getName() : psn.getEname());
      } else {
        form.setName(StringUtils.isNotBlank(psn.getEname()) ? psn.getEname() : psn.getName());
      }
      form.setPsnInfo(psn);
      if (form.getNeedStatistics()) {
        PsnStatistics psta = this.psnStatisticsDao.get(form.getPsnId());
        if (psta != null) {
          form.setPubSum(psta.getPubSum());
          form.setPrjSum(psta.getPrjSum());
          form.sethIndex(psta.getHindex());
          form.setCitedSum(psta.getCitedSum());
          form.setOpenPrjSum(psta.getOpenPrjSum());
          form.setReadSum(psta.getVisitSum() == null ? 0L : psta.getVisitSum());
        }
        // form.setReadSum(readStatisticsDao.findPsnReadSumByPsnId(form.getPsnId()));
        form.setDownloadCount(downloadCollectStatisticsDao.countPsnResourceDownload(form.getPsnId()));
      }
    }
    return form;
  }

  @Override
  public PersonProfileForm getUnifiedPsnInfoByForm(PersonProfileForm form) {
    // 获取人员基本信息
    form = this.getUnifiedPsnInfoByPsnId(form);
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(form.getPsnId());
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      form.setPsnProfileUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl());
    }
    PersonOpen personOpen = personOpenDao.getPersonOpenByPsnId(form.getPsnId());
    if (personOpen != null) {
      form.setPsnOpenId(personOpen.getOpenId());
    }
    String zhPsnRegionName = "";
    String enPsnRegionName = "";
    if (form.getPsnInfo() != null && form.getPsnInfo().getRegionId() != null) {
      Long psnRegionId = form.getPsnInfo().getRegionId();
      // 根据地区ID和上一级地区ID构建人员所在地区信息
      int count = 0;
      while (psnRegionId != null) {
        count++;
        if (count > 5) {
          break;
        }
        ConstRegion cre = this.constRegionDao.findRegionNameById(psnRegionId);
        if (cre != null) {
          psnRegionId = cre.getSuperRegionId();
          if (StringUtils.isNotBlank(zhPsnRegionName)) {
            zhPsnRegionName = cre.getZhName() + ", " + zhPsnRegionName;
          } else {
            zhPsnRegionName = cre.getZhName();
          }
          if (StringUtils.isNotBlank(enPsnRegionName)) {
            enPsnRegionName = cre.getEnName() + ", " + enPsnRegionName;
          } else {
            enPsnRegionName = cre.getEnName();
          }
        } else {
          break;
        }
      }
    } else {
      zhPsnRegionName = regionName;
      enPsnRegionName = regionNameEn;
      if (form.getPsnInfo() != null) {
        form.getPsnInfo().setRegionId(156L);
      }
    }
    form.setZhPsnRegionInfo(zhPsnRegionName);
    form.setEnPsnRegionInfo(enPsnRegionName);
    buildPsnTitolo(form);
    this.buildPsnInfoByLanguage(form);
    return form;
  }

  /**
   * 构建人员显示信息-------单位+部门、职称+头衔
   *
   * @param psn
   */
  private void buildPsnTitolo(PersonProfileForm form) {
    Person psn = form.getPsnInfo();
    if (psn != null) {
      // 获取单位名称
      form.setInsName(psn.getInsName());
      if (StringUtils.isBlank(form.getInsName()) && psn.getInsId() != null) {
        Institution ins = institutionDao.findInsName(psn.getInsId());
        if (ins != null) {
          form.setInsName(StringUtils.isNotBlank(ins.getZhName()) ? ins.getZhName() : ins.getEnName());
        }
      }
      // 获取部门名
      form.setDepartment(psn.getDepartment());
      // 构建单位和部门信息
      if (StringUtils.isBlank(form.getInsName()) || StringUtils.isBlank(form.getDepartment())) {

        form.setInsAndDepInfo((StringUtils.isBlank(form.getInsName()) ? "" : form.getInsName())
            + (StringUtils.isBlank(form.getDepartment()) ? "" : form.getDepartment()));
      } else {
        form.setInsAndDepInfo(form.getInsName() + ", " + form.getDepartment());
      }
      // 获取职称信息
      form.setPosition(psn.getPosition());
      // 获取头衔信息
      form.setTitolo(psn.getTitolo());
      // 构建职称和头衔信息
      if (StringUtils.isBlank(form.getPosition()) || StringUtils.isBlank(form.getTitolo())) {
        form.setPositionAndTitolo((StringUtils.isBlank(form.getPosition()) ? "" : form.getPosition())
            + (StringUtils.isBlank(form.getTitolo()) ? "" : form.getTitolo()));
      } else {
        form.setPositionAndTitolo(form.getPosition() + ", " + form.getTitolo());
      }
    }
  }

  @Override
  public void buildPsnInfoConfig(PersonProfileForm form) throws PsnCnfException {
    try {
      PsnCnfBuild cnfBuild = cnfBuildService.get(form.getPsnId());
      // 个人简介权限
      if (cnfBuild != null && cnfBuild.getCnfMoudle() != null) {
        form.setCnfAnyMode(cnfBuild.getCnfMoudle().getAnyMod());
      }
      form.setCnfBuild(cnfBuild);
    } catch (PsnCnfException e) {
      throw new PsnCnfException("个人配置信息为空，psnId=" + form.getPsnId());
    } catch (Exception e) {
      logger.error("构建个人信息模块显示权限出错， psnId = " + form.getPsnId(), e);
    }
  }

  @Override
  public void savePersonBrief(String brief, Long psnId) {
    if (brief == null) {
      brief = "";
    }
    try {
      // 加入缓存
      this.psnCacheService.put(PersonManager.PSN_BRIEF_CACHE, PersonManager.PSN_BRIEF_CACHE_TIMEOUT, psnId.toString(),
          brief);
      String briefRoot = this.getRootFolder() + "/brief";
      this.fileService.writeText(brief, briefRoot, getBriefFileName(psnId), "utf-8");
    } catch (Exception e) {
      logger.error("写个人简介出错， psnId=" + psnId, e);
    }
  }

  @Override
  public Person findPersonContactInfo(Long psnId) {
    return this.personProfileDao.findPersonContactInfoById(psnId);

  }

  @Override
  public PersonProfileForm updatePersonContactInfo(PersonProfileForm form) {
    try {
      // 查看权限 & 数据内容有无
      form.setAnyUserEmail(PsnCnfConst.ALLOWS);
      form.setAnyUserTel(PsnCnfConst.ALLOWS);
      Integer result = 1;
      result = saveContactCnf(form);
      if (result == 1) {
        // 往psn_email新增或更新数据，将新的email设为首要邮箱-----首要邮箱改变了才执行
        if (!form.getPsnFirstEmail()) {
          result = updatePsnFirstEmail(form.getPsnId(), form.getEmail());
        }
        if (result == 1) {
          // 更新sys_user和person表中联系信息
          result = updateContaceToUserAndPsn(form);
          if (result == 1) {
            // 数据同步
            RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(form.getPsnId());
            if (rsp == null) {
              rsp = new RcmdSyncPsnInfo(form.getPsnId());
            }
            rsp.setContactFlag(1);
            rsp.setEmailFlag(1);
            rcmdSyncPsnInfoDao.save(rsp);
            // 更新人员solr信息
            // psnSolrInfoModifyService.updateSolrPsnInfo(form.getPsnId());
            personalManager.refreshPsnSolrInfoByTask(form.getPsnId());
            // 更新sie的人员信息
            personalManager.updateSIEPersonInfo(form.getPsnId());
            // 发送邮件--首要邮箱改变了才发送邮件
            if (!form.getPsnFirstEmail()) {
              psnFirstEmailService.restSendUpdateFirstEmail(form.getPsnInfo(), false);
            }
            // 刷新psn_html
            // psnHtmlRefreshService.updatePsnHtmlRefresh(form.getPsnId());
          }
        }
      }
      if (result != 1) {
        return null;
      }
    } catch (Exception e) {
      logger.error("保存联系信息出错， psnId = ", form.getPsnId(), e);
      return null;
    }
    return form;
  }


  @Override
  public void savePsnInfoNew(PersonProfileForm form) throws PsnException {
    String result = "";
    try {
      if (form.getPsnId() != null) {
        Person psn = personProfileDao.get(form.getPsnId());
        if (psn != null) {
          // 保存到person表中
          psn = this.buildPsnByForm(psn, form);
          // 不是首要单位的设置为首要单位
          this.setWorkHistoryToPrimary(psn, form);
          personProfileDao.save(psn);
          // 更新人员信息完整度
          this.refreshComplete(form.getPsnId());
          // 同步到其他表中：发件箱、收件箱、评论等
          // 同步保存冗余
          int isPrivate = psnPrivateService.isPsnPrivate(psn.getPersonId()) ? 1 : 0;
          psn.setIsPrivate(isPrivate);
          this.syncSavePersonKnow(psn);
          // 更新psnHtml
          // psnHtmlRefreshService.updatePsnHtmlRefresh(psn.getPersonId());

          result = "success";
        }
      } else {
        result = "noId";
      }
    } catch (Exception e) {
      logger.error("保存人员信息出错，psnId = " + form.getPsnId(), e);
      result = "error";
    }
    // return result;
  }

  private Person buildPsnByForm(Person person, PersonProfileForm form) {
    if (person == null) {
      person = new Person();
    }
    // 单位部门职称都是通过勾选工作经历得来的，会传workId
    if (form.getWorkId() != null) {
      WorkHistory work = workHistoryDao.get(form.getWorkId());
      if (work != null) {
        form.setInsId(work.getInsId());
        form.setInsName(work.getInsName());
        form.setDepartment(work.getDepartment());
        form.setPosition(work.getPosition());
        if (form.getRegionId() == null || form.getRegionId() == 0) {
          if (work.getInsId() != null) {
            Long regionId = institutionDao.findInsRegionId(work.getInsId());
            if (regionId != null) {
              form.setRegionId(regionId);
            } else {
              // 默认中国
              form.setRegionId(156L);
            }
          } else {
            form.setRegionId(156L);
          }
        }
      }
    }
    // person.setDegreeName(form.getDegreeName());
    // 职务
    String position = form.getPosition() == null ? null : form.getPosition().trim();
    if (StringUtils.isNotBlank(position)) {
      ConstPosition constPosition = constPositionService.getPosByName(position);
      if (constPosition != null) {
        form.setPosId(constPosition.getId());
        form.setPosGrades(constPosition.getGrades());
      } else {
        form.setPosId(null);
        form.setPosGrades(null);
      }
    } else {
      form.setPosId(null);
      form.setPosGrades(null);
    }
    person.setInsId(form.getInsId());
    person.setInsName(form.getInsName());
    person.setDepartment(form.getDepartment());
    person.setPosition(form.getPosition());
    person.setPosId(form.getPosId());
    person.setPosGrades(form.getPosGrades());
    person.setTitolo(form.getTitolo());
    person.setRegionId(form.getRegionId());
    // person.setSex(form.getSex());
    // person.setAvatars(this.refreshRemoteAvatars(person.getPersonId(),
    // person.getSex(), person.getAvatars()));
    // person.setEname(form.getFirstName() + " " + form.getLastName());
    return person;
  }

  /**
   * 保存联系信息的权限
   *
   * @param form
   * @param anyUserEmail
   * @param anyUserTel
   * @return
   */
  public Integer saveContactCnf(PersonProfileForm form) {
    try {
      PsnConfigContact cnfContact = new PsnConfigContact();
      // 查看权限 & 数据内容有无
      cnfContact.setAnyUserEmail(form.getAnyUserEmail());
      // boolean hasTextEmail = StringUtils.isNotBlank(form.getEmail());
      cnfContact.setAnyViewEmail(cnfContact.getAnyUserEmail() & PsnCnfConst.ALLOWS);
      cnfContact.setAnyUserTel(form.getAnyUserTel());
      // boolean hasTextTel = StringUtils.isNotBlank(form.getTel());
      cnfContact.setAnyViewTel(cnfContact.getAnyUserTel() & PsnCnfConst.ALLOWS);
      psnCnfService.save(SecurityUtils.getCurrentUserId(), cnfContact);
      return 1;
    } catch (Exception e) {
      logger.error("保存人员联系信息出错，psnId=" + form.getPsnId(), e);
    }
    return 0;
  }

  // private String saveContactToUser(Person psn) {
  // Map result = new HashMap();
  // if (psn != null) {
  // Person newPsn = this.personProfileDao.get(psn.getPersonId());
  // if (newPsn != null) {
  // newPsn.setEmail(psn.getEmail());
  // newPsn.setTel(psn.getTel());
  // this.personProfileDao.save(newPsn);
  // RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psn.getPersonId());
  // if (rsp == null) {
  // rsp = new RcmdSyncPsnInfo(psn.getPersonId());
  // }
  // rsp.setContactFlag(1);
  // rcmdSyncPsnInfoDao.save(rsp);
  // result.put("result", "success");
  // } else {
  // logger.error("保存人员联系信息，没有找到对应的人员记录， psnId=" + psn.getPersonId() +
  // ", email=" + psn.getEmail()
  // + ", tel=" + psn.getTel());
  // result.put("result", "error");
  // }
  // } else {
  // logger.error("保存人员联系信息，人员对象参数为空");
  // result.put("result", "error");
  // }
  // return result;
  // }

  /**
   * 更新首要邮箱
   *
   * @param psnId
   * @param email
   * @return
   */
  private Integer updatePsnFirstEmail(Long psnId, String email) {
    try {
      personEmailDao.clearFirstEmail(psnId);
      PersonEmailRegister psnEmail = personEmailDao.getPsnEmail(psnId, email);
      if (psnEmail == null) {
        psnEmail = new PersonEmailRegister();
        psnEmail.setPerson(personProfileDao.get(psnId));
        psnEmail.setIsVerify(0L);
        psnEmail.setFirstMail(1L);
        psnEmail.setLoginMail(0L);
        psnEmail.setEmail(email);
        // 设置@左边和右边部分的值
      } else {
        psnEmail.setFirstMail(1L);
      }
      personEmailDao.save(psnEmail);
      pdwhAddrPsnUpdateRecordDao.deleteRecordByPsnId(psnId);
      PdwhAddrPsnUpdateRecord record = new PdwhAddrPsnUpdateRecord(psnId, 2, 0);
      pdwhAddrPsnUpdateRecordDao.save(record);
      return 1;
    } catch (Exception e) {
      logger.error("更新首要邮箱出错， psnId=" + psnId + ", email=" + email, e);
      return 0;
    }
  }

  /**
   * 保存联系信息到sys_user和person表
   *
   * @param form
   * @return
   */
  private Integer updateContaceToUserAndPsn(PersonProfileForm form) {
    try {
      User user = userService.findUserById(form.getPsnId());
      if (user != null) {
        user.setEmail(form.getEmail());
        userDao.save(user);
      } else {
        form.setErrorMsg("can not find user");
        return 0;
      }
      // 更新person表中email字段值
      personProfileDao.updatePsnContactInfo(form.getPsnId(), form.getEmail(), form.getTel());
      Person psn = personProfileDao.getPersonForEmail(form.getPsnId());
      form.setPsnInfo(psn);
      return 1;
    } catch (Exception e) {
      logger.error("保存联系信息到sys_user和person表出错，psnId=" + form.getPsnId(), e);
      return 0;
    }
  }

  @Override
  public Integer saveAvatars(Long psnId, String path) {
    try {
      // 取的个人旧的数据
      Person person = personProfileDao.get(psnId);
      // path = this.refreshRemoteAvatars(psnId, person.getSex(), path);
      // 地址未改变，不需要同步与修改
      // if (path.equals(person.getAvatars())) {
      // return 1;
      // }
      person.setAvatars(path);
      // 把新修改的数据保存至数据库
      personProfileDao.save(person);
      // 同步rcmd
      RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
      if (rsp == null) {
        rsp = new RcmdSyncPsnInfo(psnId);
      }
      rsp.setAdditinfoFlag(1);
      rcmdSyncPsnInfoDao.save(rsp);
      // 同步到其他节点
      int isPrivate = psnPrivateService.isPsnPrivate(person.getPersonId()) ? 1 : 0;
      person.setIsPrivate(isPrivate);
      this.saveToBatchJobs(psnId);
      // 更新信息完整度.
      this.refreshComplete(psnId);
      // 更新人员solr信息
      // psnSolrInfoModifyService.updateSolrPsnInfo(psnId);
      personalManager.refreshPsnSolrInfoByTask(psnId);
      // 更新sie的人员信息
      personalManager.updateSIEPersonInfo(psnId);
      // 更新人员检索
      /*
       * psnScoreService.updatePsnScore(psnId, PsnScoreConstants.PSN_AVATAR);
       */
      return 1;
    } catch (Exception e) {
      logger.error("保存用户头像 出错", e);
      return 0;
    }
  }

  public void saveToBatchJobs(Long psnId) throws ServiceException {
    try {
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.PSN_AVATARS_SYNC,
          BatchJobUtil.getContext(psnId), BatchWeightEnum.B.toString());
      batchJobsService.saveJob(job);
    } catch (Exception e) {
      throw new ServiceException(e);
    }

  }

  public void saveNameToBatchJobs(Long psnId) throws ServiceException {
    try {
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.PSN_AVATARS_SYNC,
          BatchJobUtil.getContext(psnId), BatchWeightEnum.B.toString());
      batchJobsService.saveJob(job);
    } catch (Exception e) {
      throw new ServiceException(e);
    }

  }

  @Override
  public void setWorkHistoryToPrimary(Person psn, PersonProfileForm form) {
    if (form.getPsnId() != null || form.getWorkId() != null) {
      WorkHistory workHistory = workHistoryDao.findPsnWorkHistory(form.getPsnId(), form.getWorkId());
      if (workHistory != null) {
        if ((workHistory.getIsPrimary() != null && workHistory.getIsPrimary().longValue() != 1)
            || workHistory.getIsPrimary() == null) {
          RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(form.getPsnId());
          PsnRefreshUserInfo psnRefInfo = psnRefreshUserInfoDao.get(form.getPsnId());
          if (rsp == null) {
            rsp = new RcmdSyncPsnInfo(form.getPsnId());
          }
          if (psnRefInfo == null) {
            psnRefInfo = new PsnRefreshUserInfo(form.getPsnId());
          }
          if (StringUtils.isNotBlank(form.getPosition())) {
            String position = form.getPosition().trim();
            if (!position.equalsIgnoreCase(psn.getPosition())) {
              rsp.setExperienceFlag(1);
              psnRefInfo.setDegree(1);
              psnRefInfo.setPosition(1);
            }
          }
          // 更新所在地区
          rsp.setAdditinfoFlag(1);
          // 首要工作单位
          rsp.setInsFlag(1);
          psnRefInfo.setIns(1);
          rsp.setWorkFlag(1);
          rcmdSyncPsnInfoDao.save(rsp);
          psnRefreshUserInfoDao.save(psnRefInfo);
          workHistoryDao.updateIsPrimary(form.getPsnId());
          educationHistoryDao.updateEduIsPrimary(form.getPsnId());
          // 设置为首要单位
          workHistory.setIsPrimary(1L);
          workHistoryDao.save(workHistory);
          workHistoryInsInfoService.updateWorkHistoryInsInfo(workHistory, 1);
        }
      }
    }
  }

  /**
   * 根据PsnId获取构建机构信息
   *
   * @param psnIds
   * @return
   */
  @Override
  public List<Map<String, String>> findInsNamesBypsnIds(List<Long> psnIds) throws ServiceException {
    List<Long> insIds = new ArrayList<Long>();
    // 获取insId
    List<Map<String, Long>> insIdsCount = findInsIdByPsnId(psnIds);
    for (int i = 0; i < insIdsCount.size(); i++) {
      insIds.add(insIdsCount.get(i).get("insId"));
    }
    // 根据insId获取机构名字
    return findInsNamesByinsIds(insIds);
  }

  /**
   * 根据insId获取构建机构信息
   *
   * @param insIds
   * @return
   */
  @Override
  public List<Map<String, String>> findInsNamesByinsIds(List<Long> insIds) throws ServiceException {
    String languageVersion = LocaleContextHolder.getLocale().toString();
    List<Institution> institutions = institutionDao.findBitchInsName(insIds);
    List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
    if (CollectionUtils.isNotEmpty(institutions)) {
      for (Long insId : insIds) {
        for (Institution institution : institutions) {
          if (insId.equals(institution.getId())) {
            Map<String, String> map = new HashMap<String, String>();
            if (Locale.CHINA.toString().equals(languageVersion)) {
              if (StringUtils.isNotBlank(institution.getZhName())) {
                map.put("name", institution.getZhName());
              } else {
                map.put("name", institution.getEnName());
              }
              map.put("insId", institution.getId().toString());
            } else {
              if (StringUtils.isNotBlank(institution.getEnName())) {
                map.put("name", institution.getEnName());
              } else {
                map.put("name", institution.getZhName());
              }
              map.put("insId", institution.getId().toString());
            }
            mapList.add(map);
          }
        }
      }
    }
    return mapList;
  }

  @Override
  /**
   * 根据Id获取地区名字等
   *
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public List<Map<String, String>> findInsNamesByregIds(List<Long> psnIds) throws ServiceException {
    String languageVersion = LocaleContextHolder.getLocale().toString();
    List<Long> regIds = new ArrayList<Long>();
    List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
    List<ConstRegion> constRegions = new ArrayList<ConstRegion>();
    // 获取regId和它的统计数
    List<Map<String, Object>> regIdsCount = findRegIdByPsnId(psnIds);
    if (!regIdsCount.isEmpty()) {
      for (int i = 0; i < regIdsCount.size(); i++) {
        regIds.add((Long) regIdsCount.get(i).get("regionId"));
      }
      // 根据regId取地区
      constRegions = constRegionDao.findBitchRegionName(regIds);
    }
    if (CollectionUtils.isNotEmpty(constRegions)) {
      for (ConstRegion constRegion : constRegions) {
        Map<String, String> map = new HashMap<String, String>();
        if (Locale.CHINA.toString().equals(languageVersion)) {
          if (StringUtils.isNotBlank(constRegion.getZhName())) {
            map.put("regionName", constRegion.getZhName());
          } else {
            map.put("regionName", constRegion.getEnName());
          }
          map.put("regionId", constRegion.getId().toString());
        } else {
          if (StringUtils.isNotBlank(constRegion.getEnName())) {
            map.put("regionName", constRegion.getEnName());
          } else {
            map.put("regionName", constRegion.getZhName());
          }
          map.put("regionId", constRegion.getId().toString());
        }
        mapList.add(map);
      }
    }
    return mapList;
  }

  /**
   * 根据psnId获取regsId和regId数量
   */
  @Override
  public List<Map<String, Object>> findRegIdByPsnId(List<Long> psnIds) throws ServiceException {
    List<Map<String, Object>> regNameCount = new ArrayList<Map<String, Object>>();
    if (!psnIds.isEmpty()) {
      regNameCount = personProfileDao.getRegListInfoByPsnIds(psnIds);
    }
    return regNameCount;
  }

  /**
   * 根据psnId获取insId和insId数量
   */
  @Override
  public List<Map<String, Long>> findInsIdByPsnId(List<Long> psnIds) throws ServiceException {
    List<Map<String, Long>> InsNameCount = new ArrayList<Map<String, Long>>();
    if (!psnIds.isEmpty()) {
      InsNameCount = personProfileDao.getInsListInfoByPsnIds(psnIds);
    }
    return InsNameCount;
  }

  @Override
  public List<Map<String, Object>> sortPsnByReg(PsnListViewForm form) throws Exception {
    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> regCountMap = personDao.sortPersonByRegId(form.getPsnId());
    if (CollectionUtils.isNotEmpty(regCountMap)) {
      for (Map<String, Object> map : regCountMap) {
        ConstRegion constRegion = constRegionDao.findRegionNameById(NumberUtils.toLong(map.get("REGIONID") + ""));
        if (constRegion != null) {
          Map<String, Object> regMap = new HashMap<String, Object>();
          regMap.put("regionId", map.get("REGIONID"));
          Locale locale = LocaleContextHolder.getLocale();
          if (Locale.US.equals(locale)) {
            regMap.put("regionName", constRegion.getEnName());
          } else {
            regMap.put("regionName", constRegion.getZhName());
          }
          listMap.add(regMap);
        }
      }
    }
    return listMap;
  }

  /**
   * 获取机构
   */
  @Override
  public List<Map<String, String>> findInsNames(Long psnId) throws ServiceException {
    List<WorkHistory> workHistory = workHistoryDao.getPsnWorkHistory(psnId);
    List<WorkHistory> listWorkHistory = new ArrayList<WorkHistory>();
    Set<String> set = new HashSet<String>();
    for (WorkHistory workHistorys : workHistory) {
      if (!set.add(workHistorys.getInsId().toString())) {
        listWorkHistory.add(workHistorys);
      }
    }
    workHistory.removeAll(listWorkHistory);
    List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
    List<String> insIdList = new ArrayList<String>();// 用于保存所有机构Id来排除掉重复的
    if (workHistory.size() < 5) {
      List<EducationHistory> educationHistory = educationHistoryDao.getPsnEduHistory(psnId);
      List<EducationHistory> listEducationHistory = new ArrayList<EducationHistory>();
      Set<String> eduSet = new HashSet<String>();
      for (EducationHistory educationHistorys : educationHistory) {
        if (!eduSet.add(educationHistorys.getInsId().toString())) {
          listEducationHistory.add(educationHistorys);
        }
      }
      educationHistory.removeAll(listEducationHistory);

      for (WorkHistory workHistorys : workHistory) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("insId", workHistorys.getInsId().toString());
        map.put("name", workHistorys.getInsName());
        insIdList.add(workHistorys.getInsId().toString());
        mapList.add(map);
      }
      if (workHistory.size() + educationHistory.size() < 5) {
        for (EducationHistory educationHistorys : educationHistory) {
          if (!insIdList.contains(educationHistorys.getInsId().toString())) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("insId", educationHistorys.getInsId().toString());
            map.put("name", educationHistorys.getInsName());
            insIdList.add(educationHistorys.getInsId().toString());
            mapList.add(map);
          }
        }
        getFriendIns(psnId, mapList, insIdList);

      } else {
        for (EducationHistory educationHistorys : educationHistory) {
          if (!insIdList.contains(educationHistorys.getInsId().toString())) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("insId", educationHistorys.getInsId().toString());
            map.put("name", educationHistorys.getInsName());
            insIdList.add(educationHistorys.getInsId().toString());
            mapList.add(map);
          }
          if (mapList.size() >= 5) {
            break;
          }
        }
        if (mapList.size() < 5) {
          getFriendIns(psnId, mapList, insIdList);
        }
      }
    } else {
      for (int i = 0; i < 5; i++) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("insId", workHistory.get(i).getInsId().toString());
        map.put("name", workHistory.get(i).getInsName());
        insIdList.add(workHistory.get(i).getInsId().toString());
        mapList.add(map);
      }
    }
    // 根据语言环境切换中英文机构名称
    List<Long> insIds = new ArrayList<>();
    for (String insId : insIdList) {
      if (insId != null && !insId.equals("")) {
        insIds.add(Long.parseLong(insId));
      }
    }
    List<Map<String, String>> insNames = findInsNamesByinsIds(insIds);
    for (int i = 0; i < insIds.size(); i++) {
      for (Map<String, String> insName : insNames) {
        if (insName.get("insId").equals(mapList.get(i).get("insId"))) {
          mapList.get(i).put("name", insName.get("name"));
        }
      }
    }
    return mapList;
  }

  /**
   * 从好友中获取机构
   */
  public void getFriendIns(Long psnId, List<Map<String, String>> mapList, List<String> insIdList)
      throws ServiceException {
    List<Long> friendIds = friendService.getFriendListByPsnId(psnId);
    if (!CollectionUtils.isEmpty(friendIds)) {
      List<Map<String, String>> friendIns = findInsNamesBypsnIds(friendIds);
      for (Map<String, String> friendInstution : friendIns) {
        if (!insIdList.contains(friendInstution.get("insId"))) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("insId", friendInstution.get("insId").toString());
          map.put("name", friendInstution.get("name"));
          mapList.add(map);
        }
        if (mapList.size() >= 5) {
          break;
        }
      }
    }

  }

  /**
   * 获取地区
   */
  @Override
  public List<Map<String, String>> findRegNames(Long psnId) throws ServiceException {
    List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
    List<Map<String, String>> regIdReNames = findInsNames(psnId);
    List<Long> regIds = new ArrayList<Long>();
    List<String> regIdList = new ArrayList<String>();// 用于排除和好友那边重复的地区
    Locale locale = LocaleContextHolder.getLocale();
    if (!regIdReNames.isEmpty()) {
      for (Map<String, String> regIdReName : regIdReNames) {
        regIds.add(Long.valueOf(regIdReName.get("insId")));
      }
      List<Long> regionIds = institutionDao.findBitchRegionId(regIds);
      List<ConstRegion> constRegions = constRegionDao.findBitchRegionName(regionIds);
      List<ConstRegion> listConstRegion = new ArrayList<ConstRegion>();
      Set<String> set = new HashSet<String>();
      for (ConstRegion constRegion : constRegions) {
        if (!set.add(constRegion.getId().toString())) {
          listConstRegion.add(constRegion);
        }
      }
      constRegions.removeAll(listConstRegion);
      if (constRegions.size() < 5) {
        List<Long> friendIds = friendService.getFriendListByPsnId(psnId);
        List<Map<String, String>> friendRegs = new ArrayList<Map<String, String>>();
        if (CollectionUtils.isNotEmpty(friendIds)) {
          friendRegs = findInsNamesByregIds(friendIds);
        }
        if (friendRegs.size() + constRegions.size() >= 5) {
          for (ConstRegion constRegion : constRegions) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("regionId", constRegion.getId().toString());
            if (Locale.US.equals(locale)) {
              map.put("regionName", constRegion.getEnName());
            } else {
              map.put("regionName", constRegion.getZhName());
            }
            regIdList.add(constRegion.getId().toString());
            mapList.add(map);
          }
          for (int i = 0; mapList.size() < 5 && i < friendRegs.size(); i++) {
            if (!regIdList.contains(friendRegs.get(i).get("regionId").toString())) {
              Map<String, String> map = new HashMap<String, String>();
              map.put("regionId", friendRegs.get(i).get("regionId").toString());
              map.put("regionName", friendRegs.get(i).get("regionName"));
              mapList.add(map);
            }
          }
        } else if (friendRegs.size() + constRegions.size() < 5) {
          for (ConstRegion constRegion : constRegions) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("regionId", constRegion.getId().toString());
            if (Locale.US.equals(locale)) {
              map.put("regionName", constRegion.getEnName());
            } else {
              map.put("regionName", constRegion.getZhName());
            }
            regIdList.add(constRegion.getId().toString());
            mapList.add(map);
          }
          for (Map<String, String> friendReg : friendRegs) {
            if (!regIdList.contains(friendReg.get("regionId").toString())) {
              Map<String, String> map = new HashMap<String, String>();
              map.put("regionId", friendReg.get("regionId").toString());
              map.put("regionName", friendReg.get("regionName"));
              mapList.add(map);
            }
          }

        }

      } else {
        for (int i = 0; i < 5; i++) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("regionId", constRegions.get(i).getId().toString());
          if (Locale.US.equals(locale)) {
            map.put("regionName", constRegions.get(i).getEnName());
          } else {
            map.put("regionName", constRegions.get(i).getZhName());
          }
          mapList.add(map);
        }
      }
    }
    return mapList;
  }

  @Override
  public void buildPsnAvatars(List<PersonSearch> psnList) throws ServiceException {
    List<Long> psnIds = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(psnList)) {
      for (PersonSearch ps : psnList) {
        psnIds.add(ps.getPsnId());
      }
    }
    List<Person> pList = personProfileDao.getPsnAvatarsByPsnIds(psnIds);
    if (CollectionUtils.isNotEmpty(pList)) {
      for (PersonSearch ps : psnList) {
        for (Person psn : pList) {
          if (psn.getPersonId().equals(ps.getPsnId())) {
            ps.setAvatars(psn.getAvatars());
          }
        }
      }
    }
  }

  @Override
  public Page<PersonSearch> matchPsnByEmail(Page<PersonSearch> page, String searchString) throws PsnException {
    List<PersonSearch> psnInfoList = new ArrayList<PersonSearch>();
    // 匹配账号
    Page<Long> longPage = new Page<Long>();
    try {
      if (page != null) {
        longPage.setPageNo(page.getPageNo());
        longPage.setPageSize(page.getPageSize());
      }
      if (StringUtils.isNotBlank(searchString)) {
        longPage = userDao.matchPsnByEmail(longPage, searchString, null);
      }
      if (CollectionUtils.isNotEmpty(longPage.getResult())) {
        // WCW 20171028 不为空时设置总页数,totalPages的setter和getter方法是个坑
        page.setTotalCount(longPage.getTotalCount());
        page.setTotalPages((long) Math.ceil((double) longPage.getTotalCount() / longPage.getPageSize()));
        // 查找人员信息
        List<Person> psnList = personProfileDao.personByPsnIdsList(longPage.getResult());
        if (CollectionUtils.isNotEmpty(psnList)) {
          String locale = LocaleContextHolder.getLocale().toString();
          String name = "";
          for (Person psn : psnList) {
            PersonSearch psnInfo = new PersonSearch();
            psnInfo.setAvatars(psn.getAvatars());
            if ("zh_CN".equals(locale)) {
              name = StringUtils.isNotBlank(psn.getName()) ? psn.getName() : psn.getEname();
            } else {
              name = StringUtils.isNotBlank(psn.getEname()) ? psn.getEname() : psn.getName();
            }
            psnInfo.setName(name);
            psnInfo.setInsName(psn.getInsName());
            psnInfo.setPsnId(psn.getPersonId());
            psnInfo.setDes3PsnId(psn.getPersonDes3Id());
            psnInfoList.add(psnInfo);
          }
        }
      }
    } catch (Exception e) {
      logger.error("发现好友，匹配人员账号出错 searchString = " + searchString, e);
      throw new PsnException(e);
    }
    page.setResult(psnInfoList);
    return page;
  }

  /**
   * 构建地区和机构,科技领域的数据回显
   */
  @Override
  public String getRecommendInsReg(List<Long> psnIds, PsnListViewForm form) throws ServiceException {
    dataTypeConversion(form);
    List<Map<String, String>> insIdsCount = getRecommendInsCount(psnIds, form);
    List<Map<String, String>> regIdsCount = getRecommendRegCount(psnIds, form);
    List<Map<String, String>> scienceAreaCount = getRecommendAreaCount(psnIds, form);
    List<Map<String, Object>> listCount = new ArrayList<Map<String, Object>>();
    Map<String, Object> recommendCount = new HashMap<String, Object>();
    recommendCount.put("insId", insIdsCount);
    recommendCount.put("regionId", regIdsCount);
    recommendCount.put("scienceAreaId", scienceAreaCount);
    listCount.add(recommendCount);
    String count = JacksonUtils.listToJsonStr(listCount);
    count = count.replace("[]", "{}");
    count = count.replace("[", "");
    count = count.replace("]", "");

    return count;
  }

  /**
   * 处理接受的数据类型转化
   *
   * @param form
   */
  public void dataTypeConversion(PsnListViewForm form) {

    // 解析传过来的json数据
    if (!form.getAllFilterValues().isEmpty()) {
      String allFilterValues = "[" + form.getAllFilterValues() + "]";
      List<Map<String, String>> paramList = JacksonUtils.jsonListUnSerializer(allFilterValues);
      for (Map<String, String> map : paramList) {
        String insId = map.get("insId").toString();
        if (!insId.isEmpty()) {
          String[] insIdList = insId.split(",");
          List<Long> insIdListPsn = new ArrayList<Long>();
          for (String ins : insIdList) {
            insIdListPsn.add(NumberUtils.toLong(ins));
          }
          form.setInsIdListPsn(insIdListPsn);
        }
        String regId = map.get("regionId");
        if (!regId.isEmpty()) {
          String[] regIdList = regId.split(",");
          List<Long> regIdListPsn = new ArrayList<Long>();
          for (String reg : regIdList) {
            regIdListPsn.add(NumberUtils.toLong(reg));
          }
          form.setRegionIdListPsn(regIdListPsn);
        }
        String scienceAreaId = map.get("scienceAreaId");
        if (!scienceAreaId.isEmpty()) {
          String[] scaIdList = scienceAreaId.split(",");
          List<Integer> scaIdListPsn = new ArrayList<Integer>();
          for (String scaId : scaIdList) {
            scaIdListPsn.add(NumberUtils.toInt(scaId));
          }
          form.setSaIdListPsn(scaIdListPsn);
        }
      }

    }
    List<Long> insIdList = new ArrayList<Long>();
    List<Long> regionIdList = new ArrayList<Long>();
    List<Integer> scienceAreaIdList = new ArrayList<Integer>();
    if (!form.getInsId().isEmpty()) {
      String[] insIds = form.getInsId().split(",");
      for (String insId : insIds) {
        insIdList.add(NumberUtils.toLong(insId));
      }
      form.setInsIdList(insIdList);
    }
    if (!form.getRegionId().isEmpty()) {
      String[] regionIds = form.getRegionId().split(",");
      for (String regionId : regionIds) {
        regionIdList.add(NumberUtils.toLong(regionId));
      }
      form.setRegionIdList(regionIdList);
    }
    if (!form.getScienceAreaId().isEmpty()) {
      String[] scienceAreaIds = form.getScienceAreaId().split(",");
      for (String scienceAreaId : scienceAreaIds) {
        scienceAreaIdList.add(Integer.valueOf(scienceAreaId));
      }
      form.setSaIdList(scienceAreaIdList);
    }
  }

  /**
   * 构建机构的数据回显
   */
  public List<Map<String, String>> getRecommendInsCount(List<Long> psnIds, PsnListViewForm form) {
    List<Map<String, Object>> insIds = new ArrayList<Map<String, Object>>();
    List<Map<String, String>> insIdsCount = new ArrayList<Map<String, String>>();
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (!psnIds.isEmpty() && !(CollectionUtils.isEmpty(form.getInsIdListPsn()))) {
        insIds = personProfileDao.getInsCountByPsnIds(psnIds, form.getRegionIdList(), form.getSaIdList(),
            form.getInsIdListPsn());
        if (CollectionUtils.isNotEmpty(insIds)) {
          for (Map<String, Object> list : insIds) {
            map.put(list.get("insId").toString(), list.get("count").toString());
          }
          insIdsCount.add(map);

        }
      }
    } catch (Exception e) {
      logger.error("构建机构的数据回显", e);
    }

    return insIdsCount;
  }

  /**
   * 构建地区的数据回显
   */
  public List<Map<String, String>> getRecommendRegCount(List<Long> psnIds, PsnListViewForm form) {
    List<Map<String, String>> regIdsCount = new ArrayList<Map<String, String>>();
    List<Map<String, Object>> regIds = new ArrayList<Map<String, Object>>();
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (!psnIds.isEmpty() && !(CollectionUtils.isEmpty(form.getRegionIdListPsn()))) {
        regIds =
            personProfileDao.getRegIdCount(psnIds, form.getInsIdList(), form.getSaIdList(), form.getRegionIdListPsn());
        for (Map<String, Object> list : regIds) {
          map.put(list.get("regionId").toString(), list.get("count").toString());
        }
        regIdsCount.add(map);
      }
    } catch (Exception e) {
      logger.error("构建地区的数据回显", e);
    }
    return regIdsCount;

  }

  /**
   * 构建研究领域的数据回显
   */
  public List<Map<String, String>> getRecommendAreaCount(List<Long> psnIds, PsnListViewForm form) {
    List<Map<String, String>> scienceAreaIdsCount = new ArrayList<Map<String, String>>();
    List<Map<String, Object>> scaIds = new ArrayList<Map<String, Object>>();
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (!psnIds.isEmpty() && !(CollectionUtils.isEmpty(form.getSaIdListPsn()))) {
        scaIds = personProfileDao.getScienceAreaIdCount(psnIds, form.getInsIdList(), form.getRegionIdList(),
            form.getSaIdListPsn());
        for (Map<String, Object> list : scaIds) {
          map.put(list.get("scienceAreaId").toString(), list.get("count").toString());
        }
        scienceAreaIdsCount.add(map);
      }
    } catch (Exception e) {
      logger.error("构建研究领域的数据回显", e);
    }
    return scienceAreaIdsCount;
  }

  /**
   * 根据语言环境构建显示的人员信息
   *
   * @param form
   */
  private void buildPsnInfoByLanguage(PersonProfileForm form) {
    Person psn = form.getPsnInfo();
    String name = "";
    if (psn != null) {
      if ("en_US".equals(form.getLanguage())) {
        name = StringUtils.isNotBlank(psn.getEname()) ? psn.getEname() : psn.getName();
        // 地区
        form.setPsnRegionInfo(
            StringUtils.isNotBlank(form.getEnPsnRegionInfo()) ? form.getEnPsnRegionInfo() : form.getZhPsnRegionInfo());
      } else {
        name = StringUtils.isNotBlank(psn.getName()) ? psn.getName() : psn.getEname();
        // 地区
        form.setPsnRegionInfo(
            StringUtils.isNotBlank(form.getZhPsnRegionInfo()) ? form.getZhPsnRegionInfo() : form.getEnPsnRegionInfo());
      }
      form.setName(name);
    }
  }

  @Override
  public String editPsnName(PersonProfileForm form) throws PsnException {
    String result = "error";
    try {
      Long psnId = form.getPsnId();
      if (psnId > 0) {
        Person psn = personProfileDao.get(psnId);
        if (psn != null) {

          psn.setName(form.getName());
          // 中文名字为空，但英文的lastName或firstName不为空的
          if (StringUtils.isBlank(form.getName())) {
            if (StringUtils.isNotBlank(form.getZhFirstName()) && StringUtils.isNotBlank(form.getZhLastName())) {
              psn.setName(form.getZhLastName() + form.getZhFirstName());
            }
            if (StringUtils.isBlank(psn.getName())) {
              psn.setName(form.getFirstName() + " " + form.getLastName());
            }
          }

          psn.setZhFirstName(form.getZhFirstName());
          psn.setZhLastName(form.getZhLastName());
          psn.setEname(form.getFirstName() + " " + form.getLastName());
          psn.setOtherName(form.getOtherName());
          // 若中文名或中文姓还是为空，那就用英文的姓名填充
          /*
           * if (StringUtils.isBlank(psn.getZhLastName()) && StringUtils.isBlank(psn.getZhFirstName())) {
           * psn.setZhFirstName(form.getFirstName()); psn.setZhLastName(form.getLastName()); }
           */
          // 根据firstname lastname 更新默认头像
          String oldFirstName = psn.getFirstName();
          String oldLastName = psn.getLastName();
          psn.setFirstName(form.getFirstName());
          psn.setLastName(form.getLastName());
          if ((StringUtils.isBlank(oldFirstName) || !oldFirstName.equals(form.getFirstName()))
              || (StringUtils.isBlank(oldLastName) || !oldLastName.equals(form.getLastName()))) {
            rebuildAvatars(psn);
          }
          form.setAvatars(psn.getAvatars());

          personProfileDao.save(psn);

          // 标记人员信息有刷新
          PsnRefreshUserInfo refInfo = psnRefreshUserInfoDao.get(psnId);
          if (refInfo == null) {
            refInfo = new PsnRefreshUserInfo(psnId);
          }
          refInfo.setNameFlag(1);
          refreshSuggestStrByTask(psnId, 1);// 更新全站检索下拉框推荐词
          // SCM-13851同步人员姓名
          this.saveNameToBatchJobs(psnId);
          psnRefreshUserInfoDao.save(refInfo);
          this.updatePersonPmName(psnId);
          // 标记人员html需刷新
          // psnHtmlRefreshService.updatePsnHtmlRefresh(psnId);
          RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
          if (rsp == null) {
            rsp = new RcmdSyncPsnInfo(psnId);
          }
          rsp.setNameFlag(1);
          rcmdSyncPsnInfoDao.save(rsp);
          result = "success";
        } else {
          result = "person is not found";
        }
      } else {
        result = "psnId is empty";
      }
    } catch (Exception e) {
      logger.error("保存人员姓名出错， psnId=" + form.getPsnId() + ", name=" + form.getName(), e);
      throw new PsnException(e);
    }
    return result;
  }

  @Override
  public void updatePersonPmName(Long psnId) {
    // SCM-17073 更新姓名常量
    try {
      pdwhPubMacthTaskRecordDao.deleteRecordByPsnId(psnId);
      PdwhAddrPsnUpdateRecord record = new PdwhAddrPsnUpdateRecord(psnId, 2, 0);
      pdwhPubMacthTaskRecordDao.save(record);
    } catch (Exception e) {
      logger.error("pdwhPubMacthTaskRecordDao保存更新人员记录出错");
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

  @Override
  public List<PersonSearch> buildPsnInfoForList(List<Long> psnIds) throws PsnException {
    String locale = LocaleContextHolder.getLocale().toString();
    List<PersonSearch> psnInfoList = new ArrayList<PersonSearch>();
    List<Person> psnList = personProfileDao.getPsnListInfoByPsnIds(psnIds);
    if (CollectionUtils.isNotEmpty(psnList)) {
      Map<Long, String> urlMap = this.buildPsnShortUrlMap(psnProfileUrlDao.findPsnShortUrls(psnIds));
      for (Person psn : psnList) {
        PersonSearch info = new PersonSearch();
        info.setPsnId(psn.getPersonId());
        info.setDes3PsnId(psn.getPersonDes3Id());
        info.setAvatars(psn.getAvatars());
        info.setInsName(psn.getInsName());
        info.setDepartment(psn.getDepartment());
        info.setPosition(psn.getPosition());
        info.setTitolo(psn.getTitolo());
        info.setName(this.getPsnName(psn, locale));
        info = this.buildPsnScienceAreas(info, psn.getPersonId());
        info = this.buildPsnKeyWords(info, psn.getPersonId());
        info = this.buildPsnStatisticsInfo(info, psn.getPersonId());
        info = this.buildPsnShortUrl(urlMap, info);
        psnInfoList.add(info);
      }
    }
    return psnInfoList;
  }

  /**
   * 构建人员关键词信息
   *
   * @param psnInfo
   * @param psnId
   * @return
   */
  private PersonSearch buildPsnKeyWords(PersonSearch psnInfo, Long psnId) {
    List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(psnId);
    psnInfo.setDiscList(keyList);
    return psnInfo;
  }

  /**
   * 构建科技领域
   */
  private PersonSearch buildPsnScienceAreas(PersonSearch psnInfo, Long psnId) {
    List<PsnScienceArea> scienceList = psnScienceAreaDao.queryScienceArea(psnId, 1);
    String languageVersion = LocaleContextHolder.getLocale().toString();
    if (!Locale.CHINA.toString().equals(languageVersion)) {
      for (PsnScienceArea psnScienceArea : scienceList) {
        psnScienceArea.setScienceArea(psnScienceArea.getEnScienceArea());
      }
    }
    psnInfo.setScienceList(scienceList);
    return psnInfo;
  }

  /**
   * 构建人员统计信息
   *
   * @param psnInfo
   * @param psnId
   * @return
   */
  private PersonSearch buildPsnStatisticsInfo(PersonSearch psnInfo, Long psnId) {
    // TODO 取公开的成果和项目数
    PsnStatistics psnSta = psnStatisticsDao.getPubAndPrjNum(psnId);
    // psnSta，getPubSum，getPrjSum都要做非空判断，防止出现空指针问题
    if (psnSta == null) {
      psnInfo.setPubSum(0);
      psnInfo.setPrjSum(0);
    } else {
      psnInfo.setPubSum(psnSta.getPubSum() == null ? 0 : psnSta.getPubSum());
      psnInfo.setPrjSum(psnSta.getPrjSum() == null ? 0 : psnSta.getPrjSum());
    }
    return psnInfo;
  }

  /**
   * 获取用户信息(包含头衔的显示信息).
   */
  @Override
  public Person getPersonByRecommend(Long personId) throws ServiceException {
    Person psn = personProfileDao.getPersonByRecommend(personId);
    if (psn == null)
      return null;
    psn.setViewName(this.buildPsnShowName(psn));// 构建显示名称.
    psn.setViewTitolo(this.getPsnViewTitolo(psn));
    return psn;
  }

  /**
   * 保存当前人邮件语言版本
   *
   * @param lanVer
   * @return
   */
  @Override
  public boolean saveCurrPsnEmailLanguageVersion(String lanVer) {
    return personProfileDao.savePsnEmailLanguageVersion(SecurityUtils.getCurrentUserId(), lanVer);
  }

  /**
   * 查询当前人是否已经关注
   */
  @Override
  public AttPerson payAttention(Long psnId, Long reqPsnId) throws Exception {
    AttPerson attPerson = attPersonDao.find(psnId, reqPsnId);
    return attPerson;
  }

  /**
   * 设置人员短地址
   * 
   * @param shortUrlMap
   * @param psnInfo
   * @return
   */
  private PersonSearch buildPsnShortUrl(Map<Long, String> shortUrlMap, PersonSearch psnInfo) {
    Long psnId = psnInfo.getPsnId();
    if (shortUrlMap.containsKey(psnId) && StringUtils.isNotBlank(shortUrlMap.get(psnId))) {
      psnInfo.setPsnShortUrl("/P/" + shortUrlMap.get(psnId));
    } else {
      psnInfo.setPsnShortUrl("/psnweb/outside/homepage?des3PsnId=" + Des3Utils.encodeToDes3(psnId.toString()));
    }
    return psnInfo;
  }

  /**
   * 构建人员短地址Map
   * 
   * @param urlList
   * @return
   */
  private Map<Long, String> buildPsnShortUrlMap(List<PsnProfileUrl> urlList) {
    Map<Long, String> urlMap = new HashMap<Long, String>();
    if (CollectionUtils.isNotEmpty(urlList)) {
      for (PsnProfileUrl psnProfile : urlList) {
        urlMap.put(psnProfile.getPsnId(), psnProfile.getPsnIndexUrl());
      }
    }
    return urlMap;
  }

  /**
   * 人员姓名，机构修改后要将suggest_str_init表中对应的数据重跑。SuggestStrIndexTask任务
   * 
   * @param suggestId 当前推荐词的id（可以是psnId或者机构id）
   * @param type 资源类型（1:人员，2：机构名）
   */
  private void refreshSuggestStrByTask(Long suggestId, Integer type) {
    if (suggestId != null && suggestId > 0) {
      keywordsDicDao.updateSuggestStrStatus(suggestId, type, 0);
    }
  }

}
