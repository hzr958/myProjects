package com.smate.web.prj.enums;

/**
 * 项目信息查询枚举类
 * 
 * @author zk
 *
 */
public enum PrjInfoQueryEnum {

  WECHAT(2);// 微信项目信息

  // 定义私有变量
  private int value;

  // 构造函数，枚举类型只能为私有
  private PrjInfoQueryEnum(int value) {

    this.value = value;

  }

  @Override
  public String toString() {

    return this.value + "";

  }

  public Integer toInt() {

    return Integer.valueOf(this.value);

  }
}
