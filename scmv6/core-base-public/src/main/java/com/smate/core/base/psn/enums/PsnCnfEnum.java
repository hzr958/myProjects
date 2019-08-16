package com.smate.core.base.psn.enums;



public enum PsnCnfEnum {
  // 注意：必须status=0时，runs才有效。
  // 执行类别（数字表示二进制位，待执行类别）：
  // 1(1)更新数据，2(1<<1)、调整模块顺序，2(1<<2)、移除过时模块，3(1<<3)、清空配置（慎用，不可恢复），4(1<<4)、旧配置转换，6保留位，7项目，8成果，9工作经历，10教育经历，11学科领域，12所教课程，13个人简介，14联系方式。如：仅调整模块顺序，则值为1；补全项目和成果，则值为1<<6
  // & 1<<7(项目和成果的位运算)；其他情况根据位的组合运算，得到相应的值；值为0时，什么效果都没有，相当于空跑
  // 利用构造函数传参

  DIRTY(1), MOVE(1 << 1), DELMOD(1 << 2), CLEAN(1 << 3), TMP235(1 << 4), PRJ(1 << 6), PUB(1 << 7), WORK(1 << 8), EDU(
      1 << 9), EXPERTISE(1 << 10), TAUGHT(1 << 11), BRIEF(1 << 12), CONTACT(1 << 13), POSITION(1 << 14), SSI(
          1 << 15), HINDEX(1 << 16), ALL(
              1 << 6 | 1 << 7 | 1 << 8 | 1 << 9 | 1 << 10 | 1 << 11 | 1 << 12 | 1 << 13 | 1 << 14 | 1 << 15 | 1 << 16);

  // 定义私有变量
  private int run;

  // 构造函数，枚举类型只能为私有
  private PsnCnfEnum(int run) {

    this.run = run;

  }

  @Override
  public String toString() {

    return String.valueOf(this.run);

  }
}
