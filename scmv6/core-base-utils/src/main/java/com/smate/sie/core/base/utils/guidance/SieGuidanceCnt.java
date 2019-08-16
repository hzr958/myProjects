package com.smate.sie.core.base.utils.guidance;

/**
 * 二次引导后台任务使用常量
 * 
 * @author ztg
 *
 */
public class SieGuidanceCnt {

  // 引导规则CID值
  public static final int CNF_COMPLETE_INS_CID = 1; // 完善单位信息
  public static final int CNF_MAINTAIN_INS_CID = 5; // 单位信息是否变化
  public static final int CNF_UNITMERGE_CID = 7; // 部门是否合并
  public static final int CNF_FUNDCHANGE_CID = 8; // 基金机会有变化


  // 引导规则表中MSG字段被替换值
  public static final String CNF_MAINTAIN_MSG_1 = "maintainDate";
  public static final String CNF_UNITMERGE_MSG_1 = "total";
  public static final String CNF_FUNDCHANGE_MSG_1 = "count1";
  public static final String CNF_FUNDCHANGE_MSG_2 = "count2";
}
