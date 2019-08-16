package com.smate.web.prj.consts;

import java.util.ArrayList;
import java.util.List;

/**
 * 资助机构常量类
 * 
 * @author wsn
 * @date Nov 13, 2018
 */
public class FundAgencyConst {

  public static final Integer AWARD_AGENCY_OPT = 1; // 赞资助机构

  public static final Integer CANCEL_AWARD_AGENCY_OPT = 0; // 取消赞资助机构

  public static final Integer INTEREST_AGENCY_OPT = 1; // 关注资助机构

  public static final Integer CANCEL_INTEREST_AGENCY_OPT = 0; // 取消关注资助机构

  public static final Integer SHARE_TO_DYN = 1; // 分享到动态

  public static final Integer SHARE_TO_FRIEND = 2; // 分享给好友

  public static final Integer SHARE_TO_GROUP = 3; // 分享到群组

  public static final Integer SHARE_TO_WECHAT = 4; // 出微信二维码

  public static final Integer SHARE_TO_SINA = 5; // 分享到新浪微博

  public static final Integer SHARE_TO_FACEBOOK = 6; // 分享到facebook

  public static final Integer SHARE_TO_LINKEDIN = 7; // 分享到Linkedin

  public static final Integer SHARE_TO_QQ = 8; // 分享到QQ

  public static final Integer ADD_AGENCY_STATISTICS = 1; // 增加资助机构操作统计数

  public static final Integer REDUCE_AGENCY_STATISTICS = 0; // 减少资助机构操作统计数

  public static final List<Integer> SHARE_TYPE_LIST = new ArrayList<Integer>(); // 分享操作类型

  public static final List<Integer> AWARD_OPT_LIST = new ArrayList<Integer>(); // 赞操作类型

  public static final List<Integer> INTEREST_OPT_LIST = new ArrayList<Integer>(); // 关注操作类型

  public static final String ERROR_MSG_AGENCY_OR_AGENCY_ID_NULL = "scm-1001"; // 资助机构或资助机构ID为空
  public static final String ERROR_MSG_PSN_ID_NULL = "scm-1002"; // 人员ID为空
  public static final String ERROR_MSG_OPERATE_TYPE_ERROR = "scm-1003"; // 操作类型错误
  public static final String ERROR_MSG_MAXNUM_AGENCY = "scm-1004"; // 已经关注了足够多的资助机构
  public static final String ERROR_MSG_MINNUM_AGENCY = "scm-1005"; // 至少要关注一定数目的资助机构
  public static final String ERROR_MSG_NOT_INTERESTED_AGENCY = "scm-1006"; // 未关注该资助机构
  public static final String ERROR_MSG_HAS_INTERESTED_AGENCY = "scm-1007"; // 已关注该资助机构
  public static final String ERROR_MSG_SHARE_TO_PLATFORM_ERROR = "scm-1008"; // 分享资助机构，分享平台参数错误

  static {
    // 分享操作类型
    SHARE_TYPE_LIST.add(SHARE_TO_DYN);
    SHARE_TYPE_LIST.add(SHARE_TO_FRIEND);
    SHARE_TYPE_LIST.add(SHARE_TO_GROUP);
    SHARE_TYPE_LIST.add(SHARE_TO_WECHAT);
    SHARE_TYPE_LIST.add(SHARE_TO_SINA);
    SHARE_TYPE_LIST.add(SHARE_TO_FACEBOOK);
    SHARE_TYPE_LIST.add(SHARE_TO_LINKEDIN);
    SHARE_TYPE_LIST.add(SHARE_TO_QQ);
    // 赞操作类型
    AWARD_OPT_LIST.add(AWARD_AGENCY_OPT);
    AWARD_OPT_LIST.add(CANCEL_AWARD_AGENCY_OPT);
    // 关注操作类型
    INTEREST_OPT_LIST.add(INTEREST_AGENCY_OPT);
    INTEREST_OPT_LIST.add(CANCEL_INTEREST_AGENCY_OPT);
  }

}
