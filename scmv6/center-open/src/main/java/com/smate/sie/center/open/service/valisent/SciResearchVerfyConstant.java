package com.smate.sie.center.open.service.valisent;

/**
 * 科研验证常量
 * 
 * @author hd
 *
 */
public class SciResearchVerfyConstant {

  public static Integer VERIFY_TYPE_PSN = 1; // 人员验证
  public static Integer VERIFY_TYPE_ORG = 2; // 单位验证
  public static Integer VERIFY_TYPE_PUB = 3; // 项目成果验证
  public static Integer VERIFY_TYPE_PSNPUB = 4; // 人员成果验证

  public static Integer ALREADY_ACCEPT = 1; // 已经受理
  public static Integer PARAME_ERROR = 2;// 参数错误

  public static final String KEY_TYPE_PRP = "1";
  public static final String KEY_TYPE_PROGRESS = "2";
  public static final String KEY_TYPE_CONCLUDE = "3";

  public static final String BASIC_EMPTY = "科研验证，具体参数basic_info不能为空";
  public static final String PSN_EMPTY = "科研验证，具体参数persons不能为空";
  public static final String PUB_EMPTY = "科研验证，具体参数publications不能为空";
  public static final String ORG_EMPTY = "科研验证，具体参数organizations不能为空";
  public static final String KEY_CODE_EMPTY = "科研验证，具体参数key_code不能为空";
  public static final String KEY_TYPE_EMPTY = "科研验证，具体参数key_type不能为空";
  public static final String GRANT_ORG_EMPTY = "科研验证，具体参数grant_org不能为空";
  public static final String DUP_INCORECT = "科研验证，查重数据有多条";

  public static final String SUCESS = "受理成功";

  public static final String PUB_TYPE_VALUE1 = "期刊论文";
  public static final String PUB_TYPE_VALUE2 = "会议论文";



}
