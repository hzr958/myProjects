package com.smate.core.web.sns.consts;

import java.util.ArrayList;
import java.util.List;

/**
 * 屏蔽菜单的列表.
 * 
 * @author maojianguo
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class ExcludeMenuConst {
  // 科研之友系统中存在但在成果在线系统中被隐藏的菜单的ID.
  public static final List<Long> nsfcExcludeMenuIds = new ArrayList<Long>();
  // 成果在线系统中存在但在科研之友系统中被隐藏的菜单的ID.
  public static final List<Long> scmExcludeMenuIds = new ArrayList<Long>();
  static {
    nsfcExcludeMenuIds.add(1100L);
    nsfcExcludeMenuIds.add(2L);
    nsfcExcludeMenuIds.add(3L);
    nsfcExcludeMenuIds.add(5L);
    nsfcExcludeMenuIds.add(401L);
    nsfcExcludeMenuIds.add(403L);
    nsfcExcludeMenuIds.add(404L);
    nsfcExcludeMenuIds.add(405L);
    nsfcExcludeMenuIds.add(10016L);

    nsfcExcludeMenuIds.add(4032L);
    nsfcExcludeMenuIds.add(104041L);
    nsfcExcludeMenuIds.add(104042L);
    nsfcExcludeMenuIds.add(104043L);
    nsfcExcludeMenuIds.add(4031L);
    nsfcExcludeMenuIds.add(10017L);
    nsfcExcludeMenuIds.add(100171L);
    nsfcExcludeMenuIds.add(10020L);
    nsfcExcludeMenuIds.add(6L);
    nsfcExcludeMenuIds.add(9L);
    // 过滤成果推广及其子菜单_MJG__2013-06-27-SCM-2852.
    nsfcExcludeMenuIds.add(8L);
    nsfcExcludeMenuIds.add(801L);
    nsfcExcludeMenuIds.add(802L);
    nsfcExcludeMenuIds.add(803L);
    nsfcExcludeMenuIds.add(804L);
    nsfcExcludeMenuIds.add(805L);
    // 过滤应用菜单下的“我的文件”、“找专家”、“热点领域”子菜单_MJG__2013-06-27-SCM-2852.
    nsfcExcludeMenuIds.add(10013L);
    nsfcExcludeMenuIds.add(1211L);
    nsfcExcludeMenuIds.add(521L);
    // 过滤科研之友的应用菜单_MJG_2013-07-08_SCM-2959.
    nsfcExcludeMenuIds.add(4L);
    // 过滤科研之友的文献及其子菜单_MJG_2013-07_08_SCM-2959.
    nsfcExcludeMenuIds.add(10008L);
    nsfcExcludeMenuIds.add(200L);
    nsfcExcludeMenuIds.add(201L);
    nsfcExcludeMenuIds.add(202L);
    nsfcExcludeMenuIds.add(10011L);
    nsfcExcludeMenuIds.add(10012L);

    // 过滤更多菜单及其子菜单_MJG_2013-07-08_SCM
    nsfcExcludeMenuIds.add(11L);
    nsfcExcludeMenuIds.add(110100L);
    nsfcExcludeMenuIds.add(110200L);
    nsfcExcludeMenuIds.add(110300L);
    nsfcExcludeMenuIds.add(110101L);
    nsfcExcludeMenuIds.add(110102L);
    nsfcExcludeMenuIds.add(110103L);
    nsfcExcludeMenuIds.add(110201L);
    nsfcExcludeMenuIds.add(110202L);
    // 新手指南
    nsfcExcludeMenuIds.add(1600L);
  }

  static {
    // 过滤成果在线的应用菜单及其子菜单_MJG_2013-06-27-SCM-2852，SCM-2959.
    scmExcludeMenuIds.add(12L);
    scmExcludeMenuIds.add(501L);
    scmExcludeMenuIds.add(502L);
    scmExcludeMenuIds.add(503L);
    scmExcludeMenuIds.add(504L);
    scmExcludeMenuIds.add(505L);
    scmExcludeMenuIds.add(507L);
    // 过滤成果在线-首页-智能简历菜单_MJG_2013-07-08_SCM-2959.
    scmExcludeMenuIds.add(1203L);
    // 过滤更多菜单及其子菜单_MJG_2013-07-08_SCM
    scmExcludeMenuIds.add(11L);
    scmExcludeMenuIds.add(110100L);
    scmExcludeMenuIds.add(110200L);
    scmExcludeMenuIds.add(110300L);
    scmExcludeMenuIds.add(110101L);
    scmExcludeMenuIds.add(110102L);
    scmExcludeMenuIds.add(110103L);
    scmExcludeMenuIds.add(110201L);
    scmExcludeMenuIds.add(110202L);
  }
}
