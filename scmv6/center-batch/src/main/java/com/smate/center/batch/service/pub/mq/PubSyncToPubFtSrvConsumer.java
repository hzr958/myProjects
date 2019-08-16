package com.smate.center.batch.service.pub.mq;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.enums.pub.PubSyncToPubFtSrvTypeEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.rcmd.PubFulltextRcmdService;

/**
 * 接收成果冗余信息到pubftsrv. rcmd
 * 
 * @author pwl
 * 
 */
@Component("pubSyncToPubFtSrvConsumer")
public class PubSyncToPubFtSrvConsumer {

  /**
   * 
   */

  private static Logger logger = LoggerFactory.getLogger(PubSyncToPubFtSrvConsumer.class);

  @Autowired
  private PubFulltextRcmdService pubFulltextRcmdService;

  public void receive(PubSyncToPubFtSrvMessage message) throws ServiceException {

    try {
      PubSyncToPubFtSrvMessage msg = (PubSyncToPubFtSrvMessage) message;
      if (msg.getType() == PubSyncToPubFtSrvTypeEnum.UPDATE_ISI_PUBLICATION) {
        if (CollectionUtils.isNotEmpty(msg.getList())) {
          this.pubFulltextRcmdService.saveIsiPublication(msg.getList());
        }
      }
    } catch (Exception e) {
      logger.error("MQ接收成果全文推荐匹配信息出现异常", e);
      throw new ServiceException(e);
    }
  }

}
