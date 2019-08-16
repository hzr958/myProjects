package com.smate.web.psn.service.profile;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigEduDao;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.dao.consts.ConstDictionaryDao;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.web.psn.dao.profile.PsnInfoFillContentDao;
import com.smate.web.psn.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.PsnInfoDaoException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.exception.WorkHistoryException;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.psninfo.SyncPerson;
import com.smate.web.psn.model.rcmdsync.RcmdSyncPsnInfo;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.ins.InsPsnCountService;
import com.smate.web.psn.service.institution.InstitutionManager;
import com.smate.web.psn.service.personsync.PersonSyncService;
import com.smate.web.psn.service.psnhtml.PsnHtmlRefreshService;
import com.smate.web.psn.service.user.search.UserSearchService;
import com.smate.web.psn.utils.LocaleStringUtils;

/**
 * 人员教育服务接口实现
 * 
 * @author Administrator
 *
 */
@Service("educationHistoryService")
@Transactional(rollbackFor = Exception.class)
public class EducationHistoryServiceImpl implements EducationHistoryService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private PsnCnfService psnCnfService;
  @Autowired
  private InstitutionManager institutionManager;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private UserSearchService userSearchService;
  @Autowired
  private FriendService friendService;
  @Autowired
  private PsnPrivateService psnPrivateService;
  @Autowired
  private PersonSyncService personSyncService;
  @Autowired
  private PsnHtmlRefreshService psnHtmlRefreshService;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private PsnInfoFillContentDao psnInfoFillContentDao;
  @Autowired
  private ConstDictionaryDao constDictionaryDao;
  @Value("${domainscm}")
  private String domianScm;
  @Autowired
  private PersonalManager personalManager;
  @Autowired
  private PsnConfigEduDao psnConfigEduDao;
  @Autowired
  private InsPsnCountService insPsnCountService;
  @Autowired
  private InsPortalDao insPortalDao;

  /**
   * 判断用户是否存在教育经历
   */
  @Override
  public boolean isEduHistoryExit(Long psnId) throws PsnException {
    try {
      return this.educationHistoryDao.isEduHistoryExit(psnId);
    } catch (PsnInfoDaoException e) {
      logger.error("判断用户是否存在教育经历", e);
      throw new PsnException(e);
    }
  }

  /**
   * 保存教育经历
   */
  @Override
  public Long saveEducationHistory(EducationHistory form, boolean isSyncAuthority, Integer anyUser)
      throws PsnException {
    Long eduId = this.saveEduHistory(form, isSyncAuthority, anyUser);

    PsnConfigEdu cnfEdu = new PsnConfigEdu();
    cnfEdu.getId().setEduId(eduId);

    cnfEdu.setAnyUser(anyUser);
    // 查看权限 & 数据内容有无
    cnfEdu.setAnyView(cnfEdu.getAnyUser());
    try {
      psnCnfService.save(form.getPsnId(), cnfEdu);
    } catch (com.smate.core.base.exception.ServiceException e) {
      logger.error("保存教育经历权限出错", e);
    }

    return eduId;
  }

  @Override
  public Long saveEducationHistory(EducationHistory form, boolean isSyncAuthority, Integer anyUser, boolean isPrimary)
      throws PsnException {
    Long eduId = this.saveEduHistory(form, isSyncAuthority, anyUser, isPrimary);

    PsnConfigEdu cnfEdu = new PsnConfigEdu();
    cnfEdu.getId().setEduId(eduId);

    cnfEdu.setAnyUser(anyUser);
    // 查看权限 & 数据内容有无
    cnfEdu.setAnyView(cnfEdu.getAnyUser());
    try {
      psnCnfService.save(form.getPsnId(), cnfEdu);
    } catch (com.smate.core.base.exception.ServiceException e) {
      logger.error("保存教育经历权限出错", e);
    }

    return eduId;
  }

  /**
   * 保存教育经历,如果ID不为空，则更新，如果为空，则添加.--注册
   */
  @Override
  public Long saveEduHistory(EducationHistory form, boolean isSyncAuthority, Integer anyUser) throws PsnException {
    try {
      // 查找单位ID
      if (form.getInsId() == null) {
        Long insId = institutionManager.getInsIdByName(form.getInsName(), form.getInsName());
        form.setInsId(insId);
      }
      // 冗余同步用户信息
      RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(form.getPsnId());
      if (rsp == null) {
        rsp = new RcmdSyncPsnInfo(form.getPsnId());
      }
      rsp.setEduFlag(1);
      // personManager.updateIns(form.getInsId(), form.getInsName(),
      // form.getPsnId());
      // rsp.setInsFlag(1);
      // Institution ins =
      // institutionManager.getInstitution(form.getInsName(),
      // form.getInsId());
      // userSearchService.updateUserIns(form.getPsnId(), ins);
      // workHistoryDao.updateIsPrimary(form.getPsnId());
      // educationHistoryDao.updateEduIsPrimary(form.getPsnId());
      // 保存或更新教育经历
      Long eduId = this.saveOrUpdateEduHistory(form);
      // 获取当前用户记录.
      Person psn = personProfileDao.get(form.getPsnId());

      // SCM-16216 学位信息
      String degreeName = form.getDegreeName();
      if (StringUtils.isNotBlank(degreeName)) {
        degreeName = degreeName.trim().toLowerCase();
        List<ConstDictionary> degreeList = constDictionaryDao.findConstByCategory("psn_degree");
        for (ConstDictionary degree : degreeList) {
          String degreeZhName = degree.getZhCnName().trim().toLowerCase();
          String degreeEnName = degree.getEnUsName().trim().toLowerCase();
          if (degreeName.equals(degreeZhName) || degreeName.equals(degreeEnName)) {
            psn.setDegree(degree.getSeqNo());
            break;
          }
        }
      }
      Institution ins = null;
      // 查找单位ID
      if (form.getInsId() == null && StringUtils.isNotBlank(form.getInsName())) {
        ins = institutionManager.findByName(form.getInsName());
        if (ins != null) {
          form.setInsId(ins.getId());
        }
      } else {
        ins = institutionManager.getInstitution(form.getInsId());

      }
      // SCM-16216 region信息
      if (ins != null) {
        psn.setRegionId(ins.getRegionId());
      }

      psn.setDegreeName(form.getDegreeName());
      personProfileDao.save(psn);
      this.saveEduOtherInfo(psn);
      rcmdSyncPsnInfoDao.save(rsp);
      // psn.setInsName(form.getInsName());
      // psn.setInsId(form.getInsId());
      // personProfileDao.save(psn);
      // psnHtmlRefreshService.updatePsnHtmlRefresh(form.getPsnId());
      return eduId;
    } catch (Exception e) {
      logger.error("保存人员教育经历出错", e);
      throw new PsnException();
    }
  }

  private void getDegreeName() {
    // TODO Auto-generated method stub

  }

  @Override
  public Long saveEduHistory(EducationHistory form, boolean isSyncAuthority, Integer anyUser, boolean isPrimary)
      throws PsnException {
    try {
      Institution ins = null;
      // 查找单位ID
      if (form.getInsId() == null) {
        ins = institutionManager.findByName(form.getInsName());
        if (ins != null) {
          form.setInsId(ins.getId());
        }
      } else {
        ins = institutionManager.getInstitution(form.getInsId());

      }
      // 冗余同步用户信息
      RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(form.getPsnId());
      if (rsp == null) {
        rsp = new RcmdSyncPsnInfo(form.getPsnId());
      }
      rsp.setEduFlag(1);
      // personManager.updateIns(form.getInsId(), form.getInsName(),
      // form.getPsnId());
      // rsp.setInsFlag(1);
      // Institution ins =
      // institutionManager.getInstitution(form.getInsName(),
      // form.getInsId());
      // userSearchService.updateUserIns(form.getPsnId(), ins);
      // workHistoryDao.updateIsPrimary(form.getPsnId());
      // educationHistoryDao.updateEduIsPrimary(form.getPsnId());
      // 保存或更新教育经历
      Long eduId = this.saveOrUpdateEduHistory(form);
      // WCW scm13379 degree信息更新到person表
      List<EducationHistory> findEduInsList = educationHistoryDao.findEduInsList(form.getPsnId());
      boolean isfounded = false;
      String degreeId = "";
      List<ConstDictionary> degreeList = constDictionaryDao.findConstByCategory("psn_degree");

      Locale locale = LocaleContextHolder.getLocale();
      String degreeName = "";
      int eduPos = 0;
      while (!isfounded) {
        // 查出来的数据是上次修改的而不是本次修改的，暂时做替换处理
        if (findEduInsList.size() == 0 || findEduInsList.get(eduPos).getEduId().equals(eduId)) {
          degreeName = form.getDegreeName().trim().toLowerCase();
        } else {
          degreeName = findEduInsList.get(eduPos).getDegreeName() == null ? ""
              : findEduInsList.get(eduPos).getDegreeName().trim().toLowerCase();
        }
        if (!degreeName.equals("") && degreeName != null) {
          if (locale.equals(Locale.US)) {
            for (ConstDictionary degree : degreeList) {
              if (degreeName.equals(degree.getEnUsName().trim().toLowerCase())) {
                isfounded = true;
                degreeId = degree.getSeqNo();
                degreeName = degree.getEnUsName();
                break;
              }
            }
          } else {
            for (ConstDictionary degree : degreeList) {
              if (degreeName.equals(degree.getZhCnName().trim().toLowerCase())) {
                isfounded = true;
                degreeId = degree.getSeqNo();
                degreeName = degree.getZhCnName();
                break;
              }
            }
          }
        }
        if (++eduPos >= findEduInsList.size()) {
          isfounded = true;
        }
      }

      Person psn = personProfileDao.get(form.getPsnId());
      if ((psn.getRegionId() == null || psn.getRegionId() == 0L) && ins != null) {
        psn.setRegionId(ins.getRegionId());
      }
      psn.setDegree(degreeId);
      psn.setDegreeName(degreeName);
      personProfileDao.save(psn);
      // 获取当前用户记录.
      this.saveEduOtherInfo(psn);
      rcmdSyncPsnInfoDao.save(rsp);
      // psn.setInsName(form.getInsName());
      // psn.setInsId(form.getInsId());
      // personProfileDao.save(psn);
      // psnHtmlRefreshService.updatePsnHtmlRefresh(form.getPsnId());
      if (Objects.nonNull(ins)) {
        addInsPsnCount(form.getInsName(), 1);
      }
      return eduId;
    } catch (Exception e) {
      logger.error("保存人员教育经历出错", e);
      throw new PsnException();
    }
  }

  /**
   * 增加(减少)工作经历相关机构人数
   */
  public void addInsPsnCount(String insName, Integer type) {
    if (StringUtils.isEmpty(insName)) {
      return;
    }
    try {
      insPsnCountService.addPsnCount(insName, type);
    } catch (Exception e) {
      logger.error("更新机构人数出错,insName={}", insName, e);
    }
  }

  /**
   * 如果“设置首要单位”条件为否，且当前insid是首要单位，则取消当前insid的首要单位设置
   * 
   * @param form
   * @param rsp
   */
  @Deprecated
  public void removePrimary(EducationHistory form, RcmdSyncPsnInfo rsp) {
    if (rsp == null) {
      rsp = new RcmdSyncPsnInfo(form.getPsnId());
    }
    if ((form.getIsPrimary() == null || form.getIsPrimary() == 0) && form.getPsnId() > 0) {
      Person psn = personManager.getPersonBaseInfo(form.getPsnId());
      if (psn.getInsId().equals(form.getInsId()) || form.getInsName().equals(psn.getInsName())) {
        psn.setInsId(null);
        psn.setInsName(null);
      }
      rsp.setInsFlag(1);
      personManager.save(psn);
      // 修改了获取检索人员服务的方式_MJG_SCM-5909.
      if (userSearchService != null) {
        // 同步更新人员检索表中的单位信息_MJG_SCM-5120.
        userSearchService.updateUserIns(form.getPsnId(), null);
      }
    }
  }

  /**
   * 保存或更新教育经历
   */
  public Long saveOrUpdateEduHistory(EducationHistory form) {
    form.setDescription(HtmlUtils.htmlUnescape(form.getDescription()));
    // 不存在，直接保存
    if (form.getEduId() == null) {
      educationHistoryDao.save(form);
      return form.getEduId();
    } else {// 存在，更新
      EducationHistory eduHistory = educationHistoryDao.get(form.getEduId());
      // 不存在，直接保存
      if (eduHistory == null) {
        educationHistoryDao.save(form);
        return form.getEduId();
      } else {
        eduHistory.setDegreeName(form.getDegreeName());
        eduHistory.setFromMonth(form.getFromMonth());
        eduHistory.setFromYear(form.getFromYear());
        eduHistory.setInsId(form.getInsId());
        eduHistory.setInsName(form.getInsName());
        eduHistory.setStudy(form.getStudy());
        eduHistory.setToMonth(form.getToMonth());
        eduHistory.setToYear(form.getToYear());
        eduHistory.setPsnId(form.getPsnId());
        eduHistory.setIsPrimary(form.getIsPrimary());
        // 教育描述
        eduHistory.setDescription(form.getDescription());
        eduHistory.setIsActive(form.getIsActive());
        educationHistoryDao.save(eduHistory);
        return eduHistory.getEduId();
      }
    }
  }

  private void saveEduOtherInfo(Person currPsn) {
    // 修改添加工作经历同步到好友节点
    try {
      SyncPerson syncPsn = friendService.syncPersonByIns(currPsn.getPersonId());
      if (currPsn.getRegionId() == null || currPsn.getRegionId() == 0L) {
        currPsn.setRegionId(syncPsn.getRegionId());
      }
      currPsn.setInsName(syncPsn.getPsnInsName());
      int isPrivate = psnPrivateService.isPsnPrivate(currPsn.getPersonId()) ? 1 : 0;
      currPsn.setIsPrivate(isPrivate);
      personSyncService.dealPersonSync(currPsn, syncPsn);
    } catch (ServiceException e) {
      logger.error("添加教育经历同步到好友节点出错！", e);
    }
  }

  @Override
  public List<EducationHistory> findPsnAllEducationHistory(Long psnId) throws PsnException {
    return educationHistoryDao.findEduInsByPsnId(psnId);
  }

  @Override
  public EducationHistory findEducationHistoryById(Long eduId) throws PsnException {
    return educationHistoryDao.get(eduId);
  }

  @Override
  public String delEducationHistory(Long psnId, Long eduId) throws PsnException {
    try {
      EducationHistory edu = educationHistoryDao.isEduHistoryOwner(psnId, eduId);
      if (edu != null) {
        // 删除教育经历
        educationHistoryDao.deleteEduHistory(eduId);
        // 删除权限配置
        PsnConfigEdu cnfEdu = new PsnConfigEdu();
        cnfEdu.getId().setEduId(eduId);
        psnCnfService.del(psnId, cnfEdu);
        // 刷新人员信息完整度
        personManager.refreshComplete(psnId);
        // 删除个人信息补充过度中相应数据
        // psnInfoFillContentDao.deleteByPsnIdSaveId(psnId, eduId);
        // 更新人员solr信息
        // psnSolrInfoModifyService.updateSolrPsnInfo(psnId);
        personalManager.refreshPsnSolrInfoByTask(psnId);
        // 更新sie的人员信息
        personalManager.updateSIEPersonInfo(psnId);
        addInsPsnCount(edu.getInsName(), 2);
        return "success";
      } else {
        return "notFound";
      }
    } catch (Exception e) {
      logger.error("删除教育经历出错， psnId=" + psnId + "eduId=" + eduId, e);
    }
    return "error";
  }

  @Override
  public PersonProfileForm buildPsnEduHistoryListInfo(PersonProfileForm form) throws WorkHistoryException {
    List<EducationHistory> eduList = educationHistoryDao.findByPsnId(form.getPsnId());
    if (!CollectionUtils.isEmpty(eduList)) {
      for (EducationHistory edu : eduList) {
        edu.setEduDesc(this.buildEduDesc(edu));
        // 单位对应的图片
        /**
         * 需求变更(SCM-23894):
         * 由于生产数据库中sie的ins_portal存在2000多条数据,而我们的insLogo静态文件夹中只有一千多条,所以采用读取sie表ins_portal的数据,由于我们个人版库中ins_portal
         * 是sie表ins_portal的视图,所以在实际操作时读取该表的数据(视图的数据会跟着基表的数据变动而变动),若通过id能够获取到则设置现有图片,若获取不到则设置默认
         */
        if (NumberUtils.isNotNullOrZero(edu.getInsId())) {
          InsPortal insPortal = insPortalDao.get(edu.getInsId());
          String insImgPath = insPortal != null ? insPortal.getLogo() : "";
          edu.setInsImgPath(StringUtils.isNotEmpty(insImgPath) ? domianScm + insImgPath : null);
        }
      }
    }
    form.setEduList(eduList);
    return form;
  }



  /**
   * 构建工作经历信息 ： 部门、职称、开始年月-结束年月
   * 
   * @param work
   * @return
   */
  private String buildEduDesc(EducationHistory edu) {
    String department = StringUtils.isBlank(edu.getStudy()) ? "" : edu.getStudy();
    String position = StringUtils.isBlank(edu.getDegreeName()) ? "" : edu.getDegreeName();
    Long fromYear = edu.getFromYear();
    Long toYear = edu.getToYear();
    Long fromMonth = edu.getFromMonth();
    Long toMonth = edu.getToMonth();
    Long isActive = edu.getIsActive() == null ? 0l : edu.getIsActive();
    StringBuilder workDesc = new StringBuilder();
    if (StringUtils.isBlank(department) || StringUtils.isBlank(position)) {
      workDesc.append(department + position);
    } else {
      workDesc.append(department + ", " + position);
    }
    if (fromYear != null) {
      if (StringUtils.isBlank(workDesc.toString())) {
        workDesc.append(fromYear);
      } else {
        workDesc.append(", " + fromYear);
      }
      if (fromMonth != null) {
        workDesc.append("/" + fromMonth);
      }
      if (toYear != null) {
        workDesc.append(" - " + toYear);
        if (toMonth != null) {
          workDesc.append("/" + toMonth);
        }
      } else {
        if (isActive == 1) {
          workDesc.append(LocaleStringUtils.getStringByLocale(" - to Present", " - 至今"));
        }
      }
    }
    // SCM-12698
    /*
     * if (StringUtils.isNotBlank(edu.getDescription())) { if
     * (StringUtils.isNotBlank(workDesc.toString())) { workDesc.append(", " + edu.getDescription()); }
     * else { workDesc.append(edu.getDescription()); } }
     */
    return workDesc.toString();
  }

  @Override
  public boolean isOwnerOfEduHistory(Long psnId, Long eduId) throws PsnException {
    EducationHistory edu = educationHistoryDao.isEduHistoryOwner(psnId, eduId);
    if (edu != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean hasEduConfigLost(Long psnId, Long cnfId) throws PsnException {
    return educationHistoryDao.checkPsnConfigEduLost(psnId, cnfId);
  }

  @Override
  public EducationHistory getEducationHistoryByEduId(Long psnId, Long eduId) throws PsnException {
    EducationHistory educationHistory = educationHistoryDao.findPsnEducationHistory(psnId, eduId);
    if (educationHistory != null && educationHistory.getInsId() != null) {
      // 单位对应的图片
      educationHistory.setInsImgPath(domianScm + "/insLogo/" + educationHistory.getInsId() + ".jpg");
    }
    return educationHistory;
  }

}
