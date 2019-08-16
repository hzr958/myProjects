package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.InsPortalManager;

/**
 * 
 * 发送pubmed成果XML到单位.
 * 
 * @author liqinghua
 * 
 */
@Component("pubMedPubCacheAssignProducer")
public class PubMedPubCacheAssignProducer {

  private String queneName = "pubMedPubCacheAssignMessage";
  @Autowired
  private InsPortalManager insPortalManager;
  @Autowired
  private PubMedPubCacheAssignConsumer pubMedPubCacheAssignConsumer;

  /**
   * 发送成果XML到指定单位.
   * 
   * @param xmlId
   * @param xmlData
   * @param pubType
   * @param dbId
   * @param insId
   * @throws ServiceException
   */
  public void sendAssignMsg(Long xmlId, String xmlData, Long insId, int type) throws ServiceException {
    Integer nodeId = insPortalManager.getInsNodeId(insId);
    if (nodeId != null) {
      PubMedPubCacheAssignMessage msg = new PubMedPubCacheAssignMessage(type, xmlId, xmlData, insId);
      this.pubMedPubCacheAssignConsumer.receive(msg);
    }
  }


}
