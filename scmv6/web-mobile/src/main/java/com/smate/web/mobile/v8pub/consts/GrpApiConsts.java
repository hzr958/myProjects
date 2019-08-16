package com.smate.web.mobile.v8pub.consts;

/**
 * 群组相关API
 * 
 * @author wsn
 * @date May 8, 2019
 */
public class GrpApiConsts {

  public static final String GET_GRP_MEMBERS = "/grpdata/member/list"; // 获取群组成员
  public static final String GET_GRP_BASEINFO = "/grpdata/info/base"; // 获取群组基本信息
  public static final String GET_GRP_APPLY_MEMBERS = "/grpdata/member/apply"; // 获取群组成员
  public static final String DEAL_GRP_MEMBERS_APPLY = "/grpdata/apply/deal"; // 获取群组成员
  public static final String GET_GRP_RELATION_PRJ_INFO = "/prjdata/relationgrp/info";// 获取与项目关联的群组信息
  public static final String GET_GRP_DYN_LIST = "/dyndata/grpdyn/list"; // 获取群组动态列表信息
  public static final String API_RESULT_SUCCESS = "success"; // 成功标志
  public static final String API_RESULT_ERROR = "error"; // 失败标志

  public static String FIND_GROUP_LIST = "/grpdata/findgrouplist";// 获取推荐群组列表
  public static String MY_GROUP_LIST = "/grpdata/mygrouplist";// 获取我的群组列表
  public static String GET_SHARE_GRPLIST = "/grpdata/sharegrouplist";// 获取分享群组列表
  public static String OP_RCMD_GROUP = "/grpdata/optionrcmdgrp";// 获取我的群组列表
  public static String OP_JOIN_GROUP = "/grpdata/applyjoingrp";// 获取我的群组列表
  public static String GRP_PUB_LIST = "/grpdata/grouppublist";// 获取群组成果列表
  public static String GRP_SHARE_PUB_LIST = "/grpdata/sharegrppublist";// 群组动态获取群组成果列表
  public static String GET_GRP_CONTROL = "/grpdata/getgrpcontrol";// 获取群组设置
  public static String GRP_DYN_AWARD = "/dyndata/grpdyn/award"; // 处理群组动态赞操作
  public static String GRP_COLLECT_FILE = "/grpdata/file/collect"; // 收藏群组文件操作
  public static String GRP_DYN_PUBLISH_BASE_INFO = "/dyndata/publish/psninfo"; // 群组动态发布所需相关信息
  public static String GRP_DYN_PUBLISH = "/dyndata/grpdyn/publish"; // 发布新的群组动态
  public static String GRP_DYN_DETAILS = "/dyndata/grpdyn/info"; // 单个群组动态信息
  public static String GRP_DYN_COMMENTS_LIST = "/dyndata/grpdyn/comments"; // 群组动态评论列表
  public static String GRP_DYN_DO_COMMENT = "/dyndata/grpdyn/docomment"; // 评论群组动态
  public static String GRP_OPT_STICKY = "/grpdata/opt/sticky"; // 群组置顶操作
  public static String GRP_OPT_QUIT = "/grpdata/opt/quit"; // 退出群组操作

}
