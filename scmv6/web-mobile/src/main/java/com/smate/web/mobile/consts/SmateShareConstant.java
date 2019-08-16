package com.smate.web.mobile.consts;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.smate.core.base.dyn.consts.DynamicConstants;

/**
 * 分享功能常量
 * 
 * @author wsn
 * @date May 24, 2019
 */
public class SmateShareConstant {

  // 资源类型
  public static final String SHARE_SNS_PUB_RES_TYPE = "pub"; // 个人库成果
  public static final String SHARE_PDWH_PUB_RES_TYPE = "pdwhpub"; // 基准库成果
  public static final String SHARE_PRJ_RES_TYPE = "prj"; // 项目
  public static final String SHARE_PSN_FILE_RES_TYPE = "psnfile"; // 个人文件
  public static final String SHARE_GROUP_FILE_RES_TYPE = "grpfile"; // 群组文件
  public static final String SHARE_FUND_RES_TYPE = "fund"; // 基金
  public static final String SHARE_AGENCY_RES_TYPE = "agency"; // 资助机构
  public static final String SHARE_NEWS_RES_TYPE = "news"; // 新闻

  // 分享到的地方
  public static final String SHARE_TO_DYNAMIC = "dyn"; // 首页动态
  public static final String SHARE_TO_FRIEND = "friend"; // 联系人
  public static final String SHARE_TO_GROUP = "group"; // 群组

  // 分享相关接口url
  public static final String SHARE_TO_GROUP_URL = "/dyndata/grpdyn/publish"; // 分享资源给群组接口的url
  public static final String SHARE_TO_DYN_URL = "/dyndata/share/todyn"; // 分享资源到首页动态接口的url
  public static final String SHARE_PSN_FILE_TO_FRIENDS_URL = "/psndata/sharefile/tofriend"; // 分享个人文件给联系人url
  public static final String SHARE_FUND_TO_FRIENDS_URL = "/psndata/sharefund/tofriend"; // 分享基金给联系人url
  public static final String SHARE_AGENCY_TO_FRIENDS_URL = "/prjdata/shareagency/tofriend"; // 分享资助机构给联系人url
  public static final String SHARE_PRJ_TO_FRIENDS_URL = "/dyndata/shareprj/tofriend"; // 分享项目给联系人url
  public static final String SHARE_PSN_PUB_TO_FRIENDS_URL = "/dyndata/sharepub/tofriend"; // 分享个人成果给联系人url
  public static final String SHARE_PDWH_PUB_TO_FRIENDS_URL = "/dyndata/sharepub/tofriend"; // 分享基准库成果给联系人url
  public static final String SHARE_UPDATE_STATISTICS_URL = "/dyndata/share/updatesharestatic"; // 更新分享统计数url
  public static final String SHARE_GRP_LIST = "/grpdata/share/grplist"; // 可选的分享到的群组列表
  public static final String SHARE_GRP_RECORD = "/dyndata/grpdyn/share"; // 记录群组动态分享操作

  // 分享资源类型为空
  public static final String SHARE_ERROR_CODE_NULL_RESTYPE = "1";
  public static final String SHARE_ERROR_MSG_NULL_RESTYPE = "the resType is null";
  // 分享资源ID为空
  public static final String SHARE_ERROR_CODE_NULL_RESID = "2";
  public static final String SHARE_ERROR_MSG_NULL_RESID = "the des3ResId is null";
  // 调用接口的域名为空
  public static final String SHARE_ERROR_CODE_NULL_DOMAINMOBILE = "3";
  public static final String SHARE_ERROR_MSG_NULL_DOMAINMOBILE = "the domainMobile is null";
  // 当前登录的人员ID为空
  public static final String SHARE_ERROR_CODE_NULL_CURRENTPSNID = "4";
  public static final String SHARE_ERROR_MSG_NULL_CURRENTPSNID = "the currentPsnId is null";
  // 分享给联系人的ID或email为空
  public static final String SHARE_ERROR_CODE_NULL_RECEIVERS = "5";
  public static final String SHARE_ERROR_MSG_NULL_RECEIVERS = "the receiver's ids or emails is null";
  // 分享到群组的群组id为空
  public static final String SHARE_ERROR_CODE_NULL_GROUPID = "6";
  public static final String SHARE_ERROR_MSG_NULL_GROUPID = "the des3GrpId is null";
  // 分享的资源json信息为空
  public static final String SHARE_ERROR_CODE_NULL_JSONINFO = "7";
  public static final String SHARE_ERROR_MSG_NULL_JSONINFO = "the resJsonInfo is null";
  // 接口返回数据异常
  public static final String SHARE_ERROR_CODE_INTERFACE_ERROR = "8";
  public static final String SHARE_ERROR_MSG_INTERFACE_ERROR = "interface returns error";
  // 发生异常
  public static final String SHARE_ERROR_CODE_EXCEPTION = "9";
  public static final String SHARE_ERROR_MSG_EXCEPTION = "has a exception";
  // 资源类型不对
  public static final String SHARE_ERROR_CODE_INCORRECT_RESTYPE = "10";
  public static final String SHARE_ERROR_MSG_INCORRECT_RESTYPE = "the resType is incorrect";
  // 操作类型为空
  public static final String SHARE_ERROR_CODE_NULL_OPERATETYPE = "11";
  public static final String SHARE_ERROR_MSG_NULL_OPERATETYPE = "the operateType is null";
  // 参数校验不通过
  public static final String SHARE_ERROR_CODE_CHECK_FALSE = "12";
  public static final String SHARE_ERROR_MSG_CHECK_FALSE = "the params check false";
  // 更新分享统计数失败
  public static final String SHARE_ERROR_CODE_UPDATE_STATISTICS_ERROR = "13";
  public static final String SHARE_ERROR_MSG_UPDATE_STATISTICS_ERROR = "update share statistics failed";
  // 群组资源所在群组ID为空
  public static final String SHARE_ERROR_CODE_NULL_GRP_RES_GRPID = "14";
  public static final String SHARE_ERROR_MSG_NULL_GRP_RES_GRPID = "the group resource's group id is null";
  // 分享资源后更新记录群组动态的分享信息异常
  public static final String SHARE_ERROR_CODE_GRP_DYN_SHARE_UPDATE = "15";
  public static final String SHARE_ERROR_MSG_GRP_DYN_SHARE_UPDATE = "update share group dynamic failed";

