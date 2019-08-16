package com.smate.center.task.single.constants;

/**
 * 邮件推广常量类
 * 
 * @author zk
 */
public interface EtemplateConstant {

  final static String LOCALE_ZH_CN = "zh_CN";// 当前语言环境-中文.
  final String ZH_LOCALE = "zh_CN";
  final String EN_LOCALE = "en_US";
  final static String TEMPLATE_SUFFIX = ".ftl";// 邮件模版的后缀名.
  // 模板CODE 见bpo.promote_mail_template

  // 根据好友的论文推荐任务beanName
  final String BASE_FRD_PUB_RECMD = "baseFrdPubRecmd";
  // 模板编号
  final Integer FRD_PUB_RECMD_CODE = 21;
  // 模板名
  final String FRD_PUB_RECMD_TEMPLATE = "Base_friend_Pub_Recmd";

  // 研究领域推荐
  final String RESEACHAREA_RECMD = "researchAreaRecmd";
  final Integer RESEACHAREA_RECMD_CODE = 22;
  final String RESEACHAREA_RECMD_TEMPLATE = "ResearchArea_Recommend";

  // 根据会议论文的期刊推荐
  final String BASE_PUB_JOURNAL_RECMD = "basePubJournalRecmd";
  final Integer BASE_PUB_JOURNAL_RECMD_CODE = 23;
  final String BASE_PUB_JOURNAL_RECMD_TEMPLATE = "Base_Pub_Journal_Recommend";

  // 根据研究领域的基金机会推荐
  final String BASE_RA_FC_RECMD = "baseRAFCRecmd";
  final Integer BASE_RA_FC_RECMD_CODE = 24;
  final String BASE_RA_FC_RECMD_TEMPLATE = "Base_ResearchArea_FundChance_Recmd";

  // 根据研究领域的期刊推荐
  final String BASE_RA_JOURNAL_RECMD = "baseRAJournalRecmd";
  final Integer BASE_RA_JOURNAL_RECMD_CODE = 25;
  final String BASE_RA_JOURNAL_RECMD_TEMPLATE = "Base_ResearchArea_Journal_Recmd";

  // 根据研究领域的论文推荐
  final String BASE_RA_pub_RECMD = "baseRAPubRecmd";
  final Integer BASE_RA_PUB_RECMD_CODE = 26;
  final String BASE_RA_PUB_RECMD_TEMPLATE = "Base_ResearchArea_Pub_Recmd";

  // 邀请认同研究领域
  final String INVITE_ENDORSE_RESEARCHAREA = "inviteEndorseResearchArea";
  final Integer INVITE_ENDORSE_RESEARCHAREA_CODE = 27;
  final String INVITE_ENDORSE_RESEARCHAREA_TEMPLATE = "Invite_endorse_researchArea";

  // 通知被认同并邀请认同对方
  final String NOTICE_BE_ENDORSED_INVITE = "noticeBeEndorsedInvite";
  final Integer NOTICE_BE_ENDORSED_INVITE_CODE = 28;
  final String NOTICE_BE_ENDORSED_INVITE_TEMPLATE = "Notice_Be_Endorsed_Invite";

  // 好友动态更新
  final String FRIEND_WEEK_DYNAMIC = "friendWeekDynamic";
  final Integer FRIEND_WEEK_DYNAMIC_CODE = 116;
  final String FRIEND_WEEK_DYNAMIC_TEMPLATE = "weekly_friends_status_update";

  // 成果更新,确认
  final String PUB_CONFIRM_PROMOTE = "pubConfirmPromote";
  final Integer PUB_CONFIRM_CODE = 114;
  final String PUB_CONFIRM_TEMPLATE = "new_thesis_update";

  // 基金类别推荐（除优青、杰青、创新研究群体）
  final String FUND_CATEGORY_RECMD = "fundCategoryRecmd";
  final Integer FUND_CATEGORY_RECMD_CODE = 29;
  final String FUND_CATEGORY_RECMD_TEMPLATE = "NSFC_Fund_Category_Recmd";

