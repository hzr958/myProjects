package com.smate.center.batch.chain.pubassign.process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.chain.pubassign.task.IPubAssignXmlTask;
import com.smate.center.batch.exception.pub.XmlProcessStopExecuteException;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.rol.pubassign.PubAssignEiXmlService;

/**
 * Ei成果指派XML展开.
 * 
 * @author liqinghua
 * 
 */
public class EiPubAssignXmlProcess implements IPubAssignXmlProcess {


  /**
   * 
   */
  private static final long serialVersionUID = 9191428796000489716L;
  // logger.
  private Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 
   */
  private List<IPubAssignXmlTask> tasks = null;
  @Autowired
  private PubAssignEiXmlService pubAssignXmlService;

  @Override
  public void setTasks(List<IPubAssignXmlTask> tasks) {
    Assert.notNull(tasks);
    Assert.notEmpty(tasks);

    this.tasks = tasks;

    logger.info("初始化EiPubAssignXmlProcess Tasks成功");
  }

  @Override
  public void start(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    if (context.isImport() && context.isEiImport()) {

      // 如果已经展开过成果XML，则没必要再次展开
      if (pubAssignXmlService.isExtractAssignData(context.getCurrentPubId())) {
        return;
      }
      for (int index = 0; index < this.tasks.size(); index++) {
        IPubAssignXmlTask task = this.tasks.get(index);
        boolean flag = task.can(xmlDocument, context);
        if (flag) {
          try {
            boolean result = task.run(xmlDocument, context);
            if (!result) {
              break;
            }
          } catch (XmlProcessStopExecuteException e) {
            logger.error("EiPubAssignXmlProcess终止处理该XML: task=" + task.getName(), e);
            throw e;
          } catch (Exception e) {
            logger.error("EiPubAssignXmlProcess终止处理该XML: task=" + task.getName(), e);
            throw e;
          }
        }
      }
    }
  }

}
