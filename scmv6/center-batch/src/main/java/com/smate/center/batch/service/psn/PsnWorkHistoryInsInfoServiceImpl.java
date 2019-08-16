package com.smate.center.batch.service.psn;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnWorkHistoryInsInfoDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnWorkHistoryInsInfo;

/**
 * 个人工作经历单位信息服务类
 * 
 * @author zk
 * 
 */
@Service("psnWorkHistoryInsInfoService")
@Transactional(rollbackFor = Exception.class)
public class PsnWorkHistoryInsInfoServiceImpl implements PsnWorkHistoryInsInfoService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnWorkHistoryInsInfoDao psnWorkHistoryInsInfoDao;

  /**
   * 保存<科研之友用>
   */
  @Override
  public void savePsnWorkHistoryInsInfo(PsnWorkHistoryInsInfo psnWorkHistoryInsInfo) throws ServiceException {

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
   * 保存<用该方法前请做查重>
   */
  @Override
  public void savePsnWorkHistoryInsInfo(Long psnId, Long insId, String insNameZh, String insNameEn, String departmentZh,
      String departmentEn, String positionZh, String positionEn) throws ServiceException {
    PsnWorkHistoryInsInfo psnWorkHistoryInsInfo = new PsnWorkHistoryInsInfo(psnId, insId, insNameZh, insNameEn,
        departmentZh, departmentEn, positionZh, positionEn);
    psnWorkHistoryInsInfoDao.save(psnWorkHistoryInsInfo);
  }



  /**
   * 通过psnId获取人员工作经历单位信息<科研之友用>
   */
  @Override
  public PsnWorkHistoryInsInfo getPsnWorkHistoryInsInfo(Long psnId) throws ServiceException {

    try {
      return psnWorkHistoryInsInfoDao.getPsnWorkHistoryInsInfo(psnId);
    } catch (Exception e) {
      logger.error("通过psnId获取人员工作经历单位信息出错", e);
      return null;
    }
  }
}
