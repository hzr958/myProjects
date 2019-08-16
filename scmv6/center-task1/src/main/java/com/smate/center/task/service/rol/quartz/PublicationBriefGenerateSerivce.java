package com.smate.center.task.service.rol.quartz;

import java.util.Locale;

import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.IBriefDriver;

public interface PublicationBriefGenerateSerivce {

  boolean generateBrief(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception;

  String getLanguagesBrief(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context,
      IBriefDriver briefDriver) throws Exception;

}
