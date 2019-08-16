package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 删除资源同步各种信息的生产者
 * 
 * @author Scy
 * 
 */
@Component("delResProducer")
public class DelResProducer {

  @Autowired
  private DelResConsumer delResConsumer;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public void syncDelResMessage(Long actionKey, Integer actionType, Long psnId) throws ServiceException {
    try {
      DelResMessage delResMessage =
          new DelResMessage(actionKey, actionType, psnId, SecurityUtils.getCurrentAllNodeId().get(0));
      delResConsumer.receive(delResMessage);;
    } catch (Exception e) {
      logger.error("删除资源发送端发送消息同步失败！", e);
      throw new ServiceException(e);
    }
  }

}
