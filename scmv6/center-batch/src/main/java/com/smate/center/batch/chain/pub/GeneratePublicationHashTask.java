package com.smate.center.batch.chain.pub;

import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.XmlProcessStopExecuteException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.util.pub.PublicationHash;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * @author yamingd 生成成果字段HashCode
 */
public class GeneratePublicationHashTask implements IPubXmlTask {

  /**
   * 
   */
  private final String name = "generate_publication_hash";

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

    String etitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");
    String ctitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");

    if ("".equals(etitle) && "".equals(ctitle)) {
      throw new XmlProcessStopExecuteException("成果标题不能为空！！！！！");
    }

    Integer hash = PublicationHash.cleanTitleHash(ctitle);
    Integer hash2 = PublicationHash.cleanTitleHash(etitle);

    // 从V2.6补充同步的结题报告成果，不需要那么严格，否则导入不了
    if (hash == null && hash2 == null && XmlOperationEnum.SyncFromOldPrj.equals(context.getCurrentAction())) {
      hash = ctitle == null ? null : ctitle.hashCode();
      hash2 = etitle == null ? null : etitle.hashCode();
    }
    // 先注释掉：允许单独输入特殊字符
    // if (hash == null && hash2 == null) {
    // throw new XmlProcessStopExecuteException("成果标题Hash不能为空！！！！！");
    // }

    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_hash",
        hash != null ? String.valueOf(hash) : "");
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_hash",
        hash2 != null ? String.valueOf(hash2) : "");

    // 指纹生成
    if (xmlDocument.isPatent()) {
      String patentNo = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_no");
      hash = PublicationHash.patentFingerPrint(patentNo);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "finger_print",
          hash != null ? String.valueOf(hash) : "");
    } else if (xmlDocument.isJournalArticle()) {
      String volume = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume");
      String issue = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue");
      String startPage = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page");
      String endPage = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page");
      hash = PublicationHash.journalArticleFingerPrint(volume, issue, startPage, endPage);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "finger_print",
          hash != null ? String.valueOf(hash) : "");
    }

    return true;
  }

}
