package com.smate.core.base.utils.constant.wechat;

/**
 * 微信公共常量.
 * 
 * @author xys
 *
 */
public class WeChatConstant {

  /**
   * 微信加密签名.
   */
  public static final String SIGNATURE = "signature";
  /**
   * 时间戳.
   */
  public static final String TIMESTAMP = "timestamp";
  /**
   * 随机数.
   */
  public static final String NONCE = "nonce";
  /**
   * 随机字符串.
   */
  public static final String ECHOSTR = "echostr";
  public static final String ACCESS_TOKEN_CACHE_NAME = "accessTokenCacheName";
  public static final String ACCESS_TOKEN_CACHE_KEY = "accessTokenCacheKey";
  public static final String REQ_URL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";

  /**
   * access token key.
   */
  public static final String ACCESS_TOKEN_KEY = "access_token";
  public static final String EXPIRES_IN_KEY = "expires_in";

  /**
   * js-api ticket
   */
  public static final String WX_JSAPI_TICKET_CACHE_NAME = "wsJsapiTicketCacheName";
  public static final String WX_JSAPI_TICKET_CACHE_EKY = "wsJsapiTicketCacheKey";

  /**
   * access token刷新间隔时间.
   */
  public static final int ACCESS_TOKEN_REFRESH_INTERVAL = 6900;
  /**
   * access token缓存有效期. WSN_accessToken经常过一段时间后调用接口时返回信息说失效了，所以缩短缓存时间
   * 注意从微信那边获取access_token的接口一天最多调用2000次，切记
   */
  public static final int ACCESS_TOKEN_CACHE_EXPIRES = 300;
  /**
   * access token获取缓存间隔时间.
   */
  public static final int ACCESS_TOKEN_GETCACHE_INTERVAL = 5;
  /**
   * request method key.
   */
  public static final String REQ_METHOD_KEY = "reqMethod";
  /**
   * request method:GET.
   */
  public static final String REQ_METHOD_GET = "GET";
  /**
   * request method:POST.
   */
  public static final String REQ_METHOD_POST = "POST";
  /**
   * 消息来源key.
   */
  public static final String MSG_SOURCE_KEY = "msg_source_key";
  /**
   * 消息来源：wechat.
   */
  public static final String MSG_SOURCE_WECHAT = "msg_source_wechat";
  /**
   * 消息来源：smate.
   */
  public static final String MSG_SOURCE_SMATE = "msg_source_smate";
  /**
   * 模板消息 key.
   */
  public static final String TEMPLATE_MSG_KEY = "template_msg_key";
  public static final String ERRCODE_KEY = "errcode";
  public static final String ERRMSG_KEY = "errmsg";
  public static final int ERRCODE_0 = 0;
  public static final String ERRMSG_OK = "ok";
  public static final String ERRMSG_JOB_SUCCESS = "send job submission success";
  /**
   * 日志类型:绑定
   */
  public static final String LOG_TYPE_BINDING = "1";
  /**
   * 日志类型:解绑
   */
  public static final String LOG_TYPE_UNBINDING = "2";
  /**
   * 日志状态:成功
   */
  public static final int LOG_STATUS_SUCCESS = 1;
  /**
   * 日志状态:失败
   */
  public static final int LOG_STATUS_FAIL = -1;

  /**
   * action:我的项目.
   */
  public static final String ACTION_MYPRJ = "/prjweb/wechat/findprjs";
  /**
   * action:我的成果.
   */
  public static final String ACTION_MYPUB = "/pub/querylist/psn";
  /**
   * action:资助计划.
   */
  public static final String ACTION_FUNDINGSCHEME = "/prjweb/wechat/findfunds";
  /**
   * action:论文认领.
   */
  public static final String ACTION_PAPERCONFIRMATION = "/pub/confirmpublist?toBack=centerMsg";

  public static final String APPID_DEV = "wxad7da95e8513880e";

  public static final String SECRET_DEV = "ef49278d44642cdfb1da8b5e9e3bdf65";

  public static final String APPID_ALPHA = "wx181ad9807e2fc97d";

  public static final String SECRET_ALPHA = "cbe580b79d2052141456528e86667210";

  public static final String APPID_TEST = "wx260b81ecc4d9b8fd";

  public static final String SECRET_TEST = "40e3a61edc6cb53601002c03efd32be5";

  public static final String APPID_UAT = "wx9f97ed51c193ca1a";

  public static final String SECRET_UAT = "83f7b595b5f31062fafdfb5d1eb30b48";

  public static final String APPID_RUN = "wx470ee6855bdd417a";

  public static final String SECRET_RUN = "593e306f38ee8595bf9e18acf1981abe";

}
