package com.smate.center.task.v8pub.enums;

/**
 * 成果处理结果枚举
 * 
 * @author tsz
 *
 * @date 2018年6月7日
 */
public enum PubHandlerStatusEnum {
  SUCCESS("SUCCESS"), ERROR("ERROR");

  private String value;

  private PubHandlerStatusEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
