package com.smate.center.batch.chain.pubassign.task;

import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;

/**
 * ISI成果匹配-成果指派，匹配部门任务.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchDeptTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -1067383980985748550L;
  private final String name = "PubAssignMatchDeptTask";

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    return context.hasMatchedPsn();
  }

  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {
    // TODO 添加成果匹配作者部门ISI名称代码（因单位部门数据还不完善，因此，暂不处理）
    return true;
  }

}
