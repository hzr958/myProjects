package com.smate.center.batch.process.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.XmlProcessStopExecuteException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;

/**
 * @author zjh 成果认领任务链
 */
public class PubConfirmXmlProcess extends BasePubXmlProcess {
  // logger.
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
  	 * 
  	 */
  private IPubXmlProcess nextProcess = null;
  /**
  	 * 
  	 */
  private List<IPubXmlTask> tasks = null;

  @Override
  public void setNextProcess(IPubXmlProcess process) {
    if (process != null) {
      String name = process.getClass().getName();
      if (name.endsWith("PubConfirmXmlProcess")) {
        return;
      }
    }
    this.nextProcess = process;

  }


  @Override
  public IPubXmlProcess getNextProcess() {
    return this.nextProcess;
  }

  @Override
  public void setTasks(List<IPubXmlTask> tasks) {
    Assert.notNull(tasks);
    Assert.notEmpty(tasks);
    this.tasks = tasks;
    super.setTasks(tasks);
    logger.info("初始化PubConfirmXmlProcess Tasks成功");
  }

  @Override
  public void start(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    for (int index = 0; index < this.tasks.size(); index++) {
      IPubXmlTask task = this.tasks.get(index);
      boolean flag = task.can(xmlDocument, context);
      if (flag) {
        try {

          task.run(xmlDocument, context);

        } catch (XmlProcessStopExecuteException e) {
          logger.error("终止处理该XML", e);
          throw e;
        }
      }
    }
    if (this.nextProcess != null) {
      this.nextProcess.start(xmlDocument, context);
    }

  }

}
