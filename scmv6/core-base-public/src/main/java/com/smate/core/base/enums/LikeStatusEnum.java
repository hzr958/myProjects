package com.smate.core.base.enums;

/**
 * 点赞状态枚举类
 * 
 * @author ChuanjieHou
 * @date 2017年9月26日
 */
public enum LikeStatusEnum implements IBaseEnum<Integer> {


  /**
   * 没有赞 0
   */
  UNLIKE(0, "取消赞"),
  /**
   * 已赞 1
   */
  LIKE(1, "赞");


  private Integer value;
  private String description;

  private LikeStatusEnum(Integer val, String desc) {
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

  public static LikeStatusEnum valueOf(Integer val) {
    if (val == null) {
      throw new NullPointerException();
    }
    if (val.intValue() == 1) {
      return LIKE;
    } else if (val.intValue() == 0) {
      return UNLIKE;
    } else {
      throw new IllegalArgumentException("试图将整数值：" + val.intValue() + "转换为LikeStatusEnum类型时失败！原因：没有对应该值的枚举类型！");
    }
  }

}
