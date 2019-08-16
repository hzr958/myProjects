package com.smate.center.batch.service.pub.mq;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PublicationRolPdwh;
import com.smate.center.batch.service.pdwh.pub.PublicationPdwhService;
import com.smate.center.batch.service.pub.RolPublicationXmlManager;
import com.smate.center.batch.service.rol.pub.PublicationRolStatusTransService;


/**
 * 基准库成果批量抓取指派给单位接收端.
 * 
 * @author zll
 * 
 */

@Component("pdwhPubCacheAssignConsumer")
public class PdwhPubCacheAssignConsumer {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RolPublicationXmlManager rolPublicationXmlManager;
  @Autowired
  private PublicationPdwhService publicationPdwhService;
  @Autowired
  private PublicationRolStatusTransService publicationRolStatusTransService;

  public void receive(PdwhPubCacheAssignMessage message) {
    try {
      PdwhPubCacheAssignMessage msg = (PdwhPubCacheAssignMessage) message;
      logger.debug("#####Pdwh成果批量抓取指派给单位数据xmlId:" + msg.getXmlId());
      // 是否是导入文献库的文獻
      if (msg.getType() == 1 && StringUtils.isNotBlank(msg.getXmlData())) {
        Long pubId = rolPublicationXmlManager.backgroundImportPdwhPubXml(msg);
        this.savePdwhId(msg, pubId);
        // 是否是删除匹配错误的文献
      } else if (msg.getType() == 2) {
        // publicationRolStatusTransService.delEiMatchPub(msg.getXmlId(), msg.getInsId());
        publicationRolStatusTransService.delPdwhMatchPub(msg.getXmlId(), msg.getInsId());
      }
    } catch (Exception e) {
      logger.error("EiPubCacheAssignConsumer导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  private void savePdwhId(PdwhPubCacheAssignMessage msg, Long pubId) {
    Long queryId = msg.getXmlId();
    if (msg.getDbid() != null) {
      PublicationRolPdwh pubPdwh = publicationPdwhService.savePublicationRolPdwh(pubId, msg.getDbid(), queryId);
      publicationPdwhService.refreshPubPdwhToXml(pubPdwh);
    }

  }

}
