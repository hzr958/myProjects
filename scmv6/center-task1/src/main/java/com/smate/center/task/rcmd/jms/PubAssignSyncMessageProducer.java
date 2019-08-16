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
 * 成果指派，删除指派MQ消息，包括成果XML.
 * 
 * @author zjh
 *
 */
@Component("pubAssignSyncMessageProducer")
public class PubAssignSyncMessageProducer {
  private static final long serialVersionUID = -6381283385616778111L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final String queueName = "pubAssignSync";
  @Autowired
  private PubAssignSyncMessageConsumer pubAssignSyncMessageConsumer;

  public void publishAssignError(Long psnId, Long insId, Long insPubId, Integer errorType) throws ServiceException {
    try {
      Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
      PubAssignSyncMessage msg = new PubAssignSyncMessage(psnId, insId, insPubId,
          PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR, errorType, nodeId);
      pubAssignSyncMessageConsumer.receive(msg);

    } catch (Exception e) {
      logger.error("发布成果指派XML错误.", e);
      throw new ServiceException(e);
    }
  }

}
