package com.smate.center.batch.chain.pub;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;


/**
 * source_url链接修正xml
 * 
 * @author zk
 */

public class PubXMLSourceUrlTask implements IPubXmlTask {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String name = "pubXml_source_url";

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

    boolean isTemp = false;
    boolean isNewUrl = false;

    String sourceDbId = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_id");
    if (!"4".equals(sourceDbId)) {
      return true;
    }
    String sourceUrl = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_url");

    if (sourceUrl != null) {
      sourceUrl = StringUtils.trim(sourceUrl);

      String oldUrl = "http://ckrd.cnki.net/grid20/";
      String newUrl = "http://epub.cnki.net/grid2008/";
      // 判断url是否包含TEMP，如果包含则证明该url可能会失效，导致打开报错，所以此处直接设为空
      isTemp = StringUtils.endsWithIgnoreCase(sourceUrl, "TEMP");
      isNewUrl = StringUtils.startsWithIgnoreCase(sourceUrl, oldUrl) && sourceUrl.length() > oldUrl.length();
      if (isTemp) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_url", "");

      } else if (isNewUrl) {
        sourceUrl = newUrl + sourceUrl.substring(oldUrl.length());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_url", sourceUrl);
      }
    }

    // 修正fulltextUrl链接不正确问题，在publication标签下
    String fulltextUrl = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url");

    if (fulltextUrl != null) {
      fulltextUrl = StringUtils.trim(fulltextUrl);

      String oldUrl = "http://ckrd.cnki.net/grid20/";
      String newUrl = "http://epub.cnki.net/grid2008/";
      isTemp = StringUtils.endsWithIgnoreCase(fulltextUrl, "TEMP");
      isNewUrl = StringUtils.startsWithIgnoreCase(fulltextUrl, oldUrl) && fulltextUrl.length() > oldUrl.length();
      if (isTemp) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url", "");

      } else if (isNewUrl) {
        fulltextUrl = newUrl + fulltextUrl.substring(oldUrl.length());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fulltext_url", fulltextUrl);
      }
    }

    // 修正fulltextUrl链接不正确问题，在pub_fulltext标签下
    fulltextUrl = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url");

    if (fulltextUrl != null) {
      fulltextUrl = StringUtils.trim(fulltextUrl);

      String oldUrl = "http://ckrd.cnki.net/grid20/";
      String newUrl = "http://epub.cnki.net/grid2008/";
      isTemp = StringUtils.endsWithIgnoreCase(fulltextUrl, "TEMP");
      isNewUrl = StringUtils.startsWithIgnoreCase(fulltextUrl, oldUrl) && fulltextUrl.length() > oldUrl.length();
      if (isTemp) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url", "");

      } else if (isNewUrl) {
        fulltextUrl = newUrl + fulltextUrl.substring(oldUrl.length());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "fulltext_url", fulltextUrl);
      }
    }
    logger.info("pubXml的source_url链接修正完成!");
    return true;
  }
}
