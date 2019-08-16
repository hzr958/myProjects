package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.RcmdSyncDBService;

/**
 * 人员冗余信息同步标记消息. sns
 * 
 * @author lqh
 * 
 */
@Component("rcmdSyncFlagMessageConsumer")
public class RcmdSyncFlagMessageConsumer {

  /**
   * 
   */

  private static Logger logger = LoggerFactory.getLogger(RcmdSyncFlagMessageConsumer.class);

  @Autowired
  private RcmdSyncDBService rcmdSyncDBService;

  public void receive(RcmdSyncFlagMessage msg) throws ServiceException {
    RcmdSyncFlagMessage message = (RcmdSyncFlagMessage) msg;
    Assert.notNull(message, "人员冗余信息同步标记消息数据为空");
    try {
      rcmdSyncDBService.saveSyncInfo(message);
      logger.debug("RcmdSyncFlagMessageConsumer，数据已处理");
    } catch (Exception e) {
      logger.error("RcmdSyncFlagMessageConsumer数据处理:psnid=" + (message != null ? message.getPsnId() : "null"));
    }
  }
}
