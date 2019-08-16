package com.smate.web.psn.service.setting;



/**
 * 记住密码常量类
 * 
 * @author tsz
 * 
 */
public interface RememberPwdContants {
  public static Long pwdData = 30 * 24 * 60 * 60L; // cookie保存时间
  public static String LOGINFO = "sala"; // 自动登录信息
}
