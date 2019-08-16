package com.smate.center.oauth.consts;

/**
 * oauth系统 常量类
 * 
 * @author tsz
 *
 */
public class OauthConsts {

  /*
   * 登录 错误次数
   */
  public static final String OAUTH_LOGIN_ERROR_NUM = "OAUTH_LOGIN_ERROR_NUM";

  public static final int TOKEN_MAXLENGTH = 16;
  public static final int LOGIN_TYPE_MOBILE_CODE = 15;// 手机验证码登录

  // ------------------微博登录 begin-------------------------------
  public static final String RESPONSE_TYPE = "code";// 授权类型，此值固定为“code”
  public static final String WEIBO_LOGIN_STATE = "weibotoscm";// 客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值
  public static final String WEIBO_LOGIN_CACHE = "weibo_login_cache";// 微博登录相关参数缓存名称
  public static final String WEIBO_LOGIN_PARAMS = "weiboLoginParams";

  // ------------------微博登录 end-------------------------------

}
