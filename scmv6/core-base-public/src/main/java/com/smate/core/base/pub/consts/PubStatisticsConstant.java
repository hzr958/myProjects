package com.smate.core.base.pub.consts;

/**
 * 成果统计常量.
 * 
 * @author xys
 * 
 */
public interface PubStatisticsConstant {

  public static final int STATISTIC_TYPE_READ = 1;// 阅读统计类型
  public static final int STATISTIC_TYPE_AWARD = 2;// 赞统计类型
  public static final int STATISTIC_TYPE_SHARE = 3;// 分享统计类型
  public static final int STATISTIC_TYPE_DOWNLOAD = 4;// 下载量统计类型

  public static final int PLUS_1 = 1;// 加1
  public static final int MINUS_1 = -1;// 减1

  public static final String FIELD_NAME_READ = "readNum";// 字段名：阅读
  public static final String FIELD_NAME_AWARD = "awardNum";// 字段名：赞
  public static final String FIELD_NAME_SHARE = "shareNum";// 字段名：分享
  public static final String FIELD_NAME_DOWNLOAD = "downloadNum";// 字段名：下载量
}
