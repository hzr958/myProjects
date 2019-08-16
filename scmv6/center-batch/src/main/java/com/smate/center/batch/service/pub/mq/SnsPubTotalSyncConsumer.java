package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.PubRolSubmissionStatService;

/**
 * 个人成果数量同步到单位. sie
 * 
 * @author liqinghua
 * 
 */
@Component("snsPubTotalSyncConsumer")
public class SnsPubTotalSyncConsumer {

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRolSubmissionStatService pubRolSubmissionStatService;

  public void receive(SnsPubTotalSyncMessage message) throws ServiceException {

    SnsPubTotalSyncMessage msg = (SnsPubTotalSyncMessage) message;
    logger.debug("接收到个人成果数量同步到单位insId:{},psnId:{},total:{}",
        new Object[] {msg.getInsId(), msg.getPsnId(), msg.getTotal()});
    pubRolSubmissionStatService.setPubTotal(msg.getInsId(), msg.getPsnId(), msg.getTotal(), msg.getSubmitTotal());
  }
}
