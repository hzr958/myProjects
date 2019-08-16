package com.smate.core.base.utils.constant;

/**
 * 消息中心需要用到的常量
 * 
 * @author ZZX
 *
 */
public class MsgConstants {
  /**
   * 消息类型消息类型： 0=系统消息、1=请求添加好友消息、2=成果认领、3=全文认领、4=成果推荐、5=好友推荐、
   * 6=基金推荐、7=站内信、8=请求加入群组消息、9=邀请加入群组消息、10=群组动向 , 11=请求全文消息
   */
  public static String MSG_TYPE = "msgType";

  public static String MSG_TYPE_ADD_FRIEND_REQUEST = "1"; // 请求添加好友消息
  public static String MSG_TYPE_PUB_COMFIRM = "2"; // 成果认领
  public static String MSG_TYPE_PUB_FULLTEXT_COMFIRM = "3"; // 全文认领
  public static String MSG_TYPE_PUB_RCMD = "4"; // 成果推荐
  public static String MSG_TYPE_FRIEND__RCMD = "5"; // 好友推荐
  public static String MSG_TYPE_FUND__RCMD = "6"; // 基金推荐
  public static String MSG_TYPE_SMATE_INSIDE_LETTER = "7"; // 站内信
  public static String MSG_TYPE_ADD_GRP_REQUEST = "8"; // 请求加入群组消息
  public static String MSG_TYPE_ADD_GRP_INVITE = "9"; // 邀请加入群组消息
  public static String MSG_TYPE_GRP_DYNAMIC = "10"; // 群组动向
  public static String MSG_TYPE_PUBFULLTEXT_REQUEST = "11"; // 请求全文消息

  public static String MSG_SENDER_ID = "senderId"; // 发送者ID
  public static String MSG_RECEIVER_IDS = "receiverIds"; // 接收者IDs-集合
  public static String MSG_CONTENT_ID = "contentId"; // 消息内容ID

  public static String MSG_CONTENT = "content"; // 消息内容
  public static String MSG_CONTENT_NEWEST = "contentNewest"; // 最新消息内容

  public static String MSG_GRP_ID = "grpId"; // 群组ID
  public static String MSG_PUB_ID = "pubId"; // 成果ID
  public static String MSG_PUB_ID_LIST = "pubIdList"; // 成果ID列表
  public static String MSG_FILE_ID = "fileId"; // 文件ID
  public static String MSG_FILE_IDS = "fileIds"; // 文件ID
  public static String MSG_FRIEND_ID = "friendId"; // 好友Id
  public static String MSG_RCMD_FRIEND_ID_LIST = "rcmdFriendIdList"; // 推荐好友Id集合，逗号隔离
  public static String MSG_REQUEST_FRIEND_ID = "requestFriendId"; // 请求好友Id
  public static String MSG_RELATION_ID = "msgRelationId"; // MSG_RELATION主键ID
  public static String MSG_SMATE_INSIDE_LETTER_TYPE = "smateInsideLetterType"; // 站内信，具体类型，text=文本，file=文件，pub=成果

  public static String MSG_SMATE_INSIDE_LETTER_TYPE_TEXT = "text"; // 站内信，具体类型，text=文本
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_FILE = "file"; // 站内信，具体类型file=文件
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_PUB = "pub"; // 站内信，具体类型，pub=成果
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_PRJ = "prj"; // 站内信，具体类型，prj=项目
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_PDWH_PUB = "pdwhpub"; // 站内信，具体类型，pdwhPub=基准库成果
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_FUND = "fund"; // 站内信，具体类型，fund=基金full
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_FULLTEXT = "fulltext"; // 站内信，具体类型，text=全文消息
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_AGENCY = "agency"; // 站内信，具体类型，agency=资助机构
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_NEWS = "news"; // 站内信，具体类型，NEWS = 新闻
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_PSN = "psnUrl"; // 站内信，具体类型，psnUrl = 个人主页
  public static String MSG_SMATE_INSIDE_LETTER_TYPE_INS = "ins"; // 站内信，具体类型，ins = 机构
  public static String MSG_BELONG_PERSON = "belongPerson"; // 站内信，file,pub是否属于个人默认为true

  public static String MSG_GRP_MSG_TYPE = "grpMsgType"; // 群组消息类型
  public static String MSG_GRP_MSG_ADD_FILE = "addFile"; // 添加文件
  public static String MSG_GRP_MSG_ADD_COURSE = "addCourse"; // 添加课件
  public static String MSG_GRP_MSG_ADD_WORK = "addWork"; // 添加作业
  public static String MSG_GRP_MSG_ADD_PUB = "addPub"; // 添加成果
  public static String MSG_GRP_FILE_ID = "grpFileId"; // 群组文件Id
  public static String MSG_GRP_PUB_ID = "grpPubId"; // 群组成果id
  public static String MSG_REQUEST_GRP_ID = "requestGrpId"; // 请求的群组Id
  public static String MSG_RCMD_GRP_ID = "rcmdGrpId"; // 推荐的群组Id
  public static String MSG_GRP_NAME = "grpName"; // 群组名称

