package com.smate.center.batch.service.psn;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnInsDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnInsSns;

/**
 * 
 * 
 */
@Service("psnInsService")
@Transactional(rollbackFor = Exception.class)
public class PsnInsServiceImpl implements PsnInsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnInsDao psnInsDao;

  @Override
  public void addPsnIns(PsnInsSns psnIns) throws ServiceException {

    try {
      psnInsDao.save(psnIns);
    } catch (Exception e) {

      logger.error("同步操作，保存PsnIns失败！", e);
      throw new ServiceException(e);

    }

  }

  @Override
  public void updatePsnIns(Long fromPsnId, Long toPsnId, Long insId) throws ServiceException {
    try {
      psnInsDao.updatePsnIns(fromPsnId, toPsnId, insId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  /**
   * 删除人员与单位关系.
   * 
   * @param psnId
   * @param insId
   * @throws ServiceException
   */
  public void delPsnIns(Long psnId, Long insId) throws ServiceException {
    try {
      psnInsDao.deletePsnIns(psnId, insId);
    } catch (Exception e) {

      logger.error("删除人员与单位关系失败！", e);
      throw new ServiceException(e);

    }

  }

  @Override
  public void delPsnIns(PsnInsSns psnIns) throws ServiceException {
    psnInsDao.delete(psnIns);
  }

  @Override
  public PsnInsSns findPsnInsSns(Long psnId, Long insId) throws ServiceException {
    try {
      return psnInsDao.findPsnInsSns(psnId, insId);
    } catch (Exception e) {
      logger.error("获取人员与单位关系失败！", e);
      throw new ServiceException(e);

    }
  }

  @Override
  public void savePsnInsSns(PsnInsSns psnIns) throws ServiceException {
    try {
      psnInsDao.save(psnIns);
    } catch (Exception e) {
      logger.error("保存人员与单位关系失败！", e);
      throw new ServiceException(e);

    }

  }

  @Override
  public List<Long> findInsIdsByPsnId(Long psnId) throws ServiceException {
    try {
      return psnInsDao.findInsIdsByPsnId(psnId);
    } catch (Exception e) {
      logger.error("人员与单位关系的单位ID列表失败！", e);
      throw new ServiceException(e);

    }

  }

  /**
   * 所有insid
   */
  @Override
  public List<Long> findPsnInsIdsByPsnId(Long psnId) throws ServiceException {
    try {
      return psnInsDao.getPsnInsSnsInsIds(psnId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> findAllowSubmitInsIdsByPsnId(Long psnId) throws ServiceException {
    try {
      return psnInsDao.findAllowSubmitInsIdsByPsnId(psnId);
    } catch (Exception e) {
      logger.error("人员与单位关系的单位ID列表(允许提交成果)失败！", e);
      throw new ServiceException(e);

    }
  }

  @Override
  public void syncOldPsnIns(Map<String, Object> oldData, boolean isResearch) throws ServiceException {
    try {
      Long psnId = Long.valueOf(oldData.get("PSN_ID").toString());
      Long insId = Long.valueOf(oldData.get("INS_ID").toString());
      Long notInJob = oldData.get("NOT_IN_JOB") == null ? 0 : Long.valueOf(oldData.get("NOT_IN_JOB").toString());
      Integer allowSubmitPub = 0;
      if (isResearch) {
        allowSubmitPub = 1;
      }
      PsnInsSns psnInsSns = new PsnInsSns(psnId, insId, notInJob, allowSubmitPub);
      this.psnInsDao.save(psnInsSns);
    } catch (Exception e) {
      logger.error("同步V2.6人员与单位关系数据", e);
      throw new ServiceException(e);

    }
  }

  /**
   * 查找个人加入单位.
   * 
   * @param psnId
   * @return
   */
  public List<PsnInsSns> findPsnInsSnsList(Long psnId) {
    return this.psnInsDao.findPsnInsSnsList(psnId);
  }
}
