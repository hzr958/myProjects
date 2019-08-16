package com.smate.center.batch.chain.pubassign.task;

import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;

/**
 * scopus成果匹配-成果指派，匹配部门任务.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchSpsDeptTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -1842265870938413389L;
  private final String name = "PubAssignMatchSpsDeptTask";

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
    // TODO 添加成果匹配作者部门scopus名称代码（因单位部门数据还不完善，因此，暂不处理）
    return true;
  }

}
