package com.smate.center.batch.model.psn;

/**
 * 存放CONST_MAIL_TYPE表中各种邮件类型的MAIL_TYPE_ID
 * 
 * @author YPH
 * 
 */
public class ConstMailTypeDictionary {
  /**
   * 忽略本次查询，直接返回没有关闭（false)
   */
  public static final Long IGNORE = -1l;
  /**
   * 邀请加为好友时发送邮件
   */
  public static final Long REQUEST_ADD_FRIEND = 1l;
  /**
   * 邀请加入群组时发送邮件
   */
  public static final Long REQUEST_JOIN_GROUP = 2l;
  /**
   * 添加好友成功时发送邮件
   */
  public static final Long ADD_FRIEND_SUCCESS = 3l;
  /**
   * 加入群组成功时发送邮件
   */
  public static final Long JOIN_GROUP_SUCCESS = 4l;
  /**
   * 评价好友时发送邮件
   */
  public static final Long EVALUATION_FRIEND = 5l;
  /**
   * 请求更新引用次数时发送邮件
   */
  public static final Long REQUEST_UPDATE_COUNT = 6l;
  /**
   * 推荐成果/文献/文件时发送邮件
   */
  public static final Long SUGGEST = 7l;
  /**
   * 有新消息时发送邮件
   */
  public static final Long EXIST_NEW_MESSAGE = 8l;

  /**
   * 群组新增邮件
   */
  public static final Long GROUP_ADD_NEW_FILE = 9l;

  /**
   * 市场推广
   */
  public static final Long MARKET_POPULAR = 10l;

  /**
   * 邀请群组成员共享成果
   */
  public static final Long GROUP_SHARE_PUB = 11l;

  /**
   * 邀请群组成员共享文件
   */
  public static final Long GROUP_SHARE_FILE = 12l;

  /**
   * 请求加入群组时发送邮件_SCM-1506.
   */
  public static final Long REQUEST_TO_JOIN_GROUP = 13l;

  /**
   * 更新群组公告时发送邮件_SCM-1751.
   */
  public static final Long GROUP_UPDATE_ANNOUNCE = 14l;
}
