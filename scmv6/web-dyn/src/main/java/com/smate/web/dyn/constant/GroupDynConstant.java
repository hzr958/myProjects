package com.smate.web.dyn.constant;

/**
 * 群组动态 常量
 * 
 * @author tsz
 *
 */
public class GroupDynConstant {

  // 接口参数 常量 start
  public final static String MAP_DATA_PSNID = "psnId"; // 参数人员id
  public final static String MAP_DATA_GROUPID = "groupId"; // 参数群组id
  public final static String MAP_DATA_RESTYPE = "resType"; // 参数 资源类型
  public final static String MAP_DATA_RESID = "resId"; // 参数资源id
  public final static String MAP_DATA_DYNCONTENT = "dynContent"; // 参数 动态内容
  public final static String MAP_DATA_TEMPTYPE = "tempType"; // 参数 动态模版类型
  public final static String MAP_DATA_PUBSIMPLEMAP = "pubSimpleMap";// 成果动态参数

  public final static String MAP_DATA_DATABASETYPE = "databaseType";// 区分个人库和成果库
  public final static String MAP_DATA_DBID = "dbId";// 区分个人库和成果库
  public final static String GROUP_DYN_RESTYPE_PUB = "pub"; // 参数 资源类型 成果类
  public final static String GROUP_DYN_RESTYPE_FILE = "file";// 参数资源类型 文件类

  public final static String GROUP_DYN_TEMPTYPE_PUBLISHDYN = "PUBLISHDYN"; // 参数模版类型
                                                                           // 发布新动态模版
  public final static String GROUP_DYN_TEMPTYPE_ADDFILE = "ADDFILE"; // 添加文件模版
  public final static String GROUP_DYN_TEMPTYPE_ADDPUB = "ADDPUB"; // 添加成果模版
  public final static String GROUP_DYN_TEMPTYPE_SHARE = "SHARE"; // 分享到群组模版
  public final static String GROUP_DYN_TEMPTYPE_SHAREPUB = "SHAREPUB"; // 分享成果到群组
                                                                       // 模版
  // 接口参数 常量 end

  // 模版构建参数 常量start
  public final static String TEMPLATE_DATA_DYN_ID = "GROUP_DYN_ID";
  public final static String TEMPLATE_DATA_DYN_TYPE = "DYN_TYPE";
  public final static String TEMPLATE_DATA_PSN_WORK_INFO = "PSN_WORK_INFO";
  public final static String TEMPLATE_DATA_AUTHOR_AVATAR = "AUTHOR_AVATAR";
  public final static String TEMPLATE_DATA_DYN_CONTENT = "GROUP_DYN_CONTENT";
  public final static String TEMPLATE_DATA_DES3_PSN_ID = "DES3_PSN_ID";

  public final static String TEMPLATE_DATA_EN_AUTHOR_NAME = "EN_AUTHOR_NAME";
  public final static String TEMPLATE_DATA_ZH_AUTHOR_NAME = "ZH_AUTHOR_NAME";

  public final static String TEMPLATE_DATA_RES_TYPE = "RES_TYPE";
  public final static String TEMPLATE_DATA_RES_ID = "RES_ID";
  public final static String TEMPLATE_DATA_DES3_RES_ID = "DES3_RES_ID";
  public final static String TEMPLATE_DATA_RES_NAME = "RES_NAME";
  public final static String TEMPLATE_DATA_RES_DESC = "RES_DESC";
  public final static String TEMPLATE_DATA_RES_FILE_IMAGE = "FILE_IMAGE";
  public final static String TEMPLATE_DATA_RES_FILE_TYPE = "FILE_TYPE";

  public final static String TEMPLATE_DATA_RES_AUTHOR_NAMES = "AUTHOR_NAMES";

  public final static String TEMPLATE_DATA_RES_EN_NAME = "EN_RES_NAME";
  public final static String TEMPLATE_DATA_RES_ZH_NAME = "ZH_RES_NAME";
  public final static String TEMPLATE_DATA_RES_EN_DESC = "EN_RES_DESC";
  public final static String TEMPLATE_DATA_RES_ZH_DESC = "ZH_RES_DESC";
  public final static String TEMPLATE_DATA_RES_FULL_TEXT_ID = "FULL_TEXT_FIELD";
  public final static String TEMPLATE_DATA_RES_FULL_TEXT_IMAGE = "FULL_TEXT_IMAGE";
  public final static String TEMPLATE_DATA_PUB_INDEX_URL = "PUB_INDEX_URL";
  public final static String TEMPLATE_DATA_PDWH_URL = "PDWH_URL";// 基准库

  public final static String TEMPLATE_DATA_PDWH_DBID = "DB_ID";// 网站Id

  public final static String TEMPLATE_DATA_FUND_LOGO_URL = "FUND_LOGO_URL"; // 基金图片
  public final static String TEMPLATE_DATA_AGENCY_LOGO_URL = "AGENCY_LOGO_URL"; // 资助机构图片
  public final static String TEMPLATE_DATA_NEWS_LOGO_URL = "NEWS_LOGO_URL"; // 新闻图片

  public final static String TEMPLATE_DATA_NOTENCODE_DYN_ID = "GROUP_NOTENCODE_DYN_ID"; // 未加密的动态ID

  // 模版构建参数 常量end

}
