package com.smate.web.v8pub.enums;

/**
 * 成果权限枚举
 * 
 * @author yewenlong
 * @date 2019年4月16日
 */
public enum PubConfigEnum implements IBaseEnum<Integer> {

  // 0=没有赞
  PUB_SLEF(4, "隐私"),
  // 1=已经赞
  PUB_OPEN(7, "公开");

  private Integer value;
  private String description;

  private PubConfigEnum(Integer val, String desc) {
    this.value = val;
    this.description = desc;
  }

  @Override
  public Integer getValue() {
    return this.value;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  public static PubConfigEnum valueOf(Integer val) {
    if (val == null) {
      throw new NullPointerException();
    }
    if (val.intValue() == 4) {
      return PUB_SLEF;
    } else if (val.intValue() == 7) {
      return PUB_OPEN;
    } else {
      throw new IllegalArgumentException("试图将整数值：" + val.intValue() + "转换为PubConfigEnum类型时失败！原因：没有对应该值的枚举类型！");
    }
  }
}
