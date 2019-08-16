package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.constant.PubAssignSyncMessageEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubAssignSyncMessage;
import com.smate.center.batch.service.psn.PsnStatisticsService;
import com.smate.center.batch.service.pub.rcmd.PublicationConfirmService;


/**
 * 成果指派MQ消息，包括成果XML.
 * 
 * @author liqinghua
 * 
 */
@Component("pubAssignSyncMessageConsumer")
public class PubAssignSyncMessageConsumer {

  /**
   * 
   */
  private static final long serialVersionUID = 4360558586641085937L;

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
        // 指派
        logger.debug(String.format("接受到指派成果XML数据pubId:%s,insId:%s", msg.getPsnId(), msg.getInsId()));
        // 先保存指派
        this.publicationConfirmService.receivePubAssignFromInsSyncMessage(msg, PubAssignSyncMessageEnum.ASSIGN);
        // 如果用户成果库中已经存在严格重复成果，自动给用户确认，但是严格查重需要更新个人库的引用次数和收录情况.
        this.publicationConfirmService.autoComfirmDupPub(msg);
      } else if (PubAssignSyncMessageEnum.DIS_ASSIGN.equals(actionType)) {
        // 删除指派
        logger.debug(String.format("接受到删除指派成果数据pubId:%s,insId:%s", msg.getPsnId(), msg.getInsId()));
        this.publicationConfirmService.receivePubAssignFromInsSyncMessage(msg, PubAssignSyncMessageEnum.DIS_ASSIGN);
      } else if (PubAssignSyncMessageEnum.SYNC_ASSIGN_XML.equals(actionType)) {
        // 指派同步XML
        this.publicationConfirmService.receivePubAssignFromInsSyncMessage(msg,
            PubAssignSyncMessageEnum.SYNC_ASSIGN_XML);
      } else if (PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR.equals(actionType)) {
        // 同步XML失败
        this.publicationConfirmService.receivePubAssignFromInsSyncMessage(msg,
            PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR);
      }
      // 同步待确认成果数.
      if (msg.getPsnId() != null) {
        Long pendingCfmPubNum = publicationConfirmService.getPubConfirmCountByPsnId(msg.getPsnId());
        pendingCfmPubNumMessageProducer.syncPendingCfmPubNum(msg.getPsnId(), pendingCfmPubNum.intValue());
        // scm-6710 成果指派后更新psnStatistics的待确认成果数
        psnStatisticsService.updateStatisticsPcfPub(msg.getPsnId(), pendingCfmPubNum.intValue());
      }

    } catch (Exception e) {
      logger.error("接受成果指派消息错误", e);
      throw new ServiceException(e);
    }

  }
}
