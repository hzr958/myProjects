package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.DynamicSyncService;

/**
 * 动态同步消费者. sns
 * 
 * @author chenxiangrong
 * 
 */
@Component("snsDynamicSyncConsumer")
public class SnsDynamicSyncConsumer {
  /**
   * 
   */
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynamicSyncService dynamicSyncService;

  public void receive(SnsDynamicSyncMessage message) throws ServiceException {
    logger.info("开始处理动态同步！");
    SnsDynamicSyncMessage dynamicMessage = (SnsDynamicSyncMessage) message;
    dynamicSyncService.syncDynamic(dynamicMessage.getDynamic(), dynamicMessage.getExtJson(),
        dynamicMessage.getResType());
  }
}
