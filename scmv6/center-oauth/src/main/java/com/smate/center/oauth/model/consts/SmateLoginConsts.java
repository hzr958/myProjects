package com.smate.center.oauth.model.consts;

/**
 * 登录相关的常量
 *
 * @author wsn
 * @createTime 2017年6月7日 上午9:57:29
 *
 */
public class SmateLoginConsts {

  public static String SNS_REMEMBER_ME = "SNSRememberMe"; // 获取AID时的AID类型
  public static int SNS_OPENID_TYPE_SIX = 6; // 表示openId是科研之友本身业务需要而去生成的
  public static String SNS_DEFAULT_TOKEN = "00000000"; // 科研之友默认的token8个0
  public static Long SNS_DEFAULT_OPENID = 99999999L; // 科研之友用的系统级别openId，没有openId但要调用open系统的时候用
  public static String WEIBO_REJECT_LOGIN_CODE = "21330"; // 微博登录，在登录微博账号页面拒绝授权返回码

}
