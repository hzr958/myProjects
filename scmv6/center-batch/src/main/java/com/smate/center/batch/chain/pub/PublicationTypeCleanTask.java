package com.smate.center.batch.chain.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * 读取类别名称到XML.
 * 
 * @author yamingd
 */
public class PublicationTypeCleanTask implements IPubXmlTask {

  /**
   * 
   */
  private final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 
   */
  private final String name = "publication_type_clean";

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#getName()
   */
  @Override
  public String getName() {
    return this.name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#can(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#run(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    int pubType = xmlDocument.getPubTypeId();
    ConstPubType ptype = null;
    ptype = context.getXmlServiceFactory().getConstPubTypeService().get(pubType);
    if (ptype == null) {
      logger.error("成果类别ID不正确，/put_type/@id=" + String.valueOf(pubType));
    } else {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "zh_name", ptype.getZhName());
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "en_name", ptype.getEnName());
    }

    return true;
  }

}
