package com.smate.center.task.v8pub.enums;

/**
 * 成果所属库的枚举类
 * 
 * @author houchuanjie
 */
public enum PubDbEnum {
  /**
   * 基准库
   */
  PDWH(0),
  /**
   * 个人库
   */
  SNS(1);
  private Integer value;

  PubDbEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

}
