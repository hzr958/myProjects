package com.smate.core.base.utils.constant.security;

/**
 * 安全信息常量（登录，权限验证...）
 * 
 * @author tsz
 *
 */
public class SecurityConstants {
  /**
   * 关联的缓存
   */
  public static final String UNION_URL_CACHE = "unionUrlCache"; // url缓存
  public static final String AUTO_LOGIN_PARAMETER_NAME = "AID"; // 自动登录 参数名字
  public static final String AUTO_LOGIN_PARAMETER_NAME_HOST = "HOST"; // 自动登录
                                                                      // 参数名字

  public static final String AUTO_LOGIN_URL = "AUTO_LOGIN_URL"; // 自动登录URL

  /*
   * 自动登录缓存信息
   */
  public static final String AUTO_LOGIN_INFO_CACHE = "AUTO_LOGIN_OAUTH";

  /*
   * user-details 缓存信息
   */
  public static final String USER_DETAILS_INFO_CACHE = "USER_DETAILS";
  /*
   * 连接重定向 时需要的 sessionid 参数名字
   */
  public static final String URL_REDIRECT_SESSION_ID_PARAMETER = "SID";

  /*
   * cookie 中sessionid 变量名字
   */
  public static final String COOKIE_SESSION_ID_PARAMETER = "JSESSIONID";

  /*
   * 登录验证码
   */
  public static final String OAUTH_VALIDATE_CODE = "OAUTH_VALIDATE_CODE";

  /*
   * 是否是从oauth系统登录后或共享权限
   */
  public static final String FROM_OAUTH = "FROM_OAUTH";

  /*
   * 是否已在oauth这边登录过
   */
  public static final String OAUTH_LOGIN = "OAUTH_LOGIN";

  /*
   * 记住登录过期时间-----1年
   */
  public static final int REMEMBER_ME_TIME = 12 * 60 * 60 * 24 * 30;
  /*
   * 记住登录过期时间----测试用，正式代码禁用
   */
  public static final int TEST_REMEMBER_ME_TIME = 2 * 60 * 60;

  // 缓存的sessionId
  public static final String CACHE_SESSIONID = "CACHE_SESSIONID";

  // 用户权限信息缓存时间-----1个月
  public static final int USER_DETAILS_CACHE_TIME = 30 * 24 * 60 * 60;

  // 系统标志在cookie中名称
  public static final String SYS_COOKIE_NAME = "SYS";

}
