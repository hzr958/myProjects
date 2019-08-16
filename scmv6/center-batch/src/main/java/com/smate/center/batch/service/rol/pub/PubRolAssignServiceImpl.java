package com.smate.center.batch.service.rol.pub;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.constant.PublicationRolStatusEnum;
import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.ClearTaskNoticeEvent;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.service.pub.mq.PubAssignMessage;
import com.smate.center.batch.service.pub.mq.PubAssignMessageKindEnum;
import com.smate.center.batch.service.pub.mq.PubAssignMessageModeEnum;
import com.smate.center.batch.service.pub.mq.PubRolPsnStatRefreshProducer;

/**
 * 成果指派服务.
 * 
 * @author liqinghua
 * 
 */
@Service("pubRolAssignService")
@Transactional(rollbackFor = Exception.class)
public class PubRolAssignServiceImpl implements PubRolAssignService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubRolPersonService pubRolPersonService;
  @Autowired
  private PubAssignMatchService pubAssignMatchService;
  @Autowired
  private PubAssignCnkiMatchService pubAssignCnkiMatchService;
  @Autowired
  private PubAssignCniprMatchService pubAssignCniprMatchService;
  @Autowired
  private PubAssignCnkiPatMatchService pubAssignCnkiPatMatchService;
  @Autowired
  private PubAssignSpsMatchService pubAssignSpsMatchService;
  @Autowired
  private PubAssignPubMedMatchService pubAssignPubMedMatchService;
  @Autowired
  private PublicationRolService publicationRolService;
  @Resource(name = "pubRolPsnStatRefreshProducer")
  private PubRolPsnStatRefreshProducer pubRolPsnStatRefreshProducer;
  @Autowired
  private TaskNoticeService taskNoticeService;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private PubAssignEiMatchService pubAssignEiMatchService;

  @Override
  public void doAssignPub(PubAssignMessage message) throws ServiceException {

    Assert.notNull(message.getOpPsnId(), "属性opPsnId不能为空.");
    Assert.notNull(message.getInsId(), "属性insId不能为空.");
    Integer messageKind = message.getMessageKind();
    Assert.notNull(messageKind, "属性MessageKind不能为空.");
    Integer mode = message.getMode();
    Assert.notNull(mode, "属性Mode不能为空.");

    try {
      if (mode == PubAssignMessageModeEnum.byPsn) {
        // 按人指派成果
        if (messageKind == PubAssignMessageKindEnum.AddNewPsn || messageKind == PubAssignMessageKindEnum.ApproveNewPsn
            || messageKind == PubAssignMessageKindEnum.UpdatePsn
            || messageKind == PubAssignMessageKindEnum.CONFIRM_PUB) {
          // 人员如果不在单位，直接跳过
          if (!rolPsnInsDao.isPsnInIns(message.getPsnId(), message.getInsId())) {
            return;
          }
          // isi ===============现在EI暂时与ISI使用同样的指派流程
          pubAssignMatchService.assignByPsn(message.getPsnId(), message.getInsId());
          // cnki
          pubAssignCnkiMatchService.assignByPsn(message.getPsnId(), message.getInsId());
          // cnipr
          pubAssignCniprMatchService.assignByPsn(message.getPsnId(), message.getInsId());
          // cnkipat
          pubAssignCnkiPatMatchService.assignByPsn(message.getPsnId(), message.getInsId());
          // scopus
          pubAssignSpsMatchService.assignByPsn(message.getPsnId(), message.getInsId());
          // pubMed
          pubAssignPubMedMatchService.assignByPsn(message.getPsnId(), message.getInsId());
          // 更新统计数
          pubRolPsnStatRefreshProducer.sendRefreshMessage(message.getInsId(), message.getPsnId());

        } else if (messageKind == PubAssignMessageKindEnum.DeletePsn) {
          // 删除指派关系
          this.pubRolPersonService.removeAssignByPsnId(message.getPsnId(), message.getInsId());
          logger.info("删除指派关系：psnId={},insId={}", new Object[] {message.getPsnId(), message.getInsId()});
        }
      } else if (mode == PubAssignMessageModeEnum.byPub) {// 按成果指派
        PublicationRol pub = publicationRolService.getPublicationById(message.getPubId());
        if (pub == null || !pub.getInsId().equals(message.getInsId())
            || !(pub.getStatus() == PublicationRolStatusEnum.APPROVED
                || pub.getStatus() == PublicationRolStatusEnum.NEED_CONFIRM)) {
          return;
        }
        // ISI
        pubAssignMatchService.assignByPub(pub, message.getInsId());
        // EI
        pubAssignEiMatchService.assignByPub(pub, message.getInsId());
        // CNKI
        pubAssignCnkiMatchService.assignByPub(pub, message.getInsId());
        // CNIPR
        pubAssignCniprMatchService.assignByPub(pub, message.getInsId());
        // CNKI-PAT
        pubAssignCnkiPatMatchService.assignByPub(pub, message.getInsId());
        // scopus
        pubAssignSpsMatchService.assignByPub(pub, message.getInsId());
        // pubMed
        pubAssignPubMedMatchService.assignByPub(pub, message.getInsId());
        // 更新统计数
        pubRolPsnStatRefreshProducer.sendPubRefreshMessage(message.getInsId(), pub.getId());
      }

      // 清理缓存中的任务统计数
      taskNoticeService.clearTaskNotice(ClearTaskNoticeEvent.getInstance(message.getInsId(), 0, 0, 1));
    } catch (Exception e) {
      logger.error("doAssignPub错误:message={}", message);
      throw new ServiceException(e);
    }
  }
}
