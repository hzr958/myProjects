package com.smate.center.batch.service.pub.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.enums.pub.PubSyncToPubFtSrvTypeEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.util.pub.LogUtils;
import com.smate.core.base.utils.constant.ServiceConstants;

/**
 * 同步成果冗余信息到pubftsrv.
 * 
 * @author pwl
 * 
 */
@Component(value = "pubSyncToPubFtSrvProducer")
public class PubSyncToPubFtSrvProducer {

  @Autowired
  private PubSyncToPubFtSrvConsumer pubSyncToPubFtSrvConsumer;
  private static Logger logger = LoggerFactory.getLogger(PubSyncToPubFtSrvProducer.class);

  public void sendUpdatePubMessage(List<Map<String, Object>> list) throws ServiceException {
    try {
      PubSyncToPubFtSrvMessage message = new PubSyncToPubFtSrvMessage(list,
          PubSyncToPubFtSrvTypeEnum.UPDATE_PUBLICATION, ServiceConstants.SCHOLAR_NODE_ID_1);
      pubSyncToPubFtSrvConsumer.receive(message);
    } catch (Exception e) {
      LogUtils.error(logger, e, "同步成果冗余信息到pubftsrv出现异常");
    }
  }

  public void sendUpdatePubMessage(Map<String, Object> map) throws ServiceException {

    try {
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
      list.add(map);
      PubSyncToPubFtSrvMessage message = new PubSyncToPubFtSrvMessage(list,
          PubSyncToPubFtSrvTypeEnum.UPDATE_ISI_PUBLICATION, ServiceConstants.PDWH_NODE_ID);
      pubSyncToPubFtSrvConsumer.receive(message);
    } catch (Exception e) {
      LogUtils.error(logger, e, "同步ISI成果冗余信息到pubftsrv出现异常");
    }
  }

  public void sendUpdatePubOwnerMatch(Long pubId, Long psnId, Integer match) throws ServiceException {
    try {
      PubSyncToPubFtSrvMessage message = new PubSyncToPubFtSrvMessage(pubId, psnId, match,
          PubSyncToPubFtSrvTypeEnum.UPDATE_PUB_OWNER_MATCH, ServiceConstants.SCHOLAR_NODE_ID_1);
      pubSyncToPubFtSrvConsumer.receive(message);
    } catch (Exception e) {
      LogUtils.error(logger, e, "同步成果与成果拥有者是否匹配到pubftsrv出现异常");
    }
  }

  public void sendUpdateFulltextMessage(Long pubId, Long psnId, Long fulltextFileId, int permission)
      throws ServiceException {
    try {
      PubSyncToPubFtSrvMessage message = new PubSyncToPubFtSrvMessage(pubId, psnId, fulltextFileId, permission,
          PubSyncToPubFtSrvTypeEnum.UPDATE_FULLTEXT, ServiceConstants.SCHOLAR_NODE_ID_1);
      pubSyncToPubFtSrvConsumer.receive(message);
    } catch (Exception e) {
      LogUtils.error(logger, e, "同步成果附件变更信息到pubftsrv出现异常");
    }
  }

}
