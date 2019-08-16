package com.smate.center.batch.service.pub;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;


/**
 * @author yamingd XML过程任务单元
 */
public interface IPubXmlTask {
  /**
   * 任务单元名称.
   * 
   * @return Sting
   * 
   */
  String getName();

  /**
   * 是否可以运行本任务单元.
   * 
   * @param xmlDocument XmlDocument
   * @param context XmlProcessContext
   * @return boolean
   */
  boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context);

  /**
   * 执行本任务单元.
   * 
   * @param xmlDocument XmlDocument
   * @param context XmlProcessContext
   * @return boolean
   * @throws Exception Exception
   */
  boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception;
}
