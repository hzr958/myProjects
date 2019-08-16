package com.smate.web.mobile.v8pub.consts;

/**
 * @ClassName PubApiInfoConsts
 * @Description 移动端成果操作API调用地址
 * @Author LIJUN
 * @Date 2018/8/16
 * @Version v1.0
 */
public class PubApiInfoConsts {
  // sns 成果操作
  public static String LIKE_SNS = "/data/pub/opt/like";// 赞/取消赞成果
  public static String COMMENT_SNS = "/data/pub/opt/comment";// 评论成果
  public static String COMMENT_LIST_SNS = "/data/pub/opt/commentlist";// 成果评论列表
  public static String SHARE_CALLBACK_SNS = "/data/pub/opt/share";// 分享成果回调（增加统计数）
  public static String UPDATE_PERMISSION = "/data/pub/opt/permission";// 更改成果权限
  public static String STATISTICS_SNS = "";// 成果统计数
  public static String COMMENTNUMBER_SNS = "/data/pub/opt/commentnumber";// 成果评论数
  public static String VIEW_CALLBACK_SNS = "/data/pub/opt/view";// 成果查看回调，增加阅读数
  public static String RCMD_FT_LIST_SNS = "/data/pub/opt/view";// 全文认领数据列表
  public static String PUB_DETAIL_SNS = "/data/pub/details/sns";// 个人成果详情sns
  public static String DEAL_SNS_PUB_FULLTEXT_REQ = "/data/pub/fulltext/ajaxrequpdate";// 处理全文请求消息
  public static String ADD_SNS_PUB_FULLTEXT_REQ = "/data/pub/fulltext/ajaxreqadd"; // 添加全文请求
  public static String PUB_FULLTEXT_CONFIRM_COUNT = "/data/pub/getpubfulltextconfirmcount";// 成果全文认领统计数
  public static String PUB_CONFIRM_COUNT = "/data/pub/getpubconfirmcount";// 成果认领数
  public static String IMPORT_SNS_PUB = "/data/pub/sns/importtomypub";// 导入个人库成果（个人库成果详情页面“这是我的成果”功能）
  public static String PUB_FULLTEXT_PERMIS = "/data/pub/fulltext/getPermission";// 获取成果全文权限
  public static String PUB_FULLTEXT_LIST = "/data/pub/pubfulltextlist";// 成果全文认领列表
  public static String CONFIRM_PUB_FULLTEXT = "/data/pub/opt/confirmpubft";// 确认是成果的全文
  public static String REJECT_PUB_FULLTEXT = "/data/pub/opt/rejectpubft";// 确认不是成果的全文
  public static String PUB_FULLTEXT_DETAILS = "/data/pub/pubfulltext/details"; // 全文详情信息
  public static String SNS_PUB_STATISTICS = "/data/pub/sns/statistics"; // 个人库成果统计数
  public static String OPT_STATUS_SNS = "/data/pub/sns/optstatus";// 个人库成果操作状态
  public static String CONFIRM_PUB_OPT = "/data/pub/affirmconfirm"; // 确认成果认领
  public static String REJECT_PUB_OPT = "/data/pub/ignoreconfirm"; // 忽略成果认领
  public static String PUB_INDEX_AND_TOTAL_COUNT = "/data/pub/index"; // 按指定查询条件，查询指定的成果所在的index和一共能查找到的成果数量
  // 公共
  public static String PUB_DETAIL = "/data/pub/query/detail";// 成果详情查询（简要信息，不含统计数等数据）
  public static String CHECK_PDWH_PUB_ISDEL = "/data/pub/checkpdwhPub";// 查看基准库成果是否删除
  public static String PRJ_DETAI = "/app/prjweb/query/detail";// 项目详情查询
  public static String FUND_DETAI = "/app/prjweb/fund/query/detail";// 基金详情查询
  public static String PUB_LIST = "/data/pub/query/list";// 成果列表
  public static String OPEN_PUB_LIST = "/data/pub/getOpenPubList";// 公开的成果列表

