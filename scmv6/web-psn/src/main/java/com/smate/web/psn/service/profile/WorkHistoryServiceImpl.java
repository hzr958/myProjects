package com.smate.web.psn.service.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.ConstPosition;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.consts.service.ConstPositionService;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigWorkDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.dao.profile.PsnInfoFillContentDao;
import com.smate.web.psn.dao.psnlog.PsnUpdateWorkLogDao;
import com.smate.web.psn.dao.psnrefreshinfo.PsnRefreshUserInfoDao;
import com.smate.web.psn.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.web.psn.dao.rol.psnwork.RolWorkHistoryDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.PsnInfoDaoException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.exception.WorkHistoryException;
import com.smate.web.psn.model.consts.LocaleConsts;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.psninfo.PsnRefreshUserInfo;
import com.smate.web.psn.model.psninfo.SyncPerson;
import com.smate.web.psn.model.psnlog.PsnUpdateWorkLog;
import com.smate.web.psn.model.rcmdsync.RcmdSyncPsnInfo;
import com.smate.web.psn.model.rol.workhistory.RolWorkHistory;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.ins.InsPsnCountService;
import com.smate.web.psn.service.institution.InstitutionManager;
import com.smate.web.psn.service.personsync.PersonSyncService;
import com.smate.web.psn.service.psnhtml.PsnHtmlRefreshService;
import com.smate.web.psn.service.user.search.UserSearchService;

/**
 * 人员工作经历服务接口实现
 * 
 * @author Administrator
 */
