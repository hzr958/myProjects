package com.smate.sie.core.base.utils.consts.validate;

import java.util.HashMap;
import java.util.Map;

/**
 * 科研认证后台任务常量
 * 
 * @author ztg
 *
 */
public class SieKpiVdConstant {

  // 调用接口类型
  public static final int VERIFY_TYPE_PSN = 1; // 人员验证
  public static final int VERIFY_TYPE_ORG = 2; // 单位验证
  public static final int VERIFY_TYPE_PUB = 3; // 项目成果验证
  public static final int VERIFY_TYPE_PSNPUB = 4; // 人员成果验证

  // 加密是取配置公钥用
  public static String SNS_TOKEN = "00000000";// 个人版
  public static String SIE_TOKEN = "11111111";// 机构版
  public static String COLUD_TOKEN = "22222222";// 云计算


  // 接口调用状态
  public static final int REQ_SUCCESS = 1; // 已调用
  public static final int REQ_ERROR = 2; // 调用异常或调用失败， 或者已经调用， status=error


  // 接口返回结果中会用到的字段：
  public static final String RESULT_RESULT = "result";
  public static final String RESULT_STATUS = "status";
  public static final String RESULT_TYPE = "type";
  public static final String RESULT_ITEMSTATUS = "itemStatus";
  public static final String RESULT_ITEMMSG = "itemMsg";
  public static final String RESULT_CORRELATION_DATA = "correlationData";

  // 四个接口目前已经统一了返回值itemStatus
  public static final int RESULT_VALIDATE_PASS = 1;// 认证结果， 通过
  public static final int RESULT_VALIDATE_DOUBT = 2;// 认证结果，存疑（个人版接口是不匹配）
  public static final int REUSLT_VALIDATE_UNCERTAIN = 3;// 认证结果， 不确定 UNPASS


  // 接口返回结果中的status字段取值情况
  public static final String RESULT_STATUS＿SUCCESS = "success";// 请求接口验证，可验证
  public static final String RESULT_STATUS＿ERROR = "error";// 接口请求参数合法性校验不通过


  // 接口请求数据相关字段
  public static final String MAP_DATA = "data"; // 服务参数
  public static final String MAP_TOKEN = "token";
  public static final String MAP_OPENID = "openid";
  public static final String ENCODE_CONTENT = "content";
  public static final String MAP_DATA_PUBINFOLIST = "pubInfoList";


  // 个人验证接口返回数据type
  public static final int NAME_MOBILE_TYPE = 1; // 姓名＋手机验证
  public static final int NAME_EMAIL_TYPE = 2; // 姓名＋邮箱验证
  public static final int NAME_CERT_TYPE = 3; // 姓名＋证件验证
  // 如果还有其他type，后续加上


  // 个人认证接口返回itemStatus值增加4
  public static final int PSN_PARAMS_DEFECT = 4;// 验证数据合法性不通过，比如某参数缺失

  // 单位认证接口返回itemStatus值增加3
  public static final int ORG_PARAMS_DEFECT = 3;// 验证数据合法性不通过，比如某参数缺失



  // 已调用接口，验证情况分类, kpi_validate_detail.status 存值
  public static final int ALL_VALIDATE_PASS = 1;// 一条数据中所有需验证字段验证结果是， 通过，都是 VALIDATE_PASS，则1， 验证正确
  public static final int ALL_VALIDATE_EXIT_DOUBT = 2;// 一条数据中，不存在VALIDATE_UNPASS， 存在VALIDATE_DOUBT， 则3， 验证存疑
  public static final int ALL_VALIDATE_EXIT_UNCERTAIN = 3;// 一条数据中如果存在一个验证结果是VALIDATE_UNPASS， 不通过， 则2， 验证不通过 UNPASS
  public static final int REQ_PARAM_ILLEGAL = 4;// 接口请求参数合法性校验不通过


  public static final String PSN_VD_PHONENO_CODE = "irisv-101"; // phoneNo不能为空
  public static final String PSN_VD_NAME_CODE = "irisv-102"; // name不能为空
  public static final String PSN_VD_EMAIL_CODE = "irisv-103"; // email不能空


  public static Map<Integer, Object> INTERFACE_TYPE_MAP = SieKpiVdConstant.setInterfaceTypeDesc();

  public static Map<Integer, Object> setInterfaceTypeDesc() {
    Map<Integer, Object> map = new HashMap<Integer, Object>();
    map.put(SieKpiVdConstant.VERIFY_TYPE_PSN, "人员验证接口");
    map.put(SieKpiVdConstant.VERIFY_TYPE_ORG, "单位验证接口");
    map.put(SieKpiVdConstant.VERIFY_TYPE_PUB, "项目成果验证接口");
    map.put(SieKpiVdConstant.VERIFY_TYPE_PSNPUB, "人员成果验证接口");
    return map;
  }

  /**
   * 返回一个map， 键值存的个人，单位接口返回的响应码
   * 
   * @return
   */
  public static Map<String, String> getResponseCodeMap() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("irisv-101", "scm-1101");
    map.put("irisv-102", "scm-1102");
    map.put("irisv-103", "scm-1103");
    map.put("irisv-201", "scm-2100");
    map.put("irisv-202", "scm-2101");
    return map;
  }



  // "scm-2063 title为空";
  // "scm-2053 title不能超过2000个字符";
  // "scm-2054 authorNames不能超过1000个字符";
  // "scm-2064 authorNames为空
  //
  // "scm-2059" doi为空
  // "scm-2060" journalName为空
  // "scm-2061" publishYear为空
  // "scm-2062" fundingInfo为空
  // scm-2052 title不能为空
  // scm-2057 authorNames不能为空
  /**
   * 返回一个map, 键值存储的是 项目成果相应码对应的校验字段
   */
  public static Map<String, String> getPubResponseCodeMap() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("scm-2063", "title");
    map.put("scm-2053", "title");
    map.put("scm-2052", "title");
    map.put("scm-2054", "authorNames");
    map.put("scm-2064", "authorNames");
    map.put("scm-2057", "authorNames");
    map.put("scm-2059", "doi");
    map.put("scm-2060", "journalName");
    map.put("scm-2061", "publishYear");
    map.put("scm-2062", "fundingInfo");
    return map;
  }


  // "scm-2052 title不能为空";
  // "scm-2053 title不能超过2000个字符";
  // "scm-2054 authorNames不能超过1000个字符";
  // "scm-2057 authorNames不能为空"
  /**
   * 返回一个map, 键值存储的是人员成果响应码对应的校验字段
   */
  public static Map<String, String> getPsnPubResponseCodeMap() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("scm-2052", "title");
    map.put("scm-2053", "title");
    map.put("scm-2054", "authorNames");
    map.put("scm-2057", "authorNames");
    return map;
  }

}
