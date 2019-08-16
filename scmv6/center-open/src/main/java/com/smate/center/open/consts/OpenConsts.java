package com.smate.center.open.consts;

/**
 * open系统常量类.
 *
 * @author tsz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public class OpenConsts {

  public static final int OPEN_LENGTH = 8;
  public static final int TOKEN_LENGTH = 8;
  public static final int TOKEN_MAXLENGTH = 16;

  // openid创建方式
  public static final int OPENID_CREATE_TYPE_0 = 0; // 通过openid验证页面生成
  public static final int OPENID_CREATE_TYPE_1 = 1; // 通过guid验证 系统自动生成
  public static final int OPENID_CREATE_TYPE_2 = 2; // 通过人员注册 或人员绑定自动生成
  public static final int OPENID_CREATE_TYPE_3 = 3; // 微信 绑定生成
  public static final int OPENID_CREATE_TYPE_4 = 4; // 互联互通帐号关联
  public static final int OPENID_CREATE_TYPE_5 = 5; // 后台任务自动生成
  public static final int OPENID_CREATE_TYPE_6 = 6; // 处理人员合并后 的历史关联记录生成
  /*
   * 任务执行状态
   */
  public static final int TASK_STATUS_WAIT = 0;
  public static final int TASK_STATUS_REPEAT = 1;
  public static final int TASK_STATUS_FAIL = 99;

  /**
   * 关联的缓存
   */
  public static final String UNION_URL_CACHE = "unionUrlCache"; // url缓存
  public static final String UNION_URL_PARAM_CACHE = "unionUrlParamCache"; // url参数缓存

  public static final String UNION_REFRESH_PUB_CACHE = "unionRefreshPubCache"; // 刷新成果
  // 缓存
  public static final String UNION_REFRESH_GROUP_PUB_CACHE = "unionRefreshGroupPubCache"; // 刷新群组成果
  // 缓存
  public static final String UNION_REFRESH_PSN_CACHE = "unionRefreshPsnCache"; // 刷新人员
  // 缓存
  public static final String UNION_GROUP_CACHE = "unionGroupCache"; // 互联互通
  // Group缓存

  public static final String UNION_GROUP_CODE_CACHE = "unionGroupCodeOpenIdCache"; // 互联互通
  // GroupCode
  // OpenId缓存

  public static final String UNION_URL = "unionUrl";
  public static final String UNION_RESULT = "unionResult";
  public static final String MAP_GROUP_XML = "groupXml"; // 服务参数
  public static final String MAP_GROUP_DATA = "groupData"; // 服务参数
  public static final String MAP_DATA_THIRD_PRJ = "thirdPrjInfo"; // 第三方项目信息
  public static final String MAP_GROUP_CODE = "groupCode"; // 互联互通 群组id加密的code

  public static final String LOGIN_CACHE = "LonginErrorNum";

  public static final String VALIDATE_CODE = "validateCode";

  public static final String MAP_TYPE = "type"; // 服务参数
  public static final String MAP_OPENID = "openid"; // 服务参数
  public static final String MAP_TOKEN = "token"; // 服务参数
  public static final String MAP_DATA = "data"; // 服务参数
  public static final String MAP_USER_IP = "userIP"; // 服务参数

  public static final String MAP_PSNID = "psnId"; // 服务参数
  public static final String MAP_NSFCRPTID = "nsfcRptId"; // 服务参数
  public static final String MAP_SERVICE_TYPE = "serviceType"; // 服务参数
  public static final String MAP_DATA_TYPE = "dataType"; // 服务参数
  public static final String MAP_SYNCXML = "syncXml"; // 服务参数
  public static final String PSN_DATA = "psnData"; // 服务参数
  public static final String MAP_PARAM_XML = "paramXml"; // 服务参数
  public static final String MAP_GUID = "guid"; // 服务参数
  public static final String MAP_ROLID = "rolId"; // 服务参数
  public static final String MAP_DATAPATAMETMAP = "dataPatametMap"; // 服务参数
  public static final String MAP_CODE = "code";

  public static final String CREATE_TYPE_KEY = "createType"; // 关联类型 4：互联互通
  public static final String CREATE_TYPE_VAL_4 = "4"; // 关联类型 4：互联互通

  public static final String MAP_EXCLUDEDPUBIDS = "excludedPubIDS"; // 服务参数
  public static final String MAP_PSNGUIDID = "psnGuidID"; // 服务参数
  public static final String MAP_PUBTYPES = "pubTypes"; // 服务参数
  public static final String MAP_KEYWORDS = "keywords"; // 服务参数
  public static final Object MAP_PUBIDS = "pubIDS"; // 服务参数
  public static final Object MAP_HISTORY_PUBLICATION = "historyPublication"; // 服务参数

  public static final String MAP_DATA_PERSON_REGISTER = "personRegister";
  public static final String MAP_DATA_PERSON_SYSCH = "personSysch"; // 人员同步
  public static final String MAP_DATA_FROM_SYS = "fromSys";// 来源系统
  public static final String MAP_UNBUND_SERVICE = "5h6jjk9b";// 来源系统

  public static final String MAP_AUTO_LOGIN_URL_CODE = "autoLoginUrlCode"; // 自动登录url编号
  public static final String MAP_AUTO_LOGIN_GROUP_CODE = "autoLoginGroupCode"; // 自动登录群组编号
  public static final String MAP_AUTO_LOGIN_Create_GROUP_CODE = "autoLoginCreateGroupCode"; // 自动登录群组编号

  public static final String RESULT_STATUS = "status"; // 返回数据key
  public static final String RESULT_STATUS_SUCCESS = "success"; // 返回数据key
  public static final String RESULT_STATUS_ERROR = "error"; // 返回数据key
  public static final String RESULT_MSG = "msg"; // 返回数据key
  public static final String RESULT_DATA = "data"; // 返回数据key
  public static final String RESULT_RESULT = "result"; // 返回数据key
  public static final String RESULT_TIME = "time";
  public static final String RESULT_DATALIST = "resultList";
  public static final String RESULT_SMATE = "smate";
  public static final String RESULT_NSFC_RESULT = "nsfcResult";
  public static final String RESULT_COUNT = "count";
  public static final String RESULT_FLAG = "flag";
  public static final String RESULT_GET_PUB_STATUS = "getPubStatus"; // 0 正常 ，
  // 1手动刷新限制，2自动刷新限制，
  // 3没成果，4，参数格式出错
  // sie系统 传递的参数
  public static final String MAP_JNAME = "jname"; // 期刊名
  public static final String MAP_ISSN = "issn";
  public static final String MAP_JNAME_FROM = "jnameFrom";// jname 的来源
  public static final String MAP_JID = "jid"; // Journal ID (system generated
  // key)

  public static final String SMATE_TOKEN = "00000000"; // smate系统token
  public static final String SIE_TOKEN = "11111111"; // smate系统token
  public static final Long SYSTEM_OPENID = 99999999L; // 系统级别的openId
  // 主要给不能确定到具体人的 数据交互

  // 可以 使用系统级别openid(99999999)的服务类别
  // 1.微信 群发消息 接收服务 2.获取微信消息交互授权码服务 ...(可自行添加)
  public static final String[] SYSTEM_OPENID_ENABLE_SERVICE =
      {"b7303c9f", "7c630c84", "xc410fnd", "5fc82b4c", "ae72a069", "3djd2x9s", "d9ff82cb", "b71c6408", "csg53dfk",
          "d485987e", "3c9b459c", "c461ac42", "qq908ufd", "u83nu2n0", "qh232qtj", "sxol209x", "xouj2zoa", "ncwpx93k",
          "unn22url", "oxui3en2", "sht22url", "msg77msg", "v663ttyy", "965eb72c", "cfm55pub", "pdwh5pub", "sh66info",
          "v5detail", "psn2solr", "psndsolr", "open22id", "kwsplit1", "kwsios01", "kwsios02", "kwsres02", "seaibkw3",
          "seapbkw4", "seap1bkw", "seap2bkw", "seap3bkw", "lhd007xs", "lhd008xs", "usn22pwd", "4kxiw2p0", "pdh22jid",
          "seacpsn1", "siedept1", "siedept3", "siedept4", "siedept5", "siesync1", "siesync2", "syncusn1", "syncpsn1",
          "obt28loe", "up28uton", "pdw83pkd", "tsl83pkd", "sieregi1", "obtuslgn", "siesync3", "gpin9reg", "yung90kk",
          "sieobtjy", "sief5eli", "sc97pd40", "sc74gg25", "che5gg25", "srv00pub", "fun2solr", "fundsolr", "nkwsrcmd",
          "hkwsrcmd", "seaibpkw", "seapbpkw", "sieptatn", "syncfsie", "5h6jjk9b", "lxj3219s", "jyh99kls", "siescirv",
          "siescget", "kpipayv1", "hguu65op", "fgg34oiu", "nju12lib", "sieyusfs", "sieopvda", "seap1rid"};

  /*
   * Open-项目接收与合并
   */
  public static final String RECEIVE_TYPE = "receive";// 接收方式
  public static final String RECEIVE_TYPE_RESTFUL = "restful";
  public static final String RECEIVE_TYPE_WEBSERV = "webservice";
  public static final String OBJECT_LIST = "objectList";// 数据集合
  public static final String PROJECT = "project";// 数据集合
  public static final String NOT_ANY_MORE_VALUE = "not any more value ^"; // 空
  public static final Integer NO_ANY_INT_VALUE = -9999999; // 空
  public static final Long NO_ANY_LONG_VALUE = -9999999l; // 空

  /**
   * 微信消息 参数 常量
   */
  public static final String WECHAT_DATA_FIRST = "first";// 微信 消息内容
  public static final String WECHAT_DATA_KEYWORD1 = "keyword1";// 微信 消息发送机构
  public static final String WECHAT_DATA_KEYWORD2 = "keyword2";// 微信 消息内容
  public static final String WECHAT_DATA_KEYWORD3 = "keyword3";// 微信 消息时间 描述
  public static final String WECHAT_DATA_REMARK = "remark";// 微信 消息时间 描述
  public static final String WECHAT_DATA_SMATE_TEMP_ID = "smateTempId";// 微信
  // 消息
  // 模板类别id
  public static final int WECHAT_MAX_LENGTH = 150; // insName+ content+
  // prjTime 的最大长度
  public static final int WECHAT_MIN_LENGTH = 20; // insName+ content+ prjTime
  // 的最小长度

  public static final String DYN_TOKEN = "dynamicToken"; // 动态token
  public static final String DYN_OPENID = "dynamicOpenid"; // 动态token
  public static final String DYN_TOKEN_TYPE = "dynamicTokenType"; // 动态token
  // 类型
  public static final String DYN_TOKEN_CACHE = "DYN_TOKEN_CACHE"; // 动态token
  // 缓存名

  public static final String DYN_OPENID_CACHE = "DYN_OPENID_CACHE"; // 动态token

  /**
   * 群组创建 常量
   */
  public static final String MAP_DATA_GROUP_PSN = "groupPsn";

  /**
   * 人员数据-协议
   */
  public static final String PSN_DATA_PROTOCOL = "protocol";
  /**
   * http协议
   */
  public static final String PROTOCOL_HTTP = "1";
  /**
   * https协议
   */
  public static final String PROTOCOL_HTTPS = "2";

  // 请求类型
  public static final String REAUEST_TYPE = "requestType";
  public static final String REAUEST_TYPE_1 = "1"; // ws
  public static final String REAUEST_TYPE_2 = "2"; // restful

  public static final String MAP_INS_ID = "insId"; // 单位id
  // 来源
  public static final String FROM_SIE = "SIE"; //
  public static final String FROM_IRIS = "IRIS"; //
  public static final String MAP_AVATARS = "avatars"; // 头像

}
