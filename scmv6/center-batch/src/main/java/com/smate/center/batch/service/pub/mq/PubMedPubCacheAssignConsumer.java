package com.smate.center.batch.service.pub.mq;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstRefDb;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.center.batch.service.pdwh.pub.PublicationPdwhService;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.center.batch.service.pub.RolPublicationXmlManager;
import com.smate.center.batch.service.rol.pub.PublicationRolStatusTransService;
import com.smate.center.batch.util.pub.ImportPubXmlUtils;

/**
 * pubMed成果批量抓取指派给单位接收端.
 * 
 * @author liqinghua
 * 
 */
@Deprecated
@Component("pubMedCacheAssignConsumer")
public class PubMedPubCacheAssignConsumer {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private RolPublicationXmlManager rolPublicationXmlManager;
  @Autowired
  private PublicationRolStatusTransService publicationRolStatusTransService;
  @Autowired
  private ConstRefDbService constRefDbService;
  @Autowired
  private PublicationPdwhService publicationPdwhService;

  public void receive(PubMedPubCacheAssignMessage msg) throws ServiceException {

    try {
      logger.debug("#####PUBMED成果批量抓取指派给单位数据xmlId:" + msg.getXmlId());
      // 是否是导入isi文献库的文献
      if (msg.getType() == 1 && StringUtils.isNotBlank(msg.getXmlData())) {
        Long pubId = rolPublicationXmlManager.backgroundImportPubMedPubXml(msg);
        this.savePdwhId(msg, pubId);
        // 是否是删除匹配错误的文献
      } else if (msg.getType() == 2) {
        publicationRolStatusTransService.delPubMedMatchPub(msg.getXmlId(), msg.getInsId());
      }
    } catch (Exception e) {
      logger.error("IsiPubCacheAssignConsumer导入XML错误", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 保存基准库ID.
   * 
   * @param msg
   * @param pubId
   * @throws Exception
   */
  private void savePdwhId(PubMedPubCacheAssignMessage msg, Long pubId) throws Exception {
    Long queryId = msg.getXmlId();
    Long dbId = getXmlSourceDbId(msg.getXmlData());
    if (dbId != null) {
      // 需要发送到单位的成果存至pdwhPubAssign
      /*
       * PublicationPdwh pubPdwh = this.publicationPdwhService.savePublicationPdwh(pubId, dbId.intValue(),
       * queryId); publicationPdwhService.refreshPubPdwhToXml(pubPdwh);
       */
    }
  }

  /**
   * 获取XML中的DBID.
   * 
   * @param xmlData
   * @return
   */
  private Long getXmlSourceDbId(String xmlData) {
    try {
      String dbCode = ImportPubXmlUtils.getXmlSourceDbCode(xmlData);
      ConstRefDb sourceDb = constRefDbService.getConstRefDbByCode(dbCode);
      if (sourceDb == null) {
        return null;
      } else {
        return sourceDb.getId();
      }
    } catch (Exception e) {
      logger.error("获取XML中的DBID错误", e);
    }
    return null;
  }
}
