/**
 * 
 */
package com.smate.center.batch.chain.pub.rol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.smate.center.batch.enums.pub.XmlOperationEnum;
import com.smate.center.batch.exception.pub.XmlProcessStopExecuteException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.process.pub.BasePubXmlProcess;
import com.smate.center.batch.process.pub.IPubXmlProcess;
import com.smate.center.batch.service.pub.IPubXmlTask;

/**
 * 后台导入成果XML处理.
 * 
 * @author yamingd
 */
public class ROLXmlBackgroundImportProcess extends BasePubXmlProcess {

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
  public IPubXmlProcess getNextProcess() {
    return this.nextProcess;
  }

  @Override
  public void setNextProcess(IPubXmlProcess process) {
    if (process != null) {
      String name = process.getClass().getName();
      if (name.endsWith("ROLXmlBackgroundImportProcess")) {
        return;
      }
    }
    this.nextProcess = process;
  }

  @Override
  public void setTasks(List<IPubXmlTask> tasks) {
    Assert.notNull(tasks);
    Assert.notEmpty(tasks);

    this.tasks = tasks;
    super.setTasks(tasks);
    logger.info("初始化ROLXmlBackgroundImportProcess Tasks成功");
  }

  @Override
  public void start(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    context.setCurrentAction(XmlOperationEnum.OfflineImport);

    for (int index = 0; index < this.tasks.size(); index++) {
      IPubXmlTask task = this.tasks.get(index);
      boolean flag = task.can(xmlDocument, context);
      if (flag) {
        try {

          task.run(xmlDocument, context);

        } catch (XmlProcessStopExecuteException e) {
          e.printStackTrace();
          logger.error("ROLXmlBackgroundImportProcess终止处理该XML: task=" + task.getName(), e);
          throw e;
        } catch (Exception e) {
          e.printStackTrace();
          logger.error("ROLXmlBackgroundImportProcess终止处理该XML: task=" + task.getName(), e);
          throw e;
        }
      }
    }
    if (this.nextProcess != null) {
      this.nextProcess.start(xmlDocument, context);
    }
  }

}
