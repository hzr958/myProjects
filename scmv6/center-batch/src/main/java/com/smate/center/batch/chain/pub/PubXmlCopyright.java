package com.smate.center.batch.chain.pub;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * 去除摘要版权信息
 * 
 * @author zk
 */

public class PubXmlCopyright implements IPubXmlTask {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String name = "pubXml_copyright";

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

    // 英文摘要
    String enAbstract = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract");
    xmlDocument = removePubXMLCopyright(xmlDocument, enAbstract, "en_abstract");
    // 中文摘要
    String zhAbstract = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract");
    xmlDocument = removePubXMLCopyright(xmlDocument, zhAbstract, "zh_abstract");
    logger.info("pubXml去除摘要版权信息!");
    return true;
  }

  // 过滤版权信息.
  private PubXmlDocument removePubXMLCopyright(PubXmlDocument xmlDocument, String enAbstract, String localAbstract)
      throws BatchTaskException {

    if (StringUtils.isNotBlank(enAbstract)) {
      // Test //enAbstract +=
      // "(C) All rights reserved © 2003 American Chemical Society.";
      int len = enAbstract.length();
      String firstHalf = "";
      String lastHalf = "";
      if (len > 100) {// 摘要拆分成两部分，以后100个字符为划分点
        firstHalf = enAbstract.substring(0, len - 100);
        lastHalf = enAbstract.substring(len - 100);
      } else {// 不够100个字符
        lastHalf = enAbstract;
      }

      int copyPos = StringUtils.indexOfAny(lastHalf,
          new String[] {"All rights reserved", "Copyright", "(C)", "(c)", "©", "&copy;"});// lastHalf是否包含数组中的任意版权字符
      if (copyPos > -1) {
        lastHalf = lastHalf.substring(0, copyPos);// 截取包含的版权字符
        int dotPos = StringUtils.lastIndexOf(lastHalf, ".");// 以最后出现的英文句号为划分点
        if (dotPos > -1) {
          lastHalf = lastHalf.substring(0, dotPos + 1);// 包含句号
          // 拼接去除版权信息的摘要内容写回xml中
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, localAbstract, firstHalf + lastHalf);
        }
      }
    }
    return xmlDocument;
  }
}
