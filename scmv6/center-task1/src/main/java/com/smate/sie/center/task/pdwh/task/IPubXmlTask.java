package com.smate.sie.center.task.pdwh.task;

/**
 * XML过程任务单元
 * 
 * @author jszhou
 *
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
