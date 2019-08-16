package com.smate.center.batch.chain.pub;

import org.apache.commons.lang.StringUtils;

import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.XmlProcessStopExecuteException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.center.batch.util.pub.PubXmlDbUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * @author yamingd 处理标题、摘要、关键字的HTML标记等
 */
public class PublicationTAKCleanTask implements IPubXmlTask {

  private static final int DB_TITLE_MAX = 250;
  /**
   * 
   */
  private String name = "publication_title_abstract_keyword_clean";

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
    // 导入的不执行
    if (XmlOperationEnum.Import.equals(context.getCurrentAction())) {
      return false;
    } ;
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

    // 处理etitle,ctitle,eabstract,cabstract,ekeywords,ckeywords

    String ctitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title");
    String etitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title");

    if (StringUtils.isNotBlank(ctitle)) {
      // ctitle = ctitle.replaceAll("&amp;", "&");
      ctitle = ctitle.replaceAll("&quot;", "\"");
      ctitle = ctitle.replaceAll("&apos;", "'");
      ctitle = ctitle.replaceAll("&gt;", ">");
      ctitle = ctitle.replaceAll("&lt;", "<");
      ctitle = ctitle.replaceAll("&nbsp;", " ");
      ctitle = ctitle.replaceAll("\r\n", "<br/>");
    }
    if (StringUtils.isNotBlank(etitle)) {
      // etitle = etitle.replaceAll("&amp;", "&");
      etitle = etitle.replaceAll("&quot;", "\"");
      etitle = etitle.replaceAll("&apos;", "'");
      etitle = etitle.replaceAll("&gt;", ">");
      etitle = etitle.replaceAll("&lt;", "<");
      etitle = etitle.replaceAll("&nbsp;", " ");
      etitle = etitle.replaceAll("\r\n", "<br/>");
    }

    ctitle = XmlUtil.trimThreateningHtml(ctitle);
    etitle = XmlUtil.trimThreateningHtml(etitle);

    etitle = XmlUtil.trimP(etitle);
    ctitle = XmlUtil.trimP(ctitle);


    if (xmlDocument.isWorkingPaper()) {
      if ("".equals(etitle)) {
        etitle = ctitle;
      } else {
        ctitle = etitle;
      }
    }

    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title", ctitle);
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title", etitle);

    String dbCode = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "source_db_code");
    // 从V2.6补充同步的结题报告成果，不需要那么严格，否则导入不了
    if (XmlOperationEnum.SyncFromOldPrj.equals(context.getCurrentAction())) {
      String tpctitle = XmlUtil.trimAllHtml(ctitle);
      String tpetitle = XmlUtil.trimAllHtml(etitle);
      if (!"".equals(tpctitle) || !"".equals(tpetitle)) {
        ctitle = tpctitle;
        etitle = tpetitle;
      }
    } else {
      // 再处理一次存放到数据库
      // scm-4094
      if (!PubXmlDbUtils.isIEEEXploreDb(dbCode)) {
        ctitle = XmlUtil.trimAllHtml(ctitle);
        etitle = XmlUtil.trimAllHtml(etitle);
      }
    }

    if ("".equals(etitle) && "".equals(ctitle)) {
      throw new XmlProcessStopExecuteException("成果标题不能为空！！！！！");
    }

    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_text",
        StringUtils.substring(ctitle, 0, DB_TITLE_MAX));
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_text",
        StringUtils.substring(etitle, 0, DB_TITLE_MAX));

    String cabstract = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract");
    if (StringUtils.isNotBlank(cabstract)) {
      cabstract = cabstract.replaceAll("&amp;", "&");
      cabstract = cabstract.replaceAll("&quot;", "\"");
      cabstract = cabstract.replaceAll("&apos;", "'");
      cabstract = cabstract.replaceAll("&gt;", ">");
      cabstract = cabstract.replaceAll("&lt;", "<");
      cabstract = cabstract.replaceAll("&nbsp;", " ");
      cabstract = cabstract.replaceAll("\r\n", "<br/>");
    }
    cabstract = XmlUtil.trimStartEndP(cabstract);
    cabstract = XmlUtil.trimThreateningHtml(cabstract);
    cabstract = cabstract.replaceAll("(&nbsp;)+", " ");
    if ("".equals(XmlUtil.trimAllHtml(cabstract))) {
      cabstract = "";
    }
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract", cabstract);

    String eabstract = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract");
    if (StringUtils.isNotBlank(eabstract)) {
      eabstract = eabstract.replaceAll("&amp;", "&");
      eabstract = eabstract.replaceAll("&quot;", "\"");
      eabstract = eabstract.replaceAll("&apos;", "'");
      eabstract = eabstract.replaceAll("&gt;", ">");
      eabstract = eabstract.replaceAll("&lt;", "<");
      eabstract = eabstract.replaceAll("&nbsp;", " ");
      eabstract = eabstract.replaceAll("\r\n", "<br/>");
    }
    eabstract = XmlUtil.trimStartEndP(eabstract);
    eabstract = XmlUtil.trimThreateningHtml(eabstract);
    eabstract = eabstract.replaceAll("(&nbsp;)+", " ");
    if ("".equals(XmlUtil.trimAllHtml(eabstract))) {
      eabstract = "";
    }
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract", eabstract);

    String ekeywords = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
    // ekeywords = PublicationHelper.changeSBCChar(ekeywords).trim();
    ekeywords = org.apache.commons.lang.StringUtils.strip(ekeywords, ";").trim();
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords", ekeywords);

    String ckeywords = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
    // ckeywords = PublicationHelper.changeSBCChar(ckeywords).trim();
    ckeywords = org.apache.commons.lang.StringUtils.strip(ckeywords, ";").trim();
    xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords", ckeywords);

    // DOI
    String doi = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi");
    xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "doi", StringUtils.trimToEmpty(doi));

    // article_number
    String articleNumber = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number");
    xmlDocument.setNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "article_number",
        StringUtils.substring(articleNumber, 0, 90));

    return true;

  }

}
