package com.smate.center.open.service.data.third.login;

/**
 * 获取动态token ，动态token使用时间限制
 * 
 * @author tsz
 *
 */
public enum DynamicTokenEnum {
  CXC_LOGIN(2 * 60 * 60), // 创新成

  GXSTI_LOGIN(2 * 60 * 60), // 广西自然基金系统

  JXSTC_LOGIN(2 * 60 * 60), // 江西科技业务系统

  ECNU_LOGIN(2 * 60 * 60), // 华东师范大学

  MOBILE_RELATION(1 * 60 * 60), // 移动端的关联
  TEST_RELATION(60), // 测试专用-移动端的关联

  NORMAL_LOGIN(2 * 60 * 60), // 普通登陆用 (可以公用)
  GZWS_LOGIN(2 * 60 * 60); // 广州市卫生和计划生育委员会科教业务综合服务平台

  // 定义私有变量
  private Integer module;

  // 构造函数，枚举类型只能为私有
  private DynamicTokenEnum(Integer module) {
    this.module = module;
  }

  public Integer toInt() { // 定义一个实例成员函数
    return module;
  }
}
