package com.smate.center.batch.chain.pubassign.process;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.chain.pubassign.task.IPubAssignMatchTask;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;


/**
 * 成果指派XML拆分.
 * 
 * @author liqinghua
 * 
 */
public interface IPubAssignMatchProcess extends Serializable {

  /**
   * 注入本过程的任务单元列表.
   * 
   * @param tasks 任务单元列表
   */
  void setTasks(List<IPubAssignMatchTask> tasks);

  /**
   * context上下文对象.
   * 
   * @param context
   * @throws Exception
   */
  void start(PubAssginMatchContext context) throws Exception;

  /**
   * 获取下一个任务集.
   * 
   * @return
   */
  public IPubAssignMatchProcess getNextProcess();

  /**
   * 设置下一个任务集合.
   * 
   * @param process
   */
  public void setNextProcess(IPubAssignMatchProcess process);
}
