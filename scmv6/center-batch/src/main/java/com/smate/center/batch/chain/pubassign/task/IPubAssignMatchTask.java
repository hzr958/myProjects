package com.smate.center.batch.chain.pubassign.task;

import java.io.Serializable;

import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;

/**
 * 成果匹配任务接口.
 * 
 * @author liqinghua
 * 
 */
public interface IPubAssignMatchTask extends Serializable {

  public String getName();

  /**
   * 是否能运行.
   * 
   * @param context
   * @return
   */
  public boolean can(PubAssginMatchContext context);

  /**
   * 匹配任务执行.
   * 
   * @param context
   * @return
   * @throws Exception
   */
  public boolean run(PubAssginMatchContext context) throws Exception;
}
