package com.smate.center.batch.service.pub.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.enums.pub.SnsSyncGroupActionEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupPsn;

/**
 * 同步群组.
 * 
 * @author zhuangyanming
 * 
 */
@Component("snsSyncGroupProducer")
public class SnsSyncGroupProducer {

  @Autowired
  private SnsSyncGroupConsumer snsSyncGroupConsumer;
  protected final Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * 同步群组信息到群组成员.
   * 
   * @param groupPsn
   * @param psnList
   * @throws ServiceException
   */
  public void syncGroupInvitePsnToSns(GroupPsn groupPsn, List<Long> psnList) throws ServiceException {

    try {
      Assert.notNull(groupPsn, "群组不能为空.");

      SnsSyncGroup message = new SnsSyncGroup(groupPsn, psnList, groupPsn.getGroupNodeId(),
          SnsSyncGroupActionEnum.SYNC_GROUP_PSN_TO_INVITE_PSN);

      snsSyncGroupConsumer.receive(message);;
    } catch (Exception e) {

      logger.error("发送群组信息到群组成员同步失败！", e);
      throw new ServiceException();

    }

  }
}
