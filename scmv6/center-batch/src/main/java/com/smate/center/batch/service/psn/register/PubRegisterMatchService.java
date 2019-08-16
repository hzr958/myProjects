package com.smate.center.batch.service.psn.register;

import java.util.Map;

/**
 * 新注册用户成果匹配的装饰器模式接口.
 * 
 * @author mjg
 * 
 */
public interface PubRegisterMatchService {

  final static String PARAM_PSN_ID = "psnId";
  final static String PARAM_EMAIL = "email";
  final static String PARAM_ZHNAME = "zhName";
  final static String PARAM_ENNAME = "enName";
  final static String PARAM_FIRST_NAME = "firstName";
  final static String PARAM_LAST_NAME = "lastName";
  final static String PARAM_OTHER_NAME = "otherName";
  final static String PARAM_INS_ID = "insId";// 单位ID.
  final static String PARAM_INS_NAME = "insName";// 单位名称.
  final static String PARAM_START_YEAR = "startYear";
  final static String PARAM_END_YEAR = "endYear";

  /**
   * 新注册用户匹配成果的逻辑.
   * 
   * @param params
   */
  void registerUserMatchPub(Map<String, Object> params);
}
