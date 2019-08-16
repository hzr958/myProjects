package com.smate.center.task.model.rol.quartz;

/**
 * 成果提交统计数更新消息.
 * 
 * @author liqinghua
 * 
 */
public class PubRolSubmissionStatRefreshEnum {
  /**
   * 更新整个单位的成果提交统计数.
   */
  public static Integer REFRESH_INS = 1;

  /**
   * 更新某单位某人员的成果提交统计数.
   */
  public static Integer REFRESH_INS_PSN = 2;

}
