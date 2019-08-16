package com.smate.center.task.rcmd.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubAssignSyncMessageEnum;
import com.smate.center.task.model.rol.quartz.PubAssignSyncMessage;
import com.smate.center.task.service.rcmd.quartz.PublicationConfirmService;
import com.smate.center.task.single.service.person.PsnStatisticsService;

@Component("pubAssignSyncMessageConsumer")
public class PubAssignSyncMessageConsumer {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationConfirmService publicationConfirmService;
  @Autowired
  private PendingCfmPubNumMessageProducer pendingCfmPubNumMessageProducer;
  @Autowired
  private PsnStatisticsService psnStatisticsService;

  public void receive(PubAssignSyncMessage msg) throws ServiceException {
    try {
      PubAssignSyncMessageEnum actionType = msg.getActionType();
      if (PubAssignSyncMessageEnum.ASSIGN.equals(actionType)) {
        logger.debug(String.format("接受到指派成果XML数据pubId:%s,insId:%s", msg.getPsnId(), msg.getInsId()));
        publicationConfirmService.receivePubAssignFromInsSyncMessage(msg, PubAssignSyncMessageEnum.ASSIGN);
        // 如果用户成果库中已经存在严格重复成果，自动给用户确认，但是严格查重需要更新个人库的引用次数和收录情况.
        publicationConfirmService.autoComfirmDupPub(msg);
      } else if (PubAssignSyncMessageEnum.DIS_ASSIGN.equals(actionType)) {
        // 删除指派
        logger.debug(String.format("接受到删除指派成果数据pubId:%s,insId:%s", msg.getPsnId(), msg.getInsId()));
        publicationConfirmService.receivePubAssignFromInsSyncMessage(msg, PubAssignSyncMessageEnum.DIS_ASSIGN);
      } else if (PubAssignSyncMessageEnum.SYNC_ASSIGN_XML.equals(actionType)) {
        // 指派同步XML
        this.publicationConfirmService.receivePubAssignFromInsSyncMessage(msg,
            PubAssignSyncMessageEnum.SYNC_ASSIGN_XML);
      } else if (PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR.equals(actionType)) {
        // 同步XML失败
        publicationConfirmService.receivePubAssignFromInsSyncMessage(msg,
            PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR);
      }

      if (msg.getPsnId() != null) {
        Long pendingCfmPubNum = publicationConfirmService.getPubConfirmCountByPsnId(msg.getPsnId());
        pendingCfmPubNumMessageProducer.syncPendingCfmPubNum(msg.getPsnId(), pendingCfmPubNum.intValue());
        psnStatisticsService.updateStatisticsPcfPub(msg.getPsnId(), pendingCfmPubNum.intValue());
      }

    } catch (Exception e) {
      logger.error("接受成果指派消息错误", e);
      throw new ServiceException(e);
    }

  }
}
