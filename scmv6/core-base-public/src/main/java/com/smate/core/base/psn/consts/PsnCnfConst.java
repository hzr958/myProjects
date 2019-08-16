package com.smate.core.base.psn.consts;

/**
 * 个人配置：常量
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCnfConst {

  // 用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  Integer ALLOWS_ANY = 1;
  Integer ALLOWS_FRIEND = 1 << 1;
  Integer ALLOWS_SELF = 1 << 2;

  Integer ALLOWS = ALLOWS_ANY + ALLOWS_FRIEND + ALLOWS_SELF;

  // 任务状态类别：1、成功，-1、失败，0、待运行
  Integer CNF_SUCC_STATUS = 1;
  Integer CNF_FAIL_STATUS = -1;
  Integer CNF_TORUN_STATUS = 0;

  String EDU = "edu";
  String WORK = "work";
  String PUB = "pub";
  String PRJ = "prj";
  String CONTACT = "contact";
  @Deprecated
  String PSN_CNF_TASK_RUN = "psn_cnf_task_enable";

  // 最少hindex要求
  Integer COND_MIN_HINDEX = 5;

  String CNF_CACHE_KEY = "sns_cnf_cache_key";

  // 最多缓存20分钟
  Integer CNF_CACHE_TIME_OUT = 60 * 20;
}
