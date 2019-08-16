package com.smate.web.v8pub.consts;

/**
 * v8pub项目查询成果的相关常量
 * 
 * @author aijiangbin
 * @date 2018年8月14日
 */
public class V8pubQueryPubConst {

  public static final String QUERY_PUB_URL = "/data/pub/query/list"; // v8pub项目
  // 查询成果的url

  public static final String QUERY_PUB_DETAIL_URL = "/data/pub/query/detail"; // v8pub项目
  // 查询成果详情的url

  /**
   * 获取成果列表的服务
   */
  public static final String QUERY_PUB_BY_PUB_ID_SERVICE = "pubQueryByPubId"; // 通过id查询成果的service

  public static final String QUERY_GRP_PUB_BY_PUB_ID_SERVICE = "grpPubQueryByPubId"; // 通过id查询群组成果的service

  public static final String QUERY_PDWH_PUB_BY_PUB_ID_SERVICE = "pdwhPubQueryByPubId"; // 通过id查询基准库的成果的service

  public static final String QUERY_PUB_FULLTEXT_BY_PUB_ID_SERVICE = "pubFulltextQueryByPubId"; // 通过id查询成果全文信息的service

  public static final String QUERY_SNS_PUB_DETAIL_BY_PUB_ID_SERVICE = "pubFulltextQueryByPubId"; // 通过id查询个人库的成果详情
  // ,信息比较少

  public static final String CENTER_OPEN_PUB_LIST = "centerOpenPubList"; // 查询成果列表，返回基本信息和统计数

  public static final String CENTER_OPEN_GRP_PUB_LIST = "centerOpenGrpPubList"; // 查询成果列表，返回基本信息和统计数

  public static final String QUERY_PUB_SITUATION_LIST = "pubSituationList";// 查询成果列表
  // 不分页
  public static final String QUERY_PUB_UNCONFIRM_LIST = "pubConfirmList";// 未认领的成果列表

  public static final String CENTER_OPEN_PUB_ASSIGN_LOG_LIST = "centerOpenPubAssignLogList";// 未认领的成果列表

  public static final String OPEN_PSN_PUBLIC_PUB = "openPsnPublicPub";// 公开的成果

  public static final String PUB_LIST_QUERY_BY_PUB_IDS = "pubListQueryByPubIds";// 通过pubids 查询成果列表

  public static final String QUERY_PSN_REPRESENT_PUBS = "psnRepresentPubList"; // 通过des3PsnId获取个人的代表成果
  /**
   * 查看单个成果的详情的服务
   */
  public static final String QUERY_SNS_EDIT_PUB_DETAIL_BY_PUB_ID_SERVICE = "snsEditPub"; // 通过id查询个人库的所有和详情相关的信息
  public static final String QUERY_OPEN_SNS_PUB_DETAIL_BY_PUB_ID_SERVICE = "openSnsPub"; // 通过id查询个人库的基本信息
  public static final String OPEN_GRP_PUB = "openGrpPub"; // open系统群组成果基本信息
  public static final String PDWH_PUB = "pdwhPub"; // 基准库成果详情
  public static final String PDWH_PUBBY_PUB_CONFIRM_ID = "pdwhPubByPubConfirmId"; // 基准库成果详情

  /**
   * 成果认领
   */
  public static final String PUB_CONFIRM = "/data/pub/pubconfirm";

  public final static String SUCCESS = "success"; // 成功状态

  public final static String ERROR = "error"; // 失败状态

}
