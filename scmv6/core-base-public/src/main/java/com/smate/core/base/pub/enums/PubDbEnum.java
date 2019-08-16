package com.smate.core.base.pub.enums;

import com.smate.core.base.utils.number.NumberUtils;

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

  public static PubDbEnum getPubDbEnumByValue(Integer value) {
    if (NumberUtils.isNotNullOrZero(value)) {
      PubDbEnum[] values = values();
      for (PubDbEnum pubDbEnum : values) {
        if (pubDbEnum.getValue() == value) {
          return pubDbEnum;
        }
      }
    }
    return null;
  }

}
