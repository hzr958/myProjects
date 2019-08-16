package com.smate.center.task.quartz;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;

import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.IBriefDriver;
import com.smate.center.task.single.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pub.BriefFormatter;

public class PublicationBriefGenerateTask implements IPubXmlTask {
  /**
   * 
   */
  public final static String Brief_GENERATE = "brief_generate";

  @Override
  public String getName() {
    return this.Brief_GENERATE;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return true;
  }

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