  // 个人Hindex指数、成果数计算任务
  final String PSN_HINDEX_PUBS = "psnHindexAndPubs";
  final Integer PSN_HINDEX_PUBS_CODE = 0;

  // 基金类别推荐[只适用于优青（优秀青年科学基金）、杰青（国家杰出青年科学基金）、创新研究群体]
  final String FUND_CATEGORY_JQYQ_RECMD = "fundCategoryJqyqRecmd";
  final Integer FUND_CATEGORY_JQYQ_RECMD_CODE = 30;
  final String FUND_CATEGORY_JQYQ_RECMD_TEMPLATE = "NSFC_Only_Jqyq_Recmd";

  // 每天邮件监控日志
  final String DAYS_EMAIL_MONITOR = "daysEmailMonitor";
  final Integer DAYS_EMAIL_MONITOR_CODE = 31;
  final String DAYS_EMAIL_MONITOR_TEMPLATE = "days_email_monitor";

  // 申请基金的合作者推荐
  final String APPLY_FUND_COOP_RECMD = "applyFundCoopRecmd";
  final Integer APPLY_FUND_COOP_RECMD_CODE = 32;
  final String APPLY_FUND_COOP_RECMD_TEMPLATE = "apply_fund_coop_recommend";

  // ［方向名/项目名］的重点项目
  final String FUND_CATEGORY_IMPRT = "fundCategoryImprt";
  final Integer FUND_CATEGORY_IMPRT_CODE = 33;
  final String FUND_CATEGORY_IMPRT_TEMPLATE = "Fund_Category_Imprt_Recmd";

  // 引用数更新标题：您最新的论文引用次数和H指数
  final String UPDATE_PUB_CITED = "updatePubCited";
  final Integer UPDATE_PUB_CITED_CODE = 35;
  final String UPDATE_PUB_CITED_TEMPLATE = "update_pub_cited";

  // 推荐论文
  final String RECOMMEND_PUB = "recommendPub";
  final Integer RECOMMEND_PUB_CODE = 184;
  final String RECOMMEND_PUB_TEMPLATE = "recommend_pub";

  // 成果全文推荐
  final String PUB_FULLTEXT_RECMD = "pubFullTextRecmd";
  final Integer PUB_FULLTEXT_RECMD_CODE = 196;
  final String PUB_FULLTEXT_RECMD_TEMPLATE = "pub_FullText_Recmd";

  // 可以发送邮件
  final Integer CAN_SEND = 1;
  // 不可以发送邮件
  final Integer NOT_CAN_SEND = 0;

  final Integer ONE = 1;
  final Integer ZERO = 0;

  // 发送成功
  final Integer SUCCESS = 1;
  // 无内容
  final Integer NO_CONTENT = 2;
  // 发送失败
  final Integer FAIL = -1;

  // mail_promote_status表状态位控制值
  final Integer PROMOTE_STATUS_1 = 0002;
  final Integer PROMOTE_STATUS_2 = 0003;

  // 每周好友动态用 start
  // 判断是否处理了成果
  final String ENTER_PUB = "enterPub";
  // 判断是否处理了个人首页
  final String ENTER_PAGE = "enterPage";
  // 判断是否处理了研究领域
  final String ENTER_RA = "enterRa";
  // 每周好友动态用 end

  // 邮件设置url
  String MAIL_SET_URL = "/scmwebsns/user/setting/getMailTypeList";

  // 邮件待发送状态（一个用户可能有多封邮件发送，用来控制用户每天只能发送一封相同模板邮件 ）
  final Integer WAIT_SEND_STATUS = 3;
  // 邮件可以发送状态
  final Integer CAN_SEND_STATUS = 0;

  // 将待发送状态改为可以发送状态任务
  final String EMAI_STATUS_MANAGER = "emailStatusManager";
  final Integer EMAIL_STATUS_MANAGER_CODE = 34;

  String BASE_URL = "https://www.scholarmate.com/resscmwebsns/images_v5/";
  String NO_FULLTEXT_IMG = "images2016/file_img.jpg";
}
