package com.smate.core.base.pub.consts;

/**
 * 常量
 * 
 * @author zk
 * 
 */
public class StatisticsSyncConstant {

  public final static String ADD_RECORD = "add"; // 添加记录

  public final static String DELETE_RECORD = "delete"; // 添加记录

  public final static Integer AWARD_TYPE = 111; // 赞类型

  public final static Integer SHARE_TYPE = 222; // 分享类型

  public final static Integer DOWNLOAD_TYPE = 333; // 下载类型

  public final static Integer RES_TYPE_PUB = 1;// 成果

  public final static String DOWNLOAD_NUM = "downloadNum";// 下载类型，与SnsSyncPubStatistics字段名对应

  // -------------- ACTION TYPE xys begin -------------------
  /**
   * 操作类型：阅读.
   */
  public final static int ACTION_TYPE_READ = 1;
  /**
   * 操作类型：赞.
   */
  public final static int ACTION_TYPE_AWARD = 2;
  /**
   * 操作类型：分享.
   */
  public final static int ACTION_TYPE_SHARE = 3;
  /**
   * 操作类型：下载.
   */
  public final static int ACTION_TYPE_DOWNLOAD = 4;
  // -------------- ACTION TYPE xys end -------------------
}
