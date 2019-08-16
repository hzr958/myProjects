package com.smate.center.batch.service.pub.mq;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.rol.pub.InsPortalManager;

/**
 * 发送成果xml到单位
 * 
 * @author Administrator
 *
 */
@Component("pdwhPubCacheAssignProducer")
public class PdwhPubCacheAssignProducer {
  @Autowired
  private InsPortalManager insPortalManager;
  @Resource(name = "pdwhPubCacheAssignConsumer")
  private PdwhPubCacheAssignConsumer pdwhPubCacheAssignConsumer;

  /**
   * 发送成果XML到指定单位.
   * 
   * @param xmlId
   * @param xmlData
   * @param insId
   * @param type 1导入，2删除
   * @param dbid TODO
   * @throws ServiceException
   */
  public void sendAssignMsg(Long xmlId, String xmlData, Long insId, int type, Integer dbid) throws ServiceException {
    Integer nodeId = insPortalManager.getInsNodeId(insId);
    if (nodeId != null) {
      PdwhPubCacheAssignMessage message = new PdwhPubCacheAssignMessage(type, xmlId, xmlData, insId, dbid);
      this.pdwhPubCacheAssignConsumer.receive(message);
    }
  }

}
