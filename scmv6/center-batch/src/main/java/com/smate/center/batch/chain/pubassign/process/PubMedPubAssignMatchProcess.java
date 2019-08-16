package com.smate.center.batch.chain.pubassign.process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.smate.center.batch.chain.pubassign.task.IPubAssignMatchTask;
import com.smate.center.batch.exception.pub.XmlProcessStopExecuteException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;

/**
 * PubMed成果匹配任务.
 * 
 * @author liqinghua
 * 
 */
public class PubMedPubAssignMatchProcess implements IPubAssignMatchProcess {

  /**
   * 
   */
  private static final long serialVersionUID = -4819995073153592899L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private List<IPubAssignMatchTask> tasks = null;

  @Override
  public void setTasks(List<IPubAssignMatchTask> tasks) {
    Assert.notNull(tasks);
    Assert.notEmpty(tasks);
    this.tasks = tasks;
  }

  @Override
  public void start(PubAssginMatchContext context) throws Exception {

    for (int index = 0; index < this.tasks.size(); index++) {
      IPubAssignMatchTask task = this.tasks.get(index);
      boolean flag = task.can(context);
      if (flag) {
        try {
          boolean result = task.run(context);
          if (!result) {
            break;
          }
        } catch (XmlProcessStopExecuteException e) {
          e.printStackTrace();
          logger.error("PubMedPubAssignMatchProcessPubMed成果匹配任务: task=" + task.getName(), e);
          throw e;
        } catch (Exception e) {
          e.printStackTrace();
          logger.error("PubMedPubAssignMatchProcessPubMed成果匹配任务: task=" + task.getName(), e);
          throw e;
        }
      }
    }
    if (this.getNextProcess() != null) {
      this.getNextProcess().start(context);
    }
  }

  @Override
  public IPubAssignMatchProcess getNextProcess() {
    return null;
  }

  @Override
  public void setNextProcess(IPubAssignMatchProcess process) {}

}
