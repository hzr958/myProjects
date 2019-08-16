package com.smate.center.batch.chain.pub.rol;

import com.smate.center.batch.exception.pub.PublicationNotFoundException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.util.pub.PubXmlDocumentBuilder;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * @author yamingd 导入xml时合并Xml.
 */
public class ImportXmlMergeTask implements IPubXmlTask {

  private String name = "import_xml_merge";

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    String dupPubId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id");
    return XmlUtil.isNumeric(dupPubId);
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    String dupPubId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id");
    if (XmlUtil.isNumeric(dupPubId)) {
      PublicationXml pubxml =
          context.getXmlServiceFactory().getPublicationXmlService().rolGetById(Long.parseLong(dupPubId));
      if (pubxml != null) {
        String xmlString = pubxml.getXmlData();
        PubXmlDocumentBuilder.mergeWhenImport(xmlDocument, xmlString);
        // context.setCurrentPubId(Long.parseLong(dupPubId));
        // xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "pub_id", dupPubId);
        // 重置
        // xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "dup_pub_id", "");
      } else {
        throw new PublicationNotFoundException(dupPubId);
      }
    }

    return true;
  }

}
