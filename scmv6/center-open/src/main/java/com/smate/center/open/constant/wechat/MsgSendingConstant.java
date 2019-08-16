package com.smate.center.open.constant.wechat;

/**
 * 消息发送常量.
 * 
 * @author xys
 *
 */
public class MsgSendingConstant {

  /**
   * 普通用户openid.
   */
  public static final String TO_USER = "touser";
  /**
   * 消息类型.
   */
  public static final String MSG_TYPE = "msgtype";
  /**
   * request url key.
   */
  public static final String REQ_URL_KEY = "reqUrl";
  /**
   * request url:custom.
   */
  public static final String REQ_URL_COSTOM = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
  /**
   * request url:mass sendall.
   */
  public static final String REQ_URL_MASS_SENDALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
  /**
   * request url:mass send.
   */
  public static final String REQ_URL_MASS_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/send";
  /**
   * 待发送消息key.
   */
  public static final String MSG_TOBESENT_KEY = "msgToBeSent";

  // -------------------------- 消息类型 begin ------------------------------
  /**
   * 文本消息.
   */
  public static final String MSG_TYPE_TEXT = "text";
  /**
   * 图片消息.
   */
  public static final String MSG_TYPE_IMAGE = "image";
  /**
   * 语音消息.
   */
  public static final String MSG_TYPE_VOICE = "voice";
  /**
   * 视频消息.
   */
  public static final String MSG_TYPE_VIDEO = "video";
  /**
   * 音乐消息.
   */
  public static final String MSG_TYPE_MUSIC = "music";
  /**
   * 图文消息.
   */
  public static final String MSG_TYPE_NEWS = "news";
  /**
   * 卡券.
   */
  public static final String MSG_TYPE_WXCARD = "wxcard";

  /**
   * 图文消息(群发).
   */
  public static final String MSG_TYPE_MPNEWS = "mpnews";
  /**
   * 视频消息(群发).
   */
  public static final String MSG_TYPE_MPVIDEO = "mpvideo";
  // -------------------------- 消息类型 end ------------------------------

  /**
   * bind url key.
   */
  public static final String BIND_URL_KEY = "bindUrl";
  /**
   * 文本消息内容.
   */
  public static final String CONTENT = "content";
  /**
   * 绑定成功消息内容.
   */
  public static final String BINDING_SUCCESS_CONTENT = "binding_success_content";
  /**
   * 群发消息Filter key.
   */
  public static final String FILTER_KEY = "filter_key";
  /**
   * 是否根据分组进行群发 key.
   */
  public static final String IS_TO_GROUP_KEY = "is_to_group_key";
  /**
   * 是否向全部用户发送 key.
   */
  public static final String IS_TO_ALL_KEY = "is_to_all_key";
  /**
   * 用户分组id key.
   */
  public static final String GROUP_ID_KEY = "group_id_key";
  /**
   * OpenID列表 key.
   */
  public static final String LIST_TOUSER_KEY = "list_touser_key";
  /**
   * 是否检查绑定 key.
   */
  public static final String IS_CHECK_BINDING_KEY = "is_check_binding_key";
}
