package com.smate.center.open.constant.wechat;

/**
 * 消息接收常量.
 * 
 * @author xys.
 *
 */
public class MsgReceivingConstant {

  /**
   * 开发者微信号.
   */
  public static final String TO_USER_NAME = "ToUserName";
  /**
   * 发送方帐号（一个OpenID）.
   */
  public static final String FROM_USER_NAME = "FromUserName";

  // -------------------------- 消息类型 begin ------------------------------
  /**
   * 消息类型key.
   */
  public static final String MSG_TYPE_KEY = "MsgType";
  /**
   * 事件.
   */
  public static final String MSG_TYPE_EVENT = "event";
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
   * 小视频消息.
   */
  public static final String MSG_TYPE_SHORTVIDEO = "shortvideo";
  /**
   * 地理位置消息.
   */
  public static final String MSG_TYPE_LOCATION = "location";
  /**
   * 链接消息.
   */
  public static final String MSG_TYPE_LINK = "link";
  // -------------------------- 消息类型 end ------------------------------

  // ------------------------------- 事件类型 begin -----------------------
  /**
   * 事件类型key.
   */
  public static final String EVENT_TYPE_KEY = "Event";
  /**
   * 订阅.
   */
  public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
  /**
   * 取消订阅.
   */
  public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
  /**
   * 扫描带参数二维码事件(用户已关注时的事件).
   */
  public static final String EVENT_TYPE_SCAN = "SCAN";
  /**
   * 上报地理位置事件.
   */
  public static final String EVENT_TYPE_LOCATION = "LOCATION";
  /**
   * 点击菜单拉取消息时的事件.
   */
  public static final String EVENT_TYPE_CLICK = "CLICK";
  /**
   * 点击菜单跳转链接时的事件.
   */
  public static final String EVENT_TYPE_VIEW = "VIEW";
  // ------------------------------- 事件类型 end -----------------------

  // --------------------------- 事件KEY值 begin -----------------------
  /**
   * 事件KEY值key.
   */
  public static final String EVENT_KEY_KEY = "EventKey";
  /**
   * 我的项目.
   */
  public static final String EVENT_KEY_MY_PRJ = "my_prj_0_0";
  /**
   * 我的成果.
   */
  public static final String EVENT_KEY_MY_PUB = "my_pub_0_1";
  /**
   * 资助计划.
   */
  public static final String EVENT_KEY_FUNDING_SCHEME = "funding_scheme_1_0";
  /**
   * 论文认领.
   */
  public static final String EVENT_KEY_PAPER_CONFIRMATION = "paper_confirmation_1_1";
  // --------------------------- 事件KEY值 end -----------------------
}
