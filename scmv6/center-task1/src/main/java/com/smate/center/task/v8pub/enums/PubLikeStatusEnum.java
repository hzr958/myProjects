package com.smate.center.task.v8pub.enums;

import com.smate.core.base.enums.IBaseEnum;

/**
 * 成果赞状态
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public enum PubLikeStatusEnum implements IBaseEnum<Integer> {

  // 0=没有赞
  PUB_LIKE(1, "赞"),
  // 1=已经赞
  PUB_UNLIKE(0, "取消赞");

  private Integer value;
  private String description;

  private PubLikeStatusEnum(Integer val, String desc) {
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

  public static PubLikeStatusEnum valueOf(Integer val) {
    if (val == null) {
      throw new NullPointerException();
    }
    if (val.intValue() == 1) {
      return PUB_LIKE;
    } else if (val.intValue() == 0) {
      return PUB_UNLIKE;
    } else {
      throw new IllegalArgumentException("试图将整数值：" + val.intValue() + "转换为LikeStatusEnum类型时失败！原因：没有对应该值的枚举类型！");
    }
  }
}
