package com.smate.center.batch.constant;

/**
 * 微信发送个人信息任务 错误常量类
 * 
 * @author hzr
 *
 */
public class ErrorMsgConstant {
  /*
   * 
   * 下划线后第一位数字错误说明
   * 
   * D--Batch任务调度JobExecute处理异常 R--任务Reader处理异常 W--任务Writer处理异常 D--任务Decider处理异常
   * 
   * 
   */
  // 任务调度
  public static final String SYS_B001 = "SYS_D001, 任务调度异常";
  public static final String SYS_B002 = "SYS_D002, 调度任务数设置异常";
  public static final String SYS_B003 = "SYS_D003, 任务运行超时，请检查相关代码";
  public static final String SYS_B004 = "SYS_D004, 任务运行异常";
  public static final String SYS_B005 = "SYS_D005, 任务发起出错";

  // Reader
  public static final String SYS_R001 = "SYS_R001, 未获取到具体业务表对应的msg_id";
  public static final String SYS_R002 = "SYS_R002, 通过msg_id未获取到具体业务表对应信息";
  public static final String SYS_R003 = "SYS_R003, 检测到V_BATCH_JOBS表中重复任务";
  public static final String SYS_R004 = "SYS_R004, 获取相关信息出错";
  public static final String SYS_R005 = "SYS_R005, 必要参数为空";
  public static final String SYS_R006 = "SYS_R006, psnid为空";
  public static final String SYS_R007 = "SYS_R007, openid为空";
  public static final String SYS_R008 = "SYS_R008, token为空";
  public static final String SYS_R009 = "SYS_R009, 处理相关信息出错";
  public static final String SYS_R010 = "SYS_R010, jason解析出错";

  // Writer
  public static final String SYS_W001 = "SYS_W001, 未获取到具体业务表对应信息";
  public static final String SYS_W002 = "SYS_W002, 调用restful异常";
  public static final String SYS_W003 = "SYS_W003, 调用restful，返回值为空";
  public static final String SYS_W004 = "SYS_W004, 处理相关信息出错";
  public static final String SYS_W005 = "SYS_W005, 未获取到第三方系统服务";
  public static final String SYS_W006 = "SYS_W006, 第三方系统服务运行错误";
  public static final String SYS_W007 = "SYS_W007, 第三方系统服务返回值为空";
  public static final String SYS_W008 = "SYS_W008, 必要参数为空";
  public static final String SYS_W009 = "SYS_W009, psnid为空";
  public static final String SYS_W010 = "SYS_W010, openid为空";
  public static final String SYS_W011 = "SYS_W011, token为空";
  public static final String SYS_W012 = "SYS_W012, jason解析出错";

  // Decider
  public static final String SYS_D001 = "SYS_D001, 未获取到任务对应8位策略码";
  public static final String SYS_D002 = "SYS_D002, 任务8位策略码未识别";
}
