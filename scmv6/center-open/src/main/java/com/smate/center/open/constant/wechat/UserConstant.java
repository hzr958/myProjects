package com.smate.center.open.constant.wechat;

/**
 * 用户常量.
 * 
 * @author xys
 *
 */
public class UserConstant {

  /**
   * 关注该公众账号的总用户数.
   */
  public static final String TOTAL = "total";
  /**
   * 拉取的OPENID个数.
   */
  public static final String COUNT = "count";
  /**
   * OPENID列表.
   */
  public static final String DATA = "data";
  /**
   * 拉取列表的最后一个用户的OPENID.
   */
  public static final String NEXT_OPENID = "next_openid";
  public static final String OPENID = "openid";
  public static final String REQ_URL_USER_LIST = "https://api.weixin.qq.com/cgi-bin/user/get";
}
