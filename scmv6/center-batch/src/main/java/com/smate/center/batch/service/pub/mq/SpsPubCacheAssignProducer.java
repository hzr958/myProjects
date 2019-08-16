package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.InsPortalManager;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 
 * 发送scopus成果XML到单位.
 * 
 * @author liqinghua
 * 
 */
@Component("spsPubCacheAssignProducer")
public class SpsPubCacheAssignProducer {

  private String queneName = "spsPubCacheAssignMessage";
  @Autowired
  private InsPortalManager insPortalManager;
  @Autowired
  private SpsPubCacheAssignConsumer spsPubCacheAssignConsumer;

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
      SpsPubCacheAssignMessage msg = new SpsPubCacheAssignMessage(type, xmlId, xmlData, insId);
      this.spsPubCacheAssignConsumer.receive(msg);
    }
  }

}
