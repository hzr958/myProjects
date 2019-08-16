package com.smate.center.oauth.model.consts;

public class OpenConsts {

  // openid创建方式
  public static final int OPENID_CREATE_TYPE_0 = 0; // 通过openid验证页面生成
  public static final int OPENID_CREATE_TYPE_1 = 1; // 通过guid验证 系统自动生成
  public static final int OPENID_CREATE_TYPE_2 = 2; // 通过人员注册 或人员绑定自动生成
  public static final int OPENID_CREATE_TYPE_3 = 3; // 微信 绑定生成
  public static final int OPENID_CREATE_TYPE_4 = 4; // 互联互通帐号关联
  public static final int OPENID_CREATE_TYPE_5 = 5; // 后台任务自动生成
  public static final int OPENID_CREATE_TYPE_6 = 6; // 处理人员合并后 的历史关联记录生成
  public static final int OPENID_CREATE_TYPE_7 = 7; // 记住登录时获取人员openId时没找到时创建

  public static final String SMATE_TOKEN = "00000000"; // smate系统token
  public static final String DYN_OPENID_CACHE = "DYN_OPENID_CACHE"; // 动态token
}
