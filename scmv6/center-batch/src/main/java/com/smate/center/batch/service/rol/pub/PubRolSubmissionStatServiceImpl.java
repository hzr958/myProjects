package com.smate.center.batch.service.rol.pub;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.PubRolSubmissionStatDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubRolSubmissionStat;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果提交统计数.
 * 
 * @author liqinghua
 * 
 */
@Service("pubRolSubmissionStatService")
@Transactional(rollbackFor = Exception.class)
public class PubRolSubmissionStatServiceImpl implements PubRolSubmissionStatService {

  /**
   * 
   */
  private static final long serialVersionUID = 5957981890198625567L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRolSubmissionStatDao pubRolSubmissionStatDao;
  @Autowired
  private InsUnitRolService insUnitRolService;

  @Override
  public void refreshPubRolSubmissionStat(Long insId) throws ServiceException {

    try {
      pubRolSubmissionStatDao.refreshPubSubmission(insId);
    } catch (Exception e) {
      logger.error("更新单位成果提交统计数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void refreshPubRolSubmissionStat(Long insId, Long psnId) throws ServiceException {
    try {
      pubRolSubmissionStatDao.refreshPubSubmission(insId, psnId);
    } catch (Exception e) {
      logger.error("更新单位人员成果统计数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void setPubTotal(Long insId, Long psnId, Long total, Long snsSubmitTotal) throws ServiceException {
    try {
      PubRolSubmissionStat stat = pubRolSubmissionStatDao.findByPsnIdInsId(psnId, insId);
      if (stat == null) {
        stat = new PubRolSubmissionStat(psnId, insId, 0L, total, 0, snsSubmitTotal);
      } else {
        stat.setTotalOutputs(total);
        stat.setSnsSubmitTotal(snsSubmitTotal);
      }
      pubRolSubmissionStatDao.save(stat);
    } catch (Exception e) {
      logger.error("设置个人成果总数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void setSubmitDate(Long insId, Long psnId) throws ServiceException {
    try {
      PubRolSubmissionStat stat = pubRolSubmissionStatDao.findByPsnIdInsId(psnId, insId);
      if (stat == null) {
        stat = new PubRolSubmissionStat(psnId, insId, 0L, 0L, 0, 0L);
      }
      stat.setLastSubmitDate(new Date());
      pubRolSubmissionStatDao.save(stat);
    } catch (Exception e) {
      logger.error("设置个人成果总数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Page<PubRolSubmissionStat> queryUrgencyList(Page<PubRolSubmissionStat> page, Long unitId, String psnName)
      throws ServiceException {
    try {
      Long insId = SecurityUtils.getCurrentInsId();
      this.pubRolSubmissionStatDao.queryUrgencyList(page, insId, unitId, psnName);
      // 获取部门
      if (page.getResult() != null && page.getResult().size() > 0) {
        String unitName = null;
        for (PubRolSubmissionStat stat : page.getResult()) {
          if (unitId != null && unitName == null) {
            unitName = this.insUnitRolService.getInsUnitName(unitId);
            stat.setPsnUnitName(unitName);
          } else if (unitId == null && stat.getPsnUnitId() != null) {
            stat.setPsnUnitName(insUnitRolService.getInsUnitName(stat.getPsnUnitId()));
          } else if (unitId != null) {
            stat.setPsnUnitName(unitName);
          }
        }

      }
      return page;
    } catch (Exception e) {
      logger.error("检索催交成果人员列表", e);
      throw new ServiceException(e);
    }
  }

}
