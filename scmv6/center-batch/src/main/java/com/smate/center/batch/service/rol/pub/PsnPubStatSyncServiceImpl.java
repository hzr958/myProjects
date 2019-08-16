package com.smate.center.batch.service.rol.pub;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.PubAssignModeEnum;
import com.smate.center.batch.dao.rol.psn.PsnPubSendFlagDao;
import com.smate.center.batch.dao.rol.pub.PubPsnRolDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.InsUnit;
import com.smate.center.batch.model.rol.pub.PubPsnRol;
import com.smate.center.batch.service.psn.InsPersonService;
import com.smate.center.batch.service.pub.mq.PubAssignSyncMessageProducer;
import com.smate.center.batch.service.pub.mq.PubRolPsnStatRefreshProducer;


/**
 * 个人成果统计同步服务.
 * 
 * @author yamingd
 * 
 */
@Service("psnPubStatSyncService")
public class PsnPubStatSyncServiceImpl implements PsnPubStatSyncService {

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPsnRolDao pubPsnRolDao;
  @Autowired
  private InsPersonService insPersonService;
  @Autowired
  private InsUnitRolService insUnitRolService;
  @Resource(name = "pubAssignSyncMessageProducer")
  private PubAssignSyncMessageProducer pubAssignSyncMessageProducer;
  @Autowired
  private PubRolPsnStatRefreshProducer pubRolPsnStatRefreshProducer;
  @Autowired
  private PubRolMemberDao pubRolMemberDao;
  @Autowired
  private PubAssignMatchService pubAssignMatchService;
  @Autowired
  private PubAssignCnkiMatchService pubAssignCnkiMatchService;
  @Autowired
  private PubAssignSpsMatchService pubAssignSpsMatchService;
  @Autowired
  private PsnPubSendFlagDao psnPubSendFlagDao;

  @Override
  public PubPsnRol addPubPsn(Long pubId, Long insId, Long psnId, Integer assignMode, boolean isConfirm, Long pmId)
      throws ServiceException {

    try {
      return this.addPubPsn(pubId, insId, psnId, null, null, assignMode, isConfirm, pmId);
    } catch (Exception e) {
      logger.error("添加成果人员关系错误", e);
      throw new ServiceException("添加成果人员关系错误", e);
    }

  }

  @Override
  public PubPsnRol addPubPsn(Long pubId, Long insId, Long psnId, Float score, Integer seqNo, Integer assignMode,
      boolean isConfirm, Long pmId) throws ServiceException {
    try {
      Integer sendFlag = this.psnPubSendFlagDao.getSendFlag(psnId);
      PubPsnRol pubPsnRol = pubPsnRolDao.getAllStatusPubPsn(pubId, psnId);
      if (pubPsnRol != null) {
        // 之前是等待确认或者已经确认
        if (pubPsnRol.getConfirmResult() == 0 || pubPsnRol.getConfirmResult() == 1) {
          // 修改PMID
          pubPsnRol.setAuthorPMId(pmId);
          pubPsnRol.setScore(score);
          pubPsnRol.setPsnLogin(sendFlag);
          this.pubPsnRolDao.save(pubPsnRol);
        } else {
          // 如果是后台任务重新指派上了，直接退出，要不然用户要重复认领
          if (assignMode != null && PubAssignModeEnum.BACKGROUND_JOB == assignMode) {
            return pubPsnRol;
          }
          // 之前是拒绝，则修改为重新确认状态
          pubPsnRol.setIsSend(0);
          pubPsnRol.setConfirmResult(0);
          pubPsnRol.setScore(score);
          pubPsnRol.setPsnLogin(sendFlag);
          this.pubPsnRolDao.save(pubPsnRol);
        }
        return pubPsnRol;
      }
      pubPsnRol = new PubPsnRol(pubId, insId, psnId, isConfirm ? 1 : 0, assignMode, pmId);
      pubPsnRol.setScore(score);
      pubPsnRol.setSeqNo(seqNo);
      pubPsnRol.setPsnLogin(sendFlag);
      RolPsnIns rolPsnIns = this.insPersonService.findRolPsnIns(insId, psnId);
      if (rolPsnIns != null && rolPsnIns.getUnitId() != null) {
        pubPsnRol.setUnitId(rolPsnIns.getUnitId());
        InsUnit insUnit = insUnitRolService.getInsUnitRolById(rolPsnIns.getUnitId());
        if (insUnit != null)
          pubPsnRol.setParentUnitId(insUnit.getSuperInsUnitId());
      }
      this.pubPsnRolDao.save(pubPsnRol);
      // 更新认领统计数据
      pubRolPsnStatRefreshProducer.sendRefreshMessage(insId, psnId);
      return pubPsnRol;
    } catch (DaoException e) {
      logger.error("添加成果人员关系错误", e);
      throw new ServiceException("添加成果人员关系错误", e);
    }
  }

