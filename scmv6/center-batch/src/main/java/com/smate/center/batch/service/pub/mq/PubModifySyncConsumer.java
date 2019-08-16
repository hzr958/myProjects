package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.service.psn.PsnJnlRefreshService;
import com.smate.center.batch.service.pub.PubFulltextPsnRcmdService;
import com.smate.center.batch.service.pub.PubRefSyncRolService;
import com.smate.center.batch.service.pub.PublicationService;
import com.smate.center.batch.util.pub.PublicationArticleType;

/**
 * 接收成果、文献更新刷新记录. sns
 * 
 * @author WeiLong Peng
 * 
 */
@Component("pubModifySyncConsumer")
public class PubModifySyncConsumer {

  /**
   * 
   */
  private static Logger logger = LoggerFactory.getLogger(PubModifySyncConsumer.class);
  @Autowired
  private PubRefSyncRolService pubRefSyncRolService;
  @Autowired
  private PsnJnlRefreshService psnJnlRefreshService;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private PubFulltextPsnRcmdService pubFulltextPsnRcmdService;

  public void receive(PubModifySyncMessage message) throws ServiceException {
    Assert.notNull(message, "消息为NULL");

    try {
      if (message != null) {
        PubModifySyncMessage msg = (PubModifySyncMessage) message;
        if (msg.getPub() != null) {
          Publication pub = msg.getPub();
          psnJnlRefreshService.markPsnJnlRefresh(pub.getId(), pub.getPsnId(), pub.getArticleType(), msg.getIsDel());

          // 文献修改需要同步保存记录到PUB_REF_SYNCROL_FLAG
          if (pub.getArticleType() == PublicationArticleType.REFERENCE) {
            pubRefSyncRolService.markPubRefSync(pub.getId(), msg.getIsDel());
          }

          if (pub.getArticleType() == PublicationArticleType.OUTPUT) {
            this.publicationService.syncPubToPubFtSrv(pub);
            if (pub.getStatus() == 1) { // 删除成果自动确认全文不是这条成果的
              this.pubFulltextPsnRcmdService.deletePubFulltextPsnRcmd(pub.getId());
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("消费成果、文献更新刷新记录失败：", e);
    }
  }

}
