package com.smate.center.task.service.rol.quartz;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.IBriefDriver;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.pub.BriefFormatter;

@Service("publicationBriefGenerateSerivce")
@Transactional(rollbackFor = Exception.class)
public class PublicationBriefGenerateSerivceImpl implements PublicationBriefGenerateSerivce {
  @Override
  public String getLanguagesBrief(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context,
      IBriefDriver briefDriver) throws Exception {
    Map result = briefDriver.getData(locale, xmlDocument, context);
    String pattern = briefDriver.getPattern();
    BriefFormatter formatter = new BriefFormatter(locale, result);
    String brief = formatter.format(pattern);
    formatter = null;
    return brief;
  }

  @Override
  public boolean generateBrief(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
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

}
