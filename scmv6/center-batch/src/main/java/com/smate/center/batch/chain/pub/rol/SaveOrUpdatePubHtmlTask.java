package com.smate.center.batch.chain.pub.rol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.service.rol.pub.PubInfoHtmlBuilder;

/**
 * 保存或者更新PubHtml任务
 * 
 * @author Scy
 * 
 */
public class SaveOrUpdatePubHtmlTask implements IPubXmlTask {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private String name = "save_update_pub_html";

  @Autowired
  private PubInfoHtmlBuilder pubInfoHtmlBuilder;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return true;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    try {
      this.pubInfoHtmlBuilder.buildHtml(xmlDocument);
      return true;
    } catch (Exception e) {
      // 此错误不外抛，以免影响正常成果录入
      logger.error("保存更新成果html内容出错，pubId=" + xmlDocument.getPubId(), e);
    }
    return false;
  }

}
