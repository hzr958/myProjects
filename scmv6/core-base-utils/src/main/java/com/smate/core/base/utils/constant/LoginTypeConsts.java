package com.smate.core.base.utils.constant;

/**
 * 登录类型常量
 * 
 * @author wsn
 *
 */
public class LoginTypeConsts {

  public static final Integer AUTO_LOGIN = 10; // 通过拼接AID到链接上自动登录进系统

  public static final Integer BUILD_SECURITY_BY_AID = 11; // 没有权限的时候通过直接取cookie里面的AID，构建权限进入系统

  public static final Integer ISIS_LOGIN = 1; // ISIS登录

  public static final Integer CITE_LOGIN = 2; // CITE登录

  public static final Integer SHARE = 3; // SHARE方式 ？？

  public static final Integer STIAS_LOGIN = 4; // STIAS方式 ？？

  public static final Integer INSTEAD_LOGIN = 5; // INSTEAD方式

  public static final Integer GX_LOGIN = 6; // 广西系统登录的（猜的）

  public static final Integer HN_LOGIN = 7; // 海南系统登录的（猜的）

  public static final Integer NORMAL_LOGIN = 8; // 平常的输入账号密码方式登录的

  public static final Integer MINI_LOGIN = 9; // mini页面登录的（填写几位数的，对应的是egrant系统的insId,对应scholar2.egrant_rol表）
}
