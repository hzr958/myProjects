package com.smate.center.task.single.constants;

import java.io.Serializable;

/**
 * 推荐动态常量类.
 * 
 * @author mjg
 * 
 */
public class RcmdDynamicConstants implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 8124208366497581035L;
  public static final int DYN_RECOMMEND_TYPE_PUB = 0;// 推荐动态类型-成果.
  public static final int DYN_RECOMMEND_TYPE_FUND = 2;// 推荐动态类型-基金.
  public static final String DYN_RCMD_ACTION_SAVE = "action_save";// 保存推荐动态.
  public static final String DYN_RCMD_ACTION_DEL = "action_del";// 删除推荐动态.
}
