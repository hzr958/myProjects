package com.smate.web.mobile.v8pub.consts;

/**
 * @description 移动端群组文件相关常量类
 * @author xiexing
 * @date 2019年5月14日
 */
public class MobileGrpFileApiConsts {
  /**
   * 查询群组文件列表
   */
  public static final String QUERY_GRP_FILE_LIST = "/grpdata/file/querygrpfilelist";

  /**
   * 查询群组文件筛选条件
   */
  public static final String QUERY_CONDITIONS = "/grpdata/file/queryconditions";

  /**
   * 获取文件详情进行分享
   */
  public static final String QUERY_FILE_DETAIL = "/grpdata/file/querygrpfiledetail";

  /**
   * 获取分享联系人
   */
  public static final String QUERY_SHARE_FRIEND = "/psndata/friend/ajaxgetmyfriendnames";

  /**
   * 获取群组列表
   */
  public static final String QUERY_SHARE_GRP_LIST = "/grpdata/mygrouplist";

  /**
   * 获取选中的群组信息
   */
  public static final String QUERY_SHARE_GRP_INFO = "/grpdata/file/grpinfo";

  /**
   * 文件分享给联系人
   */
  public static final String FILE_SHARE_TO_FRIEND = "/grpdata/file/sharetofriend";

  /**
   * 文件分享给群组
   */
  public static final String FILE_SHARE_TO_GRP = "/dyndata/file/sharetogrp";

}
