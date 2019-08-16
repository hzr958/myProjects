package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果、文献更新发送刷新记录使用.
 * 
 * @author WeiLong Peng
 * 
 */
@Component("pubModifySyncProducer")
public class PubModifySyncProducer {

  @Autowired
  private PubModifySyncConsumer pubModifySyncConsumer;
  private static Logger logger = LoggerFactory.getLogger(PubModifySyncProducer.class);
  String queueName = "pubModifySync";

  public void sendSyncMessage(Publication pub, Integer isDel) throws ServiceException {

    Assert.notNull(pub, "pub不能为空");
    try {
      if (pub != null) {
        PubModifySyncMessage message = new PubModifySyncMessage(pub, isDel, SecurityUtils.getCurrentAllNodeId().get(0));
        pubModifySyncConsumer.receive(message);
      }
    } catch (Exception e) {
      logger.error("发送成果、文献刷新记录失败：", e);
      throw new ServiceException("发送成果、文献刷新记录失败：", e);
    }
  }


}
