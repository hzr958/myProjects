package com.smate.core.base.utils.constant.dyn;

/**
 * 动态模板常量
 * 
 * @author AiJiangBin
 *
 */
public class DynTemplateConstant {
  /**
   * 快速分享的动态
   */
  public final static String QUICK_SHARE_DYN = "QUICK_SHARE_DYN";
  /**
   * 动态模板的类型
   */
  public final static String DYN_TYPE = "DYN_TYPE";

  // 发表人，相当于，动态的首次生产者
  /**
   * 发表人Id 加密
   */
  public final static String PRODUCER_PSN_ID = "PRODUCER_PSN_ID";
  /**
   * 发表人Id 加密
   */
  public final static String DES3_PRODUCER_PSN_ID = "DES3_PRODUCER_PSN_ID";
  /**
   * 动态拥有人id
   */
  public final static String DYN_OWNER_DES3_ID = "DYN_OWNER_DES3_ID";
  /**
   * 发表人的头像
   */
  public final static String PERSON_AVATARS = "PERSON_AVATARS";
  /**
   * 发表人的姓名中文
   */
  public final static String PERSON_NAME_ZH = "PERSON_NAME_ZH";

  /**
   * 发表人的姓名英文
   * 
   */
  public final static String PERSON_NAME_EN = "PERSON_NAME_EN";
  /**
   * 发表人的机构单位
   */
  public final static String PERSON_INSINFO_ZH = "PERSON_INSINFO_ZH";
  public final static String PERSON_INSINFO_EN = "PERSON_INSINFO_EN";
  /**
   * 发表人的职称
   */
  public final static String PERSON_POSITION = "PERSON_POSITION";
  /**
   * 发表人 发布时间
   */
  public final static String PUBLISH_TIME = "PUBLISH_TIME";
  /**
   * 发表人的动态内容
   */
  public final static String USER_ADD_CONTENT = "USER_ADD_CONTENT";
  /**
   * 发表人的动态Id
   */
  public final static String DYN_ID = "DYN_ID";
  /**
   * 父动态Id
   */
  public final static String PARENT_DYN_ID = "PARENT_DYN_ID";
  public final static String DES3_PARENT_DYN_ID = "DES3_PARENT_DYN_ID";
  /**
   * 发表人的动态des3_Id
   */
  public final static String DES3_DYN_ID = "DES3_DYN_ID";
  /**
   * 发表人的 资源 Id
   */
  public final static String RES_ID = "RES_ID";
  /**
   * 发表人的 资源DES3_RES_ID
   */
  public final static String DES3_RES_ID = "DES3_RES_ID";
  /**
   * 发表人的是否成果DES3_PUB_ID
   */
  public final static String HAS_DES3_PUB_ID = "HAS_DES3_PUB_ID";
  public final static String RES_OWNER_DES3ID = "RES_OWNER_DES3ID";

  public final static String PUB_ID = "PUB_ID";
  public final static String DES3_PUB_ID = "DES3_PUB_ID";
  /**
   * 资源类型 1：成果 ;
   */
  public final static String RES_TYPE = "RES_TYPE";

  /**
   * 赞数
   */
  public final static String AWARD_COUNT = "AWARD_COUNT";

  /**
   * 分享数
   */
  public final static String SHARE_COUNT = "SHARE_COUNT";
  /**
   * 评论数
   */
  public final static String COMMENT_COUNT = "COMMENT_COUNT";

  /**
   * link标题中文
   */
  public final static String LINK_TITLE_ZH = "LINK_TITLE_ZH";

  /**
   * link标题英文
   */
  public final static String LINK_TITLE_EN = "LINK_TITLE_EN";
  /**
   * link地址
   */
  public final static String LINK_URL = "LINK_URL";
  /**
   * link图片
   */
  public final static String LINK_IMAGE = "LINK_IMAGE";

  /**
   * 全文图片
   */
  public final static String FULLTEXT_IMAGE = "FULLTEXT_IMAGE";
  /**
   * 全文id
   */
  public final static String FULLTEXT_DES3FILEID = "FULLTEXT_DES3FILEID";
  public final static String FULLTEXT_DES3PUBID = "FULLTEXT_DES3PUBID";

  /**
   * 成果 描述中文
   */
  public final static String PUB_DESCR_ZH = "PUB_DESCR_ZH";

  /**
   * 成果 描述英文
   */
  public final static String PUB_DESCR_EN = "PUB_DESCR_EN";

  /**
   * 成果作者
   */
  public final static String PUB_AUTHORS = "PUB_AUTHORS";

  /**
   * 成果发表时间
   */
  public final static String PUB_PUBLISHYEAR = "PUB_PUBLISHYEAR";

  /**
   * 操作（包括，点赞，分享评论）人的Id
   */
  public final static String DES3_PSN_A_ID = "DES3_PSN_A_ID";
  /**
   * 操作（包括，点赞，分享评论）人的姓名
   */
  public final static String OPERATOR_NAME = "OPERATOR_NAME";

  /**
   * 操作（包括，点赞，分享评论，添加成果）类型
   */
  public final static String OPERATOR_TYPE_ZH = "OPERATOR_TYPE_ZH";

  public final static String OPERATOR_TYPE_EN = "OPERATOR_TYPE_EN";

  public final static String[] OPERATOR_VAL = {"评论了", "赞了", "分享了"};

  public final static String[] OPERATOR_VAL_EN = {"commented", "liked", "shared"};

  public final static String[] OPERATOR_VAL_NEW = {"评论了", "赞了", "分享了"};

  public final static String[] OPERATOR_VAL_EN_NEW =
      {"commented", "liked", "shared"};

