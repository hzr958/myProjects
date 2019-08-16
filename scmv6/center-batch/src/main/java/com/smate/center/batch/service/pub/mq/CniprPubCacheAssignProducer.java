package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.InsPortalManager;

/**
 * 
 * 发送CNIPR成果XML到单位.
 * 
 * @author liqinghua
 * 
 */
@Component("cniprPubCacheAssignProducer")
public class CniprPubCacheAssignProducer {

  private String queneName = "cniprPubCacheAssignMessage";
  @Autowired
  private InsPortalManager insPortalManager;
  @Autowired
  private CniprPubCacheAssignConsumer cniprPubCacheAssignConsumer;

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
      CniprPubCacheAssignMessage msg = new CniprPubCacheAssignMessage(xmlId, xmlData, insId);
      this.cniprPubCacheAssignConsumer.receive(msg);
    }
  }


}
