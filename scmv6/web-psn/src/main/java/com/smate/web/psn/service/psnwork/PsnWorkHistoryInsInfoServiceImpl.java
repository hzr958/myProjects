package com.smate.web.psn.service.psnwork;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.psn.dao.psnwork.PsnWorkHistoryInsInfoDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.PsnInfoDaoException;
import com.smate.web.psn.exception.WorkHistoryException;
import com.smate.web.psn.model.consts.LocaleConsts;
import com.smate.web.psn.model.workhistory.PsnWorkHistoryInsInfo;

/**
 * 个人工作经历单位信息服务接口实现
 * 
 * @author Administrator
 *
 */
@Service("psnWorkHistoryInsInfoService")
@Transactional(rollbackFor = Exception.class)
public class PsnWorkHistoryInsInfoServiceImpl implements PsnWorkHistoryInsInfoService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Value("${domainscm}")
  private String domianScm;
  @Autowired
  private InsPortalDao insPortalDao;

  /**
   * 保存<科研之友用>
   */
  @Override
  public void savePsnWorkHistoryInsInfo(PsnWorkHistoryInsInfo psnWorkHistoryInsInfo) throws PsnException {
    PsnWorkHistoryInsInfo psnWork = getPsnWorkHistoryInsInfo(psnWorkHistoryInsInfo.getPsnId());
    if (psnWork == null) {
      psnWorkHistoryInsInfoDao.save(psnWorkHistoryInsInfo);
    } else {
      psnWork.setInsId(psnWorkHistoryInsInfo.getInsId());
      psnWork.setInsNameZh(psnWorkHistoryInsInfo.getInsNameZh());
      psnWork.setInsNameEn(psnWorkHistoryInsInfo.getInsNameEn());
      psnWork.setDepartmentZh(psnWorkHistoryInsInfo.getDepartmentZh());
      psnWork.setDepartmentEn(psnWorkHistoryInsInfo.getDepartmentEn());
      psnWork.setPositionZh(psnWorkHistoryInsInfo.getPositionZh());
      psnWork.setPositionEn(psnWorkHistoryInsInfo.getPositionEn());
      psnWorkHistoryInsInfoDao.save(psnWork);
    }

  }

  /**
   * 通过psnId获取人员工作经历单位信息<科研之友用>
   */
  @Override
  public PsnWorkHistoryInsInfo getPsnWorkHistoryInsInfo(Long psnId) throws PsnException {

    try {
      return psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psnId);
    } catch (PsnInfoDaoException e) {
      logger.error("通过insId,psnId获取人员工作经历单位信息出错", e);
      return null;
    }
  }

  @Override
  public List<WorkHistory> getPsnWorkHistory(Long psnId) throws PsnException {
    List<WorkHistory> workList = workHistoryDao.findWorkInsByPsnId(psnId);
    if (!CollectionUtils.isEmpty(workList)) {
      for (WorkHistory work : workList) {
        work.setWorkDesc(this.buildWorkDesc(work));
        // 单位对应的图片
        if (NumberUtils.isNotNullOrZero(work.getInsId())) {
          InsPortal insPortal = insPortalDao.get(work.getInsId());
          String insImgPath = insPortal != null ? insPortal.getLogo() : "";
          work.setInsImgPath(StringUtils.isNotEmpty(insImgPath) ? domianScm + insImgPath : null);
        }
      }
    }
    return workList;
  }

  @Override
  public WorkHistory getWorkHistoryByWorkId(Long psnId, Long workId) throws PsnException {
    WorkHistory workHistory = workHistoryDao.findPsnWorkHistory(psnId, workId);
    if (workHistory != null && workHistory.getInsId() != null) {
      // 单位对应的图片
      workHistory.setInsImgPath(domianScm + "/insLogo/" + workHistory.getInsId() + ".jpg");
    }
    return workHistory;
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
    return workDesc.toString();
  }

  // 获取首要内容
  @Override
  public WorkHistory getFirstWork(Long psnId) throws WorkHistoryException {
    return workHistoryDao.getFirstWork(psnId);
  }

  @Override
  public WorkHistory findWorkHistoryById(Long workId) throws PsnException {
    return workHistoryDao.get(workId);
  }

  @Override
  public void deletePsnWorkHistoryInsInfo(Long psnId, Long insId, String insName) throws PsnException {
    try {
      psnWorkHistoryInsInfoDao.deletePsnWorkHistoryInsInfo(psnId, insId, insName);
    } catch (Exception e) {
      logger.error("删除人员首要单位信息出错,psnId=" + psnId + ",insId=" + insId + ",insName=" + insName, e);
    }

  }
}
