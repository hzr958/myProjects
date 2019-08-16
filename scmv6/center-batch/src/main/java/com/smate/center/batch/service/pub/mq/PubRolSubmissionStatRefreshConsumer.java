package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.PubRolSubmissionStatService;

/**
 * 成果提交统计数更新消息.
 * 
 * @author liqinghua
 * 
 */
public class PubRolSubmissionStatRefreshConsumer implements ILocalQueneConsumer {

  /**
   * 
   */
  private static final long serialVersionUID = 956268524131575210L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRolSubmissionStatService pubRolSubmissionStatService;

  @Override
  public void receive(BaseLocalQueneMessage message) throws ServiceException {
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
