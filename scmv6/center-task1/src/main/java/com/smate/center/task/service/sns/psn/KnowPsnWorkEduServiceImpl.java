package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.center.task.dao.sns.psn.PsnKnowWorkEduDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.PsnKnowWorkEdu;

/**
 * @author lcw
 * 
 */
@Service("knowPsnWorkEduService")
@Transactional(rollbackFor = Exception.class)
public class KnowPsnWorkEduServiceImpl implements KnowPsnWorkEduService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnKnowWorkEduDao psnKnowWorkEduDao;

  @Autowired
  InstitutionDao institutionDao;

  @Override
  public void deleteAll() throws ServiceException {
    psnKnowWorkEduDao.deleteAll();
  }

  @Override
  public void matchKnowPsnWorkEdu(List<PsnKnowWorkEdu> psnWorkEduList) throws ServiceException {
    try {
      if (CollectionUtils.isEmpty(psnWorkEduList))
        return;
      for (PsnKnowWorkEdu psnKnowWorkEdu : psnWorkEduList) {
        psnKnowWorkEduDao.delPsnKnowWorkEdu(psnKnowWorkEdu.getPsnId());
        psnKnowWorkEduDao.save(psnKnowWorkEdu);
      }
    } catch (Exception e) {
      logger.error("Know保存人员工作教育经历出错", e);
    }
  }

}