  public final static String[] FUND_OPERATOR_VAL_NEW = {"赞了", "分享了"};

  public final static String[] FUND_OPERATOR_VAL_EN_NEW = {"liked", "shared"};

  public final static String[] PRJ_OPERATOR_VAL_NEW = {"评论了", "赞了", "分享了"};

  public final static String[] PRJ_OPERATOR_VAL_EN_NEW = {"commented", "liked", "shared"};

  public final static String[] AGENCY_OPERATOR_VAL_NEW = {"赞了", "分享了", "关注了"};

  public final static String[] AGENCY_OPERATOR_VAL_EN_NEW =
      {"liked", "shared", "concerned"};

  /**
   * 操作（包括，点赞，分享评论）人的头像
   */
  public final static String OPERATOR_AVATARS = "OPERATOR_AVATARS";

  /**
   * 操作（包括，点赞，分享评论）人的评论内容
   */
  public final static String OPERATOR_COMMENT = "OPERATOR_COMMENT";

  /**
   * 操作（包括，点赞，分享评论）人的操作时间
   */
  public final static String OPERAROR_TIME = "OPERAROR_TIME";

  /**
   * 操作的行为1：评论了，2：赞了 ，3：分享了
   */
  public final static String OPERATE_STATUS = "OPERATE_STATUS";

  /**
   * 
   * 分享别人的动态的人的姓名
   */
  public final static String SHARER_NAME = "SHARER_NAME";

  /**
   * 分享别人的动态的人的头像
   */
  public final static String SHARER_AVATARS = "SHARER_AVATARS";

  /**
   * 分享别人的动态的人的评论
   */
  public final static String SHARER_COMMENT = "SHARER_COMMENT";

  /**
   * 分享别人的动态的人的机构
   */
  public final static String SHARER_INSNAME = "SHARER_INSNAME";

  /**
   * 分享别人的动态的人的职称
   */
  public final static String SHARER_POSITION = "SHARER_POSITION";

  /**
   * 分享别人的动态的人的时间
   */
  public final static String SHARER_TIME = "SHARER_TIME";

  /**
   * 被赞，分享，评论的动态id
   */
  public final static String PARENT_DYNID = "PARENT_DYNID";

  /**
   * PC中文原始模版
   */
  public final static String ORIGINAL_TEMPLATE_ZH = "ORIGINAL_TEMPLATE_ZH";
  /**
   * PC英文原始模版
   */
  public final static String ORIGINAL_TEMPLATE_EN = "ORIGINAL_TEMPLATE_EN";
  /**
   * 移动端中文原始模版
   */
  public final static String MOBILE_ORIGINAL_TEMPLATE_ZH = "MOBILE_ORIGINAL_TEMPLATE_ZH";
  /**
   * 移动端英文原始模版
   */
  public final static String MOBILE_ORIGINAL_TEMPLATE_EN = "MOBILE_ORIGINAL_TEMPLATE_EN";
  /**
   * A类动态类型
   */
  public final static String ATEMP = "ATEMP";
  /**
   * B1类动态类型
   */
  public final static String B1TEMP = "B1TEMP";
  /**
   * B2动态类型
   */
  public final static String B2TEMP = "B2TEMP";
  /**
   * B3动态类型
   */
  public final static String B3TEMP = "B3TEMP";
  /**
   * C动态类型
   */
  public final static String CTEMP = "CTEMP";
  /**
   * d动态类型
   */
  public final static String DTEMP = "DTEMP";

  /**
   * 英文动态HTML
   */
  public final static String DYN_HTML_EN = "DYN_HTML_EN";

  /**
   * 中文动态HTML
   */
  public final static String DYN_HTML_ZH = "DYN_HTML_ZH";

  public final static String DYN_TEMPLATE = "DYN_TEMPLATE";
  /**
   * PC端中文模板名称
   */
  public final static String DYN_TEMPLATE_ZH = "DYN_TEMPLATE_ZH";
  /**
   * PC端英文模板名称
   */
  public final static String DYN_TEMPLATE_EN = "DYN_TEMPLATE_EN";
  /**
   * 移动端中文模板名称
   */
  public final static String MOBILE_DYN_TEMPLATE_ZH = "MOBILE_DYN_TEMPLATE_ZH";
  /**
   * 移动端英文模板名称
   */
  public final static String MOBILE_DYN_TEMPLATE_EN = "MOBILE_DYN_TEMPLATE_EN";

  public final static Integer AWARD_TYPE = 1;
  /**
   * 查询评论的默认最大结果
   */
  public final static int DEF_MAX_RESULT = 10;
  /**
   * 成果动态类型
   */
  public final static String RES_TYPE_PUB = "1";
  /**
   * 评论查询的条数
   */
  public final static int RESULT_COUNT = 10;

  public final static String OPERATOR_TYPE1 = "";

  public final static String PUB_INDEX_URL = "PUB_INDEX_URL";

  public final static String PDWH_URL = "PDWH_URL";

  public final static String DB_ID = "DB_ID";

  public final static String FUND_TITLE_ZH = "FUND_TITLE_ZH"; // 基金标题

  public final static String FUND_DESC_ZH = "FUND_DESC_ZH"; // 基金描述

  public final static String FUND_TITLE_EN = "FUND_TITLE_EN"; // 基金标题

  public final static String FUND_DESC_EN = "FUND_DESC_EN"; // 基金描述

  public final static String FUND_LOGO_URL = "FUND_LOGO_URL"; // 基金logo url

  public final static String FUND_ID = "FUND_ID"; // 基金ID

  public final static String ENCODE_FUND_ID = "ENCODE_FUND_ID"; // 加密的基金ID

}
