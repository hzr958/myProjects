package com.smate.core.base.psn.service.resume;



/**
 * 当用户未设置公开信息时，根据不同用户，初始化不同的公开信息.
 * 
 * @author liqinghua
 * 
 */
@Deprecated
public interface ResumeInitProcess {
  // 科研之友用户
  Long USER = 2L;

  // 好友
  Long FRIEND = 3L;

  // 自己查看
  Long SELF = 5L;

  // 配置信息显示
  Integer SHOW = 1;
  // 不显示
  Integer UNSHOW = 0;

  // 显示全部
  Integer ALL = 1;
  // 不显示全部
  Integer UNALL = 0;

  // 我的主页显示顺序
  Integer SEQ1 = 1;
  Integer SEQ2 = 2;
  Integer SEQ3 = 3;
  Integer SEQ4 = 4;
  Integer SEQ5 = 5;
  Integer SEQ6 = 6;
  Integer SEQ7 = 7;
  Integer SEQ8 = 8;
  Integer SEQ9 = 9;
  // 需要修复
  Integer TO_FIXED = 1;

  // 已经修复
  Integer FIXED = 0;


}
