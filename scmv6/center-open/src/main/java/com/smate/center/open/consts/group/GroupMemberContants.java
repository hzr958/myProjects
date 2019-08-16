package com.smate.center.open.consts.group;

/**
 * 群组成员的相关常量.
 * 
 * @author mjg
 * 
 */
public class GroupMemberContants {

  // 加入群组的方式(对应GroupPsn类中的joinInGroupStyle属性值).
  public static final String JOININ_GROUP_STYLE_INVITE = "invited";
  public static final String JOININ_GROUP_STYLE_APPLY = "apply";
  // 加入群组的消息类型(对应MessageNoticeOutBox类中的msgType属性值).
  public static final Integer GROUP_MSG_TYPE_INVITE = 2;
  public static final Integer GROUP_MSG_TYPE_APPLY = 4;
  // 是否同意加入群组[2=需要普通成员确认,1=已确认,0=否,空=需要管理员确认]
  public static final String ACCEPT_BY_USER = "2";
  public static final String ACCEPT = "1";
  public static final String NOT_ACCEPT = "0";
  // 群组操作任务的任务类型(对应FriendGroupOperateTaskInfo类中的operation属性值).
  public static final Integer GROUP_OPERATE_TASK_TYPE = 3;
  // 删除状态
  public static final String GROUP_MEMBER_STATUS_DELLET = "99";
  // 正常状态
  public static final String GROUP_MEMBER_STATUS_NORMAL = "01";
  // 群组成员角色[1-创建人,2-管理员;3-普通成员]
  public static final String GROUP_ROLE_CREATOR = "1";
  public static final String GROUP_ROLE_ADMIN = "2";
  public static final String GROUP_ROLE_MEMBER = "3";
  // 公开类型[O=开放,H=半开放,P=保密]
  public static final String FULL_OPEN = "O";
  public static final String HALF_OPEN = "H";
  public static final String NOT_OPEN = "P";
}
