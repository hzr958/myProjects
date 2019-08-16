package com.smate.center.batch.service.psn;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

@Service("workHistoryService")
@Transactional(rollbackOn = Exception.class)
public class WorkHistoryServiceImpl implements WorkHistoryService {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WorkHistoryDao workHistoryDao;

  @Autowired
  private ResumeService resumeService;

  @Override
  public List<WorkHistory> findWorkHistoryByPsnId(Long psnId) throws SysServiceException {
    try {
      List<WorkHistory> workList = workHistoryDao.findListByPersonId(psnId);
      if (CollectionUtils.isNotEmpty(workList)) {
        for (WorkHistory work : workList) {
          work.setAuthority(resumeService.getWorkAuthority(work.getWorkId()));
        }
      }
      return workList;
    } catch (Exception e) {
      logger.error("findWorkHistoryByPsnId获取指定人员的工作经历单位(带判断权限)出错psnid" + psnId, e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 根据psnId获取该人的首要工作单位名称. * @author yph
   */
  @Override
  public String getPrimaryWorkNameByPsnId(Long psnId) {
    return workHistoryDao.getPrimaryWorkNameByPsnId(psnId);
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
  public boolean isWorkHistoryExit(Long psnId) throws ServiceException {

    try {
      return this.workHistoryDao.isWorkHistoryExit(psnId);
    } catch (Exception e) {
      logger.error("判断用户是否存在教育经历", e);
      throw new ServiceException(e);
    }
  }

}
