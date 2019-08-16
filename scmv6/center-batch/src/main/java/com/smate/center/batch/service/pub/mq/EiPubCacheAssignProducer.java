package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.InsPortalManager;

/**
 * 
 * 发送ISI成果XML到单位.
 * 
 * @author liqinghua
 * 
 */
@Component("eiPubCacheAssignProducer")
public class EiPubCacheAssignProducer {

  private String queneName = "eiPubCacheAssignMessage";
  @Autowired
  private InsPortalManager insPortalManager;
  @Autowired
  private EiPubCacheAssignConsumer eiPubCacheAssignConsumer;

  /**
   * 发送成果XML到指定单位.
   * 
   * @param xmlId
   * @param xmlData
   * @param insId
   * @param type 1导入，2删除
   * @throws ServiceException
   */
  public void sendAssignMsg(Long xmlId, String xmlData, Long insId, int type) throws ServiceException {
    Integer nodeId = insPortalManager.getInsNodeId(insId);
    if (nodeId != null) {
      EiPubCacheAssignMessage msg = new EiPubCacheAssignMessage(type, xmlId, xmlData, insId);
      this.eiPubCacheAssignConsumer.receive(msg);
    }
  }

}
