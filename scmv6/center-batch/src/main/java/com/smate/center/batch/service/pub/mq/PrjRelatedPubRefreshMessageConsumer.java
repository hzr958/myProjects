package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.oldXml.prj.PrjRelatedPubContant;
import com.smate.center.batch.service.pub.PrjRelatedPubRefreshService;

/**
 * 项目相关成果刷新同步消息消费者. sns
 * 
 * @author xys
 * 
 */
@Component("prjRelatedPubRefreshMessageConsumer")
public class PrjRelatedPubRefreshMessageConsumer {

  private static Logger logger = LoggerFactory.getLogger(PrjRelatedPubRefreshMessageConsumer.class);
  @Autowired
  private PrjRelatedPubRefreshService prjRelatedPubRefreshService;

  public void receive(PrjRelatedPubRefreshMessage message) throws ServiceException {
    PrjRelatedPubRefreshMessage prjRelatedPubRefreshMessage = (PrjRelatedPubRefreshMessage) message;
    Assert.notNull(message, "项目相关成果刷新同步消息为空");
    try {
      // 保存待刷新记录
      this.prjRelatedPubRefreshService.savePrjRelatedPubNeedRefresh(prjRelatedPubRefreshMessage.getPrjId(),
          prjRelatedPubRefreshMessage.getPubId(), prjRelatedPubRefreshMessage.getPsnId(),
          prjRelatedPubRefreshMessage.getRefreshSource());
    } catch (Exception e) {
      if (prjRelatedPubRefreshMessage.getRefreshSource() == PrjRelatedPubContant.REFRESH_SOURCE_PRJ) {
        logger.error("PrjRelatedPubRefreshMessageConsumer数据处理:prjId="
            + (prjRelatedPubRefreshMessage != null ? prjRelatedPubRefreshMessage.getPrjId() : "null"));
      } else if (prjRelatedPubRefreshMessage.getRefreshSource() == PrjRelatedPubContant.REFRESH_SOURCE_PUB) {
        logger.error("PrjRelatedPubRefreshMessageConsumer数据处理:pubId="
            + (prjRelatedPubRefreshMessage != null ? prjRelatedPubRefreshMessage.getPubId() : "null"));
      }
    }
  }

}
