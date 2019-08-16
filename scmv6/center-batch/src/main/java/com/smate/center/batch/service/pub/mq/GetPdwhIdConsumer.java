package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pdwh.pub.DbCachePfetchQueryService;

/**
 * 获取查询基准库ID消息消费者.
 * 
 * @author liqinghua
 * 
 */
@Component("getPdwhIdConsumer")
public class GetPdwhIdConsumer {

  private static Logger logger = LoggerFactory.getLogger(GetPdwhIdConsumer.class);
  private static final long serialVersionUID = 3295807696213189273L;
  @Autowired
  private DbCachePfetchQueryService dbCachePfetchQueryService;

  public void receive(GetPdwhIdMessage message) throws ServiceException {
    GetPdwhIdMessage msg = (GetPdwhIdMessage) message;
    try {
      logger.debug("获取到查询基准库ID消息pubid=" + msg.getPubId());

      dbCachePfetchQueryService.saveGetPdwhIdMessage(msg);
    } catch (Exception e) {
      logger.error("获取查询基准库ID消息消费者", e);
      throw new ServiceException("获取查询基准库ID消息消费者", e);
    }
  }

}
