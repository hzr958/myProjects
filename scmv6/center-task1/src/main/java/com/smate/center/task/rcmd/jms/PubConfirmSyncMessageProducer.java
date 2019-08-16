package com.smate.center.task.rcmd.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubAssignSyncMessageEnum;
import com.smate.center.task.model.rol.quartz.PubAssignSyncMessage;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 个人确认成果从SIE同步XML到科研之友.
 * 
 * @author liqinghua
 * 
 */
@Component("pubConfirmSyncMessageProducer")
public class PubConfirmSyncMessageProducer {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final String queueName = "PubConfirmSyncMessage";
  @Autowired
  private PubConfirmSyncMessageConsumer pubConfirmSyncMessageConsumer;

  /**
   * 发布成果指派XML.
   * 
   * @throws ServiceException
   */
  public void publishAssignXml(Long psnId, Long insId, Long insPubId, String pubXml) throws ServiceException {
    try {
      Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
      PubAssignSyncMessage msg =
          new PubAssignSyncMessage(psnId, insId, insPubId, pubXml, PubAssignSyncMessageEnum.SYNC_ASSIGN_XML, nodeId);
      pubConfirmSyncMessageConsumer.receive(msg);
    } catch (Exception e) {
      logger.error("发布成果指派XML错误.", e);
      throw new ServiceException(e);
    }
  }

  public String getQueueName() {
    return queueName;
  }

}
