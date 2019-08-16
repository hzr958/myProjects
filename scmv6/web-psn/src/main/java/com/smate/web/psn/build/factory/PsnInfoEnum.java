package com.smate.web.psn.build.factory;

/**
 * 人员信息构建类别枚举
 * 
 * @author zk
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public enum PsnInfoEnum {

  groupFriend(3), // 群组人员好友
  mobile(1), // 移动端人员信息显示
  personSearch(2);// 人员检索

  // 定义私有变量
  private int value;

  // 构造函数，枚举类型只能为私有
  private PsnInfoEnum(int value) {

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
