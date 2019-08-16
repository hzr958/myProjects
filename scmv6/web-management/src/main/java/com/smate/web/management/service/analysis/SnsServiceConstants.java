package com.smate.web.management.service.analysis;

/**
 * SNS Service层 缓存 key
 * 
 * @author lvxingzhi
 *
 */
public class SnsServiceConstants {
  /*
   * cache分类名称
   */
  public static final String SNS_TYPE_PSN = "sns_type_psn";

  /*
   * 缓存key
   */
  // 个人推荐基金列表缓存
  public static final String SNS_KEY_PSN_FUND_COMMENTS = "sns_key_psn_fund_comments";
  // 可能感兴趣的论文缓存
  public static final String SNS_KEY_PSN_REF_PUB_COMMEND = "sns_key_psn_ref_pub_commend";
  // 可能感兴趣的论文缓存--搜索
  public static final String SNS_KEY_PSN_REF_PUB_SEARCH = "sns_key_psn_ref_pub_search";
  // 论文投稿建议--默认
  public static final String SNS_KEY_PSN_SUITABLE_JOURNAL = "sns_key_psn_suitable_journal";
  // 论文投稿建议--合作者
  public static final String SNS_KEY_PSN_COOPERATOR = "sns_key_psn_cooperator";
  // 项目申请--相关文献
  public static final String SNS_KEY_PSN_REF_COMMEND = "sns_key_psn_ref_commend";
  // 项目申请--基金机会
  public static final String SNS_KEY_PSN_FUND_CHANCE = "sns_key_psn_fund_chance";
  // 动态--可能认识的人
  public static final String SNS_DYNAMIC_PSN_KNOW = "sns_dynamic_psn_know";
  // 群组--可能感兴趣的群组
  public static final String SNS_GROUP_INTEREST = "sns_group_interest";
}