  // 资源类型Map 分享的类型1、2个人成果，4项目，11基金，22、24基准库成果，25资助机构
  public static final Map<String, Integer> RES_TYPE_MAP;
  static {
    Map<String, Integer> typeMap = new HashMap<String, Integer>();
    typeMap.put(SHARE_SNS_PUB_RES_TYPE, DynamicConstants.RES_TYPE_PUB);
    typeMap.put(SHARE_PDWH_PUB_RES_TYPE, DynamicConstants.RES_TYPE_PUB_PDWH);
    typeMap.put(SHARE_PRJ_RES_TYPE, DynamicConstants.RES_TYPE_PRJ);
    typeMap.put(SHARE_PSN_FILE_RES_TYPE, DynamicConstants.RES_TYPE_FILE);
    typeMap.put(SHARE_GROUP_FILE_RES_TYPE, DynamicConstants.RES_TYPE_GROUP_FILE);
    typeMap.put(SHARE_FUND_RES_TYPE, DynamicConstants.RES_TYPE_FUND);
    typeMap.put(SHARE_AGENCY_RES_TYPE, DynamicConstants.RES_TYPE_AGENCY);
    typeMap.put(SHARE_NEWS_RES_TYPE, DynamicConstants.RES_TYPE_NEWS);
    RES_TYPE_MAP = Collections.unmodifiableMap(typeMap);
  }


  // 分享到的地方，值参考： SharePlatformEnum
  public static final Map<String, String> SHARE_TO_PLATFORM_MAP;
  static {
    Map<String, String> platformMap = new HashMap<String, String>();
    platformMap.put(SHARE_TO_DYNAMIC, "1");
    platformMap.put(SHARE_TO_FRIEND, "2");
    platformMap.put(SHARE_TO_GROUP, "3");
    SHARE_TO_PLATFORM_MAP = Collections.unmodifiableMap(platformMap);
  }


  // 分享资源到群组对应的动态模板类型Map
  public static final Map<String, String> GROUP_TMEP_TYPE_MAP;
  static {
    Map<String, String> tempTypeMap = new HashMap<String, String>();
    tempTypeMap.put(SHARE_SNS_PUB_RES_TYPE, "GRP_SHAREPUB");
    tempTypeMap.put(SHARE_PDWH_PUB_RES_TYPE, "GRP_SHAREPDWHPUB");
    tempTypeMap.put(SHARE_PRJ_RES_TYPE, "GRP_SHAREPRJ");
    tempTypeMap.put(SHARE_PSN_FILE_RES_TYPE, "GRP_SHAREFILE");
    tempTypeMap.put(SHARE_GROUP_FILE_RES_TYPE, "GRP_SHAREFILE");
    tempTypeMap.put(SHARE_FUND_RES_TYPE, "GRP_SHAREFUND");
    tempTypeMap.put(SHARE_AGENCY_RES_TYPE, "GRP_SHAREAGENCY");
    // tempTypeMap.put(SHARE_NEWS_RES_TYPE, DynamicConstants.RES_TYPE_NEWS);
    GROUP_TMEP_TYPE_MAP = Collections.unmodifiableMap(tempTypeMap);
  }


  // 获取项目信息
  public static final String FIND_SHARE_PRJ_INFO_URL = "/prjdata/project/getdetails";
  // 获取基准库或个人库成果信息
  public static final String FIND_SHARE_PUB_INFO_URL = "/data/pub/details/forshare";
  // 获取基金信息
  public static final String FIND_SHARE_FUND_INFO_URL = "/prjdata/share/fundinfo";
  // 获取资助机构的信息
  public static final String FIND_SHARE_AGENCY_INFO_URL = "/prjdata/aidinsdetail/forshare";
  // 获取新闻信息
  public static final String FIND_SHARE_NEWS_INFO_URL = "/dynweb/news/mobile/forshare";
  // 获取群组文件信息
  public static final String FIND_SHARE_GROUP_FILE_INFO_URL = "/grpdata/file/querygrpfiledetail";


  // 从哪个地方点击的分享
  public static final String FROM_GRP_DYN = "grp_dyn"; // 从群组动态点击的分享

}