  // 文件信息
  public static String MSG_FILE_NAME = "fileName";
  public static String MSG_FILE_TYPE = "fileType";
  public static String MSG_FILE_PATH = "filePath";
  public static String MSG_ARCHIVE_FILE_ID = "archiveFileId";
  // 项目信息
  public static String MSG_DES3_PRJ_ID = "des3PrjId";
  public static String MSG_PRJ_TITLE_ZH = "prjTitleZh";
  public static String MSG_PRJ_TITLE_EN = "prjTitleEn";
  public static String MSG_PRJ_AUTHOR_NAME_ZH = "prjAuthorNameZh";
  public static String MSG_PRJ_AUTHOR_NAME_EN = "prjAuthorNameEn";
  public static String MSG_PRJ_BRIEF_DESC_ZH = "prjBriefZh";
  public static String MSG_PRJ_BRIEF_DESC_EN = "prjBriefEn";
  public static String MSG_PRJ_URL = "prjUrl";
  public static String MSG_PRJ_IMG = "prjImg";

  // 机构信息
  public static String MSG_DES3_INS_ID = "des3InsId";
  public static String MSG_INS_HOME_URL = "insHomeUrl";
  // 个人主页信息
  public static String MSG_DES3_PSN_ID = "des3PsnId";
  public static String MSG_PSN_PROFILE_URL = "psnProfileUrl";
  // 新闻信息
  public static String MSG_DES3_NEWS_ID = "des3NewsId";
  public static String MSG_NEWS_TITLE = "newsTitle";
  public static String MSG_NEWS_BRIEF = "newsBrief";
  public static String MSG_NEWS_URL = "newsUrl";
  public static String MSG_NEWS_IMG = "newsImg";
  // 成果信息
  public static String MSG_PUB_TITLE_ZH = "pubTitleZh";
  public static String MSG_PUB_TITLE_EN = "pubTitleEn";
  public static String MSG_PUB_AUTHOR_NAME = "pubAuthorName";
  public static String MSG_PUB_BRIEF_DESC_ZH = "pubBriefZh";
  public static String MSG_PUB_BRIEF_DESC_EN = "pubBriefEn";
  public static String MSG_ROLPUB_ID = "rolpubId"; // 推荐成果id
  public static String MSG_PUB_DBID = "dbid"; // 成果的dbid

  public static String MSG_RES_COUNT = "resCount"; // 资源数量

  // 基金信息
  public static String MSG_FUND_ZH_TITLE = "fundZhTitle";
  public static String MSG_FUND_EN_TITLE = "fundEnTitle";
  public static String MSG_FUND_AGENCY_NAME = "fundAgencyName";
  public static String MSG_FUND_SCIENCE_AREA = "fundScienceArea";
  public static String MSG_FUND_APPLY_TIME = "fundApplyTime";
  public static String MSG_FUND_LOGO_URL = "fundLogoUrl";
  public static String MSG_FUND_SHOW_TITLE = "showTitle";
  public static String MSG_FUND_DESC_ZH = "zhFundDesc";
  public static String MSG_FUND_DESC_EN = "enFundDesc";
  public static String MSG_FUND_DESC_ZH_BR = "zhFundDescBr";
  public static String MSG_FUND_DESC_EN_BR = "enFundDescBr";

  // 短地址
  public static String MSG_PUB_SHORT_URL = "pubShortUrl";
  public static String MSG_GRP_PUB_SHORT_URL = "grpPubShortUrl";
  public static String MSG_GRP_SHORT_URL = "grpShortUrl";

  // 全文信息
  public static String MSG_HAS_PUB_FULLTEXT = "hasPubFulltext";
  public static String MSG_PUB_FULLTEXT_ID = "pubFulltextId";
  public static String MSG_PUB_FULLTEXT_EXT = "pubFulltextExt"; // 后缀名
  public static String MSG_PUB_FULLTEXT_IMAGE_PATH = "pubFulltextImagePath"; // 图片则有路径
  public static String MSG_PUB_FULLTEXT_PERMIT = "pubFulltextPermit"; // 图片则有路径

  // 资助机构
  public static String MSG_AGENCY_ZH_TITLE = "agencyZhTitle"; // 中文标题
  public static String MSG_AGENCY_EN_TITLE = "agencyEnTitle"; // 英文标题
  public static String MSG_AGENCY_DESC_ZH = "zhAgencyDesc";
  public static String MSG_AGENCY_DESC_EN = "enAgencyDesc";
  public static String MSG_AGENCY_DESC_ZH_BR = "zhAgencyDescBr";
  public static String MSG_AGENCY_DESC_EN_BR = "enAgencyDescBr";
  public static String MSG_AGENCY_LOGO_URL = "agencyLogoUrl";

  public static String MSG_OPEN_ID = "openId"; // OPEN_ID
  public static String MSG_TOKEN = "token"; // TOKEN
  public static String MSG_FUND_ID = "fundId"; // 基金ID
  public static String MSG_FULLTEXT_ID = "fulltextId"; // 全文Id
  public static String MSG_FUND_INFO = "fundInfo"; // 基金信息

  public static String MSG_AGENCY_ID = "agencyId"; // 资助机构ID
  public static String MSG_DES3_AGENCY_ID = "des3AgencyId"; // 资助机构ID
  public static String MSG_AGENCY_INFO = "agencyInfo"; // 资助机构信息

  public static String MSG_NEWS_ID = "newsId"; // 新闻ID

  public static String MSG_NOT_PERMISSION_PSNIDS = "notPermissionPsnIds"; // 没有权限的人员id
                                                                          // 集合列表

}