@Service("workHistoryService")
@Transactional(rollbackFor = Exception.class)
public class WorkHistoryServiceImpl implements WorkHistoryService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final String regionName = "中国";
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private ConstPositionService constPositionService;
  @Autowired
  private InstitutionManager institutionManager;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private UserSearchService userSearchService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private WorkHistoryInsInfoService workHistoryInsInfoService;
  @Autowired
  private PsnPrivateService psnPrivateService;
  @Autowired
  private PsnCnfService psnCnfService;
  @Autowired
  private RolWorkHistoryDao rolWorkHistoryDao;
  @Autowired
  private PsnUpdateWorkLogDao psnUpdateWorkLogDao;
  @Autowired
  private FriendService friendService;
  @Autowired
  private PersonSyncService personSyncService;
  @Autowired
  private PsnHtmlRefreshService psnHtmlRefreshService;
  @Autowired
  private PsnInfoFillContentDao psnInfoFillContentDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private HibernateTemplate hibernateTemplate;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private PsnConfigWorkDao psnConfigWorkDao;
  @Autowired
  private InsPsnCountService insPsnCountService;
  @Autowired
  private InsPortalDao insPortalDao;

  /**
   * 判断用户是否存在教育经历.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public boolean isWorkHistoryExit(Long psnId) throws PsnException {
    try {
      return this.workHistoryDao.isWorkHistoryExit(psnId);
    } catch (PsnInfoDaoException e) {
      logger.error("判断用户是否存在教育经历", e);
      throw new PsnException(e);
    }
  }

  /**
   * 保存工作经历，编辑、添加--注册.
   */
  @Override
  public Long saveWorkHistory(WorkHistory workHistory, boolean isSyncAuthority, Integer anyUser)
      throws WorkHistoryException {
    Long workId = this.saveWorkHistory(workHistory, anyUser);
    this.addWorkCnf(workHistory.getPsnId(), workId, anyUser); // 添加工作经历权限
    return workId;
  }

  /**
   * 新的保存或更新工作经历
   * 
   * @param workHistory
   * @param isSyncAuthority
   * @param anyUser
   * @param isPrimary
   * @return
   * @throws WorkHistoryException
   */
  @Override
  public Long saveWorkHistory(WorkHistory workHistory, boolean isSyncAuthority, Integer anyUser, Long isPrimary)
      throws WorkHistoryException {
    Long workId = this.saveWorkHistory(workHistory, anyUser, isPrimary);
    this.addWorkCnf(workHistory.getPsnId(), workId, anyUser); // 添加工作经历权限
    return workId;
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

  // 移动端注册后完善工作经历信息调用-------------会更新person表中单位、部门、职称等信息
  @Override
  public Long saveWorkHistory(WorkHistory workHistory, Integer anyUser) throws WorkHistoryException {
    Long psnId = workHistory.getPsnId();
    // 冗余同步用户信息
    RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
    PsnRefreshUserInfo psnRefInfo = psnRefreshUserInfoDao.get(psnId);
    if (rsp == null) {
      rsp = new RcmdSyncPsnInfo(psnId);
    }
    if (psnRefInfo == null) {
      psnRefInfo = new PsnRefreshUserInfo(psnId);
    }
    workHistory = this.reBuildWorkHistory(workHistory);
    // 首要工作单位
    rsp.setInsFlag(1);
    psnRefInfo.setIns(1);
    rsp.setExperienceFlag(1);
    psnRefInfo.setDegree(1);
    psnRefInfo.setPosition(1);
    // personManager.updateIns(workHistory.getInsId(),
    // workHistory.getInsName(), workHistory.getPsnId());
    Institution ins = institutionManager.getInstitution(workHistory.getInsName(), workHistory.getInsId());
    userSearchService.updateUserIns(workHistory.getPsnId(), ins);
    workHistoryDao.updateIsPrimary(psnId);
    educationHistoryDao.updateEduIsPrimary(psnId);
    // 获取当前用户记录.
    Person psn = personProfileDao.get(workHistory.getPsnId());
    this.addPosition2Psn(psn, workHistory);// 添加职称到Person表
    // SCM-12132_WSN_2017-4-11
    if (StringUtils.isNotBlank(workHistory.getDepartment())) {
      psn.setDepartment(workHistory.getDepartment());
    }
    boolean addFlag = false;
    // 存在则更新，不存在则直接保存
    Long workId = this.saveOrUpdateWorkHistory(workHistory, addFlag);
    // 同步更新人员单位信息变更的相关冗余信息.
    this.saveWorkOtherInfo(workHistory, psn, workId, addFlag, anyUser);
    psn.setInsName(workHistory.getInsName());
    psn.setInsId(workHistory.getInsId());
    // 保存regionId信息
    if (ins != null) {
      psn.setRegionId(ins.getRegionId());
    }
    personProfileDao.save(psn);
    rsp.setWorkFlag(1);
    rcmdSyncPsnInfoDao.save(rsp);
    psnRefreshUserInfoDao.save(psnRefInfo);
    // 同步到rol.psn_workHistory_ins_info
    // historySyncToRolProducer.syncMessage(form.getPsnId());
    return workId;
  }

  /**
   * 新的保存或更新工作经历，没有设置首要工作单位，默认不更新首要工作单位
   * 
   * @param workHistory
   * @param anyUser
   * @param isPrimary
   * @return
   * @throws WorkHistoryException
   */
  @Override
  public Long saveWorkHistory(WorkHistory workHistory, Integer anyUser, Long isPrimary) throws WorkHistoryException {
    Long psnId = workHistory.getPsnId();
    // 冗余同步用户信息
    RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
    PsnRefreshUserInfo psnRefInfo = psnRefreshUserInfoDao.get(psnId);
    if (rsp == null) {
      rsp = new RcmdSyncPsnInfo(psnId);
    }
    if (psnRefInfo == null) {
      psnRefInfo = new PsnRefreshUserInfo(psnId);
    }
    workHistory = this.reBuildWorkHistory(workHistory);
    // 不是首要单位，判断是否有首要单位了，没有则将当前单位保存为首要单位
    // 1.判断有没有首要单位
    boolean hasPrimary = workHistoryDao.hasPrimaryWorkHistory(psnId);
    // 2.没有首要单位，将当前单位设为首要单位
    if (!hasPrimary) {
      workHistory.setIsPrimary(1L);
      isPrimary = 1L;
    }
    rsp.setWorkFlag(1);
    rsp.setInsFlag(1);
    rsp.setExperienceFlag(1);
    psnRefInfo.setIns(1);
    psnRefInfo.setDegree(1);
    psnRefInfo.setPosition(1);
    Institution ins = institutionManager.getInstitution(workHistory.getInsName(), workHistory.getInsId());
    userSearchService.updateUserIns(workHistory.getPsnId(), ins);
    // // 获取当前用户记录.
    Person psn = personProfileDao.findPersonInfoForWrok(workHistory.getPsnId());
    boolean addFlag = false;
    // 存在则更新，不存在则直接保存
    if ("addPrimary".equals(workHistory.getOperateType())) {
      workHistoryDao.updateIsPrimary(psnId);
    }
    Long workId = this.saveOrUpdateWorkHistory(workHistory, addFlag);
    // 同步更新人员单位信息变更的相关冗余信息.
    this.saveWorkOtherInfo(workHistory, psn, workId, addFlag, anyUser);
    rcmdSyncPsnInfoDao.save(rsp);
    psnRefreshUserInfoDao.save(psnRefInfo);
    // 是首要单位的话得更新Person表中人员单位信息
    if (isPrimary == 1) {
      psn = personProfileDao.get(workHistory.getPsnId());
      this.addPosition2Psn(psn, workHistory);// 添加职称到Person表
      if (StringUtils.isNotBlank(workHistory.getDepartment())) {
        psn.setDepartment(workHistory.getDepartment());
      }
      psn.setInsName(workHistory.getInsName());
      psn.setInsId(workHistory.getInsId());
      if (ins != null) {
        psn.setRegionId(ins.getRegionId());
      }
      personProfileDao.save(psn);
    }
    addInsPsnCount(workHistory.getInsName(), 1);
    return workId;
  }

  /**
   * 重新构建workHistory对象
   * 
   * @param workHistory
   * @return
   */
  private WorkHistory reBuildWorkHistory(WorkHistory workHistory) {
    // 查找单位ID
    if (workHistory.getInsId() == null) {
      Long insId = institutionManager.getInsIdByName(workHistory.getInsName(), workHistory.getInsName());
      workHistory.setInsId(insId);
    } // 职务
    String position = workHistory.getPosition() == null ? null : workHistory.getPosition().trim();
    if (workHistory.getPosId() == null && StringUtils.isNotBlank(position)) {
      ConstPosition constPosition = constPositionService.getPosByName(position);
      if (constPosition != null) {
        workHistory.setPosId(constPosition.getId());
        workHistory.setPosGrades(constPosition.getGrades());
      } else {
        workHistory.setPosId(null);
        workHistory.setPosGrades(null);
      }
    } else if (workHistory.getPosId() != null) {
      Integer posGrades = constPositionService.getPosGrades(workHistory.getPosId());
      if (posGrades != null) {
        workHistory.setPosGrades(posGrades);
      } else {
        workHistory.setPosId(null);
        workHistory.setPosGrades(null);
      }
    } else {
      workHistory.setPosId(null);
      workHistory.setPosGrades(null);
    }

    return workHistory;
  }

  // 添加职称到Person表
  private void addPosition2Psn(Person person, WorkHistory workHistory) throws PsnException {
    if (person == null) {
      return;
    }
    Integer posGrades = 0;
    if (workHistory.getPosId() != null) {
      posGrades = constPositionService.getPosGrades(workHistory.getPosId());
    } else {
      if (StringUtils.isNotBlank(workHistory.getPosition())) {
        ConstPosition constPosition = constPositionService.getPosByName(workHistory.getPosition().trim());
        if (constPosition != null) {
          workHistory.setPosId(constPosition.getId());
          posGrades = constPosition.getGrades();
        }
      }

    }

    if (StringUtils.isNotBlank(workHistory.getPosition())) {
      person.setPosGrades(posGrades);
      person.setPosition(workHistory.getPosition().trim());
      person.setPosId(workHistory.getPosId());
    }
  }

  private Long saveWorkHis(WorkHistory workHistory, WorkHistory form) {
    workHistory.setFromMonth(form.getFromMonth());
    workHistory.setFromYear(form.getFromYear());
    workHistory.setInsId(form.getInsId());
    workHistory.setInsName(form.getInsName());
    workHistory.setInsId(form.getInsId());
    workHistory.setIsActive(form.getIsActive());
    workHistory.setIsPrimary(form.getIsPrimary());
    workHistory.setDepartment(form.getDepartment());
    workHistory.setPosId(form.getPosId());
    workHistory.setPosGrades(form.getPosGrades());
    workHistory.setPosition(form.getPosition());
    if (form.getIsActive() != null && form.getIsActive() == 1) {
      workHistory.setToMonth(null);
      workHistory.setToYear(null);
    } else {
      workHistory.setToMonth(form.getToMonth());
      workHistory.setToYear(form.getToYear());
    }
    // 工作描述
    workHistory.setDescription(form.getDescription());
    workHistoryDao.save(workHistory);
    return workHistory.getWorkId();
  }

  /**
   * 保存或更新工作经历
   * 
   * @return
   */
  private Long saveOrUpdateWorkHistory(WorkHistory workHistory, boolean addFlag) {
    workHistory.setDescription(HtmlUtils.htmlUnescape(workHistory.getDescription()));
    // 不存在，直接保存
    Long workId = null;
    if (workHistory.getWorkId() == null) {
      workHistoryDao.save(workHistory);
      workId = workHistory.getWorkId();
      addFlag = true;
      // 更新个人工作经历单位信息，用于人员列表_zk_SCM-5552R好友列表，1：是首要单位，0：非首要单位
      if (workHistory.getIsPrimary() != null && workHistory.getIsPrimary() > 0) {
        workHistoryInsInfoService.updateWorkHistoryInsInfo(workHistory, 1);
      } else {
        workHistoryInsInfoService.updateWorkHistoryInsInfo(workHistory, 0);
      }
    } else {
      // 存在，更新
      WorkHistory oldworkHistory = workHistoryDao.get(workHistory.getWorkId());
      // 不存在，直接保存
      if (oldworkHistory == null) {
        addFlag = true;
        workHistoryDao.save(workHistory);
        workId = workHistory.getWorkId();
        // 默认公开工作经历

        // 更新个人工作经历单位信息，用于人员列表_zk_SCM-5552R好友列表
        if (workHistory.getIsPrimary() != null && workHistory.getIsPrimary() > 0) {
          workHistoryInsInfoService.updateWorkHistoryInsInfo(workHistory, 0);
        } else {
          workHistoryInsInfoService.updateWorkHistoryInsInfo(workHistory, 1);
        }
      } else {
        addFlag = false;
        workId = this.saveWorkHis(oldworkHistory, workHistory);
        if (workHistory.getIsPrimary() != null && workHistory.getIsPrimary() > 0) {
          // 更新个人工作经历单位信息，用于人员列表_zk_SCM-5552R好友列表
          workHistoryInsInfoService.updateWorkHistoryInsInfo(workHistory, 1);
        }
      }
    }

    return workId;
  }

  /**
   * 如果“设置首要单位”条件为否，且当前insid是首要单位，则取消当前insid的首要单位设置
   */
  @Deprecated
  private void removePrimary(WorkHistory workHistory, Person psn, RcmdSyncPsnInfo rsp) {
    if ((workHistory.getIsPrimary() == null || workHistory.getIsPrimary() == 0) && workHistory.getPsnId() > 0) {
      if (psn == null) {
        // 获取当前用户记录.
        psn = personManager.getPersonBaseInfo(workHistory.getPsnId());
      }
      if (rsp == null) {
        rsp = new RcmdSyncPsnInfo(workHistory.getPsnId());
      }
      if (psn.getInsId().equals(workHistory.getInsId()) || workHistory.getInsName().equals(psn.getInsName())) {
        psn.setInsId(null);
        psn.setInsName(null);
        // 刷新用户冗余数据
        rsp.setInsFlag(1);
      }

      personManager.save(psn);

      if (userSearchService != null) {
        // 同步更新人员检索表中的单位信息_MJG_SCM-5120.
        userSearchService.updateUserIns(workHistory.getPsnId(), null);
      }
    }
  }

  /**
   * 同步更新人员单位信息变更的相关冗余信息
   * 
   * @param form
   * @param currPsn
   * @param workId
   * @param addFlag
   * @param anyUser
   * @throws WorkHistoryException
   */
  private void saveWorkOtherInfo(WorkHistory form, Person currPsn, Long workId, Boolean addFlag, Integer anyUser)
      throws WorkHistoryException {
    try {
      // 修改添加工作经历同步到好友节点
      SyncPerson syncPsn = friendService.syncPersonByIns(currPsn.getPersonId());
      currPsn.setRegionId(syncPsn.getRegionId());
      currPsn.setInsName(syncPsn.getPsnInsName());
      int isPrivate = psnPrivateService.isPsnPrivate(currPsn.getPersonId()) ? 1 : 0;
      currPsn.setIsPrivate(isPrivate);
      personSyncService.dealPersonSync(currPsn, syncPsn);
      if (!"2".equals(form.getAuthority())) {
        // 保存更新工作信息，发邮件给好友,每人每天只保留一条记录,重复则忽悠
        PsnUpdateWorkLog uwLog = psnUpdateWorkLogDao.getPsnUpdateWorkLog(form.getPsnId(), workId);
        if (uwLog == null) {
          uwLog = new PsnUpdateWorkLog();
          uwLog.setPsnId(form.getPsnId());
          uwLog.setWorkId(workId);
          uwLog.setCreateDate(new Date());
          uwLog.setStatus(0);
          psnUpdateWorkLogDao.save(uwLog);
        } else {
          uwLog.setWorkId(workId);
          uwLog.setCreateDate(new Date());
          psnUpdateWorkLogDao.save(uwLog);
        }
      }
    } catch (Exception e) {
      throw new WorkHistoryException("同步单位信息出错", e);
    }
  }

  /**
   * 添加工作经历同步到ROL
   */
  private void addWorkHistoryToRol(Long workId, Long psnId, Long insId, Long fromYear, Long fromMonth, Long toYear,
      Long toMonth, Long isActive) {
    try {
      // 查重
      List<RolWorkHistory> works = findRolWorkHistory(workId, psnId);
      if (works != null && works.size() == 0) {
        RolWorkHistory rolWorkHistory =
            createRolWorkHistory(workId, psnId, insId, fromYear, fromMonth, toYear, toMonth, isActive);
        rolWorkHistoryDao.save(rolWorkHistory);
      }
    } catch (Exception e) {
      logger.error("同步添加工作经历出错", e);
    }
  }

  /**
   * 修改工作经历同步到ROL
   */
  private void updateWorkHistoryToRol(Long workId, Long psnId, Long insId, Long fromYear, Long fromMonth, Long toYear,
      Long toMonth, Long isActive) {
    try {
      if (workId == null || psnId == null) {
        return;
      }
      List<RolWorkHistory> rolWorkHistorys = rolWorkHistoryDao.findRolWorkHistory(workId, psnId);
      if (rolWorkHistorys != null && rolWorkHistorys.size() > 0) {
        RolWorkHistory rolWorkHistory = rolWorkHistorys.get(0);
        rolWorkHistory.setWorkId(workId);
        rolWorkHistory.setPsnId(psnId);
        rolWorkHistory.setInsId(insId);
        rolWorkHistory.setFromYear(fromYear);
        rolWorkHistory.setFromMonth(fromMonth);
        rolWorkHistory.setToYear(toYear);
        rolWorkHistory.setToMonth(toMonth);
        rolWorkHistory.setIsActive(isActive);
        rolWorkHistoryDao.updateRolWorkHistory(rolWorkHistory);
      }
    } catch (Exception e) {
      logger.error("同步修改工作经历出错:" + e);
    }
  }

  /**
   * 查找工作经历---ROL
   * 
   * @param workId
   * @param psnId
   * @return
   */
  private List<RolWorkHistory> findRolWorkHistory(Long workId, Long psnId) {
    try {
      if (workId == null || psnId == null) {
        return null;
      }
      return rolWorkHistoryDao.findRolWorkHistory(workId, psnId);
    } catch (Exception e) {
      logger.error("查找工作经历出错,workId:" + workId + ",psnId:" + psnId, e);
    }
    return null;
  }

  /**
   * 构建RolWorkHistory对象
   * 
   * @param workId
   * @param psnId
   * @param insId
   * @param fromYear
   * @param fromMonth
   * @param toYear
   * @param toMonth
   * @param isActive
   * @return
   */
  private RolWorkHistory createRolWorkHistory(Long workId, Long psnId, Long insId, Long fromYear, Long fromMonth,
      Long toYear, Long toMonth, Long isActive) {
    RolWorkHistory rolWorkHistory = new RolWorkHistory();
    rolWorkHistory.setWorkId(workId);
    rolWorkHistory.setPsnId(psnId);
    rolWorkHistory.setInsId(insId);
    rolWorkHistory.setFromYear(fromYear);
    rolWorkHistory.setFromMonth(fromMonth);
    if (toYear != null) {
      rolWorkHistory.setToYear(toYear);
    }
    if (toMonth != null) {
      rolWorkHistory.setToMonth(toMonth);
    }
    rolWorkHistory.setIsActive(isActive);
    return rolWorkHistory;
  }

  // 添加工作经历权限
  private void addWorkCnf(Long psnId, Long workId, Integer anyUser) throws WorkHistoryException {
    PsnConfigWork cnfWork = new PsnConfigWork();
    cnfWork.getId().setWorkId(workId);

    cnfWork.setAnyUser(anyUser);
    // 查看权限 & 数据内容有无
    cnfWork.setAnyView(cnfWork.getAnyUser());
    try {
      psnCnfService.save(psnId, cnfWork);
    } catch (com.smate.core.base.exception.ServiceException e) {
      logger.error("保存工作经历权限出错", e);
    }
  }

  @Override
  public String delWorkHistory(Long workId, Long psnId) throws WorkHistoryException {
    try {
      WorkHistory work = workHistoryDao.get(workId);
      if (work != null && work.getPsnId().longValue() == psnId.longValue()) {
        // 自己才能删除自己的工作经历
        workHistoryDao.deleteWorkHistory(workId);
        // 删除权限
        PsnConfigWork cnfWork = new PsnConfigWork();
        cnfWork.getId().setWorkId(workId);
        psnCnfService.del(psnId, cnfWork);
        // 更新人员信息完整度
        personManager.refreshComplete(psnId);
        // 删除评价信息
        // friendService.delPersonFappraisal(psnId, workId);
        // 同步删除rol库的人员工作经历
        rolWorkHistoryDao.deleteWorkHistory(workId, psnId);
        // 删除个人信息补充过度中相应数据
        // psnInfoFillContentDao.deleteByPsnIdSaveId(psnId, workId);
        // 标记人员信息需要同步
        RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
        PsnRefreshUserInfo psnRefInfo = psnRefreshUserInfoDao.get(psnId);
        if (rsp == null) {
          rsp = new RcmdSyncPsnInfo(psnId);
        }
        if (psnRefInfo == null) {
          psnRefInfo = new PsnRefreshUserInfo(psnId);
        }
        rsp.setWorkFlag(1);
        rsp.setInsFlag(1);
        psnRefInfo.setIns(1);
        rcmdSyncPsnInfoDao.save(rsp);
        psnRefreshUserInfoDao.save(psnRefInfo);
        // 如果删除的是首要工作单位，则从新设置人员的首要工作经历
        if (work.getIsPrimary() == 1) {
          workHistoryInsInfoService.checkWorkHistoryInsInfo(work);
          this.resetPsnInsInfo(psnId);
        }
        addInsPsnCount(work.getInsName(), 2);
        return "success";
      } else {
        return "notFind";
      }
    } catch (Exception e) {
      logger.error("删除工作经历出错，psnId=" + psnId + ", workId=" + workId, e);
      return "error";
    }

  }

  @Override
  public PersonProfileForm buildPsnWorkHistorySelector(PersonProfileForm form) throws WorkHistoryException {
    List<WorkHistory> psnWorkList = workHistoryDao.findListByPersonId(form.getPsnId());
    // Map<Long, String> workSelect = new HashMap<Long, String>();
    // Map<Long, String> workAddr = new HashMap<Long, String>();
    Person psn = form.getPsnInfo();
    if (psnWorkList != null && psnWorkList.size() > 0) {
      for (WorkHistory work : psnWorkList) {
        String insName = StringUtils.isBlank(work.getInsName()) ? "" : work.getInsName();
        String department = StringUtils.isBlank(work.getDepartment()) ? "" : work.getDepartment();
        String position = StringUtils.isBlank(work.getPosition()) ? "" : work.getPosition();
        StringBuilder workStr = new StringBuilder();
        if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
          workStr.append(insName + department);
        } else {
          workStr.append(insName + ", " + department);
        }
        if (StringUtils.isNotBlank(position)) {
          if (StringUtils.isNotBlank(workStr.toString())) {
            workStr.append(", " + position);
          } else {
            workStr.append(position);
          }
        }
        work.setWorkInsInfoStr(workStr.toString());
        // workSelect.put(work.getWorkId(), workStr.toString());
        String addr = this.buildPsnWorkHistoryAddr(work);
        if (addr != null) {
          // workAddr.put(work.getWorkId(), addr);
          work.setWorkInsAddr(addr);
        }
        this.setPsnCurrentWork(psn, work, form, workStr.toString());
      }
      // form.setWorkSelect(workSelect);
      // form.setWorkAddr(workAddr);
      form.setWorkList(psnWorkList);
    }
    return form;
  }


  @Override
  public List<WorkHistory> buildSimplePsnWorkHistorySelector(Long psnId) throws WorkHistoryException {
    List<WorkHistory> psnWorkList = workHistoryDao.findListByPersonId(psnId);
    List<WorkHistory> showWorkList = new ArrayList<>();
    if (psnWorkList != null && psnWorkList.size() > 0) {
      for (WorkHistory work : psnWorkList) {
        WorkHistory newWork = new WorkHistory();
        String insName = StringUtils.isBlank(work.getInsName()) ? "" : work.getInsName();
        String department = StringUtils.isBlank(work.getDepartment()) ? "" : work.getDepartment();
        String position = StringUtils.isBlank(work.getPosition()) ? "" : work.getPosition();
        StringBuilder workStr = new StringBuilder();
        if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
          workStr.append(insName + department);
        } else {
          workStr.append(insName + ", " + department);
        }
        if (StringUtils.isNotBlank(position)) {
          if (StringUtils.isNotBlank(workStr.toString())) {
            workStr.append(", " + position);
          } else {
            workStr.append(position);
          }
        }
        newWork.setWorkInsInfoStr(workStr.toString());
        newWork.setWorkId(work.getWorkId());
        showWorkList.add(newWork);
      }
    }
    return showWorkList;
  }

  /**
   * 构建工作经历对应的地区
   * 
   * @param work
   * @return
   */
  private String buildPsnWorkHistoryAddr(WorkHistory work) {
    Long insId = work.getInsId();
    // Map<Long, String> addrMap = new HashMap<Long, String>();
    if (insId != null) {
      Long regionId = institutionDao.findInsRegionId(insId);
      work.setRegionId(regionId);
      String zhPsnRegionName = "";
      String enPsnRegionName = "";
      int count = 0;
      while (regionId != null) {
        count++;
        if (count > 5) {
          break;
        }
        ConstRegion cre = this.constRegionDao.findRegionNameById(regionId);
        if (cre != null) {
          regionId = cre.getSuperRegionId();
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
      // addrMap.put(work.getWorkId(),
      // StringUtils.isNotBlank(zhPsnRegionName) ? zhPsnRegionName :
      // enPsnRegionName);
      return StringUtils.isNotBlank(zhPsnRegionName) ? zhPsnRegionName : enPsnRegionName;
    } else {
      work.setRegionId(156L);
      if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
        return LocaleConsts.default_Region_Name_EN;
      } else {
        return LocaleConsts.default_Region_Name;
      }
    }
  }

  @Override
  public PersonProfileForm buildPsnWorkHistoryListInfo(PersonProfileForm form) throws WorkHistoryException {
    List<WorkHistory> workList = workHistoryDao.findListByPersonId(form.getPsnId());
    if (!CollectionUtils.isEmpty(workList)) {
      for (WorkHistory work : workList) {
        work.setWorkDesc(this.buildWorkDesc(work));
        // 单位对应的图片
        // if (work.getInsId() != null) {
        // work.setInsImgPath(domainScm + "/insLogo/" + work.getInsId() + ".jpg");
        // }
        /**
         * 需求变更(SCM-23894):
         * 由于生产数据库中sie的ins_portal存在2000多条数据,而我们的insLogo静态文件夹中只有一千多条,所以采用读取sie表ins_portal的数据,由于我们个人版库中ins_portal
         * 是sie表ins_portal的视图,所以在实际操作时读取该表的数据(视图的数据会跟着基表的数据变动而变动),若通过id能够获取到则设置现有图片,若获取不到则设置默认
         */
        if (NumberUtils.isNotNullOrZero(work.getInsId())) {
          InsPortal insPortal = insPortalDao.get(work.getInsId());
          String insImgPath = insPortal != null ? insPortal.getLogo() : "";
          work.setInsImgPath(StringUtils.isNotEmpty(insImgPath) ? domainScm + insImgPath : null);
        }
      }
    }
    form.setWorkList(workList);
    return form;
  }

  /**
   * 构建工作经历信息 ： 部门、职称、开始年月-结束年月
   * 
   * @param work
   * @return
   */
  private String buildWorkDesc(WorkHistory work) {
    String department = StringUtils.isBlank(work.getDepartment()) ? "" : work.getDepartment();
    String position = StringUtils.isBlank(work.getPosition()) ? "" : work.getPosition();
    Long fromYear = work.getFromYear();
    Long toYear = work.getToYear();
    Long fromMonth = work.getFromMonth();
    Long toMonth = work.getToMonth();
    Long isActive = work.getIsActive() == null ? 0l : work.getIsActive();
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
          if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
            workDesc.append(" - " + LocaleConsts.TO_PRESENT_EN);
          } else {
            workDesc.append(" - " + LocaleConsts.TO_PRESENT);
          }
        }
      }
    }
    // SCM-12698
    /*
     * if (StringUtils.isNotBlank(work.getDescription())) { if
     * (StringUtils.isNotBlank(workDesc.toString())) { workDesc.append(", " + work.getDescription()); }
     * else { workDesc.append(work.getDescription()); } }
     */
    return workDesc.toString();
  }

  /**
   * 设置当前人员关联的工作经历
   * 
   * @param psn
   * @param work
   * @param form
   */
  private void setPsnCurrentWork(Person psn, WorkHistory work, PersonProfileForm form, String workStr) {
    // if (psn != null) {
    // if (psn.getInsId() != null) {
    // if (work.getInsId() != null && Long.compare(work.getInsId(),
    // psn.getInsId()) == 0) {
    // form.setCurrentWorkId(work.getWorkId());
    // form.setCurrentWorkStr(workStr);
    // }
    // } else if (StringUtils.isNotBlank(psn.getInsName())) {
    // if (psn.getInsName().equals(work.getInsName())) {
    // form.setCurrentWorkId(work.getWorkId());
    // form.setCurrentWorkStr(workStr);
    // }
    // }
    // }
    if (work != null) {
      if (work.getIsPrimary() != null && work.getIsPrimary() == 1) {
        form.setCurrentWorkId(work.getWorkId());
        form.setCurrentWorkStr(workStr);
      }
    }
  }

  /**
   * 删除当前人员使用的工作经历后设置最新的工作经历为人员当前工作经历
   * 
   * @param psnId
   */
  @Override
  public void resetPsnInsInfo(Long psnId) {
    List<WorkHistory> workList = workHistoryDao.findListByPersonId(psnId);
    PersonProfileForm form = new PersonProfileForm();
    if (workList != null && workList.size() > 0) {
      form.setWorkId(workList.get(0).getWorkId());
    }
    // 若人员工作经历都删除了，将person表中单位信息也清除
    Person psn = personProfileDao.get(psnId);
    form.setPsnId(psnId);
    form.setTitolo(psn.getTitolo());
    personManager.savePsnInfoNew(form);
  }

  @Override
  public boolean isOwnerOfWorkHistory(Long psnId, Long workId) throws WorkHistoryException {
    return workHistoryDao.isOwnerOfWorkHistory(psnId, workId);
  }

  @Override
  public Long isPrimaryWorkHistory(Long psnId, Long workId) throws WorkHistoryException {
    if (workId == null || psnId == null) {
      return 0L;
    }
    Long result = workHistoryDao.isPrimaryWorkHistory(psnId, workId);
    if (result == null) {
      return 0L;
    }
    return result;
  }

  @Override
  public List<Long> findWorkByPsnId() throws ServiceException {
    try {
      List<Long> result = null;
      Long userPsnId = SecurityUtils.getCurrentUserId();
      result = workHistoryDao.findWorkByPrimary(userPsnId);// 如果work是首要
      if (CollectionUtils.isEmpty(result)) {
        result = workHistoryDao.findEduByPrimary(userPsnId);// 如果edu是首要
      }
      if (CollectionUtils.isEmpty(result)) {
        result = workHistoryDao.findWorkByPsnId(userPsnId);// 如果是当前
      }
      if (CollectionUtils.isEmpty(result)) {// 按最近日期
        result = new ArrayList<Long>();
        Long workInsId = workHistoryDao.getWorkInsIdByLastDate(userPsnId);
        if (workInsId != null && workInsId > 0) {
          result.add(workInsId);
        } else {
          Long eduInsId = workHistoryDao.getEduInsIdByLastDate(userPsnId);
          if (eduInsId != null && eduInsId > 0) {
            result.add(eduInsId);
          }
        }
      }
      return result;
    } catch (Exception e) {
      logger.error("获取指定人员的所有工作单位ID出错", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean hasPsnConfigWorkLost(Long psnId, Long cnfId) throws WorkHistoryException {
    return workHistoryDao.checkPsnConfigWorkLost(psnId, cnfId);
  }

  @Override
  public WorkHistory getWorkHistoryByPsnId(Long psnId) throws WorkHistoryException {
    try {
      if (!this.isWorkHistoryExit(psnId)) {
        return null;
      }
      return workHistoryDao.getWorkHistoryByPsnId(psnId);
    } catch (Exception e) {
      logger.error("获取人员工作经历出错，psnId={}", psnId);
      throw new WorkHistoryException(e);
    }
  }

}
