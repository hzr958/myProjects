package com.smate.center.batch.chain.pubassign.process;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.chain.pubassign.task.IPubAssignXmlTask;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * 成果指派XML拆分.
 * 
 * @author liqinghua
 * 
 */
public interface IPubAssignXmlProcess extends Serializable {

  /**
   * 注入本过程的任务单元列表.
   * 
   * @param tasks 任务单元列表
   */
  void setTasks(List<IPubAssignXmlTask> tasks);

  /**
   * @param xmlDocument XmlDocument
   * @param context XmlProcessContext上下文对象
   */
  void start(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception;
}
