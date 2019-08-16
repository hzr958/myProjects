package com.smate.center.batch.chain.pubassign.process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.smate.center.batch.chain.pubassign.task.IPubAssignMatchTask;
import com.smate.center.batch.exception.pub.XmlProcessStopExecuteException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;

/**
 * 单位人员匹配单位所有Cnipr成果任务第一步.
 * 
 * @author liqinghua
 * 
 */
public class CniprPsnAssignMatchStep1Process implements IPubAssignMatchProcess {

  /**
   * 
   */
  private static final long serialVersionUID = 693133856385941029L;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private List<IPubAssignMatchTask> tasks = null;
  private IPubAssignMatchProcess nextProcess;

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
          task.run(context);
        } catch (XmlProcessStopExecuteException e) {
          logger.error("CniprPsnAssignMatchStep1Process成果匹配任务: task=" + task.getName(), e);
          throw e;
        } catch (Exception e) {
          logger.error("CniprPsnAssignMatchStep1Process成果匹配任务: task=" + task.getName(), e);
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
    return this.nextProcess;
  }

  @Override
  public void setNextProcess(IPubAssignMatchProcess process) {
    this.nextProcess = process;
  }

}
