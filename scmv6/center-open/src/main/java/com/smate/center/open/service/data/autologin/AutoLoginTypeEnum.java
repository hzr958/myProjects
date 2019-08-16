package com.smate.center.open.service.data.autologin;

/**
 * 自动登陆 类型于 过期时间枚举
 * 
 * @author tsz
 *
 */
public enum AutoLoginTypeEnum {
  WCR(2 * 60 * 1000L), // 微信 注册后自动登陆

  Interconnection(2 * 60 * 1000L), // 互联互通 登陆链接到科研之友

  YJS(2 * 60 * 1000L), // 云计算 登陆链接到科研之友

  ScmRegistered(2 * 60 * 1000L), // 科研之友登录成功自动跳转页面

  Temporary(1 * 60 * 1000L), // 临时使用

  SNSRememberMe(12 * 30 * 24 * 60 * 60 * 1000L), // 记住登录

  BPOLogin(10 * 60 * 1000L), // BPO代登录

  ThirdSysLoginFrame(5 * 60 * 1000L), // 第三方业务系统登录---弹出框中的

  ThirdSysLoginPage(30 * 60 * 1000L), // 第三方业务系统登录----正常页面中的

  ResetPWD(30 * 24 * 60 * 60 * 1000L), // 邮件链接里面自动登录并弹出重置密码框用

  TestRememberMe(2 * 60 * 60 * 1000L); // 测试记住登录-------只能测试用，正常逻辑勿用
  // 定义私有变量
  private long module;

  // 构造函数，枚举类型只能为私有
  private AutoLoginTypeEnum(long module) {
    this.module = module;
  }

  public long toLong() { // 定义一个实例成员函数
    return module;
  }
}
