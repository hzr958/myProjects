package com.smate.center.batch.process.pub;

import java.util.List;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;

/**
 * XML处理过程接口.
 * 
 * @author yamingd
 * 
 */
public interface IPubXmlProcess {
  /**
   * 注入下一个过程实例.
   * 
   * @param process 过程
   */
  void setNextProcess(IPubXmlProcess process);

  /**
   * 返回下一个过程实例.
   * 
   * @return IXmlProcess
   */
  IPubXmlProcess getNextProcess();

  /**
   * 注入本过程的任务单元列表.
   * 
   * @param tasks 任务单元列表
   */
  void setTasks(List<IPubXmlTask> tasks);

  /**
   * 获取task.
   * 
   * @param name
   * @return
   */
  IPubXmlTask getPubXmlTask(String name);

  /**
   * @param xmlDocument XmlDocument
   * @param context XmlProcessContext上下文对象
   */
  void start(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception;
}
