package com.smate.center.batch.chain.pub;

import org.springframework.util.Assert;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;


/**
 * 专利类型处理.
 * 
 * @author liqinghua
 * 
 */
public class PublicationPatentTask implements IPubXmlTask {

  /**
   * 
   */
  private final String name = "pub_patent";

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    return context.getPubTypeId() == PublicationTypeEnum.PATENT;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    Assert.notNull(context.getPubTypeId());
    Assert.notNull(context.getCurrentUserId());
    Assert.notNull(context.getArticleType());
    Assert.notNull(context.getCurrentAction());
    Assert.notNull(context.getCurrentNodeId());

    // 从cnki导入的专利，对"专利类别"进行处理
    String patentType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type");
    if ("发明专利".equals(patentType) || "Invention Patent".equals(patentType)) {
      xmlDocument.setNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type", "51");
    } else if ("实用新型".equals(patentType) || "Utility model".equals(patentType)) {
      xmlDocument.setNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type", "52");
    } else if ("外观专利".equals(patentType) || "Appearance Patent".equals(patentType)) {
      xmlDocument.setNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type", "53");
    }

    return true;
  }
}
