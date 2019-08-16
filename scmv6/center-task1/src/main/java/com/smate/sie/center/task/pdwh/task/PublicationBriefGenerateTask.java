package com.smate.sie.center.task.pdwh.task;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.LocaleUtils;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.sie.center.task.pdwh.brief.IBriefDriver;
import com.smate.sie.core.base.utils.pub.BriefFormatter;

/** @author yamingd 生成简要描述（页面表格的来源列） */
public class PublicationBriefGenerateTask implements IPubXmlTask {

  /**
   * 
   */
  public final static String Brief_GENERATE = "brief_generate";

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#getName()
   */
  @Override
  public String getName() {
    return this.Brief_GENERATE;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#can(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#run(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    String formTmpl = xmlDocument.getFormTemplate();

    IBriefDriver briefDriver = context.getBrifDriverFactory().getDriver(formTmpl, context.getPubTypeId());
    if (briefDriver != null) {
      String briefZh = getLanguagesBrief(LocaleUtils.toLocale("zh_CN"), xmlDocument, context, briefDriver);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc", briefZh);
      String briefEn = getLanguagesBrief(LocaleUtils.toLocale("en_US"), xmlDocument, context, briefDriver);
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en", briefEn);
    }
    return true;
  }

  private String getLanguagesBrief(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context,
      IBriefDriver briefDriver) throws Exception {
    Map result = briefDriver.getData(locale, xmlDocument, context);
    String pattern = briefDriver.getPattern();
    BriefFormatter formatter = new BriefFormatter(locale, result);
    String brief = formatter.format(pattern);
    formatter = null;
    return brief;
  }

}
