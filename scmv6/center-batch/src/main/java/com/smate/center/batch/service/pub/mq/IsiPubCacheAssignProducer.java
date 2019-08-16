package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.InsPortalManager;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 
 * 发送ISI成果XML到单位.
 * 
 * @author liqinghua
 * 
 */
@Component("isiPubCacheAssignProducer")
public class IsiPubCacheAssignProducer {

  private String queneName = "isiPubCacheAssignMessage";
  @Autowired
  private InsPortalManager insPortalManager;
  @Autowired
  private IsiPubCacheAssignConsumer isiPubCacheAssignConsumer;

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
      IsiPubCacheAssignMessage msg = new IsiPubCacheAssignMessage(type, xmlId, xmlData, insId);
      this.isiPubCacheAssignConsumer.receive(msg);
    }
  }

}
