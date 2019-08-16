package com.smate.center.task.service.rol.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rol.quartz.PubRolSubmissionDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PubRolSubmission;
import com.smate.center.task.model.rol.quartz.PubRolSubmissionStatusEnum;
import com.smate.center.task.model.rol.quartz.PublicationRol;
import com.smate.center.task.rcmd.jms.PubRolSubmissionStatRefreshProducer;
import com.smate.center.task.single.enums.pub.PublicationOperationEnum;
import com.smate.center.task.single.service.pub.PublicationLogService;

@Service("pubRolSubmissionService")
@Transactional(rollbackFor = Exception.class)
public class PubRolSubmissionServiceImpl implements PubRolSubmissionService {
  @Autowired
  private PubRolSubmissionDao pubRolSubmissionDao;
  @Autowired
  private PublicationRolService publicationRolService;
  @Autowired
  private PublicationLogService publicationLogService;
  @Autowired
  private PubInsSyncRolService pubInsSyncRolService;
  @Autowired
  private PubRolSubmissionStatRefreshProducer pubRolSubmissionStatRefreshProducer;

  /**
   * 成果确认后，把确认的成果设置为提交.
   * 
   * @param snsPubId
   * @param insPubId
   * @param psnId
   * @param insId
   * @throws ServiceException
   */
  @Override
  public void submitConfirmPub(Long snsPubId, Long insPubId, Long psnId, Long insId) throws ServiceException {
    try {
      PubRolSubmission sub = pubRolSubmissionDao.getSubmissionBySNSPubId(snsPubId, insId);
      // 查重
      if (sub == null) {
        // 不存在，插入
        sub = this.pubRolSubmissionDao.create(psnId, snsPubId, insId, psnId, PubRolSubmissionStatusEnum.APPROVED);
      } else {
        // 如果以前已经提交过成果被单位拒绝或者允许撤销，则只需要将前提交信息重置
        sub.setSubmitStatus(PubRolSubmissionStatusEnum.APPROVED);
        sub.setWithdrawReqConfirmDate(null);
        sub.setWithdrawReqDate(null);
        sub.setWithdrawStatus(null);
        sub.setInsConfirmDate(null);
        sub.setReSubmitDate(null);
        sub.setRealInsPub(null);
        sub.setInsConfirmDate(null);
      }
      // 插入关系
      PublicationRol insPub = publicationRolService.getPublicationById(insPubId);
      sub.setInsPub(insPub);
      this.pubRolSubmissionDao.save(sub);
      this.publicationLogService.logOp(snsPubId, psnId, insId, PublicationOperationEnum.Approve, null);
      // 冗余提交标记
      this.pubInsSyncRolService.updateSnsPubSubmittedFlag(snsPubId, insId, true);
      // 累计提交数.
      // 1.更新提交统计数字 (pub_submisstion_stat)
      pubRolSubmissionStatRefreshProducer.sendRefreshMessage(insId, psnId);
    } catch (Exception e) {

    }
  }

}
