package com.smate.center.batch.process.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;


/**
 * 转换导入XMl成为标准XML.
 * 
 * @author hzr
 * 
 */
public class PubImportXmlTranslateProcess extends BasePubXmlProcess {

  // logger.
  private Logger logger = LoggerFactory.getLogger(getClass());

  private List<IPubXmlTask> tasks = null;

  @Override
  public IPubXmlProcess getNextProcess() {
    return null;
  }

  @Override
  public void setNextProcess(IPubXmlProcess process) {

  }

  @Override
  public void setTasks(List<IPubXmlTask> tasks) {
    Assert.notNull(tasks);
    Assert.notEmpty(tasks);
    this.tasks = tasks;
    super.setTasks(tasks);
    logger.info("初始化ImportXmlTranslateProcess Tasks成功");
  }

  @Override
  public void start(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    for (int index = 0; index < this.tasks.size(); index++) {
      IPubXmlTask task = this.tasks.get(index);
      boolean flag = task.can(xmlDocument, context);
      if (flag) {
        try {

          task.run(xmlDocument, context);

        } catch (Exception e) {
          logger.error("ImportXmlTranslateProcess终止处理该XML", e);
          throw e;
        }
      }
    }
  }

}
