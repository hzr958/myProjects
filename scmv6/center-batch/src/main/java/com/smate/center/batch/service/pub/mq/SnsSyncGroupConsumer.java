package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.enums.pub.SnsSyncGroupActionEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.GroupSnsService;

/**
 * 群组同步. sns
 * 
 * @author zhuangyanming
 * 
 */
@Component("snsSyncGroupConsumer")
public class SnsSyncGroupConsumer {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupSnsService groupSnsService;

  public void receive(SnsSyncGroup msg) throws ServiceException {
    SnsSyncGroup message = (SnsSyncGroup) msg;

    Assert.notNull(message);
    SnsSyncGroupActionEnum action = message.getAction();
    try {

      logger.info("接受到群组同步消息，{}", message);

      if (action == SnsSyncGroupActionEnum.SYNC_GROUP_PSN_TO_INVITE_PSN) {

        groupSnsService.syncGroupInvitePsn(message.getGroupPsn(), message.getPsnList());

      }

    } catch (Exception e) {
      logger.error("同步群组失败！", e);
    }
  }
}
