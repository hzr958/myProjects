package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 人员同步推荐信息到推荐服务器消息生产者.
 * 
 * @author liqinghua
 * 
 */
@Component("rcmdSyncFlagMessageProducer")
public class RcmdSyncFlagMessageProducer {

  @Autowired
  private RcmdSyncFlagMessageConsumer rcmdSyncFlagMessageConsumer;
  private static Logger logger = LoggerFactory.getLogger(RcmdSyncFlagMessageProducer.class);

  /**
   * 同步文献信息.
   * 
   * @param message
   * @return int 0:同步失败; 1：同步成功;
   * @throws ServiceException
   */
  public int syncPsnRefc(Long psnId, Long pubId, int isDel) {
    try {
      logger.debug("进入人员同步文献信息同步消息!psnId=" + psnId);
      RcmdSyncFlagMessage message = RcmdSyncFlagMessage.getInstance(psnId);
      message.setRefcFlag(1);
      message.setRefc(pubId, isDel);
      rcmdSyncFlagMessageConsumer.receive(message);
    } catch (ServiceException e) {
      logger.error("人员冗余同步文献消息错误psnId=" + psnId, e);
      return 0;
    }
    return 1;
  }


  /**
   * 同步成果信息.
   * 
   * @param message
   * @return int 0:同步失败; 1：同步成功;
   * @throws ServiceException
   */
  public int syncPsnPub(Long psnId, Long pubId, int isDel) {
    try {
      logger.debug("进入人员冗余信息同步消息!psnId=" + psnId);
      RcmdSyncFlagMessage message = RcmdSyncFlagMessage.getInstance(psnId);
      message.setPubFlag(1);
      message.setPub(pubId, isDel);
      rcmdSyncFlagMessageConsumer.receive(message);
    } catch (ServiceException e) {
      logger.error("人员冗余信息同步消息错误psnId=" + psnId, e);
      return 0;
    }
    return 1;
  }

  /**
   * 同步additinfo设置
   */
  public int syncPsnIcons(Long psnId) {
    try {
      RcmdSyncFlagMessage message = RcmdSyncFlagMessage.getInstance(psnId);
      message.setAdditinfoFlag(1);
      rcmdSyncFlagMessageConsumer.receive(message);
    } catch (ServiceException e) {
      logger.error("同步关注设置消息错误psnId=" + psnId, e);
      return 0;
    }
    return 1;
  }

  /**
   * 同步个人邮箱
   * 
   * @param psnId
   * @return
   */
  public int syncPsnEmail(Long psnId) {
    try {
      RcmdSyncFlagMessage message = RcmdSyncFlagMessage.getInstance(psnId);
      message.setEmailFlag(1);
      rcmdSyncFlagMessageConsumer.receive(message);
    } catch (ServiceException e) {
      logger.error("同步个人邮箱同步消息错误psnId=" + psnId, e);
      return 0;
    }
    return 1;
  }

  /**
   * @param message
   * @return int 0:同步失败; 1：同步成功;
   * @throws ServiceException
   */
  public int syncRcmdPsnInfoMessage(RcmdSyncFlagMessage message) {
    try {
      logger.debug("进入人员冗余信息同步消息!psnId=" + message.getPsnId());
      rcmdSyncFlagMessageConsumer.receive(message);
    } catch (ServiceException e) {
      logger.error("人员冗余信息同步消息错误psnId=" + message.getPsnId(), e);
      return 0;
    }
    return 1;
  }
}
