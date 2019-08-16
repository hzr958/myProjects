package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.InsPortalManager;

/**
 * 
 * 发送CNKI成果XML到单位.
 * 
 * @author liqinghua
 * 
 */
@Component("cnkiPubCacheAssignProducer")
public class CnkiPubCacheAssignProducer {

  private String queneName = "cnkiPubCacheAssignMessage";
  @Autowired
  private InsPortalManager insPortalManager;
  @Autowired
  private CnkiPubCacheAssignConsumer cnkiPubCacheAssignConsumer;

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
  public void sendAssignMsg(Long xmlId, String xmlData, Long insId) throws ServiceException {
    Integer nodeId = insPortalManager.getInsNodeId(insId);
    if (nodeId != null) {
      CnkiPubCacheAssignMessage msg = new CnkiPubCacheAssignMessage(xmlId, xmlData, insId);
      this.cnkiPubCacheAssignConsumer.receive(msg);;
    }
  }


}
