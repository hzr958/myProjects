package com.smate.web.psn.model.friend;

/**
 * 好友请求的常量
 *
 * @author wsn
 * @createTime 2017年7月25日 下午8:40:38
 *
 */
public class FriendReqConst {

  public static final Integer FRIEND_REQ_DEFAULT = 0; // 默认，未处理

  public static final Integer FRIEND_REQ_AGREE = 1; // 接受

  public static final Integer FRIEND_REQ_REFUSE = 2; // 忽略

  public static final Integer FRIEND_REQ_REPET = 3; // 因重发好友请求而作废的请求

  public static final Integer FRIEND_REQ_DEAL_ONE = 4; // 互相好友请求未操作的一方而作废的请求

  public static final Integer FRIEND_REQ_CANCEL = 5; // 取消好友请求而作废的请求

  public static final Integer FRIEND_REQ_DELET = -1; // 好友移除请求
}
