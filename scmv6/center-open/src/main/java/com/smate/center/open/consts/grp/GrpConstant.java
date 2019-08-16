package com.smate.center.open.consts.grp;

import java.util.Arrays;
import java.util.List;

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
  public final static String GRP_CONTROL_SPECIAL_NUMBER = "0";
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
   * 
   */
  public final static String GRP_BASEINFO_DEFAULT_AUATARS = "/resmod/smate-pc/img/logo_grpdefault.png";
  /**
   * 群组按类别logo地址
   */
  public final static String GRP_BASEINFO_DEFAULT_LOGO_PATH = "/resmod/images/group/default_logo";
  /**
   * 群组统计表创建默认的人员数量
   */
  public final static int GRP_STATISTICS_DEFAULT_MEMBERCOUNT = 1;
  /**
   * 项目群组类别
   */
  public final static int GRP_CATEGORY_PRJ = 11;
  public final static int GRP_CATEGORY_INTEREST = 12; //兴趣群组
  public final static List<Integer> GRP_CATEGOTY = Arrays.asList(10, 11, 12);
  // 群组信息----------end--------------
}
