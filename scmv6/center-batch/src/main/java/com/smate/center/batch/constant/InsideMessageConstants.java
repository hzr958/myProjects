package com.smate.center.batch.constant;

/**
 * 站内短信常量.
 * 
 * @author liqinghua
 * 
 */
public interface InsideMessageConstants {

  /** 对某人进行评价. */
  int MSG_TYPE_EVALUATION_OF = 1;
  /** 某人请求您帮助检索成果. */
  int MSG_TYPE_REQUEST_COL_PUB = 2;
  /** 来某人的好友推荐. */
  int MSG_TYPE_FRIEND_RECOMMEND = 3;
  /** 邀请某人为某群共享成果. */
  int MSG_TYPE_INVITE_ADDPUBFOR_GROUP = 11;
  /** 邀请某人为某群共享文件. */
  int MSG_TYPE_INVITE_ADDFILEFOR_GROUP = 12;
  /** 站内短信. */
  int MSG_TYPE_INSIDE_MESSAGE = 14;
  /** 分享群组. */
  int MSG_TYPE_SHARE_GROUP = 48;
  /** 分享简历. */
  int MSG_TYPE_SHARE_CV = 49;
  /** 成果全文附件查看请求. */
  int MSG_TYPE_REQUEST_VIEW_PUBFILE = 50;
  /** 分享基金推荐. */
  int MSG_TYPE_SHARE_FUND = 51;
  /** 分享期刊推荐. */
  int MSG_TYPE_SHARE_JOURNAL = 52;
  /** 分享领域推荐. */
  int MSG_TYPE_SHARE_AREA = 53;
  /** 祝贺新工作. */
  int MSG_TYPE_CONGRATULATION_JOB = 54;
  /** 回复全文请求申请. */
  int MSG_TYPE_REPLAY_FULLTEXT_REQUEST = 55;
  /** 热点领域. */
  int MSG_TYPE_SHARE_HOTWORDS = 56;

  /** 站内信模板. */
  int MSG_TEMPLATE_INSIDE_MESSAGE = 1;
  /** 站内信评价好友模板. */
  int MSG_TEMPLATE_INSIDE_EVA_FRIEND = 3;
  /** 站内信推荐好友模板. */
  int MSG_TEMPLATE_INSIDE_CMD_FRIEND = 5;
  /** 站内信分享简历模板. */
  int MSG_TEMPLATE_INSIDE_SHARE_CV = 7;
  /** 站内信祝贺新工作模板. */
  int MSG_TEMPLATE_INSIDE_CONGRATUTION_JOB = 9;
  /** 邀请某人为群组分享成果模板. */
  int MSG_TEMPLATE_INVITE_ADDPUBFOR_GROUP = 11;
  /** 邀请某人为群组分享文件模板. */
  int MSG_TEMPLATE_INVITE_ADDFILEFOR_GROUP = 13;

  /** 好友分享文件分享收件箱详情模板. */
  int MSG_TEMPLATE_SHARE_SHAREFILE = 15;
  /** 好友分享成果/文献分享收件箱详情模板. */
  int MSG_TEMPLATE_SHARE_SHAREPUB = 17;

}