  @Override
  public PubPsnRol addPubPsn(Long pubId, Long insId, Long psnId, Float score, Integer seqNo, Integer assignMode,
      boolean isConfirm) throws ServiceException {
    try {
      Long pmId = null;
      if (seqNo != null) {
        pmId = this.pubRolMemberDao.getPubMemberPmId(pubId, seqNo);
      }

      return this.addPubPsn(pubId, insId, psnId, score, seqNo, assignMode, isConfirm, pmId);
    } catch (Exception e) {
      logger.error("添加成果人员关系错误", e);
      throw new ServiceException("添加成果人员关系错误", e);
    }
  }

  @Override
  public void removePubPsn(Long pubId, Long insId, Long psnId) throws ServiceException {

    try {
      PubPsnRol pubPsnRol = pubPsnRolDao.getAllStatusPubPsn(pubId, psnId);
      if (pubPsnRol == null) {
        return;
      }
      this.removePubPsn(pubPsnRol);
    } catch (DaoException e) {
      logger.error("删除成果、人员关系错误", e);
      throw new ServiceException("删除成果、人员关系错误", e);
    }
  }

  /**
   * 删除成果与人员关系.
   * 
   * @param pubPsnRol
   * @throws ServiceException
   */
  private void removePubPsn(PubPsnRol pubPsnRol) throws ServiceException {

    try {
      // 删除指派分数
      this.pubAssignMatchService.removePubPsnAssignScore(pubPsnRol.getPubId(), pubPsnRol.getPsnId());
      this.pubAssignCnkiMatchService.removePubPsnAssignScore(pubPsnRol.getPubId(), pubPsnRol.getPsnId());
      this.pubAssignSpsMatchService.removePubPsnAssignScore(pubPsnRol.getPubId(), pubPsnRol.getPsnId());
      pubPsnRolDao.delete(pubPsnRol);

      // 删除智能指派关系
      this.pubAssignMatchService.removePubPsnAssignScore(pubPsnRol.getPubId(), pubPsnRol.getPsnId());
      this.pubAssignCnkiMatchService.removePubPsnAssignScore(pubPsnRol.getPubId(), pubPsnRol.getPsnId());
      // 更新成果认领统计数据
      pubRolPsnStatRefreshProducer.sendRefreshMessage(pubPsnRol.getInsId(), pubPsnRol.getPsnId());
      // 发送MQ消息，删除指派lqh add
      pubAssignSyncMessageProducer.publishDisAssign(pubPsnRol.getPsnId(), pubPsnRol.getInsId(), pubPsnRol.getId(),
          pubPsnRol.getPubId());
    } catch (Exception e) {
      logger.error("removePubPsn删除成果、人员关系错误pubId=" + pubPsnRol.getPubId() + "insId=" + pubPsnRol.getInsId(), e);
    }
  }

  @Override
  public void removePubPsnByPmId(Long pubId, Long pmId) throws ServiceException {

    List<PubPsnRol> pubpsns = this.pubPsnRolDao.getAllStatusPubPsnByPubPmId(pubId, pmId);
    if (pubpsns != null && pubpsns.size() > 0) {
      for (PubPsnRol pubpsn : pubpsns) {
        this.removePubPsn(pubpsn);
      }
    }
  }

  @Override
  public void removePubPsn(Long pubId, Long insId) throws ServiceException {
    try {
      List<PubPsnRol> pubPsnRols = pubPsnRolDao.getAllStatusPubPsnByPubInsId(pubId, insId);
      // 更新认领成果数
      if (pubPsnRols != null && pubPsnRols.size() > 0) {
        for (PubPsnRol pubPsnRol : pubPsnRols) {
          this.removePubPsn(pubPsnRol);
        }
      }
    } catch (Exception e) {
      logger.error("removePubPsn删除成果、人员关系错误pubId=" + pubId + "insId=" + insId, e);
    }
  }

}
