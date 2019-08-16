package com.smate.sie.center.task.pdwh.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.sie.center.task.model.SieConstPubType;
import com.smate.sie.center.task.pdwh.service.SieConstPubTypeService;

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

  @Autowired
  private SieConstPubTypeService sieConstPubTypeService;

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
    SieConstPubType ptype = null;
    if (pubType == 2 || pubType == 3 || pubType == 4) {
      ptype = sieConstPubTypeService.get(pubType);
      if (ptype == null) {
        logger.error("成果类别ID不正确，/put_type/@id=" + String.valueOf(pubType));
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "zh_name", ptype.getZhName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "en_name", ptype.getEnName());
      }
    } else if (pubType == 5) {// 专利
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "zh_name", "专利");
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_TYPE_XPATH, "en_name", "patent");
    }
    return true;
  }

}
