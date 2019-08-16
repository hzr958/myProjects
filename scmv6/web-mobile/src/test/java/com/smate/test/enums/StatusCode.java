package com.smate.test.enums;

import java.util.Objects;

public enum StatusCode {
  OK(1, "200"), PARAMERROR(2, "400"), SERVERERROR(3, "500");

  private Integer num;// 序号
  private String code;// 编码

  private StatusCode(Integer num, String code) {
    this.num = num;
    this.code = code;
  }

  public void setNum(Integer num) {
    this.num = num;
  }

  public Integer getNum() {
    return this.num;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }

  /**
   * 通过序号获取编码
   * 
   * @param num
   * @return
   */
  public static String getCodeByNum(Integer num) {
    if (Objects.isNull(num)) {
      return "";
    }
    StatusCode[] values = values();
    for (StatusCode sc : values) {
      if (num.intValue() == sc.getNum().intValue()) {
        return sc.getCode();
      }
    }
    return "不存在此编码";
  }
}