  public static String PSN_HAS_PRIVATE_PUB = "/data/pub/psnhasprivatepub";// 个人是否有隐私成果的数量
  public static String SAVE_REPRESENT_PUB = "/data/pub/saverepresentpub";// 保存代表成果

  public static String COLLECT_PUB = "/data/pub/opt/Collect";// 收藏取消收藏成果

  public static String INIT_IMPORT_PUB = "/data/pub/import/init";// 初始化待导入成果
  public static String SAVE_IMPORT_PUB = "/data/pub/import/save";// 保存待导入成果

  // pdwh成果操作
  public static String EXISTS_PDWH = "/data/pub/opt/ajaxpdwhisexists";// 赞/取消赞成果
  public static String LIKE_PDWH = "/data/pub/opt/pdwhlike";// 赞/取消赞成果
  public static String COMMENT_PDWH = "/data/pub/opt/pdwhcomment";// 评论成果
  public static String COMMENT_LIST_PDWH = "/data/pub/opt/commentpdwhlist";// 成果评论列表
  public static String SHARE_CALLBACK_PDWH = "/data/pub/opt/pdwhshare";// 分享成果回调（增加统计数）
  public static String STATISTICS_PDWH = "/data/pub/pdwhstatistics";// 成果统计数
  public static String COMMENTNUMBER_PDWH = "/data/pub/opt/pdwhcommentnumber";// 成果评论数
  public static String VIEW_CALLBACK_PDWH = "/data/pub/opt/pdwhview";// 成果查看回调，增加阅读数
  public static String PUB_DETAIL_PDWH = "/data/pub/details/pdwh";// 个人成果详情pdwh
  public static String OPT_STATUS_PDWH = "/data/pub/pdwh/optstatus"; // 对基准库操作的状态（是否收藏该基准库成果，是否赞了该基准库成果）
  public static String IMPORT_PDWH_PUB = "/data/pub/import/pdwhpubtomypub";// 导入基准库成果（基准库成果详情页面“这是我的成果”功能）

  public final static String GET_PUB_CONDITIONAL = "/data/pubrecommend/getconditional";// 查询推荐设置条件
  public final static String PUB_RECOMMEND_LIST = "/data/pubrecommend/publist";// 查询推荐成果
  public final static String ADD_SCIENAREA = "/data/pub/recommend/addscienareas";// 添加科技领域
  public final static String MOBILE_ADD_SCIENAREAS = "/data/pubrecommend/savepsnareas";// 移动端添加科技领域
  public final static String DELETE_SCIENAREA = "/data/pub/recommend/deletescienareas";// 删除科技领域
  public final static String ADD_KEYWORD = "/data/pub/recommend/addkeywords";// 添加关键词
  public final static String MOBILE_ADD_KEYWORDS = "/data/pubrecommend/savepsnkeyword";// 移动端添加关键词
  public final static String DELETE_KEYWORD = "/data/pub/recommend/deletekeywords";// 删除关键词
  public final static String ALL_KEYWORD_LIST = "/data/pubrecommend/allpsnkeylist";// 查询推荐成果
  public static String NOT_VIEW_PDWH = "/data/pub/opt/uninterestedremdpub";// 首页论文推荐不感兴趣
  public final static String GET_PUB_RECOMMEND_DYN = "/data/pub/recommend/getpubrecommendshowindyn";

  public static String UPDATE_RES_STATIC = "/dyndata/share/updatesharestatic";// 更新分享到站外的资源统计数

  public static String REPRESENT_PRJ_LIST = "/psndata/mobile/getrepresentprjlist";// 代表项目的
  public static String REPRESENT_SAVE_PRJ = "/psndata/mobile/saverepresentprjlist";// 代表项目的
  public static String PERSON_PRJ_LIST = "/prjdata/wechat/findprjs";// 人员的项目
  public static String REPRESENT_PRJ_CONDITION_AGENCY = "/prjdata/wechat/prjcondition";// 代表项目查询条件获取资助机构

}
