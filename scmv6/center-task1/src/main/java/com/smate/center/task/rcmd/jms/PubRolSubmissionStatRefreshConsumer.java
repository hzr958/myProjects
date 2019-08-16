package com.smate.center.task.rcmd.jms;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PubRolSubmissionStatRefreshEnum;
import com.smate.center.task.model.rol.quartz.PubRolSubmissionStatRefreshMessage;
import com.smate.center.task.service.rol.quartz.PubRolSubmissionStatService;

/**
 * 成果提交统计数更新消息.
 * 
 * @author liqinghua
 * 
 */
@Component("pubRolSubmissionStatRefreshConsumer")
public class PubRolSubmissionStatRefreshConsumer implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 765995779104193312L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRolSubmissionStatService pubRolSubmissionStatService;

  public void receive(PubRolSubmissionStatRefreshMessage message) throws ServiceException {
    try {
      PubRolSubmissionStatRefreshMessage msg = (PubRolSubmissionStatRefreshMessage) message;
      logger.debug("接收到成果提交统计数更新消息");
      if (PubRolSubmissionStatRefreshEnum.REFRESH_INS.equals(msg.getActionType())) {
        pubRolSubmissionStatService.refreshPubRolSubmissionStat(msg.getInsId());
      } else if (PubRolSubmissionStatRefreshEnum.REFRESH_INS_PSN.equals(msg.getActionType())) {
        pubRolSubmissionStatService.refreshPubRolSubmissionStat(msg.getInsId(), msg.getPsnId());
      }
      logger.debug("完成一个接收到成果提交统计数更新消息");
    } catch (ServiceException e) {
      throw e;
    }
  }
}
