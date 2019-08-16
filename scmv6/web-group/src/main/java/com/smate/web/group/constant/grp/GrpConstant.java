package com.smate.web.group.constant.grp;

/**
 * 群组常量类
 * 
 * @author zzx
 *
 */
public class GrpConstant {
  // 群组成员----------start--------------
  /**
   * 群组拥有者
   */
  public final static int GRP_ROLE_OWNER = 1;
  /**
   * 群组管理员
   */
  public final static int GRP_ROLE_ADMIN = 2;
  /**
   * 群组成员
   */
  public final static int GRP_ROLE_MEMBER = 3;
  /**
   * 99=删除（被移除出群组）
   */
  public final static int GRP_DEL_MEMBER_TYPE_A = 99;
  /**
   * 98=删除（自动退出群组）
   */
  public final static int GRP_DEL_MEMBER_TYPE_B = 98;
  /**
   * 群组成员状态 ， [ 01 = 正常 ， 99删除] 默认值 01
   */
  public final static String GRP_MEMBER_STATUS_NORMAL = "01";

  // 群组成员----------end--------------
  // 群组信息----------start--------------
  /**
   * 群组模块权限默认状态[1=是 ， 0=否 ] 默认1
   */
  public final static String GRP_CONTROL_DEFAULT_NUMBER = "1";
  /**
   * 群组公开类型--O
   */
  public final static String GRP_BASEINFO_OPENTYPE_O = "O";
  /**
   * 群组公开类型--H
   */
  public final static String GRP_BASEINFO_OPENTYPE_H = "H";
  /**
   * 群组公开类型--P
   */
  public final static String GRP_BASEINFO_OPENTYPE_P = "P";
  /**
   * 群组状态 ， [ 01 = 正常 ， 99删除] 默认值 01
   */
  public final static String GRP_BASEINFO_STATUS_NORMAL = "01";
  /**
   * 群组状态 ， [ 01 = 正常 ， 99删除] 默认值 01
   */
  public final static String GRP_BASEINFO_STATUS_DEL = "99";
  /**
   * 群组默认头像
   */
  public final static String GRP_BASEINFO_DEFAULT_AUATARS = "/resscmwebsns/images_v5/50X50g.gif";

  /**
   * 群组按类别logo地址
   */
  public final static String GRP_BASEINFO_DEFAULT_LOGO_PATH = "/resmod/images/group/default_logo";

  /**
   * 群组统计表创建默认的人员数量
   */
  public final static int GRP_STATISTICS_DEFAULT_MEMBERCOUNT = 1;

  // 群组信息----------end--------------
  /**
   * 成果列表 统一url
   */
  public final static String PUB_LIST_URL = "/data/pub/query/list";
  /**
   * 群组成果统计数url
   */
  public final static String GRP_PUB_COUNT_URL = "/data/pub/query/grppubcount";
  /**
   * 个人成果统计数url
   */
  public final static String PSN_PUB_COUNT_URL = "/data/pub/query/snspubcount";

  public final static String DES3_PUB_ID = "des3PubId"; // 加密的成果id
}
