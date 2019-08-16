package com.smate.center.task.rcmd.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PubRolPsnStatRefreshEnum;
import com.smate.center.task.model.rol.quartz.PubRolPsnStatRefreshMessage;
import com.smate.center.task.service.rol.quartz.PubRolPsnStatService;

@Component("pubRolPsnStatRefreshConsumer")
public class PubRolPsnStatRefreshConsumer {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRolPsnStatService pubRolPsnStatService;

  public void receive(PubRolPsnStatRefreshMessage message) throws ServiceException {
    try {

      PubRolPsnStatRefreshMessage msg = (PubRolPsnStatRefreshMessage) message;
      logger.debug("接收到成果指派统计数更新消息");
      if (PubRolPsnStatRefreshEnum.REFRESH_INS.equals(msg.getActionType())) {
        pubRolPsnStatService.refreshPubRolPsnStat(msg.getInsId());
      } else if (PubRolPsnStatRefreshEnum.REFRESH_INS_PSN.equals(msg.getActionType())) {
        pubRolPsnStatService.refreshPubRolPsnStat(msg.getInsId(), msg.getPsnId());
      } else if (PubRolPsnStatRefreshEnum.REFRESH_INS_PSN_BATCH.equals(msg.getActionType())) {
        pubRolPsnStatService.refreshPubRolPsnStat(msg.getInsId(), msg.getPsnIds());
      } else if (PubRolPsnStatRefreshEnum.REFRESH_PUB_PSN.equals(msg.getActionType())) {
        pubRolPsnStatService.refreshPubPsnStat(msg.getInsId(), msg.getPubId());
      }
      logger.debug("完成一个接收到成果指派统计数更新消息");

    } catch (ServiceException e) {
      throw e;
    }
  }

}
