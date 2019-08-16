package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.enums.pub.SnsSyncGroupStatisActionEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.GroupStatisticsService;

/**
 * 群组统计信息同步. sie
 * 
 * @author zyx
 * 
 */
@Component("SyncGroupStatisticsConsumer")
public class SyncGroupStatisticsConsumer {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupStatisticsService groupStatisticsService;

  public void receive(SnsSyncGroupStatisticsMessage msg) throws ServiceException, ClassCastException {
    logger.debug("接收到群组统计同步信息:{}", msg);
    Assert.notNull(msg, "接收到群组统计同步信息为空");

    SnsSyncGroupStatisticsMessage message = (SnsSyncGroupStatisticsMessage) msg;

    if (message.getAction() == SnsSyncGroupStatisActionEnum.SYNC_ALL_UPDATE) {
      groupStatisticsService.handleAddOrUpdateSync(message);
    } else if (message.getAction() == SnsSyncGroupStatisActionEnum.SYNC_GROUP_DEL) {// 按群组ID删除合作分析数据
      groupStatisticsService.handleDelSync(message.getGroupId());
    }

  }
}
